package es.commerzbank.ice.embargos.domain.dto;

import java.math.BigDecimal;

public class AccountingPendingDTO {
    private String codPeticion;
    private String codSolicitud;
    private String NIF;
    private String nombre;
    private String iban;
    private String tipo;
    private BigDecimal importe;
    private BigDecimal cambio;
    private BigDecimal importeDivisa;
    private String divisa;

    public AccountingPendingDTO(String codPeticion, String codSolicitud, String NIF, String nombre, String iban, String tipo,
                                BigDecimal importe, BigDecimal cambio, String divisa) {
        this.codPeticion = codPeticion;
        this.codSolicitud = codSolicitud;
        this.NIF = NIF;
        this.nombre = nombre;
        this.iban = iban;
        this.tipo = tipo;
        this.importe = importe;
        this.cambio = cambio;
        this.importeDivisa = cambio != null && importe != null ? importe.multiply(cambio) : null;
        this.divisa = divisa;
    }

    public String getCodSolicitud() {
        return codSolicitud;
    }

    public void setCodSolicitud(String codSolicitud) {
        this.codSolicitud = codSolicitud;
    }

    public String getNIF() {
        return NIF;
    }

    public void setNIF(String NIF) {
        this.NIF = NIF;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public BigDecimal getCambio() {
        return cambio;
    }

    public void setCambio(BigDecimal cambio) {
        this.cambio = cambio;
    }

    public BigDecimal getImporteDivisa() {
        return importeDivisa;
    }

    public void setImporteDivisa(BigDecimal importeDivisa) {
        this.importeDivisa = importeDivisa;
    }

    public String getDivisa() {
        return divisa;
    }

    public void setDivisa(String divisa) {
        this.divisa = divisa;
    }

    public String getCodPeticion() {
        return codPeticion;
    }

    public void setCodPeticion(String codPeticion) {
        this.codPeticion = codPeticion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
