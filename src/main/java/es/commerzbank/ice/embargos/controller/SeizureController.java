package es.commerzbank.ice.embargos.controller;

import es.commerzbank.ice.embargos.domain.dto.*;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/seizure")
public class SeizureController {
    private static final Logger LOG = LoggerFactory.getLogger(PetitionController.class);

    @GetMapping(value = "/{codeFileControl}")
    @ApiOperation(value="hello world")
    public ResponseEntity<List<SeizureDTO>> getSeizureListByCodeFileControl(Authentication authentication,
                                                                                 @PathVariable("codeFileControl") Long codeFileControl){
        ResponseEntity<List<SeizureDTO>> response = null;
        List<SeizureDTO> result = new ArrayList<SeizureDTO>();

        SeizureStatusDTO sstatus = new SeizureStatusDTO();
        sstatus.setCode("1");
        sstatus.setCode("Pendiente");

        SeizureDTO s1 = new SeizureDTO();
        s1.setIdSeizure("id s 1");
        s1.setIdSeizureRequest("id sr 1");
        s1.setName("name 1");
        s1.setNameInternal("internal name 1");
        s1.setNIF("12345678J");
        s1.setRequestedAmount(BigDecimal.TEN.multiply(BigDecimal.TEN));
        s1.setStatus(sstatus);

        SeizureDTO s2 = new SeizureDTO();
        s2.setIdSeizure("id s 2");
        s2.setIdSeizureRequest("id sr 2");
        s2.setName("name 2");
        s2.setNameInternal("internal name 2");
        s2.setNIF("87654321P");
        s2.setRequestedAmount(BigDecimal.ONE);
        s2.setStatus(sstatus);

        result.add(s1);
        result.add(s2);

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @GetMapping(value = "/{codeFileControl}/case/{idSeizure}/accounts")
    @ApiOperation(value="write something here")
    public ResponseEntity<List<SeizedBankAccountDTO>> getBankAccountListByCodeFileControlAndPetitionCase(Authentication authentication,
                                                                                                         @PathVariable("codeFileControl") Long codeFileControl,
                                                                                                         @PathVariable("idSeizure") String idSeizure){
        ResponseEntity<List<SeizedBankAccountDTO>> response = null;
        List<SeizedBankAccountDTO> result = new ArrayList<SeizedBankAccountDTO>();

        SeizureActionDTO saction = new SeizureActionDTO();
        saction.setCode("00");
        saction.setDescription("Sin actuacion");

        SeizureStatusDTO sstatus = new SeizureStatusDTO();
        sstatus.setCode("1");
        sstatus.setCode("Pendiente");

        SeizedBankAccountDTO s1 = new SeizedBankAccountDTO();
        s1.setAmount(BigDecimal.ZERO);
        s1.setBankAccountCurrency("EUR");
        s1.setBankAccountStatus("ACTIVE");
        s1.setFxRate(BigDecimal.ONE);
        s1.setIdSeizedBankAccount("id sbk 1");
        s1.setSeizureAction(saction);
        s1.setSeizureStatus(sstatus);

        SeizedBankAccountDTO s2 = new SeizedBankAccountDTO();
        s2.setAmount(BigDecimal.ZERO);
        s2.setBankAccountCurrency("USD");
        s2.setBankAccountStatus("ACTIVE");
        s2.setFxRate(new BigDecimal("1.1"));
        s2.setIdSeizedBankAccount("id sbk 2");
        s2.setSeizureAction(saction);
        s2.setSeizureStatus(sstatus);

        result.add(s1);
        result.add(s2);

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @GetMapping(value = "/actions")
    @ApiOperation(value="x was here")
    public ResponseEntity<List<SeizureActionDTO>> getSeizureActions(Authentication authentication)
    {
        ResponseEntity<List<SeizureActionDTO>> response = null;
        List<SeizureActionDTO> result = new ArrayList<SeizureActionDTO>();

        SeizureActionDTO saction = new SeizureActionDTO();
        saction.setCode("00");
        saction.setDescription("Sin actuacion");

        SeizureActionDTO saction2 = new SeizureActionDTO();
        saction2.setCode("01");
        saction2.setDescription("Retencion realizada");

        result.add(saction);
        result.add(saction2);

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @GetMapping(value = "/accountingStatuses")
    @ApiOperation(value="write something there")
    public ResponseEntity<List<SeizureStatusDTO>> getAccountingStatuses(Authentication authentication)
    {
        ResponseEntity<List<SeizureStatusDTO>> response = null;
        List<SeizureStatusDTO> result = new ArrayList<SeizureStatusDTO>();

        SeizureStatusDTO sstatus = new SeizureStatusDTO();
        sstatus.setCode("1");
        sstatus.setCode("Pendiente");

        SeizureStatusDTO sstatus2 = new SeizureStatusDTO();
        sstatus2.setCode("2");
        sstatus2.setCode("Pendiente de contabilizacion");

        SeizureStatusDTO sstatus3 = new SeizureStatusDTO();
        sstatus3.setCode("3");
        sstatus3.setCode("Contabilizado");

        result.add(sstatus);
        result.add(sstatus2);
        result.add(sstatus3);

        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }
}
