package es.commerzbank.ice.embargos.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.commerzbank.ice.embargos.domain.dto.PetitionDTO;
import es.commerzbank.ice.embargos.service.PetitionService;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/petition")
public class PetitionController {

	private static final Logger LOG = LoggerFactory.getLogger(PetitionController.class);

	@Autowired
	private PetitionService petitionService;
	
	@GetMapping(value = "/filecontrol/{codeFileControl}")
	public ResponseEntity<Page<PetitionDTO>> getByCodeFileControl(@RequestHeader(value="token",required=true) String token,
			  												   @PathVariable("codeFileControl") Long codeFileControl, Pageable pageable){
		ResponseEntity<Page<PetitionDTO>> response = null;
		Page<PetitionDTO> result = null;

		try {
		
			result = petitionService.getByCodeFileControl(codeFileControl, pageable);

			response = new ResponseEntity<>(result, HttpStatus.OK);
			
		} catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			LOG.error("ERROR: ".concat(e.getLocalizedMessage()));
		}
		
		return response;
	}
	
}
