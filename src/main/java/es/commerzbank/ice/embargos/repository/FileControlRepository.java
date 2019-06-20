package es.commerzbank.ice.embargos.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.TipoFichero;

public interface FileControlRepository extends JpaRepository<ControlFichero, Long>{

	public List<ControlFichero> findByTipoFichero(TipoFichero tipoFichero, Pageable pageable);

	//@Query(value = "SELECT p.*, ip.* FROM PETICIONES p INNER JOIN PETICION_INFORMACION ip on (p.COD_CONTROL_FICHERO = ip.COD_CONTROL_FICHERO AND p.COD_PETICION = ip.COD_PETICION) WHERE p.COD_CONTROL_FICHERO = :codControlFichero", nativeQuery=true)
	@Query("select p.codPeticion, p.estadoIntPeticion.codEstadoIntPeticion, pi.nif, pi.razonSocial, pi.iban1, pi.iban2, pi.iban3, pi.iban4, pi.iban5, pi.iban6 from ControlFichero c JOIN c.peticiones p JOIN c.peticionInformacions pi where p.codPeticion = pi.codPeticion AND c.codControlFichero = :codControlFichero")
	public List<Object[]> findPeticionesInformacionByCodeFileControl(@Param("codControlFichero") Long codControlFichero);
}
