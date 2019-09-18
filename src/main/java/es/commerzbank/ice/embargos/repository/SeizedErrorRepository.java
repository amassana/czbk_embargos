package es.commerzbank.ice.embargos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.ErrorTraba;
import es.commerzbank.ice.embargos.domain.entity.Traba;

public interface SeizedErrorRepository extends JpaRepository<ErrorTraba, Long>{
   

}
