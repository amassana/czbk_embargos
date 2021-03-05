package es.commerzbank.ice.embargos.controller;

import es.commerzbank.ice.comun.lib.security.Permissions;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.embargos.domain.dto.FileControlDTO;
import es.commerzbank.ice.embargos.domain.dto.FinalResponseDTO;
import es.commerzbank.ice.embargos.domain.dto.OrderingEntity;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.event.Norma63Fase6;
import es.commerzbank.ice.embargos.service.AccountingService;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.FinalResponseService;
import es.commerzbank.ice.embargos.utils.DownloadReportFile;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/finalResponse")
public class FinalResponseController {
	private static final Logger logger = LoggerFactory.getLogger(FinalResponseController.class);

	@Autowired
	private FinalResponseService finalResponseService;
	
	@Autowired
	private AccountingService accountingService;

	@Autowired
	private FileControlService fileControlService;
	
	@Autowired
	private Norma63Fase6 norma63Fase6;

	@Autowired
	private GeneralParametersService generalParametersService;

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
		
		try {
			DownloadReportFile.setTempFileName("TGSSAnexo" + num_anexo);

			DownloadReportFile.setFileTempPath(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_JASPER_TEMP));

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

		try {
			DownloadReportFile.setTempFileName("f6_finalization");

			DownloadReportFile.setFileTempPath(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_JASPER_TEMP));

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

		try {
			DownloadReportFile.setTempFileName("payment-letter-CGPJ");

			DownloadReportFile.setFileTempPath(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_JASPER_TEMP));

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

		try {
			DownloadReportFile.setTempFileName("payment-letter-N63");

			DownloadReportFile.setFileTempPath(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_JASPER_TEMP));

			DownloadReportFile.writeFile(finalResponseService.generatePaymentLetterN63(cod_control_fichero));

			return DownloadReportFile.returnToDownloadFile();
  
		} catch (Exception e) {
			logger.error("Error in generatePaymentLetterN63", e);
			System.out.println(e);

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/{codeFileControl}/generateFinalResponse", produces = { "application/json" })
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
				norma63Fase6.generarFase6(codeFileControl, authentication.getName());
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
	
	
    @PostMapping(value = "/{codeFileControl}/accounting")
    @ApiOperation(value="Envio a contabilidad si la entidad ordenante asociada al Fichero final tiene cuenta en Commerzbank.")
    public ResponseEntity<FileControlDTO> sendAccountingFinalFile(Authentication authentication,
    										  @PathVariable("codeFileControl") Long codeFileControl){
    	
    	logger.info("sendAccountingFinalFile - start");
    	
    	ResponseEntity<FileControlDTO> response = null;
		boolean result = false;

		FileControlDTO resultFileControlDTO = null;
		
		try {

			String userName = authentication.getName();
		
			accountingService.sendAccountingFinalFile(codeFileControl, userName);
			
			//Se obtiene el fileControl que se va a retornar del Fichero Final:
			resultFileControlDTO = fileControlService.getByCodeFileControl(codeFileControl);

			response = new ResponseEntity<>(resultFileControlDTO,HttpStatus.OK);
		} catch (Exception e) {

			response = new ResponseEntity<>(resultFileControlDTO,HttpStatus.BAD_REQUEST);

			logger.error("ERROR in sendAccountingFinalFile: ", e);
		}

		logger.info("sendAccountingFinalFile - end");
		return response;
    	
    }
}
