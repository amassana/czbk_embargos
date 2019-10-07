package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the CONTROL_FICHERO database table.
 * 
 */
@Entity
@Table(name="CONTROL_FICHERO")
@NamedQuery(name="ControlFichero.findAll", query="SELECT c FROM ControlFichero c")
public class ControlFichero implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "control_fichero_seq_gen", sequenceName = "SEC_CONTROL_FICHERO", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "control_fichero_seq_gen")
	@Column(name="COD_CONTROL_FICHERO", unique=true, nullable=false)
	private long codControlFichero;

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

	@Column(name="RUTA_FICHERO", length=256)
	private String rutaFichero;

	@Column(name="NUM_CRC", length=30)
	private String numCrc;

	@Column(name="NUM_ENVIO", length=8)
	private String numEnvio;

	@Column(name="USUARIO_ULT_MODIFICACION", length=10)
	private String usuarioUltModificacion;

	@Column(name="COD_TAREA")
	private BigDecimal codTarea;
	
	//bi-directional many-to-one association to ApunteContable
	@OneToMany(mappedBy="controlFichero")
	private List<ApunteContable> apunteContables;

	//bi-directional many-to-one association to ControlFichero
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_CONTROL_FICHERO_RESPUESTA")
	private ControlFichero controlFicheroRespuesta;

	//bi-directional many-to-one association to ControlFichero
	@OneToMany(mappedBy="controlFicheroRespuesta")
	private List<ControlFichero> controlFicheroRespuestaList;

	//bi-directional many-to-one association to ControlFichero
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_CONTROL_FICHERO_ORIGEN")
	private ControlFichero controlFicheroOrigen;

	//bi-directional many-to-one association to ControlFichero
	@OneToMany(mappedBy="controlFicheroOrigen")
	private List<ControlFichero> controlFicheroOrigenList;

	//bi-directional many-to-one association to EntidadesComunicadora
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_ENTIDAD_PRESENTADORA")
	private EntidadesComunicadora entidadesComunicadora;

	//bi-directional many-to-one association to EstadoCtrlfichero
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="COD_ESTADO", referencedColumnName="COD_ESTADO", nullable=false),
		@JoinColumn(name="COD_TIPO_FICHERO", referencedColumnName="COD_TIPO_FICHERO", nullable=false)
		})
	private EstadoCtrlfichero estadoCtrlfichero;

	//bi-directional many-to-one association to TipoFichero
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_TIPO_FICHERO", nullable=false, insertable=false, updatable=false)
	private TipoFichero tipoFichero;

	//bi-directional many-to-many association to Traba
	@ManyToMany(mappedBy="controlFicheros")
	private List<Traba> trabas;

	//bi-directional many-to-one association to DetalleControlFichero
	@OneToMany(mappedBy="controlFichero")
	private List<DetalleControlFichero> detalleControlFicheros;

	//bi-directional many-to-one association to Embargo
	@OneToMany(mappedBy="controlFichero")
	private List<Embargo> embargos;

	//bi-directional many-to-one association to ErrorTraba
	@OneToMany(mappedBy="controlFichero")
	private List<ErrorTraba> errorTrabas;

	//bi-directional many-to-one association to FicheroFinal
	@OneToMany(mappedBy="controlFichero")
	private List<FicheroFinal> ficheroFinals;

	//bi-directional many-to-one association to LevantamientoTraba
	@OneToMany(mappedBy="controlFichero")
	private List<LevantamientoTraba> levantamientoTrabas;

	//bi-directional many-to-one association to Peticion
	@OneToMany(mappedBy="controlFichero")
	private List<Peticion> peticiones;

	//bi-directional many-to-one association to PeticionInformacion
	@OneToMany(mappedBy="controlFichero")
	private List<PeticionInformacion> peticionInformacions;

	//bi-directional many-to-one association to TareasPendiente
	@OneToMany(mappedBy="controlFichero")
	private List<TareasPendiente> tareasPendientes;

	//bi-directional many-to-one association to TareasRealizada
	@OneToMany(mappedBy="controlFichero")
	private List<TareasRealizada> tareasRealizadas;

	public ControlFichero() {
	}

	public long getCodControlFichero() {
		return this.codControlFichero;
	}

	public void setCodControlFichero(long codControlFichero) {
		this.codControlFichero = codControlFichero;
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

	public String getRutaFichero() {
		return rutaFichero;
	}

	public void setRutaFichero(String rutaFichero) {
		this.rutaFichero = rutaFichero;
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

	public BigDecimal getCodTarea() {
		return codTarea;
	}

	public void setCodTarea(BigDecimal codTarea) {
		this.codTarea = codTarea;
	}

	public List<ApunteContable> getApunteContables() {
		return this.apunteContables;
	}

	public void setApunteContables(List<ApunteContable> apunteContables) {
		this.apunteContables = apunteContables;
	}

	public ApunteContable addApunteContable(ApunteContable apunteContable) {
		getApunteContables().add(apunteContable);
		apunteContable.setControlFichero(this);

		return apunteContable;
	}

	public ApunteContable removeApunteContable(ApunteContable apunteContable) {
		getApunteContables().remove(apunteContable);
		apunteContable.setControlFichero(null);

		return apunteContable;
	}

	public ControlFichero getControlFicheroRespuesta() {
		return this.controlFicheroRespuesta;
	}

	public void setControlFicheroRespuesta(ControlFichero controlFicheroRespuesta) {
		this.controlFicheroRespuesta = controlFicheroRespuesta;
	}

	public List<ControlFichero> getControlFicheroRespuestaList() {
		return this.controlFicheroRespuestaList;
	}

	public void setControlFicheroRespuestaList(List<ControlFichero> controlFicheroRespuestaList) {
		this.controlFicheroRespuestaList = controlFicheroRespuestaList;
	}

	public ControlFichero addControlFicheroRespuestaList(ControlFichero controlFicheroRespuestaList) {
		getControlFicheroRespuestaList().add(controlFicheroRespuestaList);
		controlFicheroRespuestaList.setControlFicheroRespuesta(this);

		return controlFicheroRespuestaList;
	}

	public ControlFichero removeControlFicheroRespuestaList(ControlFichero controlFicheroRespuestaList) {
		getControlFicheroRespuestaList().remove(controlFicheroRespuestaList);
		controlFicheroRespuestaList.setControlFicheroRespuesta(null);

		return controlFicheroRespuestaList;
	}

	public ControlFichero getControlFicheroOrigen() {
		return this.controlFicheroOrigen;
	}

	public void setControlFicheroOrigen(ControlFichero controlFicheroOrigen) {
		this.controlFicheroOrigen = controlFicheroOrigen;
	}

	public List<ControlFichero> getControlFicheroOrigenList() {
		return this.controlFicheroOrigenList;
	}

	public void setControlFicheroOrigenList(List<ControlFichero> controlFicheroOrigenList) {
		this.controlFicheroOrigenList = controlFicheroOrigenList;
	}

	public ControlFichero addControlFicheroOrigenList(ControlFichero controlFicheroOrigenList) {
		getControlFicheroOrigenList().add(controlFicheroOrigenList);
		controlFicheroOrigenList.setControlFicheroOrigen(this);

		return controlFicheroOrigenList;
	}

	public ControlFichero removeControlFicheroOrigenList(ControlFichero controlFicheroOrigenList) {
		getControlFicheroOrigenList().remove(controlFicheroOrigenList);
		controlFicheroOrigenList.setControlFicheroOrigen(null);

		return controlFicheroOrigenList;
	}

	public EntidadesComunicadora getEntidadesComunicadora() {
		return this.entidadesComunicadora;
	}

	public void setEntidadesComunicadora(EntidadesComunicadora entidadesComunicadora) {
		this.entidadesComunicadora = entidadesComunicadora;
	}

	public EstadoCtrlfichero getEstadoCtrlfichero() {
		return this.estadoCtrlfichero;
	}

	public void setEstadoCtrlfichero(EstadoCtrlfichero estadoCtrlfichero) {
		this.estadoCtrlfichero = estadoCtrlfichero;
	}

	public TipoFichero getTipoFichero() {
		return this.tipoFichero;
	}

	public void setTipoFichero(TipoFichero tipoFichero) {
		this.tipoFichero = tipoFichero;
	}

	public List<Traba> getTrabas() {
		return this.trabas;
	}

	public void setTrabas(List<Traba> trabas) {
		this.trabas = trabas;
	}

	public List<DetalleControlFichero> getDetalleControlFicheros() {
		return this.detalleControlFicheros;
	}

	public void setDetalleControlFicheros(List<DetalleControlFichero> detalleControlFicheros) {
		this.detalleControlFicheros = detalleControlFicheros;
	}

	public DetalleControlFichero addDetalleControlFichero(DetalleControlFichero detalleControlFichero) {
		getDetalleControlFicheros().add(detalleControlFichero);
		detalleControlFichero.setControlFichero(this);

		return detalleControlFichero;
	}

	public DetalleControlFichero removeDetalleControlFichero(DetalleControlFichero detalleControlFichero) {
		getDetalleControlFicheros().remove(detalleControlFichero);
		detalleControlFichero.setControlFichero(null);

		return detalleControlFichero;
	}

	public List<Embargo> getEmbargos() {
		return this.embargos;
	}

	public void setEmbargos(List<Embargo> embargos) {
		this.embargos = embargos;
	}

	public Embargo addEmbargo(Embargo embargo) {
		getEmbargos().add(embargo);
		embargo.setControlFichero(this);

		return embargo;
	}

	public Embargo removeEmbargo(Embargo embargo) {
		getEmbargos().remove(embargo);
		embargo.setControlFichero(null);

		return embargo;
	}

	public List<ErrorTraba> getErrorTrabas() {
		return this.errorTrabas;
	}

	public void setErrorTrabas(List<ErrorTraba> errorTrabas) {
		this.errorTrabas = errorTrabas;
	}

	public ErrorTraba addErrorTraba(ErrorTraba errorTraba) {
		getErrorTrabas().add(errorTraba);
		errorTraba.setControlFichero(this);

		return errorTraba;
	}

	public ErrorTraba removeErrorTraba(ErrorTraba errorTraba) {
		getErrorTrabas().remove(errorTraba);
		errorTraba.setControlFichero(null);

		return errorTraba;
	}

	public List<FicheroFinal> getFicheroFinals() {
		return this.ficheroFinals;
	}

	public void setFicheroFinals(List<FicheroFinal> ficheroFinals) {
		this.ficheroFinals = ficheroFinals;
	}

	public FicheroFinal addFicheroFinal(FicheroFinal ficheroFinal) {
		getFicheroFinals().add(ficheroFinal);
		ficheroFinal.setControlFichero(this);

		return ficheroFinal;
	}

	public FicheroFinal removeFicheroFinal(FicheroFinal ficheroFinal) {
		getFicheroFinals().remove(ficheroFinal);
		ficheroFinal.setControlFichero(null);

		return ficheroFinal;
	}

	public List<LevantamientoTraba> getLevantamientoTrabas() {
		return this.levantamientoTrabas;
	}

	public void setLevantamientoTrabas(List<LevantamientoTraba> levantamientoTrabas) {
		this.levantamientoTrabas = levantamientoTrabas;
	}

	public LevantamientoTraba addLevantamientoTraba(LevantamientoTraba levantamientoTraba) {
		getLevantamientoTrabas().add(levantamientoTraba);
		levantamientoTraba.setControlFichero(this);

		return levantamientoTraba;
	}

	public LevantamientoTraba removeLevantamientoTraba(LevantamientoTraba levantamientoTraba) {
		getLevantamientoTrabas().remove(levantamientoTraba);
		levantamientoTraba.setControlFichero(null);

		return levantamientoTraba;
	}

	public List<Peticion> getPeticiones() {
		return this.peticiones;
	}

	public void setPeticiones(List<Peticion> peticiones) {
		this.peticiones = peticiones;
	}

	public Peticion addPeticione(Peticion peticione) {
		getPeticiones().add(peticione);
		peticione.setControlFichero(this);

		return peticione;
	}

	public Peticion removePeticione(Peticion peticion) {
		getPeticiones().remove(peticion);
		peticion.setControlFichero(null);

		return peticion;
	}

	public List<PeticionInformacion> getPeticionInformacions() {
		return this.peticionInformacions;
	}

	public void setPeticionInformacions(List<PeticionInformacion> peticionInformacions) {
		this.peticionInformacions = peticionInformacions;
	}

	public PeticionInformacion addPeticionInformacion(PeticionInformacion peticionInformacion) {
		getPeticionInformacions().add(peticionInformacion);
		peticionInformacion.setControlFichero(this);

		return peticionInformacion;
	}

	public PeticionInformacion removePeticionInformacion(PeticionInformacion peticionInformacion) {
		getPeticionInformacions().remove(peticionInformacion);
		peticionInformacion.setControlFichero(null);

		return peticionInformacion;
	}

	public List<TareasPendiente> getTareasPendientes() {
		return this.tareasPendientes;
	}

	public void setTareasPendientes(List<TareasPendiente> tareasPendientes) {
		this.tareasPendientes = tareasPendientes;
	}

	public TareasPendiente addTareasPendiente(TareasPendiente tareasPendiente) {
		getTareasPendientes().add(tareasPendiente);
		tareasPendiente.setControlFichero(this);

		return tareasPendiente;
	}

	public TareasPendiente removeTareasPendiente(TareasPendiente tareasPendiente) {
		getTareasPendientes().remove(tareasPendiente);
		tareasPendiente.setControlFichero(null);

		return tareasPendiente;
	}

	public List<TareasRealizada> getTareasRealizadas() {
		return this.tareasRealizadas;
	}

	public void setTareasRealizadas(List<TareasRealizada> tareasRealizadas) {
		this.tareasRealizadas = tareasRealizadas;
	}

	public TareasRealizada addTareasRealizada(TareasRealizada tareasRealizada) {
		getTareasRealizadas().add(tareasRealizada);
		tareasRealizada.setControlFichero(this);

		return tareasRealizada;
	}

	public TareasRealizada removeTareasRealizada(TareasRealizada tareasRealizada) {
		getTareasRealizadas().remove(tareasRealizada);
		tareasRealizada.setControlFichero(null);

		return tareasRealizada;
	}

}