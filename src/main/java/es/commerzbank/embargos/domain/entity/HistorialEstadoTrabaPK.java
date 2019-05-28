package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the HISTORIAL_ESTADO_TRABA database table.
 * 
 */
@Embeddable
public class HistorialEstadoTrabaPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COD_TRABA", insertable=false, updatable=false, unique=true, nullable=false)
	private long codTraba;

	@Column(name="SECUENCIA_ESTADO", unique=true, nullable=false)
	private long secuenciaEstado;

	public HistorialEstadoTrabaPK() {
	}
	public long getCodTraba() {
		return this.codTraba;
	}
	public void setCodTraba(long codTraba) {
		this.codTraba = codTraba;
	}
	public long getSecuenciaEstado() {
		return this.secuenciaEstado;
	}
	public void setSecuenciaEstado(long secuenciaEstado) {
		this.secuenciaEstado = secuenciaEstado;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof HistorialEstadoTrabaPK)) {
			return false;
		}
		HistorialEstadoTrabaPK castOther = (HistorialEstadoTrabaPK)other;
		return 
			(this.codTraba == castOther.codTraba)
			&& (this.secuenciaEstado == castOther.secuenciaEstado);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.codTraba ^ (this.codTraba >>> 32)));
		hash = hash * prime + ((int) (this.secuenciaEstado ^ (this.secuenciaEstado >>> 32)));
		
		return hash;
	}
}