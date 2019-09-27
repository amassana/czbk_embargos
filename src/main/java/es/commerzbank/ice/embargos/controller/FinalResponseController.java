package es.commerzbank.ice.embargos.controller;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.commerzbank.ice.embargos.config.OracleDataSourceEmbargosConfig;
import es.commerzbank.ice.embargos.domain.dto.FinalResponseDTO;
import es.commerzbank.ice.embargos.domain.dto.LiftingDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.service.FinalResponseService;
import es.commerzbank.ice.utils.DownloadReportFile;
import io.swagger.annotations.ApiOperation;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/finalResponse")
public class FinalResponseController {
	private static final Logger logger = LoggerFactory.getLogger(FinalResponseController.class);

	@Autowired
	private FinalResponseService finalResponseService;

	@Value("${commerzbank.jasper.temp}")
	private String pdfSavedPath;

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

	@GetMapping(value = "/{codeFileControl}/finalResponseCase/{codeFinalResponse}/bankAccounts")
	@ApiOperation(value = "Devuelve la lista de cuentas disponibles para el caso indicado de levantamiento.")
	public ResponseEntity<FinalResponseDTO> getBankAccountListByCodeFileControlAndFinalResponse(
			Authentication authentication, @PathVariable("codeFileControl") Long codeFileControl,
			@PathVariable("codeFinalResponse") Long codeFinalResponse) {
		logger.info("LiftingController - getBankAccountListByCodeFileControlAndFinalResponse - start");
		ResponseEntity<FinalResponseDTO> response = null;
		FinalResponseDTO result = null;

		try {

			result = finalResponseService.AddBankAccountList(codeFileControl, codeFinalResponse);

			response = new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {

			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

			logger.error("ERROR in getBankAccountListByCodeFileControlAndFinalResponse: ", e);
		}

		logger.info("LiftingController - getBankAccountListByCodeFileControlAndFinalResponse - end");
		return response;
	}

	// mover a otro controller
	@GetMapping("/anexo/{cod_usuario}/{cod_traba}/{num_anexo}/report")
	public ResponseEntity<InputStreamResource> generarAnexo(@PathVariable("cod_usuario") BigDecimal cod_usuario,
			@PathVariable("cod_traba") BigDecimal cod_traba, @PathVariable("num_anexo") Integer num_anexo)
			throws Exception {
		logger.info("SeizureController - generarAnexo - start");

		ResponseEntity<InputStreamResource> response = downloadAnexo(cod_usuario, cod_traba, num_anexo);

		logger.info("SeizureController - generarAnexo - end");

		return response;
	}

	private ResponseEntity<InputStreamResource> downloadAnexo(BigDecimal cod_usuario, BigDecimal cod_traba,
			Integer num_anexo) {
		logger.info("SeizureController - downloadAnexo - start");
		
		DownloadReportFile.setTempFileName("TGSSAnexo" + num_anexo);

		DownloadReportFile.setFileTempPath(pdfSavedPath);

		try {

			DownloadReportFile.writeFile(finalResponseService.generarAnexo(cod_usuario, cod_traba, num_anexo));

			logger.info("SeizureController - downloadAnexo - end");
			return DownloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in downloadAnexo", e);

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{fileControl}/report")
	@ApiOperation(value = "Devuelve el fichero de respuesta final de embargos")
	public ResponseEntity<InputStreamResource> generarRespuestaFinalEmbargo(
			@PathVariable("fileControl") Integer codFileControl) {

		DownloadReportFile.setTempFileName("f6_finalization");

		DownloadReportFile.setFileTempPath(pdfSavedPath);

		try {

			DownloadReportFile.writeFile(finalResponseService.generarRespuestaFinalEmbargo(codFileControl));

			return DownloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in generarRespuestaFinalEmbargo", e);

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/cgpj/{cod_traba}/report")
	@ApiOperation(value = "Devuelve el fichero de solicitud de ejecucion")
	public ResponseEntity<InputStreamResource> generatePaymentLetterCGPJ(
			@PathVariable("cod_traba") String cod_solicitud_ejecucion) {

		DownloadReportFile.setTempFileName("payment-letter-CGPJ");

		DownloadReportFile.setFileTempPath(pdfSavedPath);

		try {

			DownloadReportFile.writeFile(finalResponseService.generatePaymentLetterCGPJ(cod_solicitud_ejecucion));

			return DownloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in generatePaymentLetterCGPJ", e);

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/transferOrder/{cod_control_fichero}/report")
	@ApiOperation(value = "Devuelve un fichero de orden de tansferencia")
	private ResponseEntity<InputStreamResource> generatePaymentLetterN63(
			@PathVariable(name = "cod_control_fichero") String cod_control_fichero) {

		DownloadReportFile.setTempFileName("payment-letter-N63");

		DownloadReportFile.setFileTempPath(pdfSavedPath);

		try {

			DownloadReportFile.writeFile(finalResponseService.generatePaymentLetterN63(cod_control_fichero));

			return DownloadReportFile.returnToDownloadFile();
  
		} catch (Exception e) {
			logger.error("Error in generatePaymentLetterN63", e);
			System.out.println(e);

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
