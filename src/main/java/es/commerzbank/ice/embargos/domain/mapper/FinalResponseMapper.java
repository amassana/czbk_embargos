package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.FinalResponseDTO;
import es.commerzbank.ice.embargos.domain.entity.ResultadoEmbargo;

@Mapper(componentModel="spring")
public abstract class FinalResponseMapper {

	@Mappings({
		@Mapping(source = "codResultadoEmbargo", target = "codeFinalResponse"),
		@Mapping(source = "embargo.numeroEmbargo", target = "seizureNumber"),
		@Mapping(source = "embargo.nif", target = "nif"),
		@Mapping(source = "embargo.nombre", target = "holder")
	})
	public abstract FinalResponseDTO toFinalResponse(ResultadoEmbargo resultado);

}
