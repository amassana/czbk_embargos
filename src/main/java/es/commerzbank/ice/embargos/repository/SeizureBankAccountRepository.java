package es.commerzbank.ice.embargos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.commerzbank.ice.embargos.domain.entity.CuentaEmbargo;

public interface SeizureBankAccountRepository extends JpaRepository<CuentaEmbargo, Long>{

}
