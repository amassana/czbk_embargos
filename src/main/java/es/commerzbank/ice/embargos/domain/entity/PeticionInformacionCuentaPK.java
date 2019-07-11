package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the PETICION_INFORMACION_CUENTAS database table.
 * 
 */
@Embeddable
public class PeticionInformacionCuentaPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COD_PETICION_INFORMACION", insertable=false, updatable=false, unique=true, nullable=false, length=10)
	private String codPeticionInformacion;

	@Column(unique=true, nullable=false, length=47)
	private String iban;

	public PeticionInformacionCuentaPK() {
	}
	public String getCodPeticionInformacion() {
		return this.codPeticionInformacion;
	}
	public void setCodPeticionInformacion(String codPeticionInformacion) {
		this.codPeticionInformacion = codPeticionInformacion;
	}
	public String getIban() {
		return this.iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof PeticionInformacionCuentaPK)) {
			return false;
		}
		PeticionInformacionCuentaPK castOther = (PeticionInformacionCuentaPK)other;
		return 
			this.codPeticionInformacion.equals(castOther.codPeticionInformacion)
			&& this.iban.equals(castOther.iban);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.codPeticionInformacion.hashCode();
		hash = hash * prime + this.iban.hashCode();
		
		return hash;
	}
}