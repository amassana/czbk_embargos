package es.commerzbank.ice.embargos.formats.aeat.levantamientotrabas;

import java.util.Date;

public class RegistroControlEntidadTransmisora {
	
	private String indicadorRegistro;
	private String delegacionAgenciaReceptora;
	private String codigoEntidadTransmisora;
	private Date fechaInicioCiclo;
	private Integer numeroEntidadesCreditoIncluidas;
	private String indicadorEstadoLevantamientos;
	private Date fechaAltaRegistro;
	private String horaAltaRegistro;
	
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
	public String getIndicadorEstadoLevantamientos() {
		return indicadorEstadoLevantamientos;
	}
	public void setIndicadorEstadoLevantamientos(String indicadorEstadoLevantamientos) {
		this.indicadorEstadoLevantamientos = indicadorEstadoLevantamientos;
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
