package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.PetitionCaseDTO;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacion;
import es.commerzbank.ice.utils.EmbargosConstants;

@Mapper(componentModel="spring")
public abstract class InformationPetitionMapper {

	@Mappings({
		@Mapping(source = "peticionInformacion.codPeticion", target = "codePetitionCase"),
		@Mapping(source = "peticionInformacion.razonSocial", target = "name"),
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
		@Mapping(source = "peticionInformacion.iban6", target = "bankAccount6.iban"),
		@Mapping(source = "statusCodeTEST", target = "bankAccount1.statusCode"),
		@Mapping(source = "statusCodeTEST", target = "bankAccount2.statusCode"),
		@Mapping(source = "statusCodeTEST", target = "bankAccount3.statusCode"),
		@Mapping(source = "statusCodeTEST", target = "bankAccount4.statusCode"),
		@Mapping(source = "statusCodeTEST", target = "bankAccount5.statusCode"),
		@Mapping(source = "statusCodeTEST", target = "bankAccount6.statusCode"),
		@Mapping(source = "statusDescTEST", target = "bankAccount1.statusDescription"),
		@Mapping(source = "statusDescTEST", target = "bankAccount2.statusDescription"),
		@Mapping(source = "statusDescTEST", target = "bankAccount3.statusDescription"),
		@Mapping(source = "statusDescTEST", target = "bankAccount4.statusDescription"),
		@Mapping(source = "statusDescTEST", target = "bankAccount5.statusDescription"),
		@Mapping(source = "statusDescTEST", target = "bankAccount6.statusDescription")
	})
	public abstract PetitionCaseDTO toInformationPetitionDTO(PeticionInformacion peticionInformacion,
			String statusCodeTEST, String statusDescTEST);
	
	@AfterMapping
	protected void setInformationPetitionDTOAfterMapping(PeticionInformacion peticionInformacion, @MappingTarget PetitionCaseDTO petitionCaseDTO) {
		
		String indCasoRevisado = peticionInformacion.getIndCasoRevisado();
		
		boolean isReviewed = indCasoRevisado!=null && EmbargosConstants.IND_FLAG_YES.equals(indCasoRevisado);
		
		petitionCaseDTO.setIsReviewed(isReviewed);
	}	
}
