package es.commerzbank.ice.embargos.repository;

import es.commerzbank.ice.embargos.domain.entity.ResultadoEmbargo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SeizureSummaryRepository extends JpaRepository<ResultadoEmbargo, Long>
{

}
