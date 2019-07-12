package es.commerzbank.ice.embargos.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.commerzbank.ice.embargos.domain.dto.BankAccountDTO;
import es.commerzbank.ice.embargos.domain.dto.PetitionCaseDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacion;
import es.commerzbank.ice.embargos.domain.mapper.InformationPetitionMapper;
import es.commerzbank.ice.embargos.repository.InformationPetitionRepository;
import es.commerzbank.ice.embargos.service.CustomerService;
import es.commerzbank.ice.embargos.service.InformationPetitionService;
import es.commerzbank.ice.utils.EmbargosConstants;

@Service
@Transactional
public class InformationPetitionServiceImpl implements InformationPetitionService{

	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private InformationPetitionMapper informationPetitionMapper;
	
	@Autowired
	private InformationPetitionRepository informationPetitionRepository;

	@Override
	public PetitionCaseDTO getByCodeInformationPetition(String codeInformationPetition) {

		Optional<PeticionInformacion> peticionInformacionOpt = informationPetitionRepository.findById(codeInformationPetition);
		
		if(!peticionInformacionOpt.isPresent()) {
			return null;

		}
		//TODO cambiar parametro mockeados:
		
		PeticionInformacion peticionInformacion = peticionInformacionOpt.get();
			
		
		return informationPetitionMapper.toInformationPetitionDTO(peticionInformacion, "02", "Activa");
	}
	
	@Override
	public boolean saveSelectedAccounts(String codeInformationPetition, String nif, Boolean revised, List<String> codeAccountList) {

		//Getting the PetitionInformation with "codePetition":
		Optional<PeticionInformacion> peticionInformacionOpt = informationPetitionRepository.findById(codeInformationPetition);
		
		if (!peticionInformacionOpt.isPresent()) {
			return false;
		}
		
		PeticionInformacion peticionInformacion = peticionInformacionOpt.get();

		//Getting the customer account list:
		List<BankAccountDTO> listAccounts = customerService.listAllAccountsByNif(nif);
		
		//Organize Accounts in a hashmap, where codeAccount is the key:
		Map<String,BankAccountDTO> accountsMap = new HashMap<>();
		for (BankAccountDTO bankAccountDTO : listAccounts) {
			accountsMap.put(bankAccountDTO.getCodeBankAccount(), bankAccountDTO);
		}
		
		//Antes de asignar las cuentas seleccionadas, resetear las cuentas antiguas:
		resetPetitionCustomerAccounts(peticionInformacion);
		
		//Iterating by the selected accounts:
		int i=1;
		for (String codeAccount : codeAccountList) {
			
			BankAccountDTO bankAccountDTO = accountsMap.get(codeAccount);
			
			if (bankAccountDTO!=null) {
				switch(i) {
					case 1:
						peticionInformacion.setCuenta1(bankAccountDTO.getCodeBankAccount());
						peticionInformacion.setIban1(bankAccountDTO.getIban());
						peticionInformacion.setClaveSeguridad1(generateClaveSeguridad(bankAccountDTO.getIban()));
						break;
					case 2:
						peticionInformacion.setCuenta2(bankAccountDTO.getCodeBankAccount());
						peticionInformacion.setIban2(bankAccountDTO.getIban());
						peticionInformacion.setClaveSeguridad2(generateClaveSeguridad(bankAccountDTO.getIban()));
						break;
					case 3:
						peticionInformacion.setCuenta3(bankAccountDTO.getCodeBankAccount());
						peticionInformacion.setIban3(bankAccountDTO.getIban());
						peticionInformacion.setClaveSeguridad3(generateClaveSeguridad(bankAccountDTO.getIban()));
						break;
					case 4:
						peticionInformacion.setCuenta4(bankAccountDTO.getCodeBankAccount());
						peticionInformacion.setIban4(bankAccountDTO.getIban());
						peticionInformacion.setClaveSeguridad4(generateClaveSeguridad(bankAccountDTO.getIban()));
						break;
					case 5:
						peticionInformacion.setCuenta5(bankAccountDTO.getCodeBankAccount());
						peticionInformacion.setIban5(bankAccountDTO.getIban());
						peticionInformacion.setClaveSeguridad5(generateClaveSeguridad(bankAccountDTO.getIban()));
						break;
					case 6:
						peticionInformacion.setCuenta6(bankAccountDTO.getCodeBankAccount());
						peticionInformacion.setIban6(bankAccountDTO.getIban());
						peticionInformacion.setClaveSeguridad6(generateClaveSeguridad(bankAccountDTO.getIban()));
						break;
					default:
				}
				i++;
			}
		}
		
		//Flag de peticion de informacion revisada:
		String revisedStr = (revised!=null && revised.booleanValue()) ? EmbargosConstants.IND_FLAG_YES : EmbargosConstants.IND_FLAG_NO;
		peticionInformacion.setIndCasoRevisado(revisedStr);
		
		informationPetitionRepository.save(peticionInformacion);
		
		return true;
	}
	
	private void resetPetitionCustomerAccounts(PeticionInformacion peticionInformacion) {
	
		peticionInformacion.setCuenta1(null);
		peticionInformacion.setCuenta2(null);
		peticionInformacion.setCuenta3(null);
		peticionInformacion.setCuenta4(null);
		peticionInformacion.setCuenta5(null);
		peticionInformacion.setCuenta6(null);
		
		peticionInformacion.setIban1(null);
		peticionInformacion.setIban2(null);
		peticionInformacion.setIban3(null);
		peticionInformacion.setIban4(null);
		peticionInformacion.setIban5(null);
		peticionInformacion.setIban6(null);
		
		peticionInformacion.setClaveSeguridad1(null);
		peticionInformacion.setClaveSeguridad2(null);
		peticionInformacion.setClaveSeguridad3(null);
		peticionInformacion.setClaveSeguridad4(null);
		peticionInformacion.setClaveSeguridad5(null);
		peticionInformacion.setClaveSeguridad6(null);
	}
	
	private String generateClaveSeguridad(String iban) {

		//La clave consiste en la fecha del proceso de generacion en formato aaaammdd, seguido
		//de los 4 primeros digitos de la cuenta y rellenando con ceros el resto del campo:
		
		if(iban==null || iban.length() < 4) {
			return null;
		}
		
		StringBuilder result = new StringBuilder();
	
		String actualLocalDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		
		result.append(actualLocalDate).append(iban.substring(0, 4));
		
		return result.toString();
		
	}

	@Override
	public List<PetitionCaseDTO> getAllByControlFichero(ControlFichero controlFichero) {

		List<PeticionInformacion> peticionInformacionList = informationPetitionRepository.findAllByControlFichero(controlFichero);
		
		List<PetitionCaseDTO> informationPetitionDTOList = new ArrayList<>();
		for(PeticionInformacion peticionInformacion : peticionInformacionList) {
			PetitionCaseDTO informationPetition = informationPetitionMapper.toInformationPetitionDTO(peticionInformacion, "02", "Activa");
		
			informationPetitionDTOList.add(informationPetition);
		}
		
		return informationPetitionDTOList;
	}

	@Override
	public Integer getCountPendingPetitionCases(ControlFichero controlFichero) {
		
		return informationPetitionRepository.countByControlFicheroAndIndCasoRevisadoOrIndCasoRevisadoNull(controlFichero, EmbargosConstants.IND_FLAG_NO);

	}

	@Override
	public Integer getCountReviewedPetitionCases(ControlFichero controlFichero) {
		
		return informationPetitionRepository.countByControlFicheroAndIndCasoRevisado(controlFichero, EmbargosConstants.IND_FLAG_YES);

	}
	
	@Override
	public Integer getCountPetitionCases(ControlFichero controlFichero) {
		
		return informationPetitionRepository.countByControlFichero(controlFichero);

	}
	
	@Override
	public boolean savePetitionCase(Long codeFileControl, String codePetitionCase, PetitionCaseDTO petitionCase, String userModif) {

		
		Optional<PeticionInformacion> peticionInformacionOpt = informationPetitionRepository.findById(codePetitionCase);
		
		if (!peticionInformacionOpt.isPresent()) {
			return false;
		}
		
		PeticionInformacion peticionInformacion = peticionInformacionOpt.get();
		
		//Antes de asignar las cuentas seleccionadas, resetear las cuentas antiguas:
		resetPetitionCustomerAccounts(peticionInformacion);
		
		//Seteo de las cuentas seleccionadas:
		if (petitionCase.getBankAccount1()!=null) {
			peticionInformacion.setCuenta1(petitionCase.getBankAccount1().getCodeBankAccount());
			peticionInformacion.setIban1(petitionCase.getBankAccount1().getIban());
			peticionInformacion.setClaveSeguridad1(generateClaveSeguridad(petitionCase.getBankAccount1().getIban()));
		}
		if (petitionCase.getBankAccount2()!=null) {
			peticionInformacion.setCuenta2(petitionCase.getBankAccount2().getCodeBankAccount());
			peticionInformacion.setIban2(petitionCase.getBankAccount2().getIban());
			peticionInformacion.setClaveSeguridad2(generateClaveSeguridad(petitionCase.getBankAccount2().getIban()));
		}
		if (petitionCase.getBankAccount3()!=null) {
			peticionInformacion.setCuenta3(petitionCase.getBankAccount3().getCodeBankAccount());
			peticionInformacion.setIban3(petitionCase.getBankAccount3().getIban());
			peticionInformacion.setClaveSeguridad3(generateClaveSeguridad(petitionCase.getBankAccount3().getIban()));
		}
		if (petitionCase.getBankAccount4()!=null) {
			peticionInformacion.setCuenta4(petitionCase.getBankAccount4().getCodeBankAccount());
			peticionInformacion.setIban4(petitionCase.getBankAccount4().getIban());
			peticionInformacion.setClaveSeguridad4(generateClaveSeguridad(petitionCase.getBankAccount4().getIban()));
		}
		if (petitionCase.getBankAccount5()!=null) {
			peticionInformacion.setCuenta5(petitionCase.getBankAccount5().getCodeBankAccount());
			peticionInformacion.setIban5(petitionCase.getBankAccount5().getIban());
			peticionInformacion.setClaveSeguridad5(generateClaveSeguridad(petitionCase.getBankAccount5().getIban()));
		}
		if (petitionCase.getBankAccount6()!=null) {
			peticionInformacion.setCuenta6(petitionCase.getBankAccount6().getCodeBankAccount());
			peticionInformacion.setIban6(petitionCase.getBankAccount6().getIban());
			peticionInformacion.setClaveSeguridad6(generateClaveSeguridad(petitionCase.getBankAccount6().getIban()));
		}
		
		//Flag de revisado:
		if (petitionCase.getIsReviewed()!=null && petitionCase.getIsReviewed()) {
			peticionInformacion.setIndCasoRevisado(EmbargosConstants.IND_FLAG_YES);
		} else {
			peticionInformacion.setIndCasoRevisado(EmbargosConstants.IND_FLAG_NO);
		}
		
        //Usuario y fecha ultima modificacion:
		peticionInformacion.setUsuarioUltModificacion(userModif);
        peticionInformacion.setFUltimaModificacion(BigDecimal.valueOf(System.currentTimeMillis()));
		
		informationPetitionRepository.save(peticionInformacion);
		
		return true;
	}

}
