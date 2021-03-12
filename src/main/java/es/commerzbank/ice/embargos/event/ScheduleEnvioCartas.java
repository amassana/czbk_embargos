package es.commerzbank.ice.embargos.event;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.LiftingService;
import es.commerzbank.ice.embargos.service.SeizureService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleEnvioCartas
{
  private static final Logger logger = LoggerFactory.getLogger(ScheduleEnvioCartas.class);

  @Autowired
  private FileControlService fileControlService;

  @Autowired
  private SeizureService seizureService;

  @Autowired
  private LiftingService liftingService;

  @Scheduled(cron = "0 0/5 * * * ?")
  public void scheduleEnvioCartas()
  {
    try
    {
      List<ControlFichero> pendientes = fileControlService.cartasPendientesEnvio();

      for (ControlFichero pendiente : pendientes)
      {
        try {
          logger.info("Enviando cartas para control fichero " + pendiente.getCodControlFichero());

          if (pendiente.getTipoFichero().getCodTipoFichero() == EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_NORMA63 ||
                  pendiente.getTipoFichero().getCodTipoFichero() == EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_AEAT)
          {
            seizureService.generateSeizureLetters(pendiente);
          }
          else if (pendiente.getTipoFichero().getCodTipoFichero() == EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_NORMA63 ||
                  pendiente.getTipoFichero().getCodTipoFichero() == EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_AEAT)
          {
            liftingService.generateLiftingLetters(pendiente);
          }
          fileControlService.cartaEnviada(pendiente);
        }
        catch (Exception e) {
          logger.error("Error enviando las cartas del fichero "+ pendiente.getCodControlFichero(), e);
        }
      }
    } catch (Exception e) {
      logger.error("Error mientras se enviaban las cartas", e);
    }
  }
}
