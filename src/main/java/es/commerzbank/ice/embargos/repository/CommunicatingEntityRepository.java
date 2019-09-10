package es.commerzbank.ice.embargos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora;

public interface CommunicatingEntityRepository extends JpaRepository<EntidadesComunicadora, Long>, JpaSpecificationExecutor<EntidadesComunicadora>{

	public EntidadesComunicadora findByNifEntidad(String identificadorEntidad);

	@Transactional
	@Modifying
	@Query("update EntidadesComunicadora a set a.indActivo = :indActivo where a.codEntidadPresentadora = :id")
	public void updateIndActivo(@Param("id") Long idCommunicatingEntity, @Param("indActivo") String indFlagNo);
}
