package es.commerzbank.ice.embargos.service;

import java.util.List;
import java.util.Map;

import es.commerzbank.ice.embargos.domain.dto.BankAccountDTO;
import es.commerzbank.ice.embargos.domain.dto.BankAccountLiftingDTO;
import es.commerzbank.ice.embargos.domain.dto.LiftingAuditDTO;
import es.commerzbank.ice.embargos.domain.dto.LiftingDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;

public interface LiftingService {

	List<LiftingDTO> getAllByControlFichero(ControlFichero controlFichero);

	List<BankAccountLiftingDTO> getAllByControlFicheroAndLevantamiento(Long codeFileControl, Long codeLifting);

	boolean saveLifting(Long codeFileControl, Long codeLifting, Map<String, Object> parametros, String userModif);

	List<LiftingAuditDTO> getAuditByCodeLiftingCase(Long codeLiftingCase);

}
