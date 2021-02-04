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
import es.commerzbank.ice.comun.lib.domain.entity.Tarea;
import es.commerzbank.ice.comun.lib.repository.FestiveRepo;
import es.commerzbank.ice.comun.lib.repository.TaskRepo;
import es.commerzbank.ice.comun.lib.util.ValueConstants;
import es.commerzbank.ice.embargos.domain.entity.FestivoEmbargo;
import es.commerzbank.ice.embargos.domain.entity.TareasPendiente;
import es.commerzbank.ice.embargos.domain.mapper.FestivoMapper;
import es.commerzbank.ice.embargos.domain.mapper.TareasPendienteMapper;
import es.commerzbank.ice.embargos.repository.FestivoRepo;
import es.commerzbank.ice.embargos.repository.TareasPendienteRepo;
import es.commerzbank.ice.utils.EmbargosConstants;

@Service
@Transactional(transactionManager="transactionManager")
public class CopiaEmbargosComunes {
	private static final Logger logger = LoggerFactory.getLogger(CopiaEmbargosComunes.class);
	
	@Autowired
	private FestiveRepo festiveRepo;
	
	@Autowired
	private FestivoMapper festivoMapper;
	
	@Autowired
	private FestivoRepo festivoRepo;
	
	@Autowired
	private TareasPendienteRepo tareasPendienteRepo;
	
	@Autowired
	private TareasPendienteMapper tareasPendienteMapper;
	
	@Autowired
	private TaskRepo taskRepo;

	// TODO
	// Rehabilitar cuando CGPJ esté reactivado
	@Async
	//@Scheduled(fixedRate = 3600000)
	public void copiaFestivosToEmbargos() {
		try {
			List<Festivo> listaFestivos = festiveRepo.findAll();
			List<FestivoEmbargo> listaFestivosEmbargo = festivoMapper.toFestivoEmbargo(listaFestivos);
			festivoRepo.deleteAll();
			festivoRepo.saveAll(listaFestivosEmbargo);
			
		} catch (Exception e) {
			logger.error("ERROR - CopiaEmbargosComunes - copiaFestivosToEmbargos() ", e);
		}
	}

	// TODO
	// Rehabilitar cuando CGPJ esté reactivado
	@Async
	//@Scheduled(fixedRate = 3600000)
	public void copiaTareasPendientesToComunes() {
		try {
			List<TareasPendiente> listaTareasPendientes = tareasPendienteRepo.findAll();
			List<Tarea> listaTareas = tareasPendienteMapper.toTarea(listaTareasPendientes);
			taskRepo.deleteByAccionEstado(EmbargosConstants.TAREA_PENDIENTE, ValueConstants.STATUS_TASK_PENDING);
			taskRepo.saveAll(listaTareas);
			
		} catch (Exception e) {
			logger.error("ERROR - CopiaEmbargosComunes - copiaTareasPendientesToComunes() ", e);
		}
	}
}
