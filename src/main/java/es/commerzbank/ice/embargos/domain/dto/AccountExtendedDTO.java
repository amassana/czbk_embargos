package es.commerzbank.ice.embargos.domain.dto;

import java.math.BigDecimal;

public class AccountExtendedDTO extends BankAccountDTO {

	private BigDecimal amount;
	private BigDecimal change;
	private BigDecimal currencyAmount;
	private String currency;
	private String result;
	private String countedStatus;
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getChange() {
		return change;
	}
	public void setChange(BigDecimal change) {
		this.change = change;
	}
	public BigDecimal getCurrencyAmount() {
		return currencyAmount;
	}
	public void setCurrencyAmount(BigDecimal currencyAmount) {
		this.currencyAmount = currencyAmount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getCountedStatus() {
		return countedStatus;
	}
	public void setCountedStatus(String countedStatus) {
		this.countedStatus = countedStatus;
	}
	
	
}
