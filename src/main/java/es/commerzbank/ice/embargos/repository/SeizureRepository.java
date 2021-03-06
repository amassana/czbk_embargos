package es.commerzbank.ice.embargos.repository;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface SeizureRepository extends JpaRepository<Embargo, Long>{

	List<Embargo> findAllByControlFichero(ControlFichero controlFichero);

	@Query(value = "select e from Embargo e where e.numeroEmbargo = :numeroEmbargo AND e.controlFichero.fechaIncorporacion >= :fechaIncorporacionDesde ORDER BY e.controlFichero.fechaIncorporacion DESC")
	List<Embargo> findAllByNumeroEmbargo(@Param("numeroEmbargo") String numeroEmbargo, @Param("fechaIncorporacionDesde") BigDecimal fechaIncorporacionDesde);
	
	@Query(value = "select * from EMBARGO where FECHA_LIMITE_TRABA=TO_CHAR(sysdate,'YYYYMMDD') and COD_ENTIDAD_ORDENANTE=(select COD_ENTIDAD_ORDENANTE from ENTIDADES_ORDENANTES where DES_ENTIDAD='AEAT')", nativeQuery = true)
	List<Embargo> listEmbargosTransferToTax();

	@Query(value = "select * from EMBARGO where COD_CONTROL_FICHERO = :codControlFichero AND NUMERO_EMBARGO = :numeroEmbargo", nativeQuery = true)
	Embargo findByControlFicheroNumeroEmbargo(@Param("codControlFichero") Long codControlFichero, @Param("numeroEmbargo") String numeroEmbargo);
}
