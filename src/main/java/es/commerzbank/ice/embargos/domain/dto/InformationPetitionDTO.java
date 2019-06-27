package es.commerzbank.ice.embargos.domain.dto;

public class InformationPetitionDTO {

	private String nif;
	private String name;
	private AccountDTO account1;
	private AccountDTO account2;
	private AccountDTO account3;
	private AccountDTO account4;
	private AccountDTO account5;
	private AccountDTO account6;
	
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
	public AccountDTO getAccount1() {
		return account1;
	}
	public void setAccount1(AccountDTO account1) {
		this.account1 = account1;
	}
	public AccountDTO getAccount2() {
		return account2;
	}
	public void setAccount2(AccountDTO account2) {
		this.account2 = account2;
	}
	public AccountDTO getAccount3() {
		return account3;
	}
	public void setAccount3(AccountDTO account3) {
		this.account3 = account3;
	}
	public AccountDTO getAccount4() {
		return account4;
	}
	public void setAccount4(AccountDTO account4) {
		this.account4 = account4;
	}
	public AccountDTO getAccount5() {
		return account5;
	}
	public void setAccount5(AccountDTO account5) {
		this.account5 = account5;
	}
	public AccountDTO getAccount6() {
		return account6;
	}
	public void setAccount6(AccountDTO account6) {
		this.account6 = account6;
	}
	
}
