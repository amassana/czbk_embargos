package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ESTADO_RESP_TRABA database table.
 * 
 */
@Entity
@Table(name="ESTADO_RESP_TRABA")
@NamedQuery(name="EstadoRespTraba.findAll", query="SELECT e FROM EstadoRespTraba e")
public class EstadoRespTraba implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_ESTADO_RESP_TRABA", unique=true, nullable=false, length=4)
	private String codEstadoRespTraba;

	@Column(name="DES_ESTADO_RESP_TRABA", length=60)
	private String desEstadoRespTraba;

	//bi-directional many-to-one association to SolicitudesTraba
	@OneToMany(mappedBy="estadoRespTraba")
	private List<SolicitudesTraba> solicitudesTrabas;

	public EstadoRespTraba() {
	}

	public String getCodEstadoRespTraba() {
		return this.codEstadoRespTraba;
	}

	public void setCodEstadoRespTraba(String codEstadoRespTraba) {
		this.codEstadoRespTraba = codEstadoRespTraba;
	}

	public String getDesEstadoRespTraba() {
		return this.desEstadoRespTraba;
	}

	public void setDesEstadoRespTraba(String desEstadoRespTraba) {
		this.desEstadoRespTraba = desEstadoRespTraba;
	}

	public List<SolicitudesTraba> getSolicitudesTrabas() {
		return this.solicitudesTrabas;
	}

	public void setSolicitudesTrabas(List<SolicitudesTraba> solicitudesTrabas) {
		this.solicitudesTrabas = solicitudesTrabas;
	}

	public SolicitudesTraba addSolicitudesTraba(SolicitudesTraba solicitudesTraba) {
		getSolicitudesTrabas().add(solicitudesTraba);
		solicitudesTraba.setEstadoRespTraba(this);

		return solicitudesTraba;
	}

	public SolicitudesTraba removeSolicitudesTraba(SolicitudesTraba solicitudesTraba) {
		getSolicitudesTrabas().remove(solicitudesTraba);
		solicitudesTraba.setEstadoRespTraba(null);

		return solicitudesTraba;
	}

}