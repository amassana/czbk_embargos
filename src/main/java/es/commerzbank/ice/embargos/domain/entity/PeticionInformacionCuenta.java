package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the PETICION_INFORMACION_CUENTAS database table.
 * 
 */
@Entity
@Table(name="PETICION_INFORMACION_CUENTAS")
@NamedQuery(name="PeticionInformacionCuenta.findAll", query="SELECT p FROM PeticionInformacionCuenta p")
public class PeticionInformacionCuenta implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PeticionInformacionCuentaPK id;

	@Column(length=20)
	private String cuenta;

	//bi-directional many-to-one association to PeticionInformacion
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_PETICION_INFORMACION", nullable=false, insertable=false, updatable=false)
	private PeticionInformacion peticionInformacion;

	public PeticionInformacionCuenta() {
	}

	public PeticionInformacionCuentaPK getId() {
		return this.id;
	}

	public void setId(PeticionInformacionCuentaPK id) {
		this.id = id;
	}

	public String getCuenta() {
		return this.cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public PeticionInformacion getPeticionInformacion() {
		return this.peticionInformacion;
	}

	public void setPeticionInformacion(PeticionInformacion peticionInformacion) {
		this.peticionInformacion = peticionInformacion;
	}

}