package es.commerzbank.ice.embargos.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.commerzbank.ice.embargos.domain.dto.PetitionDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.Peticion;
import es.commerzbank.ice.embargos.domain.mapper.PetitionMapper;
import es.commerzbank.ice.embargos.repository.PetitionRepository;
import es.commerzbank.ice.embargos.service.PetitionService;

@Service
@Transactional
public class PetitionServiceImpl implements PetitionService{

	@Autowired
	private PetitionMapper petitionMapper;
	
	@Autowired
	private PetitionRepository petitionRepository;
	
	@Override
	public Page<PetitionDTO> getByCodeFileControl(Long codeFileControl, Pageable pageable) {
		
		List<PetitionDTO> peticionDTOList = new ArrayList<>();
		
		ControlFichero controlFichero = new ControlFichero();
		controlFichero.setCodControlFichero(codeFileControl);
	
		Page<Peticion> peticionList = petitionRepository.findByControlFichero(controlFichero, pageable);
		
		PetitionDTO petitionDTO = null;
		for (Peticion peticion : peticionList) {
		
			petitionDTO = petitionMapper.toPetitionDTO(peticion);
			peticionDTOList.add(petitionDTO);
		}
		
		return new PageImpl<>(peticionDTOList, pageable, peticionDTOList.size());
	}

}
