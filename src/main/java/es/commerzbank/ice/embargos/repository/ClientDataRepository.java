package es.commerzbank.ice.embargos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.commerzbank.ice.embargos.domain.entity.DatosCliente;

public interface ClientDataRepository extends JpaRepository<DatosCliente, String>{

}
