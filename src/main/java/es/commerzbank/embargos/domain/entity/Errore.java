package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ERRORES database table.
 * 
 */
@Entity
@Table(name="ERRORES")
@NamedQuery(name="Errore.findAll", query="SELECT e FROM Errore e")
public class Errore implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COD_ERROR", unique=true, nullable=false)
	private long codError;

	@Column(name="DESCRIPCION_ERROR", length=240)
	private String descripcionError;

	@Column(name="NUMERO_ERROR", length=3)
	private String numeroError;

	//bi-directional many-to-one association to ErrorTraba
	@OneToMany(mappedBy="errore")
	private List<ErrorTraba> errorTrabas;

	public Errore() {
	}

	public long getCodError() {
		return this.codError;
	}

	public void setCodError(long codError) {
		this.codError = codError;
	}

	public String getDescripcionError() {
		return this.descripcionError;
	}

	public void setDescripcionError(String descripcionError) {
		this.descripcionError = descripcionError;
	}

	public String getNumeroError() {
		return this.numeroError;
	}

	public void setNumeroError(String numeroError) {
		this.numeroError = numeroError;
	}

	public List<ErrorTraba> getErrorTrabas() {
		return this.errorTrabas;
	}

	public void setErrorTrabas(List<ErrorTraba> errorTrabas) {
		this.errorTrabas = errorTrabas;
	}

	public ErrorTraba addErrorTraba(ErrorTraba errorTraba) {
		getErrorTrabas().add(errorTraba);
		errorTraba.setErrore(this);

		return errorTraba;
	}

	public ErrorTraba removeErrorTraba(ErrorTraba errorTraba) {
		getErrorTrabas().remove(errorTraba);
		errorTraba.setErrore(null);

		return errorTraba;
	}

}