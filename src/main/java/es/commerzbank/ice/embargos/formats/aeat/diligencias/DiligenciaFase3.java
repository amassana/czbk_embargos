package es.commerzbank.ice.embargos.formats.aeat.diligencias;

import java.math.BigDecimal;
import java.util.Date;

public class DiligenciaFase3 {

	private String indicadorRegistro;
	private String nifDeudor;
	private String nombreDeudor;
	private String siglasViaPublica;
	private String nombreViaPublica;
	private String numeroPortal;
	private String letraPortal;
	private String escalera;
	private String piso;    
	private String puerta;  
	private String nombreMunicipio;
	private String codigoPostal;
	private String numeroDiligenciaEmbargo;
	private BigDecimal importeTotalAEmbargar;
	private Date fechaGeneracionDiligencia;
	private String codigoCuentaCliente1;
	private String codigoCuentaCliente2;
	private String codigoCuentaCliente3;
	
	public String getIndicadorRegistro() {
		return indicadorRegistro;
	}
	public void setIndicadorRegistro(String indicadorRegistro) {
		this.indicadorRegistro = indicadorRegistro;
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
	public String getSiglasViaPublica() {
		return siglasViaPublica;
	}
	public void setSiglasViaPublica(String siglasViaPublica) {
		this.siglasViaPublica = siglasViaPublica;
	}
	public String getNombreViaPublica() {
		return nombreViaPublica;
	}
	public void setNombreViaPublica(String nombreViaPublica) {
		this.nombreViaPublica = nombreViaPublica;
	}
	public String getNumeroPortal() {
		return numeroPortal;
	}
	public void setNumeroPortal(String numeroPortal) {
		this.numeroPortal = numeroPortal;
	}
	public String getLetraPortal() {
		return letraPortal;
	}
	public void setLetraPortal(String letraPortal) {
		this.letraPortal = letraPortal;
	}
	public String getEscalera() {
		return escalera;
	}
	public void setEscalera(String escalera) {
		this.escalera = escalera;
	}
	public String getPiso() {
		return piso;
	}
	public void setPiso(String piso) {
		this.piso = piso;
	}
	public String getPuerta() {
		return puerta;
	}
	public void setPuerta(String puerta) {
		this.puerta = puerta;
	}
	public String getNombreMunicipio() {
		return nombreMunicipio;
	}
	public void setNombreMunicipio(String nombreMunicipio) {
		this.nombreMunicipio = nombreMunicipio;
	}
	public String getCodigoPostal() {
		return codigoPostal;
	}
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	public String getNumeroDiligenciaEmbargo() {
		return numeroDiligenciaEmbargo;
	}
	public void setNumeroDiligenciaEmbargo(String numeroDiligenciaEmbargo) {
		this.numeroDiligenciaEmbargo = numeroDiligenciaEmbargo;
	}
	public BigDecimal getImporteTotalAEmbargar() {
		return importeTotalAEmbargar;
	}
	public void setImporteTotalAEmbargar(BigDecimal importeTotalAEmbargar) {
		this.importeTotalAEmbargar = importeTotalAEmbargar;
	}
	public Date getFechaGeneracionDiligencia() {
		return fechaGeneracionDiligencia;
	}
	public void setFechaGeneracionDiligencia(Date fechaGeneracionDiligencia) {
		this.fechaGeneracionDiligencia = fechaGeneracionDiligencia;
	}
	public String getCodigoCuentaCliente1() {
		return codigoCuentaCliente1;
	}
	public void setCodigoCuentaCliente1(String codigoCuentaCliente1) {
		this.codigoCuentaCliente1 = codigoCuentaCliente1;
	}
	public String getCodigoCuentaCliente2() {
		return codigoCuentaCliente2;
	}
	public void setCodigoCuentaCliente2(String codigoCuentaCliente2) {
		this.codigoCuentaCliente2 = codigoCuentaCliente2;
	}
	public String getCodigoCuentaCliente3() {
		return codigoCuentaCliente3;
	}
	public void setCodigoCuentaCliente3(String codigoCuentaCliente3) {
		this.codigoCuentaCliente3 = codigoCuentaCliente3;
	}
		
}
