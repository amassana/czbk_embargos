package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ESTADO_RESP_LEVANTAMIENTO database table.
 * 
 */
@Entity
@Table(name="ESTADO_RESP_LEVANTAMIENTO")
@NamedQuery(name="EstadoRespLevantamiento.findAll", query="SELECT e FROM EstadoRespLevantamiento e")
public class EstadoRespLevantamiento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_ESTADO_RESP_LEVANTAMIENTO", unique=true, nullable=false, length=4)
	private String codEstadoRespLevantamiento;

	@Column(name="DES_ESTADO_RESP_LEVANTAMIENTO", length=60)
	private String desEstadoRespLevantamiento;

	//bi-directional many-to-one association to SolicitudesLevantamiento
	@OneToMany(mappedBy="estadoRespLevantamiento")
	private List<SolicitudesLevantamiento> solicitudesLevantamientos;

	public EstadoRespLevantamiento() {
	}

	public String getCodEstadoRespLevantamiento() {
		return this.codEstadoRespLevantamiento;
	}

	public void setCodEstadoRespLevantamiento(String codEstadoRespLevantamiento) {
		this.codEstadoRespLevantamiento = codEstadoRespLevantamiento;
	}

	public String getDesEstadoRespLevantamiento() {
		return this.desEstadoRespLevantamiento;
	}

	public void setDesEstadoRespLevantamiento(String desEstadoRespLevantamiento) {
		this.desEstadoRespLevantamiento = desEstadoRespLevantamiento;
	}

	public List<SolicitudesLevantamiento> getSolicitudesLevantamientos() {
		return this.solicitudesLevantamientos;
	}

	public void setSolicitudesLevantamientos(List<SolicitudesLevantamiento> solicitudesLevantamientos) {
		this.solicitudesLevantamientos = solicitudesLevantamientos;
	}

	public SolicitudesLevantamiento addSolicitudesLevantamiento(SolicitudesLevantamiento solicitudesLevantamiento) {
		getSolicitudesLevantamientos().add(solicitudesLevantamiento);
		solicitudesLevantamiento.setEstadoRespLevantamiento(this);

		return solicitudesLevantamiento;
	}

	public SolicitudesLevantamiento removeSolicitudesLevantamiento(SolicitudesLevantamiento solicitudesLevantamiento) {
		getSolicitudesLevantamientos().remove(solicitudesLevantamiento);
		solicitudesLevantamiento.setEstadoRespLevantamiento(null);

		return solicitudesLevantamiento;
	}

}