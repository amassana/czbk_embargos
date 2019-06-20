package es.commerzbank.ice.embargos.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.embargos.domain.dto.FileTypeDTO;
import es.commerzbank.ice.embargos.domain.entity.TipoFichero;
import es.commerzbank.ice.embargos.domain.mapper.FileTypeMapper;
import es.commerzbank.ice.embargos.repository.FileTypeRepository;
import es.commerzbank.ice.embargos.service.FileTypeService;

@Service
@Transactional
public class FileTypeServiceImpl implements FileTypeService {

	@Autowired
	FileTypeMapper fileTypeMapper;
	
	@Autowired
	FileTypeRepository fileTypeRepository;
	
	@Override
	public List<FileTypeDTO> listAllFileType() {

		List<FileTypeDTO> fileTypeList = new ArrayList<>();	
		List<TipoFichero> tipoFicheroList = fileTypeRepository.findAll();
		
		FileTypeDTO fileTypeDTO = null;
		
		for(TipoFichero tipoFichero : tipoFicheroList) {
			
			fileTypeDTO = fileTypeMapper.toFileTypeDTO(tipoFichero);
			
			fileTypeList.add(fileTypeDTO);

		}
		
		return fileTypeList;
		
	}

}
