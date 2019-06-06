package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the HISTORIAL_ESTADO_TRABA database table.
 * 
 */
@Entity
@Table(name="HISTORIAL_ESTADO_TRABA")
@NamedQuery(name="HistorialEstadoTraba.findAll", query="SELECT h FROM HistorialEstadoTraba h")
public class HistorialEstadoTraba implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private HistorialEstadoTrabaPK id;

	@Column(name="F_ULTIMA_MODIFICACION", precision=14)
	private BigDecimal fUltimaModificacion;

	@Column(name="FECHA_ESTADO", precision=14)
	private BigDecimal fechaEstado;

	@Column(name="USUARIO_ULT_MODIFICACION", length=10)
	private String usuarioUltModificacion;

	//bi-directional many-to-one association to EstadoTraba
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_ESTADO", nullable=false)
	private EstadoTraba estadoTraba;

	//bi-directional many-to-one association to Traba
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_TRABA", nullable=false, insertable=false, updatable=false)
	private Traba traba;

	public HistorialEstadoTraba() {
	}

	public HistorialEstadoTrabaPK getId() {
		return this.id;
	}

	public void setId(HistorialEstadoTrabaPK id) {
		this.id = id;
	}

	public BigDecimal getFUltimaModificacion() {
		return this.fUltimaModificacion;
	}

	public void setFUltimaModificacion(BigDecimal fUltimaModificacion) {
		this.fUltimaModificacion = fUltimaModificacion;
	}

	public BigDecimal getFechaEstado() {
		return this.fechaEstado;
	}

	public void setFechaEstado(BigDecimal fechaEstado) {
		this.fechaEstado = fechaEstado;
	}

	public String getUsuarioUltModificacion() {
		return this.usuarioUltModificacion;
	}

	public void setUsuarioUltModificacion(String usuarioUltModificacion) {
		this.usuarioUltModificacion = usuarioUltModificacion;
	}

	public EstadoTraba getEstadoTraba() {
		return this.estadoTraba;
	}

	public void setEstadoTraba(EstadoTraba estadoTraba) {
		this.estadoTraba = estadoTraba;
	}

	public Traba getTraba() {
		return this.traba;
	}

	public void setTraba(Traba traba) {
		this.traba = traba;
	}

}