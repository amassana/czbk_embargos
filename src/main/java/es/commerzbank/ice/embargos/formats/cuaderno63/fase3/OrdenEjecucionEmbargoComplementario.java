package es.commerzbank.ice.embargos.formats.cuaderno63.fase3;

import java.math.BigDecimal;

public class OrdenEjecucionEmbargoComplementario {

	private Integer codigoRegistro;
	private String nifDeudor;
	private String nombreDeudor;
	private String domicilioDeudor;
	private String municipio;
	private String codigoPostal;
	private String identificadorDeuda;
	private BigDecimal importeTotalAEmbargar;
	private String textoLibre1;
	private String textoLibre2;
	private String textoLibre3;
	
	public Integer getCodigoRegistro() {
		return codigoRegistro;
	}
	public void setCodigoRegistro(Integer codigoRegistro) {
		this.codigoRegistro = codigoRegistro;
	}
	public String getNifDeudor() {
		return nifDeudor;
	}
	public void setNifDeudor(String nifDeudor) {
		this.nifDeudor = nifDeudor;
	}
	public String getNombreDeudor() {
		return nombreDeudor;
	}
	public void setNombreDeudor(String nombreDeudor) {
		this.nombreDeudor = nombreDeudor;
	}
	public String getDomicilioDeudor() {
		return domicilioDeudor;
	}
	public void setDomicilioDeudor(String domicilioDeudor) {
		this.domicilioDeudor = domicilioDeudor;
	}
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	public String getCodigoPostal() {
		return codigoPostal;
	}
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	public String getIdentificadorDeuda() {
		return identificadorDeuda;
	}
	public void setIdentificadorDeuda(String identificadorDeuda) {
		this.identificadorDeuda = identificadorDeuda;
	}
	public BigDecimal getImporteTotalAEmbargar() {
		return importeTotalAEmbargar;
	}
	public void setImporteTotalAEmbargar(BigDecimal importeTotalAEmbargar) {
		this.importeTotalAEmbargar = importeTotalAEmbargar;
	}
	public String getTextoLibre1() {
		return textoLibre1;
	}
	public void setTextoLibre1(String textoLibre1) {
		this.textoLibre1 = textoLibre1;
	}
	public String getTextoLibre2() {
		return textoLibre2;
	}
	public void setTextoLibre2(String textoLibre2) {
		this.textoLibre2 = textoLibre2;
	}
	public String getTextoLibre3() {
		return textoLibre3;
	}
	public void setTextoLibre3(String textoLibre3) {
		this.textoLibre3 = textoLibre3;
	}
	
}
