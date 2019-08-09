package es.commerzbank.ice.embargos.domain.dto;

import java.util.Date;

public class FileControlFiltersDTO {

	private Long fileType;
	private FileControlStatusDTO status;
	private Boolean isProcessed;
	private Date startDate;
	private Date endDate;
	
	public Long getFileType() {
		return fileType;
	}
	public void setFileType(Long fileType) {
		this.fileType = fileType;
	}
	public FileControlStatusDTO getStatus() {
		return status;
	}
	public void setStatus(FileControlStatusDTO status) {
		this.status = status;
	}
	public Boolean getIsProcessed() {
		return isProcessed;
	}
	public void setIsProcessed(Boolean isProcessed) {
		this.isProcessed = isProcessed;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	

	
	
}
