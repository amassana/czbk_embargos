package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="H_LEVANTAMIENTO_TRABA")
@NamedQuery(name="HLevantamientoTraba.findAll", query="SELECT h FROM HLevantamientoTraba h")
public class HLevantamientoTraba implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private HLevantamientoTrabaPK id;
	
	@Column(name = "COD_TRABA", nullable = false)
	private BigDecimal codTraba;
	
	@Column(name = "COD_CONTROL_FICHERO", nullable = false)
	private BigDecimal codControlFichero;
	
	@Column(name = "ESTADO_CONTABLE")
	private BigDecimal estadoContable;
	
	@Column(name = "ESTADO_EJECUTADO")
	private BigDecimal estadoEjecutado;
	
	@Column(name = "USUARIO_ULT_MODIFICACION", length = 10)
	private String usuarioUltModificacion;
	
	@Column(name = "F_ULTIMA_MODIFCACION", precision = 14)
	private BigDecimal fUltimaModificacion; 
	
	@Column(name = "COD_DEUDA_DEUDOR", length = 8)
	private String codDeudaDeudor;
	
	@Column(name = "IND_CASO_REVISADO", length = 1)
	private String indCasoRevisado;

	public HLevantamientoTrabaPK getId() {
		return id;
	}

	public void setId(HLevantamientoTrabaPK id) {
		this.id = id;
	}

	public BigDecimal getCodTraba() {
		return codTraba;
	}

	public void setCodTraba(BigDecimal codTraba) {
		this.codTraba = codTraba;
	}

	public BigDecimal getCodControlFichero() {
		return codControlFichero;
	}

	public void setCodControlFichero(BigDecimal codControlFichero) {
		this.codControlFichero = codControlFichero;
	}

	public BigDecimal getEstadoContable() {
		return estadoContable;
	}

	public void setEstadoContable(BigDecimal estadoContable) {
		this.estadoContable = estadoContable;
	}

	public BigDecimal getEstadoEjecutado() {
		return estadoEjecutado;
	}

	public void setEstadoEjecutado(BigDecimal estadoEjecutado) {
		this.estadoEjecutado = estadoEjecutado;
	}

	public String getUsuarioUltModificacion() {
		return usuarioUltModificacion;
	}

	public void setUsuarioUltModificacion(String usuarioUltModificacion) {
		this.usuarioUltModificacion = usuarioUltModificacion;
	}

	public BigDecimal getfUltimaModificacion() {
		return fUltimaModificacion;
	}

	public void setfUltimaModificacion(BigDecimal fUltimaModificacion) {
		this.fUltimaModificacion = fUltimaModificacion;
	}

	public String getCodDeudaDeudor() {
		return codDeudaDeudor;
	}

	public void setCodDeudaDeudor(String codDeudaDeudor) {
		this.codDeudaDeudor = codDeudaDeudor;
	}

	public String getIndCasoRevisado() {
		return indCasoRevisado;
	}

	public void setIndCasoRevisado(String indCasoRevisado) {
		this.indCasoRevisado = indCasoRevisado;
	}
}
