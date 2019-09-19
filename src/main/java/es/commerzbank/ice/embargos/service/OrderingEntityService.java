package es.commerzbank.ice.embargos.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.commerzbank.ice.embargos.domain.dto.OrderingEntity;

public interface OrderingEntityService {

	boolean createUpdateOrderingEntity(OrderingEntity orderingEntity, String name);

	boolean deleteOrderingEntity(Long idOrderingEntity);

	OrderingEntity viewOrderingEntity(Long idOrderingEntity);

	List<OrderingEntity> listAll();

	Page<OrderingEntity> filter(Pageable dataPage);

}
