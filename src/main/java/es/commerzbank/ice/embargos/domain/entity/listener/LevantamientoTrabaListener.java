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
import es.commerzbank.ice.embargos.domain.dto.LiftingDTO;
import es.commerzbank.ice.embargos.domain.entity.AuditoriaEmb;
import es.commerzbank.ice.embargos.domain.entity.EstadoLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.LevantamientoTraba;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.embargos.domain.mapper.LiftingMapper;
import es.commerzbank.ice.embargos.domain.mapper.LiftingStatusMapper;
import es.commerzbank.ice.embargos.repository.LiftingStatusRepository;
import es.commerzbank.ice.embargos.repository.SeizedRepository;
import es.commerzbank.ice.embargos.service.AuditoriaEmbService;
import es.commerzbank.ice.utils.EmbargosConstants;

public class LevantamientoTrabaListener {

	private static final Logger logger = LoggerFactory.getLogger(LevantamientoTrabaListener.class);
	
	@Autowired
	private AuditoriaEmbService auditoriaEmbService; 
	
	@Autowired
	private LiftingMapper liftingMapper;
	
	@Autowired
	private LiftingStatusMapper liftingStatusMapper;
	
	@Autowired
	private LiftingStatusRepository liftingStatusRepository;
	
	@Autowired
	private SeizedRepository seizedRepository;
	
	@PostPersist
	@PostUpdate
	public void methodExecuteBeforeSave(LevantamientoTraba levantamientoTraba) {
	   	
		try {
			AutowireEmbHelper.autowire(this, this.auditoriaEmbService);
			AutowireEmbHelper.autowire(this, this.liftingMapper);
			AutowireEmbHelper.autowire(this, this.liftingStatusMapper);
			AutowireEmbHelper.autowire(this, this.liftingStatusRepository);
			AutowireEmbHelper.autowire(this, this.seizedRepository);
			
			if (levantamientoTraba.getEstadoLevantamiento()!=null) {
				Optional<EstadoLevantamiento> optEstadoLevantamiento = liftingStatusRepository.findById(levantamientoTraba.getEstadoLevantamiento().getCodEstado());
				if (optEstadoLevantamiento.isPresent()) {
					levantamientoTraba.setEstadoLevantamiento(optEstadoLevantamiento.get());
				}
			}
			
			if (levantamientoTraba.getTraba()!=null) {
				Optional<Traba> optTraba = seizedRepository.findById(levantamientoTraba.getTraba().getCodTraba());
				if (optTraba.isPresent()) {
					levantamientoTraba.setTraba(optTraba.get());
				}
			}
			
			AuditoriaEmb auditoria = new AuditoriaEmb();
			auditoria.setTabla(EmbargosConstants.TABLA_LEVANTAMIENTO_TRABA);
			auditoria.setValorPk(String.valueOf(levantamientoTraba.getCodLevantamiento()));
			auditoria.setPkLogico(String.valueOf(levantamientoTraba.getCodLevantamiento()));
			auditoria.setUsuario(levantamientoTraba.getUsuarioUltModificacion());
			auditoria.setFecha(LocalDateTime.now());
			
			LiftingDTO liftingDTO = liftingMapper.toLiftingDTO(levantamientoTraba);
			if (levantamientoTraba.getEstadoLevantamiento()!=null) {
				liftingDTO.setStatus(liftingStatusMapper.toLiftingStatus(levantamientoTraba.getEstadoLevantamiento()));	
			}
			
			Gson gson = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new JsonSerializer<ZonedDateTime>() {
				@Override
				public JsonElement serialize(ZonedDateTime zoned, Type type, JsonSerializationContext jsonSerializationContext) {
					return new JsonPrimitive((DateTimeFormatter.ISO_OFFSET_DATE_TIME).format(zoned));
				}
	            }).create();
			
			String liftingDTOStr = gson.toJson(liftingDTO);
			
			auditoria.setNewValue(liftingDTOStr);
			
			auditoriaEmbService.saveAuditoria(auditoria);				
		}
		catch (Exception e) {
			logger.error("Error en el grabado de auditoria de LevantamientoTraba", e);
		}
	}
}
