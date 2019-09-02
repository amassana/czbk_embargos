package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.BankAccountLiftingDTO;
import es.commerzbank.ice.embargos.domain.entity.CuentaLevantamiento;

@Mapper(componentModel="spring")
public abstract class BankAccountLiftingMapper {

	@Mappings({
		@Mapping(source = "codCuentaLevantamiento", target = "codLiftingAccount"),
		@Mapping(source = "iban", target = "iban"),
		@Mapping(source = "cuenta", target = "account"),
		@Mapping(source = "cambio", target = "change"),
		@Mapping(source = "importe", target = "amount"),
		@Mapping(source = "indContabilizado", target = "indAccounting"),
		@Mapping(source = "actuacion", target = "action")
	})
	public abstract BankAccountLiftingDTO toBankAccountLiftingDTO(CuentaLevantamiento cuentaLevantamiento);
}
