package es.commerzbank.ice.embargos.domain.mapper;

import java.math.BigDecimal;

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
		@Mapping(source = "communicatingEntity.code", target = "entidadesComunicadora.codEntidadPresentadora")
	})
	public abstract EntidadesOrdenante toEntidadOrdenante(OrderingEntity orderingEntity);

	@Mappings({
		@Mapping(source = "codEntidadOrdenante", target = "codOrderingEntity"),
		@Mapping(source = "nifEntidad", target = "nifEntity"),
		@Mapping(source = "desEntidad", target = "desEntity"),
		@Mapping(source = "entidadesComunicadora.codEntidadPresentadora", target = "communicatingEntity.code"),
		@Mapping(source = "entidadesComunicadora.desEntidad", target = "communicatingEntity.name")
	})
	public abstract OrderingEntity toOrderingEntity(EntidadesOrdenante entidadesOrdenante);

	@AfterMapping
	protected void setOrderingEntityAfterMapping(@MappingTarget OrderingEntity orderingentity, EntidadesOrdenante entidadesOrdenante) {
		
		orderingentity.setActive(entidadesOrdenante.getIndActivo().equals(EmbargosConstants.IND_FLAG_SI) ? true : false);
		orderingentity.setIndCgpj(entidadesOrdenante.getIndCgpj() != null && entidadesOrdenante.getIndCgpj().equals(EmbargosConstants.IND_FLAG_SI) ? true : false);
		orderingentity.setIndAeat(entidadesOrdenante.getIndFormatoAeat() != null && entidadesOrdenante.getIndFormatoAeat().equals(EmbargosConstants.IND_FLAG_SI) ? true : false);
		orderingentity.setIndNorma63(entidadesOrdenante.getIndNorma63() != null && entidadesOrdenante.getIndNorma63().equals(EmbargosConstants.IND_FLAG_SI) ? true : false);
	}
	
	@AfterMapping
	protected void setEntidadOrdenanteAfterMapping(@MappingTarget EntidadesOrdenante entidadesOrdenante, OrderingEntity orderingentity) {
		entidadesOrdenante.setIndActivo(orderingentity.isActive() ? EmbargosConstants.IND_FLAG_SI : EmbargosConstants.IND_FLAG_NO);
		
		entidadesOrdenante.setIndFormatoAeat(orderingentity.getIndAeat() ? EmbargosConstants.IND_FLAG_SI : EmbargosConstants.IND_FLAG_NO);
		entidadesOrdenante.setIndCgpj(orderingentity.getIndCgpj() ? EmbargosConstants.IND_FLAG_SI : EmbargosConstants.IND_FLAG_NO);
		entidadesOrdenante.setIndNorma63(orderingentity.getIndNorma63() ? EmbargosConstants.IND_FLAG_SI : EmbargosConstants.IND_FLAG_NO);
	}
}
