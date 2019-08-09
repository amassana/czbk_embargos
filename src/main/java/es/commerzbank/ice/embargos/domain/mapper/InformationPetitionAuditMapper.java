package es.commerzbank.ice.embargos.domain.mapper;

import java.util.Date;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.PetitionCaseDTO;
import es.commerzbank.ice.embargos.domain.entity.HPeticionInformacion;
import es.commerzbank.ice.utils.EmbargosConstants;

@Mapper(componentModel="spring")
public abstract class InformationPetitionAuditMapper {

	@Mappings({
		@Mapping(source = "hPeticionInformacion.id.codPeticion", target = "codePetitionCase"),
		@Mapping(source = "hPeticionInformacion.razonSocial", target = "name"),
		@Mapping(source = "hPeticionInformacion.cuenta1", target = "bankAccount1.codeBankAccount"),
		@Mapping(source = "hPeticionInformacion.cuenta2", target = "bankAccount2.codeBankAccount"),
		@Mapping(source = "hPeticionInformacion.cuenta3", target = "bankAccount3.codeBankAccount"),
		@Mapping(source = "hPeticionInformacion.cuenta4", target = "bankAccount4.codeBankAccount"),
		@Mapping(source = "hPeticionInformacion.cuenta5", target = "bankAccount5.codeBankAccount"),
		@Mapping(source = "hPeticionInformacion.cuenta6", target = "bankAccount6.codeBankAccount"),
		@Mapping(source = "hPeticionInformacion.iban1", target = "bankAccount1.iban"),
		@Mapping(source = "hPeticionInformacion.iban2", target = "bankAccount2.iban"),
		@Mapping(source = "hPeticionInformacion.iban3", target = "bankAccount3.iban"),
		@Mapping(source = "hPeticionInformacion.iban4", target = "bankAccount4.iban"),
		@Mapping(source = "hPeticionInformacion.iban5", target = "bankAccount5.iban"),
		@Mapping(source = "hPeticionInformacion.iban6", target = "bankAccount6.iban"),
		@Mapping(source = "statusCodeTEST", target = "bankAccount1.status"),
		@Mapping(source = "statusCodeTEST", target = "bankAccount2.status"),
		@Mapping(source = "statusCodeTEST", target = "bankAccount3.status"),
		@Mapping(source = "statusCodeTEST", target = "bankAccount4.status"),
		@Mapping(source = "statusCodeTEST", target = "bankAccount5.status"),
		@Mapping(source = "statusCodeTEST", target = "bankAccount6.status"),
		@Mapping(source = "hPeticionInformacion.usuarioUltModificacion", target = "modifiedUser")
	})
	public abstract PetitionCaseDTO toInformationPetitionDTO(HPeticionInformacion hPeticionInformacion,
			String statusCodeTEST, String statusDescTEST);
	
	@AfterMapping
	protected void setInformationPetitionDTOAfterMapping(HPeticionInformacion hPeticionInformacion, @MappingTarget PetitionCaseDTO petitionCaseDTO) {
		
		String indCasoRevisado = hPeticionInformacion.getIndCasoRevisado();
		
		boolean isReviewed = indCasoRevisado!=null && EmbargosConstants.IND_FLAG_YES.equals(indCasoRevisado);
		
		petitionCaseDTO.setIsReviewed(isReviewed);
		
		//Fecha de ultima modificacion:
		if (hPeticionInformacion.getId().getFUltimaModificacion()>0) {
			
			//TODO: mirar que sea fecha local
			
			Date modifiedDate = new Date(hPeticionInformacion.getId().getFUltimaModificacion());
			
			petitionCaseDTO.setModifiedDate(modifiedDate);
		}
	}	
}
