package es.commerzbank.ice.embargos.repository;

import es.commerzbank.ice.embargos.domain.entity.SolicitudesLevantamiento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudLevantamientoRepository
	extends JpaRepository<SolicitudesLevantamiento, String>
{
}
