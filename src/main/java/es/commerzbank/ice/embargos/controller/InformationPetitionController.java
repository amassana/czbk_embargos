package es.commerzbank.ice.embargos.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.commerzbank.ice.embargos.domain.dto.InformationPetitionDTO;
import es.commerzbank.ice.embargos.service.InformationPetitionService;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/informationpetition")
public class InformationPetitionController {

	private static final Logger LOG = LoggerFactory.getLogger(InformationPetitionController.class);
	
	@Autowired
	private InformationPetitionService informationPetitionService;
	
	@GetMapping(value = "/{codePetition}")
	public ResponseEntity<InformationPetitionDTO> getByCodePetition(@RequestHeader(value="token",required=true) String token,
			  												   @PathVariable("codePetition") Long codePetition){
		InformationPetitionDTO result = null;
		
		ResponseEntity<InformationPetitionDTO> response = null;

		try {
		
			result = informationPetitionService.getByCodePetition(codePetition);

			response = new ResponseEntity<>(result, HttpStatus.OK);
			
		} catch (Exception e) {
			
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			
			LOG.error("ERROR: ".concat(e.getLocalizedMessage()));
		}
		
		return response;
	}
}
