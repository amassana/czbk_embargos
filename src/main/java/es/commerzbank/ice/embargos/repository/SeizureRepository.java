package es.commerzbank.ice.embargos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.commerzbank.ice.embargos.domain.entity.Embargo;

public interface SeizureRepository extends JpaRepository<Embargo, Long>{

}
