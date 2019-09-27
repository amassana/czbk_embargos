package es.commerzbank.ice.embargos.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.embargos.domain.dto.BankAccountDTO;
import es.commerzbank.ice.embargos.domain.dto.PetitionCaseDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.service.InformationPetitionBankAccountService;
import es.commerzbank.ice.embargos.service.InformationPetitionService;
import es.commerzbank.ice.embargos.service.PetitionService;
import es.commerzbank.ice.utils.DownloadReportFile;
import es.commerzbank.ice.utils.EmbargosConstants;
import io.swagger.annotations.ApiOperation;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/petition")
public class PetitionController {

	private static final Logger logger = LoggerFactory.getLogger(PetitionController.class);

	@Autowired
	private InformationPetitionService informationPetitionService;

	@Autowired
	private InformationPetitionBankAccountService informationPetitionBankAccountService;

	@Autowired
	private PetitionService petitionService;

	@Autowired
	private GeneralParametersService generalParametersService;

	@GetMapping(value = "/{codeFileControl}")
	@ApiOperation(value = "Devuelve la lista de casos para una peticion de informacion.")
	public ResponseEntity<List<PetitionCaseDTO>> getPetitionCaseListByCodeFileControl(Authentication authentication,
			@PathVariable("codeFileControl") Long codeFileControl) {
		logger.info("PetitionController - getPetitionCaseListByCodeFileControl - start");
		ResponseEntity<List<PetitionCaseDTO>> response = null;
		List<PetitionCaseDTO> result = null;

		try {

			ControlFichero controlFichero = new ControlFichero();
			controlFichero.setCodControlFichero(codeFileControl);

			result = informationPetitionService.getAllByControlFichero(controlFichero);

			response = new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {

			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

			logger.error("ERROR in getPetitionCaseListByCodeFileControl: ", e);
		}

		logger.info("PetitionController - getPetitionCaseListByCodeFileControl - end");
		return response;
	}

	@GetMapping(value = "/{codeFileControl}/petitioncase/{codePetitionCase}/bankAccounts")
	@ApiOperation(value = "Devuelve la lista de cuentas disponibles para el caso indicado de PETICION_INFORMACION_CUENTAS.")
	public ResponseEntity<List<BankAccountDTO>> getBankAccountListByCodeFileControlAndPetitionCase(
			Authentication authentication, @PathVariable("codeFileControl") Long codeFileControl,
			@PathVariable("codePetitionCase") Long codePetitionCase) {
		logger.info("PetitionController - getBankAccountListByCodeFileControlAndPetitionCase - start");
		ResponseEntity<List<BankAccountDTO>> response = null;
		List<BankAccountDTO> result = null;

		try {

			result = informationPetitionBankAccountService.getAllByControlFicheroAndPeticionInformacion(codeFileControl,
					codePetitionCase);

			response = new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {

			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

			logger.error("ERROR in getBankAccountListByCodeFileControlAndPetitionCase: ", e);
		}

		logger.info("PetitionController - getBankAccountListByCodeFileControlAndPetitionCase - end");
		return response;
	}

	@PostMapping(value = "/{codeFileControl}/petitioncase/{codePetitionCase}", produces = {
			"application/json" }, consumes = { "application/json" })
	@ApiOperation(value = "Actualiza el caso de PETICION_INFORMACION indicado, guardando los datos que se traspasen")
	public ResponseEntity<String> savePetitionCase(Authentication authentication,
			@PathVariable("codeFileControl") Long codeFileControl,
			@PathVariable("codePetitionCase") Long codePetitionCase, @RequestBody PetitionCaseDTO petitionCase) {
		logger.info("PetitionController - savePetitionCase - start");
		ResponseEntity<String> response = null;
		boolean result = true;

		try {

			String userModif = authentication.getName();

			result = informationPetitionService.savePetitionCase(codeFileControl, codePetitionCase, petitionCase,
					userModif);

			if (result) {
				response = new ResponseEntity<>(HttpStatus.OK);
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {

			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

			logger.error("ERROR in savePetitionCase: ", e);
		}

		logger.info("PetitionController - savePetitionCase - end");
		return response;

	}

	@GetMapping(value = "/{codeFileControl}/petitioncase/{codePetitionCase}/audit")
	public ResponseEntity<List<PetitionCaseDTO>> getAuditPetitionCase(Authentication authentication,
			@PathVariable("codeFileControl") Long codeFileControl,
			@PathVariable("codePetitionCase") Long codePetitionCase) {
		logger.info("PetitionController - getAuditPetitionCase - start");
		ResponseEntity<List<PetitionCaseDTO>> response = null;
		List<PetitionCaseDTO> result = null;

		try {

			result = informationPetitionService.getAuditByCodeInformationPetition(codePetitionCase);

			response = new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {

			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

			logger.error("ERROR in getAuditPetitionCase: ", e);
		}

		logger.info("PetitionController - getAuditPetitionCase - end");
		return response;

	}

	@GetMapping("/{codeFileControl}/report") // f1
	@ApiOperation(value = "Devuelve report PettitionRequest Fase1")
	public ResponseEntity<InputStreamResource> f1PettitionRequest(
			@PathVariable("codeFileControl") Integer codeFileControl) {
		logger.info("PetitionController - f1PettitionRequest - start");
		
		DownloadReportFile.setTempFileName("petitionReportRequest");

		DownloadReportFile.setFileTempPath(pdfSavedPath);

		try {

			DownloadReportFile.writeFile(petitionService.generateF1PettitionRequest(codeFileControl));

			logger.info("PetitionController - f1PettitionRequest - end");
			return DownloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in f1PettitionRequest", e);
	
			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/response/{codeFileControl}/report") // f2
	@ApiOperation(value = "Devuelve report PettitionResponse Fase2")
	public ResponseEntity<InputStreamResource> f2PettitionResponse(
			@PathVariable("codeFileControl") Integer codeFileControl) {
		logger.info("PetitionController - f2PettitionResponse - start");

		try {
			DownloadReportFile.setTempFileName("pettition-response");

			DownloadReportFile.setFileTempPath(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_JASPER_TEMP));

			DownloadReportFile.writeFile(petitionService.generateF2PettitionResponse(codeFileControl));

			logger.info("PetitionController - f2PettitionResponse - end");
			return DownloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in petitionReport", e);

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
