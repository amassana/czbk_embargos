package es.commerzbank.ice.embargos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.LevantamientoTraba;

@Repository
public interface LiftingRepository extends JpaRepository<LevantamientoTraba, Long>{

	public List<LevantamientoTraba> findAllByControlFichero(ControlFichero controlFichero);

	@Transactional
	@Modifying
	@Query("update LevantamientoTraba l set l.indCasoRevisado = :estado where l.codLevantamiento = :id")
	public void updateStatus(@Param("id") Long codelifting, @Param("estado") String status);

}
