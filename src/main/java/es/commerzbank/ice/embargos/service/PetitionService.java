package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.embargos.domain.dto.PetitionDTO;
import es.commerzbank.ice.embargos.domain.entity.Peticion;

public interface PetitionService {

	public PetitionDTO getByCodeFileControl(Long codeFileControl);
	public byte[] generateF1PettitionRequest(Integer codeFileControl, String oficina) throws Exception;
	public byte[] generateF2PettitionResponse(Integer codeFileControl, String oficina) throws Exception;

	void changeStatus(Peticion peticion, Long cgpjEstadoInternoProcesado);
}
