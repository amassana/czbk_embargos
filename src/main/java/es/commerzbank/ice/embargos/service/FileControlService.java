package es.commerzbank.ice.embargos.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.commerzbank.ice.embargos.domain.dto.FileControlDTO;
import es.commerzbank.ice.embargos.domain.dto.FileControlFiltersDTO;

public interface FileControlService {

	public Page<FileControlDTO> fileSearch(FileControlFiltersDTO fileControlFiltersDTO, Pageable pageable) throws Exception;
	
	public FileControlDTO getByCodeFileControl(Long codeFileControl);
	
	public List<FileControlDTO> getAuditByCodeFileControl(Long codeFileControl);
	
	public boolean tramitarFicheroInformacion(Long codeFileControl) throws IOException;
	
	public boolean updateFileControl(Long codeFileControl, FileControlDTO fileControlDTO);

	public byte[] generarReporteListado(Integer [] codTipoFichero, Integer codEstado, boolean isPending, Date fechaInicio, Date fechaFin) throws Exception;
}
