package es.commerzbank.ice.embargos.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.EstadoTraba;
import es.commerzbank.ice.embargos.domain.entity.Traba;

public interface SeizedBankAccountRepository extends JpaRepository<CuentaTraba, Long>{

	public List<CuentaTraba> findAllByTrabaOrderByNumeroOrdenCuentaAsc(Traba traba);
	
	public CuentaTraba findByCodCuentaTrabaAndCuentaAndEstadoTraba(Long codeFileControl, Long idSeizure, EstadoTraba estadoTraba);

	public CuentaTraba findByCuentaAndImporteAndEstadoTraba(String cuenta, BigDecimal importe, EstadoTraba estadoTraba);
}
