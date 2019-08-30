package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the H_TRABAS database table.
 * 
 */
@Entity
@Table(name="H_TRABAS")
@NamedQuery(name="HTraba.findAll", query="SELECT h FROM HTraba h")
public class HTraba implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private HTrabaPK id;

	@Column(name="COD_CUENTA_INMOVILIZACION")
	private BigDecimal codCuentaInmovilizacion;

	@Column(name="COD_CUENTA_RECAUDACION")
	private BigDecimal codCuentaRecaudacion;

	@Column(name="COD_ESTADO")
	private BigDecimal codEstado;

	@Column(name="F_ULTIMA_MODIFICACION")
	private BigDecimal fUltimaModificacion;

	@Column(name="FECHA_LIMITE")
	private BigDecimal fechaLimite;

	@Column(name="FECHA_TRABA")
	private BigDecimal fechaTraba;

	@Column(name="IMPORTE_TRABADO")
	private BigDecimal importeTrabado;

	@Column(name="IND_MAS_CUENTAS")
	private BigDecimal indMasCuentas;

	@Column(name="IND_MODIFICADO")
	private String indModificado;

	@Column(name="USUARIO_ULT_MODIFICACION")
	private String usuarioUltModificacion;

	//bi-directional many-to-one association to HEmbargo
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="COD_AUDITORIA", referencedColumnName="COD_AUDITORIA", insertable=false, updatable=false),
		@JoinColumn(name="COD_EMBARGO", referencedColumnName="COD_EMBARGO", insertable=false, updatable=false)
		})
	private HEmbargo HEmbargo;

	public HTraba() {
	}

	public HTrabaPK getId() {
		return this.id;
	}

	public void setId(HTrabaPK id) {
		this.id = id;
	}

	public BigDecimal getCodCuentaInmovilizacion() {
		return this.codCuentaInmovilizacion;
	}

	public void setCodCuentaInmovilizacion(BigDecimal codCuentaInmovilizacion) {
		this.codCuentaInmovilizacion = codCuentaInmovilizacion;
	}

	public BigDecimal getCodCuentaRecaudacion() {
		return this.codCuentaRecaudacion;
	}

	public void setCodCuentaRecaudacion(BigDecimal codCuentaRecaudacion) {
		this.codCuentaRecaudacion = codCuentaRecaudacion;
	}

	public BigDecimal getCodEstado() {
		return this.codEstado;
	}

	public void setCodEstado(BigDecimal codEstado) {
		this.codEstado = codEstado;
	}

	public BigDecimal getFUltimaModificacion() {
		return this.fUltimaModificacion;
	}

	public void setFUltimaModificacion(BigDecimal fUltimaModificacion) {
		this.fUltimaModificacion = fUltimaModificacion;
	}

	public BigDecimal getFechaLimite() {
		return this.fechaLimite;
	}

	public void setFechaLimite(BigDecimal fechaLimite) {
		this.fechaLimite = fechaLimite;
	}

	public BigDecimal getFechaTraba() {
		return this.fechaTraba;
	}

	public void setFechaTraba(BigDecimal fechaTraba) {
		this.fechaTraba = fechaTraba;
	}

	public BigDecimal getImporteTrabado() {
		return this.importeTrabado;
	}

	public void setImporteTrabado(BigDecimal importeTrabado) {
		this.importeTrabado = importeTrabado;
	}

	public BigDecimal getIndMasCuentas() {
		return this.indMasCuentas;
	}

	public void setIndMasCuentas(BigDecimal indMasCuentas) {
		this.indMasCuentas = indMasCuentas;
	}

	public String getIndModificado() {
		return this.indModificado;
	}

	public void setIndModificado(String indModificado) {
		this.indModificado = indModificado;
	}

	public String getUsuarioUltModificacion() {
		return this.usuarioUltModificacion;
	}

	public void setUsuarioUltModificacion(String usuarioUltModificacion) {
		this.usuarioUltModificacion = usuarioUltModificacion;
	}

	public HEmbargo getHEmbargo() {
		return this.HEmbargo;
	}

	public void setHEmbargo(HEmbargo HEmbargo) {
		this.HEmbargo = HEmbargo;
	}

}