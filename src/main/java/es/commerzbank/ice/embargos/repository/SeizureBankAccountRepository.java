package es.commerzbank.ice.embargos.repository;

import es.commerzbank.ice.embargos.domain.entity.Embargo;
import org.springframework.data.jpa.repository.JpaRepository;

import es.commerzbank.ice.embargos.domain.entity.CuentaEmbargo;

import java.util.List;

public interface SeizureBankAccountRepository extends JpaRepository<CuentaEmbargo, Long>{
    public List<CuentaEmbargo> findAllByEmbargo(Embargo embargo);
}
