package es.commerzbank.ice.embargos.domain.mapper;

import es.commerzbank.ice.embargos.domain.dto.CGPJPetitionDTO;
import es.commerzbank.ice.embargos.domain.dto.PetitionDTO;
import es.commerzbank.ice.embargos.domain.entity.Peticion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel="spring")
public abstract class PetitionMapper {

	@Mappings({
		@Mapping (source = "codPeticion", target = "codePetition"),
		@Mapping (source = "estadoIntPeticion.codEstadoIntPeticion", target = "codeStatusPetition")
	})
	public abstract PetitionDTO toPetitionDTO (Peticion peticion);

	@Mappings({
			@Mapping (target = "petitionCode", source = "codPeticion"),
	})
	public abstract CGPJPetitionDTO toCGPJPetitionDTO (Peticion peticion);
}
