package es.commerzbank.ice.embargos.service.files;

import java.io.IOException;

import es.commerzbank.ice.comun.lib.util.ICEParserException;

public interface Cuaderno63SeizedService {
	
	public void tramitarTrabas(Long codControlFicheroPeticion, String usuarioTramitador) throws IOException, ICEParserException;
}
