package es.commerzbank.ice.embargos.domain.dto;

import java.util.List;

public class FinalResponseDTO {
	private Long codeFinalResponse;
	private String seizureNumber;
	private String nif;
	private String holder;
	private List<FinalResponseBankAccountDTO> list;
	
	
	public Long getCodeFinalResponse() {
		return codeFinalResponse;
	}
	public void setCodeFinalResponse(Long codeFinalResponse) {
		this.codeFinalResponse = codeFinalResponse;
	}
	public String getSeizureNumber() {
		return seizureNumber;
	}
	public void setSeizureNumber(String seizureNumber) {
		this.seizureNumber = seizureNumber;
	}
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public String getHolder() {
		return holder;
	}
	public void setHolder(String holder) {
		this.holder = holder;
	}
	public List<FinalResponseBankAccountDTO> getList() {
		return list;
	}
	public void setList(List<FinalResponseBankAccountDTO> list) {
		this.list = list;
	}
}
