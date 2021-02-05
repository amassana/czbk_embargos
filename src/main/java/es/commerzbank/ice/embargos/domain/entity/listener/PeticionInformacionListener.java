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
import es.commerzbank.ice.embargos.domain.dto.PetitionCaseDTO;
import es.commerzbank.ice.embargos.domain.entity.AuditoriaEmb;
import es.commerzbank.ice.embargos.domain.entity.DatosCliente;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacion;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacionCuenta;
import es.commerzbank.ice.embargos.domain.mapper.InformationPetitionMapper;
import es.commerzbank.ice.embargos.repository.ClientDataRepository;
import es.commerzbank.ice.embargos.repository.InformationPetitionBankAccountRepository;
import es.commerzbank.ice.embargos.service.AuditoriaEmbService;
import es.commerzbank.ice.utils.EmbargosConstants;

public class PeticionInformacionListener {

	private static final Logger logger = LoggerFactory.getLogger(PeticionInformacionListener.class);
	
	@Autowired
	private InformationPetitionMapper informationPetitionMapper;
	
	@Autowired
	private ClientDataRepository clientDataRepository;
	
	@Autowired
	private InformationPetitionBankAccountRepository informationPetitionBankAccountRepository; 
	
	@Autowired
	private AuditoriaEmbService auditoriaEmbService;

	@PostPersist
	@PostUpdate
	public void methodExecuteBeforeSave(PeticionInformacion peticionInformacion) {
	   	
		try {
			AutowireEmbHelper.autowire(this, this.auditoriaEmbService);
			AutowireEmbHelper.autowire(this, this.informationPetitionMapper);
			AutowireEmbHelper.autowire(this, this.clientDataRepository);
			AutowireEmbHelper.autowire(this, this.informationPetitionBankAccountRepository);

			//if (peticionInformacion.getNumInvocations() > 1)
			//	return;

			logger.info("Listener "+ peticionInformacion +" "+ peticionInformacion.hashCode() + " "+ peticionInformacion.getCodPeticion() +" "+ peticionInformacion.getNumInvocations());

			if (peticionInformacion.getDatosCliente()!=null) {
				Optional<DatosCliente> optDatosCliente = clientDataRepository.findById(peticionInformacion.getDatosCliente().getNif());
				if (optDatosCliente.isPresent()) {
					peticionInformacion.setDatosCliente(optDatosCliente.get());
				}
			}
			
			if (peticionInformacion.getPeticionInformacionCuentas()!=null) {
				List<PeticionInformacionCuenta> peticionList = new ArrayList<PeticionInformacionCuenta>();
				for (PeticionInformacionCuenta peticionInfCuenta : peticionInformacion.getPeticionInformacionCuentas()) {
					if (peticionInfCuenta!=null) {
						Optional<PeticionInformacionCuenta> optPeticionInfCuenta = informationPetitionBankAccountRepository.findById(peticionInfCuenta.getId());
						if (optPeticionInfCuenta.isPresent()) {
							peticionList.add(optPeticionInfCuenta.get());
						}						
					}
				}
				
				if (peticionList.size()>0) peticionInformacion.setPeticionInformacionCuentas(peticionList);
			}
			
			AuditoriaEmb auditoria = new AuditoriaEmb();
			auditoria.setTabla(EmbargosConstants.TABLA_PETICION_INFORMACION);
			auditoria.setValorPk(String.valueOf(peticionInformacion.getCodPeticion()));
			auditoria.setPkLogico(String.valueOf(peticionInformacion.getCodPeticion()));
			auditoria.setUsuario(peticionInformacion.getUsuarioUltModificacion());
			auditoria.setFecha(LocalDateTime.now());
			
			PetitionCaseDTO petitionCaseDTO = informationPetitionMapper.toInformationPetitionDTO(peticionInformacion);
			
			Gson gson = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new JsonSerializer<ZonedDateTime>() {
				@Override
				public JsonElement serialize(ZonedDateTime zoned, Type type, JsonSerializationContext jsonSerializationContext) {
					return new JsonPrimitive((DateTimeFormatter.ISO_OFFSET_DATE_TIME).format(zoned));
				}
	            }).create();
			
			String petitionCaseDTOStr = gson.toJson(petitionCaseDTO);
			
			auditoria.setNewValue(petitionCaseDTOStr);
			
			auditoriaEmbService.saveAuditoria(auditoria);				
		}
		catch (Exception e) {
			logger.error("Error en el grabado de auditoria de PeticionInformacion", e);
		}
	}
}
