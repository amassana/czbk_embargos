package es.commerzbank.ice.embargos.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.commerzbank.ice.comun.lib.security.Permissions;
import es.commerzbank.ice.embargos.domain.dto.FileControlDTO;
import es.commerzbank.ice.embargos.domain.dto.FileControlFiltersDTO;
import es.commerzbank.ice.embargos.domain.dto.FileControlStatusDTO;
import es.commerzbank.ice.embargos.domain.dto.FileControlTypeDTO;
import es.commerzbank.ice.embargos.domain.dto.ReportParamsDTO;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.FileControlStatusService;
import es.commerzbank.ice.embargos.service.FileTypeService;
import es.commerzbank.ice.utils.DownloadReportFile;
import es.commerzbank.ice.utils.EmbargosConstants;
import io.swagger.annotations.ApiOperation;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/filecontrol")
public class FileControlController {

	private static final Logger logger = LoggerFactory.getLogger(FileControlController.class);

	@Value("${commerzbank.jasper.temp}")
	private String pdfSavedPath;

	@Autowired
	private FileControlService fileControlService;
	
	@Autowired
	private FileTypeService fileTypeService;
	
	@Autowired
	private FileControlStatusService fileControlStatusService;

	@PostMapping(value = "/filter",
			produces = { "application/json" },
			consumes = { "application/json" })
	@ApiOperation(value="Busca en CONTROL_FICHERO las entradas que cumplan el filtro especificado.")
	public ResponseEntity<Page<FileControlDTO>> filter(Authentication authentication,
													   @RequestBody FileControlFiltersDTO fileControlFilters,
													   Pageable pageable){
		logger.info("FileControlController - filter - start");
		ResponseEntity<Page<FileControlDTO>> response = null;
		Page<FileControlDTO> result = null;
		
		try {
		
			if (!Permissions.hasPermission(authentication, "ui.embargos")) {
				logger.info("FileControlController - fileSearch - forbidden");
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			
			result = fileControlService.fileSearch(fileControlFilters, pageable);
			response = new ResponseEntity<>(result, HttpStatus.OK);
		
		} catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			logger.error("ERROR in filter: ", e);
		}	
		
		logger.info("FileControlController - filter - end");
		return response;
	}
	
	@GetMapping(value = "/filetype")
	@ApiOperation(value="Devuelve la lista de tipos de ficheros admitidos en TIPO_FICHERO.")
	public ResponseEntity<List<FileControlTypeDTO>> getFileTypeList(Authentication authentication){
		logger.info("FileControlController - getFileTypeList - start");
		ResponseEntity<List<FileControlTypeDTO>> response = null;
		List<FileControlTypeDTO> result = null;
		
		result = fileTypeService.listAllFileType();
		response = new ResponseEntity<>(result, HttpStatus.OK);
		
		logger.info("FileControlController - getFileTypeList - end");
		return response;
	}

	
	@GetMapping(value = "/filetype/{idFileType}/status")
	@ApiOperation(value="Devuelve la lista de estados para un determinado tipo de archivo de ESTADO_CTRLFICHERO.")
	public ResponseEntity<List<FileControlStatusDTO>> getFileTypeStatusList(Authentication authentication,
			@PathVariable("idFileType") Long idFileType){
		logger.info("FileControlController - getFileTypeStatusList - start");
		ResponseEntity<List<FileControlStatusDTO>> response = null;
		List<FileControlStatusDTO> result = null;
	
		try {
		
		result = fileControlStatusService.listFileControlStatusByFileType(idFileType);
		response = new ResponseEntity<>(result, HttpStatus.OK);
		
		} catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			logger.error("ERROR in getFileTypeStatusList: ", e);
		}
			
		logger.info("FileControlController - getFileTypeStatusList - end");
		return response;
	}
	
	
	@GetMapping(value = "/{codeFileControl}/audit")
	//		,
	//		produces = { "application/json" },
	//		consumes = { "application/json" })
	public ResponseEntity<List<FileControlDTO>> getAuditByFileControl(Authentication authentication,
			@PathVariable("codeFileControl") Long codeFileControl){
		logger.info("FileControlController - getAuditByFileControl - start");
		ResponseEntity<List<FileControlDTO>> response = null;
		List<FileControlDTO> result = null;
	
		try {
		
			result = fileControlService.getAuditByCodeFileControl(codeFileControl);
					
			response = new ResponseEntity<>(result, HttpStatus.OK);
		
		} catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			logger.error("ERROR in getAuditByFileControl: ", e);
		}
		
		logger.info("FileControlController - getAuditByFileControl - end");
		return response;
	}
	
	@PostMapping(value = "/{codeFileControl}/process")
	@ApiOperation(value = "Tramitacion de un archivo.")
	public ResponseEntity<FileControlDTO> tramitar (Authentication authentication,
			 @PathVariable("codeFileControl") Long codeFileControl){
		logger.info("FileControlController - tramitar - start");
		ResponseEntity<FileControlDTO> response = null;
		Boolean result = null;
		
		FileControlDTO resultFileControlDTO = null;
		
		try {
			
			//Dependiendo del tipo de fichero se realizara una tramitacion u otra:
			FileControlDTO fileControlDTO = fileControlService.getByCodeFileControl(codeFileControl);
			
			if (fileControlDTO!=null && fileControlDTO.getCodeFileType()!=null) {
				
				if (fileControlDTO.getCodeFileType().equals(EmbargosConstants.COD_TIPO_FICHERO_PETICION_INFORMACION_NORMA63)){
					
					//Si es de tipo Peticion de informacion --> tramitar Fichero Informacion (fase 2):
					result = fileControlService.tramitarFicheroInformacion(codeFileControl, authentication.getName());
				
				} else if (fileControlDTO.getCodeFileType().equals(EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_NORMA63)){
					
					//Si es de tipo DiligenciaFase3 de Embargos de Cuaderno63 --> tramitar Trabas de Cuaderno 63 (fase 4):
					result = fileControlService.tramitarTrabasCuaderno63(codeFileControl, authentication.getName());
					
				} else if (fileControlDTO.getCodeFileType().equals(EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_AEAT)){
					
					//Si es de tipo DiligenciaFase3 de Embargos de AEAT --> tramitar Trabas de AEAT (fase 4):
					result = fileControlService.tramitarTrabasAEAT(codeFileControl, authentication.getName());
				}
			}
			
			//Se obtiene el fileControl que se va a retornar:
			resultFileControlDTO = fileControlService.getByCodeFileControl(codeFileControl);
			
			if (result) {
				response = new ResponseEntity<>(resultFileControlDTO, HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(resultFileControlDTO, HttpStatus.BAD_REQUEST);
			}	
		
		} catch (Exception e) {
			
			response = new ResponseEntity<>(resultFileControlDTO, HttpStatus.BAD_REQUEST);
			
			logger.error("ERROR in tramitar: ", e);
		}
		
		logger.info("FileControlController - tramitar - end");
		return response;
		
	}
	
	//TODO: pendiente de ser deprecada (se utilizara updateFileControlStatus)
	@PostMapping(value = "/{codeFileControl}")
	@ApiOperation(value = "Cambia los datos del CONTROL_FICHERO segun lo especificado.")
	public ResponseEntity<String> updateFileControl(Authentication authentication,
																  @PathVariable("codeFileControl") Long codeFileControl,
																  @RequestBody FileControlDTO fileControl){
		logger.info("FileControlController - updateFileControl - start");
		ResponseEntity<String> response = null;
		boolean result = true;
		
		try {
			
			String userModif = authentication.getName();
			
			result = fileControlService.updateFileControl(codeFileControl,fileControl, userModif);
			
			if (result) {
				response = new ResponseEntity<>(HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}	
		
		} catch (Exception e) {
			
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
			logger.error("ERROR in updateFileControl: ", e);
		}
		
		logger.info("FileControlController - updateFileControl - end");
		return response;
	}
	
	
	@PostMapping(value = "/{codeFileControl}/status")
	@ApiOperation(value = "Cambia el estado de CONTROL_FICHERO.")
	public ResponseEntity<String> updateFileControlStatus(Authentication authentication,
	  		@PathVariable("codeFileControl") Long codeFileControl,
	  		@RequestBody FileControlStatusDTO fileControlStatus){
		logger.info("FileControlController - updateFileControlStatus - start");
		ResponseEntity<String> response = null;
		boolean result = false;

		try {

			if (fileControlStatus!=null) {
				
				String userModif = authentication.getName();
				
				result = fileControlService.updateFileControlStatus(codeFileControl, fileControlStatus.getCode(), userModif);
			}
			
			
			if (result) {
				response = new ResponseEntity<>(HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {

			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

			logger.error("ERROR in updateFileControlStatus: ", e);
		}
		
		logger.info("FileControlController - updateFileControlStatus - end");
		return response;
	}
	
	
		
	@GetMapping(value = "/{codeFileControl}")
	@ApiOperation(value="Devuelve el detalle de un CONTROL_FICHERO.")
	public ResponseEntity<FileControlDTO> getByCodeFileControl(Authentication authentication,
			 @PathVariable("codeFileControl") Long codeFileControl){
		logger.info("FileControlController - getByCodeFileControl - start");
		ResponseEntity<FileControlDTO> response = null;
		FileControlDTO result = null;
		
		try {
		
			result = fileControlService.getByCodeFileControl(codeFileControl);
			response = new ResponseEntity<>(result, HttpStatus.OK);
		
		} catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			logger.error("ERROR in getByCodeFileControl: ", e);
		}	
			
		logger.info("FileControlController - getByCodeFileControl - end");
		return response;
	}

	@PostMapping("/reports/files")
	public ResponseEntity<InputStreamResource> generarReportLista(
			@RequestParam(name = "codTipoFichero", required = false) Integer[] codTipoFichero,
			@RequestParam(name = "codEstado", required = false) Integer codEstado,
			@RequestParam(name = "isPending", required = false) boolean isPending,
			@RequestBody ReportParamsDTO reportParams) throws Exception {

		logger.info("FileControlController - generarReportLista - start");
		DownloadReportFile.setTempFileName("reportList");

		DownloadReportFile.setFileTempPath(pdfSavedPath);

		try {

			DownloadReportFile.writeFile(fileControlService.generarReporteListado(codTipoFichero, codEstado, isPending,
					reportParams.getFechaInicio(), reportParams.getFechaFin()));

			logger.info("FileControlController - generarReportLista - end");
			return DownloadReportFile.returnToDownloadFile();
		} catch (Exception e) {
			logger.error("Error in generarReportLista", e);
			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
