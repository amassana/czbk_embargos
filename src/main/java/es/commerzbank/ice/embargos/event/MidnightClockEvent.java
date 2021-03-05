package es.commerzbank.ice.embargos.event;

import es.commerzbank.ice.comun.lib.util.GitUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MidnightClockEvent {
  @Scheduled(cron = "0 0 0 * * ?")
  public void midnight() {
    GitUtils.printGit();
  }
}
