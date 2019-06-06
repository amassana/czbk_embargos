package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ESTADO_INT_CANCELACION database table.
 * 
 */
@Entity
@Table(name="ESTADO_INT_CANCELACION")
@NamedQuery(name="EstadoIntCancelacion.findAll", query="SELECT e FROM EstadoIntCancelacion e")
public class EstadoIntCancelacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_ESTADO_INT_CANCELACION", unique=true, nullable=false)
	private long codEstadoIntCancelacion;

	@Column(name="DES_ESTADO_INT_CANCELACION", length=60)
	private String desEstadoIntCancelacion;

	//bi-directional many-to-one association to SolicitudesCancelacion
	@OneToMany(mappedBy="estadoIntCancelacion")
	private List<SolicitudesCancelacion> solicitudesCancelacions;

	public EstadoIntCancelacion() {
	}

	public long getCodEstadoIntCancelacion() {
		return this.codEstadoIntCancelacion;
	}

	public void setCodEstadoIntCancelacion(long codEstadoIntCancelacion) {
		this.codEstadoIntCancelacion = codEstadoIntCancelacion;
	}

	public String getDesEstadoIntCancelacion() {
		return this.desEstadoIntCancelacion;
	}

	public void setDesEstadoIntCancelacion(String desEstadoIntCancelacion) {
		this.desEstadoIntCancelacion = desEstadoIntCancelacion;
	}

	public List<SolicitudesCancelacion> getSolicitudesCancelacions() {
		return this.solicitudesCancelacions;
	}

	public void setSolicitudesCancelacions(List<SolicitudesCancelacion> solicitudesCancelacions) {
		this.solicitudesCancelacions = solicitudesCancelacions;
	}

	public SolicitudesCancelacion addSolicitudesCancelacion(SolicitudesCancelacion solicitudesCancelacion) {
		getSolicitudesCancelacions().add(solicitudesCancelacion);
		solicitudesCancelacion.setEstadoIntCancelacion(this);

		return solicitudesCancelacion;
	}

	public SolicitudesCancelacion removeSolicitudesCancelacion(SolicitudesCancelacion solicitudesCancelacion) {
		getSolicitudesCancelacions().remove(solicitudesCancelacion);
		solicitudesCancelacion.setEstadoIntCancelacion(null);

		return solicitudesCancelacion;
	}

}