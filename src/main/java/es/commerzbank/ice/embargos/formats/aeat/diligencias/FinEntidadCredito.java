package es.commerzbank.ice.embargos.formats.aeat.diligencias;

import java.math.BigDecimal;

public class FinEntidadCredito {

	private String indicadorRegistro;
	private Integer delegacionAgenciaEmisora;
	private Integer codigoEntidadCredito;
	private Integer numeroDiligenciasIncluidasEnvio;
	private BigDecimal importeTotalAEmbargar;
	private Integer codigoEntidadTransmisora;
	
	public String getIndicadorRegistro() {
		return indicadorRegistro;
	}
	public void setIndicadorRegistro(String indicadorRegistro) {
		this.indicadorRegistro = indicadorRegistro;
	}
	public Integer getDelegacionAgenciaEmisora() {
		return delegacionAgenciaEmisora;
	}
	public void setDelegacionAgenciaEmisora(Integer delegacionAgenciaEmisora) {
		this.delegacionAgenciaEmisora = delegacionAgenciaEmisora;
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
	public Integer getCodigoEntidadTransmisora() {
		return codigoEntidadTransmisora;
	}
	public void setCodigoEntidadTransmisora(Integer codigoEntidadTransmisora) {
		this.codigoEntidadTransmisora = codigoEntidadTransmisora;
	}
		
}
