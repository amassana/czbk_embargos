package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;


/**
 * The persistent class for the PAISES_IBS_RDB database table.
 * 
 */
@Entity
@Table(name="PAISES_IBS_RDB")
@NamedQuery(name="PaisesIbsRdb.findAll", query="SELECT p FROM PaisesIbsRdb p")
public class PaisesIbsRdb implements Serializable {
	private static final long serialVersionUID = 1L;

	//Nueva primary key:
	@Id
	private BigDecimal id;
	
	@Column(length=35)
	private String nombre;

	@Column(length=3)
	private String pais;

	public PaisesIbsRdb() {
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPais() {
		return this.pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

}