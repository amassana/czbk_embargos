package es.commerzbank.ice.embargos.service;

import java.io.File;
import java.io.IOException;

public interface FileManagementService {

	public void cargarFicheros();
	
	public void cargarFichero(File file) throws IOException;
}
