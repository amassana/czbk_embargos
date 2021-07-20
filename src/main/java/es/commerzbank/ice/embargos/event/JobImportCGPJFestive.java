package es.commerzbank.ice.embargos.event;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import es.commerzbank.ice.comun.lib.domain.dto.Festive;
import es.commerzbank.ice.comun.lib.domain.dto.FestiveFilter;
import es.commerzbank.ice.comun.lib.service.FestiveService;
import es.commerzbank.ice.embargos.domain.entity.FestivoEmbargo;
import es.commerzbank.ice.embargos.domain.mapper.FestivoMapper;
import es.commerzbank.ice.embargos.repository.FestivoRepo;

public class JobImportCGPJFestive implements Job {
	
	private static final Logger logger = LoggerFactory.getLogger(JobImportCGPJFestive.class);
	
	@Autowired
	private FestivoMapper festivoMapper;
	
	@Autowired
	private FestivoRepo festivoRepo;
	
	@Autowired
	private FestiveService festiveService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
			
			FestiveFilter filter = new FestiveFilter();
			filter.setFestiveDateEnd(formatter.format(LocalDate.of(LocalDate.now().getYear(),12,31)));
			filter.setFestiveDateStart(formatter.format(LocalDate.of(LocalDate.now().getYear(),1,1)));
			List<Festive> listaFestivos = festiveService.listAllFestive(filter);
			List<FestivoEmbargo> listaFestivosEmbargo = festivoMapper.toFestivoEmbargo(listaFestivos);
			festivoRepo.deleteAllYear(LocalDate.of(LocalDate.now().getYear(),1,1));
			festivoRepo.saveAll(listaFestivosEmbargo);

		} catch (Exception e) {
			logger.error("ERROR - CopiaEmbargosComunes - copiaFestivosToEmbargos() ", e);
		}
	}
}
