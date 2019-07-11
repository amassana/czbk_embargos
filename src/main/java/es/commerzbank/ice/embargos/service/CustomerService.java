package es.commerzbank.ice.embargos.service;

import java.util.List;

import es.commerzbank.ice.embargos.domain.dto.BankAccountDTO;

public interface CustomerService {

	List<BankAccountDTO> listAllAccountsByNif(String nif);
}
