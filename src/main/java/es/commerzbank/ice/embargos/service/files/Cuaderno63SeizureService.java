package es.commerzbank.ice.embargos.service.files;

import java.io.File;
import java.io.IOException;

import es.commerzbank.ice.comun.lib.util.ICEException;

public interface Cuaderno63SeizureService {

	public void cargarFicheroEmbargos(File file, String originalName, File processedFile) throws IOException, ICEException, Exception;
}
