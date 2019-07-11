package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.BankAccountDTO;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacionCuenta;

@Mapper(componentModel="spring")
public abstract class InformationPetitionBankAccountMapper {

	@Mappings({
		@Mapping(source = "bankAccountDTO.codeBankAccount", target = "cuenta"),
		@Mapping(source = "codPeticionInformacion", target = "id.codPeticionInformacion"),
		@Mapping(source = "bankAccountDTO.iban", target = "id.iban")
	})
	public abstract PeticionInformacionCuenta toPeticionInformacionCuenta(BankAccountDTO bankAccountDTO, String codPeticionInformacion);
	
	
}
