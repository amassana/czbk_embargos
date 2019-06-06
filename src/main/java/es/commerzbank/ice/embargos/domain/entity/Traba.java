package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the TRABAS database table.
 * 
 */
@Entity
@Table(name="TRABAS")
@NamedQuery(name="Traba.findAll", query="SELECT t FROM Traba t")
public class Traba implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_TRABA", unique=true, nullable=false)
	private long codTraba;

	@Column(name="F_ULTIMA_MODIFICACION", precision=14)
	private BigDecimal fUltimaModificacion;

	@Column(name="FECHA_LIMITE", precision=14)
	private BigDecimal fechaLimite;

	@Column(name="FECHA_TRABA", precision=8)
	private BigDecimal fechaTraba;

	@Column(name="IMPORTE_TRABADO")
	private BigDecimal importeTrabado;

	@Column(name="IND_MAS_CUENTAS")
	private BigDecimal indMasCuentas;

	@Column(name="IND_MODIFICADO", length=1)
	private String indModificado;

	@Column(name="USUARIO_ULT_MODIFICACION", length=10)
	private String usuarioUltModificacion;

	//bi-directional many-to-one association to ApunteContable
	@OneToMany(mappedBy="traba")
	private List<ApunteContable> apunteContables;

	//bi-directional many-to-many association to ControlFichero
	@ManyToMany
	@JoinTable(
		name="CONTROL_FICHERO_TRABA"
		, joinColumns={
			@JoinColumn(name="COD_TRABA", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="COD_CONTROL_FICHERO", nullable=false)
			}
		)
	private List<ControlFichero> controlFicheros;

	//bi-directional many-to-one association to CuentaTraba
	@OneToMany(mappedBy="traba")
	private List<CuentaTraba> cuentaTrabas;

	//bi-directional many-to-one association to ErrorTraba
	@OneToMany(mappedBy="traba")
	private List<ErrorTraba> errorTrabas;

	//bi-directional many-to-one association to HistorialEstadoTraba
	@OneToMany(mappedBy="traba")
	private List<HistorialEstadoTraba> historialEstadoTrabas;

	//bi-directional many-to-one association to LevantamientoTraba
	@OneToMany(mappedBy="traba")
	private List<LevantamientoTraba> levantamientoTrabas;

	//bi-directional many-to-one association to CuentasInmovilizacion
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_CUENTA_INMOVILIZACION", nullable=false)
	private CuentasInmovilizacion cuentasInmovilizacion;

	//bi-directional many-to-one association to CuentasRecaudacion
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_CUENTA_RECAUDACION", nullable=false)
	private CuentasRecaudacion cuentasRecaudacion;

	//bi-directional many-to-one association to Embargo
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_EMBARGO", nullable=false)
	private Embargo embargo;

	//bi-directional many-to-one association to EstadoTraba
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_ESTADO", nullable=false)
	private EstadoTraba estadoTraba;

	public Traba() {
	}

	public long getCodTraba() {
		return this.codTraba;
	}

	public void setCodTraba(long codTraba) {
		this.codTraba = codTraba;
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

	public List<ApunteContable> getApunteContables() {
		return this.apunteContables;
	}

	public void setApunteContables(List<ApunteContable> apunteContables) {
		this.apunteContables = apunteContables;
	}

	public ApunteContable addApunteContable(ApunteContable apunteContable) {
		getApunteContables().add(apunteContable);
		apunteContable.setTraba(this);

		return apunteContable;
	}

	public ApunteContable removeApunteContable(ApunteContable apunteContable) {
		getApunteContables().remove(apunteContable);
		apunteContable.setTraba(null);

		return apunteContable;
	}

	public List<ControlFichero> getControlFicheros() {
		return this.controlFicheros;
	}

	public void setControlFicheros(List<ControlFichero> controlFicheros) {
		this.controlFicheros = controlFicheros;
	}

	public List<CuentaTraba> getCuentaTrabas() {
		return this.cuentaTrabas;
	}

	public void setCuentaTrabas(List<CuentaTraba> cuentaTrabas) {
		this.cuentaTrabas = cuentaTrabas;
	}

	public CuentaTraba addCuentaTraba(CuentaTraba cuentaTraba) {
		getCuentaTrabas().add(cuentaTraba);
		cuentaTraba.setTraba(this);

		return cuentaTraba;
	}

	public CuentaTraba removeCuentaTraba(CuentaTraba cuentaTraba) {
		getCuentaTrabas().remove(cuentaTraba);
		cuentaTraba.setTraba(null);

		return cuentaTraba;
	}

	public List<ErrorTraba> getErrorTrabas() {
		return this.errorTrabas;
	}

	public void setErrorTrabas(List<ErrorTraba> errorTrabas) {
		this.errorTrabas = errorTrabas;
	}

	public ErrorTraba addErrorTraba(ErrorTraba errorTraba) {
		getErrorTrabas().add(errorTraba);
		errorTraba.setTraba(this);

		return errorTraba;
	}

	public ErrorTraba removeErrorTraba(ErrorTraba errorTraba) {
		getErrorTrabas().remove(errorTraba);
		errorTraba.setTraba(null);

		return errorTraba;
	}

	public List<HistorialEstadoTraba> getHistorialEstadoTrabas() {
		return this.historialEstadoTrabas;
	}

	public void setHistorialEstadoTrabas(List<HistorialEstadoTraba> historialEstadoTrabas) {
		this.historialEstadoTrabas = historialEstadoTrabas;
	}

	public HistorialEstadoTraba addHistorialEstadoTraba(HistorialEstadoTraba historialEstadoTraba) {
		getHistorialEstadoTrabas().add(historialEstadoTraba);
		historialEstadoTraba.setTraba(this);

		return historialEstadoTraba;
	}

	public HistorialEstadoTraba removeHistorialEstadoTraba(HistorialEstadoTraba historialEstadoTraba) {
		getHistorialEstadoTrabas().remove(historialEstadoTraba);
		historialEstadoTraba.setTraba(null);

		return historialEstadoTraba;
	}

	public List<LevantamientoTraba> getLevantamientoTrabas() {
		return this.levantamientoTrabas;
	}

	public void setLevantamientoTrabas(List<LevantamientoTraba> levantamientoTrabas) {
		this.levantamientoTrabas = levantamientoTrabas;
	}

	public LevantamientoTraba addLevantamientoTraba(LevantamientoTraba levantamientoTraba) {
		getLevantamientoTrabas().add(levantamientoTraba);
		levantamientoTraba.setTraba(this);

		return levantamientoTraba;
	}

	public LevantamientoTraba removeLevantamientoTraba(LevantamientoTraba levantamientoTraba) {
		getLevantamientoTrabas().remove(levantamientoTraba);
		levantamientoTraba.setTraba(null);

		return levantamientoTraba;
	}

	public CuentasInmovilizacion getCuentasInmovilizacion() {
		return this.cuentasInmovilizacion;
	}

	public void setCuentasInmovilizacion(CuentasInmovilizacion cuentasInmovilizacion) {
		this.cuentasInmovilizacion = cuentasInmovilizacion;
	}

	public CuentasRecaudacion getCuentasRecaudacion() {
		return this.cuentasRecaudacion;
	}

	public void setCuentasRecaudacion(CuentasRecaudacion cuentasRecaudacion) {
		this.cuentasRecaudacion = cuentasRecaudacion;
	}

	public Embargo getEmbargo() {
		return this.embargo;
	}

	public void setEmbargo(Embargo embargo) {
		this.embargo = embargo;
	}

	public EstadoTraba getEstadoTraba() {
		return this.estadoTraba;
	}

	public void setEstadoTraba(EstadoTraba estadoTraba) {
		this.estadoTraba = estadoTraba;
	}

}