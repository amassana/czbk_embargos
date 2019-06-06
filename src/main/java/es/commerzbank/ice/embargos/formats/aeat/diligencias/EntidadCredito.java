package es.commerzbank.ice.embargos.formats.aeat.diligencias;

import java.util.Date;

public class EntidadCredito {

	private String indicadorRegistro;
	private Integer delegacionAgenciaEmisora;
	private Integer codigoEntidadCredito;
	private Integer numeroEnvio;
	private Date fechaCreacionFicheroTransmision;
	private String indicadorTipoFichero;
	private Date fechaGeneracionEnvioMensualDiligencias;
	private Integer codigoEntidadTransmisora;
	private String indicadorMoneda;
	private String mensajeInformativoParaDeudor;
	
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
	public Integer getNumeroEnvio() {
		return numeroEnvio;
	}
	public void setNumeroEnvio(Integer numeroEnvio) {
		this.numeroEnvio = numeroEnvio;
	}
	public Date getFechaCreacionFicheroTransmision() {
		return fechaCreacionFicheroTransmision;
	}
	public void setFechaCreacionFicheroTransmision(Date fechaCreacionFicheroTransmision) {
		this.fechaCreacionFicheroTransmision = fechaCreacionFicheroTransmision;
	}
	public String getIndicadorTipoFichero() {
		return indicadorTipoFichero;
	}
	public void setIndicadorTipoFichero(String indicadorTipoFichero) {
		this.indicadorTipoFichero = indicadorTipoFichero;
	}
	public Date getFechaGeneracionEnvioMensualDiligencias() {
		return fechaGeneracionEnvioMensualDiligencias;
	}
	public void setFechaGeneracionEnvioMensualDiligencias(Date fechaGeneracionEnvioMensualDiligencias) {
		this.fechaGeneracionEnvioMensualDiligencias = fechaGeneracionEnvioMensualDiligencias;
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
	public String getMensajeInformativoParaDeudor() {
		return mensajeInformativoParaDeudor;
	}
	public void setMensajeInformativoParaDeudor(String mensajeInformativoParaDeudor) {
		this.mensajeInformativoParaDeudor = mensajeInformativoParaDeudor;
	}
	

	
}
