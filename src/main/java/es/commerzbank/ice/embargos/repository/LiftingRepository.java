package es.commerzbank.ice.embargos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.LevantamientoTraba;

@Repository
public interface LiftingRepository extends JpaRepository<LevantamientoTraba, Long>{

	public List<LevantamientoTraba> findAllByControlFichero(ControlFichero controlFichero);

}
