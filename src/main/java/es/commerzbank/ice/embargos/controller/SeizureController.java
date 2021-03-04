package es.commerzbank.ice.embargos.controller;

import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.domain.dto.*;
import es.commerzbank.ice.embargos.service.AccountingService;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.SeizureService;
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

import java.util.ArrayList;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/seizure")
public class SeizureController {

	private static final Logger logger = LoggerFactory.getLogger(SeizureController.class);

	@Autowired
	private SeizureService seizureService;
	
	@Autowired
	private AccountingService accountingService;
	
	@Autowired
	private GeneralParametersService generalParametersService;
	
	@Autowired
	private FileControlService fileControlService;

    @GetMapping(value = "/{codeFileControl}")
    @ApiOperation(value="Devuelve la lista de embargos para una petición de embargo.")
    public ResponseEntity<List<SeizureDTO>> getSeizureListByCodeFileControl(Authentication authentication,
                                                                                 @PathVariable("codeFileControl") Long codeFileControl){
		logger.info("SeizureController - getSeizureListByCodeFileControl - start");
    	ResponseEntity<List<SeizureDTO>> response = null;
    	List<SeizureDTO> result = new ArrayList<SeizureDTO>();
    	
    	try {
    		
			result = seizureService.getSeizureListByCodeFileControl(codeFileControl);
			response = new ResponseEntity<>(result, HttpStatus.OK);
    	
	    } catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			logger.error("ERROR in getSeizureListByCodeFileControl: ", e);
		}	
		
    	logger.info("SeizureController - getSeizureListByCodeFileControl - end");
		return response;

    }
    
    @GetMapping(value = "/{codeFileControl}/case/{idSeizure}")
    @ApiOperation(value="Devuelve el embargo a partir de su identificador.")
    public ResponseEntity<SeizureDTO> getSeizureById(Authentication authentication, @PathVariable("idSeizure") Long idSeizure){
    	logger.info("SeizureController - getSeizureById - start");
    	ResponseEntity<SeizureDTO> response = null;
    	SeizureDTO result = null;
    	
    	try {
    		  
    		//TODO: Revisar: idSeizure en este caso esta aplicando por codEmbargo. ¿tiene que ser por codTraba?
    		
			result = seizureService.getSeizureById(idSeizure);
			response = new ResponseEntity<>(result, HttpStatus.OK);
    	
	    } catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			logger.error("ERROR in getSeizureListByCodeFileControl: ", e);
		}	
		
    	logger.info("SeizureController - getSeizureById - end");
		return response;
    	
    }

    @GetMapping(value = "/{codeFileControl}/case/{idSeizure}/accounts")
    @ApiOperation(value="Devuelve el listado de cuentas bancarias asociadas a la traba.")
    public ResponseEntity<List<SeizedBankAccountDTO>> getBankAccountListBySeizure(Authentication authentication,
                                                                         		@PathVariable("codeFileControl") Long codeFileControl,
                                                                         		@PathVariable("idSeizure") Long idSeizure){
    	logger.info("SeizureController - getBankAccountListBySeizure - start");
    	ResponseEntity<List<SeizedBankAccountDTO>> response = null;
    	List<SeizedBankAccountDTO> result = new ArrayList<>();
    	
    	try {
    		
			result = seizureService.getBankAccountListBySeizure(codeFileControl,idSeizure);
			response = new ResponseEntity<>(result, HttpStatus.OK);
    	
	    } catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			logger.error("ERROR in getBankAccountListByCodeFileControlAndPetitionCase: ", e);
		}	
    	
    	logger.info("SeizureController - getBankAccountListBySeizure - end");
        return response;
    }

    @GetMapping(value = "/{codeFileControl}/actions")
    @ApiOperation(value="Devuelve el listado de acciones posibles dependiendo del tipo de entidad del fichero.")
    public ResponseEntity<List<SeizureActionDTO>> getSeizureActions(Authentication authentication,
    																@PathVariable("codeFileControl") Long codeFileControl)
    {
    	logger.info("SeizureController - getSeizureActions - start");
        ResponseEntity<List<SeizureActionDTO>> response = null;
    	List<SeizureActionDTO> result = new ArrayList<>();
    	
    	try {
    		
			result = seizureService.getSeizureActions(codeFileControl);
			response = new ResponseEntity<>(result, HttpStatus.OK);
    	
	    } catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			logger.error("ERROR in getSeizureActions: ", e);
		}	

    	logger.info("SeizureController - getSeizureActions - end");
        return response;
    	
    }

    @GetMapping(value = "/seizureStatus")
    @ApiOperation(value="Devuelve el listado de estados posibles de las trabas.")
    public ResponseEntity<List<SeizureStatusDTO>> getSeizureStatusList(Authentication authentication)
    {

    	logger.info("SeizureController - getSeizureStatusList - start");
        ResponseEntity<List<SeizureStatusDTO>> response = null;

        List<SeizureStatusDTO> result = new ArrayList<>();
          
    	try {

			result = seizureService.getSeizureStatusList();
			
			response = new ResponseEntity<>(result, HttpStatus.OK);
    	
	    } catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			logger.error("ERROR in getSeizureStatusList: ", e);
		}	
        
    	logger.info("SeizureController - getSeizureStatusList - end");
        return response;
        
    }

    @PostMapping(value = "/{codeFileControl}/case/{idSeizure}/accounts")
    @ApiOperation(value="Guarda una actualizacion para las cuentas trabadas.")
    public ResponseEntity<String> updateSeizedBankAccountList(Authentication authentication,
    														  @PathVariable("codeFileControl") Long codeFileControl,
    														  @PathVariable("idSeizure") Long idSeizure,
    														  @RequestBody SeizureSaveDTO seizureSave){
    	logger.info("SeizureController - updateSeizedBankAccountList - start");
		ResponseEntity<String> response = null;
		boolean result = false;

		try {

			String userModif = authentication.getName();

			result = seizureService.updateSeizedBankAccountList(codeFileControl, idSeizure, seizureSave,userModif);

			if (result) {
				response = new ResponseEntity<>(HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {

			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

			logger.error("ERROR in updateSeizedBankAccountList: ", e);
		}

		logger.info("SeizureController - updateSeizedBankAccountList - end");
		return response;

    }
    

    @PostMapping(value = "/{codeFileControl}/case/{idSeizure}/status")
    @ApiOperation(value="Guarda una actualizacion de estado para el caso indicado")
    public ResponseEntity<String> updateSeizureStatus(Authentication authentication,
    												  @PathVariable("codeFileControl") Long codeFileControl,
    												  @PathVariable("idSeizure") Long idSeizure,
    												  @RequestBody SeizureStatusDTO seizureStatus){
    	logger.info("SeizureController - updateSeizureStatus - start");
		ResponseEntity<String> response = null;
		boolean result = false;

		try {

			String userModif = authentication.getName();

			result = seizureService.updateSeizureStatus(idSeizure, seizureStatus, userModif);

			if (result) {
				response = new ResponseEntity<>(HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {

			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

			logger.error("ERROR in updateSeizureStatus: ", e);
		}

		logger.info("SeizureController - updateSeizureStatus - end");
		return response;

    }
    
    
//    @PostMapping(value = "/{codeFileControl}/status")
//    @ApiOperation(value="Guarda una actualizacion de estado para el caso indicado")
//    public ResponseEntity<String> updateFileControlStatus(Authentication authentication,
//    												  		@PathVariable("codeFileControl") Long codeFileControl,
//    												  		@RequestBody FileControlStatusDTO fileControlStatus){
//    	
//		ResponseEntity<String> response = null;
//		boolean result = false;
//
//		try {
//
//			if (fileControlStatus!=null) {
//				
//				String userModif = authentication.getName();
//				
//				result = fileControlService.updateFileControlStatus(codeFileControl, fileControlStatus.getCode(), userModif);
//			}
//			
//			
//			if (result) {
//				response = new ResponseEntity<>(HttpStatus.OK);
//			} else {
//				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//			}
//
//		} catch (Exception e) {
//
//			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//
//			LOG.error("ERROR in updateFileControlStatus: ", e);
//		}
//
//		return response;
//
//    }
    
    @GetMapping(value = "/{codeFileControl}/case/{idSeizure}/audit")
    @ApiOperation(value="Devuelve el histórico de cambios")
    public ResponseEntity<List<SeizureDTO>> getAuditSeizure(Authentication authentication,
    												  @PathVariable("codeFileControl") Long codeFileControl,
    												  @PathVariable("idSeizure") Long idSeizure){
    	logger.info("SeizureController - getAuditSeizure - start");
		ResponseEntity<List<SeizureDTO>> response = null;
		List<SeizureDTO> result = new ArrayList<>();

		try {

			result = seizureService.getAuditSeizure(codeFileControl, idSeizure);

			response = new ResponseEntity<>(result, HttpStatus.OK);
	    	
	    } catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			logger.error("ERROR in getAudit: ", e);
		}	
        
		logger.info("SeizureController - getAuditSeizure - end");
        return response;

    }
    
    @GetMapping(value = "/{codeFileControl}/case/{idSeizure}/accounts/audit/{codAudit}")
    @ApiOperation(value="Devuelve el histórico de cambios")
    public ResponseEntity<List<SeizedBankAccountDTO>> getAudit(Authentication authentication,
    												  @PathVariable("codeFileControl") Long codeFileControl,
    												  @PathVariable("idSeizure") Long idSeizure,
    												  @PathVariable("codAudit") Long codAudit) {
    	logger.info("SeizureController - getAudit - start");
		ResponseEntity<List<SeizedBankAccountDTO>> response = null;
		List<SeizedBankAccountDTO> result = new ArrayList<>();

		try {

			result = seizureService.getAuditSeizedBankAccounts(codeFileControl, idSeizure, codAudit);

			response = new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {

			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

			logger.error("ERROR in getAudit: ", e);
		}

		logger.info("SeizureController - getAudit - end");
		return response;
	}

  
    @PostMapping(value = "/{codeFileControl}/accounting")
    @ApiOperation(value="Envio de datos a contabilidad.")
    public ResponseEntity<FileControlDTO> sendAccounting(Authentication authentication,
    										  @PathVariable("codeFileControl") Long codeFileControl){
    	
    	logger.info("SeizureController - sendAccounting - start");
    	
    	ResponseEntity<FileControlDTO> response = null;
		boolean result = false;

		FileControlDTO resultFileControlDTO = null;
		
		try {

			String userName = authentication.getName();
		
			accountingService.sendAccountingSeizure(codeFileControl, userName);
			
			//Se obtiene el fileControl que se va a retornar:
			resultFileControlDTO = fileControlService.getByCodeFileControl(codeFileControl);
		} catch (Exception e) {

			response = new ResponseEntity<>(resultFileControlDTO,HttpStatus.INTERNAL_SERVER_ERROR);

			logger.error("ERROR in doAccounting: ", e);
		}

		logger.info("SeizureController - sendAccounting - end");
		return response;
    	
    }
	
    /*
    @PostMapping(value = "/{codeFileControl}/case/{idSeizure}/undoaccounting")
    @ApiOperation(value="Retroceder la contabilidad de una cuenta.")
    public ResponseEntity<String> undoAccounting(Authentication authentication,
    										  @PathVariable("codeFileControl") Long codeFileControl,
    										  @PathVariable("idSeizure") Long idSeizure,
    										  @RequestBody BankAccountDTO bankAccount){
    	logger.info("SeizureController - undoAccounting - start");
    	ResponseEntity<String> response = null;
		boolean result = false;

		try {

			String userName = authentication.getName();
		
			accountingService.undoAccounting(codeFileControl, idSeizure, bankAccount.getCodeBankAccount(), userName);
		} catch (Exception e) {

			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

			logger.error("ERROR in doAccounting: ", e);
		}

		logger.info("SeizureController - undoAccounting - end");
		return response;
    	
    }
    */

	@GetMapping("/notification/{idSeizure}/report")
	@ApiOperation(value = "Devuelve un justificante de embargo")
	public ResponseEntity<InputStreamResource> generateSeizureLetter(
			@PathVariable("idSeizure") Integer idSeizure) {
    	logger.info("SeizureController - generateSeizureLetter - start");

    	try {
			DownloadReportFile.setTempFileName("seizure-letter");

			DownloadReportFile.setFileTempPath(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_JASPER_TEMP));

			// seizure service falta
			DownloadReportFile.writeFile(seizureService.generateSeizureLetter(idSeizure));
			
			logger.info("SeizureController - generateSeizureLetter - end");
			return DownloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in justificanteReport", e);

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{codeFileControl}/report")
	@ApiOperation(value = "Devuelve un fichero de resumen trabas fase 3")
	public ResponseEntity<InputStreamResource> generateSeizureRequestF3(@PathVariable("codeFileControl") Integer codControlFichero) {
		logger.info("SeizureController - generateSeizureRequestF3 - start");

		try {
			DownloadReportFile.setTempFileName("seizure-request");

			DownloadReportFile.setFileTempPath(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_JASPER_TEMP));

			// seizure service falta
			DownloadReportFile.writeFile(seizureService.generateSeizureRequestF3(codControlFichero));

			logger.info("SeizureController - generateSeizureRequestF3 - end");
			return DownloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in resumenTrabas", e);

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/response/{fileControl}/report")
	@ApiOperation(value = "Devuelve un fichero de resumen trabas fase 4")
	public ResponseEntity<InputStreamResource> generateSeizureResponseF4(
			@PathVariable("fileControl") Integer codControlFichero) {
		logger.info("SeizureController - generateSeizureResponseF4 - start");

		try {
			DownloadReportFile.setTempFileName("seizure-response");

			DownloadReportFile.setFileTempPath(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_JASPER_TEMP));

			// seizure service falta
			DownloadReportFile.writeFile(seizureService.generateSeizureResponseF4(codControlFichero));

			logger.info("SeizureController - generateSeizureResponseF4 - end");
			return DownloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in resumenTrabas", e);

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	private String getPDFSavedPath() throws ICEException {

		return generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_JASPER_TEMP);

	}

}