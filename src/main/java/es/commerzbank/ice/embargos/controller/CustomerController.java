package es.commerzbank.ice.embargos.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.service.CustomerService;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/customer")
public class CustomerController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	private CustomerService customerService;

	// TODO qui invoca a aquest controller?
	@GetMapping(value = "/{nif}/accounts")
	public CustomerDTO listAllAccountsByNif(Authentication authentication,
			@PathVariable("nif") String nif){
		
		logger.info("CustomerController - listAllAccountsByNif - start");
		
		CustomerDTO customerDTO = null;
		
		try {
		
			customerDTO = customerService.findCustomerByNif(nif, false);
		
		} catch (Exception e) {
			logger.error("ERROR: ", e);
		}
		
		logger.info("CustomerController - listAllAccountsByNif - end");
		
		return customerDTO;
	}
}
