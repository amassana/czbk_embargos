package es.commerzbank.ice.embargos.formats.cuaderno63.fase5;

import java.util.Date;

public class CabeceraEmisorFase5 {

	private Integer codigoRegistro;
	private Integer codigoNRBE;
	private Integer fase;
	private Date fechaObtencionFicheroOrganismo;
	private String nifOrganismoEmisor;
	private Integer codigoINEOrganismoEmisor;
	private String nombreOrganismoEmisor;
	private Integer versionCuaderno;
	
	public Integer getCodigoRegistro() {
		return codigoRegistro;
	}
	public void setCodigoRegistro(Integer codigoRegistro) {
		this.codigoRegistro = codigoRegistro;
	}
	public Integer getCodigoNRBE() {
		return codigoNRBE;
	}
	public void setCodigoNRBE(Integer codigoNRBE) {
		this.codigoNRBE = codigoNRBE;
	}
	public Integer getFase() {
		return fase;
	}
	public void setFase(Integer fase) {
		this.fase = fase;
	}
	public Date getFechaObtencionFicheroOrganismo() {
		return fechaObtencionFicheroOrganismo;
	}
	public void setFechaObtencionFicheroOrganismo(Date fechaObtencionFicheroOrganismo) {
		this.fechaObtencionFicheroOrganismo = fechaObtencionFicheroOrganismo;
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
	public Integer getVersionCuaderno() {
		return versionCuaderno;
	}
	public void setVersionCuaderno(Integer versionCuaderno) {
		this.versionCuaderno = versionCuaderno;
	}
	
	
}
