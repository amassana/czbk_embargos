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
import es.commerzbank.ice.embargos.domain.dto.BankAccountLiftingDTO;
import es.commerzbank.ice.embargos.domain.entity.AuditoriaEmb;
import es.commerzbank.ice.embargos.domain.entity.CuentaLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.EstadoLevantamiento;
import es.commerzbank.ice.embargos.domain.mapper.BankAccountLiftingMapper;
import es.commerzbank.ice.embargos.domain.mapper.LiftingStatusMapper;
import es.commerzbank.ice.embargos.repository.LiftingStatusRepository;
import es.commerzbank.ice.embargos.service.AuditoriaEmbService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;

public class CuentaLevantamientoListener {

	private static final Logger logger = LoggerFactory.getLogger(CuentaLevantamientoListener.class);
	
	@Autowired
	private AuditoriaEmbService auditoriaEmbService; 
	
	@Autowired
	private BankAccountLiftingMapper bankAccountLiftingMapper;
	
	@Autowired
	private LiftingStatusMapper liftingStatusMapper;
	
	@Autowired
	private LiftingStatusRepository liftingStatusRepository;
	
	@PostPersist
	@PostUpdate
	public void methodExecuteBeforeSave(CuentaLevantamiento cuentaLevantamiento) {
	   	
		try {
			AutowireEmbHelper.autowire(this, this.auditoriaEmbService);
			AutowireEmbHelper.autowire(this, this.bankAccountLiftingMapper);
			AutowireEmbHelper.autowire(this, this.liftingStatusMapper);
			AutowireEmbHelper.autowire(this, this.liftingStatusRepository);
			
			if (cuentaLevantamiento.getEstadoLevantamiento()!=null) {
				Optional<EstadoLevantamiento> optEstadoLevantamiento = liftingStatusRepository.findById(cuentaLevantamiento.getEstadoLevantamiento().getCodEstado());
				if (optEstadoLevantamiento.isPresent()) {
					cuentaLevantamiento.setEstadoLevantamiento(optEstadoLevantamiento.get());
				}
			}
			
			AuditoriaEmb auditoria = new AuditoriaEmb();
			auditoria.setTabla(EmbargosConstants.TABLA_CUENTA_LEVANTAMIENTO);
			auditoria.setValorPk(String.valueOf(cuentaLevantamiento.getCodCuentaLevantamiento()));
			auditoria.setPkLogico(String.valueOf(cuentaLevantamiento.getCodCuentaLevantamiento()));
			auditoria.setUsuario(cuentaLevantamiento.getUsuarioUltModificacion());
			auditoria.setFecha(LocalDateTime.now());
			
			BankAccountLiftingDTO bankAccountLiftingDTO = bankAccountLiftingMapper.toBankAccountLiftingDTO(cuentaLevantamiento);
			if (cuentaLevantamiento.getEstadoLevantamiento()!=null) {
				bankAccountLiftingDTO.setStatus(liftingStatusMapper.toLiftingStatus(cuentaLevantamiento.getEstadoLevantamiento()));	
			}
			
			Gson gson = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new JsonSerializer<ZonedDateTime>() {
				@Override
				public JsonElement serialize(ZonedDateTime zoned, Type type, JsonSerializationContext jsonSerializationContext) {
					return new JsonPrimitive((DateTimeFormatter.ISO_OFFSET_DATE_TIME).format(zoned));
				}
	            }).create();
			
			String bankAccountLiftingDTOStr = gson.toJson(bankAccountLiftingDTO);
			
			auditoria.setNewValue(bankAccountLiftingDTOStr);
			
			auditoriaEmbService.saveAuditoria(auditoria);				
		}
		catch (Exception e) {
			logger.error("Error en el grabado de auditoria de CuentaLevantamiento", e);
		}
	}
}
