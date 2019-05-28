package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the CUENTA_EMBARGO database table.
 * 
 */
@Entity
@Table(name="CUENTA_EMBARGO")
@NamedQuery(name="CuentaEmbargo.findAll", query="SELECT c FROM CuentaEmbargo c")
public class CuentaEmbargo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_CUENTA_EMBARGO", unique=true, nullable=false)
	private long codCuentaEmbargo;

	@Column(length=2)
	private String actuacion;

	@Column(name="CLAVE_SEGURIDAD", length=20)
	private String claveSeguridad;

	@Column(length=20)
	private String cuenta;

	@Column(name="F_ULTIMA_MODIFICACION", precision=14)
	private BigDecimal fUltimaModificacion;

	@Column(length=24)
	private String iban;

	private BigDecimal importe;

	@Column(name="NUMERO_ORDEN_CUENTA")
	private BigDecimal numeroOrdenCuenta;

	@Column(name="USUARIO_ULT_MODIFICACION", length=10)
	private String usuarioUltModificacion;

	//bi-directional many-to-one association to Embargo
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_EMBARGO", nullable=false)
	private Embargo embargo;

	public CuentaEmbargo() {
	}

	public long getCodCuentaEmbargo() {
		return this.codCuentaEmbargo;
	}

	public void setCodCuentaEmbargo(long codCuentaEmbargo) {
		this.codCuentaEmbargo = codCuentaEmbargo;
	}

	public String getActuacion() {
		return this.actuacion;
	}

	public void setActuacion(String actuacion) {
		this.actuacion = actuacion;
	}

	public String getClaveSeguridad() {
		return this.claveSeguridad;
	}

	public void setClaveSeguridad(String claveSeguridad) {
		this.claveSeguridad = claveSeguridad;
	}

	public String getCuenta() {
		return this.cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public BigDecimal getFUltimaModificacion() {
		return this.fUltimaModificacion;
	}

	public void setFUltimaModificacion(BigDecimal fUltimaModificacion) {
		this.fUltimaModificacion = fUltimaModificacion;
	}

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public BigDecimal getImporte() {
		return this.importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public BigDecimal getNumeroOrdenCuenta() {
		return this.numeroOrdenCuenta;
	}

	public void setNumeroOrdenCuenta(BigDecimal numeroOrdenCuenta) {
		this.numeroOrdenCuenta = numeroOrdenCuenta;
	}

	public String getUsuarioUltModificacion() {
		return this.usuarioUltModificacion;
	}

	public void setUsuarioUltModificacion(String usuarioUltModificacion) {
		this.usuarioUltModificacion = usuarioUltModificacion;
	}

	public Embargo getEmbargo() {
		return this.embargo;
	}

	public void setEmbargo(Embargo embargo) {
		this.embargo = embargo;
	}

}