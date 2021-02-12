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
import es.commerzbank.ice.embargos.domain.dto.SeizedBankAccountDTO;
import es.commerzbank.ice.embargos.domain.entity.AuditoriaEmb;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.CuentaTrabaActuacion;
import es.commerzbank.ice.embargos.domain.entity.EstadoTraba;
import es.commerzbank.ice.embargos.domain.mapper.SeizedBankAccountMapper;
import es.commerzbank.ice.embargos.repository.SeizedBankAccountActionRepository;
import es.commerzbank.ice.embargos.repository.SeizureStatusRepository;
import es.commerzbank.ice.embargos.service.AuditoriaEmbService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;

public class CuentaTrabaListener {

	private static final Logger logger = LoggerFactory.getLogger(CuentaTrabaListener.class);
	
	@Autowired
	private AuditoriaEmbService auditoriaEmbService; 
	
	@Autowired
	private SeizedBankAccountMapper seizedBankAccountMapper;
	
	@Autowired
	private SeizureStatusRepository seizureStatusRepository;
	
	@Autowired
	private SeizedBankAccountActionRepository seizedBankAccountActionRepository;
	
	@PostPersist
	@PostUpdate
	public void methodExecuteBeforeSave(CuentaTraba cuentaTraba) {
	   	
		try {
			AutowireEmbHelper.autowire(this, this.auditoriaEmbService);
			AutowireEmbHelper.autowire(this, this.seizedBankAccountMapper);
			AutowireEmbHelper.autowire(this, this.seizureStatusRepository);
			AutowireEmbHelper.autowire(this, this.seizedBankAccountActionRepository);
			
			if (cuentaTraba.getEstadoTraba()!=null) {
				Optional<EstadoTraba> optEstadoTraba = seizureStatusRepository.findById(cuentaTraba.getEstadoTraba().getCodEstado());
				if (optEstadoTraba.isPresent()) {
					cuentaTraba.setEstadoTraba(optEstadoTraba.get());
				}
			}
			
			if (cuentaTraba.getCuentaTrabaActuacion()!=null) {
				Optional<CuentaTrabaActuacion> optCuentaTrabaActuacion = seizedBankAccountActionRepository.findById(cuentaTraba.getCuentaTrabaActuacion().getCodActuacion());
				if (optCuentaTrabaActuacion.isPresent()) {
					cuentaTraba.setCuentaTrabaActuacion(optCuentaTrabaActuacion.get());
				}
			}
			
			AuditoriaEmb auditoria = new AuditoriaEmb();
			auditoria.setTabla(EmbargosConstants.TABLA_CUENTA_TRABA);
			auditoria.setValorPk(String.valueOf(cuentaTraba.getCodCuentaTraba()));
			auditoria.setPkLogico(String.valueOf(cuentaTraba.getCodCuentaTraba()));
			auditoria.setUsuario(cuentaTraba.getUsuarioUltModificacion());
			auditoria.setFecha(LocalDateTime.now());
			
			SeizedBankAccountDTO seizedBankAccountDTO = seizedBankAccountMapper.toSeizedBankAccountDTO(cuentaTraba);
			
			Gson gson = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new JsonSerializer<ZonedDateTime>() {
				@Override
				public JsonElement serialize(ZonedDateTime zoned, Type type, JsonSerializationContext jsonSerializationContext) {
					return new JsonPrimitive((DateTimeFormatter.ISO_OFFSET_DATE_TIME).format(zoned));
				}
	            }).create();
			
			String seizedBankAccountDTOStr = gson.toJson(seizedBankAccountDTO);
			
			auditoria.setNewValue(seizedBankAccountDTOStr);
			
			auditoriaEmbService.saveAuditoria(auditoria);				
		}
		catch (Exception e) {
			logger.error("Error en el grabado de auditoria de CuentaTraba", e);
		}
	}
}
