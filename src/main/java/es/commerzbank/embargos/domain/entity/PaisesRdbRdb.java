package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the PAISES_RDB_RDB database table.
 * 
 */
@Entity
@Table(name="PAISES_RDB_RDB")
@NamedQuery(name="PaisesRdbRdb.findAll", query="SELECT p FROM PaisesRdbRdb p")
public class PaisesRdbRdb implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CODIGO_PAIS", length=3)
	private String codigoPais;

	@Column(name="CODIGO_PAIS_BE", length=4)
	private String codigoPaisBe;

	@Column(length=35)
	private String descripcion;

	@Column(name="ISO_CODE", length=3)
	private String isoCode;

	@Column(name="TIPO_PAIS", length=1)
	private String tipoPais;

	public PaisesRdbRdb() {
	}

	public String getCodigoPais() {
		return this.codigoPais;
	}

	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}

	public String getCodigoPaisBe() {
		return this.codigoPaisBe;
	}

	public void setCodigoPaisBe(String codigoPaisBe) {
		this.codigoPaisBe = codigoPaisBe;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getIsoCode() {
		return this.isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

	public String getTipoPais() {
		return this.tipoPais;
	}

	public void setTipoPais(String tipoPais) {
		this.tipoPais = tipoPais;
	}

}