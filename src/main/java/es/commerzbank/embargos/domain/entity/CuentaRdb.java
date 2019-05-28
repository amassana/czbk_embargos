package es.commerzbank.embargos.domain.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the CUENTA_RDB database table.
 * 
 */
@Entity
@Table(name="CUENTA_RDB")
@NamedQuery(name="CuentaRdb.findAll", query="SELECT c FROM CuentaRdb c")
public class CuentaRdb implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CLAVE_BALANCE", length=6)
	private String claveBalance;

	@Column(name="COMI_APERTURA", length=40)
	private String comiApertura;

	@Column(name="FECHA_ALTA", length=8)
	private String fechaAlta;

	@Column(name="FECHA_AMORTIZACION", length=8)
	private String fechaAmortizacion;

	@Column(name="FECHA_BAJA", length=8)
	private String fechaBaja;

	@Column(name="FECHA_INICIO", length=8)
	private String fechaInicio;

	@Column(name="FECHA_REV_INTERES", length=8)
	private String fechaRevInteres;

	@Column(name="FECHA_VENCIMIENTO", length=8)
	private String fechaVencimiento;

	@Column(name="GARANTIA_CLAVE", length=3)
	private String garantiaClave;

	@Column(name="GARANTIA_FEC_VTO", length=8)
	private String garantiaFecVto;

	@Column(name="GARANTIA_GARANTE", length=20)
	private String garantiaGarante;

	@Column(name="GARANTIA_GRUPO", length=4)
	private String garantiaGrupo;

	@Column(name="GARANTIA_IMPORTE", length=255)
	private String garantiaImporte;

	@Column(name="GARANTIA_ISO", length=3)
	private String garantiaIso;

	@Column(name="GARANTIA_PAIS", length=3)
	private String garantiaPais;

	@Column(name="IMPORTE_CREDITO", length=255)
	private String importeCredito;

	@Column(name="INTERES_ACREEDOR", length=40)
	private String interesAcreedor;

	@Column(name="INTERES_DEUDOR", length=40)
	private String interesDeudor;

	@Column(name="ISO_CODE", length=3)
	private String isoCode;

	@Column(name="NUMERO_CLIENTE", length=10)
	private String numeroCliente;

	@Column(name="PER_AMORTIZACION", length=1)
	private String perAmortizacion;

	@Column(name="PER_REV_INTERES", length=1)
	private String perRevInteres;

	@Column(length=2)
	private String subcuenta;

	public CuentaRdb() {
	}

	public String getClaveBalance() {
		return this.claveBalance;
	}

	public void setClaveBalance(String claveBalance) {
		this.claveBalance = claveBalance;
	}

	public String getComiApertura() {
		return this.comiApertura;
	}

	public void setComiApertura(String comiApertura) {
		this.comiApertura = comiApertura;
	}

	public String getFechaAlta() {
		return this.fechaAlta;
	}

	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public String getFechaAmortizacion() {
		return this.fechaAmortizacion;
	}

	public void setFechaAmortizacion(String fechaAmortizacion) {
		this.fechaAmortizacion = fechaAmortizacion;
	}

	public String getFechaBaja() {
		return this.fechaBaja;
	}

	public void setFechaBaja(String fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public String getFechaInicio() {
		return this.fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaRevInteres() {
		return this.fechaRevInteres;
	}

	public void setFechaRevInteres(String fechaRevInteres) {
		this.fechaRevInteres = fechaRevInteres;
	}

	public String getFechaVencimiento() {
		return this.fechaVencimiento;
	}

	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public String getGarantiaClave() {
		return this.garantiaClave;
	}

	public void setGarantiaClave(String garantiaClave) {
		this.garantiaClave = garantiaClave;
	}

	public String getGarantiaFecVto() {
		return this.garantiaFecVto;
	}

	public void setGarantiaFecVto(String garantiaFecVto) {
		this.garantiaFecVto = garantiaFecVto;
	}

	public String getGarantiaGarante() {
		return this.garantiaGarante;
	}

	public void setGarantiaGarante(String garantiaGarante) {
		this.garantiaGarante = garantiaGarante;
	}

	public String getGarantiaGrupo() {
		return this.garantiaGrupo;
	}

	public void setGarantiaGrupo(String garantiaGrupo) {
		this.garantiaGrupo = garantiaGrupo;
	}

	public String getGarantiaImporte() {
		return this.garantiaImporte;
	}

	public void setGarantiaImporte(String garantiaImporte) {
		this.garantiaImporte = garantiaImporte;
	}

	public String getGarantiaIso() {
		return this.garantiaIso;
	}

	public void setGarantiaIso(String garantiaIso) {
		this.garantiaIso = garantiaIso;
	}

	public String getGarantiaPais() {
		return this.garantiaPais;
	}

	public void setGarantiaPais(String garantiaPais) {
		this.garantiaPais = garantiaPais;
	}

	public String getImporteCredito() {
		return this.importeCredito;
	}

	public void setImporteCredito(String importeCredito) {
		this.importeCredito = importeCredito;
	}

	public String getInteresAcreedor() {
		return this.interesAcreedor;
	}

	public void setInteresAcreedor(String interesAcreedor) {
		this.interesAcreedor = interesAcreedor;
	}

	public String getInteresDeudor() {
		return this.interesDeudor;
	}

	public void setInteresDeudor(String interesDeudor) {
		this.interesDeudor = interesDeudor;
	}

	public String getIsoCode() {
		return this.isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

	public String getNumeroCliente() {
		return this.numeroCliente;
	}

	public void setNumeroCliente(String numeroCliente) {
		this.numeroCliente = numeroCliente;
	}

	public String getPerAmortizacion() {
		return this.perAmortizacion;
	}

	public void setPerAmortizacion(String perAmortizacion) {
		this.perAmortizacion = perAmortizacion;
	}

	public String getPerRevInteres() {
		return this.perRevInteres;
	}

	public void setPerRevInteres(String perRevInteres) {
		this.perRevInteres = perRevInteres;
	}

	public String getSubcuenta() {
		return this.subcuenta;
	}

	public void setSubcuenta(String subcuenta) {
		this.subcuenta = subcuenta;
	}

}