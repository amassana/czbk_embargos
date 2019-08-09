package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the H_PETICION_INFORMACION database table.
 * 
 */
@Entity
@Table(name="H_PETICION_INFORMACION")
@NamedQuery(name="HPeticionInformacion.findAll", query="SELECT h FROM HPeticionInformacion h")
public class HPeticionInformacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private HPeticionInformacionPK id;

	@Column(name="CLAVE_SEGURIDAD1", length=12)
	private String claveSeguridad1;

	@Column(name="CLAVE_SEGURIDAD2", length=12)
	private String claveSeguridad2;

	@Column(name="CLAVE_SEGURIDAD3", length=20)
	private String claveSeguridad3;

	@Column(name="CLAVE_SEGURIDAD4", length=12)
	private String claveSeguridad4;

	@Column(name="CLAVE_SEGURIDAD5", length=12)
	private String claveSeguridad5;

	@Column(name="CLAVE_SEGURIDAD6", length=12)
	private String claveSeguridad6;

	@Column(name="COD_CONTROL_FICHERO")
	private BigDecimal codControlFichero;

	@Column(name="COD_DEUDA_DEUDOR", length=8)
	private String codDeudaDeudor;

	@Column(name="COD_ENTIDAD_ORDENANTE", nullable=false)
	private BigDecimal codEntidadOrdenante;

	@Column(name="COD_FICHERO_RESPUESTA")
	private BigDecimal codFicheroRespuesta;

	@Column(name="COD_SUCURSAL")
	private BigDecimal codSucursal;

	@Column(name="CODIGO_POSTAL", length=5)
	private String codigoPostal;

	@Column(length=20)
	private String cuenta1;

	@Column(length=20)
	private String cuenta2;

	@Column(length=20)
	private String cuenta3;

	@Column(length=20)
	private String cuenta4;

	@Column(length=20)
	private String cuenta5;

	@Column(length=20)
	private String cuenta6;

	@Column(length=39)
	private String domicilio;

	@Column(length=24)
	private String iban1;

	@Column(length=24)
	private String iban2;

	@Column(length=24)
	private String iban3;

	@Column(length=24)
	private String iban4;

	@Column(length=24)
	private String iban5;

	@Column(length=24)
	private String iban6;

	@Column(name="IND_CASO_REVISADO", length=1)
	private String indCasoRevisado;

	@Column(name="IND_INFORMACION_CORRECTA", length=1)
	private String indInformacionCorrecta;

	@Column(name="IND_MAS_CUENTAS", length=1)
	private String indMasCuentas;

	@Column(length=12)
	private String municipio;

	@Column(length=9)
	private String nif;

	@Column(name="NUMERO_EMBARGO", length=13)
	private String numeroEmbargo;

	@Column(name="RAZON_SOCIAL", length=40)
	private String razonSocial;

	@Column(name="RAZON_SOCIAL_INTERNA", length=100)
	private String razonSocialInterna;

	@Column(name="TIPO_DEUDA", length=2)
	private String tipoDeuda;

	@Column(name="USUARIO_ULT_MODIFICACION", length=10)
	private String usuarioUltModificacion;

	public HPeticionInformacion() {
	}

	public HPeticionInformacionPK getId() {
		return this.id;
	}

	public void setId(HPeticionInformacionPK id) {
		this.id = id;
	}

	public String getClaveSeguridad1() {
		return this.claveSeguridad1;
	}

	public void setClaveSeguridad1(String claveSeguridad1) {
		this.claveSeguridad1 = claveSeguridad1;
	}

	public String getClaveSeguridad2() {
		return this.claveSeguridad2;
	}

	public void setClaveSeguridad2(String claveSeguridad2) {
		this.claveSeguridad2 = claveSeguridad2;
	}

	public String getClaveSeguridad3() {
		return this.claveSeguridad3;
	}

	public void setClaveSeguridad3(String claveSeguridad3) {
		this.claveSeguridad3 = claveSeguridad3;
	}

	public String getClaveSeguridad4() {
		return this.claveSeguridad4;
	}

	public void setClaveSeguridad4(String claveSeguridad4) {
		this.claveSeguridad4 = claveSeguridad4;
	}

	public String getClaveSeguridad5() {
		return this.claveSeguridad5;
	}

	public void setClaveSeguridad5(String claveSeguridad5) {
		this.claveSeguridad5 = claveSeguridad5;
	}

	public String getClaveSeguridad6() {
		return this.claveSeguridad6;
	}

	public void setClaveSeguridad6(String claveSeguridad6) {
		this.claveSeguridad6 = claveSeguridad6;
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

	public BigDecimal getCodFicheroRespuesta() {
		return this.codFicheroRespuesta;
	}

	public void setCodFicheroRespuesta(BigDecimal codFicheroRespuesta) {
		this.codFicheroRespuesta = codFicheroRespuesta;
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

	public String getCuenta1() {
		return this.cuenta1;
	}

	public void setCuenta1(String cuenta1) {
		this.cuenta1 = cuenta1;
	}

	public String getCuenta2() {
		return this.cuenta2;
	}

	public void setCuenta2(String cuenta2) {
		this.cuenta2 = cuenta2;
	}

	public String getCuenta3() {
		return this.cuenta3;
	}

	public void setCuenta3(String cuenta3) {
		this.cuenta3 = cuenta3;
	}

	public String getCuenta4() {
		return this.cuenta4;
	}

	public void setCuenta4(String cuenta4) {
		this.cuenta4 = cuenta4;
	}

	public String getCuenta5() {
		return this.cuenta5;
	}

	public void setCuenta5(String cuenta5) {
		this.cuenta5 = cuenta5;
	}

	public String getCuenta6() {
		return this.cuenta6;
	}

	public void setCuenta6(String cuenta6) {
		this.cuenta6 = cuenta6;
	}

	public String getDomicilio() {
		return this.domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getIban1() {
		return this.iban1;
	}

	public void setIban1(String iban1) {
		this.iban1 = iban1;
	}

	public String getIban2() {
		return this.iban2;
	}

	public void setIban2(String iban2) {
		this.iban2 = iban2;
	}

	public String getIban3() {
		return this.iban3;
	}

	public void setIban3(String iban3) {
		this.iban3 = iban3;
	}

	public String getIban4() {
		return this.iban4;
	}

	public void setIban4(String iban4) {
		this.iban4 = iban4;
	}

	public String getIban5() {
		return this.iban5;
	}

	public void setIban5(String iban5) {
		this.iban5 = iban5;
	}

	public String getIban6() {
		return this.iban6;
	}

	public void setIban6(String iban6) {
		this.iban6 = iban6;
	}

	public String getIndCasoRevisado() {
		return this.indCasoRevisado;
	}

	public void setIndCasoRevisado(String indCasoRevisado) {
		this.indCasoRevisado = indCasoRevisado;
	}

	public String getIndInformacionCorrecta() {
		return this.indInformacionCorrecta;
	}

	public void setIndInformacionCorrecta(String indInformacionCorrecta) {
		this.indInformacionCorrecta = indInformacionCorrecta;
	}

	public String getIndMasCuentas() {
		return this.indMasCuentas;
	}

	public void setIndMasCuentas(String indMasCuentas) {
		this.indMasCuentas = indMasCuentas;
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

	public String getNumeroEmbargo() {
		return this.numeroEmbargo;
	}

	public void setNumeroEmbargo(String numeroEmbargo) {
		this.numeroEmbargo = numeroEmbargo;
	}

	public String getRazonSocial() {
		return this.razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getRazonSocialInterna() {
		return this.razonSocialInterna;
	}

	public void setRazonSocialInterna(String razonSocialInterna) {
		this.razonSocialInterna = razonSocialInterna;
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

}