package es.commerzbank.ice.embargos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import es.commerzbank.ice.embargos.domain.entity.EntidadesOrdenante;

@Repository
public interface OrderingEntityRepository extends JpaRepository<EntidadesOrdenante, Long>, JpaSpecificationExecutor<EntidadesOrdenante>{

	public EntidadesOrdenante findByNifEntidad(String identificadorEntidad);
	
	public EntidadesOrdenante findByIdentificadorEntidad(String identificadorEntidad);

}
