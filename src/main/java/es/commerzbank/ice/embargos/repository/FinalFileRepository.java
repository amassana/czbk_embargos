package es.commerzbank.ice.embargos.repository;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.EstadoContabilizacion;
import es.commerzbank.ice.embargos.domain.entity.FicheroFinal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinalFileRepository extends JpaRepository<FicheroFinal, Long>{

	FicheroFinal findByControlFichero(ControlFichero controlFichero);

	List<FicheroFinal> findByEstadoContabilizacionEquals(EstadoContabilizacion estadoContabilizacion);

	Optional<FicheroFinal> findByCodFicheroDiligenciasEquals(Long codFicheroDiligencias);
}
