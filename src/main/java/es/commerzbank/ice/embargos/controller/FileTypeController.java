package es.commerzbank.ice.embargos.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.commerzbank.ice.embargos.domain.dto.FileTypeDTO;
import es.commerzbank.ice.embargos.service.FileTypeService;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/filetype")
public class FileTypeController {
	
	private static final Logger LOG = LoggerFactory.getLogger(FileTypeController.class);

	@Autowired
	private FileTypeService fileTypeService;
	
	@GetMapping(value = "")
	//		,
	//		produces = { "application/json" },
	//		consumes = { "application/json" })
	public ResponseEntity<List<FileTypeDTO>> fileTypeDTOAllGet(@RequestHeader(value="token",required=true) String token){
		
		ResponseEntity<List<FileTypeDTO>> response = null;
		List<FileTypeDTO> result = null;
		
		result = fileTypeService.listAllFileType();
		response = new ResponseEntity<>(result, HttpStatus.OK);
		
		return response;
	}
	
}
