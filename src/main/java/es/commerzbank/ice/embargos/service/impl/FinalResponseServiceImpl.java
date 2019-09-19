package es.commerzbank.ice.embargos.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.embargos.domain.dto.FinalResponseBankAccountDTO;
import es.commerzbank.ice.embargos.domain.dto.FinalResponseDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.CuentaResultadoEmbargo;
import es.commerzbank.ice.embargos.domain.entity.ResultadoEmbargo;
import es.commerzbank.ice.embargos.domain.mapper.FinalResponseBankAccountMapper;
import es.commerzbank.ice.embargos.domain.mapper.FinalResponseMapper;
import es.commerzbank.ice.embargos.repository.FinalResponseBankAccountRepository;
import es.commerzbank.ice.embargos.repository.FinalResponseRepository;
import es.commerzbank.ice.embargos.service.FinalResponseService;

@Service
@Transactional(transactionManager="transactionManager")
public class FinalResponseServiceImpl implements FinalResponseService {
	private static final Logger logger = LoggerFactory.getLogger(FinalResponseServiceImpl.class);
	
	@Autowired
	private FinalResponseRepository finalResponseRepository;
	
	@Autowired
	private FinalResponseMapper finalResponseMapper;
	
	@Autowired
	private FinalResponseBankAccountRepository bankAccountRepository;
	
	@Autowired
	private FinalResponseBankAccountMapper bankAccountMapper;

	@Override
	public List<FinalResponseDTO> getAllByControlFichero(ControlFichero controlFichero) {
		List<ResultadoEmbargo> result = finalResponseRepository.findAllByControlFichero(controlFichero); 
		
		List<FinalResponseDTO> response = new ArrayList<FinalResponseDTO>();
		for (ResultadoEmbargo resultado : result) {
			FinalResponseDTO element = finalResponseMapper.toFinalResponse(resultado);
			response.add(element);
		}
		
		return response;
	}

	@Override
	public FinalResponseDTO AddBankAccountList(Long codeFileControl, Long codeFinalResponse) {
		List<FinalResponseBankAccountDTO> list = new ArrayList<>();
		FinalResponseDTO response = null;
		
		Optional<ResultadoEmbargo> result = finalResponseRepository.findById(codeFinalResponse);
		
		if (result != null) {
			response = finalResponseMapper.toFinalResponse(result.get());
			
			ResultadoEmbargo resultado = new ResultadoEmbargo();
			resultado.setCodResultadoEmbargo(codeFinalResponse);
			
			List<CuentaResultadoEmbargo> cuentas = bankAccountRepository.findByResultadoEmbargo(resultado);
			
			for (CuentaResultadoEmbargo cuenta : cuentas) {
				FinalResponseBankAccountDTO account = bankAccountMapper.toBankAccount(cuenta);
				account.setAmountRaised(account.getAmountLocked() - account.getAmountTransfer());
				list.add(account);
			}
			
			response.setList(list);
		}
		
		return response;
	}

}
