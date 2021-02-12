package es.commerzbank.ice.embargos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;

@Repository
public interface FileControlRepository
	extends JpaRepository<ControlFichero, Long>, JpaSpecificationExecutor<ControlFichero>
{
	@Query(value = "select * from CONTROL_FICHERO cf where cf.COD_ESTADO = :codEstado and cf.COD_TIPO_FICHERO in (2, 7, 9, 11)", nativeQuery = true)
	List<ControlFichero> findByCodEstado(@Param("codEstado") long codEstadoControlFicheroGeneradoScheduled);
}
