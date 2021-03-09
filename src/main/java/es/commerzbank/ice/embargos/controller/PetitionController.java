package es.commerzbank.ice.embargos.controller;

import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.embargos.domain.dto.BankAccountDTO;
import es.commerzbank.ice.embargos.domain.dto.PetitionCaseDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.service.InformationPetitionBankAccountService;
import es.commerzbank.ice.embargos.service.InformationPetitionService;
import es.commerzbank.ice.embargos.service.PetitionService;
import es.commerzbank.ice.embargos.utils.DownloadReportFile;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.OfficeUtils;
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

	@Autowired
	private OfficeUtils officeUtils;

	@GetMapping(value = "/{codeFileControl}")
	@ApiOperation(value = "Devuelve la lista de casos para una peticion de informacion.")
	public ResponseEntity<List<PetitionCaseDTO>> getPetitionCaseListByCodeFileControl(Authentication authentication,
			@PathVariable("codeFileControl") Long codeFileControl)
	{
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

		return response;
	}

	@GetMapping(value = "/{codeFileControl}/petitioncase/{codePetitionCase}/bankAccounts")
	@ApiOperation(value = "Devuelve la lista de cuentas disponibles para el caso indicado de PETICION_INFORMACION_CUENTAS.")
	public ResponseEntity<List<BankAccountDTO>> getBankAccountListByCodeFileControlAndPetitionCase(
			Authentication authentication, @PathVariable("codeFileControl") Long codeFileControl,
			@PathVariable("codePetitionCase") Long codePetitionCase)
	{
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

		return response;
	}

	@PostMapping(value = "/{codeFileControl}/petitioncase/{codePetitionCase}", produces = {
			"application/json" }, consumes = { "application/json" })
	@ApiOperation(value = "Actualiza el caso de PETICION_INFORMACION indicado, guardando los datos que se traspasen")
	public ResponseEntity<String> savePetitionCase(Authentication authentication,
			@PathVariable("codeFileControl") Long codeFileControl,
			@PathVariable("codePetitionCase") Long codePetitionCase, @RequestBody PetitionCaseDTO petitionCase)
	{
		logger.info("PetitionController actualización "+ codeFileControl +"-"+ codePetitionCase);

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

		return response;

	}

	@GetMapping(value = "/{codeFileControl}/petitioncase/{codePetitionCase}/audit")
	public ResponseEntity<List<PetitionCaseDTO>> getAuditPetitionCase(Authentication authentication,
			@PathVariable("codeFileControl") Long codeFileControl,
			@PathVariable("codePetitionCase") Long codePetitionCase)
	{
		ResponseEntity<List<PetitionCaseDTO>> response = null;
		List<PetitionCaseDTO> result = null;

		try {

			result = informationPetitionService.getAuditByCodeInformationPetition(codePetitionCase);

			response = new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {

			response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

			logger.error("ERROR in getAuditPetitionCase: ", e);
		}

		return response;

	}

	@GetMapping("/{codeFileControl}/report") // f1
	@ApiOperation(value = "Devuelve report PettitionRequest Fase1")
	public ResponseEntity<InputStreamResource> f1PettitionRequest(Authentication authentication,
			@PathVariable("codeFileControl") Integer codeFileControl)
	{
		try {
			DownloadReportFile downloadReportFile = new DownloadReportFile();
			
			downloadReportFile.setTempFileName("petitionReportRequest");

			downloadReportFile.setFileTempPath(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_JASPER_TEMP));

			String oficina = officeUtils.getLocalidadUsuario(authentication);

			byte[] data = petitionService.generateF1PettitionRequest(codeFileControl, oficina);

			downloadReportFile.writeFile(data);

			return downloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in f1PettitionRequest", e);
	
			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/response/{codeFileControl}/report") // f2
	@ApiOperation(value = "Devuelve report PettitionResponse Fase2")
	public ResponseEntity<InputStreamResource> f2PettitionResponse(Authentication authentication,
			@PathVariable("codeFileControl") Integer codeFileControl)
	{
		try {
			DownloadReportFile downloadReportFile = new DownloadReportFile();
			
			downloadReportFile.setTempFileName("pettition-response");

			downloadReportFile.setFileTempPath(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_JASPER_TEMP));

			String oficina = officeUtils.getLocalidadUsuario(authentication);

			byte[] data = petitionService.generateF2PettitionResponse(codeFileControl, oficina);

			downloadReportFile.writeFile(data);

			return downloadReportFile.returnToDownloadFile();

		} catch (Exception e) {
			logger.error("Error in petitionReport", e);

			return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
