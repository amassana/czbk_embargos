package es.commerzbank.ice.embargos.controller;

import es.commerzbank.ice.comun.lib.security.Permissions;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.service.SeizureSummaryService;
import es.commerzbank.ice.utils.DownloadReportFile;
import es.commerzbank.ice.embargos.domain.dto.OrderingEntity;
import es.commerzbank.ice.embargos.scheduled.Norma63Fase6;
import es.commerzbank.ice.embargos.service.OrderingEntityService;
import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/seizureSummary")
public class SeizureSummaryController {
	private static final Logger logger = LoggerFactory.getLogger(SeizureSummaryController.class);

	@Autowired
	private Norma63Fase6 norma63Fase6;
	
	@Value("${commerzbank.jasper.temp}")
	private String pdfSavedPath;

	@Autowired
	private SeizureSummaryService seizureSummaryService;

	@GetMapping(value = "/{codeFileControl}", produces = { "application/json" })
	public ResponseEntity<Void> createNorma63(Authentication authentication,
			@PathVariable("codeFileControl") Long codeFileControl) {
		logger.info("SeizureSummaryController - createNorma63 - start");
		ResponseEntity<Void> response = null;
		OrderingEntity result = null;

		if (!Permissions.hasPermission(authentication, "ui.impuestos")) {
			logger.info("SeizureSummaryController - createNorma63 - forbidden");
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

		if (codeFileControl != null) {
			try {
				norma63Fase6.generarFase6(codeFileControl);
				response = new ResponseEntity<>(HttpStatus.OK);
			} catch (Exception e) {
				logger.error("eizureSummaryController - createNorma63 - Error while generating norma 63 summary file",
						e);
				response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		logger.info("SeizureSummaryController - createNorma63 - end");

		return response;
	}

	@GetMapping("/transfer/{account_number}/order")
	@ApiOperation(value = "Devuelve un fichero de orden de tansferencia")
	private ResponseEntity<InputStreamResource> generateSeizureSummary(
			@PathVariable(name = "account_number") String accountNumber) {

		DownloadReportFile.setTempFileName("transferenceOrder");

		DownloadReportFile.setFileTempPath(pdfSavedPath);

		try {

			DownloadReportFile.writeFile(seizureSummaryService.generateSeizureSummaryReport(accountNumber));

			return DownloadReportFile.returnToDownloadFile();
  
		} catch (Exception e) {
			logger.error("Error in generateSeizureSummary", e);
			System.out.println(e);

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
