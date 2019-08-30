package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.SeizedBankAccountDTO;
import es.commerzbank.ice.embargos.domain.entity.HCuentaTraba;
import es.commerzbank.ice.utils.EmbargosConstants;

@Mapper(componentModel="spring")
public abstract class HSeizedBankAccountMapper {

	@Mappings({
		@Mapping (source = "id.codCuentaTraba", target = "idSeizedBankAccount"),
		@Mapping (source = "iban", target = "iban"),
		@Mapping (source = "codEstado", target = "bankAccountStatus"),
		@Mapping (source = "importe", target = "amount"),
		@Mapping (source = "cambio", target = "fxRate"),
		@Mapping (source = "actuacion", target = "seizureAction.code"),
//TODO:		@Mapping (source = "traba.estadoTraba.codEstado", target = "seizureStatus.code"),
	})
	public abstract SeizedBankAccountDTO toSeizedBankAccountDTO (HCuentaTraba hCuentaTraba);

	@AfterMapping
	protected void setSeizedBankAccountDTAfterMapping(@MappingTarget SeizedBankAccountDTO seizedBankAccountDTO, HCuentaTraba hCuentaTraba) {

		seizedBankAccountDTO.setBankAccountCurrency(EmbargosConstants.ISO_MONEDA_EUR);
		
	}
}