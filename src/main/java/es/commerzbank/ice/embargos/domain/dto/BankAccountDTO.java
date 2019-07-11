package es.commerzbank.ice.embargos.domain.dto;

public class BankAccountDTO {

	private String codeBankAccount;
	private String iban;
	private String statusCode;
	private String statusDescription;
	
	public String getCodeBankAccount() {
		return codeBankAccount;
	}
	public void setCodeBankAccount(String codeBankAccount) {
		this.codeBankAccount = codeBankAccount;
	}
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusDescription() {
		return statusDescription;
	}
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}
	
	
}
