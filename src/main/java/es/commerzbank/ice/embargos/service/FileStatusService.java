package es.commerzbank.ice.embargos.service;

import java.util.List;

import es.commerzbank.ice.embargos.domain.dto.FileStatusDTO;

public interface FileStatusService {

	public List<FileStatusDTO> listAllFileStatus();
}
