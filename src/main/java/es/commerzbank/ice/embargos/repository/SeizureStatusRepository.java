package es.commerzbank.ice.embargos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.commerzbank.ice.embargos.domain.entity.EstadoTraba;

public interface SeizureStatusRepository  extends JpaRepository<EstadoTraba, Long>{

	@Query ("from EstadoTraba et where et.ocultarAUsuario = 'N'")
	public List<EstadoTraba> findAllVisibleToUser();
}
