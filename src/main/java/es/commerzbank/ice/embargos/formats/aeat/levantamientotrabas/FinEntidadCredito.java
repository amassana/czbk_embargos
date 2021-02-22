package es.commerzbank.ice.embargos.formats.aeat.levantamientotrabas;

import java.math.BigDecimal;

public class FinEntidadCredito {

	
	private String indicadorRegistro;
	private String delegacionAgenciaReceptora;
	private String codigoEntidadCredito;
	private Integer numeroDiligenciasIncluidasEnvio;
	private BigDecimal importeTotalAEmbargar;
	private BigDecimal importeTotalTrabado;
	private String codigoEntidadTransmisora;
	
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
	public String getCodigoEntidadTransmisora() {
		return codigoEntidadTransmisora;
	}
	public void setCodigoEntidadTransmisora(String codigoEntidadTransmisora) {
		this.codigoEntidadTransmisora = codigoEntidadTransmisora;
	}
		
	
}
