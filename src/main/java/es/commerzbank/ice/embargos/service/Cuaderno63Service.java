package es.commerzbank.ice.embargos.service;

import java.io.File;
import java.io.IOException;

import es.commerzbank.ice.comun.lib.util.ICEParserException;

public interface Cuaderno63Service {

	public void cargarFicheroPeticion(File file) throws IOException, ICEParserException ;
	public void tramitarFicheroInformacion(Long codControlFicheroPeticion, String usuarioTramitador) throws IOException;
	public void cargarFicheroEmbargos(File file) throws IOException;
	public void tratarFicheroLevantamientos(File file);
}
