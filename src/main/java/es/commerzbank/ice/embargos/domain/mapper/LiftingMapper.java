package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.LiftingDTO;
import es.commerzbank.ice.embargos.domain.entity.LevantamientoTraba;

@Mapper(componentModel="spring")
public abstract class LiftingMapper {

	@Mappings({
		@Mapping(source = "levantamiento.codLevantamiento", target = "codLifting"),
		@Mapping(source = "levantamiento.traba.codTraba", target = "codLock"),
		@Mapping(source = "levantamiento.traba.embargo.nombre", target = "name"),
		@Mapping(source = "levantamiento.traba.embargo.datosCliente.nif", target = "nif"),
		@Mapping(source = "levantamiento.traba.embargo.numeroEmbargo", target = "numSeizure"),
		@Mapping(source = "levantamiento.traba.embargo.importe", target = "amountDebt"),
		@Mapping(source = "levantamiento.importeLevantado", target = "amountRaised")
	})
	public abstract LiftingDTO toLiftingDTO(LevantamientoTraba levantamiento);

	@Mappings({
		@Mapping(source = "codLifting", target = "codLevantamiento"),
		@Mapping(source = "codLock", target = "traba.codTraba"),
		@Mapping(source = "name", target = "traba.embargo.nombre"),
		@Mapping(source = "codLifting", target = "traba.embargo.datosCliente.nif"),
		@Mapping(source = "codLifting", target = "traba.embargo.numeroEmbargo"),
		@Mapping(source = "codLifting", target = "traba.embargo.importe"),
		@Mapping(source = "amountRaised", target = "importeLevantado")
	})
	public abstract LevantamientoTraba toLevantamiento(LiftingDTO dto);	

	
}
