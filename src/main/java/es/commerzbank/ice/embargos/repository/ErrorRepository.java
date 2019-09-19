package es.commerzbank.ice.embargos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorRepository extends JpaRepository< es.commerzbank.ice.embargos.domain.entity.Error, Long>{
	   

	public es.commerzbank.ice.embargos.domain.entity.Error findByNumeroError(String numeroError);
}
