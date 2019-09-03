package es.commerzbank.ice.embargos.formats.aeat.diligencias;

import java.util.Date;

public class EntidadTransmisora {

	private String indicadorRegistro;
	private String delegacionAgenciaEmisora;
	private String codigoEntidadTransmisora;
	private Date fechaInicioCiclo;
	private Date fechaCreacionFicheroTransmision;
	private String indicadorTipoFichero;
	
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
