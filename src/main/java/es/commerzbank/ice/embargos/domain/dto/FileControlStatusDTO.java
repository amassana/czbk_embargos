package es.commerzbank.ice.embargos.domain.dto;

public class FileControlStatusDTO {

	private Long status;
	private String description;
	
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
