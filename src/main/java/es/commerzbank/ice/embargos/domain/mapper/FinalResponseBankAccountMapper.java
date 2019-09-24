package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.FinalResponseBankAccountDTO;
import es.commerzbank.ice.embargos.domain.entity.CuentaResultadoEmbargo;

@Mapper(componentModel="spring")
public abstract class FinalResponseBankAccountMapper {

	@Mappings({
		@Mapping(source = "codCuentaResultadoEmbargo", target = "codBankAccount"),
		@Mapping(source = "cuentaEmbargo.iban", target = "iban"),
		@Mapping(source = "cuentaEmbargo.importe", target = "amount"),
		@Mapping(source = "cuentaTraba.cuentaTrabaActuacion.codActuacion", target = "key"),
		@Mapping(source = "cuentaTraba.importe", target = "amountLocked"),
		@Mapping(source = "importeNeto", target = "amountTransfer")
	})
	public abstract FinalResponseBankAccountDTO toBankAccount(CuentaResultadoEmbargo cuenta);

}
