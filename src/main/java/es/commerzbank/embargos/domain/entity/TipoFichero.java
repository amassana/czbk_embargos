package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the TIPO_FICHERO database table.
 * 
 */
@Entity
@Table(name="TIPO_FICHERO")
@NamedQuery(name="TipoFichero.findAll", query="SELECT t FROM TipoFichero t")
public class TipoFichero implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_TIPO_FICHERO", unique=true, nullable=false)
	private long codTipoFichero;

	@Column(name="DES_TIPO_FICHERO", length=70)
	private String desTipoFichero;

	//bi-directional many-to-one association to ControlFichero
	@OneToMany(mappedBy="tipoFichero")
	private List<ControlFichero> controlFicheros;

	public TipoFichero() {
	}

	public long getCodTipoFichero() {
		return this.codTipoFichero;
	}

	public void setCodTipoFichero(long codTipoFichero) {
		this.codTipoFichero = codTipoFichero;
	}

	public String getDesTipoFichero() {
		return this.desTipoFichero;
	}

	public void setDesTipoFichero(String desTipoFichero) {
		this.desTipoFichero = desTipoFichero;
	}

	public List<ControlFichero> getControlFicheros() {
		return this.controlFicheros;
	}

	public void setControlFicheros(List<ControlFichero> controlFicheros) {
		this.controlFicheros = controlFicheros;
	}

	public ControlFichero addControlFichero(ControlFichero controlFichero) {
		getControlFicheros().add(controlFichero);
		controlFichero.setTipoFichero(this);

		return controlFichero;
	}

	public ControlFichero removeControlFichero(ControlFichero controlFichero) {
		getControlFicheros().remove(controlFichero);
		controlFichero.setTipoFichero(null);

		return controlFichero;
	}

}