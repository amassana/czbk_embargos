package es.commerzbank.ice.embargos.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.commerzbank.ice.embargos.domain.dto.FileControlDTO;
import es.commerzbank.ice.embargos.domain.dto.FileControlFiltersDTO;
import es.commerzbank.ice.embargos.domain.dto.PetitionDTO;

public interface FileControlService {

	public Page<FileControlDTO> fileSearch(FileControlFiltersDTO fileControlFiltersDTO, Pageable pageable);

	public FileControlDTO getByCodeFileControl(Long codeFileControl) throws Exception;
	
	public List<PetitionDTO> getPeticionesInformacionByCodeFileControl(Long codeFileControl);
	
}
