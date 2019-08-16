package es.commerzbank.ice.embargos.domain.mapper;

import java.util.Date;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.FileControlDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.utils.EmbargosConstants;

@Mapper(componentModel="spring")
public abstract class FileControlMapper {
	
	@Mappings({
		@Mapping(source = "controlFichero.codControlFichero", target = "code"),
		@Mapping(source = "controlFichero.nombreFichero", target = "fileName"),
		@Mapping(source = "controlFichero.estadoCtrlfichero.id.codEstado", target = "status.code"),
		@Mapping(source = "controlFichero.estadoCtrlfichero.descripcion", target = "status.description"),
		@Mapping(source = "targetTEST", target = "fileTarget"),
		@Mapping(source = "fechaTEST", target = "deliveryDate"),
		@Mapping(source = "controlFichero.tipoFichero.codTipoFichero", target = "codeFileType"),
		@Mapping(source = "controlFichero.controlFicheroOrigen.codControlFichero", target = "codeFileOrigin"),
		@Mapping(source = "controlFichero.controlFicheroRespuesta.codControlFichero", target = "codeFileResponse"),
		@Mapping(source = "controlFichero.usuarioUltModificacion", target = "modifiedUser")
	})
	public abstract FileControlDTO toFileControlDTO (ControlFichero controlFichero,
			String targetTEST,
			Date fechaTEST);
	
	
	@AfterMapping
	protected void setFileControlDTOAfterMapping(ControlFichero controlFichero, @MappingTarget FileControlDTO fileControlDTO) {
		
		//Indicador de procesado (se pasa de String a Boolean):
		boolean isProcessed = controlFichero.getIndProcesado()!=null && EmbargosConstants.IND_FLAG_SI.equals(controlFichero.getIndProcesado());
		fileControlDTO.setIsProcessed(isProcessed);
		
		
	}
	
}
