package es.commerzbank.ice.embargos.formats.aeat.diligencias;

import java.util.Date;

public class FinEntidadTransmisoraFase3 {

	private String indicadorRegistro;
	private String codigoEntidadTransmisora;
	private Date fechaCreacionFicheroTransmision;
	
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
	public Date getFechaCreacionFicheroTransmision() {
		return fechaCreacionFicheroTransmision;
	}
	public void setFechaCreacionFicheroTransmision(Date fechaCreacionFicheroTransmision) {
		this.fechaCreacionFicheroTransmision = fechaCreacionFicheroTransmision;
	}
	
	
	
}
