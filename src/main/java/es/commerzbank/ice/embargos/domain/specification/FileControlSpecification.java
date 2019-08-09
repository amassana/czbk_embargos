package es.commerzbank.ice.embargos.domain.specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import es.commerzbank.ice.comun.lib.typeutils.VB6Date;
import es.commerzbank.ice.embargos.domain.dto.FileControlFiltersDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero_;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlfichero;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlficheroPK;
import es.commerzbank.ice.embargos.domain.entity.TipoFichero;
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
		
		long codTipoFichero = 0;
		
		//Filtro por tipo de fichero:
		if (fileControlFiltersDTO.getFileType()!=null) {
			TipoFichero tipoFichero = new TipoFichero();
			codTipoFichero = fileControlFiltersDTO.getFileType()!=null ? Long.valueOf(fileControlFiltersDTO.getFileType()) : 0;
			tipoFichero.setCodTipoFichero(codTipoFichero);
			
			if (codTipoFichero != EmbargosConstants.COD_TIPO_FICHERO_ALL_TYPES) {
				predicate = criteriaBuilder.equal(root.get(ControlFichero_.TIPO_FICHERO), tipoFichero);
				predicates.add(predicate);
			}
		} else {
			predicate = criteriaBuilder.equal(root.get(ControlFichero_.TIPO_FICHERO), -1);
			predicates.add(predicate);
		}

		//Filtro de estado (no aplicar filtro si tipo de Fichero es COD_TIPO_FICHERO_ALL_TYPES (Todos) ):
		if(fileControlFiltersDTO.getStatus()!=null && fileControlFiltersDTO.getStatus().getCode()!=null 
				&& codTipoFichero != EmbargosConstants.COD_TIPO_FICHERO_ALL_TYPES) {
			
			EstadoCtrlfichero estadoCtrlFichero = new EstadoCtrlfichero();
			
			EstadoCtrlficheroPK estadoCtrlFicheroPK = new EstadoCtrlficheroPK();
			
			estadoCtrlFicheroPK.setCodEstado(fileControlFiltersDTO.getStatus().getCode());
			estadoCtrlFicheroPK.setCodTipoFichero(codTipoFichero);
			
			estadoCtrlFichero.setId(estadoCtrlFicheroPK);
			
			predicate = criteriaBuilder.equal(root.get(ControlFichero_.ESTADO_CTRLFICHERO), estadoCtrlFichero);
			predicates.add(predicate);
		}
		
		//Filtro de fechas:
		Date startDate = fileControlFiltersDTO.getStartDate();
		Date endDate = fileControlFiltersDTO.getEndDate();
			
		if (startDate!=null) {
			Integer startDateVB = VB6Date.dateToInt(startDate);
			predicate = criteriaBuilder.ge(root.get(ControlFichero_.FECHA_INCORPORACION), startDateVB);
			predicates.add(predicate);
		}
			
		if (endDate!=null) {
			Integer endDateVB = VB6Date.dateToInt(endDate);
			predicate = criteriaBuilder.le(root.get(ControlFichero_.FECHA_INCORPORACION), endDateVB);
			predicates.add(predicate);
		}
		
				
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}

}
