package es.commerzbank.ice.embargos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.Traba;

public interface SeizedBankAccountRepository extends JpaRepository<CuentaTraba, Long>{

	public List<CuentaTraba> findAllByTraba(Traba traba);
	
}