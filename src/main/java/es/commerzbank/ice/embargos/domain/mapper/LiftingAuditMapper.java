package es.commerzbank.ice.embargos.domain.mapper;

import java.util.Date;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.LiftingAuditDTO;
import es.commerzbank.ice.embargos.domain.entity.HLevantamientoTraba;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.ICEDateUtils;

@Mapper(componentModel="spring")
public abstract class LiftingAuditMapper {

	@Mappings({
		@Mapping(source = "id.codLevantamiento", target = "codLifting"),
		@Mapping(source = "estadoContable", target = "accountingStatement"),
		@Mapping(source = "estadoEjecutado", target = "executedStatus"),
		@Mapping(source = "usuarioUltModificacion", target = "modifiedUser"),
		@Mapping(source = "codDeudaDeudor", target = "debtorDebtCode")
	})
	public abstract LiftingAuditDTO toInformationPetitionDTO(HLevantamientoTraba hLevantamientoTraba);

	@AfterMapping
	protected void setInformationPetitionDTOAfterMapping(HLevantamientoTraba hLevantamientoTraba, @MappingTarget LiftingAuditDTO liftingAuditDTO) {
		
		String indCasoRevisado = hLevantamientoTraba.getIndCasoRevisado();
		
		boolean isReviewed = indCasoRevisado!=null && EmbargosConstants.IND_FLAG_YES.equals(indCasoRevisado);
		
		liftingAuditDTO.setReviewed(isReviewed);
		
		//Fecha de ultima modificacion:
		if (hLevantamientoTraba.getfUltimaModificacion()!=null) {
			
			//TODO: mirar que sea fecha local
			
			Date modifiedDate = ICEDateUtils.bigDecimalToDate(hLevantamientoTraba.getfUltimaModificacion(), ICEDateUtils.FORMAT_yyyyMMddHHmmss);
			
			liftingAuditDTO.setModifiedDate(modifiedDate);
		}
	}	
}
