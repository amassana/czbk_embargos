package es.commerzbank.ice.embargos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora;

public interface CommunicatingEntityRepository extends JpaRepository<EntidadesComunicadora, Long>, JpaSpecificationExecutor<EntidadesComunicadora>{

	public EntidadesComunicadora findByNifEntidad(String identificadorEntidad);

}
