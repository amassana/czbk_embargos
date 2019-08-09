package es.commerzbank.ice.embargos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.commerzbank.ice.embargos.domain.entity.HControlFichero;

@Repository
public interface FileControlAuditRepository extends JpaRepository<HControlFichero, Long>, JpaSpecificationExecutor<HControlFichero>{	

	@Query ("from HControlFichero hcf where hcf.id.codControlFichero = :codeFileControl")
	public List<HControlFichero> findByCodControlFichero(@Param("codeFileControl") Long codeFileControl);
	
}
