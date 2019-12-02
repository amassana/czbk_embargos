package es.commerzbank.ice.embargos.service.impl;

import es.commerzbank.ice.datawarehouse.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.service.CustomerService;

import java.util.HashSet;
import java.util.List;

@Service
@Transactional("transactionManager")
public class CustomerServiceImpl implements CustomerService {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
	
	@Autowired
	private AccountService accountService;

	@Override
	public CustomerDTO findCustomerByNif(String nif, boolean includeInactive) throws ICEException{
		/*
		String dominioTSP = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_DOMINIO);
		String endpointClientAccountDWH = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_DWH_ENDPOINT_CLIENTACCOUNT,
				EmbargosConstants.PARAMETRO_DWH_ENDPOINT_CLIENTACCOUNT_DEFAULT_VALUE);
	
		String endpoint = dominioTSP + endpointClientAccountDWH + "?nif=" + nif +"&includeInactive=true";
						
		//Llamada al endpoint de Datawarehouse utilizando el cliente Rest de Comunes:
		return clientRestService.callToRESTMethod(endpoint, HttpMethod.GET.toString(), null, CustomerDTO.class);

		 */
		try {
			List<CustomerDTO> result = accountService.showCustomerDetailsByNif(nif, null, null, includeInactive);
			if (result == null)
				return null;
			if (result.size() == 0)
				return null;
			return result.get(0);
		} catch (Exception e) {
			// TODO e lost..
			throw new ICEException("Can't recover "+ nif + " details");
		}
	}

	@Override
	public HashSet<String> findCustomerNifs() throws ICEException {
		try {
			return  accountService.showCustomerNifs();
		} catch (Exception e) {
			// TODO e lost..
			throw new ICEException("Can't recover nifs from DWH");
		}
	}

}
