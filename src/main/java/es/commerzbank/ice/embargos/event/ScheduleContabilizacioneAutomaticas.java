package es.commerzbank.ice.embargos.event;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.FicheroFinal;
import es.commerzbank.ice.embargos.service.AccountingService;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.FinalResponseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static es.commerzbank.ice.embargos.utils.EmbargosConstants.*;

@Component
public class ScheduleContabilizacioneAutomaticas
{
  private static final Logger logger = LoggerFactory.getLogger(ScheduleContabilizacioneAutomaticas.class);

  @Autowired
  private AccountingService accountingService;

  @Autowired
  private FileControlService fileControlService;

  @Autowired
  private FinalResponseService finalResponseService;

  private static final List<Long> codTipoFichero = Arrays.asList(
          COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_NORMA63,
          COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_AEAT);

  @Scheduled(cron = "*/60 * * * * ?")
  public void contabilizacionesAutomaticas() {
    List<ControlFichero> ficherosLevantamientoPendientesContabilizar = fileControlService.listByStatus(COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_PENDING_AUTOMATIC_ACCOUNTING, codTipoFichero);

    for (ControlFichero ficheroActual : ficherosLevantamientoPendientesContabilizar) {
      try {
        logger.info("Contabilizando el levantamiento "+ ficheroActual.getCodControlFichero() +" "+ ficheroActual.getNombreFichero());
        accountingService.levantamientoContabilizar(ficheroActual.getCodControlFichero(), USER_AUTOMATICO);
      } catch (Exception e) {
        logger.error("No se ha podido contabilizar el fichero de levantamiento "+ ficheroActual.getCodControlFichero() +". Se cambia el estado a Recibido", e);
        try {
          fileControlService.updateFileControlStatus(ficheroActual.getCodControlFichero(), COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_RECEIVED, USER_AUTOMATICO);
        }
        catch (Exception e2) {
          logger.error("Error cambiando de estado el fichero: "+ e2.getMessage());
        }
      }
    }

    List<FicheroFinal> ficherosFinalesPendientesContabilizar = finalResponseService.listPendingAccounting();

    for (FicheroFinal ficheroActual : ficherosFinalesPendientesContabilizar) {
      try {
        logger.info("Contabilizando el final de ciclo "+ ficheroActual.getControlFichero().getCodControlFichero() +" "+ ficheroActual.getControlFichero().getNombreFichero() +" importe "+ ficheroActual.getImporte());
        accountingService.ficheroFinalContabilizar(ficheroActual, USER_AUTOMATICO);
      } catch (Exception e) {
        logger.error("No se ha podido contabilizar el final de ciclo "+ ficheroActual.getControlFichero().getNombreFichero() +". Se cambia el estado a Contabilización manual abortada", e);
        try {
          finalResponseService.updateFinalFileAccountingStatus(ficheroActual, COD_ESTADO_CONTABILIZACION_CONTABILIZACION_AUTOMATICA_ABORTADA, USER_AUTOMATICO);
        }
        catch (Exception e2) {
          logger.error("Error cambiando de estado el fichero: "+ e2.getMessage());
        }
      }
    }
  }
}
