package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.embargos.domain.entity.FicheroFinal;

public interface FinalFileService {

	public boolean updateFinalFileAccountingStatus(FicheroFinal ficheroFinal, Long codEstadoContabilizacion, String userName);
}
