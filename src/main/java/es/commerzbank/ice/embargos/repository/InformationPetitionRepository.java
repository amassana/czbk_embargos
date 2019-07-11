package es.commerzbank.ice.embargos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacion;

public interface InformationPetitionRepository  extends JpaRepository<PeticionInformacion, String>{

	
	public PeticionInformacion findByControlFicheroAndNif(ControlFichero controlFichero, String nif);
	
	public List<PeticionInformacion> findAllByControlFichero(ControlFichero controlFichero);
	
	public Integer countByControlFicheroAndIndCasoRevisadoOrIndCasoRevisadoNull(ControlFichero controlFichero, String indCasoRevisado);
}
