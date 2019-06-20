package es.commerzbank.ice.embargos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.commerzbank.ice.embargos.domain.entity.PeticionInformacion;

public interface InformationPetitionRepository  extends JpaRepository<PeticionInformacion, Long>{

}
