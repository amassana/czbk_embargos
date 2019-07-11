package es.commerzbank.ice.embargos.service;

import java.util.List;

import es.commerzbank.ice.embargos.domain.dto.FileControlStatusDTO;

public interface FileControlStatusService {

	public List<FileControlStatusDTO> listFileControlStatusByFileType(Long idFileType);
}
