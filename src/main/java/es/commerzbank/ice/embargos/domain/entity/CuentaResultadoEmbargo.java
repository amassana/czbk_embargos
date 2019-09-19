package es.commerzbank.ice.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the CUENTA_RESULTADO_EMBARGO database table.
 * 
 */
@Entity
@Table(name="CUENTA_RESULTADO_EMBARGO")
@NamedQuery(name="CuentaResultadoEmbargo.findAll", query="SELECT c FROM CuentaResultadoEmbargo c")
public class CuentaResultadoEmbargo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "cuenta_resultado_embargo_seq_gen", sequenceName = "SEC_CUENTA_RESULTADO_EMBARGO", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cuenta_resultado_embargo_seq_gen")
	@Column(name="COD_CUENTA_RESULTADO_EMBARGO", unique=true, nullable=false)
	private long codCuentaResultadoEmbargo;

	@Column(name="IMPORTE_NETO")
	private BigDecimal importeNeto;

	//bi-directional many-to-one association to CuentaEmbargo
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_CUENTA_EMBARGO")
	private CuentaEmbargo cuentaEmbargo;

	//bi-directional many-to-one association to CuentaTraba
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_CUENTA_TRABA")
	private CuentaTraba cuentaTraba;

	//bi-directional many-to-one association to EstadoResultadoEmbargo
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_ESTADO_RESULTADO_EMBARGO")
	private EstadoResultadoEmbargo estadoResultadoEmbargo;

	//bi-directional many-to-one association to ResultadoEmbargo
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_RESULTADO_EMBARGO", nullable=false)
	private ResultadoEmbargo resultadoEmbargo;

	public CuentaResultadoEmbargo() {
	}

	public long getCodCuentaResultadoEmbargo() {
		return this.codCuentaResultadoEmbargo;
	}

	public void setCodCuentaResultadoEmbargo(long codCuentaResultadoEmbargo) {
		this.codCuentaResultadoEmbargo = codCuentaResultadoEmbargo;
	}

	public BigDecimal getImporteNeto() {
		return this.importeNeto;
	}

	public void setImporteNeto(BigDecimal importeNeto) {
		this.importeNeto = importeNeto;
	}

	public CuentaEmbargo getCuentaEmbargo() {
		return this.cuentaEmbargo;
	}

	public void setCuentaEmbargo(CuentaEmbargo cuentaEmbargo) {
		this.cuentaEmbargo = cuentaEmbargo;
	}

	public CuentaTraba getCuentaTraba() {
		return this.cuentaTraba;
	}

	public void setCuentaTraba(CuentaTraba cuentaTraba) {
		this.cuentaTraba = cuentaTraba;
	}

	public EstadoResultadoEmbargo getEstadoResultadoEmbargo() {
		return this.estadoResultadoEmbargo;
	}

	public void setEstadoResultadoEmbargo(EstadoResultadoEmbargo estadoResultadoEmbargo) {
		this.estadoResultadoEmbargo = estadoResultadoEmbargo;
	}

	public ResultadoEmbargo getResultadoEmbargo() {
		return this.resultadoEmbargo;
	}

	public void setResultadoEmbargo(ResultadoEmbargo resultadoEmbargo) {
		this.resultadoEmbargo = resultadoEmbargo;
	}

}