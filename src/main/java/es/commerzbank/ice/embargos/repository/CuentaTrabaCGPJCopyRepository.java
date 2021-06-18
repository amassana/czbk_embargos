package es.commerzbank.ice.embargos.repository;

import es.commerzbank.ice.embargos.domain.entity.CuentaTrabaCGPJCopy;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuentaTrabaCGPJCopyRepository extends JpaRepository<CuentaTrabaCGPJCopy, Long> {

    List<CuentaTrabaCGPJCopy> findByTraba(Traba traba);
}
