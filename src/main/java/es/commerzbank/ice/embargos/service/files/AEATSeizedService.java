package es.commerzbank.ice.embargos.service.files;

import java.io.IOException;

import es.commerzbank.ice.comun.lib.util.ICEException;

public interface AEATSeizedService {

	public void tramitarTrabas(Long codControlFicheroPeticion, String usuarioTramitador) throws IOException, ICEException;
}
