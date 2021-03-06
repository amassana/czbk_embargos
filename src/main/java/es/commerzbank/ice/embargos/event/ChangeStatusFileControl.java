package es.commerzbank.ice.embargos.event;

import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.service.TaskService;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlfichero;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.ICEDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static es.commerzbank.ice.embargos.utils.EmbargosConstants.*;

@Service
@Transactional(transactionManager="transactionManager")
public class ChangeStatusFileControl {
	private static final Logger logger = LoggerFactory.getLogger(ChangeStatusFileControl.class);
	
	@Autowired
	private FileControlRepository fileControlRepository;
	
	@Autowired
	private GeneralParametersService generalParameterService;

	@Autowired
	private TaskService taskService;

	private static final List<Long> codTipoFichero = Arrays.asList(
			COD_TIPO_FICHERO_ENVIO_INFORMACION_NORMA63,
			COD_TIPO_FICHERO_TRABAS_NORMA63,
			COD_TIPO_FICHERO_TRABAS_AEAT,
			COD_TIPO_FICHERO_COM_RESULTADO_FINAL_NORMA63);

	@Async
	@Scheduled(fixedRate = 60000)
	public void changeStatusFileControl() {
		try {
			List<ControlFichero> list = fileControlRepository.findFicherosByTipoFicheroByCodEstado(EmbargosConstants.COD_ESTADO_CONTROL_FICHERO_GENERADO_SCHEDULED, codTipoFichero);
			
			String folderNameAEATGenerated = generalParameterService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_AEAT_OUTBOX);
			String folderNameN63Generated = generalParameterService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_OUTBOX);
			
			for (ControlFichero controlFichero : list) {
				File fileAEAT = new File(folderNameAEATGenerated, controlFichero.getNombreFichero());
				File fileN63 = new File(folderNameN63Generated, controlFichero.getNombreFichero());
				if (!fileAEAT.exists() && !fileN63.exists())
				{
					EstadoCtrlfichero estadoCtrlfichero = null;
					
					if (controlFichero.getTipoFichero().getCodTipoFichero() == COD_TIPO_FICHERO_TRABAS_AEAT)
					{
						estadoCtrlfichero = new EstadoCtrlfichero(
			                    EmbargosConstants.COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_AEAT_SENT,
			                    COD_TIPO_FICHERO_TRABAS_AEAT);

						if (controlFichero.getControlFicheroOrigen() != null && controlFichero.getControlFicheroOrigen().getCodTarea() != null)
							taskService.closeCalendarTask(controlFichero.getControlFicheroOrigen().getCodTarea().longValue(), controlFichero.getNombreFichero());
					}
					else if (controlFichero.getTipoFichero().getCodTipoFichero() == COD_TIPO_FICHERO_ENVIO_INFORMACION_NORMA63)
					{
						estadoCtrlfichero = new EstadoCtrlfichero(
			                    EmbargosConstants.COD_ESTADO_CTRLFICHERO_ENVIO_INFORMACION_NORMA63_SENT,
			                    COD_TIPO_FICHERO_ENVIO_INFORMACION_NORMA63);

						if (controlFichero.getControlFicheroOrigen() != null && controlFichero.getControlFicheroOrigen().getCodTarea() != null)
							taskService.closeCalendarTask(controlFichero.getControlFicheroOrigen().getCodTarea().longValue(), controlFichero.getNombreFichero());
					}
					else if (controlFichero.getTipoFichero().getCodTipoFichero() == COD_TIPO_FICHERO_TRABAS_NORMA63)
					{
						estadoCtrlfichero = new EstadoCtrlfichero(
			                    EmbargosConstants.COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_NORMA63_SENT,
			                    COD_TIPO_FICHERO_TRABAS_NORMA63);

						if (controlFichero.getControlFicheroOrigen() != null && controlFichero.getControlFicheroOrigen().getCodTarea() != null)
							taskService.closeCalendarTask(controlFichero.getControlFicheroOrigen().getCodTarea().longValue(), controlFichero.getNombreFichero());
					}
					else if (controlFichero.getTipoFichero().getCodTipoFichero() == EmbargosConstants.COD_TIPO_FICHERO_COM_RESULTADO_FINAL_NORMA63)
					{
						estadoCtrlfichero = new EstadoCtrlfichero(
			                    EmbargosConstants.COD_ESTADO_CTRLFICHERO_FINAL_ENVIADO,
			                    EmbargosConstants.COD_TIPO_FICHERO_COM_RESULTADO_FINAL_NORMA63);

						if (controlFichero.getControlFicheroOrigen() != null && controlFichero.getControlFicheroOrigen().getCodTarea() != null)
							taskService.closeCalendarTask(controlFichero.getControlFicheroOrigen().getCodTarea().longValue(), controlFichero.getNombreFichero());
					}

					if (estadoCtrlfichero != null)
					{
						logger.info("El fichero "+ controlFichero.getCodControlFichero() +"-"+ controlFichero.getNombreFichero() +" ha sido le??do de la carpeta outbox. Cambiando de estado a enviado");

						controlFichero.setUsuarioUltModificacion(EmbargosConstants.USER_AUTOMATICO);
						controlFichero.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
						controlFichero.setEstadoCtrlfichero(estadoCtrlfichero);
						fileControlRepository.save(controlFichero);
					}
				}
			}
		}
		catch (Exception e) {
			logger.error("ERROR - ChangeStatusFileControl - changeStatusFileControl() ", e);
		}
	}
}
