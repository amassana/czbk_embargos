package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.domain.dto.*;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.Embargo;

import java.util.List;

public interface SeizureService {

	List<SeizureDTO> getSeizureListByCodeFileControl(Long codeFileControl);
	SeizureDTO getSeizureById(Long idSeizure);
	List<SeizedBankAccountDTO> getBankAccountListBySeizure(Long codeFileControl, Long idSeizure);
	List<SeizureActionDTO> getSeizureActions(Long codeFileControl);
	List<SeizureStatusDTO> getSeizureStatusList();
	boolean updateSeizedBankAccountList(Long codeFileControl, Long idSeizure, SeizureSaveDTO seizureSave, String userModif) throws Exception;
	boolean updateSeizedBankStatus(CuentaTraba cuentaTraba, Long codEstado, String userModif);
	boolean updateSeizedBankStatusTransaction(CuentaTraba cuentaTraba, Long codEstado, String userModif);
	boolean updateSeizureStatus(Long idSeizure, SeizureStatusDTO seizureStatusDTO, String userModif);
	boolean updateSeizureStatusTransaction(Long idSeizure, SeizureStatusDTO seizureStatusDTO, String userModif);
	List<SeizureDTO> getAuditSeizure(Long codFileControl, Long idSeizure);
	List<SeizedBankAccountDTO> getAuditSeizedBankAccounts(Long codFileControl, Long idSeizure, Long codAudit);
	
	byte[] reportSeizureLetter(Long idSeizure) throws Exception;
	byte[] reportSeizureRequestF3(Integer codControlFichero) throws Exception;
	byte[] reportSeizureResponseF4(Integer codControlFichero) throws Exception;
	
	List<Embargo> listEmbargosTransferToTax();
	boolean jobTransferToTax(String authorization, String user) throws ICEException;

    void generateSeizureLetters(ControlFichero controlFichero) throws Exception;
}
