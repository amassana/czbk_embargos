package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.LiftingStatusDTO;
import es.commerzbank.ice.embargos.domain.entity.EstadoIntLevantamiento;

@Mapper(componentModel="spring")
public abstract class LiftingStatusMapper {

	@Mappings({
		@Mapping(source = "codEstadoIntLevantamiento", target = "code"),
		@Mapping(source = "desEstadoIntLevantamiento", target = "description")
	})
	public abstract LiftingStatusDTO toLiftingStatus(EstadoIntLevantamiento estado);

}
