package es.commerzbank.ice.embargos.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.commerzbank.ice.embargos.domain.dto.BankAccountDTO;
import es.commerzbank.ice.embargos.service.CustomerService;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/customer")
public class CustomerController {

	private static final Logger LOG = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping(value = "/{nif}/accounts")
	public List<BankAccountDTO> listAllAccountsByNif(@RequestHeader(value="token",required=true) String token,
			@PathVariable("nif") String nif){
		
		return customerService.listAllAccountsByNif(nif);
		
	}
}
