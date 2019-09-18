package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ESTADO_RESULTADO_EMBARGO database table.
 * 
 */
@Entity
@Table(name="ESTADO_RESULTADO_EMBARGO")
@NamedQuery(name="EstadoResultadoEmbargo.findAll", query="SELECT e FROM EstadoResultadoEmbargo e")
public class EstadoResultadoEmbargo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@Column(name="COD_ESTADO_RESULTADO_EMBARGO", unique=true, nullable=false)
	private long codEstadoResultadoEmbargo;

	@Column(length=50)
	private String descripcion;

	//bi-directional many-to-one association to CuentaResultadoEmbargo
	@OneToMany(mappedBy="estadoResultadoEmbargo")
	private List<CuentaResultadoEmbargo> cuentaResultadoEmbargos;

	//bi-directional many-to-one association to ResultadoEmbargo
	@OneToMany(mappedBy="estadoResultadoEmbargo")
	private List<ResultadoEmbargo> resultadoEmbargos;

	public EstadoResultadoEmbargo() {
	}

	public long getCodEstadoResultadoEmbargo() {
		return this.codEstadoResultadoEmbargo;
	}

	public void setCodEstadoResultadoEmbargo(long codEstadoResultadoEmbargo) {
		this.codEstadoResultadoEmbargo = codEstadoResultadoEmbargo;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<CuentaResultadoEmbargo> getCuentaResultadoEmbargos() {
		return this.cuentaResultadoEmbargos;
	}

	public void setCuentaResultadoEmbargos(List<CuentaResultadoEmbargo> cuentaResultadoEmbargos) {
		this.cuentaResultadoEmbargos = cuentaResultadoEmbargos;
	}

	public CuentaResultadoEmbargo addCuentaResultadoEmbargo(CuentaResultadoEmbargo cuentaResultadoEmbargo) {
		getCuentaResultadoEmbargos().add(cuentaResultadoEmbargo);
		cuentaResultadoEmbargo.setEstadoResultadoEmbargo(this);

		return cuentaResultadoEmbargo;
	}

	public CuentaResultadoEmbargo removeCuentaResultadoEmbargo(CuentaResultadoEmbargo cuentaResultadoEmbargo) {
		getCuentaResultadoEmbargos().remove(cuentaResultadoEmbargo);
		cuentaResultadoEmbargo.setEstadoResultadoEmbargo(null);

		return cuentaResultadoEmbargo;
	}

	public List<ResultadoEmbargo> getResultadoEmbargos() {
		return this.resultadoEmbargos;
	}

	public void setResultadoEmbargos(List<ResultadoEmbargo> resultadoEmbargos) {
		this.resultadoEmbargos = resultadoEmbargos;
	}

	public ResultadoEmbargo addResultadoEmbargo(ResultadoEmbargo resultadoEmbargo) {
		getResultadoEmbargos().add(resultadoEmbargo);
		resultadoEmbargo.setEstadoResultadoEmbargo(this);

		return resultadoEmbargo;
	}

	public ResultadoEmbargo removeResultadoEmbargo(ResultadoEmbargo resultadoEmbargo) {
		getResultadoEmbargos().remove(resultadoEmbargo);
		resultadoEmbargo.setEstadoResultadoEmbargo(null);

		return resultadoEmbargo;
	}

}