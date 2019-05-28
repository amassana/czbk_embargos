package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the SOLICITUDES_LEVANTAMIENTO database table.
 * 
 */
@Entity
@Table(name="SOLICITUDES_LEVANTAMIENTO")
@NamedQuery(name="SolicitudesLevantamiento.findAll", query="SELECT s FROM SolicitudesLevantamiento s")
public class SolicitudesLevantamiento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_SOLICITUD_LEVANTAMIENTO", unique=true, nullable=false, length=40)
	private String codSolicitudLevantamiento;

	@Column(name="APELLIDO1_TITULAR", length=40)
	private String apellido1Titular;

	@Column(name="APELLIDO2_TITULAR", length=40)
	private String apellido2Titular;

	@Column(name="COD_TRABA")
	private BigDecimal codTraba;

	@Column(name="CODIGO_CERTIFICADO", length=64)
	private String codigoCertificado;

	@Column(length=20)
	private String consentimiento;

	@Column(length=14)
	private String documentacion;

	@Column(name="F_ULTIMA_MODIFICACION", precision=14)
	private BigDecimal fUltimaModificacion;

	@Column(name="FECHA_GENERACION", length=29)
	private String fechaGeneracion;

	@Column(length=250)
	private String finalidad;

	@Column(name="ID_TRANSMISION", length=26)
	private String idTransmision;

	@Column(name="IDENTIFICADOR_SOLICITANTE", length=10)
	private String identificadorSolicitante;

	@Column(name="IMPORTE_RESPUESTA", length=13)
	private String importeRespuesta;

	@Column(name="IMPORTE_SOLICITUD", length=13)
	private String importeSolicitud;

	@Column(name="INTERVENCION_USUARIO", length=1)
	private String intervencionUsuario;

	@Column(name="NIF_EMISOR", length=10)
	private String nifEmisor;

	@Column(name="NIF_FUNCIONARIO", length=10)
	private String nifFuncionario;

	@Column(name="NOMBRE_COMPLETO_FUNCIONARIO", length=122)
	private String nombreCompletoFuncionario;

	@Column(name="NOMBRE_COMPLETO_TITULAR", length=122)
	private String nombreCompletoTitular;

	@Column(name="NOMBRE_EMISOR", length=50)
	private String nombreEmisor;

	@Column(name="NOMBRE_SOLICITANTE", length=50)
	private String nombreSolicitante;

	@Column(name="NOMBRE_TITULAR", length=40)
	private String nombreTitular;

	@Column(name="USUARIO_ULT_MODIFICACION", length=10)
	private String usuarioUltModificacion;

	//bi-directional many-to-one association to EstadoIntLevantamiento
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_ESTADO_INT_LEVANTAMIENTO")
	private EstadoIntLevantamiento estadoIntLevantamiento;

	//bi-directional many-to-one association to EstadoRespLevantamiento
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_ESTADO_RESP_LEVANTAMIENTO")
	private EstadoRespLevantamiento estadoRespLevantamiento;

	//bi-directional many-to-one association to Peticione
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_PETICION")
	private Peticion peticione;

	//bi-directional many-to-one association to TipoDocumentacion
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_TIPO_DOCUMENTACION")
	private TipoDocumentacion tipoDocumentacion;

	public SolicitudesLevantamiento() {
	}

	public String getCodSolicitudLevantamiento() {
		return this.codSolicitudLevantamiento;
	}

	public void setCodSolicitudLevantamiento(String codSolicitudLevantamiento) {
		this.codSolicitudLevantamiento = codSolicitudLevantamiento;
	}

	public String getApellido1Titular() {
		return this.apellido1Titular;
	}

	public void setApellido1Titular(String apellido1Titular) {
		this.apellido1Titular = apellido1Titular;
	}

	public String getApellido2Titular() {
		return this.apellido2Titular;
	}

	public void setApellido2Titular(String apellido2Titular) {
		this.apellido2Titular = apellido2Titular;
	}

	public BigDecimal getCodTraba() {
		return this.codTraba;
	}

	public void setCodTraba(BigDecimal codTraba) {
		this.codTraba = codTraba;
	}

	public String getCodigoCertificado() {
		return this.codigoCertificado;
	}

	public void setCodigoCertificado(String codigoCertificado) {
		this.codigoCertificado = codigoCertificado;
	}

	public String getConsentimiento() {
		return this.consentimiento;
	}

	public void setConsentimiento(String consentimiento) {
		this.consentimiento = consentimiento;
	}

	public String getDocumentacion() {
		return this.documentacion;
	}

	public void setDocumentacion(String documentacion) {
		this.documentacion = documentacion;
	}

	public BigDecimal getFUltimaModificacion() {
		return this.fUltimaModificacion;
	}

	public void setFUltimaModificacion(BigDecimal fUltimaModificacion) {
		this.fUltimaModificacion = fUltimaModificacion;
	}

	public String getFechaGeneracion() {
		return this.fechaGeneracion;
	}

	public void setFechaGeneracion(String fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}

	public String getFinalidad() {
		return this.finalidad;
	}

	public void setFinalidad(String finalidad) {
		this.finalidad = finalidad;
	}

	public String getIdTransmision() {
		return this.idTransmision;
	}

	public void setIdTransmision(String idTransmision) {
		this.idTransmision = idTransmision;
	}

	public String getIdentificadorSolicitante() {
		return this.identificadorSolicitante;
	}

	public void setIdentificadorSolicitante(String identificadorSolicitante) {
		this.identificadorSolicitante = identificadorSolicitante;
	}

	public String getImporteRespuesta() {
		return this.importeRespuesta;
	}

	public void setImporteRespuesta(String importeRespuesta) {
		this.importeRespuesta = importeRespuesta;
	}

	public String getImporteSolicitud() {
		return this.importeSolicitud;
	}

	public void setImporteSolicitud(String importeSolicitud) {
		this.importeSolicitud = importeSolicitud;
	}

	public String getIntervencionUsuario() {
		return this.intervencionUsuario;
	}

	public void setIntervencionUsuario(String intervencionUsuario) {
		this.intervencionUsuario = intervencionUsuario;
	}

	public String getNifEmisor() {
		return this.nifEmisor;
	}

	public void setNifEmisor(String nifEmisor) {
		this.nifEmisor = nifEmisor;
	}

	public String getNifFuncionario() {
		return this.nifFuncionario;
	}

	public void setNifFuncionario(String nifFuncionario) {
		this.nifFuncionario = nifFuncionario;
	}

	public String getNombreCompletoFuncionario() {
		return this.nombreCompletoFuncionario;
	}

	public void setNombreCompletoFuncionario(String nombreCompletoFuncionario) {
		this.nombreCompletoFuncionario = nombreCompletoFuncionario;
	}

	public String getNombreCompletoTitular() {
		return this.nombreCompletoTitular;
	}

	public void setNombreCompletoTitular(String nombreCompletoTitular) {
		this.nombreCompletoTitular = nombreCompletoTitular;
	}

	public String getNombreEmisor() {
		return this.nombreEmisor;
	}

	public void setNombreEmisor(String nombreEmisor) {
		this.nombreEmisor = nombreEmisor;
	}

	public String getNombreSolicitante() {
		return this.nombreSolicitante;
	}

	public void setNombreSolicitante(String nombreSolicitante) {
		this.nombreSolicitante = nombreSolicitante;
	}

	public String getNombreTitular() {
		return this.nombreTitular;
	}

	public void setNombreTitular(String nombreTitular) {
		this.nombreTitular = nombreTitular;
	}

	public String getUsuarioUltModificacion() {
		return this.usuarioUltModificacion;
	}

	public void setUsuarioUltModificacion(String usuarioUltModificacion) {
		this.usuarioUltModificacion = usuarioUltModificacion;
	}

	public EstadoIntLevantamiento getEstadoIntLevantamiento() {
		return this.estadoIntLevantamiento;
	}

	public void setEstadoIntLevantamiento(EstadoIntLevantamiento estadoIntLevantamiento) {
		this.estadoIntLevantamiento = estadoIntLevantamiento;
	}

	public EstadoRespLevantamiento getEstadoRespLevantamiento() {
		return this.estadoRespLevantamiento;
	}

	public void setEstadoRespLevantamiento(EstadoRespLevantamiento estadoRespLevantamiento) {
		this.estadoRespLevantamiento = estadoRespLevantamiento;
	}

	public Peticion getPeticione() {
		return this.peticione;
	}

	public void setPeticione(Peticion peticione) {
		this.peticione = peticione;
	}

	public TipoDocumentacion getTipoDocumentacion() {
		return this.tipoDocumentacion;
	}

	public void setTipoDocumentacion(TipoDocumentacion tipoDocumentacion) {
		this.tipoDocumentacion = tipoDocumentacion;
	}

}