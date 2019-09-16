package es.commerzbank.ice.embargos.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import es.commerzbank.ice.embargos.domain.dto.LiftingStatusDTO;
import es.commerzbank.ice.embargos.domain.dto.PetitionCaseDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.CuentaLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.EstadoIntLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.HLevantamientoTraba;
import es.commerzbank.ice.embargos.domain.entity.HPeticionInformacion;
import es.commerzbank.ice.embargos.domain.entity.LevantamientoTraba;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacion;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacionCuenta;
import es.commerzbank.ice.embargos.domain.mapper.BankAccountLiftingMapper;
import es.commerzbank.ice.embargos.domain.mapper.BankAccountMapper;
import es.commerzbank.ice.embargos.domain.mapper.LiftingAuditMapper;
import es.commerzbank.ice.embargos.domain.mapper.LiftingMapper;
import es.commerzbank.ice.embargos.domain.mapper.LiftingStatusMapper;
import es.commerzbank.ice.embargos.repository.LiftingAuditRepository;
import es.commerzbank.ice.embargos.repository.LiftingBankAccountRepository;
import es.commerzbank.ice.embargos.repository.LiftingRepository;
import es.commerzbank.ice.embargos.repository.LiftingStatusRepository;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.LiftingService;
import es.commerzbank.ice.utils.EmbargosConstants;

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
	
	@Autowired
	private LiftingStatusRepository liftingStatusRepository;
	
	@Autowired
	private LiftingStatusMapper liftingStatusMapper;
	
	@Override
	public List<LiftingDTO> getAllByControlFichero(ControlFichero controlFichero) {
		List<LevantamientoTraba> liftingList = liftingRepository.findAllByControlFichero(controlFichero);
		
		
		List<LiftingDTO> liftingDTOList = new ArrayList<>();
		for(LevantamientoTraba levantamiento : liftingList) {
			LiftingDTO lifting = liftingMapper.toLiftingDTO(levantamiento);
			Optional<EstadoIntLevantamiento> estado = liftingStatusRepository.findById(levantamiento.getEstadoContable().longValue());
			if (estado != null) {
				LiftingStatusDTO status = liftingStatusMapper.toLiftingStatus(estado.get());
				lifting.setStatus(status);
			}
			
		
			liftingDTOList.add(lifting);
		}
		
		return liftingDTOList;
	}

	@Override
	public LiftingDTO getAllByControlFicheroAndLevantamiento(Long codeFileControl, Long codeLifting) {
		List<BankAccountLiftingDTO> bankAccountList = new ArrayList<>();
		LiftingDTO response = null;
		
		Optional<LevantamientoTraba> result = liftingRepository.findById(codeLifting);
		
		if (result != null) {
			
			response = liftingMapper.toLiftingDTO(result.get());
			
			//TODO mirar si se tiene que hacer join con PeticionInformacion para utilizar ControlFichero
			ControlFichero controlFichero = new ControlFichero();
			controlFichero.setCodControlFichero(codeFileControl);
			
			LevantamientoTraba levantamiento = new LevantamientoTraba();
			levantamiento.setCodLevantamiento(codeLifting);
			
			List<CuentaLevantamiento> cuentasLevantamiento = liftingBankAccountRepository.findByLevantamientoTraba(levantamiento);
			
			Double importeLevantado = (double) 0;
			
			for (CuentaLevantamiento cuentaLevantamiento : cuentasLevantamiento) {
				
				BankAccountLiftingDTO bankAccountDTO = bankAccountLiftingMapper.toBankAccountLiftingDTO(cuentaLevantamiento);
				bankAccountList.add(bankAccountDTO);
				
				importeLevantado += bankAccountDTO.getAmount(); 
			}
			
			response.setAmountRaised(importeLevantado);
			response.setAccounts(bankAccountList);
		}
		
		return response;
	}

	@Override
	public boolean saveLifting(Long codeFileControl, Long codelifting, LiftingDTO lifting, String userModif) throws Exception {
		boolean response = true; 
		
		if (lifting != null) {
			
			liftingRepository.updateStatus(codelifting, lifting.getStatus().getCode());
			
			LevantamientoTraba levantamiento = new LevantamientoTraba();
			levantamiento.setCodLevantamiento(codelifting);
			
			if (lifting.getAccounts() != null) {
				List<BankAccountLiftingDTO> list = lifting.getAccounts();
				for (BankAccountLiftingDTO account : list) {
					CuentaLevantamiento cuenta = bankAccountLiftingMapper.toCuentaLevantamiento(account);
					cuenta.setLevantamientoTraba(levantamiento);
					
					liftingBankAccountRepository.save(cuenta);
				}
			}
		} else {
			response = false;
		}
		
		return response;
	}

	@Override
	public List<LiftingAuditDTO> getAuditByCodeLiftingCase(Long codeLiftingCase) {
		/*List<HLevantamientoTraba> hLevantamientoTrabaList = liftingAuditRepository.findByCodLevantamiento(codeLiftingCase);
		
		List<LiftingAuditDTO> liftingList = new ArrayList<>();
		for(HLevantamientoTraba hLevantamientoTraba : hLevantamientoTrabaList) {
			LiftingAuditDTO lifting = liftingAuditMapper.toInformationPetitionDTO(hLevantamientoTraba);
		
			liftingList.add(lifting);
		}
		
		return liftingList;*/
		
		return null;
	}

	@Override
	public List<LiftingStatusDTO> getListStatus() {
		List<LiftingStatusDTO> response = null;
		List<EstadoIntLevantamiento> list = null;
		
		list = liftingStatusRepository.findAll();
		
		if (list != null && list.size() > 0) {
			response = new ArrayList<LiftingStatusDTO>();
			
			for (EstadoIntLevantamiento estado : list) {
				response.add(liftingStatusMapper.toLiftingStatus(estado));
			}
		}
		
		return response;
	}
}
