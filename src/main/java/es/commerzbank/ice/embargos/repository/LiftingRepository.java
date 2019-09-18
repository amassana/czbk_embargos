package es.commerzbank.ice.embargos.repository;

import java.math.BigDecimal;
import java.util.List;

import es.commerzbank.ice.embargos.domain.entity.Traba;
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
	@Query("update LevantamientoTraba l set l.estadoLevantamiento = :estado, l.usuarioUltModificacion = :user, l.fUltimaModificacion = :fecha where l.codLevantamiento = :id")
	public void updateStatus(@Param("id") Long codelifting, @Param("estado") BigDecimal bigDecimal2, @Param("user") String userName, @Param("fecha") BigDecimal bigDecimal);

	public List<LevantamientoTraba> findAllByTraba(Traba traba);
}
