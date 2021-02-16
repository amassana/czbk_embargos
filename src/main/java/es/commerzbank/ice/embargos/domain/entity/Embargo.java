
package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the EMBARGO database table.
 * 
 */
@Entity
@Table(name="EMBARGO")
@NamedQuery(name="Embargo.findAll", query="SELECT e FROM Embargo e")
public class Embargo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "embargo_seq_gen", sequenceName = "SEC_EMBARGO", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "embargo_seq_gen")
	@Column(name="COD_EMBARGO", unique=true, nullable=false)
	private long codEmbargo;

	@Column(name="COD_DEUDA_DEUDOR", length=8)
	private String codDeudaDeudor;

	@Column(name="COD_SUCURSAL")
	private BigDecimal codSucursal;

	@Column(name="CODIGO_POSTAL", length=5)
	private String codigoPostal;

	@Column(length=200)
	private String datregcomdet;

	@Column(length=39)
	private String domicilio;

	@Column(length=2)
	private String escalera;

	@Column(name="F_ULTIMA_MODIFICACION", precision=14)
	private BigDecimal fUltimaModificacion;

	@Column(name="FECHA_GENERACION", precision=8)
	private BigDecimal fechaGeneracion;

	@Column(name="FECHA_LIMITE_TRABA", precision=8)
	private BigDecimal fechaLimiteTraba;

	private BigDecimal importe;

	@Column(name="IND_PROCESADO", length=1)
	private String indProcesado;

	@Column(name="ISO_MONEDA", length=3)
	private String isoMoneda;

	@Column(length=1)
	private String letra;

	@Column(length=12)
	private String municipio;

	@Column(length=9)
	private String nif;

	@Column(length=100)
	private String nombre;

	@Column(name="NOMBRE_VP", length=25)
	private String nombreVp;

	private BigDecimal numero;

	@Column(name="NUMERO_EMBARGO", length=40)
	private String numeroEmbargo;

	@Column(length=2)
	private String piso;

	@Column(length=2)
	private String puerta;

	@Column(name="SIGLAS_VP", length=2)
	private String siglasVp;

	@Column(name="TIPO_DEUDA", length=2)
	private String tipoDeuda;

	@Column(name="USUARIO_ULT_MODIFICACION", length=10)
	private String usuarioUltModificacion;

	//bi-directional many-to-one association to CuentaEmbargo
	@OneToMany(mappedBy="embargo")
	private List<CuentaEmbargo> cuentaEmbargos;

	//bi-directional many-to-one association to ControlFichero
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_CONTROL_FICHERO", nullable=false)
	private ControlFichero controlFichero;

	//bi-directional many-to-one association to EntidadesOrdenante
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_ENTIDAD_ORDENANTE", nullable=false)
	private EntidadesOrdenante entidadesOrdenante;

	//bi-directional many-to-one association to Traba
	@OneToMany(mappedBy="embargo")
	private List<Traba> trabas;

	@Column(name="RAZON_SOCIAL_INTERNA", length=100)
	private String razonSocialInterna;

	
	public Embargo() {
	}

	public long getCodEmbargo() {
		return this.codEmbargo;
	}

	public void setCodEmbargo(long codEmbargo) {
		this.codEmbargo = codEmbargo;
	}

	public String getCodDeudaDeudor() {
		return this.codDeudaDeudor;
	}

	public void setCodDeudaDeudor(String codDeudaDeudor) {
		this.codDeudaDeudor = codDeudaDeudor;
	}

	public BigDecimal getCodSucursal() {
		return this.codSucursal;
	}

	public void setCodSucursal(BigDecimal codSucursal) {
		this.codSucursal = codSucursal;
	}

	public String getCodigoPostal() {
		return this.codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getDatregcomdet() {
		return this.datregcomdet;
	}

	public void setDatregcomdet(String datregcomdet) {
		this.datregcomdet = datregcomdet;
	}

	public String getDomicilio() {
		return this.domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getEscalera() {
		return this.escalera;
	}

	public void setEscalera(String escalera) {
		this.escalera = escalera;
	}

	public BigDecimal getFUltimaModificacion() {
		return this.fUltimaModificacion;
	}

	public void setFUltimaModificacion(BigDecimal fUltimaModificacion) {
		this.fUltimaModificacion = fUltimaModificacion;
	}

	public BigDecimal getFechaGeneracion() {
		return this.fechaGeneracion;
	}

	public void setFechaGeneracion(BigDecimal fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}

	public BigDecimal getFechaLimiteTraba() {
		return this.fechaLimiteTraba;
	}

	public void setFechaLimiteTraba(BigDecimal fechaLimiteTraba) {
		this.fechaLimiteTraba = fechaLimiteTraba;
	}

	public BigDecimal getImporte() {
		return this.importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
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

	public String getLetra() {
		return this.letra;
	}

	public void setLetra(String letra) {
		this.letra = letra;
	}

	public String getMunicipio() {
		return this.municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombreVp() {
		return this.nombreVp;
	}

	public void setNombreVp(String nombreVp) {
		this.nombreVp = nombreVp;
	}

	public BigDecimal getNumero() {
		return this.numero;
	}

	public void setNumero(BigDecimal numero) {
		this.numero = numero;
	}

	public String getNumeroEmbargo() {
		return this.numeroEmbargo;
	}

	public void setNumeroEmbargo(String numeroEmbargo) {
		this.numeroEmbargo = numeroEmbargo;
	}

	public String getPiso() {
		return this.piso;
	}

	public void setPiso(String piso) {
		this.piso = piso;
	}

	public String getPuerta() {
		return this.puerta;
	}

	public void setPuerta(String puerta) {
		this.puerta = puerta;
	}

	public String getSiglasVp() {
		return this.siglasVp;
	}

	public void setSiglasVp(String siglasVp) {
		this.siglasVp = siglasVp;
	}

	public String getTipoDeuda() {
		return this.tipoDeuda;
	}

	public void setTipoDeuda(String tipoDeuda) {
		this.tipoDeuda = tipoDeuda;
	}

	public String getUsuarioUltModificacion() {
		return this.usuarioUltModificacion;
	}

	public void setUsuarioUltModificacion(String usuarioUltModificacion) {
		this.usuarioUltModificacion = usuarioUltModificacion;
	}

	public List<CuentaEmbargo> getCuentaEmbargos() {
		return this.cuentaEmbargos;
	}

	public void setCuentaEmbargos(List<CuentaEmbargo> cuentaEmbargos) {
		this.cuentaEmbargos = cuentaEmbargos;
	}

	public CuentaEmbargo addCuentaEmbargo(CuentaEmbargo cuentaEmbargo) {
		getCuentaEmbargos().add(cuentaEmbargo);
		cuentaEmbargo.setEmbargo(this);

		return cuentaEmbargo;
	}

	public CuentaEmbargo removeCuentaEmbargo(CuentaEmbargo cuentaEmbargo) {
		getCuentaEmbargos().remove(cuentaEmbargo);
		cuentaEmbargo.setEmbargo(null);

		return cuentaEmbargo;
	}

	public ControlFichero getControlFichero() {
		return this.controlFichero;
	}

	public void setControlFichero(ControlFichero controlFichero) {
		this.controlFichero = controlFichero;
	}

	public EntidadesOrdenante getEntidadesOrdenante() {
		return this.entidadesOrdenante;
	}

	public void setEntidadesOrdenante(EntidadesOrdenante entidadesOrdenante) {
		this.entidadesOrdenante = entidadesOrdenante;
	}

	public List<Traba> getTrabas() {
		return this.trabas;
	}

	public void setTrabas(List<Traba> trabas) {
		this.trabas = trabas;
	}

	public Traba addTraba(Traba traba) {
		getTrabas().add(traba);
		traba.setEmbargo(this);

		return traba;
	}

	public Traba removeTraba(Traba traba) {
		getTrabas().remove(traba);
		traba.setEmbargo(null);

		return traba;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getRazonSocialInterna() {
		return razonSocialInterna;
	}

	public void setRazonSocialInterna(String razonSocialInterna) {
		this.razonSocialInterna = razonSocialInterna;
	}
}