package es.commerzbank.ice.embargos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.embargos.domain.entity.EntidadesOrdenante;

public interface OrderingEntityRepository extends JpaRepository<EntidadesOrdenante, Long>, JpaSpecificationExecutor<EntidadesOrdenante>{

	public EntidadesOrdenante findByNifEntidad(String identificadorEntidad);
	
	public EntidadesOrdenante findByIdentificadorEntidad(String identificadorEntidad);

	@Transactional
	@Modifying
	@Query("update EntidadesOrdenante a set a.indActivo = :indActivo where a.codEntidadOrdenante = :id")
	public void updateIndActivo(@Param("id") Long idOrderingEntity, @Param("indActivo") String indFlagNo);
	
}
