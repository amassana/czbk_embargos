package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ESTADO_PRIMARIO_PETICION database table.
 * 
 */
@Entity
@Table(name="ESTADO_PRIMARIO_PETICION")
@NamedQuery(name="EstadoPrimarioPeticion.findAll", query="SELECT e FROM EstadoPrimarioPeticion e")
public class EstadoPrimarioPeticion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_ESTADO_PRIMARIO_PETICION", unique=true, nullable=false, length=4)
	private String codEstadoPrimarioPeticion;

	@Column(name="DES_ESTADO_PRIMARIO_PETICION", length=60)
	private String desEstadoPrimarioPeticion;

	//bi-directional many-to-one association to Peticion
	@OneToMany(mappedBy="estadoPrimarioPeticion")
	private List<Peticion> peticiones;

	public EstadoPrimarioPeticion() {
	}

	public String getCodEstadoPrimarioPeticion() {
		return this.codEstadoPrimarioPeticion;
	}

	public void setCodEstadoPrimarioPeticion(String codEstadoPrimarioPeticion) {
		this.codEstadoPrimarioPeticion = codEstadoPrimarioPeticion;
	}

	public String getDesEstadoPrimarioPeticion() {
		return this.desEstadoPrimarioPeticion;
	}

	public void setDesEstadoPrimarioPeticion(String desEstadoPrimarioPeticion) {
		this.desEstadoPrimarioPeticion = desEstadoPrimarioPeticion;
	}

	public List<Peticion> getPeticiones() {
		return this.peticiones;
	}

	public void setPeticiones(List<Peticion> peticiones) {
		this.peticiones = peticiones;
	}

	public Peticion addPeticion(Peticion peticion) {
		getPeticiones().add(peticion);
		peticion.setEstadoPrimarioPeticion(this);

		return peticion;
	}

	public Peticion removePeticion(Peticion peticion) {
		getPeticiones().remove(peticion);
		peticion.setEstadoPrimarioPeticion(null);

		return peticion;
	}

}