package es.commerzbank.ice.embargos.service.files;

import java.io.File;
import java.io.IOException;

import es.commerzbank.ice.comun.lib.util.ICEParserException;

public interface Cuaderno63PetitionService {

	public void cargarFicheroPeticion(File file) throws IOException, ICEParserException ;
	
}
