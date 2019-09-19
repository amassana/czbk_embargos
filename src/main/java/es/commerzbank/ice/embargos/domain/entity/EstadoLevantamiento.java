package es.commerzbank.ice.embargos.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="ESTADO_LEVANTAMIENTO")
@NamedQuery(name="EstadoLevantamiento.findAll", query="SELECT e FROM EstadoLevantamiento e")
public class EstadoLevantamiento {

	@Id
	@Column(name="COD_ESTADO", unique=true, nullable=false)
	private long codEstado;

	@Column(name="ESTADO", length=40)
	private String estado;

	public long getCodEstado() {
		return codEstado;
	}

	public void setCodEstado(long codEstado) {
		this.codEstado = codEstado;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}
