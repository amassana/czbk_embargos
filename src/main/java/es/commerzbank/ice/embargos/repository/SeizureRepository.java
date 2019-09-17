package es.commerzbank.ice.embargos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.Embargo;

public interface SeizureRepository extends JpaRepository<Embargo, Long>{

	public List<Embargo> findAllByControlFichero(ControlFichero controlFichero);
	public List<Embargo> findAllByNumeroEmbargo(String numeroEmbargo);
}
