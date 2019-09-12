package es.commerzbank.ice.embargos.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.embargos.domain.dto.CommunicatingEntity;
import es.commerzbank.ice.embargos.domain.dto.OrderingEntity;
import es.commerzbank.ice.embargos.domain.entity.Apoderados;
import es.commerzbank.ice.embargos.domain.entity.Apoderados_;
import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora;
import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora_;
import es.commerzbank.ice.embargos.domain.entity.EntidadesOrdenante;
import es.commerzbank.ice.embargos.domain.mapper.CommunicatingEntityMapper;
import es.commerzbank.ice.embargos.repository.CommunicatingEntityRepository;
import es.commerzbank.ice.embargos.service.CommunicatingEntityService;
import es.commerzbank.ice.utils.EmbargosConstants;

@Service
@Transactional("transactionManager")
public class CommunicatingEntityServiceImpl implements CommunicatingEntityService {
	private static final Logger logger = LoggerFactory.getLogger(CommunicatingEntityServiceImpl.class);
	
	@Autowired
	private CommunicatingEntityRepository repository;
	
	@Autowired
	private CommunicatingEntityMapper mapper;
	
	@Override
	public boolean createUpdateCommunicatingEntity(CommunicatingEntity communicatingEntity, String name) {
		logger.info("createUpdateCommunicatingEntity - start");
		EntidadesComunicadora result = null, entidad = null;
		boolean response = true;
		
		entidad = mapper.toEntidadComunicadora(communicatingEntity);
		
		if (entidad != null) {
			entidad.setFUltimaModificacion(new BigDecimal(System.currentTimeMillis()));
			entidad.setUsuarioUltModificacion(name);
			
			result = repository.save(entidad);
			
			if (result == null || result.getCodEntidadPresentadora() <= 0) {
				response = false;
			}
		} else {
			response = false;
		}
		
		logger.info("createUpdateCommunicatingEntity - end");
		return response;
	}

	@Override
	public boolean deleteCommunicatingEntity(Long idCommunicatingEntity) {
		logger.info("deleteCommunicatingEntity - start");
		boolean response = true;
		
		if (repository.existsById(idCommunicatingEntity)) {
			repository.updateIndActivo(idCommunicatingEntity, EmbargosConstants.IND_FLAG_NO);
		} else {
			response = false;
		}
		
		logger.info("deleteCommunicatingEntity - end");
		return response;
	}

	@Override
	public CommunicatingEntity viewCommunicatingEntity(Long idCommunicatingEntity) {
		logger.info("viewCommunicatingEntity - start");
		CommunicatingEntity entity = null;

		if (repository.existsById(idCommunicatingEntity)) {
			Optional<EntidadesComunicadora> entidad = repository.findById(idCommunicatingEntity);
			
			logger.info("viewCommunicatingEntity - Resultado de la consulta - entidad = " + entidad.get().toString());
			if (entidad != null) {
				entity = mapper.toCommunicatingEntity(entidad.get());
			}
		}
		
		logger.info("viewCommunicatingEntity - end");
		return entity;
	}

	@Override
	public List<CommunicatingEntity> listAll() {
		List<EntidadesComunicadora> entidades = null;
		List<CommunicatingEntity> response = null;
		
		entidades = repository.findAll(new Specification<EntidadesComunicadora>() {

			@Override
			public Predicate toPredicate(Root<EntidadesComunicadora> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				
				predicates.add(criteriaBuilder.equal(root.get(EntidadesComunicadora_.indActivo), EmbargosConstants.IND_FLAG_YES));
		
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		});
		
		if (entidades != null && entidades.size() > 0) {
			response = new ArrayList<CommunicatingEntity>();
			for (EntidadesComunicadora a : entidades) {
				response.add(mapper.toCommunicatingEntity(a));
			}
		}
		
		return response;
	}

	@Override
	public Page<CommunicatingEntity> filter(Pageable dataPage) {
		Page<CommunicatingEntity> response = null;
		List<CommunicatingEntity> list = null;
		Page<EntidadesComunicadora> result = null;
		
		result = repository.findAll(new Specification<EntidadesComunicadora>() {

			@Override
			public Predicate toPredicate(Root<EntidadesComunicadora> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				
				predicates.add(criteriaBuilder.equal(root.get(EntidadesComunicadora_.indActivo), EmbargosConstants.IND_FLAG_YES));
		
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, dataPage);
		
		if (result != null && result.getContent() != null && result.getContent().size() > 0) {
			list = new ArrayList<CommunicatingEntity>();
			for (EntidadesComunicadora a : result.getContent()) {
				list.add(mapper.toCommunicatingEntity(a));
			}
			
			response = new PageImpl<>(list, result.getPageable(), result.getTotalElements());
		}
		
		return response;
	}

}
