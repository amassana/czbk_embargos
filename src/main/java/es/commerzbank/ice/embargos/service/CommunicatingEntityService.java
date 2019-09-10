package es.commerzbank.ice.embargos.service;

import java.util.List;

import es.commerzbank.ice.embargos.domain.dto.CommunicatingEntity;

public interface CommunicatingEntityService {

	boolean createUpdateCommunicatingEntity(CommunicatingEntity communicatingEntity, String name);

	boolean deleteCommunicatingEntity(Long idCommunicatingEntity);

	CommunicatingEntity viewCommunicatingEntity(Long idCommunicatingEntity);

	List<CommunicatingEntity> listAll();

}
