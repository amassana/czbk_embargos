package es.commerzbank.ice.embargos.repository;

import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.EstadoTraba;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface SeizedBankAccountRepository extends JpaRepository<CuentaTraba, Long>{

	List<CuentaTraba> findAllByTrabaOrderByNumeroOrdenCuentaAsc(Traba traba);
	
	CuentaTraba findByCodCuentaTrabaAndCuentaAndEstadoTraba(Long codeFileControl, Long idSeizure, EstadoTraba estadoTraba);

	CuentaTraba findByCuentaAndImporteAndEstadoTraba(String cuenta, BigDecimal importe, EstadoTraba estadoTraba);

	@Transactional
	@Modifying
	@Query(nativeQuery = true,
			value = "INSERT INTO cuenta_traba (COD_CUENTA_TRABA, COD_TRABA, COD_ESTADO) VALUES (:codCuentaTraba, :codTraba, :codEstado)")
	void insertarCuentaTraba(	@Param("codCuentaTraba") Long codCuentaTraba,
					 			@Param("codTraba") Long codTraba,
					 			@Param("codEstado") Long codEstado);
}
