package es.commerzbank.ice.embargos.domain.mapper;

import es.commerzbank.ice.comun.lib.domain.dto.AccountingNote;
import es.commerzbank.ice.embargos.domain.dto.FileControlTypeDTO;
import es.commerzbank.ice.embargos.domain.entity.CuentaLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.TipoFichero;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel="spring")
public abstract class AccountingMapper {

	@Mappings({
		@Mapping(source = "codTipoFichero", target = "code"),
		@Mapping(source = "desTipoFichero", target = "description")
	})
	public abstract AccountingNote fromCuentaLevantamiento(CuentaLevantamiento cuentaLevantamiento);
}
