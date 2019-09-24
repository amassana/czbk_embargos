package es.commerzbank.ice.embargos.repository;

import java.math.BigDecimal;
import java.util.List;

import es.commerzbank.ice.embargos.domain.entity.EstadoLevantamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.commerzbank.ice.embargos.domain.entity.CuentaLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.LevantamientoTraba;

@Repository
public interface LiftingBankAccountRepository extends JpaRepository<CuentaLevantamiento, Long>{

	public List<CuentaLevantamiento> findByLevantamientoTraba(LevantamientoTraba levantamiento);

	public CuentaLevantamiento findByCuentaAndImporteAndEstadoLevantamiento(String cuenta, BigDecimal importe, EstadoLevantamiento estadoLevantamiento);

}
