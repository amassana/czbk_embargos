package es.commerzbank.ice.embargos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.commerzbank.ice.embargos.domain.entity.CuentaResultadoEmbargo;
import es.commerzbank.ice.embargos.domain.entity.ResultadoEmbargo;

@Repository
public interface FinalResponseBankAccountRepository extends JpaRepository<CuentaResultadoEmbargo, Long> {

	List<CuentaResultadoEmbargo> findByResultadoEmbargo(ResultadoEmbargo resultado);

}