package es.commerzbank.ice.embargos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.comun.lib.service.ClientRestService;
import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.service.CustomerService;
import io.swagger.models.HttpMethod;

@Service
@Transactional("transactionManager")
public class CustomerServiceImpl implements CustomerService{
	
	@Autowired
	ClientRestService clientRestService;
	
	@Override
	public CustomerDTO findCustomerByNif(String nif) {

//		List<AccountDTO> accountList = new ArrayList<>();
//		
//		/** INIT MOCK **/
//		
//		if ("11111111A".equals(nif)){
//		
//			AccountDTO account = new AccountDTO();
//			
//			account.setAccountNum("1");
//			account.setIban("01591111111100010001");
//			account.setStatus("NORMAL");
//	
//			accountList.add(account);
//			
//			account = new AccountDTO();
//			
//			account.setAccountNum("2");
//			account.setIban("01592111111100020002");
//			account.setStatus("BLOCKED");
//			
//			accountList.add(account);
//		}
//
//		if ("22222222B".equals(nif)){
//			
//			AccountDTO account = new AccountDTO();
//			
//			account.setAccountNum("3");
//			account.setIban("01593222222200010001");
//			account.setStatus("NORMAL");
//	
//			accountList.add(account);
//			
//			account = new AccountDTO();
//			
//			account.setAccountNum("4");
//			account.setIban("01594222222200020002");
//			account.setStatus("CANCELLED");
//			
//			accountList.add(account);
//		}
//		
//		if ("33333333C".equals(nif)){
//			
//			AccountDTO account = new AccountDTO();
//			
//			account.setAccountNum("5");
//			account.setIban("01595333333300010001");
//			account.setStatus("NORMAL");
//	
//			accountList.add(account);
//			
//			account = new AccountDTO();
//			
//			account.setAccountNum("6");
//			account.setIban("01596333333300020002");
//			account.setStatus("BLOCKED");
//			
//			accountList.add(account);
//		}
//		
//		//Nifs del fichero CMAD_20190619.PET
//		
//		if ("X04113619".equals(nif)){
//			
//			AccountDTO account = new AccountDTO();
//			
//			account.setAccountNum("10");
//			account.setIban("01590411361900010010");
//			account.setStatus("NORMAL");
//	
//			accountList.add(account);
//			
//			account = new AccountDTO();
//			
//			account.setAccountNum("11");
//			account.setIban("015960411361900020011");
//			account.setStatus("BLOCKED");
//			
//			accountList.add(account);
//		}
//		if ("X08431090".equals(nif)){
//			
//			AccountDTO account = new AccountDTO();
//			
//			account.setAccountNum("12");
//			account.setIban("01590843109000010012");
//			account.setStatus("NORMAL");
//	
//			accountList.add(account);
//			
//			account = new AccountDTO();
//			
//			account.setAccountNum("13");
//			account.setIban("01590843109000020013");
//			account.setStatus("CANCELLED");
//			
//			accountList.add(account);
//		}
//		if ("X18008557".equals(nif)){
//			
//			AccountDTO account = new AccountDTO();
//			
//			account.setAccountNum("14");
//			account.setIban("01591800855700010014");
//			account.setStatus("NORMAL");
//	
//			accountList.add(account);
//			
//			account = new AccountDTO();
//			
//			account.setAccountNum("15");
//			account.setIban("01591800855700020015");
//			account.setStatus("BLOCKED");
//			
//			accountList.add(account);
//		}
//		/** END MOCK **/
		
		String endpoint = "http://commerzbank.prod.altengroup.dir:8080/datawarehouse/client-account?nif="+ nif +"&includeInactive=true";
				
		//CustomerDTO customerDTO = clientRestService.callToRESTMethod(endpoint, HttpMethod.GET.toString(), null, CustomerDTO.class);
		
		//accountList = customerDTO.getBankAccounts();
		
		//Llamada al endpoint de Datawarehouse utilizando el cliente Rest de Comunes:
		
		return clientRestService.callToRESTMethod(endpoint, HttpMethod.GET.toString(), null, CustomerDTO.class);
	}

}
