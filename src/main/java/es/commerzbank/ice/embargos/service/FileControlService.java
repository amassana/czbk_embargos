package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.domain.dto.FileControlDTO;
import es.commerzbank.ice.embargos.domain.dto.FileControlFiltersDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface FileControlService {

	Page<FileControlDTO> fileSearch(FileControlFiltersDTO fileControlFiltersDTO, Pageable pageable) throws Exception;
	
	FileControlDTO getByCodeFileControl(Long codeFileControl, String type);
	
	List<FileControlDTO> getAuditByCodeFileControl(Long codeFileControl);
	
	boolean tramitarFicheroInformacion(Long codeFileControl, String usuarioTramitador) throws IOException, ICEException;
	
	boolean tramitarTrabasCuaderno63(Long codeFileControl, String usuarioTramitador) throws IOException, ICEException;
	
	boolean tramitarTrabasAEAT(Long codeFileControl, String usuarioTramitador) throws Exception;
	
	boolean updateFileControl(Long codeFileControl, FileControlDTO fileControlDTO, String userModif);
	
	boolean updateFileControlStatus(Long codeFileControl, Long codFileControlStatus, String userModif, String tipoDatos) throws Exception;
	
	//void updateFileControlStatusTransaction(ControlFichero controlFichero, Long codEstado);
	
	//void updateFileControlStatusTransaction(ControlFichero controlFichero, Long codEstado, String userModif);
	
	void saveFileControlTransaction(ControlFichero controlFichero);
	byte[] generateFileControl(FileControlFiltersDTO fileControlFilters, String oficina) throws Exception;

	List<ControlFichero> cartasPendientesEnvio();
	void cartaEnviada(ControlFichero controlFichero);

	List<ControlFichero> listByStatus(long estado, List<Long> tiposFichero);
}
