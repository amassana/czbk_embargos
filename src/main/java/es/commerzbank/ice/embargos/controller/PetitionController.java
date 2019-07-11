package es.commerzbank.ice.embargos.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.commerzbank.ice.embargos.domain.dto.BankAccountDTO;
import es.commerzbank.ice.embargos.domain.dto.PetitionCaseDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.service.InformationPetitionBankAccountService;
import es.commerzbank.ice.embargos.service.InformationPetitionService;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/petition")
public class PetitionController {

	private static final Logger LOG = LoggerFactory.getLogger(PetitionController.class);
	
	@Autowired
	private InformationPetitionService informationPetitionService;

	@Autowired
	private InformationPetitionBankAccountService informationPetitionBankAccountService;
	
	@GetMapping(value = "/{codeFileControl}")
	public ResponseEntity<List<PetitionCaseDTO>> getPetitionCaseListByCodeFileControl(@RequestHeader(value="token",required=true) String token,
			  												   @PathVariable("codeFileControl") Long codeFileControl){
		ResponseEntity<List<PetitionCaseDTO>> response = null;
		List<PetitionCaseDTO> result = null;

		try {
						
			ControlFichero controlFichero = new ControlFichero();
			controlFichero.setCodControlFichero(codeFileControl);
			
			result = informationPetitionService.getAllByControlFichero(controlFichero);
					
			response = new ResponseEntity<>(result, HttpStatus.OK);
		
		} catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			LOG.error("ERROR: ".concat(e.getLocalizedMessage()));
		}
			
		return response;
	}
	
	@GetMapping(value = "/{codeFileControl}/petitioncase/{codePetitionCase}/bankAccounts")
	public ResponseEntity<List<BankAccountDTO>> getBankAccountistByCodeFileControlAndPetitionCase(@RequestHeader(value="token",required=true) String token,
			  												   @PathVariable("codeFileControl") Long codeFileControl,
			  												   @PathVariable("codePetitionCase") String codePetitionCase){
		ResponseEntity<List<BankAccountDTO>> response = null;
		List<BankAccountDTO> result = null;

		try {
			
			result = informationPetitionBankAccountService.getAllByControlFicheroAndPeticionInformacion(codeFileControl,
					codePetitionCase);
					
			response = new ResponseEntity<>(result, HttpStatus.OK);
		
		} catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			LOG.error("ERROR: ".concat(e.getLocalizedMessage()));
		}
			
		return response;
	}
	
	@PatchMapping(value = "/{codeFileControl}/petitioncase/{codePetitionCase}",
			produces = { "application/json" }, 
	        consumes = { "application/json" })
	public ResponseEntity<String> savePetitionCase(@RequestHeader(value="token",required=true) String token,
			@PathVariable("codeFileControl") Long codeFileControl,
			@PathVariable("codePetitionCase") String codePetitionCase,
			@RequestBody PetitionCaseDTO petitionCase){
		
		
		ResponseEntity<String> response = null;
		boolean result = true;
		
		try {
			
			//TODO falta implementar el result:
			result = informationPetitionService.savePetitionCase(codeFileControl,codePetitionCase,petitionCase);
			
			if (result) {
				response = new ResponseEntity<>(HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}	
		
		} catch (Exception e) {
			
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
			LOG.error("ERROR: ".concat(e.getLocalizedMessage()));
		}
		
		return response;
		
	}
	
	@GetMapping(value = "/{codeFileControl}/petitioncase/{codePetitionCase}/audit",
			produces = { "application/json" }, 
	        consumes = { "application/json" })
	public ResponseEntity<List<PetitionCaseDTO>> getAuditPetitionCase(@RequestHeader(value="token",required=true) String token,
			@PathVariable("codeFileControl") Long codeFileControl,
			@PathVariable("codePetitionCase") String codePetitionCase){
		
		
		ResponseEntity<List<PetitionCaseDTO>> response = null;
		List<PetitionCaseDTO> result = null;
		
		try {
			
			//TODO implementar:
			result = null;
					
			response = new ResponseEntity<>(result, HttpStatus.OK);
		
		} catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			LOG.error("ERROR: ".concat(e.getLocalizedMessage()));
		}
		
		return response;
		
	}
	
	////////////// REVISAR:
	
//	@GetMapping(value = "/filecontrol/{codeFileControl}")
//	public ResponseEntity<PetitionDTO> getByCodeFileControl(@RequestHeader(value="token",required=true) String token,
//			  												   @PathVariable("codeFileControl") Long codeFileControl){
//		ResponseEntity<PetitionDTO> response = null;
//		PetitionDTO result = null;
//
//		try {
//		
//			result = petitionService.getByCodeFileControl(codeFileControl);
//
//			response = new ResponseEntity<>(result, HttpStatus.OK);
//			
//		} catch (Exception e) {
//			
//			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
//			
//			//LOG.error("ERROR: ".concat(e.getLocalizedMessage()));
//		}
//		
//		return response;
//	}
	
	

	
}
