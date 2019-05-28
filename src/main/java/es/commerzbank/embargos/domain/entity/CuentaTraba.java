package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the CUENTA_TRABA database table.
 * 
 */
@Entity
@Table(name="CUENTA_TRABA")
@NamedQuery(name="CuentaTraba.findAll", query="SELECT c FROM CuentaTraba c")
public class CuentaTraba implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_CUENTA_TRABA", unique=true, nullable=false)
	private long codCuentaTraba;

	@Column(length=2)
	private String actuacion;

	private BigDecimal cambio;

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

	//bi-directional many-to-one association to EstadoTraba
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_ESTADO", nullable=false)
	private EstadoTraba estadoTraba;

	//bi-directional many-to-one association to Traba
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_TRABA", nullable=false)
	private Traba traba;

	public CuentaTraba() {
	}

	public long getCodCuentaTraba() {
		return this.codCuentaTraba;
	}

	public void setCodCuentaTraba(long codCuentaTraba) {
		this.codCuentaTraba = codCuentaTraba;
	}

	public String getActuacion() {
		return this.actuacion;
	}

	public void setActuacion(String actuacion) {
		this.actuacion = actuacion;
	}

	public BigDecimal getCambio() {
		return this.cambio;
	}

	public void setCambio(BigDecimal cambio) {
		this.cambio = cambio;
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

	public EstadoTraba getEstadoTraba() {
		return this.estadoTraba;
	}

	public void setEstadoTraba(EstadoTraba estadoTraba) {
		this.estadoTraba = estadoTraba;
	}

	public Traba getTraba() {
		return this.traba;
	}

	public void setTraba(Traba traba) {
		this.traba = traba;
	}

}