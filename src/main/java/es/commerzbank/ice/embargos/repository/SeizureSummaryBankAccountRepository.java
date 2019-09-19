package es.commerzbank.ice.embargos.repository;

import es.commerzbank.ice.embargos.domain.entity.CuentaEmbargo;
import es.commerzbank.ice.embargos.domain.entity.CuentaResultadoEmbargo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeizureSummaryBankAccountRepository extends JpaRepository<CuentaResultadoEmbargo, Long>{

}
