package es.commerzbank.ice.embargos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.commerzbank.ice.embargos.domain.entity.PeticionInformacion;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacionCuenta;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacionCuentaPK;

public interface InformationPetitionBankAccountRepository extends JpaRepository<PeticionInformacionCuenta, PeticionInformacionCuentaPK>{

	public List<PeticionInformacionCuenta> findByPeticionInformacion(PeticionInformacion peticionInformacion);
	
}
