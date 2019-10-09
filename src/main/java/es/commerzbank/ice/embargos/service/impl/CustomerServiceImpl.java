package es.commerzbank.ice.embargos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.comun.lib.service.ClientRestService;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.service.CustomerService;
import es.commerzbank.ice.utils.EmbargosConstants;
import io.swagger.models.HttpMethod;

@Service
@Transactional("transactionManager")
public class CustomerServiceImpl implements CustomerService {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
	
	@Autowired
	private ClientRestService clientRestService;
	
	@Autowired
	private GeneralParametersService generalParametersService;
	
	@Override
	public CustomerDTO findCustomerByNif(String nif) throws ICEException{
		String dominioTSP = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_TSP_DOMINIO);
		String endpointClientAccountDWH = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_DWH_ENDPOINT_CLIENTACCOUNT,
				EmbargosConstants.PARAMETRO_DWH_ENDPOINT_CLIENTACCOUNT_DEFAULT_VALUE);
	
		String endpoint = dominioTSP + endpointClientAccountDWH + "?nif=" + nif +"&includeInactive=true";
						
		//Llamada al endpoint de Datawarehouse utilizando el cliente Rest de Comunes:
		return clientRestService.callToRESTMethod(endpoint, HttpMethod.GET.toString(), null, CustomerDTO.class);
	}

}
