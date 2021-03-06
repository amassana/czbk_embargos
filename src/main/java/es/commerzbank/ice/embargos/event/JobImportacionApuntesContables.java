package es.commerzbank.ice.embargos.event;

import es.commerzbank.ice.comun.lib.domain.entity.ApunteContable;
import es.commerzbank.ice.comun.lib.service.AccountingNoteService;
import es.commerzbank.ice.embargos.service.AccountingService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class JobImportacionApuntesContables implements Job
{

	private static final Logger logger = LoggerFactory.getLogger(JobImportacionApuntesContables.class);

	@Autowired
	private AccountingNoteService accountingNoteService;

	@Autowired
	private AccountingService accountingService;
		
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			List<ApunteContable> unreadEntries = accountingNoteService.listUnreadEntriesAccounted(EmbargosConstants.ID_APLICACION_EMBARGOS);

			for (ApunteContable apunteContable : unreadEntries)
			{
				try
				{
					if (apunteContable.getExtraInfo1() != null) {
						logger.info("Detectado nuevo apunte contable " + apunteContable.getExtraInfo1() + "-" + apunteContable.getExtraInfo2());

						if (EmbargosConstants.APUNTES_CONTABLES_TIPO_TRABA.equals(apunteContable.getExtraInfo1()))
							accountingService.embargoCallback(Long.valueOf(apunteContable.getExtraInfo2()));
						else if (EmbargosConstants.APUNTES_CONTABLES_TIPO_EXTORNO.equals(apunteContable.getExtraInfo1()))
							accountingService.extornoCallback(Long.valueOf(apunteContable.getExtraInfo2()));
						else if (EmbargosConstants.APUNTES_CONTABLES_TIPO_LEVANTAMIENTO.equals(apunteContable.getExtraInfo1()))
							accountingService.levantamientoCallback((Long.valueOf(apunteContable.getExtraInfo2())));
						else if (EmbargosConstants.APUNTES_CONTABLES_TIPO_FINAL.equals(apunteContable.getExtraInfo1()))
							accountingService.ficheroFinalCallback((Long.valueOf(apunteContable.getExtraInfo2())));
						else
							logger.error("No se ha tratado el apunte contable");
					}
				}
				catch (Exception e)
				{
					logger.error("Error procesando un apunte contable", e);
				}

				accountingNoteService.markEntryAsRead(apunteContable);
			}
		}
		catch (Exception e) {
			logger.error("ERROR en public class JobImportacionApuntesContables con quartz", e);
			JobExecutionException e2 = new JobExecutionException(e);
	        //fire it again
			//e2.setRefireImmediately(true);
	        throw e2;
		}
		catch (Throwable t) {
			logger.error("ERROR Throwable en public class JobImportacionApuntesContables con quartz", t);
			JobExecutionException e2 = new JobExecutionException(t);
	        //fire it again
			//e2.setRefireImmediately(true);
	        throw e2;
		}
	}
}
