package es.commerzbank.ice.embargos.domain.dto;

import java.math.BigDecimal;
import java.util.List;

public class FinalResponseDTO {
	private Long codeFinalResponse;
	private String seizureNumber;
	private String nif;
	private String holder;
	private List<FinalResponseBankAccountDTO> list;
	private BigDecimal requestedSeizureAmount;
	private BigDecimal seizedAmount;
	private BigDecimal liftedAmount;
	private BigDecimal netAmount;

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

	public BigDecimal getRequestedSeizureAmount() {
		return requestedSeizureAmount;
	}

	public void setRequestedSeizureAmount(BigDecimal requestedSeizureAmount) {
		this.requestedSeizureAmount = requestedSeizureAmount;
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
