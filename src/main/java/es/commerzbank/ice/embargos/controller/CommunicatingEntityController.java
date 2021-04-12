package es.commerzbank.ice.embargos.controller;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import es.commerzbank.ice.comun.lib.security.Permissions;
import es.commerzbank.ice.embargos.domain.dto.CommunicatingEntity;
import es.commerzbank.ice.embargos.service.CommunicatingEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
		logger.debug("CommunicatingEntityController - create - start");
		ResponseEntity<Void> response = null;
		boolean result = true;
		
		if (!Permissions.hasPermission(authentication, "ui.impuestos")) {
			logger.debug("CommunicatingEntityController - create - forbidden");
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
		
		logger.debug("CommunicatingEntityController - create - end");
		return response;
	}
	
	@PutMapping(value = "",
	        produces = { "application/json" }, 
	        consumes = { "application/json" })
	public ResponseEntity<Void> update(Authentication authentication,  @RequestBody CommunicatingEntity communicatingEntity) {
		logger.debug("CommunicatingEntityController - update - start");
		ResponseEntity<Void> response = null;
		boolean result = true;
		
		if (!Permissions.hasPermission(authentication, "ui.impuestos")) {
			logger.debug("CommunicatingEntityController - update - forbidden");
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
		
		logger.debug("CommunicatingEntityController - update - end");
		return response;
	}
	
	@DeleteMapping(value = "/{idCommunicatingEntity}",
	        produces = { "application/json" })
	public ResponseEntity<Void> delete(Authentication authentication, @PathVariable("idCommunicatingEntity") Long idCommunicatingEntity) {
		logger.debug("CommunicatingEntityController - delete - start");
		ResponseEntity<Void> response = null;
		boolean result = true;
		
		if (!Permissions.hasPermission(authentication, "ui.impuestos")) {
			logger.debug("CommunicatingEntityController - delete - forbidden");
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
		if (idCommunicatingEntity != null && idCommunicatingEntity > 0) {
			result = service.deleteCommunicatingEntity(idCommunicatingEntity, authentication.getName());
			
			if (result) {
				response = new ResponseEntity<>(HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} else {
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		logger.debug("CommunicatingEntityController - delete - end");
		return response;
	}
	
	@GetMapping(value = "/{idCommunicatingEntity}",
	        produces = { "application/json" })
	public ResponseEntity<CommunicatingEntity> view(Authentication authentication, @PathVariable("idCommunicatingEntity") Long idCommunicatingEntity) {
		ResponseEntity<CommunicatingEntity> response = null;
		CommunicatingEntity result = null;
		
		if (!Permissions.hasPermission(authentication, "ui.impuestos")) {
			logger.debug("CommunicatingEntityController - view - forbidden");
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
	
	@PostMapping(value = "/export")
	public void exportCSV(Authentication authentication, HttpServletResponse response) throws Exception {
		try {
	        //set file name and content type
	        response.setContentType("text/csv");
	        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
	                "attachment; filename=\"" + "communicatingEntity.csv" + "\"");


			response.getWriter().println("sep=,");
	        //create a csv writer
	        StatefulBeanToCsv<CommunicatingEntity> writer = new StatefulBeanToCsvBuilder<CommunicatingEntity>(response.getWriter())
					.withApplyQuotesToAll(true)
					.withEscapechar('"')
					.withQuotechar('"')
					.withSeparator(',')
					.withThrowExceptions(true)
					.withOrderedResults(false)
	                .build();
	
	        //write all to csv file
			Page<CommunicatingEntity> list = service.filter(Pageable.unpaged());
	        writer.write(list.getContent());
	        
		} catch(Exception e) {
			logger.error("Error - CommunicatingEntityController - export", e);
		}
	}
	
	@PostMapping(value = "/filter",
			produces = {"application/json"})
	public ResponseEntity<Page<CommunicatingEntity>> filter(Authentication authentication, Pageable dataPage) {
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

		return response;
	}
	
	@GetMapping(value = "/listAll",
			produces = {"application/json"})
	public ResponseEntity<List<CommunicatingEntity>> listAll(Authentication authentication) {
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

		return response;
	}
}
