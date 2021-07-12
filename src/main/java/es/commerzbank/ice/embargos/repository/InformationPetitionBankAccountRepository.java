package es.commerzbank.ice.embargos.repository;

import es.commerzbank.ice.embargos.domain.entity.PeticionInformacion;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacionCuenta;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacionCuentaPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InformationPetitionBankAccountRepository extends JpaRepository<PeticionInformacionCuenta, PeticionInformacionCuentaPK>{

	List<PeticionInformacionCuenta> findByPeticionInformacionOrderByOrden(PeticionInformacion peticionInformacion);
	
}
