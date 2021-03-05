package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.domain.entity.CuentaLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.Embargo;

public interface AccountingService {

	void sendSeizure(Long codeFileControl, String userName) throws Exception;
	void sendLifting(Long codeFileControl, String userName) throws Exception;
	void sendFinalFile(Long codeFileControl, String userName) throws Exception;
	es.commerzbank.ice.comun.lib.domain.entity.ControlFichero sendAccountingLiftingBankAccount(CuentaLevantamiento cuentaLevantamiento, Embargo embargo, String userName) throws ICEException, Exception;

	void seizureCallback(Long codCuentaTraba);
	void transferenciaFinalOrganismoCallback(Long codControlFicheroFinal);
	void liftingCallback(Long codCuentaLevantamiento);

	// boolean undoAccounting(Long codeFileControl, Long idSeizure, String numAccount, String userName) throws Exception;
}
