package es.commerzbank.ice.embargos.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import es.commerzbank.ice.comun.lib.security.Permissions;
import es.commerzbank.ice.embargos.domain.dto.Representative;
import es.commerzbank.ice.embargos.service.RepresentativeService;

@CrossOrigin(origins = "*")
@RestController
public class RepresentativeController {
	private static final Logger logger = LoggerFactory.getLogger(RepresentativeController.class);
	
	@Autowired
	private RepresentativeService representativeService;
	
	
	@PostMapping(value = "/representative",
	        produces = { "application/json" }, 
	        consumes = { "application/json" })
	public ResponseEntity<Void> create(Authentication authentication, @RequestBody Representative representative) {
		ResponseEntity<Void> response = null;
		boolean result = true;
		
		if (!Permissions.hasPermission(authentication, "ui.impuestos")) {
			logger.info("RepresentativeController - create - forbidden");
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
		if (representative != null) {
			result = representativeService.createUpdateRepresentative(representative, authentication.getName());
			
			if (result) {
				response = new ResponseEntity<>(HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} else {
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
	
	@PutMapping(value = "/representative",
	        produces = { "application/json" }, 
	        consumes = { "application/json" })
	public ResponseEntity<Void> update(Authentication authentication,  @RequestBody Representative representative) {
		ResponseEntity<Void> response = null;
		boolean result = true;
		
		if (!Permissions.hasPermission(authentication, "ui.impuestos")) {
			logger.info("RepresentativeController - update - forbidden");
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
		if (representative != null) {
			result = representativeService.createUpdateRepresentative(representative, authentication.getName());
			
			if (result) {
				response = new ResponseEntity<>(HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} else {
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
	
	@DeleteMapping(value = "/representative/{idRepresentative}",
	        produces = { "application/json" })
	public ResponseEntity<Void> delete(Authentication authentication, @PathVariable("idRepresentative") Long idRepresentative) {
		ResponseEntity<Void> response = null;
		boolean result = true;
		
		if (!Permissions.hasPermission(authentication, "ui.impuestos")) {
			logger.info("RepresentativeController - delete - forbidden");
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
		if (idRepresentative != null && idRepresentative > 0) {
			result = representativeService.deleteRepresentative(idRepresentative);
			
			if (result) {
				response = new ResponseEntity<>(HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} else {
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
	
	@GetMapping(value = "/representative/{idRepresentative}",
	        produces = { "application/json" })
	public ResponseEntity<Representative> view(Authentication authentication, @PathVariable("idRepresentative") Long idRepresentative) {
		ResponseEntity<Representative> response = null;
		Representative result = null;
		
		if (!Permissions.hasPermission(authentication, "ui.impuestos")) {
			logger.info("RepresentativeController - view - forbidden");
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
		if (idRepresentative != null && idRepresentative > 0) {
			result = representativeService.viewRepresentative(idRepresentative);
			
			response = new ResponseEntity<Representative>(result, HttpStatus.OK);
		} else {
			response = new ResponseEntity<Representative>(result, HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
	
	@PostMapping(value = "/representative/filter",
			produces = {"application/json"})
	public ResponseEntity<Page<Representative>> filter(Authentication authentication, Pageable dataPage, @RequestBody Map<String, Object> parametros) {
		logger.info("RepresentativeController - listAll - start");
		ResponseEntity<Page<Representative>> response = null;
		Page<Representative> list = null;
		
		list = representativeService.filter(parametros, dataPage);
		
		if (list != null) {
			if (list.getContent().size() > 0) {
				response = new ResponseEntity<Page<Representative>>(list, HttpStatus.OK);
			} else {
				response = new ResponseEntity<Page<Representative>>(list, HttpStatus.NO_CONTENT);
			}
		} else {
			response = new ResponseEntity<Page<Representative>>(list, HttpStatus.BAD_REQUEST);
		}
		
		logger.info("RepresentativeController - listAll - end");
		return response;
	}
	
	@GetMapping(value = "/representative/listAll",
			produces = {"application/json"})
	public ResponseEntity<List<Representative>> listAll(Authentication authentication) {
		logger.info("RepresentativeController - listAll - start");
		ResponseEntity<List<Representative>> response = null;
		List<Representative> list = null;
		
		list = representativeService.listAll();
		
		if (list != null) {
			if (list.size() > 0) {
				response = new ResponseEntity<List<Representative>>(list, HttpStatus.OK);
			} else {
				response = new ResponseEntity<List<Representative>>(list, HttpStatus.NO_CONTENT);
			}
		} else {
			response = new ResponseEntity<List<Representative>>(list, HttpStatus.BAD_REQUEST);
		}
		
		logger.info("RepresentativeController - listAll - end");
		return response;
	}
}
