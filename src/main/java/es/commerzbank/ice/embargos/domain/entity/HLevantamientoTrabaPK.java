package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class HLevantamientoTrabaPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="COD_LEVANTAMIENTO", unique=true, nullable=false)
	private String codLevantamiento;

	@Column(name="CHANGE_TIMESTAMP", unique=true, nullable=false)
	private String changeTimestamp;



	public String getCodLevantamiento() {
		return codLevantamiento;
	}

	public void setCodLevantamiento(String codLevantamiento) {
		this.codLevantamiento = codLevantamiento;
	}

	public String getChangeTimestamp() {
		return changeTimestamp;
	}

	public void setChangeTimestamp(String changeTimestamp) {
		this.changeTimestamp = changeTimestamp;
	}
	
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof HLevantamientoTrabaPK)) {
			return false;
		}
		HLevantamientoTrabaPK castOther = (HLevantamientoTrabaPK)other;
		return 
			this.codLevantamiento.equals(castOther.codLevantamiento)
			&& this.changeTimestamp.equals(castOther.changeTimestamp);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.codLevantamiento.hashCode();
		hash = hash * prime + this.changeTimestamp.hashCode();
		
		return hash;
	}
}
