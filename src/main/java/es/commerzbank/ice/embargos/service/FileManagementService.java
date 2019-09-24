package es.commerzbank.ice.embargos.service;

import java.io.File;
import java.io.IOException;

import es.commerzbank.ice.comun.lib.util.ICEParserException;

public interface FileManagementService {
	// returns true if it can move the file
	public String procesarFichero(File file);
	public boolean pendingMove(File srcFile, String pendingAction);
}
