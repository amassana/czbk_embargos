package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the H_CONTROL_FICHERO database table.
 * 
 */
@Entity
@Table(name="H_CONTROL_FICHERO")
@NamedQuery(name="HControlFichero.findAll", query="SELECT h FROM HControlFichero h")
public class HControlFichero implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private HControlFicheroPK id;

	@Column(name="COD_CONTROL_FICHERO_ORIGEN")
	private BigDecimal codControlFicheroOrigen;

	@Column(name="COD_CONTROL_FICHERO_RESPUESTA")
	private BigDecimal codControlFicheroRespuesta;

	@Column(name="COD_ENTIDAD_PRESENTADORA", nullable=false)
	private BigDecimal codEntidadPresentadora;

	@Column(name="COD_ESTADO")
	private BigDecimal codEstado;

	@Column(name="COD_TAREA")
	private BigDecimal codTarea;

	@Column(name="COD_TIPO_FICHERO", nullable=false)
	private BigDecimal codTipoFichero;

	@Column(length=30)
	private String descripcion;

	@Column(name="F_ULTIMA_MODIFICACION", precision=14)
	private BigDecimal fUltimaModificacion;

	@Column(name="FECHA_COMIENZO_CICLO", precision=8)
	private BigDecimal fechaComienzoCiclo;

	@Column(name="FECHA_CREACION", precision=8)
	private BigDecimal fechaCreacion;

	@Column(name="FECHA_GENERACION_RESPUESTA")
	private BigDecimal fechaGeneracionRespuesta;

	@Column(name="FECHA_INCORPORACION", precision=14)
	private BigDecimal fechaIncorporacion;

	@Column(name="FECHA_MAXIMA_RESPUESTA")
	private BigDecimal fechaMaximaRespuesta;

	@Column(name="IND_6301", length=1)
	private String ind6301;

	@Column(name="IND_CGPJ", length=1)
	private String indCgpj;

	@Column(name="IND_PROCESADO", length=1)
	private String indProcesado;

	@Column(name="ISO_MONEDA", length=3)
	private String isoMoneda;

	@Column(name="NOMBRE_FICHERO", length=100)
	private String nombreFichero;

	@Column(name="NUM_CRC", length=30)
	private String numCrc;

	@Column(name="NUM_ENVIO", length=8)
	private String numEnvio;

	@Column(name="USUARIO_ULT_MODIFICACION", length=10)
	private String usuarioUltModificacion;

	public HControlFichero() {
	}

	public HControlFicheroPK getId() {
		return this.id;
	}

	public void setId(HControlFicheroPK id) {
		this.id = id;
	}

	public BigDecimal getCodControlFicheroOrigen() {
		return this.codControlFicheroOrigen;
	}

	public void setCodControlFicheroOrigen(BigDecimal codControlFicheroOrigen) {
		this.codControlFicheroOrigen = codControlFicheroOrigen;
	}

	public BigDecimal getCodControlFicheroRespuesta() {
		return this.codControlFicheroRespuesta;
	}

	public void setCodControlFicheroRespuesta(BigDecimal codControlFicheroRespuesta) {
		this.codControlFicheroRespuesta = codControlFicheroRespuesta;
	}

	public BigDecimal getCodEntidadPresentadora() {
		return this.codEntidadPresentadora;
	}

	public void setCodEntidadPresentadora(BigDecimal codEntidadPresentadora) {
		this.codEntidadPresentadora = codEntidadPresentadora;
	}

	public BigDecimal getCodEstado() {
		return this.codEstado;
	}

	public void setCodEstado(BigDecimal codEstado) {
		this.codEstado = codEstado;
	}

	public BigDecimal getCodTarea() {
		return this.codTarea;
	}

	public void setCodTarea(BigDecimal codTarea) {
		this.codTarea = codTarea;
	}

	public BigDecimal getCodTipoFichero() {
		return this.codTipoFichero;
	}

	public void setCodTipoFichero(BigDecimal codTipoFichero) {
		this.codTipoFichero = codTipoFichero;
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

	public BigDecimal getFechaComienzoCiclo() {
		return this.fechaComienzoCiclo;
	}

	public void setFechaComienzoCiclo(BigDecimal fechaComienzoCiclo) {
		this.fechaComienzoCiclo = fechaComienzoCiclo;
	}

	public BigDecimal getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(BigDecimal fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public BigDecimal getFechaGeneracionRespuesta() {
		return this.fechaGeneracionRespuesta;
	}

	public void setFechaGeneracionRespuesta(BigDecimal fechaGeneracionRespuesta) {
		this.fechaGeneracionRespuesta = fechaGeneracionRespuesta;
	}

	public BigDecimal getFechaIncorporacion() {
		return this.fechaIncorporacion;
	}

	public void setFechaIncorporacion(BigDecimal fechaIncorporacion) {
		this.fechaIncorporacion = fechaIncorporacion;
	}

	public BigDecimal getFechaMaximaRespuesta() {
		return this.fechaMaximaRespuesta;
	}

	public void setFechaMaximaRespuesta(BigDecimal fechaMaximaRespuesta) {
		this.fechaMaximaRespuesta = fechaMaximaRespuesta;
	}

	public String getInd6301() {
		return this.ind6301;
	}

	public void setInd6301(String ind6301) {
		this.ind6301 = ind6301;
	}

	public String getIndCgpj() {
		return this.indCgpj;
	}

	public void setIndCgpj(String indCgpj) {
		this.indCgpj = indCgpj;
	}

	public String getIndProcesado() {
		return this.indProcesado;
	}

	public void setIndProcesado(String indProcesado) {
		this.indProcesado = indProcesado;
	}

	public String getIsoMoneda() {
		return this.isoMoneda;
	}

	public void setIsoMoneda(String isoMoneda) {
		this.isoMoneda = isoMoneda;
	}

	public String getNombreFichero() {
		return this.nombreFichero;
	}

	public void setNombreFichero(String nombreFichero) {
		this.nombreFichero = nombreFichero;
	}

	public String getNumCrc() {
		return this.numCrc;
	}

	public void setNumCrc(String numCrc) {
		this.numCrc = numCrc;
	}

	public String getNumEnvio() {
		return this.numEnvio;
	}

	public void setNumEnvio(String numEnvio) {
		this.numEnvio = numEnvio;
	}

	public String getUsuarioUltModificacion() {
		return this.usuarioUltModificacion;
	}

	public void setUsuarioUltModificacion(String usuarioUltModificacion) {
		this.usuarioUltModificacion = usuarioUltModificacion;
	}

}