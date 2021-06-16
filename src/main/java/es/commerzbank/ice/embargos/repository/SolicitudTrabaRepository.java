package es.commerzbank.ice.embargos.repository;

import es.commerzbank.ice.embargos.domain.entity.SolicitudesTraba;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SolicitudTrabaRepository
	extends JpaRepository<SolicitudesTraba, String>
{
	Optional<SolicitudesTraba> findByTraba(Traba traba);
}
