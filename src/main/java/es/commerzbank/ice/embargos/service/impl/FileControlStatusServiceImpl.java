package es.commerzbank.ice.embargos.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.commerzbank.ice.embargos.domain.dto.FileControlStatusDTO;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlfichero;
import es.commerzbank.ice.embargos.domain.entity.TipoFichero;
import es.commerzbank.ice.embargos.domain.mapper.FileControlStatusMapper;
import es.commerzbank.ice.embargos.repository.FileControlStatusRepository;
import es.commerzbank.ice.embargos.service.FileControlStatusService;

@Service
@Transactional
public class FileControlStatusServiceImpl implements FileControlStatusService {

	@Autowired
	FileControlStatusMapper fileControlStatusMapper;
	
	@Autowired
	FileControlStatusRepository fileControlStatusRepository;
	
	@Override
	public List<FileControlStatusDTO> listFileControlStatusByFileType(Long idFileType) {

		List<FileControlStatusDTO> fileControlStatusDTOList = new ArrayList<>();
		
		TipoFichero tipoFichero = new TipoFichero();
		tipoFichero.setCodTipoFichero(idFileType);
		
		List<EstadoCtrlfichero> estadoCtrlFicheroList = fileControlStatusRepository.findByTipoFichero(tipoFichero);
		
		for (EstadoCtrlfichero estadoCtrlfichero : estadoCtrlFicheroList) {
			fileControlStatusDTOList.add(fileControlStatusMapper.toFileControlStatusDTO(estadoCtrlfichero));
		}
		
		return fileControlStatusDTOList;
	}

}
