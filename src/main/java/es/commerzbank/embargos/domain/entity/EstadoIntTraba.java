package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ESTADO_INT_TRABA database table.
 * 
 */
@Entity
@Table(name="ESTADO_INT_TRABA")
@NamedQuery(name="EstadoIntTraba.findAll", query="SELECT e FROM EstadoIntTraba e")
public class EstadoIntTraba implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_ESTADO_INT_TRABA", unique=true, nullable=false)
	private long codEstadoIntTraba;

	@Column(name="DES_ESTADO_INT_TRABA", length=60)
	private String desEstadoIntTraba;

	//bi-directional many-to-one association to SolicitudesTraba
	@OneToMany(mappedBy="estadoIntTraba")
	private List<SolicitudesTraba> solicitudesTrabas;

	public EstadoIntTraba() {
	}

	public long getCodEstadoIntTraba() {
		return this.codEstadoIntTraba;
	}

	public void setCodEstadoIntTraba(long codEstadoIntTraba) {
		this.codEstadoIntTraba = codEstadoIntTraba;
	}

	public String getDesEstadoIntTraba() {
		return this.desEstadoIntTraba;
	}

	public void setDesEstadoIntTraba(String desEstadoIntTraba) {
		this.desEstadoIntTraba = desEstadoIntTraba;
	}

	public List<SolicitudesTraba> getSolicitudesTrabas() {
		return this.solicitudesTrabas;
	}

	public void setSolicitudesTrabas(List<SolicitudesTraba> solicitudesTrabas) {
		this.solicitudesTrabas = solicitudesTrabas;
	}

	public SolicitudesTraba addSolicitudesTraba(SolicitudesTraba solicitudesTraba) {
		getSolicitudesTrabas().add(solicitudesTraba);
		solicitudesTraba.setEstadoIntTraba(this);

		return solicitudesTraba;
	}

	public SolicitudesTraba removeSolicitudesTraba(SolicitudesTraba solicitudesTraba) {
		getSolicitudesTrabas().remove(solicitudesTraba);
		solicitudesTraba.setEstadoIntTraba(null);

		return solicitudesTraba;
	}

}