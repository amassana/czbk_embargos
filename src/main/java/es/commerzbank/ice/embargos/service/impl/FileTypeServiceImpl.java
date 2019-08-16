package es.commerzbank.ice.embargos.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.embargos.domain.dto.FileControlTypeDTO;
import es.commerzbank.ice.embargos.domain.entity.TipoFichero;
import es.commerzbank.ice.embargos.domain.mapper.FileTypeMapper;
import es.commerzbank.ice.embargos.repository.FileTypeRepository;
import es.commerzbank.ice.embargos.service.FileTypeService;

@Service
@Transactional(transactionManager="transactionManager")
public class FileTypeServiceImpl implements FileTypeService {

	@Autowired
	FileTypeMapper fileTypeMapper;
	
	@Autowired
	FileTypeRepository fileTypeRepository;
	
	@Override
	public List<FileControlTypeDTO> listAllFileType() {

		List<FileControlTypeDTO> fileTypeList = new ArrayList<>();	
		List<TipoFichero> tipoFicheroList = fileTypeRepository.findAll();
		
		FileControlTypeDTO fileControlTypeDTO = null;
		
		for(TipoFichero tipoFichero : tipoFicheroList) {
			
			fileControlTypeDTO = fileTypeMapper.toFileTypeDTO(tipoFichero);
			
			fileTypeList.add(fileControlTypeDTO);

		}
		
		return fileTypeList;
		
	}

}
