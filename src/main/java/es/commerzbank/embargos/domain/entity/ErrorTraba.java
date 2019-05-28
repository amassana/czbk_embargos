package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the ERROR_TRABA database table.
 * 
 */
@Entity
@Table(name="ERROR_TRABA")
@NamedQuery(name="ErrorTraba.findAll", query="SELECT e FROM ErrorTraba e")
public class ErrorTraba implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="NUMERO_CAMPO", nullable=false, precision=3)
	private BigDecimal numeroCampo;

	//bi-directional many-to-one association to ControlFichero
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_CONTROL_FICHERO", nullable=false)
	private ControlFichero controlFichero;

	//bi-directional many-to-one association to Errore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_ERROR", nullable=false)
	private Errore errore;

	//bi-directional many-to-one association to Traba
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_TRABA", nullable=false)
	private Traba traba;

	public ErrorTraba() {
	}

	public BigDecimal getNumeroCampo() {
		return this.numeroCampo;
	}

	public void setNumeroCampo(BigDecimal numeroCampo) {
		this.numeroCampo = numeroCampo;
	}

	public ControlFichero getControlFichero() {
		return this.controlFichero;
	}

	public void setControlFichero(ControlFichero controlFichero) {
		this.controlFichero = controlFichero;
	}

	public Errore getErrore() {
		return this.errore;
	}

	public void setErrore(Errore errore) {
		this.errore = errore;
	}

	public Traba getTraba() {
		return this.traba;
	}

	public void setTraba(Traba traba) {
		this.traba = traba;
	}

}