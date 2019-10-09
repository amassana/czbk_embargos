package es.commerzbank.ice.embargos.domain.dto;

import java.util.List;

public class SeizureSaveDTO {
	private boolean isReviewed;
	private List<SeizedBankAccountDTO> seizedBankAccountList;
	
	
	public boolean isReviewed() {
		return isReviewed;
	}
	public void setReviewed(boolean isReviewed) {
		this.isReviewed = isReviewed;
	}
	public List<SeizedBankAccountDTO> getSeizedBankAccountList() {
		return seizedBankAccountList;
	}
	public void setSeizedBankAccountList(List<SeizedBankAccountDTO> seizedBankAccountList) {
		this.seizedBankAccountList = seizedBankAccountList;
	}
}
