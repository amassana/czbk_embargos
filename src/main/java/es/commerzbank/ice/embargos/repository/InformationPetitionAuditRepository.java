package es.commerzbank.ice.embargos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.commerzbank.ice.embargos.domain.entity.HPeticionInformacion;

public interface InformationPetitionAuditRepository extends JpaRepository<HPeticionInformacion, String>{

	
	@Query ("from HPeticionInformacion hpi where hpi.id.codPeticion = :codPeticion")
	public List<HPeticionInformacion> findByCodPeticion(@Param("codPeticion") String codPeticion);
	
}
