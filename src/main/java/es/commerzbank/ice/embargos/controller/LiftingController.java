package es.commerzbank.ice.embargos.controller;

import java.util.List;
import java.util.Map;

import es.commerzbank.ice.comun.lib.domain.dto.AccountingNote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
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
import es.commerzbank.ice.embargos.service.AccountingService;
import es.commerzbank.ice.embargos.service.LiftingService;
import es.commerzbank.ice.utils.DownloadReportFile;
import io.swagger.annotations.ApiOperation;


@CrossOrigin("*")
@RestController
@RequestMapping(value = "/lifting")
public class LiftingController {

	private static final Logger logger = LoggerFactory.getLogger(LiftingController.class);
	
	@Autowired
	private LiftingService liftingService;
	
	@Autowired
	private AccountingService accountingService;

	@GetMapping(value = "/{codeFileControl}")
	@ApiOperation(value = "Devuelve la lista de casos de levamtamientos")
	public ResponseEntity<List<LiftingDTO>> getLiftingListByCodeFileControl(Authentication authentication,
			@PathVariable("codeFileControl") Long codeFileControl) {
		logger.info("LiftingController - getLiftingListByCodeFileControl - start");
		ResponseEntity<List<LiftingDTO>> response = null;
		List<LiftingDTO> result = null;

		try {

			ControlFichero controlFichero = new ControlFichero();
			controlFichero.setCodControlFichero(codeFileControl);

			result = liftingService.getAllByControlFichero(controlFichero);

			response = new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {

			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

			logger.error("ERROR in getLiftingListByCodeFileControl: ", e);
		}

		logger.info("LiftingController - getLiftingListByCodeFileControl - end");
		return response;
	}
	
	@GetMapping(value = "/{codeFileControl}/liftingcase/{codeLifting}/bankAccounts")
	@ApiOperation(value = "Devuelve la lista de cuentas disponibles para el caso indicado de levantamiento.")
	public ResponseEntity<LiftingDTO> getBankAccountListByCodeFileControlAndLifting(
			Authentication authentication, @PathVariable("codeFileControl") Long codeFileControl,
			@PathVariable("codeLifting") Long codeLifting) {
		logger.info("LiftingController - getBankAccountListByCodeFileControlAndLifting - start");
		ResponseEntity<LiftingDTO> response = null;
		LiftingDTO result = null;

		try {

			result = liftingService.getAllByControlFicheroAndLevantamiento(codeFileControl,
					codeLifting);

			response = new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {

			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

			logger.error("ERROR in getBankAccountListByCodeFileControlAndLifting: ", e);
		}

		logger.info("LiftingController - getBankAccountListByCodeFileControlAndLifting - end");
		return response;
	}
	
	@PostMapping(value = "/{codeFileControl}/liftingcase/{codeLifting}", 
			produces = {"application/json" }, 
			consumes = { "application/json" })
	@ApiOperation(value = "Actualiza el caso de levantamiento indicado, guardando los datos que se traspasen")
	public ResponseEntity<String> saveLifting(Authentication authentication,
			@PathVariable("codeFileControl") Long codeFileControl,
			@PathVariable("codeLifting") Long codeLifting, @RequestBody LiftingDTO lifting) {
		logger.info("LiftingController - saveLifting - start");
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
		
			logger.error("ERROR in saveLifting: ", e);
		}
		
		logger.info("LiftingController - saveLifting - end");
		return response;

	}
	
	@GetMapping(value = "/{codeFileControl}/liftingcase/{codeLiftingCase}/audit")
	public ResponseEntity<List<LiftingAuditDTO>> getAuditLiftingCase(Authentication authentication,
			@PathVariable("codeLiftingCase") Long codeLiftingCase) {
		logger.info("LiftingController - getAuditPetitionCase - start");
		ResponseEntity<List<LiftingAuditDTO>> response = null;
		List<LiftingAuditDTO> result = null;

		try {

			//result = liftingService.getAuditByCodeLiftingCase(codeLiftingCase);

			response = new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {

			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

			logger.error("ERROR in getAuditLiftingCase: ", e);
		}

		logger.info("LiftingController - getAuditPetitionCase - end");
		return response;

	}
	
	@GetMapping(value = "/liftingStatus")
	public ResponseEntity<List<LiftingStatusDTO>> getListStatus(Authentication authentication) {
		logger.info("LiftingController - getListStatus - start");
		ResponseEntity<List<LiftingStatusDTO>> response = null;
		List<LiftingStatusDTO> result = null;

		try {

			result = liftingService.getListStatus();

			response = new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {

			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

			logger.error("ERROR in getListStatus: ", e);
		}

		logger.info("LiftingController - getListStatus - end");
		return response;

	}
	
	@PostMapping(value = "/{codeFileControl}/liftingcase/{codeLifting}/changeStatus/{status}", 
			produces = {"application/json" }, 
			consumes = { "application/json" })
	@ApiOperation(value = "Actualiza el estado de un levantamiento seleccionado")
	public ResponseEntity<Void> changeStatus(Authentication authentication,
			@PathVariable("codeFileControl") Long codeFileControl,
			@PathVariable("codeLifting") Long codeLifting, @PathVariable("status") Long status) {
		logger.info("LiftingController - changeStatus - start");
		ResponseEntity<Void> response = null;
		boolean result = true;
		
		try {
		
			String userModif = authentication.getName();
		
			result = liftingService.changeStatus(codeLifting, status, userModif);
		
			if (result) {
				response = new ResponseEntity<>(HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		
		} catch (Exception e) {
		
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
			logger.error("ERROR in changeStatus: ", e);
		}
		
		logger.info("LiftingController - changeStatus - end");
		return response;
	}
	
	@GetMapping(value = "/{codeFileControl}/accounting")
    @ApiOperation(value="Envio de datos a contabilidad.")
    public ResponseEntity<String> sendAccounting(Authentication authentication,
    										  @PathVariable("codeFileControl") Long codeFileControl){
    	logger.info("SeizureController - sendAccounting - start");
    	ResponseEntity<String> response = null;
		boolean result = false;

		try {

			String userName = authentication.getName();
		
			result = accountingService.sendAccountingLifting(codeFileControl, userName);
			
			
			if (result) {
				response = new ResponseEntity<>(HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {

			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

			logger.error("ERROR in doAccounting: ", e);
		}

		logger.info("SeizureController - sendAccounting - end");
		return response;

	}

	@GetMapping("/{fileControl}/report")
	@ApiOperation(value = "Devuelve el fichero de resumen de levantamiento FASE 5")
	public ResponseEntity<InputStreamResource> generarResumenLevantamientoF5(
			@PathVariable("fileControl") Integer codFileControl) {

		DownloadReportFile.setTempFileName("f5-seizure-lifting");

		DownloadReportFile.setFileTempPath(pdfSavedPath);

		try {

			DownloadReportFile.writeFile(liftingService.generarResumenLevantamientoF5(codFileControl));

			return DownloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in anexoReport", e);

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/notification/{idLifting}/report")
	@ApiOperation(value = "Devuelve un levantamiento de embargo")
	public ResponseEntity<InputStreamResource> generateLiftingLetter(
			@PathVariable("idLifting") Integer idLifting) {
		logger.info("SeizureController - generateLiftingLetter - start");

		DownloadReportFile.setTempFileName("lifting-letter");

		DownloadReportFile.setFileTempPath(pdfSavedPath);

		try {

			// seizure service falta
			DownloadReportFile.writeFile(liftingService.generateLiftingLetter(idLifting));

			logger.info("SeizureController - generateLiftingLetter - end");
			return DownloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in levantamientoReport", e);
			System.out.println(e);

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

    }

	@PostMapping(value = "/accountingNote")
	@ApiOperation(value="Tratamiento de la respuesta de Contabilidad (nota contable).")
	public ResponseEntity<String> manageAccountingNoteLiftingCallback(Authentication authentication,
																	  @RequestBody AccountingNote accountingNote){
		logger.info("SeizureController - manageAccountingNoteLiftingCallback - start");
		ResponseEntity<String> response = null;
		boolean result = false;

		try {

			String userName = authentication.getName();

			result = accountingService.manageAccountingNoteLiftingCallback(accountingNote, userName);


			if (result) {
				response = new ResponseEntity<>(HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {

			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

			logger.error("ERROR in doAccounting: ", e);
		}

		logger.info("SeizureController - manageAccountingNoteLiftingCallback - end");
		return response;

	}
}
