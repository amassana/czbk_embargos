package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the PETICIONES database table.
 * 
 */
@Entity
@Table(name="PETICIONES")
@NamedQuery(name="Peticion.findAll", query="SELECT p FROM Peticion p")
public class Peticion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_PETICION", unique=true, nullable=false, length=16)
	private String codPeticion;

	@Column(name="ESTADO_SECUNDARIO_PETICION", length=16)
	private String estadoSecundarioPeticion;

	@Column(name="F_ULTIMA_MODIFICACION", precision=14)
	private BigDecimal fUltimaModificacion;

	@Column(name="LITERAL_ERROR", length=255)
	private String literalError;

	@Column(name="NUM_ELEMENTOS")
	private BigDecimal numElementos;

	@Column(name="TIEMPO_ESTIMADO_RESPUESTA")
	private BigDecimal tiempoEstimadoRespuesta;

	@Column(name="\"TIMESTAMP\"", length=29)
	private String timestamp;

	@Column(name="USUARIO_ULT_MODIFICACION", length=10)
	private String usuarioUltModificacion;

	//bi-directional many-to-one association to ControlFichero
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_CONTROL_FICHERO")
	private ControlFichero controlFichero;

	//bi-directional many-to-one association to EstadoIntPeticion
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_ESTADO_INT_PETICION")
	private EstadoIntPeticion estadoIntPeticion;

	//bi-directional many-to-one association to EstadoPrimarioPeticion
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_ESTADO_PRIMARIO_PETICION")
	private EstadoPrimarioPeticion estadoPrimarioPeticion;

	//bi-directional many-to-one association to EstadoPrimarioResp
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_ESTADO_PRIMARIO_RESP")
	private EstadoPrimarioResp estadoPrimarioResp;

	//bi-directional many-to-one association to EstadoSecundarioResp
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_ESTADO_SECUNDARIO_RESP")
	private EstadoSecundarioResp estadoSecundarioResp;

	//bi-directional many-to-one association to SolicitudesCancelacion
	@OneToMany(mappedBy="peticion")
	private List<SolicitudesCancelacion> solicitudesCancelacions;

	//bi-directional many-to-one association to SolicitudesEjecucion
	@OneToMany(mappedBy="peticion")
	private List<SolicitudesEjecucion> solicitudesEjecucions;

	//bi-directional many-to-one association to SolicitudesLevantamiento
	@OneToMany(mappedBy="peticion")
	private List<SolicitudesLevantamiento> solicitudesLevantamientos;

	//bi-directional many-to-one association to SolicitudesTraba
	@OneToMany(mappedBy="peticion")
	private List<SolicitudesTraba> solicitudesTrabas;

	public Peticion() {
	}

	public String getCodPeticion() {
		return this.codPeticion;
	}

	public void setCodPeticion(String codPeticion) {
		this.codPeticion = codPeticion;
	}

	public String getEstadoSecundarioPeticion() {
		return this.estadoSecundarioPeticion;
	}

	public void setEstadoSecundarioPeticion(String estadoSecundarioPeticion) {
		this.estadoSecundarioPeticion = estadoSecundarioPeticion;
	}

	public BigDecimal getFUltimaModificacion() {
		return this.fUltimaModificacion;
	}

	public void setFUltimaModificacion(BigDecimal fUltimaModificacion) {
		this.fUltimaModificacion = fUltimaModificacion;
	}

	public String getLiteralError() {
		return this.literalError;
	}

	public void setLiteralError(String literalError) {
		this.literalError = literalError;
	}

	public BigDecimal getNumElementos() {
		return this.numElementos;
	}

	public void setNumElementos(BigDecimal numElementos) {
		this.numElementos = numElementos;
	}

	public BigDecimal getTiempoEstimadoRespuesta() {
		return this.tiempoEstimadoRespuesta;
	}

	public void setTiempoEstimadoRespuesta(BigDecimal tiempoEstimadoRespuesta) {
		this.tiempoEstimadoRespuesta = tiempoEstimadoRespuesta;
	}

	public String getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
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

	public EstadoIntPeticion getEstadoIntPeticion() {
		return this.estadoIntPeticion;
	}

	public void setEstadoIntPeticion(EstadoIntPeticion estadoIntPeticion) {
		this.estadoIntPeticion = estadoIntPeticion;
	}

	public EstadoPrimarioPeticion getEstadoPrimarioPeticion() {
		return this.estadoPrimarioPeticion;
	}

	public void setEstadoPrimarioPeticion(EstadoPrimarioPeticion estadoPrimarioPeticion) {
		this.estadoPrimarioPeticion = estadoPrimarioPeticion;
	}

	public EstadoPrimarioResp getEstadoPrimarioResp() {
		return this.estadoPrimarioResp;
	}

	public void setEstadoPrimarioResp(EstadoPrimarioResp estadoPrimarioResp) {
		this.estadoPrimarioResp = estadoPrimarioResp;
	}

	public EstadoSecundarioResp getEstadoSecundarioResp() {
		return this.estadoSecundarioResp;
	}

	public void setEstadoSecundarioResp(EstadoSecundarioResp estadoSecundarioResp) {
		this.estadoSecundarioResp = estadoSecundarioResp;
	}

	public List<SolicitudesCancelacion> getSolicitudesCancelacions() {
		return this.solicitudesCancelacions;
	}

	public void setSolicitudesCancelacions(List<SolicitudesCancelacion> solicitudesCancelacions) {
		this.solicitudesCancelacions = solicitudesCancelacions;
	}

	public SolicitudesCancelacion addSolicitudesCancelacion(SolicitudesCancelacion solicitudesCancelacion) {
		getSolicitudesCancelacions().add(solicitudesCancelacion);
		solicitudesCancelacion.setPeticion(this);

		return solicitudesCancelacion;
	}

	public SolicitudesCancelacion removeSolicitudesCancelacion(SolicitudesCancelacion solicitudesCancelacion) {
		getSolicitudesCancelacions().remove(solicitudesCancelacion);
		solicitudesCancelacion.setPeticion(null);

		return solicitudesCancelacion;
	}

	public List<SolicitudesEjecucion> getSolicitudesEjecucions() {
		return this.solicitudesEjecucions;
	}

	public void setSolicitudesEjecucions(List<SolicitudesEjecucion> solicitudesEjecucions) {
		this.solicitudesEjecucions = solicitudesEjecucions;
	}

	public SolicitudesEjecucion addSolicitudesEjecucion(SolicitudesEjecucion solicitudesEjecucion) {
		getSolicitudesEjecucions().add(solicitudesEjecucion);
		solicitudesEjecucion.setPeticion(this);

		return solicitudesEjecucion;
	}

	public SolicitudesEjecucion removeSolicitudesEjecucion(SolicitudesEjecucion solicitudesEjecucion) {
		getSolicitudesEjecucions().remove(solicitudesEjecucion);
		solicitudesEjecucion.setPeticion(null);

		return solicitudesEjecucion;
	}

	public List<SolicitudesLevantamiento> getSolicitudesLevantamientos() {
		return this.solicitudesLevantamientos;
	}

	public void setSolicitudesLevantamientos(List<SolicitudesLevantamiento> solicitudesLevantamientos) {
		this.solicitudesLevantamientos = solicitudesLevantamientos;
	}

	public SolicitudesLevantamiento addSolicitudesLevantamiento(SolicitudesLevantamiento solicitudesLevantamiento) {
		getSolicitudesLevantamientos().add(solicitudesLevantamiento);
		solicitudesLevantamiento.setPeticion(this);

		return solicitudesLevantamiento;
	}

	public SolicitudesLevantamiento removeSolicitudesLevantamiento(SolicitudesLevantamiento solicitudesLevantamiento) {
		getSolicitudesLevantamientos().remove(solicitudesLevantamiento);
		solicitudesLevantamiento.setPeticion(null);

		return solicitudesLevantamiento;
	}

	public List<SolicitudesTraba> getSolicitudesTrabas() {
		return this.solicitudesTrabas;
	}

	public void setSolicitudesTrabas(List<SolicitudesTraba> solicitudesTrabas) {
		this.solicitudesTrabas = solicitudesTrabas;
	}

	public SolicitudesTraba addSolicitudesTraba(SolicitudesTraba solicitudesTraba) {
		getSolicitudesTrabas().add(solicitudesTraba);
		solicitudesTraba.setPeticion(this);

		return solicitudesTraba;
	}

	public SolicitudesTraba removeSolicitudesTraba(SolicitudesTraba solicitudesTraba) {
		getSolicitudesTrabas().remove(solicitudesTraba);
		solicitudesTraba.setPeticion(null);

		return solicitudesTraba;
	}

}