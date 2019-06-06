package es.commerzbank.ice.embargos.formats.aeat.diligencias;

import java.util.Date;

public class EntidadTransmisora {

	private String indicadorRegistro;
	private Integer delegacionAgenciaEmisora;
	private Integer codigoEntidadTransmisora;
	private Date fechaInicioCiclo;
	private Date fechaCreacionFicheroTransmision;
	private String indicadorTipoFichero;
	
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
	public Integer getCodigoEntidadTransmisora() {
		return codigoEntidadTransmisora;
	}
	public void setCodigoEntidadTransmisora(Integer codigoEntidadTransmisora) {
		this.codigoEntidadTransmisora = codigoEntidadTransmisora;
	}
	public Date getFechaInicioCiclo() {
		return fechaInicioCiclo;
	}
	public void setFechaInicioCiclo(Date fechaInicioCiclo) {
		this.fechaInicioCiclo = fechaInicioCiclo;
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
	
	

	
	
}
