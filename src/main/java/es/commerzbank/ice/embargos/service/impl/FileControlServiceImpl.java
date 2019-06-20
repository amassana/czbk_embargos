package es.commerzbank.ice.embargos.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.commerzbank.ice.embargos.domain.dto.AccountDTO;
import es.commerzbank.ice.embargos.domain.dto.FileControlDTO;
import es.commerzbank.ice.embargos.domain.dto.FileControlFiltersDTO;
import es.commerzbank.ice.embargos.domain.dto.InformationPetitionDTO;
import es.commerzbank.ice.embargos.domain.dto.PetitionDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.TipoFichero;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.service.FileControlService;

@Service
@Transactional
public class FileControlServiceImpl implements FileControlService{
	
	@Autowired
	private FileControlRepository fileControlRepository;
	
	@Autowired
	private FileControlMapper fileControlMapper;
	
	@Override
	public Page<FileControlDTO> fileSearch(FileControlFiltersDTO fileControlFiltersDTO, Pageable pageable) {

		Long tipoFicheroLong = Long.valueOf(fileControlFiltersDTO.getFileType());
		
		TipoFichero tipoFichero = new TipoFichero();
		tipoFichero.setCodTipoFichero(tipoFicheroLong);
		
		List<ControlFichero> controlFicheroList = fileControlRepository.findByTipoFichero(tipoFichero, pageable);
		
		List<FileControlDTO> fileSearchResponseDTOList = new ArrayList<>();
						
		for (ControlFichero controlFichero : controlFicheroList) {
		
			FileControlDTO fileSearchResponseDTO = fileControlMapper.toFileControlDTO(controlFichero, 
					"estadoTEST", 
					"targetTEST", 
					new Date(),
					new Date());
			
			fileSearchResponseDTOList.add(fileSearchResponseDTO);
		
		}
		
		return new PageImpl<>(fileSearchResponseDTOList);
		
	}

	@Override
	public FileControlDTO getByCodeFileControl(Long codeFileControl) throws Exception {
		
		Optional<ControlFichero> controlFichero = fileControlRepository.findById(codeFileControl);
		
		if (!controlFichero.isPresent()) {
			
			return null;
		}
		
		return fileControlMapper.toFileControlDTO(controlFichero.get(),
				"estadoTEST", 
				"targetTEST", 
				new Date(),
				new Date());

	}
	
	@Override
	public List<PetitionDTO> getPeticionesInformacionByCodeFileControl(Long codeFileControl) {

		List<PetitionDTO> listPetitionDTO = new ArrayList<>();
		
		List<Object[]> peticiones = fileControlRepository.findPeticionesInformacionByCodeFileControl(codeFileControl);
		
		for (Object[] peticion :peticiones) {
			int i = 0;
			PetitionDTO petitionDTO = new PetitionDTO();
			
			petitionDTO.setCodePetition((String)peticion[i++]);
			petitionDTO.setCodeStatusPetition((Long)peticion[i++]);
			
			InformationPetitionDTO informationPetitionDTO = new InformationPetitionDTO();
			
			informationPetitionDTO.setNif((String)peticion[i++]);
			informationPetitionDTO.setName((String)peticion[i++]);
			
			List<AccountDTO> accountList = new ArrayList<>();
			
			for (int j=0;j<6;j++) {

				String iban = (String)peticion[i++];
				String status = "TEST_STATUS";
				String description = "TEST_DESCRIPTION";
				
				if (iban!=null && !iban.isEmpty()) {
					AccountDTO account = new AccountDTO();	
					account.setIban((iban));
					account.setStatus(status);
					account.setDescription(description);
			
					accountList.add(account);	
				}
				
			}
			
			informationPetitionDTO.setAccountList(accountList);
			
			petitionDTO.setInformationPetitionDTO(informationPetitionDTO);
			
			listPetitionDTO.add(petitionDTO);
			
		}
		
		return listPetitionDTO;
	}
	
}
