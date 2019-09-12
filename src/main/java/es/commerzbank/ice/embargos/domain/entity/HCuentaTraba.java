package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the H_CUENTA_TRABA database table.
 * 
 */
@Entity
@Table(name="H_CUENTA_TRABA")
@NamedQuery(name="HCuentaTraba.findAll", query="SELECT h FROM HCuentaTraba h")
public class HCuentaTraba implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private HCuentaTrabaPK id;

	private BigDecimal cambio;

	@Column(name="COD_ESTADO")
	private BigDecimal codEstado;

	@Column(name="COD_TRABA")
	private BigDecimal codTraba;

	private String cuenta;

	@Column(name="F_ULTIMA_MODIFICACION")
	private BigDecimal fUltimaModificacion;

	private String iban;

	private BigDecimal importe;

	@Column(name="NUMERO_ORDEN_CUENTA")
	private BigDecimal numeroOrdenCuenta;

	@Column(name="USUARIO_ULT_MODIFICACION")
	private String usuarioUltModificacion;

	@Column(name="ORIGEN_EMB", length=1)
	private String origenEmb;
	
	@Column(name="AGREGAR_A_TRABA", length=1)
	private String agregarATraba;
	
	@Column(name="ESTADO_CUENTA", length=10)
	private String estadoCuenta;
	
	//bi-directional many-to-one association to CuentaTrabaActuacion
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ACTUACION", nullable=true)
	private CuentaTrabaActuacion cuentaTrabaActuacion;
	
	public HCuentaTraba() {
	}

	public HCuentaTrabaPK getId() {
		return this.id;
	}

	public void setId(HCuentaTrabaPK id) {
		this.id = id;
	}

	public CuentaTrabaActuacion getCuentaTrabaActuacion() {
		return cuentaTrabaActuacion;
	}

	public void setCuentaTrabaActuacion(CuentaTrabaActuacion cuentaTrabaActuacion) {
		this.cuentaTrabaActuacion = cuentaTrabaActuacion;
	}

	public BigDecimal getCambio() {
		return this.cambio;
	}

	public void setCambio(BigDecimal cambio) {
		this.cambio = cambio;
	}

	public BigDecimal getCodEstado() {
		return this.codEstado;
	}

	public void setCodEstado(BigDecimal codEstado) {
		this.codEstado = codEstado;
	}

	public BigDecimal getCodTraba() {
		return this.codTraba;
	}

	public void setCodTraba(BigDecimal codTraba) {
		this.codTraba = codTraba;
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

	
	public String getOrigenEmb() {
		return origenEmb;
	}

	public void setOrigenEmb(String origenEmb) {
		this.origenEmb = origenEmb;
	}

	public String getAgregarATraba() {
		return agregarATraba;
	}

	public void setAgregarATraba(String agregarATraba) {
		this.agregarATraba = agregarATraba;
	}

	public String getEstadoCuenta() {
		return estadoCuenta;
	}

	public void setEstadoCuenta(String estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}
}