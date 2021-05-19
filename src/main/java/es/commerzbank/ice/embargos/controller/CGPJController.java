package es.commerzbank.ice.embargos.controller;

import es.commerzbank.ice.comun.lib.util.jasper.ReportHelper;
import es.commerzbank.ice.embargos.domain.dto.CGPJFiltersDTO;
import es.commerzbank.ice.embargos.domain.dto.CGPJPetitionDTO;
import es.commerzbank.ice.embargos.domain.dto.IntegradorRequestStatusDTO;
import es.commerzbank.ice.embargos.service.CGPJService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(value = "/informeSEPA/{codPeticion}")
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
}
