package es.commerzbank.ice.embargos.controller;

import es.commerzbank.ice.embargos.domain.dto.AuditoriaEmbDto;
import es.commerzbank.ice.embargos.domain.dto.AuditoriaEmbFilter;
import es.commerzbank.ice.embargos.service.AuditoriaEmbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping(value = "/auditoria")
@RestController
public class AuditoriaEmbController {

	private static final Logger logger = LoggerFactory.getLogger(AuditoriaEmbController.class);
	
	@Autowired
	private AuditoriaEmbService auditoriaService;
		
	@PostMapping(value = "/filter")
	public ResponseEntity<Page<AuditoriaEmbDto>> filter(Authentication authentication, Pageable dataPage, @RequestBody AuditoriaEmbFilter parametros) {
		ResponseEntity<Page<AuditoriaEmbDto>> response = null;
		Page<AuditoriaEmbDto> result = null;
		
		try {
			if (parametros != null) {
				result = auditoriaService.listFilterAuditoria(parametros, dataPage);
				
				response = new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			}
		} catch(Exception e) {
			logger.error("Error - AuditoriaController - filter ", e);
			response = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}
	
	@GetMapping(value = "/{tabla}/{pkValor}")
	public ResponseEntity<Page<AuditoriaEmbDto>> viewAuditoria(@PathVariable("pkValor") String pkValor, @PathVariable("tabla") String tabla, Pageable dataPage) {
		ResponseEntity<Page<AuditoriaEmbDto>> response = null;
		Page<AuditoriaEmbDto> result = null;
		
		try {
			if (pkValor != null && tabla!=null) {
				result = auditoriaService.listFilterAuditoria(pkValor, tabla, dataPage);
				
				response = new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
			}
		} catch(Exception e) {
			logger.error("Error - AuditoriaController - viewAuditoria ", e);
			response = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}

	@GetMapping(value = "/{idAuditoria}")
	public ResponseEntity<AuditoriaEmbDto> view(@PathVariable("idAuditoria") Long idAuditoria) {
		ResponseEntity<AuditoriaEmbDto> response = null;
		AuditoriaEmbDto result = null;
		
		if (idAuditoria != null && idAuditoria > 0)  {
			result = auditoriaService.viewAuditoria(idAuditoria);
			if (result!=null) response = new ResponseEntity<>(result, HttpStatus.OK);
			else response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		} else {
			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
		}

		return response;
	}
	
	@PostMapping(value = "/allTables")
	public ResponseEntity<List<String>> allTables(Authentication authentication) {
		ResponseEntity<List<String>> response = null;
		List<String> result = null;
		
		try {
			result = auditoriaService.listAllTables();
			response = new ResponseEntity<>(result, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("Error - AuditoriaController - allTables ", e);
			response = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}
	
	@PostMapping(value = "/allUsers")
	public ResponseEntity<List<String>> allUsers(Authentication authentication) {
		ResponseEntity<List<String>> response = null;
		List<String> result = null;
		
		try {
			result = auditoriaService.listAllUsers();
			response = new ResponseEntity<>(result, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("Error - AuditoriaController - allUsers ", e);
			response = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}
}
