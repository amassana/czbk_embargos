package es.commerzbank.ice.embargos.repository;

import es.commerzbank.ice.embargos.domain.entity.CuentaTrabaCGPJCopy;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CuentaTrabaCGPJCopyRepository extends JpaRepository<CuentaTrabaCGPJCopy, Long> {

    List<CuentaTrabaCGPJCopy> findByTraba(Traba traba);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO CUENTA_TRABA_CGPJ_COPY SELECT * FROM CUENTA_TRABA WHERE COD_TRABA = :codTraba", nativeQuery = true)
    void cloneCuentaTraba(@Param("codTraba") Long codTraba);
}
