package es.commerzbank.ice.embargos.domain.mapper;

import java.math.BigDecimal;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.CommunicatingEntity;
import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora;
import es.commerzbank.ice.utils.EmbargosConstants;

@Mapper(componentModel="spring")
public abstract class CommunicatingEntityMapper {

	@Mappings({
		@Mapping(source = "codCommunicatingEntity", target = "codEntidadPresentadora"),
		@Mapping(source = "bic", target = "bic"),
		@Mapping(source = "iban", target = "iban"),
		@Mapping(source = "account", target = "cuenta"),
		@Mapping(source = "nif", target = "nifEntidad"),
		@Mapping(source = "name", target = "desEntidad"),
		@Mapping(source = "filePrefix", target = "prefijoFicheros")
	})
	public abstract EntidadesComunicadora toEntidadComunicadora(CommunicatingEntity communicatingEntity);

	@Mappings({
		@Mapping(source = "codEntidadPresentadora", target = "codCommunicatingEntity"),
		@Mapping(source = "bic", target = "bic"),
		@Mapping(source = "iban", target = "iban"),
		@Mapping(source = "cuenta", target = "account"),
		@Mapping(source = "nifEntidad", target = "nif"),
		@Mapping(source = "desEntidad", target = "name"),
		@Mapping(source = "prefijoFicheros", target = "filePrefix")
	})
	public abstract CommunicatingEntity toCommunicatingEntity(EntidadesComunicadora entidadesComunicadora);
	
	@AfterMapping
	protected void setCommunicatingEntityAfterMapping(@MappingTarget CommunicatingEntity communicatingEntity, EntidadesComunicadora entidadesComunicadora) {
		if (entidadesComunicadora.getDiasRespuestaF1() != null) {
			communicatingEntity.setResponseDaysF1(entidadesComunicadora.getDiasRespuestaF1().intValue());
		}
		if (entidadesComunicadora.getDiasRespuestaF3() != null) {
			communicatingEntity.setResponseDaysF3(entidadesComunicadora.getDiasRespuestaF3().intValue());
		}
		if (entidadesComunicadora.getDiasRespuestaF6() != null) {
			communicatingEntity.setResponseDaysF6(entidadesComunicadora.getDiasRespuestaF6().intValue());
		}
		
		if (entidadesComunicadora.getIndActivo().equals(EmbargosConstants.IND_FLAG_YES)) {
			communicatingEntity.setActive(true);
		} else {
			communicatingEntity.setActive(false);
		}
	}
	
	@AfterMapping
	protected void setEntidadComunicadoraAfterMapping(@MappingTarget EntidadesComunicadora entidadesComunicadora, CommunicatingEntity communicatingEntity) {
		entidadesComunicadora.setIndActivo(EmbargosConstants.IND_FLAG_YES);
		entidadesComunicadora.setDiasRespuestaF1(new BigDecimal(communicatingEntity.getResponseDaysF1()));
		entidadesComunicadora.setDiasRespuestaF3(new BigDecimal(communicatingEntity.getResponseDaysF3()));
		entidadesComunicadora.setDiasRespuestaF6(new BigDecimal(communicatingEntity.getResponseDaysF6()));
	}

}
