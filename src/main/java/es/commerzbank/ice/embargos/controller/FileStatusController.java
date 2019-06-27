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

import es.commerzbank.ice.embargos.domain.dto.FileStatusDTO;
import es.commerzbank.ice.embargos.service.FileStatusService;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/filestatus")
public class FileStatusController {

	private static final Logger LOG = LoggerFactory.getLogger(FileStatusController.class);
	
	@Autowired
	private FileStatusService fileStatusService;
	
	@GetMapping(value = "")
	//		,
	//		produces = { "application/json" },
	//		consumes = { "application/json" })
	public ResponseEntity<List<FileStatusDTO>> fileStatusDTOAllGet(@RequestHeader(value="token",required=true) String token){
		
		ResponseEntity<List<FileStatusDTO>> response = null;
		List<FileStatusDTO> result = null;
		
		result = fileStatusService.listAllFileStatus();
		response = new ResponseEntity<>(result, HttpStatus.OK);
		
		return response;
	}
}
