package es.commerzbank.ice.embargos.service.files;


import es.commerzbank.ice.embargos.domain.dto.ManualLiftingDTO;

public interface AEATManualLiftingService
{
	String crearFicheroLevantamientos(ManualLiftingDTO manualLiftingDTO) throws Exception;
}
