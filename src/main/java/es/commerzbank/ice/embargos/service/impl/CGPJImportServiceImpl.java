package es.commerzbank.ice.embargos.service.impl;

import es.commerzbank.ice.datawarehouse.domain.dto.AccountDTO;
import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.domain.entity.*;
import es.commerzbank.ice.embargos.repository.*;
import es.commerzbank.ice.embargos.service.CGPJImportService;
import es.commerzbank.ice.embargos.service.CustomerService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.EmbargosUtils;
import es.commerzbank.ice.embargos.utils.ICEDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CGPJImportServiceImpl
    implements CGPJImportService
{
    @Autowired
    private FileControlRepository fileControlRepository;

    @Autowired
    private CustomerService customerService;

    @Override
    public List<ControlFichero> listPending() {
        return fileControlRepository.findPendingCGPJ();
    }

    @Autowired
    private SeizureRepository seizureRepository;

    @Autowired
    private SeizedRepository seizedRepository;

    @Autowired
    private SeizureBankAccountRepository seizureBankAccountRepository;

    @Autowired
    private SeizedBankAccountRepository seizedBankAccountRepository;

    @Override
    @Transactional(transactionManager="transactionManager")
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

        fileControlRepository.saveAndFlush(controlFichero);
    }

    private void importEmbargo(Embargo embargo)
            throws Exception
    {
        CustomerDTO customerDTO = customerService.findCustomerByNif(embargo.getNif(), false);

        embargo.setRazonSocialInterna(EmbargosUtils.determineRazonSocialInternaFromCustomer(customerDTO));

        if (embargo.getTrabas() == null)
            throw new Exception ("Se esperaba una traba asociada al embargo " + embargo.getCodEmbargo());

        if (embargo.getTrabas().size() != 1)
            throw new Exception ("Se han encontrado "+ embargo.getTrabas().size() +" trabas asociadas al embargo " + embargo.getCodEmbargo());

        Traba traba = embargo.getTrabas().get(0);

        traba.setRevisado(EmbargosConstants.IND_FLAG_NO);

        int counter = 1;
        BigDecimal fechaUltmaModif = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
        String usuarioModif = EmbargosConstants.USER_AUTOMATICO;
        embargo.setCuentaEmbargos(new ArrayList<>());
        traba.setCuentaTrabas(new ArrayList<>());

        for (AccountDTO accountDTO : customerDTO.getBankAccounts())
        {
            CuentaEmbargo cuentaEmbargo = new CuentaEmbargo();

            cuentaEmbargo.setCuenta(accountDTO.getAccountNum());
            cuentaEmbargo.setUsuarioUltModificacion(usuarioModif);
            cuentaEmbargo.setFUltimaModificacion(fechaUltmaModif);
            cuentaEmbargo.setNumeroOrdenCuenta(BigDecimal.valueOf(counter));
            cuentaEmbargo.setIban(accountDTO.getIban());

            embargo.addCuentaEmbargo(cuentaEmbargo);

            seizureBankAccountRepository.save(cuentaEmbargo);

            CuentaTraba cuentaTraba = new CuentaTraba();

            cuentaTraba.setCuenta(accountDTO.getAccountNum());
            cuentaTraba.setUsuarioUltModificacion(usuarioModif);
            cuentaTraba.setFUltimaModificacion(fechaUltmaModif);
            cuentaTraba.setNumeroOrdenCuenta(BigDecimal.valueOf(counter));
            EstadoTraba estadoTraba = new EstadoTraba();
            estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE);
            cuentaTraba.setEstadoTraba(estadoTraba);
            cuentaTraba.setIban(accountDTO.getIban());
            cuentaTraba.setEstadoCuenta(accountDTO.getStatus());
            cuentaTraba.setDivisa(accountDTO.getDivisa());
            cuentaTraba.setOrigenEmb(EmbargosConstants.IND_FLAG_YES);
            cuentaTraba.setAgregarATraba(EmbargosConstants.IND_FLAG_YES);

            seizedBankAccountRepository.save(cuentaTraba);

            counter++;
        }

        seizureRepository.save(embargo);

        seizedRepository.save(traba);
    }
}
