package es.commerzbank.ice.embargos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import es.commerzbank.ice.embargos.domain.entity.EstadoIntLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.EstadoLevantamiento;

@Service
public interface LiftingStatusRepository extends JpaRepository<EstadoLevantamiento, Long>, JpaSpecificationExecutor<EstadoLevantamiento> {
	
}
