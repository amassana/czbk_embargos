package es.commerzbank.ice.embargos.service;

public interface AccountingService {

	void embargoContabilizar(Long codeFileControl, String userName) throws Exception;
	void levantamientoContabilizar(Long codeFileControl, String userName) throws Exception;
	void sendFinalFile(Long codeFileControl, String userName) throws Exception;
	//es.commerzbank.ice.comun.lib.domain.entity.ControlFichero sendAccountingLiftingBankAccount(CuentaLevantamiento cuentaLevantamiento, Embargo embargo, String userName) throws ICEException, Exception;

	void embargoCallback(Long codCuentaTraba);
	void transferenciaFinalOrganismoCallback(Long codControlFicheroFinal);
	void levantamientoCallback(Long codCuentaLevantamiento) throws Exception;

	// boolean undoAccounting(Long codeFileControl, Long idSeizure, String numAccount, String userName) throws Exception;
}
