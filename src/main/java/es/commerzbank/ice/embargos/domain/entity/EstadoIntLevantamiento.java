package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ESTADO_INT_LEVANTAMIENTO database table.
 * 
 */
@Entity
@Table(name="ESTADO_INT_LEVANTAMIENTO")
@NamedQuery(name="EstadoIntLevantamiento.findAll", query="SELECT e FROM EstadoIntLevantamiento e")
public class EstadoIntLevantamiento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_ESTADO_INT_LEVANTAMIENTO", unique=true, nullable=false)
	private long codEstadoIntLevantamiento;

	@Column(name="DES_ESTADO_INT_LEVANTAMIENTO", length=60)
	private String desEstadoIntLevantamiento;

	//bi-directional many-to-one association to SolicitudesLevantamiento
	@OneToMany(mappedBy="estadoIntLevantamiento")
	private List<SolicitudesLevantamiento> solicitudesLevantamientos;

	public EstadoIntLevantamiento() {
	}

	public long getCodEstadoIntLevantamiento() {
		return this.codEstadoIntLevantamiento;
	}

	public void setCodEstadoIntLevantamiento(long codEstadoIntLevantamiento) {
		this.codEstadoIntLevantamiento = codEstadoIntLevantamiento;
	}

	public String getDesEstadoIntLevantamiento() {
		return this.desEstadoIntLevantamiento;
	}

	public void setDesEstadoIntLevantamiento(String desEstadoIntLevantamiento) {
		this.desEstadoIntLevantamiento = desEstadoIntLevantamiento;
	}

	public List<SolicitudesLevantamiento> getSolicitudesLevantamientos() {
		return this.solicitudesLevantamientos;
	}

	public void setSolicitudesLevantamientos(List<SolicitudesLevantamiento> solicitudesLevantamientos) {
		this.solicitudesLevantamientos = solicitudesLevantamientos;
	}

	public SolicitudesLevantamiento addSolicitudesLevantamiento(SolicitudesLevantamiento solicitudesLevantamiento) {
		getSolicitudesLevantamientos().add(solicitudesLevantamiento);
		solicitudesLevantamiento.setEstadoIntLevantamiento(this);

		return solicitudesLevantamiento;
	}

	public SolicitudesLevantamiento removeSolicitudesLevantamiento(SolicitudesLevantamiento solicitudesLevantamiento) {
		getSolicitudesLevantamientos().remove(solicitudesLevantamiento);
		solicitudesLevantamiento.setEstadoIntLevantamiento(null);

		return solicitudesLevantamiento;
	}

}