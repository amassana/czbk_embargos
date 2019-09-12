package es.commerzbank.ice.embargos.domain.dto;

public class BankAccountLiftingDTO {
	private Long codLiftingAccount;
	private String iban;
	private String account;
	private Double change;
	private Double amount;
	private String indAccounting;
	private String action;
	private String codCurrency;
	
	public BankAccountLiftingDTO() {}
	
	public BankAccountLiftingDTO(Long codLiftingAccount, String iban, String account, Double change, Double amount,
			String indAccounting, String action, String codCurrency) {
		super();
		this.codLiftingAccount = codLiftingAccount;
		this.iban = iban;
		this.account = account;
		this.change = change;
		this.amount = amount;
		this.indAccounting = indAccounting;
		this.action = action;
		this.codCurrency = codCurrency;
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
	public String getIndAccounting() {
		return indAccounting;
	}
	public void setIndAccounting(String indAccounting) {
		this.indAccounting = indAccounting;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getCodCurrency() {
		return codCurrency;
	}
	public void setCodCurrency(String codCurrency) {
		this.codCurrency = codCurrency;
	}
}
