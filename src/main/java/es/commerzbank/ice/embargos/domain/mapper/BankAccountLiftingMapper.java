package es.commerzbank.ice.embargos.domain.mapper;

import java.util.HashMap;
import java.util.Map;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.BankAccountLiftingDTO;
import es.commerzbank.ice.embargos.domain.dto.Item;
import es.commerzbank.ice.embargos.domain.entity.CuentaLevantamiento;
import es.commerzbank.ice.utils.EmbargosConstants;


@Mapper(componentModel="spring")
public abstract class BankAccountLiftingMapper {
	
	private static Map<String, String> statusList = new HashMap<>();
	static {
		statusList.put("I","Inicial");
		statusList.put("P","Pediente de contabilizar");
		statusList.put("C","Contabilizado");
	}

	@Mappings({
		@Mapping(source = "codCuentaLevantamiento", target = "codLiftingAccount"),
		@Mapping(source = "iban", target = "iban"),
		@Mapping(source = "cuenta", target = "account"),
		@Mapping(source = "cambio", target = "change"),
		@Mapping(source = "importe", target = "amount"),
		@Mapping(source = "codDivisa", target = "codCurrency")
	})
	public abstract BankAccountLiftingDTO toBankAccountLiftingDTO(CuentaLevantamiento cuentaLevantamiento);
	
	@Mappings({
		@Mapping(source = "codLiftingAccount", target = "codCuentaLevantamiento"),
		@Mapping(source = "iban", target = "iban"),
		@Mapping(source = "account", target = "cuenta"),
		@Mapping(source = "change", target = "cambio"),
		@Mapping(source = "amount", target = "importe"),
		@Mapping(source = "codCurrency", target = "codDivisa")
	})
	public abstract CuentaLevantamiento toCuentaLevantamiento(BankAccountLiftingDTO account);
	
	
	@AfterMapping
	protected void setBankAccountLiftingDTOAfterMapping(@MappingTarget BankAccountLiftingDTO dto, CuentaLevantamiento entity) {
		dto.setIndAccounting(new Item(entity.getIndContabilizado(), statusList.get(entity.getIndContabilizado())));
	}
	
	@AfterMapping
	protected void setCuentaLevantamientoAfterMapping(@MappingTarget CuentaLevantamiento entity, BankAccountLiftingDTO dto) {
		entity.setIndContabilizado((String) dto.getIndAccounting().getCode());
	}
}
