package es.commerzbank.ice.embargos.domain.dto;

public class PetitionDTO {

	private String codePetition;
	private Long codeStatusPetition;
	private InformationPetitionDTO informationPetitionDTO;
	
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
	public InformationPetitionDTO getInformationPetitionDTO() {
		return informationPetitionDTO;
	}
	public void setInformationPetitionDTO(InformationPetitionDTO informationPetitionDTO) {
		this.informationPetitionDTO = informationPetitionDTO;
	}
	

}
