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

	@Column(name="F_ULTIMA_MODIFICACION", unique=true, nullable=false, precision=14)
	private long fUltimaModificacion;

	public HControlFicheroPK() {
	}
	public long getCodControlFichero() {
		return this.codControlFichero;
	}
	public void setCodControlFichero(long codControlFichero) {
		this.codControlFichero = codControlFichero;
	}
	public long getFUltimaModificacion() {
		return this.fUltimaModificacion;
	}
	public void setFUltimaModificacion(long fUltimaModificacion) {
		this.fUltimaModificacion = fUltimaModificacion;
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
			&& (this.fUltimaModificacion == castOther.fUltimaModificacion);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.codControlFichero ^ (this.codControlFichero >>> 32)));
		hash = hash * prime + ((int) (this.fUltimaModificacion ^ (this.fUltimaModificacion >>> 32)));
		
		return hash;
	}
}