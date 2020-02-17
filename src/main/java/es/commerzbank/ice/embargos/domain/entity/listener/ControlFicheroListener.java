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
import es.commerzbank.ice.embargos.domain.dto.FileControlDTO;
import es.commerzbank.ice.embargos.domain.entity.AuditoriaEmb;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlfichero;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.repository.CommunicatingEntityRepository;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.FileControlStatusRepository;
import es.commerzbank.ice.embargos.service.AuditoriaEmbService;
import es.commerzbank.ice.utils.EmbargosConstants;

public class ControlFicheroListener {

	private static final Logger logger = LoggerFactory.getLogger(ControlFicheroListener.class);
	
	@Autowired
	private AuditoriaEmbService auditoriaEmbService; 
	
	@Autowired
	private FileControlMapper fileControlMapper;
	
	@Autowired
	private FileControlStatusRepository fileControlStatusRepository; 
	
	@Autowired
	private FileControlRepository fileControlRepository;
	
	@Autowired
	private CommunicatingEntityRepository communicatingEntityRepository;
	
	@PostPersist
	@PostUpdate
	public void methodExecuteBeforeSave(ControlFichero controlFichero) {
	   	
		try {
			AutowireEmbHelper.autowire(this, this.auditoriaEmbService);
			AutowireEmbHelper.autowire(this, this.fileControlMapper);
			AutowireEmbHelper.autowire(this, this.fileControlStatusRepository);
			AutowireEmbHelper.autowire(this, this.fileControlRepository);
			AutowireEmbHelper.autowire(this, this.communicatingEntityRepository);
			
			if (controlFichero.getEstadoCtrlfichero()!=null) {
				Optional<EstadoCtrlfichero> optEstadoCtrlfichero = fileControlStatusRepository.findById(controlFichero.getEstadoCtrlfichero().getId());
				if (optEstadoCtrlfichero.isPresent()) {
					controlFichero.setEstadoCtrlfichero(optEstadoCtrlfichero.get());
				}
			}
			
			if (controlFichero.getControlFicheroRespuesta()!=null) {
				Optional<ControlFichero> optControlFichero = fileControlRepository.findById(controlFichero.getControlFicheroRespuesta().getCodControlFichero());
				if (optControlFichero.isPresent()) {
					controlFichero.setControlFicheroRespuesta(optControlFichero.get());
				}
			}
			
			if (controlFichero.getEntidadesComunicadora()!=null) {
				Optional<EntidadesComunicadora> optEntidadesComunicadora = communicatingEntityRepository.findById(controlFichero.getEntidadesComunicadora().getCodEntidadPresentadora());
				if (optEntidadesComunicadora.isPresent()) {
					controlFichero.setEntidadesComunicadora(optEntidadesComunicadora.get());
				}
			}
			
			AuditoriaEmb auditoria = new AuditoriaEmb();
			auditoria.setTabla(EmbargosConstants.TABLA_CONTROL_FICHERO);
			auditoria.setValorPk(controlFichero.getNombreFichero());
			auditoria.setPkLogico(String.valueOf(controlFichero.getCodControlFichero()));
			auditoria.setUsuario(controlFichero.getUsuarioUltModificacion());
			auditoria.setFecha(LocalDateTime.now());
			
			FileControlDTO fileControlDTO = fileControlMapper.toFileControlDTO(controlFichero);
			
			Gson gson = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new JsonSerializer<ZonedDateTime>() {
				@Override
				public JsonElement serialize(ZonedDateTime zoned, Type type, JsonSerializationContext jsonSerializationContext) {
					return new JsonPrimitive((DateTimeFormatter.ISO_OFFSET_DATE_TIME).format(zoned));
				}
	            }).create();
			
			String fileControlDTOStr = gson.toJson(fileControlDTO);
			
			auditoria.setNewValue(fileControlDTOStr);
			
			auditoriaEmbService.saveAuditoria(auditoria);				
		}
		catch (Exception e) {
			logger.error("Error en el grabado de auditoria de ControlFichero", e);
		}
	}
}
