package es.commerzbank.ice.embargos.controller;

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

	private static final Logger LOG = LoggerFactory.getLogger(SeizureController.class);

	@Value("${commerzbank.jasper.temp}")
	private String pdfSavedPath;

	@Autowired
	private SeizureService seizureService;
	
    @GetMapping(value = "/{codeFileControl}")
    @ApiOperation(value="Devuelve la lista de embargos para una petición de embargo.")
    public ResponseEntity<List<SeizureDTO>> getSeizureListByCodeFileControl(Authentication authentication,
                                                                                 @PathVariable("codeFileControl") Long codeFileControl){
    	
    	ResponseEntity<List<SeizureDTO>> response = null;
    	List<SeizureDTO> result = new ArrayList<SeizureDTO>();
    	
    	try {
    		
			result = seizureService.getSeizureListByCodeFileControl(codeFileControl);
			response = new ResponseEntity<>(result, HttpStatus.OK);
    	
	    } catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			LOG.error("ERROR in getSeizureListByCodeFileControl: ", e);
		}	
			
		return response;

    }
    
    @GetMapping(value = "/{codeFileControl}/case/{idSeizure}")
    @ApiOperation(value="Devuelve el embargo a partir de su identificador.")
    public ResponseEntity<SeizureDTO> getSeizureById(Authentication authentication, @PathVariable("idSeizure") Long idSeizure){
    	
    	ResponseEntity<SeizureDTO> response = null;
    	SeizureDTO result = null;
    	
    	try {
    		  
    		//TODO: Revisar: idSeizure en este caso esta aplicando por codEmbargo. ¿tiene que ser por codTraba?
    		
			result = seizureService.getSeizureById(idSeizure);
			response = new ResponseEntity<>(result, HttpStatus.OK);
    	
	    } catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			LOG.error("ERROR in getSeizureListByCodeFileControl: ", e);
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
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			LOG.error("ERROR in getBankAccountListByCodeFileControlAndPetitionCase: ", e);
		}	

        return response;
    }

    @GetMapping(value = "/actions")
    @ApiOperation(value="Devuelve el listado de acciones posibles de las trabas.")
    public ResponseEntity<List<SeizureActionDTO>> getSeizureActions(Authentication authentication)
    {
    	
        ResponseEntity<List<SeizureActionDTO>> response = null;
    	List<SeizureActionDTO> result = new ArrayList<>();
    	
    	try {
    		
			result = seizureService.getSeizureActions();
			response = new ResponseEntity<>(result, HttpStatus.OK);
    	
	    } catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			LOG.error("ERROR in getSeizureActions: ", e);
		}	

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
        
        ResponseEntity<List<SeizureStatusDTO>> response = null;

        List<SeizureStatusDTO> result = new ArrayList<>();
          
    	try {

			result = seizureService.getSeizureStatusList();
			
			response = new ResponseEntity<>(result, HttpStatus.OK);
    	
	    } catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			LOG.error("ERROR in getSeizureStatusList: ", e);
		}	
        
        return response;
        
    }

    @PostMapping(value = "/{codeFileControl}/case/{idSeizure}/accounts")
    @ApiOperation(value="Guarda una actualizacion para las cuentas trabadas.")
    public ResponseEntity<String> updateSeizedBankAccountList(Authentication authentication,
    														  @PathVariable("codeFileControl") Long codeFileControl,
    														  @PathVariable("idSeizure") Long idSeizure,
    														  @RequestBody List<SeizedBankAccountDTO> seizedBankAccountList){
    	
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

			LOG.error("ERROR in updateSeizedBankAccountList: ", e);
		}

		return response;

    }
    

    @PostMapping(value = "/{codeFileControl}/case/{idSeizure}/status")
    @ApiOperation(value="Guarda una actualizacion de estado para el caso indicado")
    public ResponseEntity<String> updateSeizureStatus(Authentication authentication,
    												  @PathVariable("codeFileControl") Long codeFileControl,
    												  @PathVariable("idSeizure") Long idSeizure,
    												  @RequestBody SeizureStatusDTO seizureStatus){
    	
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

			LOG.error("ERROR in updateSeizureStatus: ", e);
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
    												  @PathVariable("idSeizure") Long idSeizure){
    	
		ResponseEntity<List<SeizureDTO>> response = null;
		List<SeizureDTO> result = new ArrayList<>();

		try {

			result = seizureService.getAuditSeizure(codeFileControl, idSeizure);

			response = new ResponseEntity<>(result, HttpStatus.OK);
	    	
	    } catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			LOG.error("ERROR in getAudit: ", e);
		}	
        
        return response;

    }
    
    @GetMapping(value = "/{codeFileControl}/case/{idSeizure}/accounts/audit/{codAudit}")
    @ApiOperation(value="Devuelve el histórico de cambios")
    public ResponseEntity<List<SeizedBankAccountDTO>> getAudit(Authentication authentication,
    												  @PathVariable("codeFileControl") Long codeFileControl,
    												  @PathVariable("idSeizure") Long idSeizure,
    												  @PathVariable("codAudit") Long codAudit){
    	
		ResponseEntity<List<SeizedBankAccountDTO>> response = null;
		List<SeizedBankAccountDTO> result = new ArrayList<>();

		try {

			result = seizureService.getAuditSeizedBankAccounts(codeFileControl, idSeizure, codAudit);

			response = new ResponseEntity<>(result, HttpStatus.OK);
	    	
	    } catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			LOG.error("ERROR in getAudit: ", e);
		}	
        
        return response;

    }
    
	@GetMapping("/{idSeizure}/case/report")
	public ResponseEntity<InputStreamResource> generarJustificanteEmbargo(
			@PathVariable("idSeizure") Integer idSeizure) {
		

		DownloadReportFile.setTempFileName("justificanteReport");

		DownloadReportFile.setFileTempPath(pdfSavedPath);

		try {

			// seizure service falta
			DownloadReportFile.writeFile(seizureService.generateJustificanteEmbargo(idSeizure));

			return DownloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			LOG.error("Error in justificanteReport", e);
			
			if (e.getMessage() == null) {
				return new ResponseEntity<InputStreamResource>(HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/propertyLien/fileControl/{fileControl}/report")
	public ResponseEntity<InputStreamResource> generarResumentTrabas(
			@PathVariable("fileControl") Integer codControlFichero) {
		
		

		DownloadReportFile.setTempFileName("resumenTrabasReport");

		DownloadReportFile.setFileTempPath(pdfSavedPath);

		try {

			// seizure service falta
			DownloadReportFile.writeFile(seizureService.generarResumenTrabas(codControlFichero));

			return DownloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			LOG.error("Error in justificanteReport", e);
			System.out.println(e);

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
