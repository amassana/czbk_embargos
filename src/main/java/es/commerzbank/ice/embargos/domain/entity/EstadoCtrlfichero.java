package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ESTADO_CTRLFICHERO database table.
 * 
 */
@Entity
@Table(name="ESTADO_CTRLFICHERO")
@NamedQuery(name="EstadoCtrlfichero.findAll", query="SELECT e FROM EstadoCtrlfichero e")
public class EstadoCtrlfichero implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private EstadoCtrlficheroPK id;

	@Column(length=100)
	private String descripcion;

	//bi-directional many-to-one association to ControlFichero
	@OneToMany(mappedBy="estadoCtrlfichero")
	private List<ControlFichero> controlFicheros;

	//bi-directional many-to-one association to TipoFichero
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_TIPO_FICHERO", nullable=false, insertable=false, updatable=false)
	private TipoFichero tipoFichero;

	public EstadoCtrlfichero() {
	}

	public EstadoCtrlfichero (long codEstado, long codTipoFichero) {
	    EstadoCtrlficheroPK estadoCtrlficheroPK = new EstadoCtrlficheroPK();
	    estadoCtrlficheroPK.setCodEstado(codEstado);
	    estadoCtrlficheroPK.setCodTipoFichero(codTipoFichero);
	    this.setId(estadoCtrlficheroPK);
	}
	
	public EstadoCtrlficheroPK getId() {
		return this.id;
	}

	public void setId(EstadoCtrlficheroPK id) {
		this.id = id;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<ControlFichero> getControlFicheros() {
		return this.controlFicheros;
	}

	public void setControlFicheros(List<ControlFichero> controlFicheros) {
		this.controlFicheros = controlFicheros;
	}

	public ControlFichero addControlFichero(ControlFichero controlFichero) {
		getControlFicheros().add(controlFichero);
		controlFichero.setEstadoCtrlfichero(this);

		return controlFichero;
	}

	public ControlFichero removeControlFichero(ControlFichero controlFichero) {
		getControlFicheros().remove(controlFichero);
		controlFichero.setEstadoCtrlfichero(null);

		return controlFichero;
	}

	public TipoFichero getTipoFichero() {
		return this.tipoFichero;
	}

	public void setTipoFichero(TipoFichero tipoFichero) {
		this.tipoFichero = tipoFichero;
	}

}