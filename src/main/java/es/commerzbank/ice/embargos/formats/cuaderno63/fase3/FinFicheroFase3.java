package es.commerzbank.ice.embargos.formats.cuaderno63.fase3;

import java.math.BigDecimal;

public class FinFicheroFase3 {

	private Integer codigoRegistro;
	private String codigoNRBE;
	private Integer numeroRegistrosFichero;
	private BigDecimal importeTotalAEmbargar;
	private String nifOrganismoEmisor;
	private Integer codigoINEOrganismoEmisor;
	private String nombreOrganismoEmisor;
	
	public Integer getCodigoRegistro() {
		return codigoRegistro;
	}
	public void setCodigoRegistro(Integer codigoRegistro) {
		this.codigoRegistro = codigoRegistro;
	}
	public String getCodigoNRBE() {
		return codigoNRBE;
	}
	public void setCodigoNRBE(String codigoNRBE) {
		this.codigoNRBE = codigoNRBE;
	}
	public Integer getNumeroRegistrosFichero() {
		return numeroRegistrosFichero;
	}
	public void setNumeroRegistrosFichero(Integer numeroRegistrosFichero) {
		this.numeroRegistrosFichero = numeroRegistrosFichero;
	}
	public BigDecimal getImporteTotalAEmbargar() {
		return importeTotalAEmbargar;
	}
	public void setImporteTotalAEmbargar(BigDecimal importeTotalAEmbargar) {
		this.importeTotalAEmbargar = importeTotalAEmbargar;
	}
	public String getNifOrganismoEmisor() {
		return nifOrganismoEmisor;
	}
	public void setNifOrganismoEmisor(String nifOrganismoEmisor) {
		this.nifOrganismoEmisor = nifOrganismoEmisor;
	}
	public Integer getCodigoINEOrganismoEmisor() {
		return codigoINEOrganismoEmisor;
	}
	public void setCodigoINEOrganismoEmisor(Integer codigoINEOrganismoEmisor) {
		this.codigoINEOrganismoEmisor = codigoINEOrganismoEmisor;
	}
	public String getNombreOrganismoEmisor() {
		return nombreOrganismoEmisor;
	}
	public void setNombreOrganismoEmisor(String nombreOrganismoEmisor) {
		this.nombreOrganismoEmisor = nombreOrganismoEmisor;
	}
	
}
