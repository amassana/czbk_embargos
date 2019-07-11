package es.commerzbank.ice.embargos.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.commerzbank.ice.embargos.domain.dto.PetitionCaseDTO;
import es.commerzbank.ice.embargos.service.InformationPetitionService;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/informationpetition")
public class InformationPetitionController {

//	private static final Logger LOG = LoggerFactory.getLogger(InformationPetitionController.class);
//	
//	@Autowired
//	private InformationPetitionService informationPetitionService;
//	
//	@GetMapping(value = "/{codeInformationPetition}")
//	public ResponseEntity<PetitionCaseDTO> getByCodeInformationPetition(@RequestHeader(value="token",required=true) String token,
//			  												   @PathVariable("codeInformationPetition") String codeInformationPetition){
//		PetitionCaseDTO result = null;
//		
//		ResponseEntity<PetitionCaseDTO> response = null;
//
//		try {
//		
//			result = informationPetitionService.getByCodeInformationPetition(codeInformationPetition);
//
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
//	
//	@PatchMapping(value = "/{codeInformationPetition}/{nif}/{revised}",
//			produces = { "application/json" }, 
//	        consumes = { "application/json" })
//	public ResponseEntity<String> saveSelectedAccounts(@RequestHeader(value="token",required=true) String token,
//			@PathVariable("codeInformationPetition") String codeInformationPetition,
//			@PathVariable("nif") String nif,
//			@PathVariable("revised") Boolean revised,
//			@RequestBody List<String> codeAccountList){
//		
//		
//		ResponseEntity<String> response = null;
//		boolean result = true;
//		
//		try {
//			
//			result = informationPetitionService.saveSelectedAccounts(codeInformationPetition, nif, revised, codeAccountList);
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
//			LOG.error("ERROR: ".concat(e.getLocalizedMessage()));
//		}
//		
//		return response;
//		
//	}
}
