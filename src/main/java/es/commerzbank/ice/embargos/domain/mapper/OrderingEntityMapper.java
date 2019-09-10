package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.OrderingEntity;
import es.commerzbank.ice.embargos.domain.entity.EntidadesOrdenante;
import es.commerzbank.ice.utils.EmbargosConstants;

@Mapper(componentModel="spring")
public abstract class OrderingEntityMapper {

	@Mappings({
		@Mapping(source = "codOrderingEntity", target = "codEntidadOrdenante"),
		@Mapping(source = "nifEntity", target = "nifEntidad"),
		@Mapping(source = "desEntity", target = "desEntidad"),
		@Mapping(source = "indCgpj", target = "indCgpj"),
		@Mapping(source = "indAeat", target = "indFormatoAeat"),
		@Mapping(source = "indNorma63", target = "indNorma63"),
		@Mapping(source = "transmitter", target = "entidadesComunicadora.codEntidadPresentadora")
	})
	public abstract EntidadesOrdenante toEntidadOrdenante(OrderingEntity orderingEntity);

	@Mappings({
		@Mapping(source = "codEntidadOrdenante", target = "codOrderingEntity"),
		@Mapping(source = "nifEntidad", target = "nifEntity"),
		@Mapping(source = "desEntidad", target = "desEntity"),
		@Mapping(source = "indCgpj", target = "indCgpj"),
		@Mapping(source = "indFormatoAeat", target = "indAeat"),
		@Mapping(source = "indNorma63", target = "indNorma63"),
		@Mapping(source = "entidadesComunicadora.codEntidadPresentadora", target = "transmitter")
	})
	public abstract OrderingEntity toOrderingEntity(EntidadesOrdenante entidadesOrdenante);

	@AfterMapping
	protected void setOrderingEntityAfterMapping(@MappingTarget OrderingEntity orderingentity, EntidadesOrdenante entidadesOrdenante) {
		if (entidadesOrdenante.getIndActivo().equals(EmbargosConstants.IND_FLAG_YES)) {
			orderingentity.setActive(true);
		} else {
			orderingentity.setActive(false);
		}
	}
}
