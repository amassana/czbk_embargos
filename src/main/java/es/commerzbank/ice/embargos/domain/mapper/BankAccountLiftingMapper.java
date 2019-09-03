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
	
	@Mappings({
		@Mapping(source = "codLiftingAccount", target = "codCuentaLevantamiento"),
		@Mapping(source = "iban", target = "iban"),
		@Mapping(source = "account", target = "cuenta"),
		@Mapping(source = "change", target = "cambio"),
		@Mapping(source = "amount", target = "importe"),
		@Mapping(source = "indAccounting", target = "indContabilizado"),
		@Mapping(source = "action", target = "actuacion")
	})
	public abstract CuentaLevantamiento toCuentaLevantamiento(BankAccountLiftingDTO account);
}
