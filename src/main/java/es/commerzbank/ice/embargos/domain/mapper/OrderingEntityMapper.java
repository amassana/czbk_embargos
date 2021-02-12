package es.commerzbank.ice.embargos.domain.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.Item;
import es.commerzbank.ice.embargos.domain.dto.OrderingEntity;
import es.commerzbank.ice.embargos.domain.dto.OrderingEntityCsv;
import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora;
import es.commerzbank.ice.embargos.domain.entity.EntidadesOrdenante;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;

@Mapper(componentModel="spring")
public abstract class OrderingEntityMapper {

	@Mappings({
		@Mapping(source = "codOrderingEntity", target = "codEntidadOrdenante"),
		@Mapping(source = "nifEntity", target = "nifEntidad"),
		@Mapping(source = "desEntity", target = "desEntidad")
	})
	public abstract EntidadesOrdenante toEntidadOrdenante(OrderingEntity orderingEntity);

	@Mappings({
		@Mapping(source = "communicatingEntity.code", target = "codeCommunicatingEntity"),
		@Mapping(source = "communicatingEntity.description", target = "descCommunicatingEntity")
	})
	public abstract OrderingEntityCsv toOrderingEntityCsv(OrderingEntity orderingEntity);
	
	public abstract ArrayList<OrderingEntityCsv> toOrderingEntityCsv(List<OrderingEntity> orderingEntity);
	
	@Mappings({
		@Mapping(source = "codEntidadOrdenante", target = "codOrderingEntity"),
		@Mapping(source = "nifEntidad", target = "nifEntity"),
		@Mapping(source = "desEntidad", target = "desEntity")
	})
	public abstract OrderingEntity toOrderingEntity(EntidadesOrdenante entidadesOrdenante);

	@AfterMapping
	protected void setOrderingEntityAfterMapping(@MappingTarget OrderingEntity orderingEntity, EntidadesOrdenante entidadesOrdenante) {
		orderingEntity.setCommunicatingEntity(new Item(entidadesOrdenante.getEntidadesComunicadora().getCodEntidadPresentadora(), entidadesOrdenante.getEntidadesComunicadora().getDesEntidad()));
		orderingEntity.setActive(entidadesOrdenante.getIndActivo().equals(EmbargosConstants.IND_FLAG_SI) ? true : false);
		orderingEntity.setIndCgpj(entidadesOrdenante.getIndCgpj() != null && entidadesOrdenante.getIndCgpj().equals(EmbargosConstants.IND_FLAG_SI) ? true : false);
		orderingEntity.setIndAeat(entidadesOrdenante.getIndFormatoAeat() != null && entidadesOrdenante.getIndFormatoAeat().equals(EmbargosConstants.IND_FLAG_SI) ? true : false);
		orderingEntity.setIndNorma63(entidadesOrdenante.getIndNorma63() != null && entidadesOrdenante.getIndNorma63().equals(EmbargosConstants.IND_FLAG_SI) ? true : false);
	}
	
	@AfterMapping
	protected void setEntidadOrdenanteAfterMapping(@MappingTarget EntidadesOrdenante entidadesOrdenante, OrderingEntity orderingEntity) {
		EntidadesComunicadora entidad = new EntidadesComunicadora();
		
		
		
		long code = (int) orderingEntity.getCommunicatingEntity().getCode();
		entidad.setCodEntidadPresentadora(code);
		entidadesOrdenante.setEntidadesComunicadora(entidad);
		
		if (orderingEntity.getIndAeat()) {
			entidadesOrdenante.setIdentificadorEntidad(orderingEntity.getNifEntity());
		}
		entidadesOrdenante.setIndActivo(orderingEntity.isActive() ? EmbargosConstants.IND_FLAG_SI : EmbargosConstants.IND_FLAG_NO);
		entidadesOrdenante.setIndFormatoAeat(orderingEntity.getIndAeat() ? EmbargosConstants.IND_FLAG_SI : EmbargosConstants.IND_FLAG_NO);
		entidadesOrdenante.setIndCgpj(orderingEntity.getIndCgpj() ? EmbargosConstants.IND_FLAG_SI : EmbargosConstants.IND_FLAG_NO);
		entidadesOrdenante.setIndNorma63(orderingEntity.getIndNorma63() ? EmbargosConstants.IND_FLAG_SI : EmbargosConstants.IND_FLAG_NO);
	}
}
