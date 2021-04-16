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
		@Mapping(source = "embargo.importe", target = "requestedSeizureAmount"),
		@Mapping(source = "traba.importeTrabado", target = "seizedAmount"),
		@Mapping(source = "totalLevantado", target = "liftedAmount"),
		@Mapping(source = "totalNeto", target = "netAmount")
	})
	public abstract FinalResponseDTO toFinalResponse(ResultadoEmbargo resultado);

}
