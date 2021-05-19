package es.commerzbank.ice.embargos.domain.dto;

public class CGPJPetitionDTO {
	private String petitionCode;
	private IntegradorRequestStatusDTO status;
	private FileControlDTO fileControlDTO;

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

	public FileControlDTO getFileControlDTO() {
		return fileControlDTO;
	}

	public void setFileControlDTO(FileControlDTO fileControlDTO) {
		this.fileControlDTO = fileControlDTO;
	}
}
