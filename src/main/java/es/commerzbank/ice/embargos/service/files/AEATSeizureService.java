package es.commerzbank.ice.embargos.service.files;

import java.io.File;
import java.io.IOException;

import es.commerzbank.ice.comun.lib.util.ICEParserException;

public interface AEATSeizureService {

	public void tratarFicheroDiligenciasEmbargo(File file) throws IOException, ICEParserException;
}
