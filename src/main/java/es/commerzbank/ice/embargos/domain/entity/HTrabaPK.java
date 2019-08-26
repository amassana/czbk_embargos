package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the H_TRABAS database table.
 * 
 */
@Embeddable
public class HTrabaPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COD_TRABA")
	private long codTraba;

	@Column(name="COD_AUDITORIA", insertable=false, updatable=false)
	private long codAuditoria;

	public HTrabaPK() {
	}
	public long getCodTraba() {
		return this.codTraba;
	}
	public void setCodTraba(long codTraba) {
		this.codTraba = codTraba;
	}
	public long getCodAuditoria() {
		return this.codAuditoria;
	}
	public void setCodAuditoria(long codAuditoria) {
		this.codAuditoria = codAuditoria;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof HTrabaPK)) {
			return false;
		}
		HTrabaPK castOther = (HTrabaPK)other;
		return 
			(this.codTraba == castOther.codTraba)
			&& (this.codAuditoria == castOther.codAuditoria);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.codTraba ^ (this.codTraba >>> 32)));
		hash = hash * prime + ((int) (this.codAuditoria ^ (this.codAuditoria >>> 32)));
		
		return hash;
	}
}