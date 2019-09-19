package es.commerzbank.ice.embargos.service.files;

import es.commerzbank.ice.comun.lib.util.ICEParserException;

import java.io.File;
import java.io.IOException;

public interface Cuaderno63LiftingService {

	public void tratarFicheroLevantamientos(File file) throws IOException;
}
