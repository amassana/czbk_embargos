package es.commerzbank.ice.embargos.repository;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
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
	@Query(value = "select * from CONTROL_FICHERO cf where cf.COD_ESTADO = :codEstado and cf.COD_TIPO_FICHERO in :tiposFichero", nativeQuery = true)
	List<ControlFichero> findFicherosByTipoFicheroByCodEstado(@Param("codEstado") long codEstadoControlFicheroGeneradoScheduled, @Param("tiposFichero") List<Long> tiposFichero);

	@Query(value = "select * from CONTROL_FICHERO cf where cf.COD_ESTADO = :codEstado and cf.COD_TIPO_FICHERO in (2, 7, 9, 11) and sysdate > to_date(cf.fecha_incorporacion,'YYYYMMDDHH24MISS')+ :dias", nativeQuery = true)
	List<ControlFichero> findByCodEstadoAndFecha(@Param("codEstado") long codEstadoControlFicheroGeneradoScheduled, @Param("dias") int dias);
	
	@Query(value = "select * from CONTROL_FICHERO cf where cf.COD_ESTADO = 6 and cf.COD_TIPO_FICHERO = 1 and cf.FECHA_COMIENZO_CICLO = :fechaComienzoCiclo and cf.FECHA_CREACION = :fechaCreacion ", nativeQuery = true)
	List<ControlFichero> findEmbargoProcesadoByFechas(@Param("fechaComienzoCiclo") BigDecimal fechaComienzoCiclo, @Param("fechaCreacion") BigDecimal fechaCreacion);

	@Query(value = "select * from CONTROL_FICHERO cf where cf.COD_ESTADO = 6 and cf.COD_TIPO_FICHERO = 1 and cf.NUM_ENVIO = :numEnvio ", nativeQuery = true)
	List<ControlFichero> findEmbargoProcesadoByNumEnvio(@Param("numEnvio") String numEnvio);

	@Query(value = "select * from CONTROL_FICHERO cf where" +
			" cf.COD_TIPO_FICHERO = "+ EmbargosConstants.COD_TIPO_FICHERO_COM_RESULTADO_FINAL_NORMA63 +
			" and cf.COD_ESTADO = "+ EmbargosConstants.COD_ESTADO_CTRLFICHERO_FINAL_AEAT_PENDIENTE_FICHERO
			, nativeQuery = true)
	List<ControlFichero> findFicherosF6FinCicloPending();
	
	Optional<ControlFichero> findByNumCrc(String numCRC);

	List<ControlFichero> findByIndEnvioCarta(String indEnvioCarta);
}
