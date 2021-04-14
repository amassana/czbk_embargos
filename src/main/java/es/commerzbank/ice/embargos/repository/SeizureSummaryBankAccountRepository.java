package es.commerzbank.ice.embargos.repository;

import es.commerzbank.ice.embargos.domain.entity.CuentaResultadoEmbargo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeizureSummaryBankAccountRepository extends JpaRepository<CuentaResultadoEmbargo, Long>{

//	@Query(value = "select importe_neto from cuenta_resultado_embargo t, resultado_embargo r where r.cod_embargo = :codEmbargo and r.cod_resultado_embargo=t.cod_resultado_embargo order by r.cod_resultado_embargo desc", nativeQuery = true)
//	List<Long> getImporteEmbargo(@Param("codEmbargo") Long codEmbargo);
}
