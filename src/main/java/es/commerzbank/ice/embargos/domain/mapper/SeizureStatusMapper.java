package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.SeizureStatusDTO;
import es.commerzbank.ice.embargos.domain.entity.EstadoTraba;

@Mapper(componentModel="spring")
public abstract class SeizureStatusMapper {

	@Mappings({
		@Mapping(source = "codEstado", target = "code"),
		@Mapping(source = "desEstado", target = "description")
	})
	public abstract SeizureStatusDTO toSeizureStatusDTO(EstadoTraba estadoTraba);
}
