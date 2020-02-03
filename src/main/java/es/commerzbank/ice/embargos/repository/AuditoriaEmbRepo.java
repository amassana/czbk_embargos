package es.commerzbank.ice.embargos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.commerzbank.ice.embargos.domain.entity.AuditoriaEmb;

@Repository
public interface AuditoriaEmbRepo extends JpaRepository<AuditoriaEmb, Long>, JpaSpecificationExecutor<AuditoriaEmb> {

	@Query(value = "select distinct(a.tabla) from Auditoria a", nativeQuery = true)
	List<String> findAllTables();
	
	@Query(value = "select distinct(a.usuario) from Auditoria a where usuario is not null", nativeQuery = true)
	List<String> findAllUsers();
}
