package es.commerzbank.ice.embargos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.embargos.domain.entity.Apoderados;

@Repository
public interface ApoderadosRepository extends JpaRepository<Apoderados, Long>, JpaSpecificationExecutor<Apoderados>{

	@Transactional
	@Modifying
	@Query("update Apoderados a set a.indActivo = :indActivo where a.id = :id")
	void updateIndActivo(@Param("id") Long idRepresentative, @Param("indActivo") String indFlagNo);

}
