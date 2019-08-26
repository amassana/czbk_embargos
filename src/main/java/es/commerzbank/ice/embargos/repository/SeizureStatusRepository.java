package es.commerzbank.ice.embargos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.commerzbank.ice.embargos.domain.entity.EstadoTraba;

public interface SeizureStatusRepository  extends JpaRepository<EstadoTraba, Long>{

}
