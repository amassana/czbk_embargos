package es.commerzbank.ice.embargos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.Embargo;

public interface SeizureRepository extends JpaRepository<Embargo, Long>{

	public List<Embargo> findAllByControlFichero(ControlFichero controlFichero);
	public List<Embargo> findAllByNumeroEmbargo(String numeroEmbargo);
	
	@Query(value = "select * from EMBARGO where FECHA_LIMITE_TRABA=TO_CHAR(sysdate,'YYYYMMDD') and COD_ENTIDAD_ORDENANTE=(select COD_ENTIDAD_ORDENANTE from ENTIDADES_ORDENANTES where DES_ENTIDAD='AEAT')", nativeQuery = true)
	List<Embargo> listEmbargosTransferToTax();
}
