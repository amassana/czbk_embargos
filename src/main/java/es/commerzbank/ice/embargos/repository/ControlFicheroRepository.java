package es.commerzbank.ice.embargos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;

public interface ControlFicheroRepository extends JpaRepository<ControlFichero, Long>{

}
