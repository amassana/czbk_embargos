package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;

import es.commerzbank.ice.embargos.domain.entity.listener.ResultadoEmbargoListener;

import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the RESULTADO_EMBARGO database table.
 * 
 */
@Entity
@Table(name="RESULTADO_EMBARGO")
@NamedQuery(name="ResultadoEmbargo.findAll", query="SELECT r FROM ResultadoEmbargo r")
@EntityListeners(ResultadoEmbargoListener.class)
public class ResultadoEmbargo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "resultado_embargo_seq_gen", sequenceName = "SEC_RESULTADO_EMBARGO", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resultado_embargo_seq_gen")
	@Column(name="COD_RESULTADO_EMBARGO", unique=true, nullable=false)
	private long codResultadoEmbargo;

	@Column(name="TOTAL_LEVANTADO")
	private BigDecimal totalLevantado;

	@Column(name="TOTAL_NETO")
	private BigDecimal totalNeto;

	@Column(name="F_ULTIMA_MODIFICACION", precision=14)
	private BigDecimal fUltimaModificacion;
	
	@Column(name="USUARIO_ULT_MODIFICACION", length=10)
	private String usuarioUltModificacion;
	
	//bi-directional many-to-one association to CuentaResultadoEmbargo
	@OneToMany(mappedBy="resultadoEmbargo")
	private List<CuentaResultadoEmbargo> cuentaResultadoEmbargos;

	//bi-directional many-to-one association to Embargo
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_EMBARGO", nullable=false)
	private Embargo embargo;

	//bi-directional many-to-one association to EstadoResultadoEmbargo
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_ESTADO_RESULTADO_EMBARGO")
	private EstadoResultadoEmbargo estadoResultadoEmbargo;
	
	//bi-directional many-to-one association to EstadoResultadoEmbargo
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_CONTROL_FICHERO")
	private ControlFichero controlFichero;

	//bi-directional many-to-one association to Traba
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_TRABA", nullable=false)
	private Traba traba;

	public ResultadoEmbargo() {
	}

	public long getCodResultadoEmbargo() {
		return this.codResultadoEmbargo;
	}

	public void setCodResultadoEmbargo(long codResultadoEmbargo) {
		this.codResultadoEmbargo = codResultadoEmbargo;
	}

	public BigDecimal getTotalLevantado() {
		return this.totalLevantado;
	}

	public void setTotalLevantado(BigDecimal totalLevantado) {
		this.totalLevantado = totalLevantado;
	}

	public BigDecimal getTotalNeto() {
		return this.totalNeto;
	}

	public void setTotalNeto(BigDecimal totalNeto) {
		this.totalNeto = totalNeto;
	}

	public List<CuentaResultadoEmbargo> getCuentaResultadoEmbargos() {
		return this.cuentaResultadoEmbargos;
	}

	public void setCuentaResultadoEmbargos(List<CuentaResultadoEmbargo> cuentaResultadoEmbargos) {
		this.cuentaResultadoEmbargos = cuentaResultadoEmbargos;
	}

	public CuentaResultadoEmbargo addCuentaResultadoEmbargo(CuentaResultadoEmbargo cuentaResultadoEmbargo) {
		getCuentaResultadoEmbargos().add(cuentaResultadoEmbargo);
		cuentaResultadoEmbargo.setResultadoEmbargo(this);

		return cuentaResultadoEmbargo;
	}

	public CuentaResultadoEmbargo removeCuentaResultadoEmbargo(CuentaResultadoEmbargo cuentaResultadoEmbargo) {
		getCuentaResultadoEmbargos().remove(cuentaResultadoEmbargo);
		cuentaResultadoEmbargo.setResultadoEmbargo(null);

		return cuentaResultadoEmbargo;
	}

	public Embargo getEmbargo() {
		return this.embargo;
	}

	public void setEmbargo(Embargo embargo) {
		this.embargo = embargo;
	}

	public EstadoResultadoEmbargo getEstadoResultadoEmbargo() {
		return this.estadoResultadoEmbargo;
	}

	public void setEstadoResultadoEmbargo(EstadoResultadoEmbargo estadoResultadoEmbargo) {
		this.estadoResultadoEmbargo = estadoResultadoEmbargo;
	}

	public Traba getTraba() {
		return this.traba;
	}

	public void setTraba(Traba traba) {
		this.traba = traba;
	}

	public ControlFichero getControlFichero() {
		return controlFichero;
	}

	public void setControlFichero(ControlFichero controlFichero) {
		this.controlFichero = controlFichero;
	}

	public BigDecimal getfUltimaModificacion() {
		return fUltimaModificacion;
	}

	public void setfUltimaModificacion(BigDecimal fUltimaModificacion) {
		this.fUltimaModificacion = fUltimaModificacion;
	}

	public String getUsuarioUltModificacion() {
		return usuarioUltModificacion;
	}

	public void setUsuarioUltModificacion(String usuarioUltModificacion) {
		this.usuarioUltModificacion = usuarioUltModificacion;
	}

}