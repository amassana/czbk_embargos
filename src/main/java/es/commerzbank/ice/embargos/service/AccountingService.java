package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.domain.entity.CuentaLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.Embargo;

public interface AccountingService {

	void sendAccountingSeizure(Long codeFileControl, String userName) throws Exception;
	void sendAccountingLifting(Long codeFileControl, String userName) throws Exception;
	void sendAccountingFinalFile(Long codeFileControl, String userName) throws Exception;
	es.commerzbank.ice.comun.lib.domain.entity.ControlFichero sendAccountingLiftingBankAccount(CuentaLevantamiento cuentaLevantamiento, Embargo embargo, String userName) throws ICEException, Exception;

	void seizureCallback(Long codCuentaTraba);
	void transferenciaFinalOrganismoCallback(Long codControlFicheroFinal);
	void liftingCallback(Long codCuentaLevantamiento);

	// boolean undoAccounting(Long codeFileControl, Long idSeizure, String numAccount, String userName) throws Exception;
}
