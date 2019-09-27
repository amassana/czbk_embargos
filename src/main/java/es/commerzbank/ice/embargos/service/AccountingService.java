package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.comun.lib.domain.dto.AccountingNote;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.domain.entity.CuentaLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.Embargo;

public interface AccountingService {

	public boolean sendAccounting(Long codeFileControl, String userName) throws ICEException, Exception;
	public boolean undoAccounting(Long codeFileControl, Long idSeizure, String numAccount, String userName) throws ICEException;
	public boolean manageAccountingNoteSeizureCallback(AccountingNote accountingNote, String userName);
	public boolean manageAccountingNoteLiftingCallback(AccountingNote accountingNote, String userName);
	public boolean sendAccountingLifting(Long codeFileControl, String userName) throws ICEException;
	public boolean sendAccountingLiftingBankAccount(CuentaLevantamiento cuentaLevantamiento, Embargo embargo, String userName) throws ICEException;
	
}
