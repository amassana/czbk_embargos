package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.PetitionDTO;
import es.commerzbank.ice.embargos.domain.entity.Peticion;

@Mapper(componentModel="spring")
public abstract class PetitionMapper {

	@Mappings({
		@Mapping (source = "codPeticion", target = "codePetition"),
		@Mapping (source = "estadoIntPeticion.codEstadoIntPeticion", target = "codeStatusPetition")
	})
	public abstract PetitionDTO toPetitionDTO (Peticion peticion);
}
