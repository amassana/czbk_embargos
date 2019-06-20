package es.commerzbank.ice.embargos.domain.mapper;

import java.util.Date;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.FileControlDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;

@Mapper(componentModel="spring")
public abstract class FileControlMapper {

	@Mappings({
		@Mapping(source = "controlFichero.codControlFichero", target = "codeFileControl"),
		@Mapping(source = "controlFichero.nombreFichero", target = "fileName"),
		@Mapping(source = "estadoTEST", target = "fileStatus"),
		@Mapping(source = "targetTEST", target = "fileTarget"),
		@Mapping(source = "fechaTEST", target = "deliveryDate"),
		@Mapping(source = "fechaFase6TEST", target = "phase6DeliveryDate"),
		@Mapping(source = "controlFichero.tipoFichero.codTipoFichero", target = "codeFileType")
	})
	public abstract FileControlDTO toFileControlDTO (ControlFichero controlFichero,
			String estadoTEST,
			String targetTEST,
			Date fechaTEST,
			Date fechaFase6TEST);
	
}
