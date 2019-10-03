package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the DATOS_CLIENTE database table.
 * 
 */
@Entity
@Table(name="DATOS_CLIENTE")
@NamedQuery(name="DatosCliente.findAll", query="SELECT d FROM DatosCliente d")
public class DatosCliente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String nif;

	@Column(name="CODIGO_POSTAL")
	private String codigoPostal;

	private String domicilio;

	private String municipio;

	private String nombre;
	
	@Column(name="IND_PENDIENTE_DWH", length=1)
	private String indPendienteDwh;
	
	@Column(name="F_ULTIMA_MODIFICACION", precision=14)
	private BigDecimal fUltimaModificacion;
	
	@Column(name="USUARIO_ULT_MODIFICACION", length=10)
	private String usuarioUltModificacion;

	//bi-directional many-to-one association to Embargo
	@OneToMany(mappedBy="datosCliente")
	private List<Embargo> embargos;

	//bi-directional many-to-one association to LevantamientoTraba
	@OneToMany(mappedBy="datosCliente")
	private List<LevantamientoTraba> levantamientoTrabas;

	//bi-directional many-to-one association to PeticionInformacion
	@OneToMany(mappedBy="datosCliente")
	private List<PeticionInformacion> peticionInformacions;

	public DatosCliente() {
	}

	public String getNif() {
		return this.nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getCodigoPostal() {
		return this.codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getDomicilio() {
		return this.domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
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
	
	public String getIndPendienteDwh() {
		return indPendienteDwh;
	}

	public void setIndPendienteDwh(String indPendienteDwh) {
		this.indPendienteDwh = indPendienteDwh;
	}

	public BigDecimal getFUltimaModificacion() {
		return fUltimaModificacion;
	}

	public void setFUltimaModificacion(BigDecimal fUltimaModificacion) {
		this.fUltimaModificacion = fUltimaModificacion;
	}

	public String getUsuarioUltModificacion() {
		return usuarioUltModificacion;
	}

	public void setUsuarioUltModificacion(String usuarioUltModificacion) {
		this.usuarioUltModificacion = usuarioUltModificacion;
	}

	public List<Embargo> getEmbargos() {
		return this.embargos;
	}

	public void setEmbargos(List<Embargo> embargos) {
		this.embargos = embargos;
	}

	public Embargo addEmbargo(Embargo embargo) {
		getEmbargos().add(embargo);
		embargo.setDatosCliente(this);

		return embargo;
	}

	public Embargo removeEmbargo(Embargo embargo) {
		getEmbargos().remove(embargo);
		embargo.setDatosCliente(null);

		return embargo;
	}

	public List<LevantamientoTraba> getLevantamientoTrabas() {
		return this.levantamientoTrabas;
	}

	public void setLevantamientoTrabas(List<LevantamientoTraba> levantamientoTrabas) {
		this.levantamientoTrabas = levantamientoTrabas;
	}

	public LevantamientoTraba addLevantamientoTraba(LevantamientoTraba levantamientoTraba) {
		getLevantamientoTrabas().add(levantamientoTraba);
		levantamientoTraba.setDatosCliente(this);

		return levantamientoTraba;
	}

	public LevantamientoTraba removeLevantamientoTraba(LevantamientoTraba levantamientoTraba) {
		getLevantamientoTrabas().remove(levantamientoTraba);
		levantamientoTraba.setDatosCliente(null);

		return levantamientoTraba;
	}

	public List<PeticionInformacion> getPeticionInformacions() {
		return this.peticionInformacions;
	}

	public void setPeticionInformacions(List<PeticionInformacion> peticionInformacions) {
		this.peticionInformacions = peticionInformacions;
	}

	public PeticionInformacion addPeticionInformacion(PeticionInformacion peticionInformacion) {
		getPeticionInformacions().add(peticionInformacion);
		peticionInformacion.setDatosCliente(this);

		return peticionInformacion;
	}

	public PeticionInformacion removePeticionInformacion(PeticionInformacion peticionInformacion) {
		getPeticionInformacions().remove(peticionInformacion);
		peticionInformacion.setDatosCliente(null);

		return peticionInformacion;
	}

}