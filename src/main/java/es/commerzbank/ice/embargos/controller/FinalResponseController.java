package es.commerzbank.ice.embargos.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.commerzbank.ice.embargos.domain.dto.FinalResponseDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.service.FinalResponseService;
import io.swagger.annotations.ApiOperation;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/finalResponse")
public class FinalResponseController {
	private static final Logger logger = LoggerFactory.getLogger(FinalResponseController.class);
	
	@Autowired
	private FinalResponseService finalResponseService;
	
	@GetMapping(value = "/{codeFileControl}")
	@ApiOperation(value = "Devuelve la lista de casos de levamtamientos")
	public ResponseEntity<List<FinalResponseDTO>> getFinalResponseListByCodeFileControl(Authentication authentication,
			@PathVariable("codeFileControl") Long codeFileControl) {
		logger.info("LiftingController - getFinalResponseListByCodeFileControl - start");
		ResponseEntity<List<FinalResponseDTO>> response = null;
		List<FinalResponseDTO> result = null;

		try {

			ControlFichero controlFichero = new ControlFichero();
			controlFichero.setCodControlFichero(codeFileControl);

			result = finalResponseService.getAllByControlFichero(controlFichero);

			response = new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {

			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

			logger.error("ERROR in getFinalResponseListByCodeFileControl: ", e);
		}

		logger.info("LiftingController - getFinalResponseListByCodeFileControl - end");
		return response;
	}
}
