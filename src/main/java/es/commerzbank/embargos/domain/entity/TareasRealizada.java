package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the TAREAS_REALIZADAS database table.
 * 
 */
@Entity
@Table(name="TAREAS_REALIZADAS")
@NamedQuery(name="TareasRealizada.findAll", query="SELECT t FROM TareasRealizada t")
public class TareasRealizada implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_TAREA_REALIZADA", unique=true, nullable=false)
	private long codTareaRealizada;

	@Column(name="COD_SUCURSAL")
	private BigDecimal codSucursal;

	@Column(length=200)
	private String descripcion;

	@Column(name="F_ULTIMA_MODIFICACION", precision=14)
	private BigDecimal fUltimaModificacion;

	@Column(name="FECHA_EFECTIVA_REALIZACION", precision=14)
	private BigDecimal fechaEfectivaRealizacion;

	@Column(name="FECHA_INICIO_AVISO", precision=14)
	private BigDecimal fechaInicioAviso;

	@Column(name="FECHA_TOPE_REALIZACION", precision=14)
	private BigDecimal fechaTopeRealizacion;

	@Column(name="USUARIO_ULT_MODIFICACION", length=10)
	private String usuarioUltModificacion;

	//bi-directional many-to-one association to ControlFichero
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_CONTROL_FICHERO")
	private ControlFichero controlFichero;

	public TareasRealizada() {
	}

	public long getCodTareaRealizada() {
		return this.codTareaRealizada;
	}

	public void setCodTareaRealizada(long codTareaRealizada) {
		this.codTareaRealizada = codTareaRealizada;
	}

	public BigDecimal getCodSucursal() {
		return this.codSucursal;
	}

	public void setCodSucursal(BigDecimal codSucursal) {
		this.codSucursal = codSucursal;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public BigDecimal getFUltimaModificacion() {
		return this.fUltimaModificacion;
	}

	public void setFUltimaModificacion(BigDecimal fUltimaModificacion) {
		this.fUltimaModificacion = fUltimaModificacion;
	}

	public BigDecimal getFechaEfectivaRealizacion() {
		return this.fechaEfectivaRealizacion;
	}

	public void setFechaEfectivaRealizacion(BigDecimal fechaEfectivaRealizacion) {
		this.fechaEfectivaRealizacion = fechaEfectivaRealizacion;
	}

	public BigDecimal getFechaInicioAviso() {
		return this.fechaInicioAviso;
	}

	public void setFechaInicioAviso(BigDecimal fechaInicioAviso) {
		this.fechaInicioAviso = fechaInicioAviso;
	}

	public BigDecimal getFechaTopeRealizacion() {
		return this.fechaTopeRealizacion;
	}

	public void setFechaTopeRealizacion(BigDecimal fechaTopeRealizacion) {
		this.fechaTopeRealizacion = fechaTopeRealizacion;
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

}