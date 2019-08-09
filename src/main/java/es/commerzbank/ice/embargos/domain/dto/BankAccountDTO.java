package es.commerzbank.ice.embargos.domain.dto;

public class BankAccountDTO {

	private String codeBankAccount;
	private String iban;
	private String status;
	
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
