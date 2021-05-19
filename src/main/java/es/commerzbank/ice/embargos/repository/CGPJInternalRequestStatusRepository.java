package es.commerzbank.ice.embargos.repository;

import es.commerzbank.ice.embargos.domain.entity.EstadoIntPeticion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CGPJInternalRequestStatusRepository extends JpaRepository<EstadoIntPeticion, Long>
{

}
