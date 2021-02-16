package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.PetitionCaseDTO;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacion;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacionCuenta;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;

@Mapper(componentModel="spring")
public abstract class InformationPetitionMapper {

	@Mappings({
		@Mapping(source = "peticionInformacion.codPeticion", target = "codePetitionCase"),
		@Mapping(source = "peticionInformacion.razonSocial", target = "name"),
		@Mapping(source = "peticionInformacion.razonSocial", target = "nameInternal"),
		@Mapping(source = "peticionInformacion.nif", target = "nif"),
		@Mapping(source = "peticionInformacion.cuenta1", target = "bankAccount1.codeBankAccount"),
		@Mapping(source = "peticionInformacion.cuenta2", target = "bankAccount2.codeBankAccount"),
		@Mapping(source = "peticionInformacion.cuenta3", target = "bankAccount3.codeBankAccount"),
		@Mapping(source = "peticionInformacion.cuenta4", target = "bankAccount4.codeBankAccount"),
		@Mapping(source = "peticionInformacion.cuenta5", target = "bankAccount5.codeBankAccount"),
		@Mapping(source = "peticionInformacion.cuenta6", target = "bankAccount6.codeBankAccount"),
		@Mapping(source = "peticionInformacion.iban1", target = "bankAccount1.iban"),
		@Mapping(source = "peticionInformacion.iban2", target = "bankAccount2.iban"),
		@Mapping(source = "peticionInformacion.iban3", target = "bankAccount3.iban"),
		@Mapping(source = "peticionInformacion.iban4", target = "bankAccount4.iban"),
		@Mapping(source = "peticionInformacion.iban5", target = "bankAccount5.iban"),
		@Mapping(source = "peticionInformacion.iban6", target = "bankAccount6.iban")
	})
	public abstract PetitionCaseDTO toInformationPetitionDTO(PeticionInformacion peticionInformacion);
	
	@AfterMapping
	protected void setInformationPetitionDTOAfterMapping(PeticionInformacion peticionInformacion, @MappingTarget PetitionCaseDTO petitionCaseDTO) {
		
		//Indicador de si el caso ha sido revisado:
		String indCasoRevisado = peticionInformacion.getIndCasoRevisado();
		boolean isReviewed = indCasoRevisado!=null && EmbargosConstants.IND_FLAG_YES.equals(indCasoRevisado);
		petitionCaseDTO.setIsReviewed(isReviewed);
				
		//Estados de las cuentas:
		if (peticionInformacion.getPeticionInformacionCuentas()!=null) {
			for (PeticionInformacionCuenta peticionInformacionCuentas : peticionInformacion.getPeticionInformacionCuentas()) {
				
				if (petitionCaseDTO.getBankAccount1() != null && petitionCaseDTO.getBankAccount1().getCodeBankAccount() !=null 
						&& petitionCaseDTO.getBankAccount1().getCodeBankAccount().equals(peticionInformacionCuentas.getCuenta())) {
					
					petitionCaseDTO.getBankAccount1().setStatus(peticionInformacionCuentas.getEstadoCuenta());
				
				} else if (petitionCaseDTO.getBankAccount2() != null && petitionCaseDTO.getBankAccount2().getCodeBankAccount() !=null 
						&& petitionCaseDTO.getBankAccount2().getCodeBankAccount().equals(peticionInformacionCuentas.getCuenta())) {
		
					petitionCaseDTO.getBankAccount2().setStatus(peticionInformacionCuentas.getEstadoCuenta());
		
				} else if (petitionCaseDTO.getBankAccount3() != null && petitionCaseDTO.getBankAccount3().getCodeBankAccount() !=null 
						&& petitionCaseDTO.getBankAccount3().getCodeBankAccount().equals(peticionInformacionCuentas.getCuenta())) {
					
					petitionCaseDTO.getBankAccount3().setStatus(peticionInformacionCuentas.getEstadoCuenta());
				
				} else if (petitionCaseDTO.getBankAccount4() != null && petitionCaseDTO.getBankAccount4().getCodeBankAccount() !=null 
						&& petitionCaseDTO.getBankAccount4().getCodeBankAccount().equals(peticionInformacionCuentas.getCuenta())) {
					
					petitionCaseDTO.getBankAccount4().setStatus(peticionInformacionCuentas.getEstadoCuenta());
				
				} else if (petitionCaseDTO.getBankAccount5() != null && petitionCaseDTO.getBankAccount5().getCodeBankAccount() !=null 
						&& petitionCaseDTO.getBankAccount5().getCodeBankAccount().equals(peticionInformacionCuentas.getCuenta())) {
					
					petitionCaseDTO.getBankAccount5().setStatus(peticionInformacionCuentas.getEstadoCuenta());
				
				} else if (petitionCaseDTO.getBankAccount6() != null && petitionCaseDTO.getBankAccount6().getCodeBankAccount() !=null 
						&& petitionCaseDTO.getBankAccount6().getCodeBankAccount().equals(peticionInformacionCuentas.getCuenta())) {
					
					petitionCaseDTO.getBankAccount6().setStatus(peticionInformacionCuentas.getEstadoCuenta());
				}
			} 
		}
		else {
			if (petitionCaseDTO.getBankAccount1() != null && petitionCaseDTO.getBankAccount1().getCodeBankAccount() !=null) {
				
				petitionCaseDTO.getBankAccount1().setStatus(EmbargosConstants.BANK_ACCOUNT_STATUS_ACTIVE);
			
			} else if (petitionCaseDTO.getBankAccount2() != null && petitionCaseDTO.getBankAccount2().getCodeBankAccount() !=null ) {
	
				petitionCaseDTO.getBankAccount2().setStatus(EmbargosConstants.BANK_ACCOUNT_STATUS_ACTIVE);
	
			} else if (petitionCaseDTO.getBankAccount3() != null && petitionCaseDTO.getBankAccount3().getCodeBankAccount() !=null ) {
				
				petitionCaseDTO.getBankAccount3().setStatus(EmbargosConstants.BANK_ACCOUNT_STATUS_ACTIVE);
			
			} else if (petitionCaseDTO.getBankAccount4() != null && petitionCaseDTO.getBankAccount4().getCodeBankAccount() !=null ) {
				
				petitionCaseDTO.getBankAccount4().setStatus(EmbargosConstants.BANK_ACCOUNT_STATUS_ACTIVE);
			
			} else if (petitionCaseDTO.getBankAccount5() != null && petitionCaseDTO.getBankAccount5().getCodeBankAccount() !=null ) {
				
				petitionCaseDTO.getBankAccount5().setStatus(EmbargosConstants.BANK_ACCOUNT_STATUS_ACTIVE);
			
			} else if (petitionCaseDTO.getBankAccount6() != null && petitionCaseDTO.getBankAccount6().getCodeBankAccount() !=null ) {
				
				petitionCaseDTO.getBankAccount6().setStatus(EmbargosConstants.BANK_ACCOUNT_STATUS_ACTIVE);
			}
		}
	}	
}
