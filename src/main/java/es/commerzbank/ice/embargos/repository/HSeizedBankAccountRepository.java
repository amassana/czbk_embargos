package es.commerzbank.ice.embargos.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.commerzbank.ice.embargos.domain.entity.HCuentaTraba;

public interface HSeizedBankAccountRepository extends JpaRepository<HCuentaTraba, Long>{

	//public List<HCuentaTraba> findAllByTraba(Traba traba);
	//public List<HCuentaTraba> findAllByCodTraba(BigDecimal codTraba);
	
	@Query ("from HCuentaTraba hct where hct.codTraba = :codTraba and hct.id.codAuditoria = :codAudit")
	public List<HCuentaTraba> findAllByCodTrabaAndCodAudit(@Param("codTraba") BigDecimal codTraba, @Param("codAudit") Long codAudit);
	
}
