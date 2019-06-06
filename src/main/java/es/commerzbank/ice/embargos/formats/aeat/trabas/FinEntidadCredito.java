package es.commerzbank.ice.embargos.formats.aeat.trabas;

import java.math.BigDecimal;

public class FinEntidadCredito {

	private String indicadorRegistro;
	private Integer delegacionAgenciaReceptora;
	private Integer codigoEntidadCredito;
	private Integer numeroDiligenciasIncluidasEnvio;
	private BigDecimal importeTotalAEmbargar;
	private BigDecimal importeTotalTrabado;
	private Integer codigoEntidadTransmisora;
	
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
	public Integer getNumeroDiligenciasIncluidasEnvio() {
		return numeroDiligenciasIncluidasEnvio;
	}
	public void setNumeroDiligenciasIncluidasEnvio(Integer numeroDiligenciasIncluidasEnvio) {
		this.numeroDiligenciasIncluidasEnvio = numeroDiligenciasIncluidasEnvio;
	}
	public BigDecimal getImporteTotalAEmbargar() {
		return importeTotalAEmbargar;
	}
	public void setImporteTotalAEmbargar(BigDecimal importeTotalAEmbargar) {
		this.importeTotalAEmbargar = importeTotalAEmbargar;
	}
	public BigDecimal getImporteTotalTrabado() {
		return importeTotalTrabado;
	}
	public void setImporteTotalTrabado(BigDecimal importeTotalTrabado) {
		this.importeTotalTrabado = importeTotalTrabado;
	}
	public Integer getCodigoEntidadTransmisora() {
		return codigoEntidadTransmisora;
	}
	public void setCodigoEntidadTransmisora(Integer codigoEntidadTransmisora) {
		this.codigoEntidadTransmisora = codigoEntidadTransmisora;
	}

	
}
