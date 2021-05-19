package es.commerzbank.ice.embargos.domain.mapper;

import es.commerzbank.ice.embargos.domain.dto.CGPJPetitionDTO;
import es.commerzbank.ice.embargos.domain.dto.IntegradorRequestStatusDTO;
import es.commerzbank.ice.embargos.domain.dto.PetitionDTO;
import es.commerzbank.ice.embargos.domain.entity.EstadoIntPeticion;
import es.commerzbank.ice.embargos.domain.entity.Peticion;
import es.commerzbank.ice.embargos.utils.ICEDateUtils;
import org.mapstruct.*;

@Mapper(componentModel="spring")
public abstract class PetitionMapper {

	@Mappings({
		@Mapping (source = "codPeticion", target = "codePetition"),
		@Mapping (source = "estadoIntPeticion.codEstadoIntPeticion", target = "codeStatusPetition")
	})
	public abstract PetitionDTO toPetitionDTO (Peticion peticion);

	@Mappings({
			@Mapping (target = "petitionCode", source = "codPeticion"),
			@Mapping (target = "status.code", source = "estadoIntPeticion.codEstadoIntPeticion"),
			@Mapping (target = "status.description", source = "estadoIntPeticion.desEstadoIntPeticion"),
			@Mapping (target = "fileControl.code", source = "controlFichero.codControlFichero"),
	})
	public abstract CGPJPetitionDTO toCGPJPetitionDTO (Peticion peticion);

	@AfterMapping
	protected void toCGPJPetitionDTOAfterMapping(Peticion peticion, @MappingTarget CGPJPetitionDTO CGPJPetitionDTO)
	{
		if (peticion.getControlFichero() != null && peticion.getControlFichero().getFechaCreacion()!=null) {
			CGPJPetitionDTO.getFileControl().setCreatedDate(ICEDateUtils.bigDecimalToDate(peticion.getControlFichero().getFechaCreacion(), ICEDateUtils.FORMAT_yyyyMMdd));
		}
	}

	@Mappings({
			@Mapping (target = "code", source = "codEstadoIntPeticion"),
			@Mapping (target = "description", source = "desEstadoIntPeticion"),
	})
	public abstract IntegradorRequestStatusDTO toEstadoIntPeticion(EstadoIntPeticion status);
}
