package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the APUNTE_CONTABLE database table.
 * 
 */
@Entity
@Table(name="APUNTE_CONTABLE")
@NamedQuery(name="ApunteContable.findAll", query="SELECT a FROM ApunteContable a")
public class ApunteContable implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ApunteContablePK id;

	@Column(name="CUENTA_DEBE", length=20)
	private String cuentaDebe;

	@Column(name="CUENTA_HABER", length=20)
	private String cuentaHaber;

	@Column(name="F_ULTIMA_MODIFICACION", precision=14)
	private BigDecimal fUltimaModificacion;

	@Column(name="FECHA_VALOR", precision=14)
	private BigDecimal fechaValor;

	private BigDecimal importe;

	@Column(name="NUMERO_ORDEN_CUENTA")
	private BigDecimal numeroOrdenCuenta;

	@Column(name="SECUENCIAL_DIARIO")
	private BigDecimal secuencialDiario;

	@Column(name="TIME_STAMP", precision=14)
	private BigDecimal timeStamp;

	@Column(name="USUARIO_ULT_MODIFICACION", length=10)
	private String usuarioUltModificacion;

	//bi-directional many-to-one association to ControlFichero
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_CONTROL_FICHERO", nullable=false)
	private ControlFichero controlFichero;

	//bi-directional many-to-one association to Traba
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_TRABA")
	private Traba traba;

	public ApunteContable() {
	}

	public ApunteContablePK getId() {
		return this.id;
	}

	public void setId(ApunteContablePK id) {
		this.id = id;
	}

	public String getCuentaDebe() {
		return this.cuentaDebe;
	}

	public void setCuentaDebe(String cuentaDebe) {
		this.cuentaDebe = cuentaDebe;
	}

	public String getCuentaHaber() {
		return this.cuentaHaber;
	}

	public void setCuentaHaber(String cuentaHaber) {
		this.cuentaHaber = cuentaHaber;
	}

	public BigDecimal getFUltimaModificacion() {
		return this.fUltimaModificacion;
	}

	public void setFUltimaModificacion(BigDecimal fUltimaModificacion) {
		this.fUltimaModificacion = fUltimaModificacion;
	}

	public BigDecimal getFechaValor() {
		return this.fechaValor;
	}

	public void setFechaValor(BigDecimal fechaValor) {
		this.fechaValor = fechaValor;
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

	public BigDecimal getSecuencialDiario() {
		return this.secuencialDiario;
	}

	public void setSecuencialDiario(BigDecimal secuencialDiario) {
		this.secuencialDiario = secuencialDiario;
	}

	public BigDecimal getTimeStamp() {
		return this.timeStamp;
	}

	public void setTimeStamp(BigDecimal timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getUsuarioUltModificacion() {
		return this.usuarioUltModificacion;
	}

	public void setUsuarioUltModificacion(String usuarioUltModificacion) {
		this.usuarioUltModificacion = usuarioUltModificacion;
	}

	public ControlFichero getControlFichero() {
		return this.controlFichero;
	}

	public void setControlFichero(ControlFichero controlFichero) {
		this.controlFichero = controlFichero;
	}

	public Traba getTraba() {
		return this.traba;
	}

	public void setTraba(Traba traba) {
		this.traba = traba;
	}

}