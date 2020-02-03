package es.commerzbank.ice.embargos.domain.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import es.commerzbank.ice.embargos.domain.dto.AuditoriaEmbDto;
import es.commerzbank.ice.embargos.domain.entity.AuditoriaEmb;

@Mapper(componentModel="spring")
public abstract class AuditoriaEmbMapper {

	public abstract AuditoriaEmbDto toAuditoriaDto(AuditoriaEmb auditoria);
	
	public abstract ArrayList<AuditoriaEmbDto> toAuditoriaDto(List<AuditoriaEmb> auditoria);
	
}
