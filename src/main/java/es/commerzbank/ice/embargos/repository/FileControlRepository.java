package es.commerzbank.ice.embargos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;

@Repository
public interface FileControlRepository extends JpaRepository<ControlFichero, Long>, JpaSpecificationExecutor<ControlFichero>{	
	
}
