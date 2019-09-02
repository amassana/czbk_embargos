package es.commerzbank.ice.embargos.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.embargos.config.OracleDataSourceEmbargosConfig;
import es.commerzbank.ice.embargos.domain.dto.BankAccountDTO;
import es.commerzbank.ice.embargos.domain.dto.BankAccountLiftingDTO;
import es.commerzbank.ice.embargos.domain.dto.LiftingAuditDTO;
import es.commerzbank.ice.embargos.domain.dto.LiftingDTO;
import es.commerzbank.ice.embargos.domain.dto.PetitionCaseDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.CuentaLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.HLevantamientoTraba;
import es.commerzbank.ice.embargos.domain.entity.HPeticionInformacion;
import es.commerzbank.ice.embargos.domain.entity.LevantamientoTraba;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacion;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacionCuenta;
import es.commerzbank.ice.embargos.domain.mapper.BankAccountLiftingMapper;
import es.commerzbank.ice.embargos.domain.mapper.BankAccountMapper;
import es.commerzbank.ice.embargos.domain.mapper.LiftingAuditMapper;
import es.commerzbank.ice.embargos.domain.mapper.LiftingMapper;
import es.commerzbank.ice.embargos.repository.LiftingAuditRepository;
import es.commerzbank.ice.embargos.repository.LiftingBankAccountRepository;
import es.commerzbank.ice.embargos.repository.LiftingRepository;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.LiftingService;

@Service
@Transactional(transactionManager="transactionManager")
public class LiftingServiceImpl implements LiftingService {

	private static final Logger LOG = LoggerFactory.getLogger(LiftingServiceImpl.class);
	
	@Autowired
	private OracleDataSourceEmbargosConfig oracleDataSourceEmbargosConfig;
	
	@Autowired
	private FileControlService fileControlService;
	
	@Autowired
	private LiftingMapper liftingMapper;
	
	@Autowired
	private LiftingRepository liftingRepository;
	
	@Autowired
	private LiftingBankAccountRepository liftingBankAccountRepository;
	
	@Autowired
	private BankAccountLiftingMapper bankAccountLiftingMapper;
	
	@Autowired
	private LiftingAuditRepository liftingAuditRepository;
	
	@Autowired
	private LiftingAuditMapper liftingAuditMapper;
	
	@Override
	public List<LiftingDTO> getAllByControlFichero(ControlFichero controlFichero) {
		//List<LevantamientoTraba> liftingList = liftingRepository.findAllByControlFichero(controlFichero);
		
		
		List<LiftingDTO> liftingDTOList = new ArrayList<>();
		/*for(LevantamientoTraba levantamiento : liftingList) {
			LiftingDTO lifting = liftingMapper.toLiftingDTO(levantamiento);
		
			liftingDTOList.add(lifting);
		}*/
		
		liftingDTOList = mockListaLevantamientos();
		
		return liftingDTOList;
	}

	private List<LiftingDTO> mockListaLevantamientos() {
		List<LiftingDTO> list = new ArrayList<LiftingDTO>();
		long id1 = 12219, id2 = 12239;
		double locked = 0;
		list.add(new LiftingDTO(id2, true, "Z87222017", "SEGURIDAD SOLUCIONES  SEGURIDAD", "B872220140311", 1608.65, locked, 1608.65, new Date(System.currentTimeMillis()), "AUTOMATICO"));
		list.add(new LiftingDTO(id1, false, "A80241789", "FERROVIAL SERVICIOS, S.A", "A802417890311", 208.47, locked, 208.47, new Date(System.currentTimeMillis()), "AUTOMATICO"));
		
		return list;
	}

	@Override
	public List<BankAccountLiftingDTO> getAllByControlFicheroAndLevantamiento(Long codeFileControl, Long codeLifting) {
		List<BankAccountLiftingDTO> bankAccountList = new ArrayList<>();
		
		//TODO mirar si se tiene que hacer join con PeticionInformacion para utilizar ControlFichero
		/*ControlFichero controlFichero = new ControlFichero();
		controlFichero.setCodControlFichero(codeFileControl);
		
		LevantamientoTraba levantamiento = new LevantamientoTraba();
		levantamiento.setCodLevantamiento(codeLifting);
		
		List<CuentaLevantamiento> cuentasLevantamiento = liftingBankAccountRepository.findByLevantamientoTraba(levantamiento);
		
		for (CuentaLevantamiento cuentaLevantamiento : cuentasLevantamiento) {
			
			BankAccountLiftingDTO bankAccountDTO = bankAccountLiftingMapper.toBankAccountLiftingDTO(cuentaLevantamiento);
			bankAccountList.add(bankAccountDTO);
		}*/
		
		bankAccountList = mockListBankAccount();
		
		return bankAccountList;
	}

	private List<BankAccountLiftingDTO> mockListBankAccount() {
		List<BankAccountLiftingDTO> list = new ArrayList<BankAccountLiftingDTO>();
		
		double change = 0;
		long id1 = 12858, id2 = 2;
		
		list.add(new BankAccountLiftingDTO(id1, "ES9801590001313661550978", "01590001313661550978", change, 208.47, "S", "1"));
		list.add(new BankAccountLiftingDTO(id2, "ES3401590001363667979978", "01590001363667979978", change, 1608.65, "S", "1"));
		
		return list;
	}

	@Override
	public boolean saveLifting(Long codeFileControl, Long codelifting, LiftingDTO lifting, String userModif) {
		return false;
	}

	@Override
	public List<LiftingAuditDTO> getAuditByCodeLiftingCase(Long codeLiftingCase) {
		//List<HLevantamientoTraba> hLevantamientoTrabaList = liftingAuditRepository.findByCodLevantamiento(codeLiftingCase);
		
		List<LiftingAuditDTO> liftingList = new ArrayList<>();
		/*for(HLevantamientoTraba hLevantamientoTraba : hLevantamientoTrabaList) {
			LiftingAuditDTO lifting = liftingAuditMapper.toInformationPetitionDTO(hLevantamientoTraba);
		
			liftingList.add(lifting);
		}*/
		
		return liftingList;
	}
}
