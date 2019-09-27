package es.commerzbank.ice.embargos.service.files;

import java.io.File;
import java.io.IOException;

public interface AEATSeizedResultService {

	public void tratarFicheroErrores(File file)  throws IOException;
}
