package es.commerzbank.ice.embargos.service;

import java.util.List;

import es.commerzbank.ice.embargos.domain.dto.PetitionCaseDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;

public interface InformationPetitionService {

	public PetitionCaseDTO getByCodeInformationPetition(String codeInformationPetition);
	
	public List<PetitionCaseDTO> getAuditByCodeInformationPetition(String codeInformationPetition);
	
	public List<PetitionCaseDTO> getAllByControlFichero(ControlFichero controlFichero);
	
	public Integer getCountPendingPetitionCases(ControlFichero controlFichero);
	
	public Integer getCountReviewedPetitionCases(ControlFichero controlFichero);
	
	public Integer getCountPetitionCases(ControlFichero controlFichero);
	
	//public boolean savePreloadedBankAccounts(String codeInformationPetition, String nif, Boolean revised, List<String>codeAccountList);
	
	public boolean savePetitionCase(Long codeFileControl, String codePetitionCase, PetitionCaseDTO petitionCase, String userModif);

}
