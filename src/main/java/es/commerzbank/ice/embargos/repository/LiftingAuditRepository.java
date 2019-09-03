package es.commerzbank.ice.embargos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.commerzbank.ice.embargos.domain.entity.HLevantamientoTraba;

public interface LiftingAuditRepository extends JpaRepository<HLevantamientoTraba, Long>{

	@Query(value = "select * from H_LEVANTAMIENTO_TRABA h where h.COD_LEVATAMIENTO = :id", nativeQuery = true)
	List<HLevantamientoTraba> findByCodLevantamiento(@Param("id") Long codeLiftingCase);

}
