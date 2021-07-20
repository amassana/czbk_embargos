package es.commerzbank.ice.embargos.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.commerzbank.ice.embargos.domain.entity.FestivoEmbargo;

public interface FestivoRepo extends JpaRepository<FestivoEmbargo, Long>{
	
	@Modifying
	@Query(value= "delete from FESTIVO f where f.F_FESTIVO >=:valueDate", nativeQuery = true)
	public default void deleteAllYear(@Param("valueDate") LocalDate creationDateTime) {
		
	}

}
