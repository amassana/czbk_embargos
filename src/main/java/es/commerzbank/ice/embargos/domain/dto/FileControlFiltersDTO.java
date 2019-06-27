package es.commerzbank.ice.embargos.domain.dto;

import java.util.Date;

public class FileControlFiltersDTO {

	private Long fileType;
	private Long fileStatus;
	private Boolean allDates;
	private Date startDate;
	private Date endDate;
	
	public Long getFileType() {
		return fileType;
	}
	public void setFileType(Long fileType) {
		this.fileType = fileType;
	}
	public Long getFileStatus() {
		return fileStatus;
	}
	public void setFileStatus(Long fileStatus) {
		this.fileStatus = fileStatus;
	}
	public Boolean getAllDates() {
		return allDates;
	}
	public void setAllDates(Boolean allDates) {
		this.allDates = allDates;
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
