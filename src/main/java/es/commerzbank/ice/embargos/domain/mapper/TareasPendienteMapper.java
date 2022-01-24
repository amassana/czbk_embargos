package es.commerzbank.ice.embargos.domain.mapper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import es.commerzbank.ice.comun.lib.domain.entity.Tarea;
import es.commerzbank.ice.embargos.domain.entity.TareasPendiente;

@Mapper(componentModel="spring")
public abstract class TareasPendienteMapper {

	@Mappings({
		@Mapping(source = "fechaTopeRealizacion", target = "fTarea"),
		@Mapping(target = "estado", expression = "java(tareasPendiente.getEstadoPorDefecto())"),
		@Mapping(target = "accion", expression = "java(tareasPendiente.getAccionPorDefecto())"),
		@Mapping(target = "indActivo", expression = "java(tareasPendiente.getIndActivoPorDefecto())"),
		@Mapping(target = "aplicacion", expression = "java(tareasPendiente.getAplicacionPorDefecto())")
	})
	public abstract Tarea toTarea(TareasPendiente tareasPendiente);
	
	public abstract ArrayList<Tarea> toTarea(List<TareasPendiente> tareasPendiente);
	
	public Timestamp LongToTimestamp(Long valor){
		Timestamp retorno = null;
		
        if (valor != null){
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    		try {
				retorno = new Timestamp(sdf.parse(valor.toString()).getTime());
			} catch (Exception e) {
			}
        }
        
        return retorno;
    }
}
