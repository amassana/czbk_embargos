package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the H_PETICION_INFORMACION database table.
 * 
 */
@Embeddable
public class HPeticionInformacionPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="COD_PETICION", unique=true, nullable=false, length=10)
	private String codPeticion;

	@Column(name="F_ULTIMA_MODIFICACION", unique=true, nullable=false, precision=14)
	private long fUltimaModificacion;

	public HPeticionInformacionPK() {
	}
	public String getCodPeticion() {
		return this.codPeticion;
	}
	public void setCodPeticion(String codPeticion) {
		this.codPeticion = codPeticion;
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
		if (!(other instanceof HPeticionInformacionPK)) {
			return false;
		}
		HPeticionInformacionPK castOther = (HPeticionInformacionPK)other;
		return 
			this.codPeticion.equals(castOther.codPeticion)
			&& (this.fUltimaModificacion == castOther.fUltimaModificacion);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.codPeticion.hashCode();
		hash = hash * prime + ((int) (this.fUltimaModificacion ^ (this.fUltimaModificacion >>> 32)));
		
		return hash;
	}
}