package es.commerzbank.ice.embargos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.commerzbank.ice.embargos.domain.entity.ErrorTraba;

public interface SeizedErrorRepository extends JpaRepository<ErrorTraba, Long>{
   

}
