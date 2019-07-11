package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.FileControlStatusDTO;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlfichero;

@Mapper(componentModel="spring")
public abstract class FileControlStatusMapper {

	@Mappings({
		@Mapping(source = "id.codEstado", target = "status"),
		@Mapping(source = "descripcion", target = "description")
	})
	public abstract FileControlStatusDTO toFileControlStatusDTO (EstadoCtrlfichero estadoCtrlfichero);
}
