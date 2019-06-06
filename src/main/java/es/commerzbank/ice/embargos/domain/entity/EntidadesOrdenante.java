package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the ENTIDADES_ORDENANTES database table.
 * 
 */
@Entity
@Table(name="ENTIDADES_ORDENANTES")
@NamedQuery(name="EntidadesOrdenante.findAll", query="SELECT e FROM EntidadesOrdenante e")
public class EntidadesOrdenante implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_ENTIDAD_ORDENANTE", unique=true, nullable=false)
	private long codEntidadOrdenante;

	@Column(name="CODIGO_INE", length=6)
	private String codigoIne;

	@Column(name="DES_ENTIDAD", length=40)
	private String desEntidad;

	@Column(name="F_ULTIMA_MODIFICACION", precision=14)
	private BigDecimal fUltimaModificacion;

	@Column(name="IDENTIFICADOR_ENTIDAD", length=2)
	private String identificadorEntidad;

	@Column(name="IND_CGPJ", length=1)
	private String indCgpj;

	@Column(name="IND_FORMATO_AEAT", length=1)
	private String indFormatoAeat;

	@Column(name="IND_NORMA63", length=1)
	private String indNorma63;

	@Column(name="NIF_ENTIDAD", length=9)
	private String nifEntidad;

	@Column(name="RPT_CONENT", length=50)
	private String rptConent;

	@Column(name="RPT_DIRENT", length=100)
	private String rptDirent;

	@Column(name="RPT_NOMENT", length=50)
	private String rptNoment;

	@Column(name="RPT_TELENT", length=50)
	private String rptTelent;

	@Column(name="USUARIO_ULT_MODIFICACION", length=10)
	private String usuarioUltModificacion;

	//bi-directional many-to-one association to Embargo
	@OneToMany(mappedBy="entidadesOrdenante")
	private List<Embargo> embargos;

	//bi-directional many-to-one association to EntidadesComunicadora
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_ENTIDAD_PRESENTADORA", nullable=false)
	private EntidadesComunicadora entidadesComunicadora;

	//bi-directional many-to-one association to PeticionInformacion
	@OneToMany(mappedBy="entidadesOrdenante")
	private List<PeticionInformacion> peticionInformacions;

	public EntidadesOrdenante() {
	}

	public long getCodEntidadOrdenante() {
		return this.codEntidadOrdenante;
	}

	public void setCodEntidadOrdenante(long codEntidadOrdenante) {
		this.codEntidadOrdenante = codEntidadOrdenante;
	}

	public String getCodigoIne() {
		return this.codigoIne;
	}

	public void setCodigoIne(String codigoIne) {
		this.codigoIne = codigoIne;
	}

	public String getDesEntidad() {
		return this.desEntidad;
	}

	public void setDesEntidad(String desEntidad) {
		this.desEntidad = desEntidad;
	}

	public BigDecimal getFUltimaModificacion() {
		return this.fUltimaModificacion;
	}

	public void setFUltimaModificacion(BigDecimal fUltimaModificacion) {
		this.fUltimaModificacion = fUltimaModificacion;
	}

	public String getIdentificadorEntidad() {
		return this.identificadorEntidad;
	}

	public void setIdentificadorEntidad(String identificadorEntidad) {
		this.identificadorEntidad = identificadorEntidad;
	}

	public String getIndCgpj() {
		return this.indCgpj;
	}

	public void setIndCgpj(String indCgpj) {
		this.indCgpj = indCgpj;
	}

	public String getIndFormatoAeat() {
		return this.indFormatoAeat;
	}

	public void setIndFormatoAeat(String indFormatoAeat) {
		this.indFormatoAeat = indFormatoAeat;
	}

	public String getIndNorma63() {
		return this.indNorma63;
	}

	public void setIndNorma63(String indNorma63) {
		this.indNorma63 = indNorma63;
	}

	public String getNifEntidad() {
		return this.nifEntidad;
	}

	public void setNifEntidad(String nifEntidad) {
		this.nifEntidad = nifEntidad;
	}

	public String getRptConent() {
		return this.rptConent;
	}

	public void setRptConent(String rptConent) {
		this.rptConent = rptConent;
	}

	public String getRptDirent() {
		return this.rptDirent;
	}

	public void setRptDirent(String rptDirent) {
		this.rptDirent = rptDirent;
	}

	public String getRptNoment() {
		return this.rptNoment;
	}

	public void setRptNoment(String rptNoment) {
		this.rptNoment = rptNoment;
	}

	public String getRptTelent() {
		return this.rptTelent;
	}

	public void setRptTelent(String rptTelent) {
		this.rptTelent = rptTelent;
	}

	public String getUsuarioUltModificacion() {
		return this.usuarioUltModificacion;
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
		embargo.setEntidadesOrdenante(this);

		return embargo;
	}

	public Embargo removeEmbargo(Embargo embargo) {
		getEmbargos().remove(embargo);
		embargo.setEntidadesOrdenante(null);

		return embargo;
	}

	public EntidadesComunicadora getEntidadesComunicadora() {
		return this.entidadesComunicadora;
	}

	public void setEntidadesComunicadora(EntidadesComunicadora entidadesComunicadora) {
		this.entidadesComunicadora = entidadesComunicadora;
	}

	public List<PeticionInformacion> getPeticionInformacions() {
		return this.peticionInformacions;
	}

	public void setPeticionInformacions(List<PeticionInformacion> peticionInformacions) {
		this.peticionInformacions = peticionInformacions;
	}

	public PeticionInformacion addPeticionInformacion(PeticionInformacion peticionInformacion) {
		getPeticionInformacions().add(peticionInformacion);
		peticionInformacion.setEntidadesOrdenante(this);

		return peticionInformacion;
	}

	public PeticionInformacion removePeticionInformacion(PeticionInformacion peticionInformacion) {
		getPeticionInformacions().remove(peticionInformacion);
		peticionInformacion.setEntidadesOrdenante(null);

		return peticionInformacion;
	}

}