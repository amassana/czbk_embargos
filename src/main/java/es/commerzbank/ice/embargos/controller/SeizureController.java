package es.commerzbank.ice.embargos.controller;

import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.embargos.domain.dto.*;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlfichero;
import es.commerzbank.ice.embargos.domain.entity.EstadoTraba;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.SeizedBankAccountRepository;
import es.commerzbank.ice.embargos.repository.SeizedRepository;
import es.commerzbank.ice.embargos.service.AccountingService;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.SeizureService;
import es.commerzbank.ice.embargos.utils.DownloadReportFile;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.ICEDateUtils;
import es.commerzbank.ice.embargos.utils.OfficeUtils;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	@Autowired
	private OfficeUtils officeUtils;

	@Autowired
	private FileControlRepository fileControlRepository;
	
	@Autowired
	private SeizedRepository seizedRepository;
	
	@Autowired
	private SeizedBankAccountRepository seizedBankAccountRepository;
	
    @GetMapping(value = "/{codeFileControl}")
    @ApiOperation(value="Devuelve la lista de embargos para una petición de embargo.")
    public ResponseEntity<List<SeizureDTO>> getSeizureListByCodeFileControl(Authentication authentication,
                                                                                 @PathVariable("codeFileControl") Long codeFileControl)
	{
    	ResponseEntity<List<SeizureDTO>> response = null;
    	List<SeizureDTO> result = new ArrayList<SeizureDTO>();
    	
    	try {
    		
			result = seizureService.getSeizureListByCodeFileControl(codeFileControl);
			response = new ResponseEntity<>(result, HttpStatus.OK);
    	
	    } catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			
			logger.error("ERROR in getSeizureListByCodeFileControl: ", e);
		}	

		return response;

    }
    
    @GetMapping(value = "/{codeFileControl}/case/{idSeizure}")
    @ApiOperation(value="Devuelve el embargo a partir de su identificador.")
    public ResponseEntity<SeizureDTO> getSeizureById(Authentication authentication, @PathVariable("idSeizure") Long idSeizure)
	{
    	ResponseEntity<SeizureDTO> response = null;
    	SeizureDTO result = null;
    	
    	try {
    		  
    		//TODO: Revisar: idSeizure en este caso esta aplicando por codEmbargo. ¿tiene que ser por codTraba?
    		
			result = seizureService.getSeizureById(idSeizure);
			response = new ResponseEntity<>(result, HttpStatus.OK);
    	
	    } catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			
			logger.error("ERROR in getSeizureListByCodeFileControl: ", e);
		}
		return response;
    	
    }

    @GetMapping(value = "/{codeFileControl}/case/{idSeizure}/accounts")
    @ApiOperation(value="Devuelve el listado de cuentas bancarias asociadas a la traba.")
    public ResponseEntity<List<SeizedBankAccountDTO>> getBankAccountListBySeizure(Authentication authentication,
                                                                         		@PathVariable("codeFileControl") Long codeFileControl,
                                                                         		@PathVariable("idSeizure") Long idSeizure){
    	ResponseEntity<List<SeizedBankAccountDTO>> response = null;
    	List<SeizedBankAccountDTO> result = new ArrayList<>();
    	
    	try {
    		
			result = seizureService.getBankAccountListBySeizure(codeFileControl,idSeizure);
			response = new ResponseEntity<>(result, HttpStatus.OK);
    	
	    } catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			
			logger.error("ERROR in getBankAccountListByCodeFileControlAndPetitionCase: ", e);
		}

        return response;
    }

    @GetMapping(value = "/{codeFileControl}/actions")
    @ApiOperation(value="Devuelve el listado de acciones posibles dependiendo del tipo de entidad del fichero.")
    public ResponseEntity<List<SeizureActionDTO>> getSeizureActions(Authentication authentication,
    																@PathVariable("codeFileControl") Long codeFileControl)
    {
        ResponseEntity<List<SeizureActionDTO>> response = null;
    	List<SeizureActionDTO> result = new ArrayList<>();
    	
    	try {
    		
			result = seizureService.getSeizureActions(codeFileControl);
			response = new ResponseEntity<>(result, HttpStatus.OK);
    	
	    } catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			
			logger.error("ERROR in getSeizureActions: ", e);
		}

        return response;
    	
    }

    @GetMapping(value = "/seizureStatus")
    @ApiOperation(value="Devuelve el listado de estados posibles de las trabas.")
    public ResponseEntity<List<SeizureStatusDTO>> getSeizureStatusList(Authentication authentication)
    {
        ResponseEntity<List<SeizureStatusDTO>> response = null;

        List<SeizureStatusDTO> result = new ArrayList<>();
          
    	try {

			result = seizureService.getSeizureStatusList();
			
			response = new ResponseEntity<>(result, HttpStatus.OK);
    	
	    } catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			
			logger.error("ERROR in getSeizureStatusList: ", e);
		}	

        return response;
        
    }

    @PostMapping(value = "/{codeFileControl}/case/{idSeizure}/accounts")
    @ApiOperation(value="Guarda una actualizacion para las cuentas trabadas.")
    public ResponseEntity<String> updateSeizedBankAccountList(Authentication authentication,
    														  @PathVariable("codeFileControl") Long codeFileControl,
    														  @PathVariable("idSeizure") Long idSeizure,
    														  @RequestBody SeizureSaveDTO seizureSave)
	{
		logger.debug("SeizureController - Actualizando embargo "+ codeFileControl +"-"+ idSeizure);
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

			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

			logger.error("ERROR in updateSeizedBankAccountList: ", e);
		}

		return response;

    }
    

    @PostMapping(value = "/{codeFileControl}/case/{idSeizure}/status")
    @ApiOperation(value="Guarda una actualizacion de estado para el caso indicado")
    public ResponseEntity<String> updateSeizureStatus(Authentication authentication,
    												  @PathVariable("codeFileControl") Long codeFileControl,
    												  @PathVariable("idSeizure") Long idSeizure,
    												  @RequestBody SeizureStatusDTO seizureStatus)
	{
		logger.debug("SeizureController - Actualizando embargo "+ codeFileControl +"-"+ idSeizure +" cambio de estado");
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

			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

			logger.error("ERROR in updateSeizureStatus: ", e);
		}

		return response;

    }

	@PostMapping(value = "/{codeFileControl}/case/{idSeizure}/{idAccount}/status")
	@ApiOperation(value="Guarda una actualizacion de estado para el caso indicado")
	public ResponseEntity<String> updateSeizureAccountStatus(Authentication authentication,
													  @PathVariable("codeFileControl") Long codeFileControl,
													  @PathVariable("idSeizure") Long idSeizure,
													  @PathVariable("idAccount") Long idAccount,
													  @RequestBody AccountStatusSeizedDTO accountStatusSeized)
	{
		logger.debug("SeizureController - Actualizando embargo "+ codeFileControl +"-"+ idSeizure +" cambiando de estado la cuenta "+ idAccount);
		ResponseEntity<String> response = null;
		boolean result = false;

		try {

			String userModif = authentication.getName();

			result = seizureService.updateAccountSeizureStatus(idAccount, idSeizure, accountStatusSeized, userModif);

			if (result) {
				response = new ResponseEntity<>(HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {

			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

			logger.error("ERROR in updateSeizureAccountStatus: ", e);
		}

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
    												  @PathVariable("idSeizure") Long idSeizure)
	{
		ResponseEntity<List<SeizureDTO>> response = null;
		List<SeizureDTO> result = new ArrayList<>();

		try {

			result = seizureService.getAuditSeizure(codeFileControl, idSeizure);

			response = new ResponseEntity<>(result, HttpStatus.OK);
	    	
	    } catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			
			logger.error("ERROR in getAudit: ", e);
		}

        return response;

    }
    
    @GetMapping(value = "/{codeFileControl}/case/{idSeizure}/accounts/audit/{codAudit}")
    @ApiOperation(value="Devuelve el histórico de cambios")
    public ResponseEntity<List<SeizedBankAccountDTO>> getAudit(Authentication authentication,
    												  @PathVariable("codeFileControl") Long codeFileControl,
    												  @PathVariable("idSeizure") Long idSeizure,
    												  @PathVariable("codAudit") Long codAudit)
	{
		ResponseEntity<List<SeizedBankAccountDTO>> response = null;
		List<SeizedBankAccountDTO> result = new ArrayList<>();

		try {

			result = seizureService.getAuditSeizedBankAccounts(codeFileControl, idSeizure, codAudit);

			response = new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {

			response = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);

			logger.error("ERROR in getAudit: ", e);
		}

		return response;
	}

  
    @PostMapping(value = "/{codeFileControl}/accounting")
    @ApiOperation(value="Envio de datos a contabilidad.")
    public ResponseEntity<FileControlDTO> sendAccounting(Authentication authentication,
    										  @PathVariable("codeFileControl") Long codeFileControl)
	{
		logger.debug("SeizureController - Se contabilizan las trabas de "+ codeFileControl);
    	ResponseEntity<FileControlDTO> response = null;
		//boolean result = false;

		FileControlDTO resultFileControlDTO = null;
		
		try {

			String userName = authentication.getName();
		
			accountingService.embargoContabilizar(codeFileControl, userName);
			
			//Se obtiene el fileControl que se va a retornar:
			resultFileControlDTO = fileControlService.getByCodeFileControl(codeFileControl);

			response = new ResponseEntity<>(resultFileControlDTO,HttpStatus.OK);
		} catch (Exception e) {

			if (e.getMessage()!=null && e.getMessage().equals("ERROR estado no adecuado")) {
				response = new ResponseEntity<>(resultFileControlDTO, HttpStatus.CONFLICT);
			}
			else {
				response = new ResponseEntity<>(resultFileControlDTO, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			logger.error("ERROR in doAccounting: ", e);
		}

		return response;
    	
    }
	
    
    @PostMapping(value = "/{codeFileControl}/case/{idSeizure}/{idAccount}/undoAccounting")
    @ApiOperation(value="Retroceder la contabilidad de una cuenta.")
    public ResponseEntity<String> undoAccounting(Authentication authentication,
    										  @PathVariable("codeFileControl") Long codeFileControl,
    										  @PathVariable("idSeizure") Long idSeizure,
    										  @PathVariable("idAccount") Long idAccount) {
    	
    	logger.debug("SeizureController - undoAccounting - start");
    	ResponseEntity<String> response = null;
    	boolean result = false;

		try {

			String userName = authentication.getName();
		
			// Se obtiene el ControlFichero
		    ControlFichero controlFichero = fileControlRepository.getOne(codeFileControl);
		       
		    // Comprobar que el fichero está en el estado adecuado
		    if (controlFichero!=null &&
		    	controlFichero.getEstadoCtrlfichero().getId().getCodEstado()!=EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PENDING_ACCOUNTING_RESPONSE  &&
	        	controlFichero.getEstadoCtrlfichero().getId().getCodEstado()!=EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PENDING_TO_SEND) {
		    	
	        	logger.debug("El fichero NO está en el estado adecuado");
	        	response = new ResponseEntity<>(HttpStatus.CONFLICT);
	        }
		    else if (controlFichero!=null) {
		    	
		    	//Se actualiza el estado de controlFichero a Recibido
		        EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero(
		        		EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_RECEIVED,
		        		EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_AEAT);
		        controlFichero.setEstadoCtrlfichero(estadoCtrlfichero);
		        	        
		        controlFichero.setUsuarioUltModificacion(userName);
		        controlFichero.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
	            
		        fileControlService.saveFileControlTransaction(controlFichero);
		        
		        //Se actualiza el estado del idSeizure a Pendiente
		        Long estadoTrabaActual = null; 
		        Optional<Traba> trabaOpt = seizedRepository.findById(idSeizure);
		        Traba traba = null;
		        
				if (trabaOpt.isPresent()) {
					traba = trabaOpt.get();
					
					estadoTrabaActual = traba.getEstadoTraba().getCodEstado();
					
					EstadoTraba estadoTraba = new EstadoTraba();
					estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE);

					traba.setEstadoTraba(estadoTraba);

					BigDecimal fechaActualBigDec = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
					traba.setUsuarioUltModificacion(userName);
					traba.setFUltimaModificacion(fechaActualBigDec);
					
					seizedRepository.save(traba);
				}
		        
		        //Se actualiza el estado de la cuenta a Pendiente
				Optional<CuentaTraba> cuentaTrabaOpt = seizedBankAccountRepository.findById(idAccount);
				CuentaTraba cuentaTraba = null;
				
				if (cuentaTrabaOpt.isPresent()) {
					cuentaTraba = cuentaTrabaOpt.get();
					EstadoTraba estadoTraba = new EstadoTraba();
					estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE);

					cuentaTraba.setEstadoTraba(estadoTraba);
					
					BigDecimal fechaActualBigDec = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
					cuentaTraba.setUsuarioUltModificacion(userName);
					cuentaTraba.setFUltimaModificacion(fechaActualBigDec);
					
					seizedBankAccountRepository.save(cuentaTraba);					
				}
				
				//Si el estado del caso, antes del cambio, era Contabilizado, deshacer el movimiento contable
				if (estadoTrabaActual!=null && estadoTrabaActual.equals(Long.valueOf(EmbargosConstants.COD_ESTADO_TRABA_CONTABILIZADA)) &&
					traba!=null && cuentaTraba!=null) {
					
					accountingService.undoAccounting(controlFichero, traba, cuentaTraba, userName);					
				}
				
				if (result) {
					response = new ResponseEntity<>(HttpStatus.OK);
				} else {
					response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
		    }
		    else {
		    	logger.debug("El fichero NO existe");
	        	response = new ResponseEntity<>(HttpStatus.CONFLICT);	
		    }
		    
		} catch (Exception e) {
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			logger.error("ERROR in undoAccounting: ", e);
		}

		logger.debug("SeizureController - undoAccounting - end");
		return response;
    	
    }
    

	@GetMapping("/notification/{idSeizure}/report")
	@ApiOperation(value = "Devuelve un justificante de embargo")
	public ResponseEntity<InputStreamResource> generateSeizureLetter(
			@PathVariable("idSeizure") Long idSeizure)
	{
    	try {
    		DownloadReportFile downloadReportFile = new DownloadReportFile();
    		
    		downloadReportFile.setTempFileName("seizure-letter");

    		downloadReportFile.setFileTempPath(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_JASPER_TEMP));

			// seizure service falta
			downloadReportFile.writeFile(seizureService.reportSeizureLetter(idSeizure));

			return downloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in justificanteReport", e);

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{codeFileControl}/report")
	@ApiOperation(value = "Devuelve un fichero de resumen trabas fase 3")
	public ResponseEntity<InputStreamResource> generateSeizureRequestF3(
			Authentication authentication,
			@PathVariable("codeFileControl") Integer codControlFichero)
	{
		try {
			DownloadReportFile downloadReportFile = new DownloadReportFile();
			
			downloadReportFile.setTempFileName("seizure-request");

			downloadReportFile.setFileTempPath(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_JASPER_TEMP));

			String oficina = officeUtils.getLocalidadUsuario(authentication);

			downloadReportFile.writeFile(seizureService.reportSeizureRequestF3(codControlFichero, oficina));

			return downloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in resumenTrabas", e);

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/response/{fileControl}/report")
	@ApiOperation(value = "Devuelve un fichero de resumen trabas fase 4")
	public ResponseEntity<InputStreamResource> generateSeizureResponseF4(
			Authentication authentication,
			@PathVariable("fileControl") Integer codControlFichero)
	{
		try {
			DownloadReportFile downloadReportFile = new DownloadReportFile();
			
			downloadReportFile.setTempFileName("seizure-response");

			downloadReportFile.setFileTempPath(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_JASPER_TEMP));

			String oficina = officeUtils.getLocalidadUsuario(authentication);

			downloadReportFile.writeFile(seizureService.reportSeizureResponseF4(codControlFichero, oficina));

			return downloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in resumenTrabas", e);

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	//private String getPDFSavedPath() throws ICEException {

		//return generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_JASPER_TEMP);

	//}

}