package es.commerzbank.ice.embargos.domain.dto;

import java.util.Date;

public class ClientLiftingManualDTO {

	private String nif;
	private String debtor;
	private String codLifting;
	private String iban;
	private String amount;
	private String type;
	private Date dateRetention;
	
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public String getDebtor() {
		return debtor;
	}
	public void setDebtor(String debtor) {
		this.debtor = debtor;
	}
	public String getCodLifting() {
		return codLifting;
	}
	public void setCodLifting(String codLifting) {
		this.codLifting = codLifting;
	}
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getDateRetention() {
		return dateRetention;
	}
	public void setDateRetention(Date dateRetention) {
		this.dateRetention = dateRetention;
	}
	
}
