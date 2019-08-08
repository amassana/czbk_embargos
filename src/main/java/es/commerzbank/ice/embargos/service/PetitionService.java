package es.commerzbank.ice.embargos.service;

import java.io.IOException;

import es.commerzbank.ice.embargos.domain.dto.PetitionDTO;

public interface PetitionService {

	public PetitionDTO getByCodeFileControl(Long codeFileControl);

	public byte[] generateJasperPDF(Integer codeFileControl) throws Exception;
}
