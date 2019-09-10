package es.commerzbank.ice.embargos.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import es.commerzbank.ice.embargos.domain.dto.Representative;
import es.commerzbank.ice.embargos.domain.entity.Apoderados;
import es.commerzbank.ice.embargos.domain.entity.Apoderados_;
import es.commerzbank.ice.embargos.domain.mapper.RepresentativeMapper;
import es.commerzbank.ice.embargos.repository.ApoderadosRepository;
import es.commerzbank.ice.embargos.service.RepresentativeService;
import es.commerzbank.ice.utils.EmbargosConstants;

@Service
public class RepresentativeServiceImpl implements RepresentativeService {

	private static final Logger logger = LoggerFactory.getLogger(RepresentativeServiceImpl.class);
	
	@Autowired
	private ApoderadosRepository apoderadosRepository;
	
	@Autowired
	private RepresentativeMapper representativeMapper;
	
	@Override
	public boolean createUpdateRepresentative(Representative representative, String user) {
		logger.info("createUpdateRepresentative - start");
		Apoderados result = null, apoderado;
		boolean response = true;
		
		apoderado = representativeMapper.toApoderado(representative);
		
		if (apoderado != null) {
			apoderado.setIndActivo(EmbargosConstants.IND_FLAG_SI);
			apoderado.setfUltimaModificacion(new Timestamp(System.currentTimeMillis()));
			apoderado.setUsuUltimaModificacion(user);
			
			result = apoderadosRepository.save(apoderado);
			
			if (result == null || result.getId() <= 0) {
				response = false;
			}
		} else {
			response = false;
		}
		
		logger.info("createUpdateRepresentative - end");
		return response;
	}

	@Override
	public boolean deleteRepresentative(Long idRepresentative) {
		logger.info("deleteRepresentative - start");
		boolean response = true;
		
		if (apoderadosRepository.existsById(idRepresentative)) {
			apoderadosRepository.updateIndActivo(idRepresentative, EmbargosConstants.IND_FLAG_NO);
		} else {
			response = false;
		}
		
		logger.info("deleteRepresentative - end");
		return response;
	}

	@Override
	public Representative viewRepresentative(Long idRepresentative) {
		logger.info("viewRepresentative - start");
		Representative representative = null;

		if (apoderadosRepository.existsById(idRepresentative)) {
			Optional<Apoderados> apoderado = apoderadosRepository.findById(idRepresentative);
			
			logger.info("viewRepresentative - Resultado de la consulta - sucursal = " + apoderado.get().toString());
			if (apoderado != null) {
				representative = representativeMapper.toRepresentative(apoderado.get());
			}
		}
		
		logger.info("viewRepresentative - end");
		return representative;
	}

	@Override
	public Page<Representative> filter(Map<String, Object> parametros, Pageable dataPage) {
		logger.info("filter - start");
		Page<Apoderados> list = null;
		List<Representative> response = new ArrayList<>();
		Page<Representative> page = null;
		
		logger.info("filter - Filtros de la consulta - Apoderados = " + parametros.toString());
		list = apoderadosRepository.findAll(new Specification<Apoderados>() {

			@Override
			public Predicate toPredicate(Root<Apoderados> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				
				predicates.add(criteriaBuilder.equal(root.get(Apoderados_.indActivo), EmbargosConstants.IND_FLAG_SI));
				
				if(parametros.get(EmbargosConstants.NAME_APODERADO) != null && !parametros.get(EmbargosConstants.NAME_APODERADO).equals("")) {
                    predicates.add(criteriaBuilder.like(root.get(Apoderados_.nombre), "%" + parametros.get(EmbargosConstants.NAME_APODERADO) + "%"));
                }
				
                if(parametros.get(EmbargosConstants.POSITION_APODERADO) != null && !parametros.get(EmbargosConstants.POSITION_APODERADO).equals("")) {
                    predicates.add(criteriaBuilder.like(root.get(Apoderados_.cargo),  "%" + parametros.get(EmbargosConstants.POSITION_APODERADO) + "%"));
                }
                
                
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
			
		}, dataPage);
		
		logger.info("filter - Resultado de la consulta - list = " + list.getContent().toString());
		if (list != null && list.getContent().size() > 0) {
			for (Apoderados a : list.getContent()) {
				response.add(representativeMapper.toRepresentative(a));
			}
			
			page = new PageImpl<>(response, list.getPageable(), list.getTotalElements());
		}
		
		logger.info("filter - end");
		return page;
	}

	@Override
	public List<Representative> listAll() {
		List<Apoderados> apoderados = null;
		List<Representative> response = null;
		
		apoderados = apoderadosRepository.findAll(new Specification<Apoderados>() {

			@Override
			public Predicate toPredicate(Root<Apoderados> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				
				predicates.add(criteriaBuilder.equal(root.get(Apoderados_.indActivo), EmbargosConstants.IND_FLAG_SI));
		
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		});
		
		if (apoderados != null && apoderados.size() > 0) {
			response = new ArrayList<Representative>();
			for (Apoderados a : apoderados) {
				response.add(representativeMapper.toRepresentative(a));
			}
		}
		
		return response;
	}

}
