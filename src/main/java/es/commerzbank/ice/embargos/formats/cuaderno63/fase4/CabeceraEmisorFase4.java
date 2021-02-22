package es.commerzbank.ice.embargos.formats.cuaderno63.fase4;

import java.util.Date;

public class CabeceraEmisorFase4 {

	private Integer codigoRegistro;
	private String codigoNRBE;
	private Integer fase;
	private Date fechaObtencionFicheroOrganismo;
	private Date fechaObtencionFicheroEntidadDeDeposito;
	private String nifOrganismoEmisor;
	private String codigoINEOrganismoEmisor;
	private String nombreOrganismoEmisor;
	private Integer versionCuaderno;
	
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
	public Date getFechaObtencionFicheroEntidadDeDeposito() {
		return fechaObtencionFicheroEntidadDeDeposito;
	}
	public void setFechaObtencionFicheroEntidadDeDeposito(Date fechaObtencionFicheroEntidadDeDeposito) {
		this.fechaObtencionFicheroEntidadDeDeposito = fechaObtencionFicheroEntidadDeDeposito;
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
	public Integer getVersionCuaderno() {
		return versionCuaderno;
	}
	public void setVersionCuaderno(Integer versionCuaderno) {
		this.versionCuaderno = versionCuaderno;
	}
	
	
}
