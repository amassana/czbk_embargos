package es.commerzbank.ice.embargos.controller;

import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.embargos.domain.dto.FinalResponseDTO;
import es.commerzbank.ice.embargos.domain.dto.FinalResponsePendingDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
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
	private GeneralParametersService generalParametersService;
	
	@Autowired
	private FileControlRepository fileControlRepository;
	
	@GetMapping(value = "/{codeFileControl}")
	@ApiOperation(value = "Devuelve la lista de casos de levamtamientos")
	public ResponseEntity<List<FinalResponseDTO>> getFinalResponseListByCodeFileControl(Authentication authentication,
			@PathVariable("codeFileControl") Long codeFileControl) {
		ResponseEntity<List<FinalResponseDTO>> response = null;
		List<FinalResponseDTO> result = null;

		try {

			ControlFichero controlFichero = new ControlFichero();
			controlFichero.setCodControlFichero(codeFileControl);

			result = finalResponseService.getAllByControlFichero(controlFichero);

			response = new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {

			response = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);

			logger.error("ERROR in getFinalResponseListByCodeFileControl: ", e);
		}

		return response;
	}

	@GetMapping(value = "/{codeFileControl}/finalResponseCase/{codeFinalResponse}/bankAccounts")
	@ApiOperation(value = "Devuelve la lista de cuentas disponibles para el caso indicado de levantamiento.")
	public ResponseEntity<FinalResponseDTO> getBankAccountListByCodeFileControlAndFinalResponse(
			Authentication authentication, @PathVariable("codeFileControl") Long codeFileControl,
			@PathVariable("codeFinalResponse") Long codeFinalResponse) {
		ResponseEntity<FinalResponseDTO> response = null;
		FinalResponseDTO result = null;

		try {

			result = finalResponseService.AddBankAccountList(codeFileControl, codeFinalResponse);

			response = new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {

			response = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);

			logger.error("ERROR in getBankAccountListByCodeFileControlAndFinalResponse: ", e);
		}

		return response;
	}

	@GetMapping(value = "/pendingFinalResponses")
	@ApiOperation(value = "Devuelve la lista de datos correspondientes a las tareas de F6 pendientes.")
	public ResponseEntity<List<FinalResponsePendingDTO>> getPendingFinalResponses(Authentication authentication) {
		ResponseEntity<List<FinalResponsePendingDTO>> response = null;
		List<FinalResponsePendingDTO> result = null;

		try {

			result = finalResponseService.listPendingCyclesNorma63();
			
			response = new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {
			response = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			logger.error("ERROR in getPendingFinalResponses: ", e);
		}

		return response;
	}

	/*
	@GetMapping("/anexo/{cod_usuario}/{cod_traba}/{num_anexo}/report")
	public ResponseEntity<InputStreamResource> generarAnexo(@PathVariable("cod_usuario") BigDecimal cod_usuario,
			@PathVariable("cod_traba") BigDecimal cod_traba, @PathVariable("num_anexo") Integer num_anexo)
			throws Exception {

		ResponseEntity<InputStreamResource> response = null;

		try {
			DownloadReportFile downloadReportFile = new DownloadReportFile();
			
			downloadReportFile.setTempFileName("TGSSAnexo" + num_anexo);

			downloadReportFile.setFileTempPath(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_JASPER_TEMP));

			downloadReportFile.writeFile(finalResponseService.generarAnexo(cod_usuario, cod_traba, num_anexo));

			response = downloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in downloadAnexo", e);

			response = new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}
	*/

	@GetMapping("/anexoTGSS/{codeFileControl}")
	public ResponseEntity<InputStreamResource> reportAnexoTGSS(
			@PathVariable("codeFileControl") Long codeFileControl,
			@RequestParam(name = "codRepresentative", required = true) Long codRepresentative)
	{
		ResponseEntity<InputStreamResource> response = null;

		try {
			DownloadReportFile downloadReportFile = new DownloadReportFile();

			downloadReportFile.setTempFileName("TGSSAnexo_"+ codeFileControl);

			downloadReportFile.setFileTempPath(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_JASPER_TEMP));

			downloadReportFile.writeFile(finalResponseService.generarAnexo(codeFileControl, codRepresentative));

			response = downloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in downloadAnexo", e);

			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}
	
	@GetMapping("/{fileControl}/report")
	@ApiOperation(value = "Devuelve el fichero de respuesta final de embargos")
	public ResponseEntity<InputStreamResource> reportFinalCiclo(
			@PathVariable("fileControl") Integer codFileControl) {

		try {
			DownloadReportFile downloadReportFile = new DownloadReportFile();
			
			downloadReportFile.setTempFileName("f6_finalization");

			downloadReportFile.setFileTempPath(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_JASPER_TEMP));

			downloadReportFile.writeFile(finalResponseService.generarRespuestaFinalEmbargo(codFileControl));

			return downloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in generarRespuestaFinalEmbargo", e);

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//@GetMapping("/cgpj/{cod_traba}/report")
	@GetMapping("/cgpj/transferencias")
	@ApiOperation(value = "Devuelve el fichero de solicitud de ejecucion")
	public ResponseEntity<InputStreamResource> reportCartaPagoCGPJ(
			@PathVariable("cod_traba") String cod_solicitud_ejecucion) {

		try {
			DownloadReportFile downloadReportFile = new DownloadReportFile();
			
			downloadReportFile.setTempFileName("payment-letter-CGPJ");

			downloadReportFile.setFileTempPath(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_JASPER_TEMP));

			downloadReportFile.writeFile(finalResponseService.generatePaymentLetterCGPJ(cod_solicitud_ejecucion));

			return downloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in generatePaymentLetterCGPJ", e);

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/ordenTransferencia/{cod_control_fichero}")
	@ApiOperation(value = "Devuelve un fichero de orden de tansferencia")
	public ResponseEntity<InputStreamResource> reportCartaPagoNorma63(
			@PathVariable(name = "cod_control_fichero") String cod_control_fichero) {

		try {
			DownloadReportFile downloadReportFile = new DownloadReportFile();
			
			downloadReportFile.setTempFileName("payment-letter-N63");

			downloadReportFile.setFileTempPath(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_JASPER_TEMP));

			downloadReportFile.writeFile(finalResponseService.generatePaymentLetterN63(cod_control_fichero));

			return downloadReportFile.returnToDownloadFile();
  
		} catch (Exception e) {
			logger.error("Error in generatePaymentLetterN63", e);
			System.out.println(e);

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/{codeFileControl}/generateFinalResponse", produces = { "application/json" })
	public ResponseEntity<Void> createNorma63(Authentication authentication,
											  @PathVariable("codeFileControl") Long codeFileControl) {
		logger.debug("SeizureSummaryController - createNorma63 - start "+ codeFileControl);
		ResponseEntity<Void> response = null;

		if (codeFileControl != null) {
			try {
				Long codControlFichero = finalResponseService.calcFinalResult(codeFileControl, authentication.getName());

				if (codControlFichero == null)
					response = new ResponseEntity<>(HttpStatus.CONFLICT);
				else
					response = new ResponseEntity<>(HttpStatus.OK);
			} catch (Exception e) {
				logger.error("SeizureSummaryController - createNorma63 - Error while generating norma 63 summary file", e);
				response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		logger.debug("SeizureSummaryController - createNorma63 - end");

		return response;
	}
	
	/*
    @PostMapping(value = "/{codeFileControl}/accounting")
    @ApiOperation(value="Envio a contabilidad si la entidad ordenante asociada al Fichero final tiene cuenta en Commerzbank.")
    public ResponseEntity<FileControlDTO> sendAccountingFinalFile(Authentication authentication,
    										  @PathVariable("codeFileControl") Long codeFileControl){
    	
    	logger.debug("sendAccountingFinalFile - contabilización "+ codeFileControl);
    	
    	ResponseEntity<FileControlDTO> response = null;
		//boolean result = false;

		FileControlDTO resultFileControlDTO = null;
		
		try {

			String userName = authentication.getName();
		
			accountingService.sendFinalFile(codeFileControl, userName);
			
			//Se obtiene el fileControl que se va a retornar del Fichero Final:
			resultFileControlDTO = fileControlService.getByCodeFileControl(codeFileControl);

			response = new ResponseEntity<>(resultFileControlDTO,HttpStatus.OK);
		} catch (Exception e) {

			response = new ResponseEntity<>(resultFileControlDTO,HttpStatus.INTERNAL_SERVER_ERROR);

			logger.error("ERROR in sendAccountingFinalFile: ", e);
		}

		logger.debug("sendAccountingFinalFile - end");
		return response;
    	
    }
    */
}
