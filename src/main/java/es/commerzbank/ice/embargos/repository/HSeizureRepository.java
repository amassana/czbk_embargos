package es.commerzbank.ice.embargos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.commerzbank.ice.embargos.domain.entity.HEmbargo;

public interface HSeizureRepository extends JpaRepository<HEmbargo, Long>{

	@Query ("from HEmbargo he where he.id.codEmbargo = :idSeizure")
	public List<HEmbargo> findByIdSeizure(@Param("idSeizure") Long idSeizure);
	
}
