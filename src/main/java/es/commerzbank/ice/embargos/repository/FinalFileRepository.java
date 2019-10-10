package es.commerzbank.ice.embargos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.FicheroFinal;

@Repository
public interface FinalFileRepository extends JpaRepository<FicheroFinal, Long>{

	public FicheroFinal findByControlFichero(ControlFichero controlFichero);
}
