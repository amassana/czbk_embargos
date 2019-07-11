package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.FileControlTypeDTO;
import es.commerzbank.ice.embargos.domain.entity.TipoFichero;

@Mapper(componentModel="spring")
public abstract class FileTypeMapper {

	@Mappings({
		@Mapping(source = "codTipoFichero", target = "code"),
		@Mapping(source = "desTipoFichero", target = "description")
	})
	public abstract FileControlTypeDTO toFileTypeDTO(TipoFichero tipoFichero);
}
