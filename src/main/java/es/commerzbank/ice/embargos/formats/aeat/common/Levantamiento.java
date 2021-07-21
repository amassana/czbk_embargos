package es.commerzbank.ice.embargos.formats.aeat.common;

import es.commerzbank.ice.embargos.formats.aeat.validaciontrabas.ErroresTrabaValidacionFase4;

import java.math.BigDecimal;

public class Levantamiento
        implements es.commerzbank.ice.embargos.formats.common.Levantamiento
{
    private es.commerzbank.ice.embargos.formats.aeat.levantamientotrabas.Levantamiento levantamiento;
    private ErroresTrabaValidacionFase4 erroresLevantamiento;

    public Levantamiento(es.commerzbank.ice.embargos.formats.aeat.levantamientotrabas.Levantamiento levantamiento) {
        this.levantamiento = levantamiento;
    }

    public Levantamiento (ErroresTrabaValidacionFase4 erroresLevantamiento) {
        this.erroresLevantamiento = erroresLevantamiento;
    }


    @Override
    public String getNumeroEmbargo() {
        return levantamiento == null ? erroresLevantamiento.getNumeroDiligenciaEmbargo() : levantamiento.getNumeroDiligenciaEmbargo();
    }

    @Override
    public BigDecimal getImporteTotalAEmbargar() {
        return levantamiento == null ? erroresLevantamiento.getImporteTotalAEmbargar() : levantamiento.getImporteTotalAEmbargar();
    }

    @Override
    public BigDecimal getImporteTotalTrabado() {
        return levantamiento == null ? erroresLevantamiento.getImporteTotalTrabado() : levantamiento.getImporteTotalTrabado();
    }

    @Override
    public String getCC1() {
        return levantamiento == null ? erroresLevantamiento.getCodigoCuentaCliente1() : levantamiento.getCodigoCuentaCliente1();
    }

    @Override
    public String getCC2() {
        return levantamiento == null ? erroresLevantamiento.getCodigoCuentaCliente2() : levantamiento.getCodigoCuentaCliente2();
    }

    @Override
    public String getCC3() {
        return levantamiento == null ? erroresLevantamiento.getCodigoCuentaCliente3() : levantamiento.getCodigoCuentaCliente3();
    }

    @Override
    public String getCC4() {
        return levantamiento == null ? erroresLevantamiento.getCodigoCuentaCliente4() : levantamiento.getCodigoCuentaCliente4();
    }

    @Override
    public String getCC5() {
        return levantamiento == null ? erroresLevantamiento.getCodigoCuentaCliente5() : levantamiento.getCodigoCuentaCliente5();
    }

    @Override
    public String getCC6() {
        return levantamiento == null ? erroresLevantamiento.getCodigoCuentaCliente6() : levantamiento.getCodigoCuentaCliente6();
    }
}
