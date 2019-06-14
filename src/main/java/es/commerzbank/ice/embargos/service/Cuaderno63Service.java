package es.commerzbank.ice.embargos.service;

import java.io.File;
import java.io.IOException;

public interface Cuaderno63Service {

	public void tratarFicheroPeticion(File file) throws IOException;
	public void tratarFicheroEmbargos(File file);
	public void tratarFicheroLevantamientos(File file);
}
