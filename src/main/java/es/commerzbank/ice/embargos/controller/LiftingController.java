package es.commerzbank.ice.embargos.controller;

import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.embargos.domain.dto.*;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.service.AccountingService;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.LiftingService;
import es.commerzbank.ice.embargos.utils.DownloadReportFile;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin("*")
@RestController
@RequestMapping(value = "/lifting")
public class LiftingController {

	private static final Logger logger = LoggerFactory.getLogger(LiftingController.class);
	
	@Autowired
	private LiftingService liftingService;
	
	@Autowired
	private AccountingService accountingService;

	@Autowired
	private GeneralParametersService generalParametersService;

	@Autowired
	private FileControlService fileControlService;

	@GetMapping(value = "/{codeFileControl}")
	@ApiOperation(value = "Devuelve la lista de casos de levamtamientos")
	public ResponseEntity<List<LiftingDTO>> getLiftingListByCodeFileControl(Authentication authentication,
			@PathVariable("codeFileControl") Long codeFileControl)
	{
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

		return response;
	}
	
	@GetMapping(value = "/{codeFileControl}/liftingcase/{codeLifting}/bankAccounts")
	@ApiOperation(value = "Devuelve la lista de cuentas disponibles para el caso indicado de levantamiento.")
	public ResponseEntity<LiftingDTO> getBankAccountListByCodeFileControlAndLifting(
			Authentication authentication, @PathVariable("codeFileControl") Long codeFileControl,
			@PathVariable("codeLifting") Long codeLifting)
	{
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

		return response;
	}
	
	@PostMapping(value = "/{codeFileControl}/liftingcase/{codeLifting}", 
			produces = {"application/json" }, 
			consumes = { "application/json" })
	@ApiOperation(value = "Actualiza el caso de levantamiento indicado, guardando los datos que se traspasen")
	public ResponseEntity<String> saveLifting(Authentication authentication,
			@PathVariable("codeFileControl") Long codeFileControl,
			@PathVariable("codeLifting") Long codeLifting, @RequestBody LiftingDTO lifting)
	{
		logger.info("LiftingController - "+ codeFileControl +"-"+ codeLifting +" actualización");

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

		return response;

	}
	
	@GetMapping(value = "/{codeFileControl}/liftingcase/{codeLiftingCase}/audit")
	public ResponseEntity<List<LiftingAuditDTO>> getAuditLiftingCase(Authentication authentication,
			@PathVariable("codeLiftingCase") Long codeLiftingCase)
	{
		ResponseEntity<List<LiftingAuditDTO>> response = null;
		List<LiftingAuditDTO> result = null;

		try {

			//result = liftingService.getAuditByCodeLiftingCase(codeLiftingCase);

			response = new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {

			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

			logger.error("ERROR in getAuditLiftingCase: ", e);
		}

		return response;

	}
	
	@GetMapping(value = "/liftingStatus")
	public ResponseEntity<List<LiftingStatusDTO>> getListStatus(Authentication authentication)
	{
		ResponseEntity<List<LiftingStatusDTO>> response = null;
		List<LiftingStatusDTO> result = null;

		try {

			result = liftingService.getListStatus();

			response = new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {

			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

			logger.error("ERROR in getListStatus: ", e);
		}

		return response;

	}
	
	@PostMapping(value = "/{codeFileControl}/liftingcase/{codeLifting}/changeStatus/{status}", 
			produces = {"application/json" }, 
			consumes = { "application/json" })
	@ApiOperation(value = "Actualiza el estado de un levantamiento seleccionado")
	public ResponseEntity<Void> changeStatus(Authentication authentication,
			@PathVariable("codeFileControl") Long codeFileControl,
			@PathVariable("codeLifting") Long codeLifting, @PathVariable("status") Long status)
	{
		logger.info("LiftingController - "+ codeFileControl +"-"+ codeLifting +" cambio de estado "+ status);

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

		return response;
	}
	
	@PostMapping(value = "/{codeFileControl}/liftingcase/{codeLifting}/{idAccount}/status")
	@ApiOperation(value="Guarda una actualizacion de estado para el caso indicado")
	public ResponseEntity<String> updateLiftingAccountStatus(Authentication authentication,
													  @PathVariable("codeFileControl") Long codeFileControl,
													  @PathVariable("codeLifting") Long codeLifting,
													  @PathVariable("idAccount") Long idAccount,
													  @RequestBody AccountStatusLiftingDTO accountStatusLifting)
	{
		logger.info("SeizureController - Actualizando levantamiento "+ codeFileControl +"-"+ codeLifting +" cambiando de estado la cuenta "+ idAccount);
		ResponseEntity<String> response = null;
		boolean result = false;

		try {

			String userModif = authentication.getName();

			result = liftingService.updateAccountLiftingStatus(idAccount, codeLifting, accountStatusLifting, userModif);

			if (result) {
				response = new ResponseEntity<>(HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {

			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

			logger.error("ERROR in updateSeizureAccountStatus: ", e);
		}

		return response;
	}
	
	@GetMapping(value = "/{codeFileControl}/accounting")
    @ApiOperation(value="Envio de datos a contabilidad.")
    public ResponseEntity<FileControlDTO> sendAccounting(Authentication authentication,
														 @PathVariable("codeFileControl") Long codeFileControl)
	{
    	logger.info("SeizureController - se contabilizan los levantamientos de "+ codeFileControl);

    	ResponseEntity<FileControlDTO> response = null;
		boolean result = false;

		FileControlDTO resultFileControlDTO = null;

		try {

			String userName = authentication.getName();
		
			accountingService.sendLifting(codeFileControl, userName);

			//Se obtiene el fileControl que se va a retornar del Fichero Final:
			resultFileControlDTO = fileControlService.getByCodeFileControl(codeFileControl);

			response = new ResponseEntity<>(resultFileControlDTO,HttpStatus.OK);
		} catch (Exception e) {

			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

			logger.error("ERROR in doAccounting: ", e);
		}

		return response;

	}

	@GetMapping("/{fileControl}/report")
	@ApiOperation(value = "Devuelve el fichero de resumen de levantamiento FASE 5")
	public ResponseEntity<InputStreamResource> generarResumenLevantamientoF5(
			@PathVariable("fileControl") Integer codFileControl) {
		try {
			DownloadReportFile downloadReportFile = new DownloadReportFile();
			
			downloadReportFile.setTempFileName("f5-seizure-lifting");

			downloadReportFile.setFileTempPath(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_JASPER_TEMP));

			downloadReportFile.writeFile(liftingService.generarResumenLevantamientoF5(codFileControl));

			return downloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in anexoReport", e);

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/notification/{idLifting}/report")
	@ApiOperation(value = "Devuelve un levantamiento de embargo")
	public ResponseEntity<InputStreamResource> generateLiftingLetter(
			@PathVariable("idLifting") Long idLifting) {

		try {
			DownloadReportFile downloadReportFile = new DownloadReportFile();
			
			downloadReportFile.setTempFileName("lifting-letter");

			downloadReportFile.setFileTempPath(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_JASPER_TEMP));

			// seizure service falta
			downloadReportFile.writeFile(liftingService.generateLiftingLetter(idLifting));

			return downloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in levantamientoReport", e);
			System.out.println(e);

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "/manual")
    @ApiOperation(value="Realiza un levantamiento de forma manual.")
    public ResponseEntity<String> manualLifting(Authentication authentication,
    											@RequestBody LiftingManualDTO liftingManualDTO) {
    	logger.info("LiftingController - levantamiento manual "+ liftingManualDTO.getSender() +" "+ liftingManualDTO.getClients().size());
		ResponseEntity<String> response = null;
		boolean result = false;

		try {
			String userModif = authentication.getName();

			result = liftingService.manualLifting(liftingManualDTO, userModif);

			if (result) {
				response = new ResponseEntity<>(HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			logger.error("ERROR in manualLifting: ", e);
		}

		return response;
    }

}
