package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.embargos.domain.entity.FicheroFinal;

public interface AccountingService {

	void embargoContabilizar(Long codeFileControl, String userName) throws Exception;
	void embargoCallback(Long codCuentaTraba);

	void levantamientoContabilizar(Long codeFileControl, String userName) throws Exception;
	void levantamientoCallback(Long codCuentaLevantamiento) throws Exception;

	void ficheroFinalContabilizar(FicheroFinal ficheroFinal, String userName) throws Exception;
	void ficheroFinalCallback(Long codFicheroFinal);
	//es.commerzbank.ice.comun.lib.domain.entity.ControlFichero sendAccountingLiftingBankAccount(CuentaLevantamiento cuentaLevantamiento, Embargo embargo, String userName) throws ICEException, Exception;

	void extornoContabilizar(ControlFichero controlFichero, Traba traba, CuentaTraba cuentaTraba, String userName) throws Exception;
	void extornoCallback(Long codCuentaTraba);
}
