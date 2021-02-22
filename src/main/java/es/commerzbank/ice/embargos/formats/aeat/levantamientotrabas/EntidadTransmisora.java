package es.commerzbank.ice.embargos.formats.aeat.levantamientotrabas;

import java.util.Date;

public class EntidadTransmisora {

	private String indicadorRegistro;
	private String delegacionAgenciaReceptora;
	private String codigoEntidadTransmisora;
	private Date fechaInicioCiclo;
	private Date fechaCreacionFicheroTrabas;
	private String indicadorTipoFichero;

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
	
}
