package es.commerzbank.ice.embargos.domain.dto;

import java.util.Date;

public class PetitionCaseDTO {

	private Long codePetitionCase;
	private String nif;
	private String name;
	private String nameInternal;
	private Boolean isReviewed;
	private Date modifiedDate;
	private String modifiedUser;
	private BankAccountDTO bankAccount1;
	private BankAccountDTO bankAccount2;
	private BankAccountDTO bankAccount3;
	private BankAccountDTO bankAccount4;
	private BankAccountDTO bankAccount5;
	private BankAccountDTO bankAccount6;
	
	public Long getCodePetitionCase() {
		return codePetitionCase;
	}
	public void setCodePetitionCase(Long codePetitionCase) {
		this.codePetitionCase = codePetitionCase;
	}
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameInternal() {
		return nameInternal;
	}
	public void setNameInternal(String nameInternal) {
		this.nameInternal = nameInternal;
	}
	public Boolean getIsReviewed() {
		return isReviewed;
	}
	public void setIsReviewed(Boolean isReviewed) {
		this.isReviewed = isReviewed;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getModifiedUser() {
		return modifiedUser;
	}
	public void setModifiedUser(String modifiedUser) {
		this.modifiedUser = modifiedUser;
	}
	public BankAccountDTO getBankAccount1() {
		return bankAccount1;
	}
	public void setBankAccount1(BankAccountDTO bankAccount1) {
		this.bankAccount1 = bankAccount1;
	}
	public BankAccountDTO getBankAccount2() {
		return bankAccount2;
	}
	public void setBankAccount2(BankAccountDTO bankAccount2) {
		this.bankAccount2 = bankAccount2;
	}
	public BankAccountDTO getBankAccount3() {
		return bankAccount3;
	}
	public void setBankAccount3(BankAccountDTO bankAccount3) {
		this.bankAccount3 = bankAccount3;
	}
	public BankAccountDTO getBankAccount4() {
		return bankAccount4;
	}
	public void setBankAccount4(BankAccountDTO bankAccount4) {
		this.bankAccount4 = bankAccount4;
	}
	public BankAccountDTO getBankAccount5() {
		return bankAccount5;
	}
	public void setBankAccount5(BankAccountDTO bankAccount5) {
		this.bankAccount5 = bankAccount5;
	}
	public BankAccountDTO getBankAccount6() {
		return bankAccount6;
	}
	public void setBankAccount6(BankAccountDTO bankAccount6) {
		this.bankAccount6 = bankAccount6;
	}
	
}
