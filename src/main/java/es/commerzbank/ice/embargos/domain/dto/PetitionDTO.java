package es.commerzbank.ice.embargos.domain.dto;

import java.util.ArrayList;
import java.util.List;

public class PetitionDTO {

	private String codePetition;
	private Long codeStatusPetition;
	private FileControlDTO fileControl;
	private List<PetitionCaseDTO> informationPetitionList = new ArrayList<>();
	
	public String getCodePetition() {
		return codePetition;
	}
	public void setCodePetition(String codePetition) {
		this.codePetition = codePetition;
	}
	public Long getCodeStatusPetition() {
		return codeStatusPetition;
	}
	public void setCodeStatusPetition(Long codeStatusPetition) {
		this.codeStatusPetition = codeStatusPetition;
	}
	public FileControlDTO getFileControl() {
		return fileControl;
	}
	public void setFileControl(FileControlDTO fileControl) {
		this.fileControl = fileControl;
	}
	public List<PetitionCaseDTO> getInformationPetitionList() {
		return informationPetitionList;
	}
	public void setInformationPetitionList(List<PetitionCaseDTO> informationPetitionList) {
		this.informationPetitionList = informationPetitionList;
	}
}
