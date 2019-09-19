package es.commerzbank.ice.embargos.service;

import java.util.List;

import es.commerzbank.ice.embargos.domain.dto.PetitionCaseDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;

public interface InformationPetitionService {

	public PetitionCaseDTO getByCodeInformationPetition(Long codeInformationPetition);
	
	public List<PetitionCaseDTO> getAuditByCodeInformationPetition(Long codeInformationPetition);
	
	public List<PetitionCaseDTO> getAllByControlFichero(ControlFichero controlFichero);
	
	public Integer getCountPendingPetitionCases(ControlFichero controlFichero);
	
	public Integer getCountReviewedPetitionCases(ControlFichero controlFichero);
	
	public Integer getCountPetitionCases(ControlFichero controlFichero);
		
	public boolean savePetitionCase(Long codeFileControl, Long codePetitionCase, PetitionCaseDTO petitionCase, String userModif);

}
