package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the DETALLE_CONTROL_FICHERO database table.
 * 
 */
@Embeddable
public class DetalleControlFicheroPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COD_CONTROL_FICHERO", insertable=false, updatable=false, unique=true, nullable=false)
	private long codControlFichero;

	@Column(name="NUM_SECUENCIA", unique=true, nullable=false)
	private long numSecuencia;

	public DetalleControlFicheroPK() {
	}
	public long getCodControlFichero() {
		return this.codControlFichero;
	}
	public void setCodControlFichero(long codControlFichero) {
		this.codControlFichero = codControlFichero;
	}
	public long getNumSecuencia() {
		return this.numSecuencia;
	}
	public void setNumSecuencia(long numSecuencia) {
		this.numSecuencia = numSecuencia;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof DetalleControlFicheroPK)) {
			return false;
		}
		DetalleControlFicheroPK castOther = (DetalleControlFicheroPK)other;
		return 
			(this.codControlFichero == castOther.codControlFichero)
			&& (this.numSecuencia == castOther.numSecuencia);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.codControlFichero ^ (this.codControlFichero >>> 32)));
		hash = hash * prime + ((int) (this.numSecuencia ^ (this.numSecuencia >>> 32)));
		
		return hash;
	}
}