package es.commerzbank.ice.embargos.controller;

import es.commerzbank.ice.embargos.domain.dto.CGPJPetitionDTO;
import es.commerzbank.ice.embargos.domain.dto.CGPJFiltersDTO;
import es.commerzbank.ice.embargos.service.CGPJService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/cgpj")
public class CGPJController {

    private static final Logger logger = LoggerFactory.getLogger(CGPJController.class);

    @Autowired
    private CGPJService service;

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
}
