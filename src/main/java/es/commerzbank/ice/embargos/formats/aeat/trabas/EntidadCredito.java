package es.commerzbank.ice.embargos.formats.aeat.trabas;

import java.util.Date;

public class EntidadCredito {

	private String indicadorRegistro;
	private Integer delegacionAgenciaReceptora;
	private Integer codigoEntidadCredito;
	private Integer numeroEnvio;
	private Date fechaCreacionFicheroTrabas;
	private String indicadorTipoFichero;
	private Integer codigoEntidadTransmisora;
	private String indicadorMoneda;
	
	
	public String getIndicadorRegistro() {
		return indicadorRegistro;
	}
	public void setIndicadorRegistro(String indicadorRegistro) {
		this.indicadorRegistro = indicadorRegistro;
	}
	public Integer getDelegacionAgenciaReceptora() {
		return delegacionAgenciaReceptora;
	}
	public void setDelegacionAgenciaReceptora(Integer delegacionAgenciaReceptora) {
		this.delegacionAgenciaReceptora = delegacionAgenciaReceptora;
	}
	public Integer getCodigoEntidadCredito() {
		return codigoEntidadCredito;
	}
	public void setCodigoEntidadCredito(Integer codigoEntidadCredito) {
		this.codigoEntidadCredito = codigoEntidadCredito;
	}
	public Integer getNumeroEnvio() {
		return numeroEnvio;
	}
	public void setNumeroEnvio(Integer numeroEnvio) {
		this.numeroEnvio = numeroEnvio;
	}
	public Date getFechaCreacionFicheroTrabas() {
		return fechaCreacionFicheroTrabas;
	}
	public void setFechaCreacionFicheroTrabas(Date fechaCreacionFicheroTrabas) {
		this.fechaCreacionFicheroTrabas = fechaCreacionFicheroTrabas;
	}
	public String getIndicadorTipoFichero() {
		return indicadorTipoFichero;
	}
	public void setIndicadorTipoFichero(String indicadorTipoFichero) {
		this.indicadorTipoFichero = indicadorTipoFichero;
	}
	public Integer getCodigoEntidadTransmisora() {
		return codigoEntidadTransmisora;
	}
	public void setCodigoEntidadTransmisora(Integer codigoEntidadTransmisora) {
		this.codigoEntidadTransmisora = codigoEntidadTransmisora;
	}
	public String getIndicadorMoneda() {
		return indicadorMoneda;
	}
	public void setIndicadorMoneda(String indicadorMoneda) {
		this.indicadorMoneda = indicadorMoneda;
	}
	

	
}
