package es.commerzbank.ice.embargos.service;


import java.math.BigDecimal;
import java.util.List;

import es.commerzbank.ice.embargos.domain.dto.SeizedBankAccountDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureActionDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureStatusDTO;
import es.commerzbank.ice.embargos.domain.entity.CuentaLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;

public interface SeizureService {

	public List<SeizureDTO> getSeizureListByCodeFileControl(Long codeFileControl);
	public SeizureDTO getSeizureById(Long idSeizure);
	public List<SeizedBankAccountDTO> getBankAccountListBySeizure(Long codeFileControl, Long idSeizure);
	public List<SeizureActionDTO> getSeizureActions(Long codeFileControl);
	public List<SeizureStatusDTO> getSeizureStatusList();
	public boolean updateSeizedBankAccountList(Long codeFileControl, Long idSeizure, List <SeizedBankAccountDTO> seizedBankAccountList, String userModif);
	public boolean updateSeizedBankStatus(CuentaTraba cuentaTraba, Long codEstado, String userModif);
	public boolean updateSeizedBankStatusTransaction(CuentaTraba cuentaTraba, Long codEstado, String userModif);
	public boolean updateSeizureStatus(Long idSeizure, SeizureStatusDTO seizureStatusDTO, String userModif);
	public boolean updateSeizureStatusTransaction(Long idSeizure, SeizureStatusDTO seizureStatusDTO, String userModif);
	public List<SeizureDTO> getAuditSeizure(Long codFileControl, Long idSeizure);
	public List<SeizedBankAccountDTO> getAuditSeizedBankAccounts(Long codFileControl, Long idSeizure, Long codAudit);
	
	public byte[] generateJustificanteEmbargo(Integer idSeizure) throws Exception;
	public byte[] generateLevantamientoReport(Integer idLifting) throws Exception;
	public byte[] generarResumenTrabasF3(Integer codControlFichero) throws Exception;
	public byte[] generarResumenTrabasF4(Integer codControlFichero) throws Exception;
	public byte[] generarAnexo(BigDecimal cod_usuario, BigDecimal cod_traba, Integer num_anexo) throws Exception;
}
