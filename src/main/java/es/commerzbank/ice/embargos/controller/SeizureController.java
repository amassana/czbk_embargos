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
import es.commerzbank.ice.utils.NumberToLetterConverter;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/seizure")
public class SeizureController {

	private static final Logger LOG = LoggerFactory.getLogger(SeizureController.class);

	@Value("${commerzbank.jasper.temp}")
	private String pdfSavedPath;

	@Autowired
	private SeizureService seizureService;

	@GetMapping("/{idSeizure}/case/report")
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

	@GetMapping("/propertyLien/fileControl/{fileControl}/report")
	public ResponseEntity<InputStreamResource> generarResumentTrabas(
			@PathVariable("fileControl") Integer codControlFichero) {
		
		

		DownloadReportFile.setTempFileName("resumenTrabasReport");

		DownloadReportFile.setFileTempPath(pdfSavedPath);

		try {

			// seizure service falta
			DownloadReportFile.writeFile(seizureService.generarResumenTrabas(codControlFichero));

			return DownloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			LOG.error("Error in justificanteReport", e);
			System.out.println(e);

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@GetMapping("/propertyLien/anexo/{importe}")
	public ResponseEntity<InputStreamResource> generarAnexo(@PathVariable("importe") BigDecimal importe) {
		
		NumberToLetterConverter ntlc = new NumberToLetterConverter();
		
		System.out.println(ntlc.Convertir(String.valueOf(importe), true));
		
		
		return null;
	}
}