package es.commerzbank.embargos.domain.entity;

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

	//bi-directional many-to-one association to Peticione
	@OneToMany(mappedBy="estadoPrimarioPeticion")
	private List<Peticione> peticiones;

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

	public List<Peticione> getPeticiones() {
		return this.peticiones;
	}

	public void setPeticiones(List<Peticione> peticiones) {
		this.peticiones = peticiones;
	}

	public Peticione addPeticione(Peticione peticione) {
		getPeticiones().add(peticione);
		peticione.setEstadoPrimarioPeticion(this);

		return peticione;
	}

	public Peticione removePeticione(Peticione peticione) {
		getPeticiones().remove(peticione);
		peticione.setEstadoPrimarioPeticion(null);

		return peticione;
	}

}