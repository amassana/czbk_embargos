package es.commerzbank.ice.embargos.domain.dto;

import java.util.Date;
import java.util.List;

public class LiftingDTO {
	private Long codLifting;
	private Long codLock;
	private LiftingStatusDTO status;
	private String nif;
	private String name;
	private String numSeizure;
	private Double amountDebt;
	private Double amountRaised;
	private Date modifiedDate;
	private String modifiedUser;
	private List<BankAccountLiftingDTO> accounts;
	
	
	public LiftingDTO() {}
	
	
	
	public LiftingDTO(Long codLifting, Long codLock, LiftingStatusDTO status, String nif, String name, String numSeizure, Double amountDebt,
			Double amountRaised, Date modifiedDate, String modifiedUser, List<BankAccountLiftingDTO> accounts) {
		this.codLifting = codLifting;
		this.status = status;
		this.nif = nif;
		this.name = name;
		this.numSeizure = numSeizure;
		this.amountDebt = amountDebt;
		this.amountRaised = amountRaised;
		this.modifiedDate = modifiedDate;
		this.modifiedUser = modifiedUser;
		this.accounts = accounts;
		this.codLock = codLock;
	}



	public Long getCodLifting() {
		return codLifting;
	}
	public void setCodLifting(Long codLifting) {
		this.codLifting = codLifting;
	}
	public LiftingStatusDTO getStatus() {
		return status;
	}
	public void setStatus(LiftingStatusDTO status) {
		this.status = status;
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
	public String getNumSeizure() {
		return numSeizure;
	}
	public void setNumSeizure(String numSeizure) {
		this.numSeizure = numSeizure;
	}
	public Double getAmountDebt() {
		return amountDebt;
	}
	public void setAmountDebt(Double amountDebt) {
		this.amountDebt = amountDebt;
	}
	public Double getAmountRaised() {
		return amountRaised;
	}
	public void setAmountRaised(Double amountRaised) {
		this.amountRaised = amountRaised;
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
	public List<BankAccountLiftingDTO> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<BankAccountLiftingDTO> accounts) {
		this.accounts = accounts;
	}
	public Long getCodLock() {
		return codLock;
	}
	public void setCodLock(Long codLock) {
		this.codLock = codLock;
	}
}
