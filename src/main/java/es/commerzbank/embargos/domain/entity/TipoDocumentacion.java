package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the TIPO_DOCUMENTACION database table.
 * 
 */
@Entity
@Table(name="TIPO_DOCUMENTACION")
@NamedQuery(name="TipoDocumentacion.findAll", query="SELECT t FROM TipoDocumentacion t")
public class TipoDocumentacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_TIPO_DOCUMENTACION", unique=true, nullable=false)
	private long codTipoDocumentacion;

	@Column(name="DES_TIPO_DOCUMENTACION", length=60)
	private String desTipoDocumentacion;

	//bi-directional many-to-one association to SolicitudesCancelacion
	@OneToMany(mappedBy="tipoDocumentacion")
	private List<SolicitudesCancelacion> solicitudesCancelacions;

	//bi-directional many-to-one association to SolicitudesEjecucion
	@OneToMany(mappedBy="tipoDocumentacion")
	private List<SolicitudesEjecucion> solicitudesEjecucions;

	//bi-directional many-to-one association to SolicitudesLevantamiento
	@OneToMany(mappedBy="tipoDocumentacion")
	private List<SolicitudesLevantamiento> solicitudesLevantamientos;

	//bi-directional many-to-one association to SolicitudesTraba
	@OneToMany(mappedBy="tipoDocumentacion")
	private List<SolicitudesTraba> solicitudesTrabas;

	public TipoDocumentacion() {
	}

	public long getCodTipoDocumentacion() {
		return this.codTipoDocumentacion;
	}

	public void setCodTipoDocumentacion(long codTipoDocumentacion) {
		this.codTipoDocumentacion = codTipoDocumentacion;
	}

	public String getDesTipoDocumentacion() {
		return this.desTipoDocumentacion;
	}

	public void setDesTipoDocumentacion(String desTipoDocumentacion) {
		this.desTipoDocumentacion = desTipoDocumentacion;
	}

	public List<SolicitudesCancelacion> getSolicitudesCancelacions() {
		return this.solicitudesCancelacions;
	}

	public void setSolicitudesCancelacions(List<SolicitudesCancelacion> solicitudesCancelacions) {
		this.solicitudesCancelacions = solicitudesCancelacions;
	}

	public SolicitudesCancelacion addSolicitudesCancelacion(SolicitudesCancelacion solicitudesCancelacion) {
		getSolicitudesCancelacions().add(solicitudesCancelacion);
		solicitudesCancelacion.setTipoDocumentacion(this);

		return solicitudesCancelacion;
	}

	public SolicitudesCancelacion removeSolicitudesCancelacion(SolicitudesCancelacion solicitudesCancelacion) {
		getSolicitudesCancelacions().remove(solicitudesCancelacion);
		solicitudesCancelacion.setTipoDocumentacion(null);

		return solicitudesCancelacion;
	}

	public List<SolicitudesEjecucion> getSolicitudesEjecucions() {
		return this.solicitudesEjecucions;
	}

	public void setSolicitudesEjecucions(List<SolicitudesEjecucion> solicitudesEjecucions) {
		this.solicitudesEjecucions = solicitudesEjecucions;
	}

	public SolicitudesEjecucion addSolicitudesEjecucion(SolicitudesEjecucion solicitudesEjecucion) {
		getSolicitudesEjecucions().add(solicitudesEjecucion);
		solicitudesEjecucion.setTipoDocumentacion(this);

		return solicitudesEjecucion;
	}

	public SolicitudesEjecucion removeSolicitudesEjecucion(SolicitudesEjecucion solicitudesEjecucion) {
		getSolicitudesEjecucions().remove(solicitudesEjecucion);
		solicitudesEjecucion.setTipoDocumentacion(null);

		return solicitudesEjecucion;
	}

	public List<SolicitudesLevantamiento> getSolicitudesLevantamientos() {
		return this.solicitudesLevantamientos;
	}

	public void setSolicitudesLevantamientos(List<SolicitudesLevantamiento> solicitudesLevantamientos) {
		this.solicitudesLevantamientos = solicitudesLevantamientos;
	}

	public SolicitudesLevantamiento addSolicitudesLevantamiento(SolicitudesLevantamiento solicitudesLevantamiento) {
		getSolicitudesLevantamientos().add(solicitudesLevantamiento);
		solicitudesLevantamiento.setTipoDocumentacion(this);

		return solicitudesLevantamiento;
	}

	public SolicitudesLevantamiento removeSolicitudesLevantamiento(SolicitudesLevantamiento solicitudesLevantamiento) {
		getSolicitudesLevantamientos().remove(solicitudesLevantamiento);
		solicitudesLevantamiento.setTipoDocumentacion(null);

		return solicitudesLevantamiento;
	}

	public List<SolicitudesTraba> getSolicitudesTrabas() {
		return this.solicitudesTrabas;
	}

	public void setSolicitudesTrabas(List<SolicitudesTraba> solicitudesTrabas) {
		this.solicitudesTrabas = solicitudesTrabas;
	}

	public SolicitudesTraba addSolicitudesTraba(SolicitudesTraba solicitudesTraba) {
		getSolicitudesTrabas().add(solicitudesTraba);
		solicitudesTraba.setTipoDocumentacion(this);

		return solicitudesTraba;
	}

	public SolicitudesTraba removeSolicitudesTraba(SolicitudesTraba solicitudesTraba) {
		getSolicitudesTrabas().remove(solicitudesTraba);
		solicitudesTraba.setTipoDocumentacion(null);

		return solicitudesTraba;
	}

}