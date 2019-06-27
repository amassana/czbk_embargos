package es.commerzbank.ice.embargos.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.commerzbank.ice.embargos.domain.dto.InformationPetitionDTO;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacion;
import es.commerzbank.ice.embargos.domain.mapper.InformationPetitionMapper;
import es.commerzbank.ice.embargos.repository.InformationPetitionRepository;
import es.commerzbank.ice.embargos.service.InformationPetitionService;

@Service
@Transactional
public class InformationPetitionServiceImpl implements InformationPetitionService{

	@Autowired
	private InformationPetitionMapper informationPetitionMapper;
	
	@Autowired
	private InformationPetitionRepository informationPetitionRepository;

	@Override
	public InformationPetitionDTO getByCodePetition(Long codePetition) {

		Optional<PeticionInformacion> peticionInformacion = informationPetitionRepository.findById(Long.toString(codePetition));
		
		if(!peticionInformacion.isPresent()) {
			return null;

		}
		
		return informationPetitionMapper.toPetitionDTO(peticionInformacion.get(), "statusCodeTEST", "StatusDescTEST");
	}	
}
