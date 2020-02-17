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
import es.commerzbank.ice.embargos.domain.dto.FinalResponseDTO;
import es.commerzbank.ice.embargos.domain.entity.AuditoriaEmb;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import es.commerzbank.ice.embargos.domain.entity.ResultadoEmbargo;
import es.commerzbank.ice.embargos.domain.mapper.FinalResponseMapper;
import es.commerzbank.ice.embargos.repository.SeizureRepository;
import es.commerzbank.ice.embargos.service.AuditoriaEmbService;
import es.commerzbank.ice.utils.EmbargosConstants;

public class ResultadoEmbargoListener {

	private static final Logger logger = LoggerFactory.getLogger(ResultadoEmbargoListener.class);
	
	@Autowired
	private AuditoriaEmbService auditoriaEmbService;

	@Autowired
	private FinalResponseMapper finalResponseMapper;

	@Autowired
	private SeizureRepository seizureRepository;
	
	@PostPersist
	@PostUpdate
	public void methodExecuteBeforeSave(ResultadoEmbargo resultadoEmbargo) {
	   	
		try {
			AutowireEmbHelper.autowire(this, this.auditoriaEmbService);
			AutowireEmbHelper.autowire(this, this.finalResponseMapper);
			AutowireEmbHelper.autowire(this, this.seizureRepository);
			
			if (resultadoEmbargo.getEmbargo()!=null) {
				Optional<Embargo> embargoOpt = seizureRepository.findById(resultadoEmbargo.getEmbargo().getCodEmbargo());
				if (embargoOpt.isPresent()) {
					resultadoEmbargo.setEmbargo(embargoOpt.get());
				}
			}
			
			AuditoriaEmb auditoria = new AuditoriaEmb();
			auditoria.setTabla(EmbargosConstants.TABLA_RESULTADO_EMBARGO);
			auditoria.setValorPk(String.valueOf(resultadoEmbargo.getCodResultadoEmbargo()));
			auditoria.setPkLogico(String.valueOf(resultadoEmbargo.getCodResultadoEmbargo()));
			auditoria.setUsuario(resultadoEmbargo.getUsuarioUltModificacion());
			auditoria.setFecha(LocalDateTime.now());
			
			FinalResponseDTO finalResponseDTO = finalResponseMapper.toFinalResponse(resultadoEmbargo);
			
			Gson gson = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new JsonSerializer<ZonedDateTime>() {
				@Override
				public JsonElement serialize(ZonedDateTime zoned, Type type, JsonSerializationContext jsonSerializationContext) {
					return new JsonPrimitive((DateTimeFormatter.ISO_OFFSET_DATE_TIME).format(zoned));
				}
	            }).create();
			
			String finalResponseDTOStr = gson.toJson(finalResponseDTO);
			
			auditoria.setNewValue(finalResponseDTOStr);
			
			auditoriaEmbService.saveAuditoria(auditoria);
		}
		catch (Exception e) {
			logger.error("Error en el grabado de auditoria de ResultadoEmbargo", e);
		}
	}
}
