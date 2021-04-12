package es.commerzbank.ice.embargos.event;

import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.commerzbank.ice.comun.lib.domain.entity.Tarea;
import es.commerzbank.ice.comun.lib.repository.TaskRepo;
import es.commerzbank.ice.comun.lib.service.ClientEmailService;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;

@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class JobTaskPendingDate implements Job {

	private static final Logger logger = LoggerFactory.getLogger(JobTaskPendingDate.class);
	
	@Autowired
	private GeneralParametersService generalParametersService;
	
	@Autowired
	private TaskRepo taskRepo; 
	
	@Autowired
	private ClientEmailService clientEmailService;
	
	@Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			logger.debug("Inicio JobTaskPendingDate con quartz");
			
			int dias = generalParametersService.loadIntegerParameter(EmbargosConstants.PARAMETRO_EMBARGOS_DIAS_TAREAS_PENDIENTES);
			List<Tarea> listTareas = taskRepo.getTaskPendingByApplicationAndDate(EmbargosConstants.ID_APLICACION_EMBARGOS, dias);
			if (listTareas!=null && listTareas.size()>0) {
				String emailAddressesTo = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_EMAIL_TO);
				String emailAddressFrom = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_EMAIL_FROM);
				
				clientEmailService.sendEmailExpirationDateTask(listTareas, emailAddressesTo, emailAddressFrom, EmbargosConstants.EMAIL_DEFAULT_FOOTER_TEXT);
			}
			
			logger.debug("Fin JobTaskPendingDate con quartz");
		}
		catch (Exception e) {
			logger.error("ERROR en JobTaskPendingDate con quartz", e);
			JobExecutionException e2 = new JobExecutionException(e);
	        //fire it again
			//e2.setRefireImmediately(true);
	        throw e2;
		}
		catch (Throwable t) {
			logger.error("ERROR Throwable en JobTaskPendingDate con quartz", t);
			JobExecutionException e2 = new JobExecutionException(t);
	        //fire it again
			//e2.setRefireImmediately(true);
	        throw e2;
		}
	}
}
