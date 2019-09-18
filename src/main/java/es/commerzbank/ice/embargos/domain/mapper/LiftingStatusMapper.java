package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.LiftingStatusDTO;
import es.commerzbank.ice.embargos.domain.entity.EstadoIntLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.EstadoLevantamiento;

@Mapper(componentModel="spring")
public abstract class LiftingStatusMapper {

	@Mappings({
		@Mapping(source = "codEstado", target = "code"),
		@Mapping(source = "estado", target = "description")
	})
	public abstract LiftingStatusDTO toLiftingStatus(EstadoLevantamiento estado);

	@Mappings({
		@Mapping(source = "code", target = "codEstado")
	})
	public abstract EstadoLevantamiento toEstadoLevantamiento(LiftingStatusDTO status);

}
