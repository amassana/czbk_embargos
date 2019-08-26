package es.commerzbank.ice.embargos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.commerzbank.ice.embargos.domain.entity.CuentaTrabaActuacion;

public interface SeizedBankAccountActionRepository extends JpaRepository<CuentaTrabaActuacion, Long>{

}
