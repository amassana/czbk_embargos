package es.commerzbank.ice.embargos.event;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.service.files.Cuaderno63FinalResponseService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class JobNorma63FinalFile implements Job {

	private static final Logger logger = LoggerFactory.getLogger(JobNorma63FinalFile.class);
	
	// Este job genera los ficheros de F6 de Norma63 que estuvieran pendientes de generar

	@Autowired
	private Cuaderno63FinalResponseService cuaderno63FinalResponseService;

	@Autowired
	private FileControlRepository fileControlRepository;
	
	@Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			logger.debug("Inicio JobNorma63FinalFile con quartz");

			List<ControlFichero> pendientes = fileControlRepository.findFicherosF6FinCicloPending();

			for (ControlFichero controlFichero : pendientes) {
				logger.info("Generando el fichero "+ controlFichero.getCodControlFichero() +" "+ controlFichero.getNombreFichero());
				cuaderno63FinalResponseService.tramitarFicheroInformacion(controlFichero);
			}

			logger.debug("Fin JobNorma63FinalFile con quartz");
		}
		catch (Exception e) {
			logger.error("ERROR en JobNorma63FinalFile con quartz", e);
			JobExecutionException e2 = new JobExecutionException(e);
	        //fire it again
			//e2.setRefireImmediately(true);
	        throw e2;
		}
		catch (Throwable t) {
			logger.error("ERROR Throwable en JobNorma63FinalFile con quartz", t);
			JobExecutionException e2 = new JobExecutionException(t);
	        //fire it again
			//e2.setRefireImmediately(true);
	        throw e2;
		}
	}
}
