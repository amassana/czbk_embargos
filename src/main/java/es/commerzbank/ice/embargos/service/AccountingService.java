package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.embargos.domain.dto.AccountingPendingDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.embargos.domain.entity.FicheroFinal;

import java.util.List;

public interface AccountingService {

	void embargoContabilizar(Long codeFileControl, String userName) throws Exception;
	void embargoCallback(Long codCuentaTraba) throws Exception;
	List<Long> embargoListaAContabilizar(Long codeFileControl) throws Exception;

	void levantamientoContabilizar(Long codeFileControl, String userName) throws Exception;
	void levantamientoCallback(Long codCuentaLevantamiento) throws Exception;
    List<Long> levantamientoListaAContabilizar(Long codeFileControl) throws Exception;

    void ficheroFinalContabilizar(FicheroFinal ficheroFinal, String userName) throws Exception;
	void ficheroFinalCallback(Long codFicheroFinal);
	//es.commerzbank.ice.comun.lib.domain.entity.ControlFichero sendAccountingLiftingBankAccount(CuentaLevantamiento cuentaLevantamiento, Embargo embargo, String userName) throws ICEException, Exception;

    long CGPJContabilizar(List<AccountingPendingDTO> pendientes, String userName) throws Exception;

    void extornoContabilizar(ControlFichero controlFichero, Traba traba, CuentaTraba cuentaTraba, String userName) throws Exception;
	void extornoCallback(Long codCuentaTraba);
}
