package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the SEMAFORO_CGPJ database table.
 * 
 */
@Entity
@Table(name="SEMAFORO_CGPJ")
@NamedQuery(name="SemaforoCgpj.findAll", query="SELECT s FROM SemaforoCgpj s")
public class SemaforoCgpj implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(nullable=false, precision=14)
	private BigDecimal fecha;

	@Column(nullable=false, length=50)
	private String maquina;

	@Column(nullable=false, length=50)
	private String usuario;

	public SemaforoCgpj() {
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