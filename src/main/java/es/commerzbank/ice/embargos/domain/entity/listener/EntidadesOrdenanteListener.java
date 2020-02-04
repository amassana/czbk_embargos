package es.commerzbank.ice.embargos.domain.entity.listener;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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
import es.commerzbank.ice.embargos.domain.dto.OrderingEntity;
import es.commerzbank.ice.embargos.domain.entity.AuditoriaEmb;
import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora;
import es.commerzbank.ice.embargos.domain.entity.EntidadesOrdenante;
import es.commerzbank.ice.embargos.domain.mapper.OrderingEntityMapper;
import es.commerzbank.ice.embargos.repository.CommunicatingEntityRepository;
import es.commerzbank.ice.embargos.service.AuditoriaEmbService;
import es.commerzbank.ice.utils.EmbargosConstants;

public class EntidadesOrdenanteListener {

	private static final Logger logger = LoggerFactory.getLogger(EntidadesOrdenanteListener.class);
	
	@Autowired
	private AuditoriaEmbService auditoriaEmbService; 
	
	@Autowired
	private OrderingEntityMapper orderingEntityMapper;
	
	@Autowired
	private CommunicatingEntityRepository communicatingEntityRepository;
	
	@PostPersist
	@PostUpdate
	public void methodExecuteBeforeSave(EntidadesOrdenante entidadOrdenante) {
	   	
		try {
			AutowireEmbHelper.autowire(this, this.auditoriaEmbService);
			AutowireEmbHelper.autowire(this, this.orderingEntityMapper);
			
			if (entidadOrdenante.getEntidadesComunicadora()!=null) {
				Optional<EntidadesComunicadora> optEntidadCom = communicatingEntityRepository.findById(entidadOrdenante.getEntidadesComunicadora().getCodEntidadPresentadora());
				if (optEntidadCom.isPresent()) {
					entidadOrdenante.setEntidadesComunicadora(optEntidadCom.get());
				}
			}
			
			AuditoriaEmb auditoria = new AuditoriaEmb();
			auditoria.setTabla(EmbargosConstants.TABLA_ENTIDADES_ORDENANTES);
			auditoria.setValorPk(entidadOrdenante.getDesEntidad());
			auditoria.setPkLogico(String.valueOf(entidadOrdenante.getCodEntidadOrdenante()));
			auditoria.setUsuario(entidadOrdenante.getUsuarioUltModificacion());
			auditoria.setFecha(LocalDateTime.now());
			
			OrderingEntity orderingEntity = orderingEntityMapper.toOrderingEntity(entidadOrdenante);
			
			Gson gson = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new JsonSerializer<ZonedDateTime>() {
				@Override
				public JsonElement serialize(ZonedDateTime zoned, Type type, JsonSerializationContext jsonSerializationContext) {
					return new JsonPrimitive((DateTimeFormatter.ISO_OFFSET_DATE_TIME).format(zoned));
				}
	            }).create();
			
			String orderingEntityStr = gson.toJson(orderingEntity);
			
			auditoria.setNewValue(orderingEntityStr);
			
			auditoriaEmbService.saveAuditoria(auditoria);				
		}
		catch (Exception e) {
			logger.error("Error en el grabado de auditoria de Organismos Ordenantes", e);
		}
	}
}
