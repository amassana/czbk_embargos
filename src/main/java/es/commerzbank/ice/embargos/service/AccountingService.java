package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.comun.lib.domain.dto.AccountingNote;
import es.commerzbank.ice.comun.lib.util.ICEException;

public interface AccountingService {

	public boolean sendAccounting(Long codeFileControl, String userName) throws ICEException;
	public boolean undoAccounting(Long codeFileControl, Long idSeizure, String numAccount, String userName) throws ICEException;
	public boolean manageAccountingNoteCallback(AccountingNote accountingNote, String userName);
	public boolean sendAccountingLifting(Long codeFileControl, String userName) throws ICEException;
	
}
