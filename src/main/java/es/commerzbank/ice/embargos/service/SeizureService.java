package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.embargos.domain.dto.*;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;

import java.util.List;

public interface SeizureService {

	List<SeizureDTO> getSeizureListByCodeFileControl(Long codeFileControl);
	SeizureDTO getSeizureById(Long idSeizure);
	List<SeizedBankAccountDTO> getBankAccountListBySeizure(Long codeFileControl, Long idSeizure);
	List<SeizureActionDTO> getSeizureActions(Long codeFileControl);
	List<SeizureStatusDTO> getSeizureStatusList();
	boolean updateSeizedBankAccountList(Long codeFileControl, Long idSeizure, SeizureSaveDTO seizureSave, String userModif) throws Exception;
	boolean updateSeizedBankStatus(CuentaTraba cuentaTraba, Long codEstado, String userModif) throws Exception;
	// boolean updateSeizedBankStatusTransaction(CuentaTraba cuentaTraba, Long codEstado, String userModif);
	boolean updateSeizureStatus(Long idSeizure, SeizureStatusDTO seizureStatusDTO, String userModif) throws Exception;
	boolean updateSeizureStatusTransaction(Long idSeizure, SeizureStatusDTO seizureStatusDTO, String userModif) throws Exception;
	List<SeizureDTO> getAuditSeizure(Long codFileControl, Long idSeizure);
	List<SeizedBankAccountDTO> getAuditSeizedBankAccounts(Long codFileControl, Long idSeizure, Long codAudit);
	
	byte[] reportSeizureLetter(Long idSeizure) throws Exception;
	byte[] reportSeizureRequestF3(Integer codControlFichero, String oficina) throws Exception;
	byte[] reportSeizureResponseF4(Integer codControlFichero, String oficina) throws Exception;

    void generateSeizureLetters(ControlFichero controlFichero) throws Exception;
	boolean updateAccountSeizureStatus(Long idAccount, Long idSeizure, AccountStatusSeizedDTO accountStatusSeized, String userModif) throws Exception;

	boolean undoAccounting(Long codeFileControl, Long idSeizure, Long idAccount, String userName) throws Exception;
}
