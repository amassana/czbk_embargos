package es.commerzbank.ice.embargos.domain.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * The persistent class for the FICHERO_FINAL database table.
 * 
 */
@Entity
@Table(name="FICHERO_FINAL")
@NamedQuery(name="FicheroFinal.findAll", query="SELECT f FROM FicheroFinal f")
public class FicheroFinal implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "fichero_final_seq_gen", sequenceName = "SEC_FICHERO_FINAL", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fichero_final_seq_gen")
	@Column(name="COD_FICHERO_FINAL", unique=true, nullable=false)
	private long codFicheroFinal;

	@Column(name="COD_FICHERO_DILIGENCIAS", nullable=false)
	private Long codFicheroDiligencias;

	@Column(name="F_ULTIMA_MODIFICACION", nullable=false, precision=14)
	private BigDecimal fUltimaModificacion;

	@Column(name="F_VALOR", nullable=false, precision=14)
	private BigDecimal fValor;

	@Column(nullable=false)
	private BigDecimal importe;

	@Column(name="IMPORTE_LEVANTADO")
	private BigDecimal importeLevantado;

	@Column(name="USUARIO_ULT_MODIFICACION", nullable=false, length=20)
	private String usuarioUltModificacion;

	//bi-directional many-to-one association to ControlFichero
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_CONTROL_FICHERO", nullable=false)
	private ControlFichero controlFichero;
	
	//bi-directional many-to-one association to EstadoContabilizacion
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_ESTADO_CONTABILIZACION", nullable=false)
	private EstadoContabilizacion estadoContabilizacion;

	public FicheroFinal() {
	}

	public long getCodFicheroFinal() {
		return this.codFicheroFinal;
	}

	public void setCodFicheroFinal(long codFicheroFinal) {
		this.codFicheroFinal = codFicheroFinal;
	}

	public Long getCodFicheroDiligencias() {
		return this.codFicheroDiligencias;
	}

	public void setCodFicheroDiligencias(Long codFicheroDiligencias) {
		this.codFicheroDiligencias = codFicheroDiligencias;
	}

	public BigDecimal getFUltimaModificacion() {
		return this.fUltimaModificacion;
	}

	public void setFUltimaModificacion(BigDecimal fUltimaModificacion) {
		this.fUltimaModificacion = fUltimaModificacion;
	}

	public BigDecimal getFValor() {
		return this.fValor;
	}

	public void setFValor(BigDecimal fValor) {
		this.fValor = fValor;
	}

	public BigDecimal getImporte() {
		return this.importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
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
	
	public EstadoContabilizacion getEstadoContabilizacion() {
		return this.estadoContabilizacion;
	}

	public void setEstadoContabilizacion(EstadoContabilizacion estadoContabilizacion) {
		this.estadoContabilizacion = estadoContabilizacion;
	}

	public BigDecimal getImporteLevantado() {
		return importeLevantado;
	}

	public void setImporteLevantado(BigDecimal importeLevantado) {
		this.importeLevantado = importeLevantado;
	}
}