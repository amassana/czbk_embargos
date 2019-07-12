package es.commerzbank.ice.embargos.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import es.commerzbank.ice.embargos.domain.dto.FileControlDTO;
import es.commerzbank.ice.embargos.domain.dto.FileControlFiltersDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlfichero;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlficheroPK;
import es.commerzbank.ice.embargos.domain.entity.TipoFichero;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.domain.specification.FileControlSpecification;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.FileControlStatusRepository;
import es.commerzbank.ice.embargos.service.Cuaderno63Service;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.InformationPetitionService;

@Service
@Transactional
public class FileControlServiceImpl implements FileControlService{
	
	private static final Logger LOG = LoggerFactory.getLogger(FileControlServiceImpl.class);

	@Autowired
	InformationPetitionService informationPetitionService;
	
	@Autowired
	Cuaderno63Service cuaderno63Service;
	
	@Autowired
	private FileControlMapper fileControlMapper;

	@Autowired
	private FileControlRepository fileControlRepository;
	
	@Autowired
	private FileControlStatusRepository fileControlStatusRepository;
	
	@Override
	public Page<FileControlDTO> fileSearch(FileControlFiltersDTO fileControlFiltersDTO, Pageable pageable) throws Exception{

		List<FileControlDTO> fileSearchResponseDTOList = new ArrayList<>();
				
		Date startDate = fileControlFiltersDTO.getStartDate();
		Date endDate = fileControlFiltersDTO.getEndDate();
		
//		//Validar fechas:
//		if (!validateDates(startDate,endDate)) {
//			throw new Exception("ERROR: incorrect dates");
//		}
		
		Long tipoFicheroLong = fileControlFiltersDTO.getFileType();
		
		TipoFichero tipoFichero = new TipoFichero();
		long codTipoFichero = tipoFicheroLong!=null ? Long.valueOf(fileControlFiltersDTO.getFileType()) : 0;
		tipoFichero.setCodTipoFichero(codTipoFichero);
		
		Specification<ControlFichero> fileControlSpecification = new FileControlSpecification(fileControlFiltersDTO);
		
		Page<ControlFichero> controlFicheroList = fileControlRepository.findAll(fileControlSpecification, pageable);
									
		for (ControlFichero controlFichero : controlFicheroList) {
		
			FileControlDTO fileSearchResponseDTO = fileControlMapper.toFileControlDTO(controlFichero, 
					new Long (1), 
					"targetTEST", 
					new Date());
			
			fileSearchResponseDTOList.add(fileSearchResponseDTO);
		
		}
		
		return new PageImpl<>(fileSearchResponseDTOList, pageable, controlFicheroList.getTotalElements());
		
	}
	
	private boolean validateDates(Date startDate, Date endDate) {
		
		return (startDate != null && endDate!=null && startDate.compareTo(endDate) <= 0);
	}

	@Override
	public FileControlDTO getByCodeFileControl(Long codeFileControl) {

		Optional<ControlFichero> controlFicheroOpt = fileControlRepository.findById(codeFileControl);
		
		if(!controlFicheroOpt.isPresent()) {
			return null;
		}
		
		return fileControlMapper.toFileControlDTO(controlFicheroOpt.get(), 
				new Long (1), 
				"targetTEST", 
				new Date());
	}

	@Override
	public boolean tramitarFicheroInformacion(Long codeFileControl) throws IOException {

		//Obtener el codigo del fichero de control:
		Optional<ControlFichero> controlFicheroOpt = fileControlRepository.findById(codeFileControl);
		
		if(!controlFicheroOpt.isPresent()) {
			return false;
		}
		
		ControlFichero controlFichero = controlFicheroOpt.get();
		
	
		//Solo se puede tramitar si no hay peticiones de informacion pendientes de ser revisadas:
		
		Integer countPendingPetitionCases = 
				informationPetitionService.getCountPendingPetitionCases(controlFichero);

		Integer countReviewedPetitionCases = 
				informationPetitionService.getCountReviewedPetitionCases(controlFichero);
		
		Integer countPetitionCases = 
				informationPetitionService.getCountPetitionCases(controlFichero);
		
		if (countPendingPetitionCases == 0 && (countReviewedPetitionCases.compareTo(countPetitionCases)==0)) {
			
			cuaderno63Service.tramitarFicheroInformacion(codeFileControl);
		
		} else {
			
			if (countPendingPetitionCases > 0) {
				LOG.debug("No se puede tramitar: quedan " + countPendingPetitionCases 
					+ " de peticiones de informacion pendientes de ser revisadas.");
			
			} else if (countReviewedPetitionCases.compareTo(countPetitionCases)!=0) {
				LOG.debug("No se puede tramitar: no se ha revisado todas las peticiones de informacion.");
			}
			
			return false;
		}
		
		return true;
	}

	@Override
	public boolean updateFileControl(Long codeFileControl, FileControlDTO fileControlDTO) {
		
		Optional<ControlFichero> controlFicheroOpt = fileControlRepository.findById(codeFileControl);
		
		if(!controlFicheroOpt.isPresent()) {
			return false;
		}
		
		ControlFichero controlFichero = controlFicheroOpt.get();
		
		//TODO solo actualiza el estado, pendiente resto de campos:
		
		if (fileControlDTO.getStatus()!=null && fileControlDTO.getStatus().getStatus()!=null) {

			EstadoCtrlficheroPK estadoCtrlficheroPK = new EstadoCtrlficheroPK();
			estadoCtrlficheroPK.setCodEstado(fileControlDTO.getStatus().getStatus());
			estadoCtrlficheroPK.setCodTipoFichero(fileControlDTO.getCodeFileType());
			
			Optional<EstadoCtrlfichero> estadoCtrlficheroOpt = fileControlStatusRepository.findById(estadoCtrlficheroPK);
			
			if(!estadoCtrlficheroOpt.isPresent()) {
				return false;
			}
			
			controlFichero.setEstadoCtrlfichero(estadoCtrlficheroOpt.get());
		}
		
		//controlFichero.setFicheroRespuesta("PRUEBA2");
		
		//TODO no se esta guardando el EstadoCtrlfichero, puede ser debido al problema en ControlFichero del "Repeated Columns" de COD_TIPO_FICHERO.
		
		fileControlRepository.save(controlFichero);
		
		return true;
	}


	
}
