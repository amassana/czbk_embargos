package es.commerzbank.ice.embargos.formats.cuaderno63.common;

import es.commerzbank.ice.embargos.formats.cuaderno63.fase5.OrdenLevantamientoRetencionFase5;

import java.math.BigDecimal;

public class Levantamiento
    implements es.commerzbank.ice.embargos.formats.common.Levantamiento
{
    private OrdenLevantamientoRetencionFase5 orden;

    public Levantamiento(OrdenLevantamientoRetencionFase5 orden) {
        this.orden = orden;
    }

    @Override
    public String getNumeroEmbargo() {
        return orden.getIdentificadorDeuda();
    }

    @Override
    public BigDecimal getImporteTotalAEmbargar() {
        return orden.getImporteTotalAEmbargar();
    }

    @Override
    public BigDecimal getImporteTotalTrabado() {
        return orden.getImporteTotalRetencionesEfectuadas();
    }

    @Override
    public String getCC1() {
        return orden.getIbanCuenta1();
    }

    @Override
    public String getCC2() {
        return orden.getIbanCuenta2();
    }

    @Override
    public String getCC3() {
        return orden.getIbanCuenta3();
    }

    @Override
    public String getCC4() {
        return orden.getIbanCuenta4();
    }

    @Override
    public String getCC5() {
        return orden.getIbanCuenta5();
    }

    @Override
    public String getCC6() {
        return orden.getIbanCuenta6();
    }
}
