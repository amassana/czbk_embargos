package es.commerzbank.ice.embargos.service;

import java.util.List;

import es.commerzbank.ice.embargos.domain.dto.FinalResponseDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;


public interface FinalResponseService {

	List<FinalResponseDTO> getAllByControlFichero(ControlFichero controlFichero);

}
