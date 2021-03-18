package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;

import es.commerzbank.ice.embargos.domain.entity.listener.CuentaLevantamientoListener;

import java.math.BigDecimal;


/**
 * The persistent class for the CUENTA_LEVANTAMIENTO database table.
 * 
 */
@Entity
@Table(name="CUENTA_LEVANTAMIENTO")
@NamedQuery(name="CuentaLevantamiento.findAll", query="SELECT c FROM CuentaLevantamiento c")
@EntityListeners(CuentaLevantamientoListener.class)
public class CuentaLevantamiento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "cuenta_levantamiento_seq_gen", sequenceName = "SEC_CUENTA_LEVANTAMIENTO", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cuenta_levantamiento_seq_gen")
	@Column(name="COD_CUENTA_LEVANTAMIENTO", unique=true, nullable=false)
	private long codCuentaLevantamiento;

	@Column(length=2)
	private String actuacion;

	private BigDecimal cambio;

	@Column(name="COD_CONTROL_FICHERO")
	private BigDecimal codControlFichero;

	@Column(length=20)
	private String cuenta;

	@Column(name="F_ULTIMA_MODIFICACION", precision=14)
	private BigDecimal fUltimaModificacion;

	@Column(length=24)
	private String iban;

	private BigDecimal importe;

	@Column(name="IND_CONTABILIZADO", length=1)
	private String indContabilizado;
	
	@Column(name="COD_DIVISA", length=3)
	private String codDivisa;

	@Column(name="NUMERO_ORDEN_CUENTA")
	private BigDecimal numeroOrdenCuenta;
	
	@Column(name="ESTADO_CUENTA", length=10)
	private String estadoCuenta;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ESTADO", nullable=false)
	private EstadoLevantamiento estadoLevantamiento;

	@Column(name="USUARIO_ULT_MODIFICACION", length=10)
	private String usuarioUltModificacion;

	//bi-directional many-to-one association to LevantamientoTraba
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_LEVANTAMIENTO", nullable=false)
	private LevantamientoTraba levantamientoTraba;

	public CuentaLevantamiento() {
	}

	public long getCodCuentaLevantamiento() {
		return this.codCuentaLevantamiento;
	}

	public void setCodCuentaLevantamiento(long codCuentaLevantamiento) {
		this.codCuentaLevantamiento = codCuentaLevantamiento;
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

	public BigDecimal getCodControlFichero() {
		return this.codControlFichero;
	}

	public void setCodControlFichero(BigDecimal codControlFichero) {
		this.codControlFichero = codControlFichero;
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

	public String getIndContabilizado() {
		return this.indContabilizado;
	}

	public void setIndContabilizado(String indContabilizado) {
		this.indContabilizado = indContabilizado;
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

	public LevantamientoTraba getLevantamientoTraba() {
		return this.levantamientoTraba;
	}

	public void setLevantamientoTraba(LevantamientoTraba levantamientoTraba) {
		this.levantamientoTraba = levantamientoTraba;
	}

	public String getCodDivisa() {
		return codDivisa;
	}

	public void setCodDivisa(String codDivisa) {
		this.codDivisa = codDivisa;
	}

	public EstadoLevantamiento getEstadoLevantamiento() {
		return estadoLevantamiento;
	}

	public void setEstadoLevantamiento(EstadoLevantamiento estadoLevantamiento) {
		this.estadoLevantamiento = estadoLevantamiento;
	}

	/**
	 * @return the estadoCuenta
	 */
	public String getEstadoCuenta() {
		return estadoCuenta;
	}

	/**
	 * @param estadoCuenta the estadoCuenta to set
	 */
	public void setEstadoCuenta(String estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}

	
}