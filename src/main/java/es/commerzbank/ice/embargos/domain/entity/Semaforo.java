package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the SEMAFORO database table.
 * 
 */
@Entity
@Table(name="SEMAFORO")
@NamedQuery(name="Semaforo.findAll", query="SELECT s FROM Semaforo s")
public class Semaforo implements Serializable {
	private static final long serialVersionUID = 1L;

	//Nueva primary key:
	@Id
	private BigDecimal id;
	
	@Column(precision=14)
	private BigDecimal fecha;

	@Column(length=25)
	private String maquina;

	@Column(length=25)
	private String usuario;

	public Semaforo() {
	}

	public BigDecimal getFecha() {
		return this.fecha;
	}

	public void setFecha(BigDecimal fecha) {
		this.fecha = fecha;
	}

	public String getMaquina() {
		return this.maquina;
	}

	public void setMaquina(String maquina) {
		this.maquina = maquina;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

}