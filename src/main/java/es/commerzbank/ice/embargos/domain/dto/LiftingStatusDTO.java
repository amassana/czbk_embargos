package es.commerzbank.ice.embargos.domain.dto;

public class LiftingStatusDTO {
	private long code;
	private String description;
	
	
	public long getCode() {
		return code;
	}
	public void setCode(long code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
