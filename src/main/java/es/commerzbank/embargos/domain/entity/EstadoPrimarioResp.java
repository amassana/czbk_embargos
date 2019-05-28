package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ESTADO_PRIMARIO_RESP database table.
 * 
 */
@Entity
@Table(name="ESTADO_PRIMARIO_RESP")
@NamedQuery(name="EstadoPrimarioResp.findAll", query="SELECT e FROM EstadoPrimarioResp e")
public class EstadoPrimarioResp implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_ESTADO_PRIMARIO_RESP", unique=true, nullable=false, length=4)
	private String codEstadoPrimarioResp;

	@Column(name="DES_ESTADO_PRIMARIO_RESP", length=60)
	private String desEstadoPrimarioResp;

	//bi-directional many-to-one association to Peticione
	@OneToMany(mappedBy="estadoPrimarioResp")
	private List<Peticion> peticiones;

	public EstadoPrimarioResp() {
	}

	public String getCodEstadoPrimarioResp() {
		return this.codEstadoPrimarioResp;
	}

	public void setCodEstadoPrimarioResp(String codEstadoPrimarioResp) {
		this.codEstadoPrimarioResp = codEstadoPrimarioResp;
	}

	public String getDesEstadoPrimarioResp() {
		return this.desEstadoPrimarioResp;
	}

	public void setDesEstadoPrimarioResp(String desEstadoPrimarioResp) {
		this.desEstadoPrimarioResp = desEstadoPrimarioResp;
	}

	public List<Peticion> getPeticiones() {
		return this.peticiones;
	}

	public void setPeticiones(List<Peticion> peticiones) {
		this.peticiones = peticiones;
	}

	public Peticion addPeticione(Peticion peticione) {
		getPeticiones().add(peticione);
		peticione.setEstadoPrimarioResp(this);

		return peticione;
	}

	public Peticion removePeticione(Peticion peticione) {
		getPeticiones().remove(peticione);
		peticione.setEstadoPrimarioResp(null);

		return peticione;
	}

}