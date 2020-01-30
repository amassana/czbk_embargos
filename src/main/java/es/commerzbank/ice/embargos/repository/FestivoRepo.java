package es.commerzbank.ice.embargos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.commerzbank.ice.embargos.domain.entity.FestivoEmbargo;

public interface FestivoRepo extends JpaRepository<FestivoEmbargo, Long>{

}