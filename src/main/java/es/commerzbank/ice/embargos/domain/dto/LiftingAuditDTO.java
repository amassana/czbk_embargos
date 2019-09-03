package es.commerzbank.ice.embargos.domain.dto;

import java.util.Date;

public class LiftingAuditDTO {

	private Long codLifting;
	private int accountingStatement;
	private int executedStatus;
	private String modifiedUser;
	private Date modifiedDate;
	private String debtorDebtCode;
	private boolean isReviewed;
	
	
	public Long getCodLifting() {
		return codLifting;
	}
	public void setCodLifting(Long codLifting) {
		this.codLifting = codLifting;
	}
	public int getAccountingStatement() {
		return accountingStatement;
	}
	public void setAccountingStatement(int accountingStatement) {
		this.accountingStatement = accountingStatement;
	}
	public int getExecutedStatus() {
		return executedStatus;
	}
	public void setExecutedStatus(int executedStatus) {
		this.executedStatus = executedStatus;
	}
	public String getModifiedUser() {
		return modifiedUser;
	}
	public void setModifiedUser(String modifiedUser) {
		this.modifiedUser = modifiedUser;
	}
	public String getDebtorDebtCode() {
		return debtorDebtCode;
	}
	public void setDebtorDebtCode(String debtorDebtCode) {
		this.debtorDebtCode = debtorDebtCode;
	}
	public boolean isReviewed() {
		return isReviewed;
	}
	public void setReviewed(boolean isReviewed) {
		this.isReviewed = isReviewed;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
}
