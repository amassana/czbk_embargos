package es.commerzbank.ice.embargos.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import es.commerzbank.ice.embargos.domain.dto.BankAccountDTO;
import es.commerzbank.ice.embargos.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService{

	@Override
	public List<BankAccountDTO> listAllAccountsByNif(String nif) {

		List<BankAccountDTO> accountList = new ArrayList<>();
		
		/** INIT MOCK **/
		
		if ("11111111A".equals(nif)){
		
			BankAccountDTO account = new BankAccountDTO();
			
			account.setCodeBankAccount("1");
			account.setIban("01591111111100010001");
			account.setStatus("NORMAL");
	
			accountList.add(account);
			
			account = new BankAccountDTO();
			
			account.setCodeBankAccount("2");
			account.setIban("01592111111100020002");
			account.setStatus("BLOCKED");
			
			accountList.add(account);
		}

		if ("22222222B".equals(nif)){
			
			BankAccountDTO account = new BankAccountDTO();
			
			account.setCodeBankAccount("3");
			account.setIban("01593222222200010001");
			account.setStatus("NORMAL");
	
			accountList.add(account);
			
			account = new BankAccountDTO();
			
			account.setCodeBankAccount("4");
			account.setIban("01594222222200020002");
			account.setStatus("CANCELLED");
			
			accountList.add(account);
		}
		
		if ("33333333C".equals(nif)){
			
			BankAccountDTO account = new BankAccountDTO();
			
			account.setCodeBankAccount("5");
			account.setIban("01595333333300010001");
			account.setStatus("NORMAL");
	
			accountList.add(account);
			
			account = new BankAccountDTO();
			
			account.setCodeBankAccount("6");
			account.setIban("01596333333300020002");
			account.setStatus("BLOCKED");
			
			accountList.add(account);
		}
		
		//Nifs del fichero CMAD_20190619.PET
		
		if ("X04113619".equals(nif)){
			
			BankAccountDTO account = new BankAccountDTO();
			
			account.setCodeBankAccount("10");
			account.setIban("01590411361900010010");
			account.setStatus("NORMAL");
	
			accountList.add(account);
			
			account = new BankAccountDTO();
			
			account.setCodeBankAccount("11");
			account.setIban("015960411361900020011");
			account.setStatus("BLOCKED");
			
			accountList.add(account);
		}
		if ("X08431090".equals(nif)){
			
			BankAccountDTO account = new BankAccountDTO();
			
			account.setCodeBankAccount("12");
			account.setIban("01590843109000010012");
			account.setStatus("NORMAL");
	
			accountList.add(account);
			
			account = new BankAccountDTO();
			
			account.setCodeBankAccount("13");
			account.setIban("01590843109000020013");
			account.setStatus("CANCELLED");
			
			accountList.add(account);
		}
		if ("X18008557".equals(nif)){
			
			BankAccountDTO account = new BankAccountDTO();
			
			account.setCodeBankAccount("14");
			account.setIban("01591800855700010014");
			account.setStatus("NORMAL");
	
			accountList.add(account);
			
			account = new BankAccountDTO();
			
			account.setCodeBankAccount("15");
			account.setIban("01591800855700020015");
			account.setStatus("BLOCKED");
			
			accountList.add(account);
		}
		/** END MOCK **/
		
		return accountList;
	}

}
