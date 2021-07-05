package es.commerzbank.ice.embargos.domain.mapper;

import es.commerzbank.ice.datawarehouse.domain.dto.AccountDTO;
import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.domain.entity.*;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;

import java.math.BigDecimal;

public class LevantamientoHelperMapper {
    public CuentaLevantamiento mapCuentaLevantamiento(
            LevantamientoTraba levantamientoTraba, String iban, BigDecimal importe,
            CustomerDTO DWHCustomer, Traba traba, String usuarioModif, BigDecimal fechaUltmaModif,
            String codigoTipoLevantamiento)
    {
        CuentaLevantamiento cuentaLevantamiento = new CuentaLevantamiento();

        AccountDTO cuenta = findBankAccount(DWHCustomer, iban);
        CuentaTraba cuentaTraba = findCuentaTraba(traba, cuenta.getAccountNum());

        cuentaLevantamiento.setLevantamientoTraba(levantamientoTraba);
        cuentaLevantamiento.setIban(iban);
        cuentaLevantamiento.setImporte(importe);
        cuentaLevantamiento.setIndContabilizado(EmbargosConstants.IND_FLAG_NO);
        cuentaLevantamiento.setUsuarioUltModificacion(usuarioModif);
        cuentaLevantamiento.setFUltimaModificacion(fechaUltmaModif);
        
        if (cuentaTraba != null) {
            cuentaLevantamiento.setCambio(cuentaTraba.getCambio());
            cuentaLevantamiento.setFechaValor(cuentaTraba.getFechaValor());
        }

        if (cuenta!=null) {
        	cuentaLevantamiento.setCuenta(cuenta.getAccountNum());
            cuentaLevantamiento.setCodDivisa(cuenta.getDivisa());
            cuentaLevantamiento.setEstadoCuenta(cuenta.getStatus());
        }
        else {
        	cuentaLevantamiento.setEstadoCuenta(EmbargosConstants.BANK_ACCOUNT_STATUS_NOTFOUND);
        }
        
        EstadoLevantamiento estadoLevantamiento = new EstadoLevantamiento();
        estadoLevantamiento.setCodEstado(EmbargosConstants.COD_ESTADO_LEVANTAMIENTO_PENDIENTE);
        cuentaLevantamiento.setEstadoLevantamiento(estadoLevantamiento);

        cuentaLevantamiento.setActuacion(codigoTipoLevantamiento);

        return cuentaLevantamiento;
    }

    private AccountDTO findBankAccount(CustomerDTO DWHCustomer, String iban)
    {
    	AccountDTO bankAccount = null;

        if (DWHCustomer!=null && DWHCustomer.getBankAccounts() != null && iban != null)
        {
            for (AccountDTO DWHBankAccount : DWHCustomer.getBankAccounts())
            {
                if (iban.equals(DWHBankAccount.getIban()))
                {
                    bankAccount = DWHBankAccount;
                    break;
                }
            }
        }

        return bankAccount;
    }

    private CuentaTraba findCuentaTraba(Traba traba, String cuenta)
    {
        for (CuentaTraba cuentaTraba : traba.getCuentaTrabas())
        {
            if (cuenta.equals(cuentaTraba.getCuenta()))
                return cuentaTraba;
        }

        return null;
    }
}
