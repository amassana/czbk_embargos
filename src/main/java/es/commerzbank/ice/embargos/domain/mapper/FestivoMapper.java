package es.commerzbank.ice.embargos.domain.mapper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import es.commerzbank.ice.comun.lib.domain.entity.Festivo;
import es.commerzbank.ice.embargos.domain.entity.FestivoEmbargo;

@Mapper(componentModel="spring")
public abstract class FestivoMapper {

	@Mappings({
		@Mapping(source = "localidad.codLocalidad", target = "codLocalidad")
	})
	public abstract FestivoEmbargo toFestivoEmbargo(Festivo festivo);
	
	public abstract ArrayList<FestivoEmbargo> toFestivoEmbargo(List<Festivo> festivo);
	
	public Long TimestampToLong(Timestamp timestamp){
		Long retorno = null;
		
        if (timestamp != null){
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    		retorno = Long.parseLong(sdf.format(timestamp));
        }
        
        return retorno;
    }
}
