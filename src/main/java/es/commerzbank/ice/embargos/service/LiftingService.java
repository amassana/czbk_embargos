package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.embargos.domain.dto.*;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;

import java.util.List;

public interface LiftingService {

	List<LiftingDTO> getAllByControlFichero(ControlFichero controlFichero);

	LiftingDTO getAllByControlFicheroAndLevantamiento(Long codeFileControl, Long codeLifting);

	boolean saveLifting(Long codeFileControl, Long codeLifting, LiftingDTO lifting, String userModif) throws Exception;

	List<LiftingAuditDTO> getAuditByCodeLiftingCase(Long codeLiftingCase);

	List<LiftingStatusDTO> getListStatus();

	boolean changeStatus(Long codeLifting, Long status, String userName) throws Exception;

	//void updateLiftingBankAccountingStatus(CuentaLevantamiento cuenta, long codEstado, String userName);

	//void updateLiftingtatus(LevantamientoTraba levantamientoTraba, long codEstado, String userName);

	byte[] generarResumenLevantamientoF5(Integer codControlFichero, String oficina) throws Exception;

	byte[] generateLiftingLetter(Long idLifting) throws Exception;
	
	boolean manualLifting(LiftingManualDTO liftingManualDTO, String userModif) throws Exception;

	void generateLiftingLetters(ControlFichero pendiente) throws Exception;

	boolean updateAccountLiftingStatus(Long idAccount, AccountStatusLiftingDTO accountStatusLifting,
			String userModif) throws Exception;

    byte[] previewContable(Long codeFileControl) throws Exception;
}