package es.commerzbank.ice.embargos.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.commerzbank.ice.embargos.domain.dto.AuditoriaEmbDto;
import es.commerzbank.ice.embargos.domain.dto.AuditoriaEmbFilter;
import es.commerzbank.ice.embargos.domain.entity.AuditoriaEmb;

public interface AuditoriaEmbService {

	Page<AuditoriaEmbDto> listFilterAuditoria(AuditoriaEmbFilter auditoriaFilter, Pageable dataPage);
	
	Page<AuditoriaEmbDto> listFilterAuditoria(String pkLogico, String tabla, Pageable dataPage);
	
	AuditoriaEmbDto viewAuditoria(Long idAuditoria);
	
	void saveAuditoria(AuditoriaEmb auditoria);
	
	List<String> listAllTables();
	
	List<String> listAllUsers();
}
