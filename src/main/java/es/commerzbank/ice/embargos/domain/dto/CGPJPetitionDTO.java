package es.commerzbank.ice.embargos.domain.dto;

public class CGPJPetitionDTO {
	private String petitionCode;
	private IntegradorRequestStatusDTO status;
	private FileControlDTO fileControl;

	public FileControlDTO getFileControl() {
		return fileControl;
	}

	public void setFileControl(FileControlDTO fileControl) {
		this.fileControl = fileControl;
	}

	public String getPetitionCode() {
		return petitionCode;
	}

	public void setPetitionCode(String petitionCode) {
		this.petitionCode = petitionCode;
	}

	public IntegradorRequestStatusDTO getStatus() {
		return status;
	}

	public void setStatus(IntegradorRequestStatusDTO status) {
		this.status = status;
	}
}
