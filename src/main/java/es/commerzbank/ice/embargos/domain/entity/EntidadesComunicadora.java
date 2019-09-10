package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the ENTIDADES_COMUNICADORAS database table.
 * 
 */
@Entity
@Table(name="ENTIDADES_COMUNICADORAS")
@NamedQuery(name="EntidadesComunicadora.findAll", query="SELECT e FROM EntidadesComunicadora e")
public class EntidadesComunicadora implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_ENTIDAD_PRESENTADORA", unique=true, nullable=false)
	private long codEntidadPresentadora;

	@Column(length=100)
	private String bic;

	@Column(name="CODIGO_INE", length=6)
	private String codigoIne;

	@Column(length=100)
	private String cuenta;

	@Column(name="DES_ENTIDAD", length=40)
	private String desEntidad;

	@Column(name="DIAS_RESPUESTA_F1")
	private BigDecimal diasRespuestaF1;

	@Column(name="DIAS_RESPUESTA_F3")
	private BigDecimal diasRespuestaF3;

	@Column(name="DIAS_RESPUESTA_F6")
	private BigDecimal diasRespuestaF6;

	@Column(name="F_ULTIMA_MODIFICACION", precision=14)
	private BigDecimal fUltimaModificacion;

	@Column(length=100)
	private String iban;

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

	@Column(name="PREFIJO_FICHEROS", length=10)
	private String prefijoFicheros;

	@Column(name="USUARIO_ULT_MODIFICACION", length=10)
	private String usuarioUltModificacion;

	@Column(name="IND_ACTIVO", length=1, nullable=false)
	private String indActivo;
	
	//bi-directional many-to-one association to ControlFichero
	@OneToMany(mappedBy="entidadesComunicadora")
	private List<ControlFichero> controlFicheros;

	//bi-directional many-to-one association to CuentasRecaudacion
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_CUENTA_RECAUDACION")
	private CuentasRecaudacion cuentasRecaudacion;

	//bi-directional many-to-one association to EntidadesOrdenante
	@OneToMany(mappedBy="entidadesComunicadora")
	private List<EntidadesOrdenante> entidadesOrdenantes;

	public EntidadesComunicadora() {
	}

	public long getCodEntidadPresentadora() {
		return this.codEntidadPresentadora;
	}

	public void setCodEntidadPresentadora(long codEntidadPresentadora) {
		this.codEntidadPresentadora = codEntidadPresentadora;
	}

	public String getBic() {
		return this.bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getCodigoIne() {
		return this.codigoIne;
	}

	public void setCodigoIne(String codigoIne) {
		this.codigoIne = codigoIne;
	}

	public String getCuenta() {
		return this.cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getDesEntidad() {
		return this.desEntidad;
	}

	public void setDesEntidad(String desEntidad) {
		this.desEntidad = desEntidad;
	}

	public BigDecimal getDiasRespuestaF1() {
		return this.diasRespuestaF1;
	}

	public void setDiasRespuestaF1(BigDecimal diasRespuestaF1) {
		this.diasRespuestaF1 = diasRespuestaF1;
	}

	public BigDecimal getDiasRespuestaF3() {
		return this.diasRespuestaF3;
	}

	public void setDiasRespuestaF3(BigDecimal diasRespuestaF3) {
		this.diasRespuestaF3 = diasRespuestaF3;
	}

	public BigDecimal getDiasRespuestaF6() {
		return this.diasRespuestaF6;
	}

	public void setDiasRespuestaF6(BigDecimal diasRespuestaF6) {
		this.diasRespuestaF6 = diasRespuestaF6;
	}

	public BigDecimal getFUltimaModificacion() {
		return this.fUltimaModificacion;
	}

	public void setFUltimaModificacion(BigDecimal fUltimaModificacion) {
		this.fUltimaModificacion = fUltimaModificacion;
	}

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
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

	public String getPrefijoFicheros() {
		return this.prefijoFicheros;
	}

	public void setPrefijoFicheros(String prefijoFicheros) {
		this.prefijoFicheros = prefijoFicheros;
	}

	public String getUsuarioUltModificacion() {
		return this.usuarioUltModificacion;
	}

	public void setUsuarioUltModificacion(String usuarioUltModificacion) {
		this.usuarioUltModificacion = usuarioUltModificacion;
	}

	public List<ControlFichero> getControlFicheros() {
		return this.controlFicheros;
	}

	public void setControlFicheros(List<ControlFichero> controlFicheros) {
		this.controlFicheros = controlFicheros;
	}

	public ControlFichero addControlFichero(ControlFichero controlFichero) {
		getControlFicheros().add(controlFichero);
		controlFichero.setEntidadesComunicadora(this);

		return controlFichero;
	}

	public ControlFichero removeControlFichero(ControlFichero controlFichero) {
		getControlFicheros().remove(controlFichero);
		controlFichero.setEntidadesComunicadora(null);

		return controlFichero;
	}

	public CuentasRecaudacion getCuentasRecaudacion() {
		return this.cuentasRecaudacion;
	}

	public void setCuentasRecaudacion(CuentasRecaudacion cuentasRecaudacion) {
		this.cuentasRecaudacion = cuentasRecaudacion;
	}

	public List<EntidadesOrdenante> getEntidadesOrdenantes() {
		return this.entidadesOrdenantes;
	}

	public void setEntidadesOrdenantes(List<EntidadesOrdenante> entidadesOrdenantes) {
		this.entidadesOrdenantes = entidadesOrdenantes;
	}

	public EntidadesOrdenante addEntidadesOrdenante(EntidadesOrdenante entidadesOrdenante) {
		getEntidadesOrdenantes().add(entidadesOrdenante);
		entidadesOrdenante.setEntidadesComunicadora(this);

		return entidadesOrdenante;
	}

	public EntidadesOrdenante removeEntidadesOrdenante(EntidadesOrdenante entidadesOrdenante) {
		getEntidadesOrdenantes().remove(entidadesOrdenante);
		entidadesOrdenante.setEntidadesComunicadora(null);

		return entidadesOrdenante;
	}

	public String getIndActivo() {
		return indActivo;
	}

	public void setIndActivo(String indActivo) {
		this.indActivo = indActivo;
	}

}