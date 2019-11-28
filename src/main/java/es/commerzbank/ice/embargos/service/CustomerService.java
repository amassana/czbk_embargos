package es.commerzbank.ice.embargos.service;

import java.util.HashSet;

import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;

public interface CustomerService {

	public CustomerDTO findCustomerByNif(String nif, boolean includeInactive) throws ICEException;
	
	public HashSet<String> findCustomerNifs() throws ICEException;
}
