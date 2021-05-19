package es.commerzbank.ice.embargos.repository;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.Peticion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PetitionRepository
	extends JpaRepository<Peticion, String>, JpaSpecificationExecutor<Peticion>
{
	// todo delete?
	public Peticion findByControlFichero(ControlFichero controlFichero);


}
