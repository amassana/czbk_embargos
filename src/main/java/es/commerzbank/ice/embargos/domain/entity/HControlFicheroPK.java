package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the H_CONTROL_FICHERO database table.
 * 
 */
@Embeddable
public class HControlFicheroPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COD_CONTROL_FICHERO", unique=true, nullable=false)
	private long codControlFichero;

	@Column(name="CHANGE_TIMESTAMP", unique=true, nullable=false)
	private String changeTimestamp;

	public HControlFicheroPK() {
	}
	public long getCodControlFichero() {
		return this.codControlFichero;
	}
	public void setCodControlFichero(long codControlFichero) {
		this.codControlFichero = codControlFichero;
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
		if (!(other instanceof HControlFicheroPK)) {
			return false;
		}
		HControlFicheroPK castOther = (HControlFicheroPK)other;
		return 
			(this.codControlFichero == castOther.codControlFichero)
			&& this.changeTimestamp.equals(castOther.changeTimestamp);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.codControlFichero ^ (this.codControlFichero >>> 32)));
		hash = hash * prime + this.changeTimestamp.hashCode();
		
		return hash;
	}
}