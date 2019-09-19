package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the CUENTA_TRABA_ACTUACION database table.
 * 
 */
@Entity
@Table(name="CUENTA_TRABA_ACTUACION")
@NamedQuery(name="CuentaTrabaActuacion.findAll", query="SELECT c FROM CuentaTrabaActuacion c")
public class CuentaTrabaActuacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_ACTUACION", unique=true, nullable=false, length=2)
	private String codActuacion;

	@Column(name="COD_EXTERNO_ACTUACION", length=2)
	private String codExternoActuacion;

	@Column(nullable=false, length=60)
	private String descripcion;

	@Column(name="TIPO_ENTIDAD", length=10)
	private String tipoEntidad;

	public CuentaTrabaActuacion() {
	}

	public String getCodActuacion() {
		return this.codActuacion;
	}

	public void setCodActuacion(String codActuacion) {
		this.codActuacion = codActuacion;
	}

	public String getCodExternoActuacion() {
		return this.codExternoActuacion;
	}

	public void setCodExternoActuacion(String codExternoActuacion) {
		this.codExternoActuacion = codExternoActuacion;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTipoEntidad() {
		return this.tipoEntidad;
	}

	public void setTipoEntidad(String tipoEntidad) {
		this.tipoEntidad = tipoEntidad;
	}

}