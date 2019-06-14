package es.commerzbank.ice.embargos.service;

import java.io.File;

public interface AEATService {

	public void tratarFicheroDiligenciasEmbargo(File file);
	public void tratarFicheroLevantamientos(File file);
	public void tratarFicheroErrores(File file);
}
