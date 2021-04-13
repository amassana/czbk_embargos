package es.commerzbank.ice.embargos.repository;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.ResultadoEmbargo;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinalResponseRepository extends JpaRepository<ResultadoEmbargo, Long>{

	List<ResultadoEmbargo> findAllByControlFichero(ControlFichero controlFichero);

	ResultadoEmbargo findByTraba(Traba traba);

}
