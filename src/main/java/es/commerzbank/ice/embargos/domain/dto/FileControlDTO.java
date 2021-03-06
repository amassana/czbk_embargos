package es.commerzbank.ice.embargos.domain.dto;

import java.util.Date;

public class FileControlDTO {

	private String code;
	private Long codeFileType;
	private FileControlStatusDTO status;
	private String fileName;
	private String requestName;
	private Boolean isProcessed;
	private String fileTarget;
	private Date deliveryDate;
	private Long codeFileOrigin;
	private Long codeFileResponse;
	private Date modifiedDate;
	private String modifiedUser;
	private Boolean isTGSS;
	private Boolean isAEAT;
	private Boolean isCGPJ;
	private Boolean isCuaderno63;
	private String rutaFichero;
	private Date createdDate;
	private Date responseGenerationDate;
	private Boolean hasSeizures;
	private Boolean hasLiftings;
	private Boolean hasExecutions;
	private String associatedFileName;
	
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
	public String getRutaFichero() {
		return rutaFichero;
	}
	public void setRutaFichero(String rutaFichero) {
		this.rutaFichero = rutaFichero;
	}
	
	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	/**
	 * @return the responseGenerationDate
	 */
	public Date getResponseGenerationDate() {
		return responseGenerationDate;
	}
	/**
	 * @param responseGenerationDate the responseGenerationDate to set
	 */
	public void setResponseGenerationDate(Date responseGenerationDate) {
		this.responseGenerationDate = responseGenerationDate;
	}
	public Boolean getIsAEAT() {
		return isAEAT;
	}
	public void setIsAEAT(Boolean isAEAT) {
		this.isAEAT = isAEAT;
	}
	public Boolean getIsCGPJ() {
		return isCGPJ;
	}
	public void setIsCGPJ(Boolean isCGPJ) {
		this.isCGPJ = isCGPJ;
	}

	public Boolean getHasSeizures() {
		return hasSeizures;
	}

	public void setHasSeizures(Boolean hasSeizures) {
		this.hasSeizures = hasSeizures;
	}

	public Boolean getHasLiftings() {
		return hasLiftings;
	}

	public void setHasLiftings(Boolean hasLiftings) {
		this.hasLiftings = hasLiftings;
	}

	public Boolean getHasExecutions() {
		return hasExecutions;
	}

	public void setHasExecutions(Boolean hasExecutions) {
		this.hasExecutions = hasExecutions;
	}

	public String getRequestName() {
		return requestName;
	}

	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}

	public String getAssociatedFileName() {
		return associatedFileName;
	}

	public void setAssociatedFileName(String associatedFileName) {
		this.associatedFileName = associatedFileName;
	}
}
