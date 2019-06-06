package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the MONEDAS_RDB database table.
 * 
 */
@Entity
@Table(name="MONEDAS_RDB")
@NamedQuery(name="MonedasRdb.findAll", query="SELECT m FROM MonedasRdb m")
public class MonedasRdb implements Serializable {
	private static final long serialVersionUID = 1L;

	//Nueva primary key:
	@Id
	private BigDecimal id;
	
	@Column(precision=5)
	private BigDecimal base;

	@Column(length=20)
	private String descripcion;

	@Column(length=3)
	private String iso;

	@Column(name="ISO_IBS", length=3)
	private String isoIbs;

	public MonedasRdb() {
	}

	public BigDecimal getBase() {
		return this.base;
	}

	public void setBase(BigDecimal base) {
		this.base = base;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getIso() {
		return this.iso;
	}

	public void setIso(String iso) {
		this.iso = iso;
	}

	public String getIsoIbs() {
		return this.isoIbs;
	}

	public void setIsoIbs(String isoIbs) {
		this.isoIbs = isoIbs;
	}

}