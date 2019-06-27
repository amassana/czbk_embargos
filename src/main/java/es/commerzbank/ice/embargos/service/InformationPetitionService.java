package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.embargos.domain.dto.InformationPetitionDTO;

public interface InformationPetitionService {

	public InformationPetitionDTO getByCodePetition(Long codePetition);
}
