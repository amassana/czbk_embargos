package es.commerzbank.ice.embargos.domain.mapper;

import es.commerzbank.ice.comun.lib.domain.dto.Festive;
import es.commerzbank.ice.embargos.domain.entity.FestivoEmbargo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel="spring")
public abstract class FestivoMapper {

	@Mappings({
		@Mapping(source = "location.codLocation", target = "codLocalidad")
	})
	public abstract FestivoEmbargo toFestivoEmbargo(Festive festivo);
	
	public abstract ArrayList<FestivoEmbargo> toFestivoEmbargo(List<Festive> festivo);
	
	public Long TimestampToLong(Timestamp timestamp){
		Long retorno = null;
		
        if (timestamp != null){
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    		retorno = Long.parseLong(sdf.format(timestamp));
        }
        
        return retorno;
    }
}
