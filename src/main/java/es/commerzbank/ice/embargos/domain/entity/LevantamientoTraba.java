package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;

import es.commerzbank.ice.embargos.domain.entity.listener.LevantamientoTrabaListener;

import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the LEVANTAMIENTO_TRABA database table.
 * 
 */
@Entity
@Table(name="LEVANTAMIENTO_TRABA")
@NamedQuery(name="LevantamientoTraba.findAll", query="SELECT l FROM LevantamientoTraba l")
@EntityListeners(LevantamientoTrabaListener.class)
public class LevantamientoTraba implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "levantamiento_traba_seq_gen", sequenceName = "SEC_LEVANTAMIENTO_TRABA", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "levantamiento_traba_seq_gen")
	@Column(name="COD_LEVANTAMIENTO", unique=true, nullable=false)
	private long codLevantamiento;

	@Column(name="COD_DEUDA_DEUDOR", length=8)
	private String codDeudaDeudor;

	@Column(name="ESTADO_CONTABLE")
	private BigDecimal estadoContable;

	@Column(name="ESTADO_EJECUTADO")
	private BigDecimal estadoEjecutado;
	
	@Column(name="IMPORTE_LEVANTADO")
	private BigDecimal importeLevantado;

	@Column(name="F_ULTIMA_MODIFICACION", precision=14)
	private BigDecimal fUltimaModificacion;

	@Column(name="USUARIO_ULT_MODIFICACION", length=10)
	private String usuarioUltModificacion;

	//bi-directional many-to-one association to CuentaLevantamiento
	@OneToMany(mappedBy="levantamientoTraba")
	private List<CuentaLevantamiento> cuentaLevantamientos;

	//bi-directional many-to-one association to ControlFichero
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_CONTROL_FICHERO", nullable=false)
	private ControlFichero controlFichero;

	//bi-directional many-to-one association to Traba
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_TRABA", nullable=false)
	private Traba traba;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ESTADO_LEVANTAMIENTO", nullable=false)
	private EstadoLevantamiento estadoLevantamiento;
	
	@Column(name="IND_CASO_REVISADO", length=1)
	private String indCasoRevisado;

	//bi-directional many-to-one association to DatosCliente
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="NIF")
	private DatosCliente datosCliente;
	
	public LevantamientoTraba() {
	}

	public long getCodLevantamiento() {
		return this.codLevantamiento;
	}

	public void setCodLevantamiento(long codLevantamiento) {
		this.codLevantamiento = codLevantamiento;
	}

	public String getCodDeudaDeudor() {
		return this.codDeudaDeudor;
	}

	public void setCodDeudaDeudor(String codDeudaDeudor) {
		this.codDeudaDeudor = codDeudaDeudor;
	}

	public BigDecimal getEstadoContable() {
		return this.estadoContable;
	}

	public void setEstadoContable(BigDecimal estadoContable) {
		this.estadoContable = estadoContable;
	}

	public BigDecimal getEstadoEjecutado() {
		return this.estadoEjecutado;
	}

	public void setEstadoEjecutado(BigDecimal estadoEjecutado) {
		this.estadoEjecutado = estadoEjecutado;
	}

	public BigDecimal getFUltimaModificacion() {
		return this.fUltimaModificacion;
	}

	public void setFUltimaModificacion(BigDecimal fUltimaModificacion) {
		this.fUltimaModificacion = fUltimaModificacion;
	}

	public String getUsuarioUltModificacion() {
		return this.usuarioUltModificacion;
	}

	public void setUsuarioUltModificacion(String usuarioUltModificacion) {
		this.usuarioUltModificacion = usuarioUltModificacion;
	}

	public List<CuentaLevantamiento> getCuentaLevantamientos() {
		return this.cuentaLevantamientos;
	}

	public void setCuentaLevantamientos(List<CuentaLevantamiento> cuentaLevantamientos) {
		this.cuentaLevantamientos = cuentaLevantamientos;
	}

	public CuentaLevantamiento addCuentaLevantamiento(CuentaLevantamiento cuentaLevantamiento) {
		getCuentaLevantamientos().add(cuentaLevantamiento);
		cuentaLevantamiento.setLevantamientoTraba(this);

		return cuentaLevantamiento;
	}

	public CuentaLevantamiento removeCuentaLevantamiento(CuentaLevantamiento cuentaLevantamiento) {
		getCuentaLevantamientos().remove(cuentaLevantamiento);
		cuentaLevantamiento.setLevantamientoTraba(null);

		return cuentaLevantamiento;
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

	public String getIndCasoRevisado() {
		return indCasoRevisado;
	}

	public void setIndCasoRevisado(String indCasoRevisado) {
		this.indCasoRevisado = indCasoRevisado;
	}

	public BigDecimal getImporteLevantado() {
		return importeLevantado;
	}

	public void setImporteLevantado(BigDecimal importeLevantado) {
		this.importeLevantado = importeLevantado;
	}

	public EstadoLevantamiento getEstadoLevantamiento() {
		return estadoLevantamiento;
	}

	public void setEstadoLevantamiento(EstadoLevantamiento estadoLevantamiento) {
		this.estadoLevantamiento = estadoLevantamiento;
	}

	public DatosCliente getDatosCliente() {
		return this.datosCliente;
	}

	public void setDatosCliente(DatosCliente datosCliente) {
		this.datosCliente = datosCliente;
	}
	
}