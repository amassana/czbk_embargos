package es.commerzbank.ice.embargos.event;

import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.embargos.domain.dto.CGPJFiltersDTO;
import es.commerzbank.ice.embargos.domain.dto.CGPJPetitionDTO;
import es.commerzbank.ice.embargos.service.CGPJService;
import es.commerzbank.ice.embargos.service.impl.EmailServiceImpl;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class JobCGPJPending implements Job {

	private static final Logger logger = LoggerFactory.getLogger(JobCGPJPending.class);
	
	@Autowired
	private GeneralParametersService generalParametersService;

	@Autowired
	private CGPJService cgpjService;
	
	@Autowired
	private EmailServiceImpl emailServiceImpl;
	
	@Override
    public void execute(JobExecutionContext arg0)
		throws JobExecutionException
	{
		try {
			logger.debug("Inicio JobCGPJPending con quartz");

			long horas = generalParametersService.loadLongParameter(EmbargosConstants.PARAMETRO_CGPJ_EMAIL_PENDIENTE_HORAS, 2L);

			CGPJFiltersDTO filters = new CGPJFiltersDTO();
			filters.setFileLoadTimestampMax(Instant.now().minus(horas, ChronoUnit.HOURS));
			filters.setStatuses(new Long[]{EmbargosConstants.CGPJ_ESTADO_INTERNO_INICIAL, EmbargosConstants.CGPJ_ESTADO_INTERNO_PROCESADO});

			Page<CGPJPetitionDTO> result = cgpjService.filter(filters, PageRequest.of(0, 100));

			if (result.getNumberOfElements() > 0) {
				String emailAddressesTo = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_EMAIL_TO);
				String emailAddressFrom = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_EMAIL_FROM);
				
				emailServiceImpl.sendCGPJPending(result.getContent(), emailAddressesTo, emailAddressFrom, EmbargosConstants.EMAIL_DEFAULT_FOOTER_TEXT);
			}
			
			logger.debug("Fin JobCGPJPending con quartz");
		}
		catch (Exception e) {
			logger.error("ERROR en JobCGPJPending con quartz", e);
			JobExecutionException e2 = new JobExecutionException(e);
	        //fire it again
			//e2.setRefireImmediately(true);
	        throw e2;
		}
		catch (Throwable t) {
			logger.error("ERROR Throwable en JobCGPJPending con quartz", t);
			JobExecutionException e2 = new JobExecutionException(t);
	        //fire it again
			//e2.setRefireImmediately(true);
	        throw e2;
		}
	}
}
