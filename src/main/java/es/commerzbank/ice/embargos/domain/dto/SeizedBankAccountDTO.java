package es.commerzbank.ice.embargos.domain.dto;

import java.math.BigDecimal;

public class SeizedBankAccountDTO {

	private Long idSeizedBankAccount;
	private String iban;
	private String bankAccountStatus;
	private String bankAccountCurrency;
	private BigDecimal amount;
	private BigDecimal fxRate;
	private SeizureActionDTO seizureAction;
	private SeizureStatusDTO seizureStatus;

	public Long getIdSeizedBankAccount() {
		return idSeizedBankAccount;
	}

	public String getBankAccountStatus() {
		return bankAccountStatus;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getBankAccountCurrency() {
		return bankAccountCurrency;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public BigDecimal getFxRate() {
		return fxRate;
	}

	public SeizureActionDTO getSeizureAction() {
		return seizureAction;
	}

	public SeizureStatusDTO getSeizureStatus() {
		return seizureStatus;
	}

	public void setIdSeizedBankAccount(Long idSeizedBankAccount) {
		this.idSeizedBankAccount = idSeizedBankAccount;
	}

	public void setBankAccountStatus(String bankAccountStatus) {
		this.bankAccountStatus = bankAccountStatus;
	}

	public void setBankAccountCurrency(String bankAccountCurrency) {
		this.bankAccountCurrency = bankAccountCurrency;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void setFxRate(BigDecimal fxRate) {
		this.fxRate = fxRate;
	}

	public void setSeizureAction(SeizureActionDTO seizureAction) {
		this.seizureAction = seizureAction;
	}

	public void setSeizureStatus(SeizureStatusDTO seizureStatus) {
		this.seizureStatus = seizureStatus;
	}



}
