package es.commerzbank.ice.embargos.domain.dto;

import java.util.Date;

public class FileControlDTO {

	private String code;
	private Long codeFileType;
	private FileControlStatusDTO status;
	private String fileName;
	private String responseFileName;
	private Boolean isProcessed;
	private String fileStatus;
	private String fileTarget;
	private Date deliveryDate;
	private Date modifiedDate;
	private String modifiedUser;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Long getCodeFileType() {
		return codeFileType;
	}
	public void setCodeFileType(Long codeFileType) {
		this.codeFileType = codeFileType;
	}
	public FileControlStatusDTO getStatus() {
		return status;
	}
	public void setStatus(FileControlStatusDTO status) {
		this.status = status;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getResponseFileName() {
		return responseFileName;
	}
	public void setResponseFileName(String responseFileName) {
		this.responseFileName = responseFileName;
	}
	public Boolean getIsProcessed() {
		return isProcessed;
	}
	public void setIsProcessed(Boolean isProcessed) {
		this.isProcessed = isProcessed;
	}
	public String getFileStatus() {
		return fileStatus;
	}
	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}
	public String getFileTarget() {
		return fileTarget;
	}
	public void setFileTarget(String fileTarget) {
		this.fileTarget = fileTarget;
	}
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getModifiedUser() {
		return modifiedUser;
	}
	public void setModifiedUser(String modifiedUser) {
		this.modifiedUser = modifiedUser;
	}
	

	
}
