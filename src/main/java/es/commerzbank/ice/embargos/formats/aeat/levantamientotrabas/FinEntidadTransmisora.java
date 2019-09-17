package es.commerzbank.ice.embargos.formats.aeat.levantamientotrabas;

import java.util.Date;

public class FinEntidadTransmisora {

	private String indicadorRegistro;
	private String codigoEntidadTransmisora;
	private Date fechaCreacionFicheroLevantamientoTrabas;
	
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
	public Date getFechaCreacionFicheroLevantamientoTrabas() {
		return fechaCreacionFicheroLevantamientoTrabas;
	}
	public void setFechaCreacionFicheroLevantamientoTrabas(Date fechaCreacionFicheroLevantamientoTrabas) {
		this.fechaCreacionFicheroLevantamientoTrabas = fechaCreacionFicheroLevantamientoTrabas;
	}
	
	
}
