package es.commerzbank.ice.embargos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import es.commerzbank.ice.embargos.domain.entity.Apoderados;

@Repository
public interface ApoderadosRepository extends JpaRepository<Apoderados, Long>, JpaSpecificationExecutor<Apoderados>{

}
