package es.commerzbank.ice.embargos.service.files;

import es.commerzbank.ice.embargos.domain.dto.ClientLiftingManualDTO;
import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora;

import java.util.List;
import java.util.Map;

public interface Cuaderno63ManualLiftingService {

	String crearFicheroLevantamientos(EntidadesComunicadora entity, Map<String, List<ClientLiftingManualDTO>> ordenesPorCliente) throws Exception;
}
