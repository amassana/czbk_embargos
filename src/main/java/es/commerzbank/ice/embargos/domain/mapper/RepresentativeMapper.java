package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.Representative;
import es.commerzbank.ice.embargos.domain.dto.SeizureDTO;
import es.commerzbank.ice.embargos.domain.entity.Apoderados;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import es.commerzbank.ice.utils.EmbargosConstants;

@Mapper(componentModel="spring")
public abstract class RepresentativeMapper {

	@Mappings({
		@Mapping(source = "id", target = "id"),
		@Mapping(source = "name", target = "nombre"),
		@Mapping(source = "position", target = "cargo")
	})
	public abstract Apoderados toApoderado(Representative representative);

	@Mappings({
		@Mapping(source = "id", target = "id"),
		@Mapping(source = "nombre", target = "name"),
		@Mapping(source = "cargo", target = "position")
	})
	public abstract Representative toRepresentative(Apoderados apoderados);

	@AfterMapping
	protected void setRepresentativeAfterMapping(@MappingTarget Representative representative, Apoderados apoderado) {
		if (apoderado.getIndActivo().equals(EmbargosConstants.IND_FLAG_SI)) {
			representative.setActive(true);
		} else {
			representative.setActive(false);
		}
	}
}
