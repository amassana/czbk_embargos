package es.commerzbank.ice.embargos.service.impl;

import es.commerzbank.ice.embargos.domain.dto.BankAccountDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacion;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacionCuenta;
import es.commerzbank.ice.embargos.domain.mapper.BankAccountMapper;
import es.commerzbank.ice.embargos.repository.InformationPetitionBankAccountRepository;
import es.commerzbank.ice.embargos.service.InformationPetitionBankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(transactionManager="transactionManager")
public class InformationPetitionBankAccountServiceImpl implements InformationPetitionBankAccountService{

	@Autowired
	BankAccountMapper bankAccountMapper;
	
	@Autowired
	InformationPetitionBankAccountRepository informationPetitionBankAccountRepository;
	
	@Override
	public List<BankAccountDTO> getAllByControlFicheroAndPeticionInformacion(Long codControlFichero,
			Long codPeticionInformacion) {
	
		List<BankAccountDTO> bankAccountList = new ArrayList<>();
		
		//TODO mirar si se tiene que hacer join con PeticionInformacion para utilizar ControlFichero
		ControlFichero controlFichero = new ControlFichero();
		controlFichero.setCodControlFichero(codControlFichero);
		
		PeticionInformacion peticionInformacion = new PeticionInformacion();
		peticionInformacion.setCodPeticion(codPeticionInformacion);
		
		List<PeticionInformacionCuenta> peticionInformacionCuentaList = informationPetitionBankAccountRepository.findByPeticionInformacionOrderByOrden(peticionInformacion);
		
		for (PeticionInformacionCuenta peticionInformacionCuenta : peticionInformacionCuentaList) {
			
			BankAccountDTO bankAccountDTO = bankAccountMapper.toBankAccountDTO(peticionInformacionCuenta);
			bankAccountList.add(bankAccountDTO);
		}
		
		return bankAccountList;
	}


}
