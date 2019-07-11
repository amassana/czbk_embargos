package es.commerzbank.ice.embargos.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.commerzbank.ice.embargos.domain.dto.FileControlDTO;
import es.commerzbank.ice.embargos.domain.dto.PetitionCaseDTO;
import es.commerzbank.ice.embargos.domain.dto.PetitionDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.Peticion;
import es.commerzbank.ice.embargos.domain.mapper.PetitionMapper;
import es.commerzbank.ice.embargos.repository.PetitionRepository;
import es.commerzbank.ice.embargos.service.Cuaderno63Service;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.InformationPetitionService;
import es.commerzbank.ice.embargos.service.PetitionService;

@Service
@Transactional
public class PetitionServiceImpl implements PetitionService{

	private static final Logger LOG = LoggerFactory.getLogger(PetitionServiceImpl.class);
	
	@Autowired
	private FileControlService fileControlService;
	
	@Autowired
	private InformationPetitionService informationPetitionService;
	
	@Autowired
	private PetitionMapper petitionMapper;
		
	@Autowired
	private Cuaderno63Service cuaderno63Service;
	
	@Autowired
	private PetitionRepository petitionRepository;
	
	@Override
	public PetitionDTO getByCodeFileControl(Long codeFileControl) {
		
		
		ControlFichero controlFichero = new ControlFichero();
		controlFichero.setCodControlFichero(codeFileControl);
	
		Peticion peticion = petitionRepository.findByControlFichero(controlFichero);

		PetitionDTO petitionDTO = petitionMapper.toPetitionDTO(peticion);
		
		if (peticion!=null) {
			
			List<PetitionCaseDTO> peticionInformacionList = 
					informationPetitionService.getAllByControlFichero(controlFichero);
			
			petitionDTO.setInformationPetitionList(peticionInformacionList);
			
			FileControlDTO fileControlDTO = fileControlService.getByCodeFileControl(codeFileControl);
			petitionDTO.setFileControl(fileControlDTO);
		
		}
		
		return petitionDTO;
	}

	
	@Override
	public boolean tramitarFicheroInformacion(String codePetition) throws IOException {

		//Obtener el codigo del fichero de control:
		Optional<Peticion> peticionOpt = petitionRepository.findById(codePetition);
		
		if(!peticionOpt.isPresent()) {
			return false;
		}
		
		Long codeFileControl = peticionOpt.get().getControlFichero().getCodControlFichero();
		
		//Solo se puede tramitar si no hay peticiones de informacion pendientes de ser revisadas:
		
		Integer countInformationPetitionInPendingCase = 
				informationPetitionService.getCountPendingCases(peticionOpt.get().getControlFichero());
		
		if (countInformationPetitionInPendingCase == 0) {
			cuaderno63Service.tramitarFicheroInformacion(codeFileControl);
		} else {
			
			LOG.debug("No se puede tramitar: quedan " + countInformationPetitionInPendingCase 
					+ " de peticiones de informacion pendientes de ser revisadas.");
			
			return false;
		}
		
		return true;
	}

}
