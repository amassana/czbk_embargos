package es.commerzbank.ice.embargos.service.files;


import es.commerzbank.ice.embargos.domain.dto.ClientLiftingManualDTO;
import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora;

import java.util.List;
import java.util.Map;

public interface AEATManualLiftingService
{
	void crearFicheroLevantamientos(EntidadesComunicadora entity, Map<String, List<ClientLiftingManualDTO>> ordenesPorCliente) throws Exception;
}
