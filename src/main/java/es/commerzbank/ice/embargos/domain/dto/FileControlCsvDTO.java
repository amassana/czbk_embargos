package es.commerzbank.ice.embargos.domain.dto;

import java.util.Date;

public class FileControlCsvDTO {

	private String code;
	private Long codeFileType;
	private Long codeStatus;
	private String descriptionStatus;
	private String fileName;
	private Boolean isProcessed;
	private String fileTarget;
	private Date deliveryDate;
	private Long codeFileOrigin;
	private Long codeFileResponse;
	private Date modifiedDate;
	private String modifiedUser;
	private Boolean isTGSS;
	private Boolean isCuaderno63;
	
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
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Boolean getIsProcessed() {
		return isProcessed;
	}
	public void setIsProcessed(Boolean isProcessed) {
		this.isProcessed = isProcessed;
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
	public Long getCodeFileOrigin() {
		return codeFileOrigin;
	}
	public void setCodeFileOrigin(Long codeFileOrigin) {
		this.codeFileOrigin = codeFileOrigin;
	}
	public Long getCodeFileResponse() {
		return codeFileResponse;
	}
	public void setCodeFileResponse(Long codeFileResponse) {
		this.codeFileResponse = codeFileResponse;
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
	public Boolean getIsTGSS() {
		return isTGSS;
	}
	public void setIsTGSS(Boolean isTGSS) {
		this.isTGSS = isTGSS;
	}
	public Boolean getIsCuaderno63() {
		return isCuaderno63;
	}
	public void setIsCuaderno63(Boolean isCuaderno63) {
		this.isCuaderno63 = isCuaderno63;
	}
	public Long getCodeStatus() {
		return codeStatus;
	}
	public void setCodeStatus(Long codeStatus) {
		this.codeStatus = codeStatus;
	}
	public String getDescriptionStatus() {
		return descriptionStatus;
	}
	public void setDescriptionStatus(String descriptionStatus) {
		this.descriptionStatus = descriptionStatus;
	}
		
}
