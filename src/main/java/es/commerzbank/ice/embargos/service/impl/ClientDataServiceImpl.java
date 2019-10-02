package es.commerzbank.ice.embargos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.domain.entity.DatosCliente;
import es.commerzbank.ice.embargos.domain.mapper.ClientDataMapper;
import es.commerzbank.ice.embargos.repository.ClientDataRepository;
import es.commerzbank.ice.embargos.service.ClientDataService;

@Service
@Transactional(transactionManager="transactionManager")
public class ClientDataServiceImpl implements ClientDataService {

	@Autowired
	private ClientDataMapper clientDataMapper;
	
	@Autowired
	private ClientDataRepository clientDataRepository; 
	
	@Override
	@Transactional(transactionManager="transactionManager", propagation = Propagation.REQUIRES_NEW)
	public void createUpdateClientDataTransaction(CustomerDTO customerDTO) {
		
		DatosCliente datosCliente  = clientDataMapper.toDatosCliente(customerDTO);
		
		clientDataRepository.save(datosCliente);
	}
}
