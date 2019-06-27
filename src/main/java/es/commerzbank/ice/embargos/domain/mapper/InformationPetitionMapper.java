package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.InformationPetitionDTO;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacion;

@Mapper(componentModel="spring")
public abstract class InformationPetitionMapper {

	@Mappings({
		@Mapping(source = "peticionInformacion.razonSocial", target = "name"),
		@Mapping(source = "peticionInformacion.iban1", target = "account1.iban"),
		@Mapping(source = "peticionInformacion.iban2", target = "account2.iban"),
		@Mapping(source = "peticionInformacion.iban3", target = "account3.iban"),
		@Mapping(source = "peticionInformacion.iban4", target = "account4.iban"),
		@Mapping(source = "peticionInformacion.iban5", target = "account5.iban"),
		@Mapping(source = "peticionInformacion.iban6", target = "account6.iban"),
		@Mapping(source = "statusCodeTEST", target = "account1.statusCode"),
		@Mapping(source = "statusCodeTEST", target = "account2.statusCode"),
		@Mapping(source = "statusCodeTEST", target = "account3.statusCode"),
		@Mapping(source = "statusCodeTEST", target = "account4.statusCode"),
		@Mapping(source = "statusCodeTEST", target = "account5.statusCode"),
		@Mapping(source = "statusCodeTEST", target = "account6.statusCode"),
		@Mapping(source = "statusDescTEST", target = "account1.statusDescription"),
		@Mapping(source = "statusDescTEST", target = "account2.statusDescription"),
		@Mapping(source = "statusDescTEST", target = "account3.statusDescription"),
		@Mapping(source = "statusDescTEST", target = "account4.statusDescription"),
		@Mapping(source = "statusDescTEST", target = "account5.statusDescription"),
		@Mapping(source = "statusDescTEST", target = "account6.statusDescription")
	})
	public abstract InformationPetitionDTO toPetitionDTO (PeticionInformacion peticionInformacion,
			String statusCodeTEST, String statusDescTEST);
}
