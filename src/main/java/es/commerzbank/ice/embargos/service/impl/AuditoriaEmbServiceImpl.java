package es.commerzbank.ice.embargos.service.impl;

import es.commerzbank.ice.embargos.domain.dto.AuditoriaEmbDto;
import es.commerzbank.ice.embargos.domain.dto.AuditoriaEmbFilter;
import es.commerzbank.ice.embargos.domain.entity.AuditoriaEmb;
import es.commerzbank.ice.embargos.domain.entity.AuditoriaEmb_;
import es.commerzbank.ice.embargos.domain.mapper.AuditoriaEmbMapper;
import es.commerzbank.ice.embargos.repository.AuditoriaEmbRepo;
import es.commerzbank.ice.embargos.service.AuditoriaEmbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuditoriaEmbServiceImpl implements AuditoriaEmbService {

	private static final Logger logger = LoggerFactory.getLogger(AuditoriaEmbServiceImpl.class);
	
	@Autowired
	private AuditoriaEmbRepo auditoriaRepo;
	
	@Autowired
	private AuditoriaEmbMapper auditoriaMapper;
	
	@Override
	public Page<AuditoriaEmbDto> listFilterAuditoria(AuditoriaEmbFilter auditoriaFilter, Pageable dataPage)
	{
		Page<AuditoriaEmb> list = null;
		List<AuditoriaEmbDto> response = new ArrayList<>();
		Page<AuditoriaEmbDto> result = null; 
		
		list = auditoriaRepo.findAll(new Specification<AuditoriaEmb>() {

			@Override
			public Predicate toPredicate(Root<AuditoriaEmb> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				
                if (auditoriaFilter.getTabla() != null){
                    predicates.add(criteriaBuilder.equal(root.get(AuditoriaEmb_.tabla), auditoriaFilter.getTabla()));
                }
                
                if (auditoriaFilter.getPkLogico() != null){
                    predicates.add(criteriaBuilder.equal(root.get(AuditoriaEmb_.pkLogico), auditoriaFilter.getPkLogico()));
                }
                
                if (auditoriaFilter.getUsuario() != null){
                    predicates.add(criteriaBuilder.equal(root.get(AuditoriaEmb_.usuario), auditoriaFilter.getUsuario()));
                }
                
                if (auditoriaFilter.getInicio()!=null && auditoriaFilter.getFin()!=null) {
                	predicates.add(criteriaBuilder.between(root.get(AuditoriaEmb_.fecha), auditoriaFilter.getInicio().atStartOfDay(), auditoriaFilter.getFin().atTime(23, 59, 59)));
                }
                else if (auditoriaFilter.getInicio()!=null) {
                	predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(AuditoriaEmb_.fecha), auditoriaFilter.getInicio().atStartOfDay()));
                }
                else if (auditoriaFilter.getFin()!=null) {
                	predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(AuditoriaEmb_.fecha), auditoriaFilter.getFin().atTime(23, 59, 59)));
                }
                
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
			
		}, dataPage);
		
		if (list != null) {
			response = auditoriaMapper.toAuditoriaDto(list.getContent());
			result = new PageImpl<>(response, list.getPageable(), list.getTotalElements());
		}

		return result;
	}

	@Override
	public List<String> listAllTables() {
		List<String> response = new ArrayList<>();
		
		List<String> list = auditoriaRepo.findAllTables();
		if (list != null) {
			response = list;
		}
		
		return response;
	}

	@Override
	public List<String> listAllUsers() {
		List<String> response = new ArrayList<>();
		
		List<String> list = auditoriaRepo.findAllUsers();
		if (list != null) {
			response = list;
		}
		
		return response;
	}

	@Override
	public AuditoriaEmbDto viewAuditoria(Long idAuditoria)
	{
		AuditoriaEmbDto auditoriaDto = null;

		Optional<AuditoriaEmb> optAuditoria = auditoriaRepo.findById(idAuditoria);
			
		if (optAuditoria.isPresent()) {
			auditoriaDto = auditoriaMapper.toAuditoriaDto(optAuditoria.get());
		}

		return auditoriaDto;
	}

	@Override
	public Page<AuditoriaEmbDto> listFilterAuditoria(String pkLogico, String tabla, Pageable dataPage) {
		AuditoriaEmbFilter auditoriaFilter = new AuditoriaEmbFilter();
		auditoriaFilter.setPkLogico(pkLogico);
		auditoriaFilter.setTabla(tabla);
		return listFilterAuditoria(auditoriaFilter, dataPage);
	}

	@Override
	@Transactional(transactionManager="transactionManager", propagation = Propagation.REQUIRES_NEW)
	public void saveAuditoria(AuditoriaEmb auditoria) {
		auditoriaRepo.save(auditoria);
	}

}
