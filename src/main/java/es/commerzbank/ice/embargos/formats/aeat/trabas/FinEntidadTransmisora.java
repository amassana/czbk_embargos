package es.commerzbank.ice.embargos.formats.aeat.trabas;

import java.util.Date;

public class FinEntidadTransmisora {

	private String indicadorRegistro;
	private String codigoEntidadTransmisora;
	private Date fechaCreacionFicheroTrabas;
	
	
	public String getIndicadorRegistro() {
		return indicadorRegistro;
	}
	public void setIndicadorRegistro(String indicadorRegistro) {
		this.indicadorRegistro = indicadorRegistro;
	}
	public String getCodigoEntidadTransmisora() {
		return codigoEntidadTransmisora;
	}
	public void setCodigoEntidadTransmisora(String codigoEntidadTransmisora) {
		this.codigoEntidadTransmisora = codigoEntidadTransmisora;
	}
	public Date getFechaCreacionFicheroTrabas() {
		return fechaCreacionFicheroTrabas;
	}
	public void setFechaCreacionFicheroTrabas(Date fechaCreacionFicheroTrabas) {
		this.fechaCreacionFicheroTrabas = fechaCreacionFicheroTrabas;
	}
	
}
