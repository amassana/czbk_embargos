package es.commerzbank.ice.embargos.service.impl;

import es.commerzbank.ice.datawarehouse.domain.dto.AccountDTO;
import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.domain.entity.*;
import es.commerzbank.ice.embargos.repository.*;
import es.commerzbank.ice.embargos.service.CGPJImportService;
import es.commerzbank.ice.embargos.service.CustomerService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.EmbargosUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static es.commerzbank.ice.embargos.utils.EmbargosConstants.IND_FLAG_NO;
import static es.commerzbank.ice.embargos.utils.EmbargosConstants.IND_FLAG_SI;

@Service
public class CGPJImportServiceImpl
    implements CGPJImportService
{
    @Autowired
    private FileControlRepository fileControlRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SeizureRepository seizureRepository;

    @Autowired
    private SeizedRepository seizedRepository;

    @Autowired
    private SeizureBankAccountRepository seizureBankAccountRepository;

    @Autowired
    private SeizedBankAccountRepository seizedBankAccountRepository;

    @Autowired
    private es.commerzbank.ice.embargos.domain.mapper.CGPJMapper CGPJMapper;

    @Override
    public List<ControlFichero> listPending() {
        return fileControlRepository.findPendingCGPJ();
    }

    @Override
    public void importCGPJ(ControlFichero controlFichero)
            throws Exception
    {
        for (Embargo embargo : controlFichero.getEmbargos()) {
            importEmbargo(embargo);
        }

        EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero(
                    EmbargosConstants.COD_ESTADO_CTRLFICHERO_CGPJ_IMPORTADO,
                    EmbargosConstants.COD_TIPO_FICHERO_PETICION_CGPJ);

        controlFichero.setEstadoCtrlfichero(estadoCtrlfichero);

        controlFichero.setIndTieneTrabas(controlFichero.getEmbargos() != null && controlFichero.getEmbargos().size() > 0 ? IND_FLAG_SI : IND_FLAG_NO);

        fileControlRepository.saveAndFlush(controlFichero);
    }

    private void importEmbargo(Embargo embargo)
            throws Exception
    {
        CustomerDTO customerDTO = customerService.findCustomerByNif(embargo.getNif(), false);

        if (embargo.getTrabas() == null)
            throw new Exception ("Se esperaba una traba asociada al embargo " + embargo.getCodEmbargo());

        if (embargo.getTrabas().size() != 1)
            throw new Exception ("Se han encontrado "+ embargo.getTrabas().size() +" trabas asociadas al embargo " + embargo.getCodEmbargo());

        Traba traba = embargo.getTrabas().get(0);

        embargo.setRazonSocialInterna(EmbargosUtils.determineRazonSocialInternaFromCustomer(customerDTO));

        traba.setRevisado(IND_FLAG_NO);

        int counter = 1;
        embargo.setCuentaEmbargos(new ArrayList<>());
        traba.setCuentaTrabas(new ArrayList<>());

        for (AccountDTO accountDTO : customerDTO.getBankAccounts())
        {
            CuentaEmbargo cuentaEmbargo = CGPJMapper.generateCuentaEmbargo(accountDTO, BigDecimal.valueOf(counter));
            embargo.addCuentaEmbargo(cuentaEmbargo);
            seizureBankAccountRepository.save(cuentaEmbargo);

            CuentaTraba cuentaTraba = CGPJMapper.generateCuentaTraba(accountDTO, BigDecimal.valueOf(counter));
            traba.addCuentaTraba(cuentaTraba);
            seizedBankAccountRepository.save(cuentaTraba);

            counter++;
        }

        seizureRepository.save(embargo);

        seizedRepository.save(traba);
    }
}
