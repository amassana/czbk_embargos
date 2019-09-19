package es.commerzbank.ice.embargos.domain.mapper;

import java.util.Date;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.FileControlDTO;
import es.commerzbank.ice.embargos.domain.entity.HControlFichero;
import es.commerzbank.ice.utils.EmbargosConstants;
import es.commerzbank.ice.utils.ICEDateUtils;

@Mapper(componentModel="spring")
public abstract class FileControlAuditMapper {

	@Mappings({
		@Mapping(source = "hControlFichero.id.codControlFichero", target = "code"),
		@Mapping(source = "hControlFichero.nombreFichero", target = "fileName"),
		@Mapping(source = "hControlFichero.codEstado", target = "status.code"),
		@Mapping(source = "descEstado", target = "status.description"),
		@Mapping(source = "targetTEST", target = "fileTarget"),
		@Mapping(source = "fechaTEST", target = "deliveryDate"),
		@Mapping(source = "hControlFichero.codTipoFichero", target = "codeFileType"),
		@Mapping(source = "hControlFichero.usuarioUltModificacion", target = "modifiedUser")
	})
	public abstract FileControlDTO toFileControlDTO (HControlFichero hControlFichero,
			String descEstado,
			String targetTEST,
			Date fechaTEST);
	
	
	
	@AfterMapping
	protected void setFileControlDTOAfterMapping(HControlFichero hControlFichero, @MappingTarget FileControlDTO fileControlDTO) {

		//Delivery date:
		if (hControlFichero.getFechaMaximaRespuesta()!=null) {
			fileControlDTO.setDeliveryDate(ICEDateUtils.bigDecimalToDate(hControlFichero.getFechaMaximaRespuesta(), ICEDateUtils.FORMAT_yyyyMMdd));
		}
		
		//Indicador de procesado (se pasa de String a Boolean):
		boolean isProcessed = hControlFichero.getIndProcesado()!=null && EmbargosConstants.IND_FLAG_SI.equals(hControlFichero.getIndProcesado());
		fileControlDTO.setIsProcessed(isProcessed);
		
		if (hControlFichero.getFUltimaModificacion()!=null) {
		
			//TODO: mirar que sea fecha local
			
			Date modifiedDate = ICEDateUtils.bigDecimalToDate(hControlFichero.getFUltimaModificacion(), ICEDateUtils.FORMAT_yyyyMMddHHmmss);
			
			fileControlDTO.setModifiedDate(modifiedDate);
		}
		
	}

}
