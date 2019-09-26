package es.commerzbank.ice.embargos.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.comun.lib.util.ICEParserException;
import es.commerzbank.ice.embargos.domain.dto.FileControlDTO;
import es.commerzbank.ice.embargos.domain.dto.FileControlFiltersDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;

public interface FileControlService {

	public Page<FileControlDTO> fileSearch(FileControlFiltersDTO fileControlFiltersDTO, Pageable pageable) throws Exception;
	
	public FileControlDTO getByCodeFileControl(Long codeFileControl);
	
	public List<FileControlDTO> getAuditByCodeFileControl(Long codeFileControl);
	
	public boolean tramitarFicheroInformacion(Long codeFileControl, String usuarioTramitador) throws IOException, ICEException, ICEParserException;
	
	public boolean tramitarTrabasCuaderno63(Long codeFileControl, String usuarioTramitador) throws IOException, ICEParserException;
	
	public boolean tramitarTrabasAEAT(Long codeFileControl, String usuarioTramitador) throws IOException, ICEParserException;
	
	public boolean updateFileControl(Long codeFileControl, FileControlDTO fileControlDTO, String userModif);
	
	public boolean updateFileControlStatus(Long codeFileControl, Long codFileControlStatus, String userModif);
	
	public void updateFileControlStatusTransaction(ControlFichero controlFichero, Long codEstado);
	
	public void updateFileControlStatusTransaction(ControlFichero controlFichero, Long codEstado, String userModif);
	
	public void saveFileControlTransaction(ControlFichero controlFichero);

	public byte[] generateFileControl(Integer [] codTipoFichero, Integer codEstado, boolean isPending, Date fechaInicio, Date fechaFin) throws Exception;
	
}
