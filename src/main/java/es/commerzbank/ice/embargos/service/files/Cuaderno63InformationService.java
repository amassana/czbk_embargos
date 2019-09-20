package es.commerzbank.ice.embargos.service.files;

import java.io.IOException;

import es.commerzbank.ice.comun.lib.util.ICEParserException;

public interface Cuaderno63InformationService {

	public void tramitarFicheroInformacion(Long codControlFicheroPeticion, String usuarioTramitador) throws IOException, ICEParserException;
	
}
