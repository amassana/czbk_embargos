package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.SeizureDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureStatusDTO;
import es.commerzbank.ice.embargos.domain.entity.HEmbargo;
import es.commerzbank.ice.embargos.domain.entity.HTraba;

@Mapper(componentModel="spring")
public abstract class HSeizureMapper {

	@Mappings({
		@Mapping (source = "id.codEmbargo", target = "idSeizureRequest"),
		@Mapping (source = "nif", target = "NIF"),
		@Mapping (source = "nombre", target = "name"),
		@Mapping (source = "razonSocialInterna", target = "nameInternal"),
		@Mapping (source = "importe", target = "requestedAmount"),
		@Mapping (source = "id.codAuditoria", target = "codAudit")
	})
	public abstract SeizureDTO toSeizureDTO(HEmbargo hEmbargo);
	
	@AfterMapping
	protected void setSeizureDTOAfterMapping(@MappingTarget SeizureDTO seizureDTO, HEmbargo hEmbargo) {
		
		HTraba hTraba = null;
		
		if (hEmbargo.getHTrabas()!=null) {
			hTraba = hEmbargo.getHTrabas().get(0);
			
			seizureDTO.setIdSeizure(String.valueOf(hTraba.getId().getCodTraba()));
			
			SeizureStatusDTO seizureStatusDTO = new SeizureStatusDTO();
			seizureStatusDTO.setCode(String.valueOf(hTraba.getCodEstado()));
			seizureDTO.setStatus(seizureStatusDTO);
		}
	}
}
