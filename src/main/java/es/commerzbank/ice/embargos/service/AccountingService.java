package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.embargos.domain.entity.FicheroFinal;

public interface AccountingService {

	void embargoContabilizar(Long codeFileControl, String userName) throws Exception;
	void levantamientoContabilizar(Long codeFileControl, String userName) throws Exception;
	void ficheroFinalContabilizar(FicheroFinal ficheroFinal, String userName) throws Exception;
	//es.commerzbank.ice.comun.lib.domain.entity.ControlFichero sendAccountingLiftingBankAccount(CuentaLevantamiento cuentaLevantamiento, Embargo embargo, String userName) throws ICEException, Exception;

	void embargoCallback(Long codCuentaTraba);
	void ficheroFinalCallback(Long codFicheroFinal);
	void levantamientoCallback(Long codCuentaLevantamiento) throws Exception;

	// boolean undoAccounting(Long codeFileControl, Long idSeizure, String numAccount, String userName) throws Exception;
}
