package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.BankAccountDTO;
import es.commerzbank.ice.embargos.domain.entity.CuentaLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacionCuenta;


@Mapper(componentModel="spring")
public abstract class BankAccountMapper {

	@Mappings({
		@Mapping(source = "cuenta", target = "codeBankAccount"),
		@Mapping(source = "id.iban", target = "iban"),
		@Mapping(source = "estadoCuenta", target="status")
	})
	public abstract BankAccountDTO toBankAccountDTO(PeticionInformacionCuenta peticionInformacionCuenta);
	
}
