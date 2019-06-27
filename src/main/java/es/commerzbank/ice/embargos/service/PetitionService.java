package es.commerzbank.ice.embargos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.commerzbank.ice.embargos.domain.dto.PetitionDTO;

public interface PetitionService {

	public Page<PetitionDTO> getByCodeFileControl(Long codeFileControl, Pageable pageable);
}
