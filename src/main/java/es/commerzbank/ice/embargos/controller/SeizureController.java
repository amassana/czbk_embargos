package es.commerzbank.ice.embargos.controller;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.commerzbank.ice.embargos.service.SeizureService;
import es.commerzbank.ice.utils.DownloadReportFile;
import io.swagger.annotations.ApiOperation;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/seizure")
public class SeizureController {

	private static final Logger LOG = LoggerFactory.getLogger(SeizureController.class);

	@Value("${commerzbank.jasper.temp}")
	private String pdfSavedPath;

	@Autowired
	private SeizureService seizureService;

	@GetMapping("/notification/{idSeizure}/report")
	@ApiOperation(value = "Devuelve un justificante de embargo")
	public ResponseEntity<InputStreamResource> generarJustificanteEmbargo(
			@PathVariable("idSeizure") Integer idSeizure) {

		DownloadReportFile.setTempFileName("justificanteReport");

		DownloadReportFile.setFileTempPath(pdfSavedPath);

		try {

			// seizure service falta
			DownloadReportFile.writeFile(seizureService.generateJustificanteEmbargo(idSeizure));

			return DownloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			LOG.error("Error in justificanteReport", e);

			if (e.getMessage() == null) {
				return new ResponseEntity<InputStreamResource>(HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/summary/fileControl/{fileControl}/requestReport")
	@ApiOperation(value = "Devuelve un fichero de resumen trabas fase 3")
	public ResponseEntity<InputStreamResource> generarResumentTrabaF3(@PathVariable("fileControl") Integer codControlFichero) {
		DownloadReportFile.setTempFileName("resumenTrabasReportF3");

		DownloadReportFile.setFileTempPath(pdfSavedPath);

		try {

			// seizure service falta
			DownloadReportFile.writeFile(seizureService.generarResumenTrabasF3(codControlFichero));

			return DownloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			LOG.error("Error in resumenTrabas", e);

			if (e.getMessage() == null) {
				return new ResponseEntity<InputStreamResource>(HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/summary/fileControl/{fileControl}/seizuresReport")
	@ApiOperation(value = "Devuelve un fichero de resumen trabas fase 4")
	public ResponseEntity<InputStreamResource> generarResumentTrabasF4(
			@PathVariable("fileControl") Integer codControlFichero) {

		DownloadReportFile.setTempFileName("resumenTrabasReportF4");

		DownloadReportFile.setFileTempPath(pdfSavedPath);

		try {

			// seizure service falta
			DownloadReportFile.writeFile(seizureService.generarResumenTrabasF4(codControlFichero));

			return DownloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			LOG.error("Error in resumenTrabas", e);

			if (e.getMessage() == null) {
				return new ResponseEntity<InputStreamResource>(HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// mover a otro controller
	@GetMapping("/anexo/{cod_usuario}/{cod_traba}/{num_anexo}")
	public ResponseEntity<InputStreamResource> generarAnexo(@PathVariable("cod_usuario") BigDecimal cod_usuario,
			@PathVariable("cod_traba") BigDecimal cod_traba, @PathVariable("num_anexo") Integer num_anexo)
			throws Exception {

		switch (num_anexo) {
		case 1:
			return downloadAnexo(cod_usuario, cod_traba, num_anexo);
		case 2:
			return downloadAnexo(cod_usuario, cod_traba, num_anexo);
		case 3:
			return downloadAnexo(cod_usuario, cod_traba, num_anexo);
		default:
			LOG.info("Error in anexoReport", "error descargando anexos");

		}

		return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ResponseEntity<InputStreamResource> downloadAnexo(BigDecimal cod_usuario, BigDecimal cod_traba,
			Integer num_anexo) {

		DownloadReportFile.setTempFileName("anexoReport" + num_anexo);

		DownloadReportFile.setFileTempPath(pdfSavedPath);

		try {

			DownloadReportFile.writeFile(seizureService.generarAnexo(cod_usuario, cod_traba, num_anexo));

			return DownloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			LOG.error("Error in anexoReport", e);

			if (e.getMessage() == null) {
				return new ResponseEntity<InputStreamResource>(HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
