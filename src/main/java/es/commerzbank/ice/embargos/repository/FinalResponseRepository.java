package es.commerzbank.ice.embargos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.ResultadoEmbargo;

@Repository
public interface FinalResponseRepository extends JpaRepository<ResultadoEmbargo, Long>{

	List<ResultadoEmbargo> findAllByControlFichero(ControlFichero controlFichero);

}