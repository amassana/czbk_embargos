package es.commerzbank.ice.embargos.domain.dto;

public class BankAccountLiftingDTO {
	private Long codLiftingAccount;
	private String iban;
	private String account;
	private Double change;
	private Double amount;
	private Item indAccounting;
	private String codCurrency;
	
	public BankAccountLiftingDTO() {}
	
	public BankAccountLiftingDTO(Long codLiftingAccount, String iban, String account, Double change, Double amount,
			Item indAccounting, String codCurrency) {
		super();
		this.codLiftingAccount = codLiftingAccount;
		this.iban = iban;
		this.account = account;
		this.change = change;
		this.amount = amount;
		this.indAccounting = indAccounting;
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
	public Item getIndAccounting() {
		return indAccounting;
	}
	public void setIndAccounting(Item indAccounting) {
		this.indAccounting = indAccounting;
	}
	public String getCodCurrency() {
		return codCurrency;
	}
	public void setCodCurrency(String codCurrency) {
		this.codCurrency = codCurrency;
	}
}
