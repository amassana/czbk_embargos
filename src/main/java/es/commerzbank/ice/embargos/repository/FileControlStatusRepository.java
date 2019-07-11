package es.commerzbank.ice.embargos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlfichero;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlficheroPK;
import es.commerzbank.ice.embargos.domain.entity.TipoFichero;

public interface FileControlStatusRepository extends JpaRepository<EstadoCtrlfichero, EstadoCtrlficheroPK>, JpaSpecificationExecutor<EstadoCtrlfichero>{	

	public List<EstadoCtrlfichero> findByTipoFichero(TipoFichero tipoFichero);
}
