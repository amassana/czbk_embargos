package es.commerzbank.ice.embargos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.commerzbank.ice.embargos.domain.entity.EntidadesOrdenante;

public interface OrderingEntityRepository extends JpaRepository<EntidadesOrdenante, Long>{

	public EntidadesOrdenante findByNifEntidad(String identificadorEntidad);
	
	public EntidadesOrdenante findByIdentificadorEntidad(String identificadorEntidad);
	
}
