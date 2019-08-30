package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the H_CUENTA_TRABA database table.
 * 
 */
@Embeddable
public class HCuentaTrabaPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COD_CUENTA_TRABA")
	private long codCuentaTraba;

	@Column(name="COD_AUDITORIA")
	private long codAuditoria;

	public HCuentaTrabaPK() {
	}
	public long getCodCuentaTraba() {
		return this.codCuentaTraba;
	}
	public void setCodCuentaTraba(long codCuentaTraba) {
		this.codCuentaTraba = codCuentaTraba;
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
		if (!(other instanceof HCuentaTrabaPK)) {
			return false;
		}
		HCuentaTrabaPK castOther = (HCuentaTrabaPK)other;
		return 
			(this.codCuentaTraba == castOther.codCuentaTraba)
			&& (this.codAuditoria == castOther.codAuditoria);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.codCuentaTraba ^ (this.codCuentaTraba >>> 32)));
		hash = hash * prime + ((int) (this.codAuditoria ^ (this.codAuditoria >>> 32)));
		
		return hash;
	}
}