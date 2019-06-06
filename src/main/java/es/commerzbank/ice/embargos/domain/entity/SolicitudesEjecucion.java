package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the SOLICITUDES_EJECUCION database table.
 * 
 */
@Entity
@Table(name="SOLICITUDES_EJECUCION")
@NamedQuery(name="SolicitudesEjecucion.findAll", query="SELECT s FROM SolicitudesEjecucion s")
public class SolicitudesEjecucion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_SOLICITUD_EJECUCION", unique=true, nullable=false, length=40)
	private String codSolicitudEjecucion;

	@Column(name="APELLIDO1_TITULAR", length=40)
	private String apellido1Titular;

	@Column(name="APELLIDO2_TITULAR", length=40)
	private String apellido2Titular;

	@Column(length=70)
	private String beneficiario;

	@Column(length=11)
	private String bic;

	@Column(name="COD_TRABA")
	private BigDecimal codTraba;

	@Column(name="CODIGO_CERTIFICADO", length=64)
	private String codigoCertificado;

	@Column(length=140)
	private String concepto;

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

	@Column(length=24)
	private String iban;

	@Column(name="ID_ORDENANTE", length=14)
	private String idOrdenante;

	@Column(name="ID_TRANSMISION", length=26)
	private String idTransmision;

	@Column(name="IDENTIFICADOR_SOLICITANTE", length=10)
	private String identificadorSolicitante;

	@Column(name="IMPORTE_RESPUESTA", length=13)
	private String importeRespuesta;

	@Column(name="IMPORTE_SOLICITUD", length=13)
	private String importeSolicitud;

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

	@Column(length=70)
	private String ordenante;

	@Column(length=4)
	private String propositotr;

	@Column(length=4)
	private String tipotr;

	@Column(name="USUARIO_ULT_MODIFICACION", length=10)
	private String usuarioUltModificacion;

	//bi-directional many-to-one association to EstadoIntEjecucion
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_ESTADO_INT_EJECUCION")
	private EstadoIntEjecucion estadoIntEjecucion;

	//bi-directional many-to-one association to EstadoRespEjecucion
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_ESTADO_RESP_EJECUCION")
	private EstadoRespEjecucion estadoRespEjecucion;

	//bi-directional many-to-one association to Peticion
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_PETICION")
	private Peticion peticion;

	//bi-directional many-to-one association to TipoDocumentacion
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_TIPO_DOCUMENTACION")
	private TipoDocumentacion tipoDocumentacion;

	public SolicitudesEjecucion() {
	}

	public String getCodSolicitudEjecucion() {
		return this.codSolicitudEjecucion;
	}

	public void setCodSolicitudEjecucion(String codSolicitudEjecucion) {
		this.codSolicitudEjecucion = codSolicitudEjecucion;
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

	public String getBeneficiario() {
		return this.beneficiario;
	}

	public void setBeneficiario(String beneficiario) {
		this.beneficiario = beneficiario;
	}

	public String getBic() {
		return this.bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
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

	public String getConcepto() {
		return this.concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
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

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getIdOrdenante() {
		return this.idOrdenante;
	}

	public void setIdOrdenante(String idOrdenante) {
		this.idOrdenante = idOrdenante;
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

	public String getOrdenante() {
		return this.ordenante;
	}

	public void setOrdenante(String ordenante) {
		this.ordenante = ordenante;
	}

	public String getPropositotr() {
		return this.propositotr;
	}

	public void setPropositotr(String propositotr) {
		this.propositotr = propositotr;
	}

	public String getTipotr() {
		return this.tipotr;
	}

	public void setTipotr(String tipotr) {
		this.tipotr = tipotr;
	}

	public String getUsuarioUltModificacion() {
		return this.usuarioUltModificacion;
	}

	public void setUsuarioUltModificacion(String usuarioUltModificacion) {
		this.usuarioUltModificacion = usuarioUltModificacion;
	}

	public EstadoIntEjecucion getEstadoIntEjecucion() {
		return this.estadoIntEjecucion;
	}

	public void setEstadoIntEjecucion(EstadoIntEjecucion estadoIntEjecucion) {
		this.estadoIntEjecucion = estadoIntEjecucion;
	}

	public EstadoRespEjecucion getEstadoRespEjecucion() {
		return this.estadoRespEjecucion;
	}

	public void setEstadoRespEjecucion(EstadoRespEjecucion estadoRespEjecucion) {
		this.estadoRespEjecucion = estadoRespEjecucion;
	}

	public Peticion getPeticion() {
		return this.peticion;
	}

	public void setPeticion(Peticion peticion) {
		this.peticion = peticion;
	}

	public TipoDocumentacion getTipoDocumentacion() {
		return this.tipoDocumentacion;
	}

	public void setTipoDocumentacion(TipoDocumentacion tipoDocumentacion) {
		this.tipoDocumentacion = tipoDocumentacion;
	}

}