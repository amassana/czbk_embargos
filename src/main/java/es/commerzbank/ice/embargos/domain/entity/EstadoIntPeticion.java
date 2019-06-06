package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ESTADO_INT_PETICION database table.
 * 
 */
@Entity
@Table(name="ESTADO_INT_PETICION")
@NamedQuery(name="EstadoIntPeticion.findAll", query="SELECT e FROM EstadoIntPeticion e")
public class EstadoIntPeticion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_ESTADO_INT_PETICION", unique=true, nullable=false)
	private long codEstadoIntPeticion;

	@Column(name="DES_ESTADO_INT_PETICION", length=60)
	private String desEstadoIntPeticion;

	//bi-directional many-to-one association to Peticion
	@OneToMany(mappedBy="estadoIntPeticion")
	private List<Peticion> peticiones;

	public EstadoIntPeticion() {
	}

	public long getCodEstadoIntPeticion() {
		return this.codEstadoIntPeticion;
	}

	public void setCodEstadoIntPeticion(long codEstadoIntPeticion) {
		this.codEstadoIntPeticion = codEstadoIntPeticion;
	}

	public String getDesEstadoIntPeticion() {
		return this.desEstadoIntPeticion;
	}

	public void setDesEstadoIntPeticion(String desEstadoIntPeticion) {
		this.desEstadoIntPeticion = desEstadoIntPeticion;
	}

	public List<Peticion> getPeticiones() {
		return this.peticiones;
	}

	public void setPeticiones(List<Peticion> peticiones) {
		this.peticiones = peticiones;
	}

	public Peticion addPeticion(Peticion peticion) {
		getPeticiones().add(peticion);
		peticion.setEstadoIntPeticion(this);

		return peticion;
	}

	public Peticion removePeticion(Peticion peticion) {
		getPeticiones().remove(peticion);
		peticion.setEstadoIntPeticion(null);

		return peticion;
	}

}