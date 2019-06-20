package es.commerzbank.ice.embargos.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.embargos.domain.dto.FileStatusDTO;
import es.commerzbank.ice.embargos.service.FileStatusService;

@Service
@Transactional
public class FileStatusServiceImpl implements FileStatusService{

	@Override
	public List<FileStatusDTO> listAllFileStatus() {

		
		List<FileStatusDTO> fileStatusList = new ArrayList<>();	

		//Mock:
		FileStatusDTO fileStatusDTO = new FileStatusDTO();
		fileStatusDTO.setCode("1");
		fileStatusDTO.setDescription("TEST_STATUS_1");	
		fileStatusList.add(fileStatusDTO);
		fileStatusDTO = new FileStatusDTO();
		fileStatusDTO.setCode("2");
		fileStatusDTO.setDescription("TEST_STATUS_2");
		
		fileStatusList.add(fileStatusDTO);
				
		return fileStatusList;
	}

}
