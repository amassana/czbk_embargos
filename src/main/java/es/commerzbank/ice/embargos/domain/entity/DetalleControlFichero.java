package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the DETALLE_CONTROL_FICHERO database table.
 * 
 */
@Entity
@Table(name="DETALLE_CONTROL_FICHERO")
@NamedQuery(name="DetalleControlFichero.findAll", query="SELECT d FROM DetalleControlFichero d")
public class DetalleControlFichero implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DetalleControlFicheroPK id;

	@Column(nullable=false, length=1048)
	private String linea;

	//bi-directional many-to-one association to ControlFichero
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_CONTROL_FICHERO", nullable=false, insertable=false, updatable=false)
	private ControlFichero controlFichero;

	public DetalleControlFichero() {
	}

	public DetalleControlFicheroPK getId() {
		return this.id;
	}

	public void setId(DetalleControlFicheroPK id) {
		this.id = id;
	}

	public String getLinea() {
		return this.linea;
	}

	public void setLinea(String linea) {
		this.linea = linea;
	}

	public ControlFichero getControlFichero() {
		return this.controlFichero;
	}

	public void setControlFichero(ControlFichero controlFichero) {
		this.controlFichero = controlFichero;
	}

}