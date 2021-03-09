package es.commerzbank.ice.embargos.event;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.service.EmailService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;

@Service
@Transactional(transactionManager="transactionManager")
public class AlertStatusFileControl {
	private static final Logger logger = LoggerFactory.getLogger(AlertStatusFileControl.class);
	
	@Autowired
	private FileControlRepository fileControlRepository;
	
	@Autowired
	private GeneralParametersService generalParameterService;
	
	@Autowired
	private EmailService emailService;
	
	@Async
	@Scheduled(fixedRate = 3600000)
	public void alertStatusFileControl() {
		try {
			int diasEmailNoLecturaFicheros = generalParameterService.loadIntegerParameter(EmbargosConstants.PARAMETRO_EMBARGOS_DIAS_EMAIL_NOLECTURA_FICHEROS);
			List<ControlFichero> list = fileControlRepository.findByCodEstadoAndFecha(EmbargosConstants.COD_ESTADO_CONTROL_FICHERO_GENERADO_SCHEDULED, diasEmailNoLecturaFicheros);
			
			String folderNameAEATGenerated = generalParameterService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_AEAT_OUTBOX);
			String folderNameN63Generated = generalParameterService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_OUTBOX);
			
			logger.info("Número de control fichero encontrado sin leer de outbox: " + list.size());
			
			List<ControlFichero> listaEnvio = new ArrayList<ControlFichero>();
			
			for (ControlFichero controlFichero : list) {
				File fileAEAT = new File(folderNameAEATGenerated, controlFichero.getNombreFichero());
				File fileN63 = new File(folderNameN63Generated, controlFichero.getNombreFichero());
				if (fileAEAT.exists() || fileN63.exists())
				{
					logger.info("El fichero "+ controlFichero.getCodControlFichero() +" NO ha sido leído de la carpeta outbox.");
					listaEnvio.add(controlFichero);
				}
			}
			
			logger.info("Número de archivos encontrado sin leer de outbox: " + listaEnvio.size());
			
			emailService.sendEmailUnreadFiles(listaEnvio);
		}
		catch (Exception e) {
			logger.error("ERROR - AlertStatusFileControl - alertStatusFileControl() ", e);
		}
	}
}
