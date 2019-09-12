package es.commerzbank.ice.embargos.service;


import java.math.BigDecimal;
import java.util.List;

import es.commerzbank.ice.comun.lib.formats.contabilidad.RespuestaContabilidad;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.domain.dto.SeizedBankAccountDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureActionDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureStatusDTO;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;

public interface SeizureService {

	public List<SeizureDTO> getSeizureListByCodeFileControl(Long codeFileControl);
	public SeizureDTO getSeizureById(Long idSeizure);
	public List<SeizedBankAccountDTO> getBankAccountListBySeizure(Long codeFileControl, Long idSeizure);
	public List<SeizureActionDTO> getSeizureActions();
	public List<SeizureStatusDTO> getSeizureStatusList();
	public boolean updateSeizedBankAccountList(Long codeFileControl, Long idSeizure, List <SeizedBankAccountDTO> seizedBankAccountList, String userModif);
	public boolean updateSeizedBankStatus(CuentaTraba cuentaTraba, Long codEstado, String userModif);
	public boolean updateSeizureStatus(Long codeFileControl, Long idSeizure, SeizureStatusDTO seizedBankAccountList, String userModif);
	public List<SeizureDTO> getAuditSeizure(Long codFileControl, Long idSeizure);
	public List<SeizedBankAccountDTO> getAuditSeizedBankAccounts(Long codFileControl, Long idSeizure, Long codAudit);
	public boolean sendAccounting(Long codeFileControl, String userName) throws ICEException;
	public boolean undoAccounting(Long codeFileControl, Long idSeizure, String numAccount, String userName);
	public boolean manageAccountingNoteCallback(RespuestaContabilidad respuestaContabilidad, String userName);
	
	public byte[] generateJustificanteEmbargo(Integer idSeizure) throws Exception;
	public byte[] generateLevantamientoReport(Integer idLifting) throws Exception;
	public byte[] generarResumenTrabasF3(Integer codControlFichero) throws Exception;
	public byte[] generarResumenTrabasF4(Integer codControlFichero) throws Exception;
	public byte[] generarAnexo(BigDecimal cod_usuario, BigDecimal cod_traba, Integer num_anexo) throws Exception;
}
