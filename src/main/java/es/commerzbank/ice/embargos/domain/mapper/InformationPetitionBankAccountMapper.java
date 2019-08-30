package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import es.commerzbank.ice.datawarehouse.domain.dto.AccountDTO;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacionCuenta;

@Mapper(componentModel="spring")
public abstract class InformationPetitionBankAccountMapper {

	@Mappings({
		@Mapping(source = "accountDTO.accountNum", target = "cuenta"),
		@Mapping(source = "codPeticionInformacion", target = "id.codPeticionInformacion"),
		@Mapping(source = "accountDTO.iban", target = "id.iban"),
		@Mapping(source = "accountDTO.status", target = "estadoCuenta")
	})
	public abstract PeticionInformacionCuenta toPeticionInformacionCuenta(AccountDTO accountDTO, String codPeticionInformacion);
	
	
}
