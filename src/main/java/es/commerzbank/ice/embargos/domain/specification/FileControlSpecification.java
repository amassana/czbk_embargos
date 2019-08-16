package es.commerzbank.ice.embargos.domain.specification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import es.commerzbank.ice.embargos.domain.dto.FileControlFiltersDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero_;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlfichero;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlficheroPK;
import es.commerzbank.ice.utils.EmbargosConstants;

public class FileControlSpecification implements Specification<ControlFichero> {

	private static final long serialVersionUID = 1L;
	
	private FileControlFiltersDTO fileControlFiltersDTO;
	
	public FileControlSpecification (FileControlFiltersDTO fileControlFiltersDTO){
		super();
		this.fileControlFiltersDTO = fileControlFiltersDTO;
	}
	
	@Override
	public Predicate toPredicate(Root<ControlFichero> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		
		List<Predicate> predicates = new ArrayList<>();
				
		Predicate predicate = null;
		
		//Filtro por tipo de fichero:
		if (fileControlFiltersDTO.getFileType()!=null) {
			
			In<Long> inClause = criteriaBuilder.in(root.get(ControlFichero_.TIPO_FICHERO));
			
			for (Long filetype : fileControlFiltersDTO.getFileType()) {
			    inClause.value(filetype);
			}
			
			predicates.add(inClause);
		} else {
			predicate = criteriaBuilder.equal(root.get(ControlFichero_.TIPO_FICHERO), -1);
			predicates.add(predicate);
		}

		//Filtro de estado (aplicar el filtro cuando fileType[] solo tiene 1 elemento, es decir, solo se ha seleccionado
		//un tipo de fichero en el multiselector del frontal):
		if(fileControlFiltersDTO.getStatus()!=null && fileControlFiltersDTO.getStatus().getCode()!=null 
				&& fileControlFiltersDTO.getFileType()!=null && fileControlFiltersDTO.getFileType().length == 1) {
			
			EstadoCtrlfichero estadoCtrlFichero = new EstadoCtrlfichero();
			
			EstadoCtrlficheroPK estadoCtrlFicheroPK = new EstadoCtrlficheroPK();
			
			estadoCtrlFicheroPK.setCodEstado(fileControlFiltersDTO.getStatus().getCode());
			estadoCtrlFicheroPK.setCodTipoFichero(fileControlFiltersDTO.getFileType()[0]);
			
			estadoCtrlFichero.setId(estadoCtrlFicheroPK);
			
			predicate = criteriaBuilder.equal(root.get(ControlFichero_.ESTADO_CTRLFICHERO), estadoCtrlFichero);
			predicates.add(predicate);
		}
		
		//Filtro de fechas:
		Date startDate = fileControlFiltersDTO.getStartDate();
		Date endDate = fileControlFiltersDTO.getEndDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		if (startDate!=null) {
			Long startDateLong = Long.parseLong(sdf.format(startDate));
			predicate = criteriaBuilder.ge(root.get(ControlFichero_.FECHA_INCORPORACION), startDateLong);
			predicates.add(predicate);
		}
			
		if (endDate!=null) {
			Long endDateLong = Long.parseLong(sdf.format(endDate));
			predicate = criteriaBuilder.le(root.get(ControlFichero_.FECHA_INCORPORACION), endDateLong);
			predicates.add(predicate);
		}
		
		//Filtro isPending:
		if (fileControlFiltersDTO.getIsPending() != null && fileControlFiltersDTO.getIsPending())
		{
			predicate = criteriaBuilder.equal(root.get(ControlFichero_.IND_PROCESADO), EmbargosConstants.IND_FLAG_NO);
			predicates.add(predicate);
		
		} else if (fileControlFiltersDTO.getIsPending() != null && !fileControlFiltersDTO.getIsPending()) {
			predicate = criteriaBuilder.equal(root.get(ControlFichero_.IND_PROCESADO), EmbargosConstants.IND_FLAG_SI);
			predicates.add(predicate);			
		}
		
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}

}
