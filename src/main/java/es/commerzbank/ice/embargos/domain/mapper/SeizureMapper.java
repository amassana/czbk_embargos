package es.commerzbank.ice.embargos.domain.mapper;

import java.math.BigDecimal;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.SeizureDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureStatusDTO;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import es.commerzbank.ice.embargos.domain.entity.Traba;

@Mapper(componentModel="spring")
public abstract class SeizureMapper {

	@Mappings({
		@Mapping (source = "codEmbargo", target = "idSeizureRequest"),
		@Mapping (source = "nif", target = "NIF"),
		@Mapping (source = "nombre", target = "name"),
		@Mapping (source = "razonSocialInterna", target = "nameInternal"),
		@Mapping (source = "importe", target = "requestedAmount")
	})
	public abstract SeizureDTO toSeizureDTO(Embargo embargo);
	
	@AfterMapping
	protected void setSeizureDTOAfterMapping(@MappingTarget SeizureDTO seizureDTO, Embargo embargo) {
		
		Traba traba = null;
		
		if (embargo.getTrabas()!=null) {
			traba = embargo.getTrabas().get(0);
			
			seizureDTO.setIdSeizure(String.valueOf(traba.getCodTraba()));
			
			SeizureStatusDTO seizureStatusDTO = new SeizureStatusDTO();
			seizureStatusDTO.setCode(String.valueOf(traba.getEstadoTraba().getCodEstado()));
			seizureStatusDTO.setDescription(traba.getEstadoTraba().getDesEstado());
			seizureDTO.setStatus(seizureStatusDTO);
			
			//Importe trabado:
			BigDecimal importeTrabado = traba.getImporteTrabado()!=null ? traba.getImporteTrabado() : BigDecimal.valueOf(0);
			seizureDTO.setSeizedAmount(importeTrabado);
			
			//Importe pendiente:
			if (embargo.getImporte()!=null) {
				BigDecimal pendingSeizedAmount = embargo.getImporte().subtract(importeTrabado);
				seizureDTO.setPendingSeizedAmount(pendingSeizedAmount);
			}
		}
	}
}
