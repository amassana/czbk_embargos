package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ESTADO_CTRLFICHERO database table.
 * 
 */
@Embeddable
public class EstadoCtrlficheroPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COD_TIPO_FICHERO", insertable=false, updatable=false, unique=true, nullable=false)
	private long codTipoFichero;

	@Column(name="COD_ESTADO", unique=true, nullable=false)
	private long codEstado;

	public EstadoCtrlficheroPK() {
	}
	public long getCodTipoFichero() {
		return this.codTipoFichero;
	}
	public void setCodTipoFichero(long codTipoFichero) {
		this.codTipoFichero = codTipoFichero;
	}
	public long getCodEstado() {
		return this.codEstado;
	}
	public void setCodEstado(long codEstado) {
		this.codEstado = codEstado;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof EstadoCtrlficheroPK)) {
			return false;
		}
		EstadoCtrlficheroPK castOther = (EstadoCtrlficheroPK)other;
		return 
			(this.codTipoFichero == castOther.codTipoFichero)
			&& (this.codEstado == castOther.codEstado);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.codTipoFichero ^ (this.codTipoFichero >>> 32)));
		hash = hash * prime + ((int) (this.codEstado ^ (this.codEstado >>> 32)));
		
		return hash;
	}
}