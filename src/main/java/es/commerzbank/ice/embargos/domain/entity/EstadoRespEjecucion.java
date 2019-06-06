package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ESTADO_RESP_EJECUCION database table.
 * 
 */
@Entity
@Table(name="ESTADO_RESP_EJECUCION")
@NamedQuery(name="EstadoRespEjecucion.findAll", query="SELECT e FROM EstadoRespEjecucion e")
public class EstadoRespEjecucion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_ESTADO_RESP_EJECUCION", unique=true, nullable=false, length=4)
	private String codEstadoRespEjecucion;

	@Column(name="DES_ESTADO_RESP_EJECUCION", length=60)
	private String desEstadoRespEjecucion;

	//bi-directional many-to-one association to SolicitudesEjecucion
	@OneToMany(mappedBy="estadoRespEjecucion")
	private List<SolicitudesEjecucion> solicitudesEjecucions;

	public EstadoRespEjecucion() {
	}

	public String getCodEstadoRespEjecucion() {
		return this.codEstadoRespEjecucion;
	}

	public void setCodEstadoRespEjecucion(String codEstadoRespEjecucion) {
		this.codEstadoRespEjecucion = codEstadoRespEjecucion;
	}

	public String getDesEstadoRespEjecucion() {
		return this.desEstadoRespEjecucion;
	}

	public void setDesEstadoRespEjecucion(String desEstadoRespEjecucion) {
		this.desEstadoRespEjecucion = desEstadoRespEjecucion;
	}

	public List<SolicitudesEjecucion> getSolicitudesEjecucions() {
		return this.solicitudesEjecucions;
	}

	public void setSolicitudesEjecucions(List<SolicitudesEjecucion> solicitudesEjecucions) {
		this.solicitudesEjecucions = solicitudesEjecucions;
	}

	public SolicitudesEjecucion addSolicitudesEjecucion(SolicitudesEjecucion solicitudesEjecucion) {
		getSolicitudesEjecucions().add(solicitudesEjecucion);
		solicitudesEjecucion.setEstadoRespEjecucion(this);

		return solicitudesEjecucion;
	}

	public SolicitudesEjecucion removeSolicitudesEjecucion(SolicitudesEjecucion solicitudesEjecucion) {
		getSolicitudesEjecucions().remove(solicitudesEjecucion);
		solicitudesEjecucion.setEstadoRespEjecucion(null);

		return solicitudesEjecucion;
	}

}