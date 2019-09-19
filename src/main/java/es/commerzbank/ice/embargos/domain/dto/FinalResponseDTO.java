package es.commerzbank.ice.embargos.domain.dto;

public class FinalResponseDTO {
	private Long code;
	private String seizureNumber;
	private String nif;
	private String holder;
	private String iban;
	private Double amount;
	private String key;
	private Double amountLocked;
	private Double amountRaised;
	private Double amountTransfer;
	
	
	public Long getCode() {
		return code;
	}
	public void setCode(Long code) {
		this.code = code;
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
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Double getAmountLocked() {
		return amountLocked;
	}
	public void setAmountLocked(Double amountLocked) {
		this.amountLocked = amountLocked;
	}
	public Double getAmountRaised() {
		return amountRaised;
	}
	public void setAmountRaised(Double amountRaised) {
		this.amountRaised = amountRaised;
	}
	public Double getAmountTransfer() {
		return amountTransfer;
	}
	public void setAmountTransfer(Double amountTransfer) {
		this.amountTransfer = amountTransfer;
	}
}
