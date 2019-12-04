package es.commerzbank.ice.embargos.scheduled;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.comun.lib.domain.entity.Festivo;
import es.commerzbank.ice.comun.lib.repository.FestiveRepo;
import es.commerzbank.ice.embargos.domain.entity.FestivoEmbargo;
import es.commerzbank.ice.embargos.domain.mapper.FestivoMapper;
import es.commerzbank.ice.embargos.repository.FestivoRepo;

@Service
@Transactional(transactionManager="transactionManager")
public class CopiaFestivosComunes {
	private static final Logger logger = LoggerFactory.getLogger(CopiaFestivosComunes.class);
	
	@Autowired
	private FestiveRepo festiveRepo;
	
	@Autowired
	private FestivoMapper festivoMapper;
	
	@Autowired
	private FestivoRepo festivoRepo;
		
	@Async
	@Scheduled(fixedRate = 3600000)
	public void copiaFestivosComunes() {
		try {
			List<Festivo> listaFestivos = festiveRepo.findAll();
			List<FestivoEmbargo> listaFestivosEmbargo = festivoMapper.toFestivoEmbargo(listaFestivos);
			festivoRepo.deleteAll();
			festivoRepo.saveAll(listaFestivosEmbargo);
			
		} catch (Exception e) {
			logger.error("ERROR - CopiaFestivosComunes - copiaFestivosComunes() ", e);
		}
	}
}
