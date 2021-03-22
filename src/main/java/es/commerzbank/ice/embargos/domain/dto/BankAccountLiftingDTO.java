package es.commerzbank.ice.embargos.domain.dto;

public class BankAccountLiftingDTO {
	private Long codLiftingAccount;
	private String iban;
	private String account;
	private String bankAccountStatus;
	private Double change;
	private Double amount;
	private LiftingStatusDTO status;
	private String codCurrency;
	
	public BankAccountLiftingDTO() {}
	
	public BankAccountLiftingDTO(Long codLiftingAccount, String iban, String account, Double change, Double amount, String codCurrency, LiftingStatusDTO status, String bankAccountStatus) {
		super();
		this.codLiftingAccount = codLiftingAccount;
		this.iban = iban;
		this.account = account;
		this.change = change;
		this.amount = amount;
		this.status = status;
		this.codCurrency = codCurrency;
		this.setBankAccountStatus(bankAccountStatus);
	}



	public Long getCodLiftingAccount() {
		return codLiftingAccount;
	}
	public void setCodLiftingAccount(Long codLiftingAccount) {
		this.codLiftingAccount = codLiftingAccount;
	}
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public Double getChange() {
		return change;
	}
	public void setChange(Double change) {
		this.change = change;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getCodCurrency() {
		return codCurrency;
	}
	public void setCodCurrency(String codCurrency) {
		this.codCurrency = codCurrency;
	}
	public LiftingStatusDTO getStatus() {
		return status;
	}
	public void setStatus(LiftingStatusDTO status) {
		this.status = status;
	}

	/**
	 * @return the bankAccountStatus
	 */
	public String getBankAccountStatus() {
		return bankAccountStatus;
	}

	/**
	 * @param bankAccountStatus the bankAccountStatus to set
	 */
	public void setBankAccountStatus(String bankAccountStatus) {
		this.bankAccountStatus = bankAccountStatus;
	}
}
