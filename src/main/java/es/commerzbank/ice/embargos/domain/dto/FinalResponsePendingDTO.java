package es.commerzbank.ice.embargos.domain.dto;

import java.sql.Timestamp;

public class FinalResponsePendingDTO {
	
	private CommunicatingEntity communicatingEntity;
	private FileControlDTO fileControlDTOF3;
	private FileControlDTO fileControlDTOF4;
	private Timestamp lastDateResponse;
	
	/**
	 * @return the communicatingEntity
	 */
	public CommunicatingEntity getCommunicatingEntity() {
		return communicatingEntity;
	}
	/**
	 * @param communicatingEntity the communicatingEntity to set
	 */
	public void setCommunicatingEntity(CommunicatingEntity communicatingEntity) {
		this.communicatingEntity = communicatingEntity;
	}
	/**
	 * @return the fileControlDTOF3
	 */
	public FileControlDTO getFileControlDTOF3() {
		return fileControlDTOF3;
	}
	/**
	 * @param fileControlDTOF3 the fileControlDTOF3 to set
	 */
	public void setFileControlDTOF3(FileControlDTO fileControlDTOF3) {
		this.fileControlDTOF3 = fileControlDTOF3;
	}
	/**
	 * @return the fileControlDTOF4
	 */
	public FileControlDTO getFileControlDTOF4() {
		return fileControlDTOF4;
	}
	/**
	 * @param fileControlDTOF4 the fileControlDTOF4 to set
	 */
	public void setFileControlDTOF4(FileControlDTO fileControlDTOF4) {
		this.fileControlDTOF4 = fileControlDTOF4;
	}
	/**
	 * @return the lastDateResponse
	 */
	public Timestamp getLastDateResponse() {
		return lastDateResponse;
	}
	/**
	 * @param lastDateResponse the lastDateResponse to set
	 */
	public void setLastDateResponse(Timestamp lastDateResponse) {
		this.lastDateResponse = lastDateResponse;
	}
}
