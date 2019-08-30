package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the H_EMBARGO database table.
 * 
 */
@Entity
@Table(name="H_EMBARGO")
@NamedQuery(name="HEmbargo.findAll", query="SELECT h FROM HEmbargo h")
public class HEmbargo implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private HEmbargoPK id;

	@Column(name="COD_CONTROL_FICHERO")
	private BigDecimal codControlFichero;

	@Column(name="COD_DEUDA_DEUDOR")
	private String codDeudaDeudor;

	@Column(name="COD_ENTIDAD_ORDENANTE")
	private BigDecimal codEntidadOrdenante;

	@Column(name="COD_SUCURSAL")
	private BigDecimal codSucursal;

	@Column(name="CODIGO_POSTAL")
	private String codigoPostal;

	private String datregcomdet;

	private String domicilio;

	private String escalera;

	@Column(name="F_ULTIMA_MODIFICACION")
	private BigDecimal fUltimaModificacion;

	@Column(name="FECHA_GENERACION")
	private BigDecimal fechaGeneracion;

	@Column(name="FECHA_LIMITE_TRABA")
	private BigDecimal fechaLimiteTraba;

	private BigDecimal importe;

	@Column(name="IND_PROCESADO")
	private String indProcesado;

	@Column(name="ISO_MONEDA")
	private String isoMoneda;

	private String letra;

	private String municipio;

	private String nif;

	private String nombre;

	@Column(name="NOMBRE_VP")
	private String nombreVp;

	private BigDecimal numero;

	@Column(name="NUMERO_EMBARGO")
	private String numeroEmbargo;

	private String piso;

	private String puerta;

	@Column(name="RAZON_SOCIAL_INTERNA")
	private String razonSocialInterna;

	@Column(name="SIGLAS_VP")
	private String siglasVp;

	@Column(name="TIPO_DEUDA")
	private String tipoDeuda;

	@Column(name="USUARIO_ULT_MODIFICACION")
	private String usuarioUltModificacion;

	//bi-directional many-to-one association to HTraba
	@OneToMany(mappedBy="HEmbargo")
	private List<HTraba> HTrabas;

	public HEmbargo() {
	}

	public HEmbargoPK getId() {
		return this.id;
	}

	public void setId(HEmbargoPK id) {
		this.id = id;
	}

	public BigDecimal getCodControlFichero() {
		return this.codControlFichero;
	}

	public void setCodControlFichero(BigDecimal codControlFichero) {
		this.codControlFichero = codControlFichero;
	}

	public String getCodDeudaDeudor() {
		return this.codDeudaDeudor;
	}

	public void setCodDeudaDeudor(String codDeudaDeudor) {
		this.codDeudaDeudor = codDeudaDeudor;
	}

	public BigDecimal getCodEntidadOrdenante() {
		return this.codEntidadOrdenante;
	}

	public void setCodEntidadOrdenante(BigDecimal codEntidadOrdenante) {
		this.codEntidadOrdenante = codEntidadOrdenante;
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

	public String getNif() {
		return this.nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
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

	public String getRazonSocialInterna() {
		return this.razonSocialInterna;
	}

	public void setRazonSocialInterna(String razonSocialInterna) {
		this.razonSocialInterna = razonSocialInterna;
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

	public List<HTraba> getHTrabas() {
		return this.HTrabas;
	}

	public void setHTrabas(List<HTraba> HTrabas) {
		this.HTrabas = HTrabas;
	}

	public HTraba addHTraba(HTraba HTraba) {
		getHTrabas().add(HTraba);
		HTraba.setHEmbargo(this);

		return HTraba;
	}

	public HTraba removeHTraba(HTraba HTraba) {
		getHTrabas().remove(HTraba);
		HTraba.setHEmbargo(null);

		return HTraba;
	}

}