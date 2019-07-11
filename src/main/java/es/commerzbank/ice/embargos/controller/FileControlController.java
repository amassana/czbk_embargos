package es.commerzbank.ice.embargos.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.commerzbank.ice.comun.lib.security.Permissions;
import es.commerzbank.ice.embargos.domain.dto.FileControlDTO;
import es.commerzbank.ice.embargos.domain.dto.FileControlFiltersDTO;
import es.commerzbank.ice.embargos.domain.dto.FileControlStatusDTO;
import es.commerzbank.ice.embargos.domain.dto.FileControlTypeDTO;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.FileControlStatusService;
import es.commerzbank.ice.embargos.service.FileTypeService;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/filecontrol")
public class FileControlController {

	private static final Logger LOG = LoggerFactory.getLogger(FileControlController.class);

	@Autowired
	private FileControlService fileControlService;
	
	@Autowired
	private FileTypeService fileTypeService;
	
	@Autowired
	private FileControlStatusService fileControlStatusService;
		
	@PostMapping(value = "/filter",
			produces = { "application/json" },
			consumes = { "application/json" })
	public ResponseEntity<Page<FileControlDTO>> filter(Authentication authentication,
													   @RequestBody FileControlFiltersDTO fileControlFilters,
													   Pageable pageable){
		
		ResponseEntity<Page<FileControlDTO>> response = null;
		Page<FileControlDTO> result = null;
		
		try {
		
			if (!Permissions.hasPermission(authentication, "ui.embargos")) {
				LOG.info("FileControlController - fileSearch - forbidden");
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			
			result = fileControlService.fileSearch(fileControlFilters, pageable);
			response = new ResponseEntity<>(result, HttpStatus.OK);
		
		} catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			LOG.error("ERROR: ".concat(e.getLocalizedMessage()));
		}	
			
		return response;
	}
	
	@GetMapping(value = "/filetype")
	//		,
	//		produces = { "application/json" },
	//		consumes = { "application/json" })
	public ResponseEntity<List<FileControlTypeDTO>> getFileTypeList(@RequestHeader(value="token",required=true) String token){
		
		ResponseEntity<List<FileControlTypeDTO>> response = null;
		List<FileControlTypeDTO> result = null;
		
		result = fileTypeService.listAllFileType();
		response = new ResponseEntity<>(result, HttpStatus.OK);
		
		return response;
	}

	
	@GetMapping(value = "/filetype/{idFileType}/status")
	//		,
	//		produces = { "application/json" },
	//		consumes = { "application/json" })
	public ResponseEntity<List<FileControlStatusDTO>> getFileTypeStatusList(@RequestHeader(value="token",required=true) String token,
			@PathVariable("idFileType") Long idFileType){
		
		ResponseEntity<List<FileControlStatusDTO>> response = null;
		List<FileControlStatusDTO> result = null;
	
		try {
		
		result = fileControlStatusService.listFileControlStatusByFileType(idFileType);
		response = new ResponseEntity<>(result, HttpStatus.OK);
		
		} catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			LOG.error("ERROR: ".concat(e.getLocalizedMessage()));
		}
			
		return response;
	}
	
	
	@GetMapping(value = "/{codeFileControl}/audit")
	//		,
	//		produces = { "application/json" },
	//		consumes = { "application/json" })
	public ResponseEntity<List<FileControlDTO>> getAuditByFileControl(@RequestHeader(value="token",required=true) String token,
			@PathVariable("codeFileControl") Long codeFileControl){
		
		ResponseEntity<List<FileControlDTO>> response = null;
		List<FileControlDTO> result = null;
	
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
	
	@PostMapping(value = "/{codeFileControl}/process")
	public ResponseEntity<String> tramitar (@RequestHeader(value="token",required=true) String token,
			 @PathVariable("codeFileControl") String codeFileControl){
	
		ResponseEntity<String> response = null;
		boolean result = true;
		
		try {
			
			//TODO falta implementar el result:
			//result = petitionService.tramitarFicheroInformacion(codePetition);
			result = false;
			
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
	
	@PatchMapping(value = "/{codeFileControl}")
	//		,
	//		produces = { "application/json" },
	//		consumes = { "application/json" })
	public ResponseEntity<String> updateFileControl(@RequestHeader(value="token",required=true) String token,
																  @PathVariable("codeFileControl") Long codeFileControl,
																  @RequestBody FileControlDTO fileControl){
		
		ResponseEntity<String> response = null;
		boolean result = true;
		
		try {
			
			//TODO: implementar el result:
			result = false;
			
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
	
	
	///////////////MIRAR SI SE UTILIZARA:
	
//	@GetMapping(value = "/{codeFileControl}")
//	public ResponseEntity<FileControlDTO> getByCodeFileControl(@RequestHeader(value="token",required=true) String token,
//			 @PathVariable("codeFileControl") Long codeFileControl){
//		
//		ResponseEntity<FileControlDTO> response = null;
//		FileControlDTO result = null;
//		
//		try {
//		
//			result = fileControlService.getByCodeFileControl(codeFileControl);
//			response = new ResponseEntity<>(result, HttpStatus.OK);
//		
//		} catch (Exception e) {
//			
//			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
//			
//			LOG.error("ERROR: ".concat(e.getLocalizedMessage()));
//		}	
//			
//		return response;
//	}

}
