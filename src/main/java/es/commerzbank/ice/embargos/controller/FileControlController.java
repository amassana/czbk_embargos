package es.commerzbank.ice.embargos.controller;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import es.commerzbank.ice.comun.lib.security.Permissions;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.embargos.domain.dto.*;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.FileControlStatusService;
import es.commerzbank.ice.embargos.service.FileTypeService;
import es.commerzbank.ice.embargos.utils.DownloadReportFile;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.OfficeUtils;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/filecontrol")
public class FileControlController {

	private static final Logger logger = LoggerFactory.getLogger(FileControlController.class);

	@Autowired
	private FileControlService fileControlService;
	
	@Autowired
	private FileTypeService fileTypeService;
	
	@Autowired
	private FileControlStatusService fileControlStatusService;

	@Autowired
	private GeneralParametersService generalParametersService;

	@Autowired
	private FileControlMapper fileControlMapper;

	@Autowired
	private OfficeUtils officeUtils;
	
	@PostMapping(value = "/export")
	public void exportCSV(Authentication authentication, @RequestBody FileControlFiltersDTO fileControlFilters, HttpServletResponse response)
			throws Exception
	{
		try {
	        //set file name and content type
	        response.setContentType("text/csv");
	        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
	                "attachment; filename=\"" + "filecontrol.csv" + "\"");

			response.getWriter().println("sep=,");
	
	        //create a csv writer
	        StatefulBeanToCsv<FileControlCsvDTO> writer = new StatefulBeanToCsvBuilder<FileControlCsvDTO>(response.getWriter())
					.withApplyQuotesToAll(true)
					.withEscapechar('"')
					.withQuotechar('"')
					.withSeparator(',')
					.withThrowExceptions(true)
					.withOrderedResults(false)
	                .build();
	
	        //write all to csv file
			Page<FileControlDTO> list = fileControlService.fileSearch(fileControlFilters, Pageable.unpaged());
			if (list!=null) {
				List<FileControlCsvDTO> listFileControlCsv = fileControlMapper.toFileControlCsvDTO(list.getContent());
		        writer.write(listFileControlCsv);
			}
			 
		} catch(Exception e) {
			logger.error("Error - FileControlController - export", e);
		}
	}
	
	@PostMapping(value = "/filter",
			produces = { "application/json" },
			consumes = { "application/json" })
	@ApiOperation(value="Busca en CONTROL_FICHERO las entradas que cumplan el filtro especificado.")
	public ResponseEntity<Page<FileControlDTO>> filter(Authentication authentication,
													   @RequestBody FileControlFiltersDTO fileControlFilters,
													   Pageable pageable){
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
			
			response = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			
			logger.error("ERROR in filecontrol/filter: ", e);
		}	

		return response;
	}
	
	@GetMapping(value = "/filetype")
	@ApiOperation(value="Devuelve la lista de tipos de ficheros admitidos en TIPO_FICHERO.")
	public ResponseEntity<List<FileControlTypeDTO>> getFileTypeList(Authentication authentication){
		ResponseEntity<List<FileControlTypeDTO>> response = null;
		List<FileControlTypeDTO> result = null;
		
		result = fileTypeService.listAllFileType();
		response = new ResponseEntity<>(result, HttpStatus.OK);

		return response;
	}

	
	@GetMapping(value = "/filetype/{idFileType}/status")
	@ApiOperation(value="Devuelve la lista de estados para un determinado tipo de archivo de ESTADO_CTRLFICHERO.")
	public ResponseEntity<List<FileControlStatusDTO>> getFileTypeStatusList(Authentication authentication,
			@PathVariable("idFileType") Long idFileType){
		ResponseEntity<List<FileControlStatusDTO>> response = null;
		List<FileControlStatusDTO> result = null;
	
		try {
		
		result = fileControlStatusService.listFileControlStatusByFileType(idFileType);
		response = new ResponseEntity<>(result, HttpStatus.OK);
		
		} catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			
			logger.error("ERROR in getFileTypeStatusList: ", e);
		}
		return response;
	}
	
	
	@GetMapping(value = "/{codeFileControl}/audit")
	//		,
	//		produces = { "application/json" },
	//		consumes = { "application/json" })
	public ResponseEntity<List<FileControlDTO>> getAuditByFileControl(Authentication authentication,
			@PathVariable("codeFileControl") Long codeFileControl){
		ResponseEntity<List<FileControlDTO>> response = null;
		List<FileControlDTO> result = null;
	
		try {
		
			result = fileControlService.getAuditByCodeFileControl(codeFileControl);
					
			response = new ResponseEntity<>(result, HttpStatus.OK);
		
		} catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			
			logger.error("ERROR in getAuditByFileControl: ", e);
		}

		return response;
	}
	
	@PostMapping(value = "/{codeFileControl}/process")
	@ApiOperation(value = "Tramitacion de un archivo.")
	public ResponseEntity<FileControlDTO> tramitar (Authentication authentication,
			 @PathVariable("codeFileControl") Long codeFileControl){
		logger.info("FileControlController - tramitar "+ codeFileControl);
		ResponseEntity<FileControlDTO> response = null;
		Boolean result = false;
		
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

			logger.info("FileControlController - tramitar "+ codeFileControl +" "+ result);
		
		} catch (Exception e) {
			
			response = new ResponseEntity<>(resultFileControlDTO, HttpStatus.INTERNAL_SERVER_ERROR);
			
			logger.error("ERROR in tramitar: ", e);
		}

		return response;
	}
	
	//TODO: pendiente de ser deprecada (se utilizara updateFileControlStatus)
	@PostMapping(value = "/{codeFileControl}")
	@ApiOperation(value = "Cambia los datos del CONTROL_FICHERO segun lo especificado.")
	public ResponseEntity<String> updateFileControl(Authentication authentication,
																  @PathVariable("codeFileControl") Long codeFileControl,
																  @RequestBody FileControlDTO fileControl){
		logger.info("FileControlController - updateFileControl "+ codeFileControl);
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
			
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			
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
		logger.info("FileControlController - updateFileControlStatus - "+ codeFileControl +" "+ fileControlStatus.getCode());
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

			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

			logger.error("ERROR in updateFileControlStatus: ", e);
		}

		return response;
	}

	@GetMapping(value = "/{codeFileControl}")
	@ApiOperation(value="Devuelve el detalle de un CONTROL_FICHERO.")
	public ResponseEntity<FileControlDTO> getByCodeFileControl(Authentication authentication,
			 @PathVariable("codeFileControl") Long codeFileControl)
	{
		ResponseEntity<FileControlDTO> response = null;
		FileControlDTO result = null;
		
		try {
		
			result = fileControlService.getByCodeFileControl(codeFileControl);
			response = new ResponseEntity<>(result, HttpStatus.OK);
		
		} catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			
			logger.error("ERROR in getByCodeFileControl: ", e);
		}	

		return response;
	}

	@GetMapping(value = "/{codeFileControl}/download")
	public ResponseEntity<Resource> download(Authentication authentication,
			 @PathVariable("codeFileControl") Long codeFileControl)
	{
		ResponseEntity<Resource> response = null;
		FileControlDTO result = null;
		
		try {
			result = fileControlService.getByCodeFileControl(codeFileControl);
			if (result != null) {
				try {
					File file = new File(result.getRutaFichero());
					HttpHeaders header = new HttpHeaders();
			        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + result.getFileName());
			        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
			        header.add("Pragma", "no-cache");
			        header.add("Expires", "0");
					
			        Path path = Paths.get(file.getAbsolutePath());
			        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
			        
			        response = ResponseEntity.ok()
			                .headers(header)
			                .contentLength(file.length())
			                .contentType(MediaType.parseMediaType("text/plain"))
			                .body(resource);
				}
				catch (Exception e){
					logger.error("FileControlController - download - error", e);
					response = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
				}
		        
			} else {
				response = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			}
			
		} catch (Exception e) {
			
			response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			
			logger.error("ERROR in download: ", e);
		}

		return response;
	}
	
	@PostMapping("/reports/files")
	public ResponseEntity<InputStreamResource> generateFileControlReport(Authentication authentication,
			//@RequestParam(name = "codTipoFichero", required = false) Integer[] codTipoFichero,
			//@RequestParam(name = "codEstado", required = false) Integer codEstado,
			//@RequestParam(name = "isPending", required = false) boolean isPending,
			//@RequestBody ReportParamsDTO reportParams,
			@RequestBody FileControlFiltersDTO fileControlFilters
	) throws Exception
	{
		try {
			DownloadReportFile downloadReportFile = new DownloadReportFile();
			downloadReportFile.setTempFileName("file-control");

			downloadReportFile.setFileTempPath(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_JASPER_TEMP));

			String oficina = officeUtils.getLocalidadUsuario(authentication);

			byte[] data = fileControlService.generateFileControl(fileControlFilters, oficina);

			downloadReportFile.writeFile(data);

			return downloadReportFile.returnToDownloadFile();
		} catch (Exception e) {
			logger.error("Error in generarReportLista", e);
			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}

