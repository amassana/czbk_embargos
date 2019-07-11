package es.commerzbank.ice.embargos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora;

public interface CommunicatingEntityRepository extends JpaRepository<EntidadesComunicadora, Long>{

	public EntidadesComunicadora findByNifEntidad(String identificadorEntidad);
}
