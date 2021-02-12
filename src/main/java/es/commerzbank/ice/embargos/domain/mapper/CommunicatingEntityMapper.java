package es.commerzbank.ice.embargos.domain.mapper;

import java.math.BigDecimal;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.CommunicatingEntity;
import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;

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

		communicatingEntity.setActive(entidadesComunicadora.getIndActivo().equals(EmbargosConstants.IND_FLAG_SI) ? true : false);
		communicatingEntity.setIndAeat(entidadesComunicadora.getIndFormatoAeat() != null && entidadesComunicadora.getIndFormatoAeat().equals(EmbargosConstants.IND_FLAG_SI) ? true : false);
		communicatingEntity.setIndCgpj(entidadesComunicadora.getIndCgpj() != null && entidadesComunicadora.getIndCgpj().equals(EmbargosConstants.IND_FLAG_SI) ? true : false);
		communicatingEntity.setIndNorma63(entidadesComunicadora.getIndNorma63() != null && entidadesComunicadora.getIndNorma63().equals(EmbargosConstants.IND_FLAG_SI) ? true : false);
	}
	
	@AfterMapping
	protected void setEntidadComunicadoraAfterMapping(@MappingTarget EntidadesComunicadora entidadesComunicadora, CommunicatingEntity communicatingEntity) {
		entidadesComunicadora.setIndActivo(communicatingEntity.isActive() ? EmbargosConstants.IND_FLAG_SI : EmbargosConstants.IND_FLAG_NO);
		
		if (communicatingEntity.getResponseDaysF1() != null) {
			entidadesComunicadora.setDiasRespuestaF1(new BigDecimal(communicatingEntity.getResponseDaysF1()));
		}
		
		if (communicatingEntity.getResponseDaysF3() != null) {
			entidadesComunicadora.setDiasRespuestaF3(new BigDecimal(communicatingEntity.getResponseDaysF3()));
		}
		
		if (communicatingEntity.getResponseDaysF6() != null) {
			entidadesComunicadora.setDiasRespuestaF6(new BigDecimal(communicatingEntity.getResponseDaysF6()));
		}
		
		if (communicatingEntity.isIndAeat()) {
			entidadesComunicadora.setIdentificadorEntidad(communicatingEntity.getNif());
		}
		
		entidadesComunicadora.setIndFormatoAeat(communicatingEntity.isIndAeat() ? EmbargosConstants.IND_FLAG_SI : EmbargosConstants.IND_FLAG_NO);
		entidadesComunicadora.setIndCgpj(communicatingEntity.isIndCgpj() ? EmbargosConstants.IND_FLAG_SI : EmbargosConstants.IND_FLAG_NO);
		entidadesComunicadora.setIndNorma63(communicatingEntity.isIndNorma63() ? EmbargosConstants.IND_FLAG_SI : EmbargosConstants.IND_FLAG_NO);
		
	}

}
