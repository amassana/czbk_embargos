package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="APODERADOS")
@NamedQuery(name="Apoderados.findAll", query="SELECT a FROM Apoderados a")
public class Apoderados implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="secuencia_id")
	@org.hibernate.annotations.GenericGenerator(
			name = "secuencia_id",
			strategy = "increment"
	)
	@Column(name="ID", unique=true, nullable=false, updatable = false)
	private long id;
	
	@Column(name="NOMBRE", length=50)
	private String nombre;

	@Column(name="CARGO", length=20)
	private String cargo;
	
	@Column(name="IND_ACTIVO", length=1)
	private String indActivo;
	
	@Column(name="F_ULTIMA_MODIFICACION")
	private Timestamp fUltimaModificacion;
	
	@Column(name="USU_ULTIMA_MODIFICACION", length=20)
	private String usuUltimaModificacion;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getIndActivo() {
		return indActivo;
	}

	public void setIndActivo(String indActivo) {
		this.indActivo = indActivo;
	}

	public Timestamp getfUltimaModificacion() {
		return fUltimaModificacion;
	}

	public void setfUltimaModificacion(Timestamp fUltimaModificacion) {
		this.fUltimaModificacion = fUltimaModificacion;
	}

	public String getUsuUltimaModificacion() {
		return usuUltimaModificacion;
	}

	public void setUsuUltimaModificacion(String usuUltimaModificacion) {
		this.usuUltimaModificacion = usuUltimaModificacion;
	}
}

