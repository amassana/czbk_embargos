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
	@Query(value = "update LEVANTAMIENTO_TRABA l set l.ESTADO_LEVANTAMIENTO = :estado, l.USUARIO_ULT_MODIFICACION = :user, l.F_ULTIMA_MODIFICACION = :fecha where l.COD_LEVANTAMIENTO = :id", nativeQuery = true)
	public void updateStatus(@Param("id") Long codelifting, @Param("estado") BigDecimal bigDecimal2, @Param("user") String userName, @Param("fecha") BigDecimal bigDecimal);

	public List<LevantamientoTraba> findAllByTraba(Traba traba);
}
