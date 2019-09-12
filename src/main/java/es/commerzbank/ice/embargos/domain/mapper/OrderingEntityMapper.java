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
		if (entidadesOrdenante.getIndActivo().equals(EmbargosConstants.IND_FLAG_SI)) {
			orderingentity.setActive(true);
		} else {
			orderingentity.setActive(false);
		}
		
		if (entidadesOrdenante.getIndCgpj().equals(EmbargosConstants.IND_FLAG_SI)) {
			orderingentity.setIndCgpj(true);
		} else {
			orderingentity.setIndCgpj(false);
		}
		
		if (entidadesOrdenante.getIndFormatoAeat().equals(EmbargosConstants.IND_FLAG_SI)) {
			orderingentity.setIndAeat(true);
		} else {
			orderingentity.setIndAeat(false);
		}
		
		if (entidadesOrdenante.getIndNorma63().equals(EmbargosConstants.IND_FLAG_SI)) {
			orderingentity.setIndNorma63(true);
		} else {
			orderingentity.setIndNorma63(false);
		}
	}
	
	@AfterMapping
	protected void setEntidadOrdenanteAfterMapping(@MappingTarget EntidadesOrdenante entidadesOrdenante, OrderingEntity orderingentity) {
		if (orderingentity.isActive()) {
			entidadesOrdenante.setIndActivo(EmbargosConstants.IND_FLAG_SI);
		} else {
			entidadesOrdenante.setIndActivo(EmbargosConstants.IND_FLAG_NO);
		}
		
		if (orderingentity.getIndCgpj()) {
			entidadesOrdenante.setIndCgpj(EmbargosConstants.IND_FLAG_SI);
		} else {
			entidadesOrdenante.setIndCgpj(EmbargosConstants.IND_FLAG_NO);
		}
		
		if (orderingentity.getIndAeat()) {
			entidadesOrdenante.setIndFormatoAeat(EmbargosConstants.IND_FLAG_SI);
		} else {
			entidadesOrdenante.setIndFormatoAeat(EmbargosConstants.IND_FLAG_NO);
		}
		
		if (orderingentity.getIndNorma63()) {
			entidadesOrdenante.setIndNorma63(EmbargosConstants.IND_FLAG_SI);
		} else {
			entidadesOrdenante.setIndNorma63(EmbargosConstants.IND_FLAG_NO);
		}
	}
}
