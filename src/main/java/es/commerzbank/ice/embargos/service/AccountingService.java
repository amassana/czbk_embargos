package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.Traba;

public interface AccountingService {

	void embargoContabilizar(Long codeFileControl, String userName) throws Exception;
	void levantamientoContabilizar(Long codeFileControl, String userName) throws Exception;
	void sendFinalFile(Long codeFileControl, String userName) throws Exception;
	//es.commerzbank.ice.comun.lib.domain.entity.ControlFichero sendAccountingLiftingBankAccount(CuentaLevantamiento cuentaLevantamiento, Embargo embargo, String userName) throws ICEException, Exception;

	void embargoCallback(Long codCuentaTraba);
	void transferenciaFinalOrganismoCallback(Long codControlFicheroFinal);
	void levantamientoCallback(Long codCuentaLevantamiento) throws Exception;

	boolean undoAccounting(ControlFichero controlFichero, Traba traba, CuentaTraba cuentaTraba, String userName) throws Exception;
}
