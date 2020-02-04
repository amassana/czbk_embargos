package es.commerzbank.ice.embargos.domain.entity.listener;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import es.commerzbank.ice.embargos.config.AutowireEmbHelper;
import es.commerzbank.ice.embargos.domain.dto.CommunicatingEntity;
import es.commerzbank.ice.embargos.domain.entity.AuditoriaEmb;
import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora;
import es.commerzbank.ice.embargos.domain.mapper.CommunicatingEntityMapper;
import es.commerzbank.ice.embargos.service.AuditoriaEmbService;
import es.commerzbank.ice.utils.EmbargosConstants;

public class EntidadesComunicadoraListener {

	private static final Logger logger = LoggerFactory.getLogger(EntidadesComunicadoraListener.class);
	
	@Autowired
	private AuditoriaEmbService auditoriaEmbService; 
	
	@Autowired
	private CommunicatingEntityMapper communicatingEntityMapper;
	
	@PostPersist
	@PostUpdate
	public void methodExecuteBeforeSave(EntidadesComunicadora entidadComunicadora) {
	   	
		try {
			AutowireEmbHelper.autowire(this, this.auditoriaEmbService);
			AutowireEmbHelper.autowire(this, this.communicatingEntityMapper);
			
			AuditoriaEmb auditoria = new AuditoriaEmb();
			auditoria.setTabla(EmbargosConstants.TABLA_ENTIDADES_COMUNICADORAS);
			auditoria.setValorPk(entidadComunicadora.getDesEntidad());
			auditoria.setPkLogico(String.valueOf(entidadComunicadora.getCodEntidadPresentadora()));
			auditoria.setUsuario(entidadComunicadora.getUsuarioUltModificacion());
			auditoria.setFecha(LocalDateTime.now());
			
			CommunicatingEntity communicatingEntity = communicatingEntityMapper.toCommunicatingEntity(entidadComunicadora);
			
			Gson gson = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new JsonSerializer<ZonedDateTime>() {
				@Override
				public JsonElement serialize(ZonedDateTime zoned, Type type, JsonSerializationContext jsonSerializationContext) {
					return new JsonPrimitive((DateTimeFormatter.ISO_OFFSET_DATE_TIME).format(zoned));
				}
	            }).create();
			
			String communicatingEntityStr = gson.toJson(communicatingEntity);
			
			auditoria.setNewValue(communicatingEntityStr);
			
			auditoriaEmbService.saveAuditoria(auditoria);				
		}
		catch (Exception e) {
			logger.error("Error en el grabado de auditoria de Organismos Emisores", e);
		}
	}
}
