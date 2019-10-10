package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ESTADO_CONTABILIZACION database table.
 * 
 */
@Entity
@Table(name="ESTADO_CONTABILIZACION")
@NamedQuery(name="EstadoContabilizacion.findAll", query="SELECT e FROM EstadoContabilizacion e")
public class EstadoContabilizacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_ESTADO", unique=true)
	private long codEstado;

	@Column(length=50)
	private String descripcion;

	//bi-directional many-to-one association to FicheroFinal
	@OneToMany(mappedBy="estadoContabilizacion")
	private List<FicheroFinal> ficheroFinals;

	public EstadoContabilizacion() {
	}

	public long getCodEstado() {
		return this.codEstado;
	}

	public void setCodEstado(long codEstado) {
		this.codEstado = codEstado;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<FicheroFinal> getFicheroFinals() {
		return this.ficheroFinals;
	}

	public void setFicheroFinals(List<FicheroFinal> ficheroFinals) {
		this.ficheroFinals = ficheroFinals;
	}

	public FicheroFinal addFicheroFinal(FicheroFinal ficheroFinal) {
		getFicheroFinals().add(ficheroFinal);
		ficheroFinal.setEstadoContabilizacion(this);

		return ficheroFinal;
	}

	public FicheroFinal removeFicheroFinal(FicheroFinal ficheroFinal) {
		getFicheroFinals().remove(ficheroFinal);
		ficheroFinal.setEstadoContabilizacion(null);

		return ficheroFinal;
	}

}