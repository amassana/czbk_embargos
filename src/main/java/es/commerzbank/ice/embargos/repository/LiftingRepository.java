package es.commerzbank.ice.embargos.repository;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.LevantamientoTraba;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LiftingRepository extends JpaRepository<LevantamientoTraba, Long>{

	List<LevantamientoTraba> findAllByControlFichero(ControlFichero controlFichero);

	List<LevantamientoTraba> findAllByTraba(Traba traba);
}
