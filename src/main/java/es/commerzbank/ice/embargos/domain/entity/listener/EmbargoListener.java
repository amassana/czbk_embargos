package es.commerzbank.ice.embargos.domain.entity.listener;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
import es.commerzbank.ice.embargos.domain.dto.SeizureDTO;
import es.commerzbank.ice.embargos.domain.entity.AuditoriaEmb;
import es.commerzbank.ice.embargos.domain.entity.DatosCliente;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.embargos.domain.mapper.SeizureMapper;
import es.commerzbank.ice.embargos.repository.ClientDataRepository;
import es.commerzbank.ice.embargos.repository.SeizedRepository;
import es.commerzbank.ice.embargos.service.AuditoriaEmbService;
import es.commerzbank.ice.utils.EmbargosConstants;

public class EmbargoListener {

	private static final Logger logger = LoggerFactory.getLogger(EmbargoListener.class);
	
	@Autowired
	private AuditoriaEmbService auditoriaEmbService; 
	
	@Autowired
	private SeizureMapper seizureMapper; 
	
	@Autowired
	private ClientDataRepository clientDataRepository;
	
	@Autowired
	private SeizedRepository seizedRepository;
	
	@PostPersist
	@PostUpdate
	public void methodExecuteBeforeSave(Embargo embargo) {
	   	
		try {
			AutowireEmbHelper.autowire(this, this.auditoriaEmbService);
			AutowireEmbHelper.autowire(this, this.seizureMapper);
			AutowireEmbHelper.autowire(this, this.clientDataRepository);
			AutowireEmbHelper.autowire(this, this.seizedRepository);
			
			if (embargo.getDatosCliente()!=null) {
				Optional<DatosCliente> optDatosCliente = clientDataRepository.findById(embargo.getDatosCliente().getNif());
				if (optDatosCliente.isPresent()) {
					embargo.setDatosCliente(optDatosCliente.get());
				}
			}
			
			if (embargo.getTrabas()!=null) {
				List<Traba> trabaList = new ArrayList<Traba>();
				for (Traba traba : embargo.getTrabas()) {
					if (traba!=null) {
						Optional<Traba> optTraba = seizedRepository.findById(traba.getCodTraba());
						if (optTraba.isPresent()) {
							trabaList.add(optTraba.get());
						}						
					}
				}
				embargo.setTrabas(trabaList);
			}
			
			AuditoriaEmb auditoria = new AuditoriaEmb();
			auditoria.setTabla(EmbargosConstants.TABLA_EMBARGO);
			auditoria.setValorPk(String.valueOf(embargo.getCodEmbargo()));
			auditoria.setPkLogico(String.valueOf(embargo.getCodEmbargo()));
			auditoria.setUsuario(embargo.getUsuarioUltModificacion());
			auditoria.setFecha(LocalDateTime.now());
			
			SeizureDTO seizureDTO = seizureMapper.toSeizureDTO(embargo);
			
			Gson gson = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new JsonSerializer<ZonedDateTime>() {
				@Override
				public JsonElement serialize(ZonedDateTime zoned, Type type, JsonSerializationContext jsonSerializationContext) {
					return new JsonPrimitive((DateTimeFormatter.ISO_OFFSET_DATE_TIME).format(zoned));
				}
	            }).create();
			
			String seizureDTOStr = gson.toJson(seizureDTO);
			
			auditoria.setNewValue(seizureDTOStr);
			
			auditoriaEmbService.saveAuditoria(auditoria);				
		}
		catch (Exception e) {
			logger.error("Error en el grabado de auditoria de Embargo", e);
		}
	}
}
