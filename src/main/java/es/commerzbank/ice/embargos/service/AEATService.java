package es.commerzbank.ice.embargos.service;

import java.io.File;
import java.io.IOException;

import es.commerzbank.ice.comun.lib.util.ICEParserException;

public interface AEATService {

	public void tratarFicheroDiligenciasEmbargo(File file) throws IOException, ICEParserException;
	public void tramitarTrabas(Long codControlFicheroPeticion, String usuarioTramitador) throws IOException, ICEParserException;
	public void tratarFicheroErrores(File file);
}
