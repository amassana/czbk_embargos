package es.commerzbank.ice.embargos.service;

import java.util.List;

import es.commerzbank.ice.embargos.domain.dto.SeizedBankAccountDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureActionDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureStatusDTO;

public interface SeizureService {

	public List<SeizureDTO> getSeizureListByCodeFileControl(Long codeFileControl);
	public SeizureDTO getSeizureById(Long idSeizure);
	public List<SeizedBankAccountDTO> getBankAccountListBySeizure(Long codeFileControl, Long idSeizure);
	public List<SeizureActionDTO> getSeizureActions();
	public List<SeizureStatusDTO> getSeizureStatusList();
	public boolean updateSeizedBankAccountList(Long codeFileControl, Long idSeizure, List <SeizedBankAccountDTO> seizedBankAccountList, String userModif);
	public boolean updateSeizureStatus(Long codeFileControl, Long idSeizure, SeizureStatusDTO seizedBankAccountList, String userModif);
	public List<SeizureDTO> getAuditSeizure(Long codFileControl, Long idSeizure);
	public List<SeizedBankAccountDTO> getAuditSeizedBankAccounts(Long codFileControl, Long idSeizure, Long codAudit);
	
	public byte[] generateJustificanteEmbargo(Integer idSeizure) throws Exception;
	public byte[] generarResumenTrabas(Integer codControlFichero) throws Exception;
}
