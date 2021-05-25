package es.commerzbank.ice.embargos.repository;

import es.commerzbank.ice.embargos.domain.entity.SolicitudesTraba;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudTrabaRepository
	extends JpaRepository<SolicitudesTraba, String>
{
}
