package es.commerzbank.ice.embargos.repository;

import es.commerzbank.ice.embargos.domain.entity.TareasPendiente;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TareasPendienteRepo extends JpaRepository<TareasPendiente, Long>
{

}
