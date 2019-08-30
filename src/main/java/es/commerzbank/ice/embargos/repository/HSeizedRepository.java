package es.commerzbank.ice.embargos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.commerzbank.ice.embargos.domain.entity.HTraba;

public interface HSeizedRepository extends JpaRepository<HTraba, Long>{

	@Query ("from HTraba ht where ht.id.codTraba = :idSeizure")
	public List<HTraba> findByIdSeizure(@Param("idSeizure") Long idSeizure);
	
}
