package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ESTADO_SECUNDARIO_RESP database table.
 * 
 */
@Entity
@Table(name="ESTADO_SECUNDARIO_RESP")
@NamedQuery(name="EstadoSecundarioResp.findAll", query="SELECT e FROM EstadoSecundarioResp e")
public class EstadoSecundarioResp implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_ESTADO_SECUNDARIO_RESP", unique=true, nullable=false, length=16)
	private String codEstadoSecundarioResp;

	@Column(name="DES_ESTADO_SECUNDARIO_RESP", length=60)
	private String desEstadoSecundarioResp;

	//bi-directional many-to-one association to Peticion
	@OneToMany(mappedBy="estadoSecundarioResp")
	private List<Peticion> peticiones;

	public EstadoSecundarioResp() {
	}

	public String getCodEstadoSecundarioResp() {
		return this.codEstadoSecundarioResp;
	}

	public void setCodEstadoSecundarioResp(String codEstadoSecundarioResp) {
		this.codEstadoSecundarioResp = codEstadoSecundarioResp;
	}

	public String getDesEstadoSecundarioResp() {
		return this.desEstadoSecundarioResp;
	}

	public void setDesEstadoSecundarioResp(String desEstadoSecundarioResp) {
		this.desEstadoSecundarioResp = desEstadoSecundarioResp;
	}

	public List<Peticion> getPeticiones() {
		return this.peticiones;
	}

	public void setPeticiones(List<Peticion> peticiones) {
		this.peticiones = peticiones;
	}

	public Peticion addPeticion(Peticion peticion) {
		getPeticiones().add(peticion);
		peticion.setEstadoSecundarioResp(this);

		return peticion;
	}

	public Peticion removePeticion(Peticion peticion) {
		getPeticiones().remove(peticion);
		peticion.setEstadoSecundarioResp(null);

		return peticion;
	}

}