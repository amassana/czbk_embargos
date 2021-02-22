package es.commerzbank.ice.embargos.formats.aeat.levantamientotrabas;

import java.util.Date;

public class EntidadCredito {

	private String indicadorRegistro;
	private String delegacionAgenciaReceptora;
	private String codigoEntidadCredito;
	private Integer numeroEnvio;
	private Date fechaCreacionFicheroLevantamientoTrabas;
	private String indicadorTipoFichero;
	private String codigoEntidadTransmisora;
	private String indicadorMoneda;
	
	public String getIndicadorRegistro() {
		return indicadorRegistro;
	}
	public void setIndicadorRegistro(String indicadorRegistro) {
		this.indicadorRegistro = indicadorRegistro;
	}
	public String getDelegacionAgenciaReceptora() {
		return delegacionAgenciaReceptora;
	}
	public void setDelegacionAgenciaReceptora(String delegacionAgenciaReceptora) {
		this.delegacionAgenciaReceptora = delegacionAgenciaReceptora;
	}
	public String getCodigoEntidadCredito() {
		return codigoEntidadCredito;
	}
	public void setCodigoEntidadCredito(String codigoEntidadCredito) {
		this.codigoEntidadCredito = codigoEntidadCredito;
	}
	public Integer getNumeroEnvio() {
		return numeroEnvio;
	}
	public void setNumeroEnvio(Integer numeroEnvio) {
		this.numeroEnvio = numeroEnvio;
	}
	public Date getFechaCreacionFicheroLevantamientoTrabas() {
		return fechaCreacionFicheroLevantamientoTrabas;
	}
	public void setFechaCreacionFicheroLevantamientoTrabas(Date fechaCreacionFicheroLevantamientoTrabas) {
		this.fechaCreacionFicheroLevantamientoTrabas = fechaCreacionFicheroLevantamientoTrabas;
	}
	public String getIndicadorTipoFichero() {
		return indicadorTipoFichero;
	}
	public void setIndicadorTipoFichero(String indicadorTipoFichero) {
		this.indicadorTipoFichero = indicadorTipoFichero;
	}
	public String getCodigoEntidadTransmisora() {
		return codigoEntidadTransmisora;
	}
	public void setCodigoEntidadTransmisora(String codigoEntidadTransmisora) {
		this.codigoEntidadTransmisora = codigoEntidadTransmisora;
	}
	public String getIndicadorMoneda() {
		return indicadorMoneda;
	}
	public void setIndicadorMoneda(String indicadorMoneda) {
		this.indicadorMoneda = indicadorMoneda;
	}
	
}
