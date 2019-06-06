package es.commerzbank.ice.embargos.listener;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import es.commerzbank.ice.embargos.service.FileManagementService;

public class FileListener extends FileAlterationListenerAdaptor {
		 
   private FileManagementService listenerService;

   // Use the constructor injection service
   public FileListener(FileManagementService listenerService) {
       this.listenerService = listenerService;
   }

   // file creation execution
   @Override
   public void onFileCreate(File file) {
	   
	   System.out.println("NUEVO FICHERO");
	   listenerService.cargarFicheros();
   }

   // file creation modification
   @Override
   public void onFileChange(File file) {
                // trigger the business
	   System.out.println("FICHERO MODIFICADO");
       listenerService.cargarFicheros();
   }

   // file creation delete
   @Override
   public void onFileDelete(File file) {
	   
	   System.out.println("FICHERO BORRADO");
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
