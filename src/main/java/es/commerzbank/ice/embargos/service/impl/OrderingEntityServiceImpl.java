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

import es.commerzbank.ice.embargos.domain.dto.OrderingEntity;
import es.commerzbank.ice.embargos.domain.entity.EntidadesOrdenante;
import es.commerzbank.ice.embargos.domain.entity.EntidadesOrdenante_;
import es.commerzbank.ice.embargos.domain.mapper.OrderingEntityMapper;
import es.commerzbank.ice.embargos.repository.OrderingEntityRepository;
import es.commerzbank.ice.embargos.service.OrderingEntityService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;

@Service
@Transactional("transactionManager")
public class OrderingEntityServiceImpl implements OrderingEntityService {
	private static final Logger logger = LoggerFactory.getLogger(OrderingEntityServiceImpl.class);
	
	@Autowired
	private OrderingEntityRepository repository;
	
	@Autowired
	private OrderingEntityMapper mapper;
	
	@Override
	public boolean createUpdateOrderingEntity(OrderingEntity orderingEntity, String name) {
		logger.info("OrderingEntityServiceImpl - createUpdateOrderingEntity - start");
		EntidadesOrdenante result = null, entidad = null;
		boolean response = true;
		
		entidad = mapper.toEntidadOrdenante(orderingEntity);
		
		if (entidad != null) {
			entidad.setIndActivo(EmbargosConstants.IND_FLAG_SI);
			entidad.setFUltimaModificacion(new BigDecimal(System.currentTimeMillis()));
			entidad.setUsuarioUltModificacion(name);
			
			result = repository.save(entidad);
			
			if (result == null || result.getCodEntidadOrdenante() <= 0) {
				response = false;
			}
		} else {
			response = false;
		}
		
		logger.info("OrderingEntityServiceImpl - createUpdateOrderingEntity - end");
		return response;
	}

	@Override
	public boolean deleteOrderingEntity(Long idOrderingEntity, String name) {
		logger.info("OrderingEntityServiceImpl - deleteOrderingEntity - start");
		boolean response = true;
		
		if (repository.existsById(idOrderingEntity)) {
			Optional<EntidadesOrdenante> optEntidad = repository.findById(idOrderingEntity);
			if (optEntidad.isPresent()) {
				EntidadesOrdenante entidad = optEntidad.get();
				entidad.setIndActivo(EmbargosConstants.IND_FLAG_NO);
				entidad.setFUltimaModificacion(new BigDecimal(System.currentTimeMillis()));
				entidad.setUsuarioUltModificacion(name);
				repository.save(entidad);
			}
			
			//repository.updateIndActivo(idOrderingEntity, EmbargosConstants.IND_FLAG_NO);
		} else {
			response = false;
		}
		
		logger.info("OrderingEntityServiceImpl - deleteOrderingEntity - end");
		return response;
	}

	@Override
	public OrderingEntity viewOrderingEntity(Long idOrderingEntity) {
		logger.info("OrderingEntityServiceImpl - viewOrderingEntity - start");
		OrderingEntity entity = null;

		if (repository.existsById(idOrderingEntity)) {
			Optional<EntidadesOrdenante> entidad = repository.findById(idOrderingEntity);
			
			logger.info("viewOrderingEntity - Resultado de la consulta - entidad = " + entidad.get().toString());
			if (entidad != null) {
				entity = mapper.toOrderingEntity(entidad.get());
			}
		}
		
		logger.info("OrderingEntityServiceImpl - viewOrderingEntity - end");
		return entity;
	}

	@Override
	public List<OrderingEntity> listAll() {
		logger.info("OrderingEntityServiceImpl - listAll - start");
		List<EntidadesOrdenante> entidades = null;
		List<OrderingEntity> response = null;
		
		entidades = repository.findAll(new Specification<EntidadesOrdenante>() {

			@Override
			public Predicate toPredicate(Root<EntidadesOrdenante> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				
				predicates.add(criteriaBuilder.equal(root.get(EntidadesOrdenante_.indActivo), EmbargosConstants.IND_FLAG_SI));
		
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		});
		
		if (entidades != null && entidades.size() > 0) {
			response = new ArrayList<OrderingEntity>();
			for (EntidadesOrdenante a : entidades) {
				response.add(mapper.toOrderingEntity(a));
			}
		}
		
		logger.info("OrderingEntityServiceImpl - listAll - end");
		return response;
	}

	@Override
	public Page<OrderingEntity> filter(Pageable dataPage) {
		logger.info("OrderingEntityServiceImpl - filter - start");
		Page<OrderingEntity> response = null;
		List<OrderingEntity> list = null;
		Page<EntidadesOrdenante> result = null;
		
		result = repository.findAll(new Specification<EntidadesOrdenante>() {

			@Override
			public Predicate toPredicate(Root<EntidadesOrdenante> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				
				predicates.add(criteriaBuilder.equal(root.get(EntidadesOrdenante_.indActivo), EmbargosConstants.IND_FLAG_SI));
		
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, dataPage);
		
		if (result != null && result.getContent() != null && result.getContent().size() > 0) {
			list = new ArrayList<OrderingEntity>();
			for (EntidadesOrdenante a : result.getContent()) {
				list.add(mapper.toOrderingEntity(a));
			}
			
			response = new PageImpl<>(list, result.getPageable(), result.getTotalElements());
		}
		
		logger.info("OrderingEntityServiceImpl - filter - end");
		return response;
	}
}
