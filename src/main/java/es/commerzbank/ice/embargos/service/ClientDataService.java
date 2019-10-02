package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;

public interface ClientDataService {

	public void createUpdateClientDataTransaction(CustomerDTO customerDTO);
}
