package es.commerzbank.ice.embargos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.Peticion;

public interface PetitionRepository  extends JpaRepository<Peticion, String>{

	
	public Peticion findByControlFichero(ControlFichero controlFichero);
}
