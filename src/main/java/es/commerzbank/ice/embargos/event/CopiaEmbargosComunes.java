package es.commerzbank.ice.embargos.event;

import es.commerzbank.ice.comun.lib.domain.entity.Tarea;
import es.commerzbank.ice.comun.lib.repository.TaskRepo;
import es.commerzbank.ice.comun.lib.util.ValueConstants;
import es.commerzbank.ice.embargos.domain.entity.TareasPendiente;
import es.commerzbank.ice.embargos.domain.mapper.TareasPendienteMapper;
import es.commerzbank.ice.embargos.repository.TareasPendienteRepo;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(transactionManager="transactionManager")
public class CopiaEmbargosComunes {
	private static final Logger logger = LoggerFactory.getLogger(CopiaEmbargosComunes.class);

	@Autowired
	private TareasPendienteRepo tareasPendienteRepo;
	
	@Autowired
	private TareasPendienteMapper tareasPendienteMapper;
	
	@Autowired
	private TaskRepo taskRepo;

	// Rehabilitar / rediseñar si hace falta sincronizar las tareas del cgpj. Si no es necesario, dejar sin ejecutar y acabar borrando.
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
