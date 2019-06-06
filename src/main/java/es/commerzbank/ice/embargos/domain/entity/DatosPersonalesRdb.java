package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the DATOS_PERSONALES_RDB database table.
 * 
 */
@Entity
@Table(name="DATOS_PERSONALES_RDB")
@NamedQuery(name="DatosPersonalesRdb.findAll", query="SELECT d FROM DatosPersonalesRdb d")
public class DatosPersonalesRdb implements Serializable {
	private static final long serialVersionUID = 1L;

	//Asignada como primary key:
	@Id
	@Column(name="NUMERO_CLIENTE", length=10)
	private String numeroCliente;
	
	@Column(name="CLAVE_CNAE", length=5)
	private String claveCnae;

	@Column(name="CLAVE_CORREO", length=3)
	private String claveCorreo;

	@Column(name="CLAVE_PAIS", length=3)
	private String clavePais;

	@Column(name="DC_CODIGO_POSTAL", length=5)
	private String dcCodigoPostal;

	@Column(name="DC_DIRECCION", length=40)
	private String dcDireccion;

	@Column(name="DC_NOMBRE_EMPRESA", length=40)
	private String dcNombreEmpresa;

	@Column(name="DC_NOMBRE_PAIS", length=32)
	private String dcNombrePais;

	@Column(name="DC_OBSERVACIONES", length=40)
	private String dcObservaciones;

	@Column(name="DC_PAIS_POSTAL", length=2)
	private String dcPaisPostal;

	@Column(name="DC_POBLACION", length=30)
	private String dcPoblacion;

	@Column(name="DE_CODIGO_POSTAL", length=5)
	private String deCodigoPostal;

	@Column(name="DE_DIRECCION", length=40)
	private String deDireccion;

	@Column(name="DE_NOMBRE_EMPRESA", length=40)
	private String deNombreEmpresa;

	@Column(name="DE_NOMBRE_PAIS", length=32)
	private String deNombrePais;

	@Column(name="DE_OBSERVACIONES", length=40)
	private String deObservaciones;

	@Column(name="DE_PAIS_POSTAL", length=2)
	private String dePaisPostal;

	@Column(name="DE_POBLACION", length=30)
	private String dePoblacion;

	@Column(name="FECHA_ALTA", length=8)
	private String fechaAlta;

	@Column(name="FECHA_BAJA", length=8)
	private String fechaBaja;

	public DatosPersonalesRdb() {
	}

	public String getClaveCnae() {
		return this.claveCnae;
	}

	public void setClaveCnae(String claveCnae) {
		this.claveCnae = claveCnae;
	}

	public String getClaveCorreo() {
		return this.claveCorreo;
	}

	public void setClaveCorreo(String claveCorreo) {
		this.claveCorreo = claveCorreo;
	}

	public String getClavePais() {
		return this.clavePais;
	}

	public void setClavePais(String clavePais) {
		this.clavePais = clavePais;
	}

	public String getDcCodigoPostal() {
		return this.dcCodigoPostal;
	}

	public void setDcCodigoPostal(String dcCodigoPostal) {
		this.dcCodigoPostal = dcCodigoPostal;
	}

	public String getDcDireccion() {
		return this.dcDireccion;
	}

	public void setDcDireccion(String dcDireccion) {
		this.dcDireccion = dcDireccion;
	}

	public String getDcNombreEmpresa() {
		return this.dcNombreEmpresa;
	}

	public void setDcNombreEmpresa(String dcNombreEmpresa) {
		this.dcNombreEmpresa = dcNombreEmpresa;
	}

	public String getDcNombrePais() {
		return this.dcNombrePais;
	}

	public void setDcNombrePais(String dcNombrePais) {
		this.dcNombrePais = dcNombrePais;
	}

	public String getDcObservaciones() {
		return this.dcObservaciones;
	}

	public void setDcObservaciones(String dcObservaciones) {
		this.dcObservaciones = dcObservaciones;
	}

	public String getDcPaisPostal() {
		return this.dcPaisPostal;
	}

	public void setDcPaisPostal(String dcPaisPostal) {
		this.dcPaisPostal = dcPaisPostal;
	}

	public String getDcPoblacion() {
		return this.dcPoblacion;
	}

	public void setDcPoblacion(String dcPoblacion) {
		this.dcPoblacion = dcPoblacion;
	}

	public String getDeCodigoPostal() {
		return this.deCodigoPostal;
	}

	public void setDeCodigoPostal(String deCodigoPostal) {
		this.deCodigoPostal = deCodigoPostal;
	}

	public String getDeDireccion() {
		return this.deDireccion;
	}

	public void setDeDireccion(String deDireccion) {
		this.deDireccion = deDireccion;
	}

	public String getDeNombreEmpresa() {
		return this.deNombreEmpresa;
	}

	public void setDeNombreEmpresa(String deNombreEmpresa) {
		this.deNombreEmpresa = deNombreEmpresa;
	}

	public String getDeNombrePais() {
		return this.deNombrePais;
	}

	public void setDeNombrePais(String deNombrePais) {
		this.deNombrePais = deNombrePais;
	}

	public String getDeObservaciones() {
		return this.deObservaciones;
	}

	public void setDeObservaciones(String deObservaciones) {
		this.deObservaciones = deObservaciones;
	}

	public String getDePaisPostal() {
		return this.dePaisPostal;
	}

	public void setDePaisPostal(String dePaisPostal) {
		this.dePaisPostal = dePaisPostal;
	}

	public String getDePoblacion() {
		return this.dePoblacion;
	}

	public void setDePoblacion(String dePoblacion) {
		this.dePoblacion = dePoblacion;
	}

	public String getFechaAlta() {
		return this.fechaAlta;
	}

	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public String getFechaBaja() {
		return this.fechaBaja;
	}

	public void setFechaBaja(String fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public String getNumeroCliente() {
		return this.numeroCliente;
	}

	public void setNumeroCliente(String numeroCliente) {
		this.numeroCliente = numeroCliente;
	}

}