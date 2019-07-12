package es.commerzbank.ice.embargos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacion;

public interface InformationPetitionRepository  extends JpaRepository<PeticionInformacion, String>{

	
	public PeticionInformacion findByControlFicheroAndNif(ControlFichero controlFichero, String nif);
	
	public List<PeticionInformacion> findAllByControlFichero(ControlFichero controlFichero);
	
	//Problema con la prioridad AND y OR (no incluye parentesis en la query), se comenta:
	//public Integer countByControlFicheroAndIndCasoRevisadoOrIndCasoRevisadoNull(ControlFichero controlFichero, String indCasoRevisado);
	
	@Query ("select count(pi) from PeticionInformacion pi where pi.controlFichero = :controlFichero and (pi.indCasoRevisado is null or pi.indCasoRevisado = :indCasoRevisado)")
	public Integer countByControlFicheroAndIndCasoRevisadoOrIndCasoRevisadoNull(@Param("controlFichero") ControlFichero controlFichero, @Param("indCasoRevisado") String indCasoRevisado);

	public Integer countByControlFicheroAndIndCasoRevisado(ControlFichero controlFichero, String indCasoRevisado);
	
	public Integer countByControlFichero(ControlFichero controlFichero);

}
