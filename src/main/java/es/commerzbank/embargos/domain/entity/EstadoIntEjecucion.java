package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ESTADO_INT_EJECUCION database table.
 * 
 */
@Entity
@Table(name="ESTADO_INT_EJECUCION")
@NamedQuery(name="EstadoIntEjecucion.findAll", query="SELECT e FROM EstadoIntEjecucion e")
public class EstadoIntEjecucion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_ESTADO_INT_EJECUCION", unique=true, nullable=false)
	private long codEstadoIntEjecucion;

	@Column(name="DES_ESTADO_INT_EJECUCION", length=60)
	private String desEstadoIntEjecucion;

	//bi-directional many-to-one association to SolicitudesEjecucion
	@OneToMany(mappedBy="estadoIntEjecucion")
	private List<SolicitudesEjecucion> solicitudesEjecucions;

	public EstadoIntEjecucion() {
	}

	public long getCodEstadoIntEjecucion() {
		return this.codEstadoIntEjecucion;
	}

	public void setCodEstadoIntEjecucion(long codEstadoIntEjecucion) {
		this.codEstadoIntEjecucion = codEstadoIntEjecucion;
	}

	public String getDesEstadoIntEjecucion() {
		return this.desEstadoIntEjecucion;
	}

	public void setDesEstadoIntEjecucion(String desEstadoIntEjecucion) {
		this.desEstadoIntEjecucion = desEstadoIntEjecucion;
	}

	public List<SolicitudesEjecucion> getSolicitudesEjecucions() {
		return this.solicitudesEjecucions;
	}

	public void setSolicitudesEjecucions(List<SolicitudesEjecucion> solicitudesEjecucions) {
		this.solicitudesEjecucions = solicitudesEjecucions;
	}

	public SolicitudesEjecucion addSolicitudesEjecucion(SolicitudesEjecucion solicitudesEjecucion) {
		getSolicitudesEjecucions().add(solicitudesEjecucion);
		solicitudesEjecucion.setEstadoIntEjecucion(this);

		return solicitudesEjecucion;
	}

	public SolicitudesEjecucion removeSolicitudesEjecucion(SolicitudesEjecucion solicitudesEjecucion) {
		getSolicitudesEjecucions().remove(solicitudesEjecucion);
		solicitudesEjecucion.setEstadoIntEjecucion(null);

		return solicitudesEjecucion;
	}

}