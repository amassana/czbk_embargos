package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.SeizureActionDTO;
import es.commerzbank.ice.embargos.domain.entity.CuentaTrabaActuacion;

@Mapper(componentModel="spring")
public abstract class SeizedBankAccountActionMapper {

	@Mappings({
		@Mapping(source = "codActuacion", target = "code"),
		@Mapping(source = "descripcion", target = "description")
	})
	public abstract SeizureActionDTO toSeizureActionDTO(CuentaTrabaActuacion cuentaTrabaActuacion);
}

	
