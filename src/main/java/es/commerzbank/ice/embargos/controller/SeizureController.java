package es.commerzbank.ice.embargos.controller;

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

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/seizure")  
public class SeizureController {
	
	private static final Logger LOG = LoggerFactory.getLogger(SeizureController.class);
	
	@Value("${commerzbank.jasper.temp}")
	private String pdfSavedPath;
	
	@Autowired
	private SeizureService seizureService;
	
	
	
	@GetMapping("/{cod_embargo}/justificante")
	public ResponseEntity<InputStreamResource> generarJustificanteEmbargo(@PathVariable("cod_embargo") Integer codEmbargo) {
		
		DownloadReportFile.setTempFileName("justificanteReport");
		
		DownloadReportFile.setFileTempPath(pdfSavedPath);
		
		LOG.info("SeizureController");
		
		try {

			//seizure service falta
			DownloadReportFile.writeFile(seizureService.generateJustificanteEmbargo(codEmbargo));

			return DownloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			LOG.error("Error in justificanteReport", e);
			System.out.println(e);

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
