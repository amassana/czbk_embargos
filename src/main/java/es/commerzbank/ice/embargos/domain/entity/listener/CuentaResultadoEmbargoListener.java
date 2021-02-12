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
import es.commerzbank.ice.embargos.domain.dto.FinalResponseBankAccountDTO;
import es.commerzbank.ice.embargos.domain.entity.AuditoriaEmb;
import es.commerzbank.ice.embargos.domain.entity.CuentaEmbargo;
import es.commerzbank.ice.embargos.domain.entity.CuentaResultadoEmbargo;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.mapper.FinalResponseBankAccountMapper;
import es.commerzbank.ice.embargos.repository.SeizedBankAccountRepository;
import es.commerzbank.ice.embargos.repository.SeizureBankAccountRepository;
import es.commerzbank.ice.embargos.service.AuditoriaEmbService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;

public class CuentaResultadoEmbargoListener {

	private static final Logger logger = LoggerFactory.getLogger(CuentaResultadoEmbargoListener.class);
	
	@Autowired
	private AuditoriaEmbService auditoriaEmbService; 
	
	@Autowired
	private FinalResponseBankAccountMapper bankAccountMapper;
	
	@Autowired
	private SeizureBankAccountRepository seizureBankAccountRepository;
	
	@Autowired
	private SeizedBankAccountRepository seizedBankAccountRepository;
	
	@PostPersist
	@PostUpdate
	public void methodExecuteBeforeSave(CuentaResultadoEmbargo cuentaResultadoEmbargo) {
	   	
		try {
			AutowireEmbHelper.autowire(this, this.auditoriaEmbService);
			AutowireEmbHelper.autowire(this, this.bankAccountMapper);
			AutowireEmbHelper.autowire(this, this.seizureBankAccountRepository);
			AutowireEmbHelper.autowire(this, this.seizedBankAccountRepository);
			
			if (cuentaResultadoEmbargo.getCuentaEmbargo()!=null) {
				Optional<CuentaEmbargo> cuentaEmbargoOpt = seizureBankAccountRepository.findById(cuentaResultadoEmbargo.getCuentaEmbargo().getCodCuentaEmbargo());
				if (cuentaEmbargoOpt.isPresent()) {
					cuentaResultadoEmbargo.setCuentaEmbargo(cuentaEmbargoOpt.get());
				}
			}
			
			if (cuentaResultadoEmbargo.getCuentaTraba()!=null) {
				Optional<CuentaTraba> cuentaTrabaOpt = seizedBankAccountRepository.findById(cuentaResultadoEmbargo.getCuentaTraba().getCodCuentaTraba());
				if (cuentaTrabaOpt.isPresent()) {
					cuentaResultadoEmbargo.setCuentaTraba(cuentaTrabaOpt.get());
				}
			}
			
			AuditoriaEmb auditoria = new AuditoriaEmb();
			auditoria.setTabla(EmbargosConstants.TABLA_CUENTA_RESULTADO_EMBARGO);
			auditoria.setValorPk(String.valueOf(cuentaResultadoEmbargo.getCodCuentaResultadoEmbargo()));
			auditoria.setPkLogico(String.valueOf(cuentaResultadoEmbargo.getCodCuentaResultadoEmbargo()));
			auditoria.setUsuario(cuentaResultadoEmbargo.getUsuarioUltModificacion());
			auditoria.setFecha(LocalDateTime.now());
			
			FinalResponseBankAccountDTO accountDTO = bankAccountMapper.toBankAccount(cuentaResultadoEmbargo);
			if (accountDTO.getAmountLocked()!=null && accountDTO.getAmountTransfer()!=null)
				accountDTO.setAmountRaised(accountDTO.getAmountLocked() - accountDTO.getAmountTransfer());
			
			Gson gson = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new JsonSerializer<ZonedDateTime>() {
				@Override
				public JsonElement serialize(ZonedDateTime zoned, Type type, JsonSerializationContext jsonSerializationContext) {
					return new JsonPrimitive((DateTimeFormatter.ISO_OFFSET_DATE_TIME).format(zoned));
				}
	            }).create();
			
			String accountDTOStr = gson.toJson(accountDTO);
			
			auditoria.setNewValue(accountDTOStr);
			
			auditoriaEmbService.saveAuditoria(auditoria);				
		}
		catch (Exception e) {
			logger.error("Error en el grabado de auditoria de CuentaResultadoEmbargo", e);
		}
	}
}
