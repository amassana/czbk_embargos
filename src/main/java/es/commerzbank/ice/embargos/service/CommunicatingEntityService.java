package es.commerzbank.ice.embargos.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.commerzbank.ice.embargos.domain.dto.CommunicatingEntity;

public interface CommunicatingEntityService {

	boolean createUpdateCommunicatingEntity(CommunicatingEntity communicatingEntity, String name);

	boolean deleteCommunicatingEntity(Long idCommunicatingEntity, String name);

	CommunicatingEntity viewCommunicatingEntity(Long idCommunicatingEntity);

	List<CommunicatingEntity> listAll();

	Page<CommunicatingEntity> filter(Pageable dataPage);

}
