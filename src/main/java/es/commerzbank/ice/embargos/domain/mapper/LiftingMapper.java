package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.LiftingDTO;
import es.commerzbank.ice.embargos.domain.entity.LevantamientoTraba;
import es.commerzbank.ice.utils.EmbargosConstants;

@Mapper(componentModel="spring")
public abstract class LiftingMapper {

	@Mappings({
		@Mapping(source = "levantamiento.codLevantamiento", target = "codLifting"),
		@Mapping(source = "levantamiento.traba.embargo.nombre", target = "name"),
		@Mapping(source = "levantamiento.traba.embargo.nif", target = "nif"),
		@Mapping(source = "levantamiento.traba.embargo.numeroEmbargo", target = "numSeizure"),
		@Mapping(source = "levantamiento.traba.embargo.importe", target = "amountDebt"),
		@Mapping(source = "levantamiento.traba.importeTrabado", target = "amountLocked")
	})
	public abstract LiftingDTO toLiftingDTO(LevantamientoTraba levantamiento);
	
	@AfterMapping
	protected void setLiftingDTOAfterMapping(LevantamientoTraba levatamiento, @MappingTarget LiftingDTO liftingDTO) {
		
		String indCasoRevisado = levatamiento.getIndCasoRevisado();
		
		boolean isReviewed = indCasoRevisado!=null && EmbargosConstants.IND_FLAG_YES.equals(indCasoRevisado);
		
		liftingDTO.setStatus(isReviewed);
		
		liftingDTO.setAmountPending(liftingDTO.getAmountDebt() - liftingDTO.getAmountLocked());
	}

	@Mappings({
		@Mapping(source = "codLifting", target = "codLevantamiento"),
		@Mapping(source = "name", target = "traba.embargo.nombre"),
		@Mapping(source = "codLifting", target = "traba.embargo.nif"),
		@Mapping(source = "codLifting", target = "traba.embargo.numeroEmbargo"),
		@Mapping(source = "codLifting", target = "traba.embargo.importe"),
		@Mapping(source = "codLifting", target = "traba.importeTrabado")
	})
	public abstract LevantamientoTraba toLevantamiento(LiftingDTO dto);	

	@AfterMapping
	protected void setLevantamientoMapping(@MappingTarget LevantamientoTraba entity, LiftingDTO dto) {
		if (dto.getStatus()) {
			entity.setIndCasoRevisado(EmbargosConstants.IND_FLAG_YES);
		} else {
			entity.setIndCasoRevisado(EmbargosConstants.IND_FLAG_NO);
		}
	}
}
