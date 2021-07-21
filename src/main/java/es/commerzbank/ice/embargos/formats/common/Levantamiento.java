package es.commerzbank.ice.embargos.formats.common;

import java.math.BigDecimal;

public interface Levantamiento {
    String getNumeroEmbargo();
    BigDecimal getImporteTotalAEmbargar();
    BigDecimal getImporteTotalTrabado();
    String getCC1();
    String getCC2();
    String getCC3();
    String getCC4();
    String getCC5();
    String getCC6();
}
