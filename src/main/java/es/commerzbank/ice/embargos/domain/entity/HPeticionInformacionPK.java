package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the H_PETICION_INFORMACION database table.
 * 
 */
@Embeddable
public class HPeticionInformacionPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COD_PETICION", unique=true, nullable=false)
	private long codPeticion;

	@Column(name="CHANGE_TIMESTAMP", unique=true, nullable=false)
	private String changeTimestamp;

	public HPeticionInformacionPK() {
	}
	public long getCodPeticion() {
		return this.codPeticion;
	}
	public void setCodPeticion(long codPeticion) {
		this.codPeticion = codPeticion;
	}
	public String getChangeTimestamp() {
		return this.changeTimestamp;
	}
	public void setChangeTimestamp(String changeTimestamp) {
		this.changeTimestamp = changeTimestamp;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof HPeticionInformacionPK)) {
			return false;
		}
		HPeticionInformacionPK castOther = (HPeticionInformacionPK)other;
		return 
			this.codPeticion == castOther.codPeticion
			&& this.changeTimestamp.equals(castOther.changeTimestamp);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.codPeticion ^ (this.codPeticion >>> 32)));
		hash = hash * prime + this.changeTimestamp.hashCode();
		
		return hash;
	}
}