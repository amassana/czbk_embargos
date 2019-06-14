package es.commerzbank.ice.embargos.listener;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.commerzbank.ice.embargos.service.FileManagementService;

public class FileListener extends FileAlterationListenerAdaptor {

	private static final Logger LOG = LoggerFactory.getLogger(FileListener.class);

	private FileManagementService listenerService;

	// Use the constructor injection service
	public FileListener(FileManagementService listenerService) {
		this.listenerService = listenerService;
	}

	// file creation execution
	@Override
	public void onFileCreate(File file) {

		try {
			LOG.debug("NUEVO FICHERO");

			// listenerService.cargarFichero(file);

		} catch (Exception e) {
			LOG.error("ERROR :" + e.getMessage(), e);
		}
	}

	// file creation modification
	@Override
	public void onFileChange(File file) {
		// trigger the business

		try {
			LOG.debug("FICHERO MODIFICADO");

			// listenerService.cargarFichero(file);

		} catch (Exception e) {
			LOG.error("ERROR :" + e.getMessage(), e);
		}
	}

	// file creation delete
	@Override
	public void onFileDelete(File file) {

		LOG.debug("FICHERO BORRADO");
	}

	// directory creation
	@Override
	public void onDirectoryCreate(File directory) {
	}

	// directory modification
	@Override
	public void onDirectoryChange(File directory) {
	}

	// directory deletion
	@Override
	public void onDirectoryDelete(File directory) {
	}

	// polling starts
	@Override
	public void onStart(FileAlterationObserver observer) {

		listenerService.cargarFicheros();
	}

	// polling ends
	@Override
	public void onStop(FileAlterationObserver observer) {
	}

}
