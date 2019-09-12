package es.commerzbank.ice.embargos.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.commerzbank.ice.embargos.domain.dto.BankAccountDTO;
import es.commerzbank.ice.embargos.domain.dto.BankAccountLiftingDTO;
import es.commerzbank.ice.embargos.domain.dto.LiftingAuditDTO;
import es.commerzbank.ice.embargos.domain.dto.LiftingDTO;
import es.commerzbank.ice.embargos.domain.dto.LiftingStatusDTO;
import es.commerzbank.ice.embargos.domain.dto.PetitionCaseDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.service.LiftingService;
import io.swagger.annotations.ApiOperation;


@CrossOrigin("*")
@RestController
@RequestMapping(value = "/lifting")
public class LiftingController {

	private static final Logger LOG = LoggerFactory.getLogger(LiftingController.class);
	
	@Autowired
	private LiftingService liftingService;

	
	@GetMapping(value = "/{codeFileControl}")
	@ApiOperation(value = "Devuelve la lista de casos de levamtamientos")
	public ResponseEntity<List<LiftingDTO>> getLiftingListByCodeFileControl(Authentication authentication,
			@PathVariable("codeFileControl") Long codeFileControl) {
		ResponseEntity<List<LiftingDTO>> response = null;
		List<LiftingDTO> result = null;

		try {

			ControlFichero controlFichero = new ControlFichero();
			controlFichero.setCodControlFichero(codeFileControl);

			result = liftingService.getAllByControlFichero(controlFichero);

			response = new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {

			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

			LOG.error("ERROR in getPetitionCaseListByCodeFileControl: ", e);
		}

		return response;
	}
	
	@GetMapping(value = "/{codeFileControl}/liftingcase/{codeLifting}/bankAccounts")
	@ApiOperation(value = "Devuelve la lista de cuentas disponibles para el caso indicado de levantamiento.")
	public ResponseEntity<LiftingDTO> getBankAccountListByCodeFileControlAndLifting(
			Authentication authentication, @PathVariable("codeFileControl") Long codeFileControl,
			@PathVariable("codeLifting") Long codeLifting) {
		ResponseEntity<LiftingDTO> response = null;
		LiftingDTO result = null;

		try {

			result = liftingService.getAllByControlFicheroAndLevantamiento(codeFileControl,
					codeLifting);

			response = new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {

			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

			LOG.error("ERROR in getBankAccountListByCodeFileControlAndPetitionCase: ", e);
		}

		return response;
	}
	
	@PostMapping(value = "/{codeFileControl}/liftingcase/{codeLifting}", 
			produces = {"application/json" }, 
			consumes = { "application/json" })
	@ApiOperation(value = "Actualiza el caso de levantamiento indicado, guardando los datos que se traspasen")
	public ResponseEntity<String> saveLifting(Authentication authentication,
			@PathVariable("codeFileControl") Long codeFileControl,
			@PathVariable("codeLifting") Long codeLifting, @RequestBody LiftingDTO lifting) {
		
		ResponseEntity<String> response = null;
		boolean result = true;
		
		try {
		
			String userModif = authentication.getName();
		
			result = liftingService.saveLifting(codeFileControl, codeLifting, lifting, userModif);
		
			if (result) {
				response = new ResponseEntity<>(HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		
		} catch (Exception e) {
		
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
			LOG.error("ERROR in savePetitionCase: ", e);
		}
		
		return response;

	}
	
	@GetMapping(value = "/{codeFileControl}/liftingcase/{codeLiftingCase}/audit")
	public ResponseEntity<List<LiftingAuditDTO>> getAuditPetitionCase(Authentication authentication,
			@PathVariable("codeLiftingCase") Long codeLiftingCase) {

		ResponseEntity<List<LiftingAuditDTO>> response = null;
		List<LiftingAuditDTO> result = null;

		try {

			//result = liftingService.getAuditByCodeLiftingCase(codeLiftingCase);

			response = new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {

			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

			LOG.error("ERROR in getAuditPetitionCase: ", e);
		}

		return response;

	}
	
	@GetMapping(value = "/liftingStatus")
	public ResponseEntity<List<LiftingStatusDTO>> getListStatus(Authentication authentication) {

		ResponseEntity<List<LiftingStatusDTO>> response = null;
		List<LiftingStatusDTO> result = null;

		try {

			result = liftingService.getListStatus();

			response = new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {

			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

			LOG.error("ERROR in getAuditPetitionCase: ", e);
		}

		return response;

	}
}
