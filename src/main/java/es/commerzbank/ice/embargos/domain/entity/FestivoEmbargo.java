package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="FESTIVO")
@NamedQuery(name="FestivoEmbargo.findAll", query="SELECT f FROM FestivoEmbargo f")
public class FestivoEmbargo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="COD_FESTIVO", unique=true, nullable=false)
	private long codFestivo;
	
	@Column(name="F_FESTIVO", nullable=false)
	private long fechaFestivo;
	
	@Column(name="COD_LOCALIDAD", nullable=false)
	private long codLocalidad;
	
	public long getCodFestivo() {
		return codFestivo;
	}

	public void setCodFestivo(long codFestivo) {
		this.codFestivo = codFestivo;
	}

	public long getFechaFestivo() {
		return fechaFestivo;
	}

	public void setFechaFestivo(long fechaFestivo) {
		this.fechaFestivo = fechaFestivo;
	}

	public long getCodLocalidad() {
		return codLocalidad;
	}

	public void setCodLocalidad(long codLocalidad) {
		this.codLocalidad = codLocalidad;
	}


	@Override
	public String toString() {
		return "Festivo [codFestivo=" + codFestivo + ", fechaFestivo=" + getFechaFestivo() + ", codLocalidad=" + codLocalidad
				+ "]";
	}
	
}
