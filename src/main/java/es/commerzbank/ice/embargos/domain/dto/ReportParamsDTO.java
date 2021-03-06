package es.commerzbank.ice.embargos.domain.dto;

import java.util.Date;

public class ReportParamsDTO {

	private Date fechaInicio;
	private Date fechaFin;

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	@Override
	public String toString() {
		return "ReportParamsDTO [fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + "]";
	}
	
	

}