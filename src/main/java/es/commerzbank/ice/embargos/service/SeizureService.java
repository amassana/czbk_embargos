package es.commerzbank.ice.embargos.service;

import java.util.List;

import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.domain.dto.SeizedBankAccountDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureActionDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureSaveDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureStatusDTO;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.Embargo;

public interface SeizureService {

	public List<SeizureDTO> getSeizureListByCodeFileControl(Long codeFileControl);
	public SeizureDTO getSeizureById(Long idSeizure);
	public List<SeizedBankAccountDTO> getBankAccountListBySeizure(Long codeFileControl, Long idSeizure);
	public List<SeizureActionDTO> getSeizureActions(Long codeFileControl);
	public List<SeizureStatusDTO> getSeizureStatusList();
	public boolean updateSeizedBankAccountList(Long codeFileControl, Long idSeizure, SeizureSaveDTO seizureSave, String userModif) throws Exception;
	public boolean updateSeizedBankStatus(CuentaTraba cuentaTraba, Long codEstado, String userModif);
	public boolean updateSeizedBankStatusTransaction(CuentaTraba cuentaTraba, Long codEstado, String userModif);
	public boolean updateSeizureStatus(Long idSeizure, SeizureStatusDTO seizureStatusDTO, String userModif);
	public boolean updateSeizureStatusTransaction(Long idSeizure, SeizureStatusDTO seizureStatusDTO, String userModif);
	public List<SeizureDTO> getAuditSeizure(Long codFileControl, Long idSeizure);
	public List<SeizedBankAccountDTO> getAuditSeizedBankAccounts(Long codFileControl, Long idSeizure, Long codAudit);
	
	public byte[] generateSeizureLetter(Integer idSeizure) throws Exception;
	public byte[] generateSeizureRequestF3(Integer codControlFichero) throws Exception;
	public byte[] generateSeizureResponseF4(Integer codControlFichero) throws Exception;
	
	public List<Embargo> listEmbargosTransferToTax();
	public boolean jobTransferToTax(String authorization, String user) throws ICEException;
}
