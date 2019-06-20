package es.commerzbank.ice.embargos.domain.dto;

import java.util.Date;

public class FileControlDTO {

	private String codeFileControl;
	private String fileName;
	private String fileStatus;
	private String fileTarget;
	private Date deliveryDate;
	private Date phase6DeliveryDate;
	private Long codeFileType;
	
	public String getCodeFileControl() {
		return codeFileControl;
	}

	public void setCodeFileControl(String codeFileControl) {
		this.codeFileControl = codeFileControl;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	public Date getPhase6DeliveryDate() {
		return phase6DeliveryDate;
	}

	public void setPhase6DeliveryDate(Date phase6DeliveryDate) {
		this.phase6DeliveryDate = phase6DeliveryDate;
	}
	
	public Long getCodeFileType() {
		return codeFileType;
	}

	public void setCodeFileType(Long codeFileType) {
		this.codeFileType = codeFileType;
	}
}
