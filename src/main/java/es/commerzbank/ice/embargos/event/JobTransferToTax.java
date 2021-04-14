package es.commerzbank.ice.embargos.event;

import es.commerzbank.ice.comun.lib.domain.dto.User;
import es.commerzbank.ice.comun.lib.domain.entity.Usuario;
import es.commerzbank.ice.comun.lib.repository.UsuarioRepo;
import es.commerzbank.ice.comun.lib.security.Token;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.service.mapper.UserMapper;
import es.commerzbank.ice.embargos.service.FinalResponseService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class JobTransferToTax implements Job {

	private static final Logger logger = LoggerFactory.getLogger(JobTransferToTax.class);
	
	// Este job obtiene todos los embargos cuyo código empiece por AEAT, con fecha de vencimiento igual a la fecha actual 
	// y genera un impuesto para cada uno de ellos.
	
	@Autowired
	private GeneralParametersService generalParametersService;
	
	@Autowired
	private FinalResponseService finalResponseService;
	
	@Autowired
    private UsuarioRepo usersRepository;
	
	@Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			logger.debug("Inicio JobTransferToTax con quartz");
			
			User user = null;
			String token = null;
			
			String userStr = generalParametersService.loadStringParameter(EmbargosConstants.JOB_EMBARGOS_TO_TAX_USER);
			Usuario usuario = usersRepository.findByNombreUsuario(userStr);
			if (usuario!=null) user = UserMapper.toUser(usuario);
			if (user!=null) token = Token.buildJWToken(user, user.getPerfil());
			
			if (token!=null) finalResponseService.jobTransferToTax(token, userStr);
			else logger.error("JobTransferToTax - No se ha podido generar el token para el usuario " + userStr);

			logger.debug("Fin JobTransferToTax con quartz");
		}
		catch (Exception e) {
			logger.error("ERROR en JobTransferToTax con quartz", e);
			JobExecutionException e2 = new JobExecutionException(e);
	        //fire it again
			//e2.setRefireImmediately(true);
	        throw e2;
		}
		catch (Throwable t) {
			logger.error("ERROR Throwable en JobTransferToTax con quartz", t);
			JobExecutionException e2 = new JobExecutionException(t);
	        //fire it again
			//e2.setRefireImmediately(true);
	        throw e2;
		}
	}
}
