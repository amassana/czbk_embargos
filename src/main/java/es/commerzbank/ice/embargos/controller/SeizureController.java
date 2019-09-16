package es.commerzbank.ice.embargos.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

import es.commerzbank.ice.comun.lib.formats.contabilidad.RespuestaContabilidad;
import es.commerzbank.ice.embargos.domain.dto.BankAccountDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizedBankAccountDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureActionDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureStatusDTO;
import es.commerzbank.ice.embargos.service.SeizureService;
import es.commerzbank.ice.utils.DownloadReportFile;
import io.swagger.annotations.ApiOperation;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/seizure")
public class SeizureController {

	private static final Logger logger = LoggerFactory.getLogger(SeizureController.class);

	@Value("${commerzbank.jasper.temp}")
	private String pdfSavedPath;

	@Autowired
	private SeizureService seizureService;
	
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

    @GetMapping(value = "/actions")
    @ApiOperation(value="Devuelve el listado de acciones posibles de las trabas.")
    public ResponseEntity<List<SeizureActionDTO>> getSeizureActions(Authentication authentication)
    {
    	logger.info("SeizureController - getSeizureActions - start");
        ResponseEntity<List<SeizureActionDTO>> response = null;
    	List<SeizureActionDTO> result = new ArrayList<>();
    	
    	try {
    		
			result = seizureService.getSeizureActions();
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
    	/*
    	ResponseEntity<List<SeizureStatusDTO>> response = null;
        List<SeizureStatusDTO> result = new ArrayList<SeizureStatusDTO>();

        SeizureStatusDTO sstatus = new SeizureStatusDTO();
        sstatus.setCode("1");
        sstatus.setCode("Pendiente");

        SeizureStatusDTO sstatus2 = new SeizureStatusDTO();
        sstatus2.setCode("2");
        sstatus2.setCode("Pendiente de contabilizacion");

        SeizureStatusDTO sstatus3 = new SeizureStatusDTO();
        sstatus3.setCode("3");
        sstatus3.setCode("Contabilizado");

        result.add(sstatus);
        result.add(sstatus2);
        result.add(sstatus3);

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
        */
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
    														  @RequestBody List<SeizedBankAccountDTO> seizedBankAccountList){
    	logger.info("SeizureController - updateSeizedBankAccountList - start");
		ResponseEntity<String> response = null;
		boolean result = false;

		try {

			String userModif = authentication.getName();

			result = seizureService.updateSeizedBankAccountList(codeFileControl, idSeizure, seizedBankAccountList,userModif);

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

			result = seizureService.updateSeizureStatus(codeFileControl, idSeizure, seizureStatus, userModif);

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
    public ResponseEntity<String> sendAccounting(Authentication authentication,
    										  @PathVariable("codeFileControl") Long codeFileControl){
    	logger.info("SeizureController - sendAccounting - start");
    	ResponseEntity<String> response = null;
		boolean result = false;

		try {

			String userName = authentication.getName();
		
			result = seizureService.sendAccounting(codeFileControl, userName);
			
			
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
		
			result = seizureService.undoAccounting(codeFileControl, idSeizure, bankAccount.getCodeBankAccount(), userName);
			
			
			if (result) {
				response = new ResponseEntity<>(HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {

			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

			logger.error("ERROR in doAccounting: ", e);
		}

		logger.info("SeizureController - undoAccounting - end");
		return response;
    	
    }
    

    @PostMapping(value = "/accountingNote")
    @ApiOperation(value="Tratamiento de la respuesta de Contabilidad (nota contable).")
    public ResponseEntity<String> manageAccountingNoteCallback(Authentication authentication,
    										  @RequestBody RespuestaContabilidad respuestaContabilidad){
    	logger.info("SeizureController - manageAccountingNoteCallback - start");
    	ResponseEntity<String> response = null;
		boolean result = false;

		try {

			String userName = authentication.getName();
		
			result = seizureService.manageAccountingNoteCallback(respuestaContabilidad, userName);
			
			
			if (result) {
				response = new ResponseEntity<>(HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {

			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

			logger.error("ERROR in doAccounting: ", e);
		}
		
    	logger.info("SeizureController - manageAccountingNoteCallback - end");
		return response;
    	
    }
    
    
	@GetMapping("/notification/{idSeizure}/seizureReport")
	@ApiOperation(value = "Devuelve un justificante de embargo")
	public ResponseEntity<InputStreamResource> generarJustificanteEmbargo(
			@PathVariable("idSeizure") Integer idSeizure) {
    	logger.info("SeizureController - generarJustificanteEmbargo - start");
		DownloadReportFile.setTempFileName("justificanteReport");

		DownloadReportFile.setFileTempPath(pdfSavedPath);

		try {

			// seizure service falta
			DownloadReportFile.writeFile(seizureService.generateJustificanteEmbargo(idSeizure));
			
			logger.info("SeizureController - generarJustificanteEmbargo - end");
			return DownloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in justificanteReport", e);

			if (e.getMessage() == null) {
				return new ResponseEntity<InputStreamResource>(HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/notification/{idLifting}/liftingReport")
	@ApiOperation(value = "Devuelve un justificante de embargo")
	public ResponseEntity<InputStreamResource> generarJustificanteEmbargo3(
			@PathVariable("idLifting") Integer idLifting) {
		logger.info("SeizureController - generarJustificanteEmbargo3 - start");
		DownloadReportFile.setTempFileName("levantamientoReport");

		DownloadReportFile.setFileTempPath(pdfSavedPath);

		try {

			// seizure service falta
			DownloadReportFile.writeFile(seizureService.generateLevantamientoReport(idLifting));
			
			logger.info("SeizureController - generarJustificanteEmbargo3 - end");
			return DownloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in levantamientoReport", e);
			System.out.println(e);

			if (e.getMessage() == null) {
				return new ResponseEntity<InputStreamResource>(HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/summary/fileControl/{fileControl}/requestReport")
	@ApiOperation(value = "Devuelve un fichero de resumen trabas fase 3")
	public ResponseEntity<InputStreamResource> generarResumentTrabaF3(@PathVariable("fileControl") Integer codControlFichero) {
		logger.info("SeizureController - generarResumentTrabaF3 - start");
		DownloadReportFile.setTempFileName("resumenTrabasReportF3");

		DownloadReportFile.setFileTempPath(pdfSavedPath);

		try {

			// seizure service falta
			DownloadReportFile.writeFile(seizureService.generarResumenTrabasF3(codControlFichero));

			logger.info("SeizureController - generarResumentTrabaF3 - end");
			return DownloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in resumenTrabas", e);

			if (e.getMessage() == null) {
				return new ResponseEntity<InputStreamResource>(HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/summary/fileControl/{fileControl}/seizuresReport")
	@ApiOperation(value = "Devuelve un fichero de resumen trabas fase 4")
	public ResponseEntity<InputStreamResource> generarResumentTrabasF4(
			@PathVariable("fileControl") Integer codControlFichero) {
		logger.info("SeizureController - generarResumentTrabasF4 - start");
		DownloadReportFile.setTempFileName("resumenTrabasReportF4");

		DownloadReportFile.setFileTempPath(pdfSavedPath);

		try {

			// seizure service falta
			DownloadReportFile.writeFile(seizureService.generarResumenTrabasF4(codControlFichero));

			logger.info("SeizureController - generarResumentTrabasF4 - end");
			return DownloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in resumenTrabas", e);

			if (e.getMessage() == null) {
				return new ResponseEntity<InputStreamResource>(HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// mover a otro controller
	@GetMapping("/anexo/{cod_usuario}/{cod_traba}/{num_anexo}")
	public ResponseEntity<InputStreamResource> generarAnexo(@PathVariable("cod_usuario") BigDecimal cod_usuario,
			@PathVariable("cod_traba") BigDecimal cod_traba, @PathVariable("num_anexo") Integer num_anexo)
			throws Exception {
		logger.info("SeizureController - generarAnexo - start");
		switch (num_anexo) {
		case 1:
			return downloadAnexo(cod_usuario, cod_traba, num_anexo);
		case 2:
			return downloadAnexo(cod_usuario, cod_traba, num_anexo);
		case 3:
			return downloadAnexo(cod_usuario, cod_traba, num_anexo);
		default:
			logger.info("Error in anexoReport", "error descargando anexos");

		}

		logger.info("SeizureController - generarAnexo - end");
		return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ResponseEntity<InputStreamResource> downloadAnexo(BigDecimal cod_usuario, BigDecimal cod_traba,
			Integer num_anexo) {
		logger.info("SeizureController - downloadAnexo - start");
		DownloadReportFile.setTempFileName("anexoReport" + num_anexo);

		DownloadReportFile.setFileTempPath(pdfSavedPath);

		try {

			DownloadReportFile.writeFile(seizureService.generarAnexo(cod_usuario, cod_traba, num_anexo));

			logger.info("SeizureController - downloadAnexo - end");
			return DownloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in anexoReport", e);

			if (e.getMessage() == null) {
				return new ResponseEntity<InputStreamResource>(HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
