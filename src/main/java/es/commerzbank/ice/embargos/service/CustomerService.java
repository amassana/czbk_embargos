package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;

public interface CustomerService {

	public CustomerDTO findCustomerByNif(String nif);
}
