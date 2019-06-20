package es.commerzbank.ice.embargos.domain.dto;

import java.util.List;

public class InformationPetitionDTO {

	private String nif;
	private String name;
	private List<AccountDTO> accountList;
	
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
	public List<AccountDTO> getAccountList() {
		return accountList;
	}
	public void setAccountList(List<AccountDTO> accountList) {
		this.accountList = accountList;
	}
	
	
}
