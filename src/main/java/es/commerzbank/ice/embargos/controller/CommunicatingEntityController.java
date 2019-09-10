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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.commerzbank.ice.comun.lib.security.Permissions;
import es.commerzbank.ice.embargos.domain.dto.CommunicatingEntity;
import es.commerzbank.ice.embargos.domain.dto.Representative;
import es.commerzbank.ice.embargos.service.CommunicatingEntityService;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/communicatingEntity")
public class CommunicatingEntityController {
	private static final Logger logger = LoggerFactory.getLogger(CommunicatingEntityController.class);
	
	@Autowired
	private CommunicatingEntityService service;
	
	@PostMapping(value = "",
	        produces = { "application/json" }, 
	        consumes = { "application/json" })
	public ResponseEntity<Void> create(Authentication authentication, @RequestBody CommunicatingEntity communicatingEntity) {
		ResponseEntity<Void> response = null;
		boolean result = true;
		
		if (!Permissions.hasPermission(authentication, "ui.impuestos")) {
			logger.info("CommunicatingEntityController - create - forbidden");
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
		if (communicatingEntity != null) {
			result = service.createUpdateCommunicatingEntity(communicatingEntity, authentication.getName());
			
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
	
	@PutMapping(value = "",
	        produces = { "application/json" }, 
	        consumes = { "application/json" })
	public ResponseEntity<Void> update(Authentication authentication,  @RequestBody CommunicatingEntity communicatingEntity) {
		ResponseEntity<Void> response = null;
		boolean result = true;
		
		if (!Permissions.hasPermission(authentication, "ui.impuestos")) {
			logger.info("CommunicatingEntityController - update - forbidden");
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
		if (communicatingEntity != null) {
			result = service.createUpdateCommunicatingEntity(communicatingEntity, authentication.getName());
			
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
	
	@DeleteMapping(value = "/{idCommunicatingEntity}",
	        produces = { "application/json" })
	public ResponseEntity<Void> delete(Authentication authentication, @PathVariable("idCommunicatingEntity") Long idCommunicatingEntity) {
		ResponseEntity<Void> response = null;
		boolean result = true;
		
		if (!Permissions.hasPermission(authentication, "ui.impuestos")) {
			logger.info("CommunicatingEntityController - delete - forbidden");
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
		if (idCommunicatingEntity != null && idCommunicatingEntity > 0) {
			result = service.deleteCommunicatingEntity(idCommunicatingEntity);
			
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
	
	@GetMapping(value = "/{idCommunicatingEntity}",
	        produces = { "application/json" })
	public ResponseEntity<CommunicatingEntity> view(Authentication authentication, @PathVariable("idCommunicatingEntity") Long idCommunicatingEntity) {
		ResponseEntity<CommunicatingEntity> response = null;
		CommunicatingEntity result = null;
		
		if (!Permissions.hasPermission(authentication, "ui.impuestos")) {
			logger.info("CommunicatingEntityController - view - forbidden");
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
		if (idCommunicatingEntity != null && idCommunicatingEntity > 0) {
			result = service.viewCommunicatingEntity(idCommunicatingEntity);
			
			response = new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}
		
		return response;
	}
	
	@PostMapping(value = "/filter",
			produces = {"application/json"})
	public ResponseEntity<Page<CommunicatingEntity>> filter(Authentication authentication, Pageable dataPage) {
		logger.info("CommunicatingEntityController - filter - start");
		ResponseEntity<Page<CommunicatingEntity>> response = null;
		Page<CommunicatingEntity> list = null;
		
		list = service.filter(dataPage);
		
		if (list != null) {
			if (list.getContent().size() > 0) {
				response = new ResponseEntity<>(list, HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(list, HttpStatus.NO_CONTENT);
			}
		} else {
			response = new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
		}
		
		logger.info("CommunicatingEntityController - filter - end");
		return response;
	}
	
	@GetMapping(value = "/listAll",
			produces = {"application/json"})
	public ResponseEntity<List<CommunicatingEntity>> listAll(Authentication authentication) {
		logger.info("CommunicatingEntityController - listAll - start");
		ResponseEntity<List<CommunicatingEntity>> response = null;
		List<CommunicatingEntity> list = null;
		
		list = service.listAll();
		
		if (list != null) {
			if (list.size() > 0) {
				response = new ResponseEntity<>(list, HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(list, HttpStatus.NO_CONTENT);
			}
		} else {
			response = new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
		}
		
		logger.info("CommunicatingEntityController - listAll - end");
		return response;
	}
}
