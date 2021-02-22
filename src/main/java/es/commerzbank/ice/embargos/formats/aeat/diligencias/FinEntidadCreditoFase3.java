package es.commerzbank.ice.embargos.formats.aeat.diligencias;

import java.math.BigDecimal;

public class FinEntidadCreditoFase3 {

	private String indicadorRegistro;
	private String delegacionAgenciaEmisora;
	private String codigoEntidadCredito;
	private Integer numeroDiligenciasIncluidasEnvio;
	private BigDecimal importeTotalAEmbargar;
	private String codigoEntidadTransmisora;
	
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
	public String getCodigoEntidadTransmisora() {
		return codigoEntidadTransmisora;
	}
	public void setCodigoEntidadTransmisora(String codigoEntidadTransmisora) {
		this.codigoEntidadTransmisora = codigoEntidadTransmisora;
	}
		
}
