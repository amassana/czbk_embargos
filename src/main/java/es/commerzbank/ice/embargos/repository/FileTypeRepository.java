package es.commerzbank.ice.embargos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.commerzbank.ice.embargos.domain.entity.TipoFichero;

public interface FileTypeRepository extends JpaRepository<TipoFichero, Long>{

}
