package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ESTADO_TRABA database table.
 * 
 */
@Entity
@Table(name="ESTADO_TRABA")
@NamedQuery(name="EstadoTraba.findAll", query="SELECT e FROM EstadoTraba e")
public class EstadoTraba implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_ESTADO", unique=true, nullable=false)
	private long codEstado;

	@Column(name="DES_ESTADO", length=40)
	private String desEstado;

	//bi-directional many-to-one association to CuentaTraba
	@OneToMany(mappedBy="estadoTraba")
	private List<CuentaTraba> cuentaTrabas;

	//bi-directional many-to-one association to HistorialEstadoTraba
	@OneToMany(mappedBy="estadoTraba")
	private List<HistorialEstadoTraba> historialEstadoTrabas;

	//bi-directional many-to-one association to Traba
	@OneToMany(mappedBy="estadoTraba")
	private List<Traba> trabas;

	public EstadoTraba() {
	}

	public long getCodEstado() {
		return this.codEstado;
	}

	public void setCodEstado(long codEstado) {
		this.codEstado = codEstado;
	}

	public String getDesEstado() {
		return this.desEstado;
	}

	public void setDesEstado(String desEstado) {
		this.desEstado = desEstado;
	}

	public List<CuentaTraba> getCuentaTrabas() {
		return this.cuentaTrabas;
	}

	public void setCuentaTrabas(List<CuentaTraba> cuentaTrabas) {
		this.cuentaTrabas = cuentaTrabas;
	}

	public CuentaTraba addCuentaTraba(CuentaTraba cuentaTraba) {
		getCuentaTrabas().add(cuentaTraba);
		cuentaTraba.setEstadoTraba(this);

		return cuentaTraba;
	}

	public CuentaTraba removeCuentaTraba(CuentaTraba cuentaTraba) {
		getCuentaTrabas().remove(cuentaTraba);
		cuentaTraba.setEstadoTraba(null);

		return cuentaTraba;
	}

	public List<HistorialEstadoTraba> getHistorialEstadoTrabas() {
		return this.historialEstadoTrabas;
	}

	public void setHistorialEstadoTrabas(List<HistorialEstadoTraba> historialEstadoTrabas) {
		this.historialEstadoTrabas = historialEstadoTrabas;
	}

	public HistorialEstadoTraba addHistorialEstadoTraba(HistorialEstadoTraba historialEstadoTraba) {
		getHistorialEstadoTrabas().add(historialEstadoTraba);
		historialEstadoTraba.setEstadoTraba(this);

		return historialEstadoTraba;
	}

	public HistorialEstadoTraba removeHistorialEstadoTraba(HistorialEstadoTraba historialEstadoTraba) {
		getHistorialEstadoTrabas().remove(historialEstadoTraba);
		historialEstadoTraba.setEstadoTraba(null);

		return historialEstadoTraba;
	}

	public List<Traba> getTrabas() {
		return this.trabas;
	}

	public void setTrabas(List<Traba> trabas) {
		this.trabas = trabas;
	}

	public Traba addTraba(Traba traba) {
		getTrabas().add(traba);
		traba.setEstadoTraba(this);

		return traba;
	}

	public Traba removeTraba(Traba traba) {
		getTrabas().remove(traba);
		traba.setEstadoTraba(null);

		return traba;
	}

}