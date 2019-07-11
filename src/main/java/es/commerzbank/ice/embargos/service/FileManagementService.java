package es.commerzbank.ice.embargos.service;

import java.io.File;
import java.io.IOException;

import es.commerzbank.ice.comun.lib.util.ICEParserException;

public interface FileManagementService {

	public void cargarFicheros();
	
	public void cargarFichero(File file) throws IOException, ICEParserException;
}
