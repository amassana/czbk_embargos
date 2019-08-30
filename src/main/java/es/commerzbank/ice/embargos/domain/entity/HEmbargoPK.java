package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the H_EMBARGO database table.
 * 
 */
@Embeddable
public class HEmbargoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COD_EMBARGO")
	private long codEmbargo;

	@Column(name="COD_AUDITORIA")
	private long codAuditoria;

	public HEmbargoPK() {
	}
	public long getCodEmbargo() {
		return this.codEmbargo;
	}
	public void setCodEmbargo(long codEmbargo) {
		this.codEmbargo = codEmbargo;
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
		if (!(other instanceof HEmbargoPK)) {
			return false;
		}
		HEmbargoPK castOther = (HEmbargoPK)other;
		return 
			(this.codEmbargo == castOther.codEmbargo)
			&& (this.codAuditoria == castOther.codAuditoria);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.codEmbargo ^ (this.codEmbargo >>> 32)));
		hash = hash * prime + ((int) (this.codAuditoria ^ (this.codAuditoria >>> 32)));
		
		return hash;
	}
}