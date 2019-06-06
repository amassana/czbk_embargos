package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the APUNTE_CONTABLE database table.
 * 
 */
@Embeddable
public class ApunteContablePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="SECUENCIA_APUNTE", unique=true, nullable=false)
	private long secuenciaApunte;

	@Column(name="COD_APUNTE_CONTABLE", unique=true, nullable=false)
	private long codApunteContable;

	public ApunteContablePK() {
	}
	public long getSecuenciaApunte() {
		return this.secuenciaApunte;
	}
	public void setSecuenciaApunte(long secuenciaApunte) {
		this.secuenciaApunte = secuenciaApunte;
	}
	public long getCodApunteContable() {
		return this.codApunteContable;
	}
	public void setCodApunteContable(long codApunteContable) {
		this.codApunteContable = codApunteContable;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ApunteContablePK)) {
			return false;
		}
		ApunteContablePK castOther = (ApunteContablePK)other;
		return 
			(this.secuenciaApunte == castOther.secuenciaApunte)
			&& (this.codApunteContable == castOther.codApunteContable);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.secuenciaApunte ^ (this.secuenciaApunte >>> 32)));
		hash = hash * prime + ((int) (this.codApunteContable ^ (this.codApunteContable >>> 32)));
		
		return hash;
	}
}