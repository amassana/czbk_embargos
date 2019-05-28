package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the CUENTAS_RECAUDACION database table.
 * 
 */
@Entity
@Table(name="CUENTAS_RECAUDACION")
@NamedQuery(name="CuentasRecaudacion.findAll", query="SELECT c FROM CuentasRecaudacion c")
public class CuentasRecaudacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_CUENTA_RECAUDACION", unique=true, nullable=false)
	private long codCuentaRecaudacion;

	@Column(length=4)
	private String banco;

	@Column(length=20)
	private String cuenta;

	@Column(length=2)
	private String dc;

	@Column(name="DESCRIPCION_CUENTA", length=40)
	private String descripcionCuenta;

	@Column(name="F_ULTIMA_MODIFICACION", precision=14)
	private BigDecimal fUltimaModificacion;

	@Column(name="IND_ACTIVA", length=1)
	private String indActiva;

	@Column(name="ISO_MONEDA", length=3)
	private String isoMoneda;

	@Column(length=4)
	private String sucursal;

	@Column(name="USUARIO_ULT_MODIFICACION", length=10)
	private String usuarioUltModificacion;

	//bi-directional many-to-one association to EntidadesComunicadora
	@OneToMany(mappedBy="cuentasRecaudacion")
	private List<EntidadesComunicadora> entidadesComunicadoras;

	//bi-directional many-to-one association to Traba
	@OneToMany(mappedBy="cuentasRecaudacion")
	private List<Traba> trabas;

	public CuentasRecaudacion() {
	}

	public long getCodCuentaRecaudacion() {
		return this.codCuentaRecaudacion;
	}

	public void setCodCuentaRecaudacion(long codCuentaRecaudacion) {
		this.codCuentaRecaudacion = codCuentaRecaudacion;
	}

	public String getBanco() {
		return this.banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getCuenta() {
		return this.cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getDc() {
		return this.dc;
	}

	public void setDc(String dc) {
		this.dc = dc;
	}

	public String getDescripcionCuenta() {
		return this.descripcionCuenta;
	}

	public void setDescripcionCuenta(String descripcionCuenta) {
		this.descripcionCuenta = descripcionCuenta;
	}

	public BigDecimal getFUltimaModificacion() {
		return this.fUltimaModificacion;
	}

	public void setFUltimaModificacion(BigDecimal fUltimaModificacion) {
		this.fUltimaModificacion = fUltimaModificacion;
	}

	public String getIndActiva() {
		return this.indActiva;
	}

	public void setIndActiva(String indActiva) {
		this.indActiva = indActiva;
	}

	public String getIsoMoneda() {
		return this.isoMoneda;
	}

	public void setIsoMoneda(String isoMoneda) {
		this.isoMoneda = isoMoneda;
	}

	public String getSucursal() {
		return this.sucursal;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public String getUsuarioUltModificacion() {
		return this.usuarioUltModificacion;
	}

	public void setUsuarioUltModificacion(String usuarioUltModificacion) {
		this.usuarioUltModificacion = usuarioUltModificacion;
	}

	public List<EntidadesComunicadora> getEntidadesComunicadoras() {
		return this.entidadesComunicadoras;
	}

	public void setEntidadesComunicadoras(List<EntidadesComunicadora> entidadesComunicadoras) {
		this.entidadesComunicadoras = entidadesComunicadoras;
	}

	public EntidadesComunicadora addEntidadesComunicadora(EntidadesComunicadora entidadesComunicadora) {
		getEntidadesComunicadoras().add(entidadesComunicadora);
		entidadesComunicadora.setCuentasRecaudacion(this);

		return entidadesComunicadora;
	}

	public EntidadesComunicadora removeEntidadesComunicadora(EntidadesComunicadora entidadesComunicadora) {
		getEntidadesComunicadoras().remove(entidadesComunicadora);
		entidadesComunicadora.setCuentasRecaudacion(null);

		return entidadesComunicadora;
	}

	public List<Traba> getTrabas() {
		return this.trabas;
	}

	public void setTrabas(List<Traba> trabas) {
		this.trabas = trabas;
	}

	public Traba addTraba(Traba traba) {
		getTrabas().add(traba);
		traba.setCuentasRecaudacion(this);

		return traba;
	}

	public Traba removeTraba(Traba traba) {
		getTrabas().remove(traba);
		traba.setCuentasRecaudacion(null);

		return traba;
	}

}