package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TITULARES_RDB database table.
 * 
 */
@Entity
@Table(name="TITULARES_RDB")
@NamedQuery(name="TitularesRdb.findAll", query="SELECT t FROM TitularesRdb t")
public class TitularesRdb implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="APELLIDO_EMPRESA", length=30)
	private String apellidoEmpresa;

	@Column(name="CLAVE_PERCEPTOR", length=1)
	private String clavePerceptor;

	@Column(name="CODIGO_POSTAL", length=5)
	private String codigoPostal;

	@Column(length=40)
	private String direccion;

	@Column(length=9)
	private String nif;

	@Column(length=20)
	private String nombre;

	@Column(name="NOMBRE_PAIS", length=20)
	private String nombrePais;

	@Column(name="NUMERO_CLIENTE", length=10)
	private String numeroCliente;

	@Column(name="NUMERO_NIE", length=22)
	private String numeroNie;

	@Column(name="NUMERO_NIE_ANT", length=22)
	private String numeroNieAnt;

	@Column(name="NUMERO_TITULAR", length=1)
	private String numeroTitular;

	@Column(name="PAIS_NIF", length=2)
	private String paisNif;

	@Column(name="PAIS_POSTAL", length=2)
	private String paisPostal;

	@Column(length=30)
	private String poblacion;

	@Column(name="TIPO_CLIENTE", length=1)
	private String tipoCliente;

	public TitularesRdb() {
	}

	public String getApellidoEmpresa() {
		return this.apellidoEmpresa;
	}

	public void setApellidoEmpresa(String apellidoEmpresa) {
		this.apellidoEmpresa = apellidoEmpresa;
	}

	public String getClavePerceptor() {
		return this.clavePerceptor;
	}

	public void setClavePerceptor(String clavePerceptor) {
		this.clavePerceptor = clavePerceptor;
	}

	public String getCodigoPostal() {
		return this.codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
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

	public String getNombrePais() {
		return this.nombrePais;
	}

	public void setNombrePais(String nombrePais) {
		this.nombrePais = nombrePais;
	}

	public String getNumeroCliente() {
		return this.numeroCliente;
	}

	public void setNumeroCliente(String numeroCliente) {
		this.numeroCliente = numeroCliente;
	}

	public String getNumeroNie() {
		return this.numeroNie;
	}

	public void setNumeroNie(String numeroNie) {
		this.numeroNie = numeroNie;
	}

	public String getNumeroNieAnt() {
		return this.numeroNieAnt;
	}

	public void setNumeroNieAnt(String numeroNieAnt) {
		this.numeroNieAnt = numeroNieAnt;
	}

	public String getNumeroTitular() {
		return this.numeroTitular;
	}

	public void setNumeroTitular(String numeroTitular) {
		this.numeroTitular = numeroTitular;
	}

	public String getPaisNif() {
		return this.paisNif;
	}

	public void setPaisNif(String paisNif) {
		this.paisNif = paisNif;
	}

	public String getPaisPostal() {
		return this.paisPostal;
	}

	public void setPaisPostal(String paisPostal) {
		this.paisPostal = paisPostal;
	}

	public String getPoblacion() {
		return this.poblacion;
	}

	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}

	public String getTipoCliente() {
		return this.tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

}