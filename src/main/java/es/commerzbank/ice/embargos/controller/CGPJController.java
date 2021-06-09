package es.commerzbank.ice.embargos.controller;

import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.util.jasper.ReportHelper;
import es.commerzbank.ice.embargos.domain.dto.AccountingPendingDTO;
import es.commerzbank.ice.embargos.domain.dto.CGPJFiltersDTO;
import es.commerzbank.ice.embargos.domain.dto.CGPJPetitionDTO;
import es.commerzbank.ice.embargos.domain.dto.IntegradorRequestStatusDTO;
import es.commerzbank.ice.embargos.service.CGPJService;
import es.commerzbank.ice.embargos.utils.DownloadReportFile;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/cgpj")
public class CGPJController {

    private static final Logger logger = LoggerFactory.getLogger(CGPJController.class);

    @Autowired
    private CGPJService service;

    @Autowired
    private ReportHelper reportHelper;

    @Autowired
    private GeneralParametersService generalParametersService;

    @PostMapping(value = "/filter")
    public ResponseEntity<Page<CGPJPetitionDTO>> filter(@RequestBody CGPJFiltersDTO filters, Pageable pageable)
    {
        ResponseEntity<Page<CGPJPetitionDTO>> response;
        Page<CGPJPetitionDTO> result = null;

        try
        {
            result = service.filter(filters, pageable);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception e)
        {
            response = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
            logger.error("ERROR in filecontrol/filter", e);
        }

        return response;
    }

    @GetMapping(value = "/status")
    public ResponseEntity<List<IntegradorRequestStatusDTO>> listStatus()
    {
        ResponseEntity<List<IntegradorRequestStatusDTO>> response;
        List<IntegradorRequestStatusDTO> result = null;

        try
        {
            result = service.listStatus();
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception e)
        {
            response = new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
            logger.error("ERROR in filecontrol/status", e);
        }

        return response;
    }

    @GetMapping(value = "/{codPeticion}/informeSEPA")
    public ResponseEntity<InputStreamResource> informeSEPA(@PathVariable("codPeticion") String codPeticion) {
        File temporaryFile = null;

        try {
            ResponseEntity<InputStreamResource> response = null;

            temporaryFile = service.informeSEPA(codPeticion);

            response = reportHelper.asResponseEntity("cgpj-sepa-"+ codPeticion, temporaryFile, ReportHelper.PDF_EXTENSION);

            return response;
        }
        catch (Exception e)
        {
            logger.error("Error in informeSEPA", e);

            return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{codPeticion}/informePeticion")
    public ResponseEntity<InputStreamResource> informePeticion(@PathVariable("codPeticion") String codPeticion) {
        try {
            DownloadReportFile downloadReportFile = new DownloadReportFile();

            downloadReportFile.setTempFileName("informePeticion");

            downloadReportFile.setFileTempPath(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_JASPER_TEMP));

            byte[] data = service.informePeticion(codPeticion);

            downloadReportFile.writeFile(data);

            return downloadReportFile.returnToDownloadFile();

        } catch (Exception e) {
            logger.error("Error in f1PettitionRequest", e);

            return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/pendientesContabilizar")
    public ResponseEntity<List<AccountingPendingDTO>> pendientesContabilizar()
    {
        ResponseEntity<List<AccountingPendingDTO>> response;

        try
        {
            List<AccountingPendingDTO> pending = service.accountingPending();
            response = new ResponseEntity<>(pending, HttpStatus.OK);
        }
        catch (Exception e)
        {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            logger.error("CGPJ: error al obtener la lista de entradas pendientes de contabilizar", e);
        }

        return response;
    }

    @PostMapping(value = "/precontable")
    public ResponseEntity<InputStreamResource> precontable(@RequestBody List<AccountingPendingDTO> pendientes) {
        File temporaryFile = null;

        try {
            ResponseEntity<InputStreamResource> response = null;

            temporaryFile = service.informeSEPA("");

            response = reportHelper.asResponseEntity("precontable", temporaryFile, ReportHelper.PDF_EXTENSION);

            return response;
        }
        catch (Exception e)
        {
            logger.error("Error in informeSEPA", e);

            return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/contabilizar")
    public ResponseEntity<Long> contabilizar(Authentication authentication, @RequestBody List<AccountingPendingDTO> pendientes) {
        ResponseEntity<Long> response;

        try
        {
            Long codControlFichero = service.contabilizar(pendientes, authentication.getName());
            response = new ResponseEntity<>(codControlFichero, HttpStatus.OK);
        }
        catch (Exception e)
        {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            logger.error("CGPJ: error al contabilizar", e);
        }

        return response;
    }

    @PostMapping(value = "/responder")
    public ResponseEntity<Boolean> responder(Authentication authentication, @RequestBody List<String> peticiones) {
        ResponseEntity<Boolean> response;

        try
        {
            boolean allReplied = service.reply(peticiones, authentication.getName());
            response = new ResponseEntity<>(allReplied, HttpStatus.OK);
        }
        catch (Exception e)
        {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            logger.error("Error al responder CGPJ", e);
        }

        return response;
    }
}
