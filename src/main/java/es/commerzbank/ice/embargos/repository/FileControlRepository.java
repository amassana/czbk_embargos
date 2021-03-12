package es.commerzbank.ice.embargos.repository;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FileControlRepository
	extends JpaRepository<ControlFichero, Long>, JpaSpecificationExecutor<ControlFichero>
{
	@Query(value = "select * from CONTROL_FICHERO cf where cf.COD_ESTADO = :codEstado and cf.COD_TIPO_FICHERO in (2, 7, 9, 11)", nativeQuery = true)
	List<ControlFichero> findByCodEstado(@Param("codEstado") long codEstadoControlFicheroGeneradoScheduled);

	@Query(value = "select * from CONTROL_FICHERO cf where cf.COD_ESTADO = 6 and cf.COD_TIPO_FICHERO = 1 and cf.FECHA_COMIENZO_CICLO = :fechaComienzoCiclo and cf.FECHA_CREACION = :fechaCreacion ", nativeQuery = true)
	List<ControlFichero> findEmbargoProcesadoByFechas(@Param("fechaComienzoCiclo") BigDecimal fechaComienzoCiclo, @Param("fechaCreacion") BigDecimal fechaCreacion);
	
	@Query(value = "select * from CONTROL_FICHERO cf where cf.COD_ESTADO = 6 and cf.COD_TIPO_FICHERO = 1 and cf.NUM_ENVIO = :numEnvio ", nativeQuery = true)
	List<ControlFichero> findEmbargoProcesadoByNumEnvio(@Param("numEnvio") String numEnvio);
	
	Optional<ControlFichero> findByNumCrc(String numCRC);

	List<ControlFichero> findByIndEnvioCarta(String indEnvioCarta);
}
