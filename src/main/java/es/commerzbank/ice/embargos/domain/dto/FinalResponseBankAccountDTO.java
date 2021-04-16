package es.commerzbank.ice.embargos.domain.dto;

import java.math.BigDecimal;

public class FinalResponseBankAccountDTO {
	private Long codBankAccount;
	private String iban;
	private BigDecimal seizedAmount;
	private BigDecimal liftedAmount;
	private BigDecimal netAmount;
	
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	public Long getCodBankAccount() {
		return codBankAccount;
	}
	public void setCodBankAccount(Long codBankAccount) {
		this.codBankAccount = codBankAccount;
	}

	public BigDecimal getSeizedAmount() {
		return seizedAmount;
	}

	public void setSeizedAmount(BigDecimal seizedAmount) {
		this.seizedAmount = seizedAmount;
	}

	public BigDecimal getLiftedAmount() {
		return liftedAmount;
	}

	public void setLiftedAmount(BigDecimal liftedAmount) {
		this.liftedAmount = liftedAmount;
	}

	public BigDecimal getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(BigDecimal netAmount) {
		this.netAmount = netAmount;
	}
}
