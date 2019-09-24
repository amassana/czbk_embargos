package es.commerzbank.ice.embargos.domain.dto;

public class FinalResponseBankAccountDTO {
	private Long codBankAccount;
	private String iban;
	private Double amount;
	private String key;
	private Double amountLocked;
	private Double amountRaised;
	private Double amountTransfer;
	
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Double getAmountLocked() {
		return amountLocked;
	}
	public void setAmountLocked(Double amountLocked) {
		this.amountLocked = amountLocked;
	}
	public Double getAmountRaised() {
		return amountRaised;
	}
	public void setAmountRaised(Double amountRaised) {
		this.amountRaised = amountRaised;
	}
	public Double getAmountTransfer() {
		return amountTransfer;
	}
	public void setAmountTransfer(Double amountTransfer) {
		this.amountTransfer = amountTransfer;
	}
	public Long getCodBankAccount() {
		return codBankAccount;
	}
	public void setCodBankAccount(Long codBankAccount) {
		this.codBankAccount = codBankAccount;
	}
}
