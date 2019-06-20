package es.commerzbank.ice.embargos.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.commerzbank.ice.embargos.domain.dto.FileControlDTO;
import es.commerzbank.ice.embargos.domain.dto.FileControlFiltersDTO;
import es.commerzbank.ice.embargos.domain.dto.PetitionDTO;
import es.commerzbank.ice.embargos.service.FileControlService;


@RestController
@RequestMapping(value = "/filecontrol")
public class FileControlController {

	private static final Logger LOG = LoggerFactory.getLogger(FileControlController.class);

	@Autowired
	private FileControlService fileControlService;
	
	@PostMapping(value = "/filter",
			produces = { "application/json" },
			consumes = { "application/json" })
	public ResponseEntity<Page<FileControlDTO>> fileSearch(@RequestHeader(value="token",required=true) String token,
														   @RequestBody FileControlFiltersDTO fileControlFiltersDTO,
														   Pageable pageable){
		
		ResponseEntity<Page<FileControlDTO>> response = null;
		Page<FileControlDTO> result = null;
		
		result = fileControlService.fileSearch(fileControlFiltersDTO, pageable);
		response = new ResponseEntity<>(result, HttpStatus.OK);
		
		return response;
	}
	
	
	@GetMapping(value = "/{codeFileControl}")
	public ResponseEntity<FileControlDTO> getByCodeFileControl(@RequestHeader(value="token",required=true) String token,
			  												   @PathVariable("codeFileControl") Long codeFileControl){
		FileControlDTO result = null;
		
		ResponseEntity<FileControlDTO> response = null;

		try {
		
			result = fileControlService.getByCodeFileControl(codeFileControl);

			response = new ResponseEntity<>(result, HttpStatus.OK);
			
		} catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			LOG.error("ERROR: ".concat(e.getLocalizedMessage()));
		}
		
		return response;
	}
	
	@GetMapping(value = "/{codeFileControl}/peticionesinformacion")
	public ResponseEntity<List<PetitionDTO>> getPeticionesInformacionByCodeFileControl(@RequestHeader(value="token",required=true) String token,
			  												   @PathVariable("codeFileControl") Long codeFileControl){
		List<PetitionDTO> result = null;
		
		ResponseEntity<List<PetitionDTO>> response = null;

		try {
		
			result = fileControlService.getPeticionesInformacionByCodeFileControl(codeFileControl);

			response = new ResponseEntity<>(result, HttpStatus.OK);
			
		} catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			LOG.error("ERROR: ".concat(e.getLocalizedMessage()));
		}
		
		return response;
	}
}
