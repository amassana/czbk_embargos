package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.*;

import es.commerzbank.ice.embargos.domain.dto.FinalResponseBankAccountDTO;
import es.commerzbank.ice.embargos.domain.entity.CuentaResultadoEmbargo;

@Mapper(componentModel="spring")
public abstract class FinalResponseBankAccountMapper {

	@Mappings({
		@Mapping(source = "codCuentaResultadoEmbargo", target = "codBankAccount"),
		@Mapping(source = "cuentaEmbargo.iban", target = "iban"),
		@Mapping(source = "cuentaTraba.importe", target = "seizedAmount"),
		@Mapping(source = "importeNeto", target = "netAmount"),
		@Mapping(source = "importeLevantado", target = "liftedAmount")
	})
	public abstract FinalResponseBankAccountDTO toBankAccount(CuentaResultadoEmbargo cuenta);

	@AfterMapping
	public void toBankAccount(@MappingTarget FinalResponseBankAccountDTO finalResponseBankAccountDTO) {
		/*
		if (finalResponseBankAccountDTO.getNetAmount() != null && finalResponseBankAccountDTO.getSeizedAmount() != null) {
			finalResponseBankAccountDTO.setLiftedAmount(finalResponseBankAccountDTO.getNetAmount().subtract(finalResponseBankAccountDTO.getSeizedAmount()));
		}*/
	}
}
