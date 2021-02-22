package es.commerzbank.ice.embargos.formats.cuaderno63.fase6;

import java.math.BigDecimal;

public class FinFicheroFase6 {

	private Integer codigoRegistro;
	private String codigoNRBE;
	private Integer numeroRegistrosFichero;
	private BigDecimal importeTotalALevantar;
	private BigDecimal importeNuevoTotalRetenido;
	private String nifOrganismoEmisor;
	private String codigoINEOrganismoEmisor;
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
	public BigDecimal getImporteTotalALevantar() {
		return importeTotalALevantar;
	}
	public void setImporteTotalALevantar(BigDecimal importeTotalALevantar) {
		this.importeTotalALevantar = importeTotalALevantar;
	}
	public BigDecimal getImporteNuevoTotalRetenido() {
		return importeNuevoTotalRetenido;
	}
	public void setImporteNuevoTotalRetenido(BigDecimal importeNuevoTotalRetenido) {
		this.importeNuevoTotalRetenido = importeNuevoTotalRetenido;
	}
	public String getNifOrganismoEmisor() {
		return nifOrganismoEmisor;
	}
	public void setNifOrganismoEmisor(String nifOrganismoEmisor) {
		this.nifOrganismoEmisor = nifOrganismoEmisor;
	}
	public String getCodigoINEOrganismoEmisor() {
		return codigoINEOrganismoEmisor;
	}
	public void setCodigoINEOrganismoEmisor(String codigoINEOrganismoEmisor) {
		this.codigoINEOrganismoEmisor = codigoINEOrganismoEmisor;
	}
	public String getNombreOrganismoEmisor() {
		return nombreOrganismoEmisor;
	}
	public void setNombreOrganismoEmisor(String nombreOrganismoEmisor) {
		this.nombreOrganismoEmisor = nombreOrganismoEmisor;
	}
	
	
}
