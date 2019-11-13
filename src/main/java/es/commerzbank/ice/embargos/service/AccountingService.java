package es.commerzbank.ice.embargos.service;

import java.text.ParseException;

import es.commerzbank.ice.comun.lib.domain.dto.AccountingNote;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.domain.entity.CuentaLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.Embargo;

public interface AccountingService {

	public boolean sendAccountingSeizure(Long codeFileControl, String userName) throws ICEException, Exception;
	public boolean sendAccountingLifting(Long codeFileControl, String userName) throws ICEException, Exception;
	public Long sendAccountingLiftingBankAccount(CuentaLevantamiento cuentaLevantamiento, Embargo embargo, String userName) throws ICEException, Exception;
	public boolean sendAccountingFinalFile(Long codeFileControl, String userName) throws ICEException, Exception;

	public boolean manageAccountingNoteSeizureCallback(AccountingNote accountingNote, String userName) throws ParseException, ICEException;
	public boolean manageAccountingNoteLiftingCallback(AccountingNote accountingNote, String userName) throws ParseException, ICEException;
	public boolean manageAccountingNoteFinalFileCallback(AccountingNote accountingNote, String userName) throws ICEException;
	
	public boolean undoAccounting(Long codeFileControl, Long idSeizure, String numAccount, String userName) throws ICEException, ParseException;
}
