package es.commerzbank.ice.embargos.domain.mapper;

import es.commerzbank.ice.datawarehouse.domain.dto.AccountDTO;
import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.domain.entity.CuentaLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.LevantamientoTraba;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.utils.EmbargosConstants;

import java.math.BigDecimal;

public class LevantamientoHelperMapper {
    public  CuentaLevantamiento mapCuentaLevantamiento(LevantamientoTraba levantamientoTraba, String iban, BigDecimal importe, CustomerDTO DWHCustomer, Traba traba, String usuarioModif, BigDecimal fechaUltmaModif)
    {
        CuentaLevantamiento cuentaLevantamiento = new CuentaLevantamiento();

        BigDecimal orden = BigDecimal.ONE;

        String cuenta = findBankAccount(DWHCustomer, iban);
        CuentaTraba cuentaTraba = findCuentaTraba(traba, iban);

        cuentaLevantamiento.setLevantamientoTraba(levantamientoTraba);
        cuentaLevantamiento.setIban(iban);
        cuentaLevantamiento.setCuenta(cuenta);
        cuentaLevantamiento.setNumeroOrdenCuenta(orden);
        cuentaLevantamiento.setImporte(importe);
        cuentaLevantamiento.setIndContabilizado(EmbargosConstants.IND_FLAG_NO);
        cuentaLevantamiento.setUsuarioUltModificacion(usuarioModif);
        cuentaLevantamiento.setFUltimaModificacion(fechaUltmaModif);
        if (cuentaTraba != null) {
            cuentaLevantamiento.setCambio(cuentaTraba.getCambio());
            cuentaLevantamiento.setCodDivisa(cuentaTraba.getDivisa());
        }
        // Estado <-
        // TO BE DEFINED cuentaLevantamiento.setActuacion();

        return cuentaLevantamiento;
    }

    private String findBankAccount(CustomerDTO DWHCustomer, String iban)
    {
        String bankAccount = null;

        if (DWHCustomer.getBankAccounts() != null && iban != null)
        {
            for (AccountDTO DWHBankAccount : DWHCustomer.getBankAccounts())
            {
                if (iban.equals(DWHBankAccount.getIban()))
                {
                    bankAccount = DWHBankAccount.getAccountNum();
                    break;
                }
            }
        }

        return bankAccount;
    }

    private CuentaTraba findCuentaTraba(Traba traba, String iban)
    {
        for (CuentaTraba cuentaTraba : traba.getCuentaTrabas())
        {
            if (iban.equals(cuentaTraba.getIban()))
                return cuentaTraba;
        }

        return null;
    }
}
