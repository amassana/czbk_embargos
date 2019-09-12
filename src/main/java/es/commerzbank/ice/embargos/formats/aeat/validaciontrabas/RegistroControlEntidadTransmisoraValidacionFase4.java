package es.commerzbank.ice.embargos.formats.aeat.validaciontrabas;

import java.util.Date;

public class RegistroControlEntidadTransmisoraValidacionFase4 {

	private String indicadorRegistro;
	private String delegacionAgenciaEmisora;
	private String codigoEntidadTransmisora;
	private Date fechaInicioCiclo;
	private Integer numeroEntidadesCreditoIncluidas;
	private String indicadorEstadoDiligencias;
	private Integer numeroEntidadesCreditoTransmitidoTrabas;
	private Integer numeroEntidadesCreditoEnviosRechazados;
	private Integer numeroEntidadesCreditoEnviosAceptados;
	private String estadoGeneralEntidadTransmisora;
	private Date fechaAltaRegistro;
	private String horaAltaRegistro;
	
	public String getIndicadorRegistro() {
		return indicadorRegistro;
	}
	public void setIndicadorRegistro(String indicadorRegistro) {
		this.indicadorRegistro = indicadorRegistro;
	}
	public String getDelegacionAgenciaEmisora() {
		return delegacionAgenciaEmisora;
	}
	public void setDelegacionAgenciaEmisora(String delegacionAgenciaEmisora) {
		this.delegacionAgenciaEmisora = delegacionAgenciaEmisora;
	}
	public String getCodigoEntidadTransmisora() {
		return codigoEntidadTransmisora;
	}
	public void setCodigoEntidadTransmisora(String codigoEntidadTransmisora) {
		this.codigoEntidadTransmisora = codigoEntidadTransmisora;
	}
	public Date getFechaInicioCiclo() {
		return fechaInicioCiclo;
	}
	public void setFechaInicioCiclo(Date fechaInicioCiclo) {
		this.fechaInicioCiclo = fechaInicioCiclo;
	}
	public Integer getNumeroEntidadesCreditoIncluidas() {
		return numeroEntidadesCreditoIncluidas;
	}
	public void setNumeroEntidadesCreditoIncluidas(Integer numeroEntidadesCreditoIncluidas) {
		this.numeroEntidadesCreditoIncluidas = numeroEntidadesCreditoIncluidas;
	}
	public String getIndicadorEstadoDiligencias() {
		return indicadorEstadoDiligencias;
	}
	public void setIndicadorEstadoDiligencias(String indicadorEstadoDiligencias) {
		this.indicadorEstadoDiligencias = indicadorEstadoDiligencias;
	}
	public Integer getNumeroEntidadesCreditoTransmitidoTrabas() {
		return numeroEntidadesCreditoTransmitidoTrabas;
	}
	public void setNumeroEntidadesCreditoTransmitidoTrabas(Integer numeroEntidadesCreditoTransmitidoTrabas) {
		this.numeroEntidadesCreditoTransmitidoTrabas = numeroEntidadesCreditoTransmitidoTrabas;
	}
	public Integer getNumeroEntidadesCreditoEnviosRechazados() {
		return numeroEntidadesCreditoEnviosRechazados;
	}
	public void setNumeroEntidadesCreditoEnviosRechazados(Integer numeroEntidadesCreditoEnviosRechazados) {
		this.numeroEntidadesCreditoEnviosRechazados = numeroEntidadesCreditoEnviosRechazados;
	}
	public Integer getNumeroEntidadesCreditoEnviosAceptados() {
		return numeroEntidadesCreditoEnviosAceptados;
	}
	public void setNumeroEntidadesCreditoEnviosAceptados(Integer numeroEntidadesCreditoEnviosAceptados) {
		this.numeroEntidadesCreditoEnviosAceptados = numeroEntidadesCreditoEnviosAceptados;
	}
	public String getEstadoGeneralEntidadTransmisora() {
		return estadoGeneralEntidadTransmisora;
	}
	public void setEstadoGeneralEntidadTransmisora(String estadoGeneralEntidadTransmisora) {
		this.estadoGeneralEntidadTransmisora = estadoGeneralEntidadTransmisora;
	}
	public Date getFechaAltaRegistro() {
		return fechaAltaRegistro;
	}
	public void setFechaAltaRegistro(Date fechaAltaRegistro) {
		this.fechaAltaRegistro = fechaAltaRegistro;
	}
	public String getHoraAltaRegistro() {
		return horaAltaRegistro;
	}
	public void setHoraAltaRegistro(String horaAltaRegistro) {
		this.horaAltaRegistro = horaAltaRegistro;
	}
	
}
