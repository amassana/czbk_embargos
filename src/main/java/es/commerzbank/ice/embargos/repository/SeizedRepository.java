package es.commerzbank.ice.embargos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.commerzbank.ice.embargos.domain.entity.Traba;

public interface SeizedRepository extends JpaRepository<Traba, Long>{

}
