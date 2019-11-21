package es.commerzbank.ice.embargos.service;

import java.util.List;

import es.commerzbank.ice.embargos.domain.dto.LiftingAuditDTO;
import es.commerzbank.ice.embargos.domain.dto.LiftingDTO;
import es.commerzbank.ice.embargos.domain.dto.LiftingManualDTO;
import es.commerzbank.ice.embargos.domain.dto.LiftingStatusDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.CuentaLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.LevantamientoTraba;

public interface LiftingService {

	List<LiftingDTO> getAllByControlFichero(ControlFichero controlFichero);

	LiftingDTO getAllByControlFicheroAndLevantamiento(Long codeFileControl, Long codeLifting);

	boolean saveLifting(Long codeFileControl, Long codeLifting, LiftingDTO lifting, String userModif) throws Exception;

	List<LiftingAuditDTO> getAuditByCodeLiftingCase(Long codeLiftingCase);

	List<LiftingStatusDTO> getListStatus();

	boolean changeStatus(Long codeLifting, Long status, String userName) throws Exception;

	void updateLiftingBankAccountingStatus(CuentaLevantamiento cuenta, long codEstado, String userName);

	public void updateLiftingtatus(LevantamientoTraba levantamientoTraba, long codEstado, String userName);

	public byte[] generarResumenLevantamientoF5(Integer cod_file_control) throws Exception;

	public byte[] generateLiftingLetter(Integer idLifting) throws Exception;
	
	boolean manualLifting(LiftingManualDTO liftingManualDTO, String userModif) throws Exception;
}