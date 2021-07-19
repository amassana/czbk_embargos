package es.commerzbank.ice.embargos.event;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.Peticion;
import es.commerzbank.ice.embargos.service.CGPJImportService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class JobImportCGPJ implements Job {

	private static final Logger logger = LoggerFactory.getLogger(JobImportCGPJ.class);

	@Autowired
	private CGPJImportService cgpjImportService;

	@Override
	@Transactional(transactionManager="transactionManager")
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			logger.debug("Inicio Import CGPJ");

			for (ControlFichero controlFichero : cgpjImportService.listPending()) {
				Peticion peticion = controlFichero.getPeticiones().get(0);
				logger.info("Importando CGPJ "+ peticion.getCodPeticion());
				cgpjImportService.importCGPJ(controlFichero, peticion);
				logger.info("Fin importación CGPJ "+ controlFichero.getCodControlFichero());
			}

			logger.debug("Fin Import");
		}
		catch (Exception e) {
			logger.error("ERROR en Import", e);
			JobExecutionException e2 = new JobExecutionException(e);
	        //fire it again
			//e2.setRefireImmediately(true);
	        throw e2;
		}
		catch (Throwable t) {
			logger.error("ERROR Throwable en Import", t);
			JobExecutionException e2 = new JobExecutionException(t);
	        //fire it again
			//e2.setRefireImmediately(true);
	        throw e2;
		}
	}



}
