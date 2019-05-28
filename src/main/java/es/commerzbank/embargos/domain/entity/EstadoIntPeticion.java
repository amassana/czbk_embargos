package es.commerzbank.embargos.domain.entity;

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

	//bi-directional many-to-one association to Peticione
	@OneToMany(mappedBy="estadoIntPeticion")
	private List<Peticione> peticiones;

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

	public List<Peticione> getPeticiones() {
		return this.peticiones;
	}

	public void setPeticiones(List<Peticione> peticiones) {
		this.peticiones = peticiones;
	}

	public Peticione addPeticione(Peticione peticione) {
		getPeticiones().add(peticione);
		peticione.setEstadoIntPeticion(this);

		return peticione;
	}

	public Peticione removePeticione(Peticione peticione) {
		getPeticiones().remove(peticione);
		peticione.setEstadoIntPeticion(null);

		return peticione;
	}

}