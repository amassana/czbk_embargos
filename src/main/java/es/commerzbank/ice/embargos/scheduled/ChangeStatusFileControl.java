package es.commerzbank.ice.embargos.scheduled;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.comun.lib.util.ValueConstants;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.utils.EmbargosConstants;

@Service
@Transactional(transactionManager="transactionManager")
public class ChangeStatusFileControl {
	private static final Logger logger = LoggerFactory.getLogger(ChangeStatusFileControl.class);
	
	@Autowired
	private FileControlRepository fileControlRepository;
	
	@Autowired
	private GeneralParametersService generalParameterService;
	
	@Async
	@Scheduled(fixedRate = 60000)
	public void changeStatusFileControl() {
		try {
			List<ControlFichero> list = fileControlRepository.findByCodEstado(EmbargosConstants.COD_ESTADO_CONTROL_FICHERO_GENERADO_SCHEDULED);
			
			String folderNameAEATGenerated = generalParameterService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_AEAT_GENERATED);
			String folderNameN63Generated = generalParameterService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_GENERATED);
			
			for (ControlFichero controlFichero : list) {
				File fileAEAT = new File(controlFichero.getRutaFichero());
				File fileN63 = new File(controlFichero.getRutaFichero());
				if (!fileAEAT.exists() || !fileN63.exists()) {
					if (controlFichero.getTipoFichero().getCodTipoFichero() == EmbargosConstants.COD_TIPO_FICHERO_TRABAS_AEAT) {
						fileControlRepository.updateCodEstado(controlFichero.getCodControlFichero(), EmbargosConstants.COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_AEAT_SENT);
					} else if (controlFichero.getTipoFichero().getCodTipoFichero() == EmbargosConstants.COD_TIPO_FICHERO_ENVIO_INFORMACION_NORMA63) {
						fileControlRepository.updateCodEstado(controlFichero.getCodControlFichero(), EmbargosConstants.COD_ESTADO_CTRLFICHERO_ENVIO_INFORMACION_NORMA63_SENT);
					} else if (controlFichero.getTipoFichero().getCodTipoFichero() == EmbargosConstants.COD_TIPO_FICHERO_TRABAS_NORMA63) {
						fileControlRepository.updateCodEstado(controlFichero.getCodControlFichero(), EmbargosConstants.COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_NORMA63_SENT);
					} else if (controlFichero.getTipoFichero().getCodTipoFichero() == EmbargosConstants.COD_TIPO_FICHERO_COM_RESULTADO_FINAL_NORMA63) {
						fileControlRepository.updateCodEstado(controlFichero.getCodControlFichero(), EmbargosConstants.COD_ESTADO_CTRLFICHERO_FINAL_ENVIADO);
					}
				}
			}
		} catch (ICEException e) {
			logger.error("ERROR - ChangeStatusFileControl - changeStatusFileControl() ", e);
		} catch (Exception e) {
			logger.error("ERROR - ChangeStatusFileControl - changeStatusFileControl() ", e);
		}
	}
}