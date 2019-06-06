package es.commerzbank.ice.embargos.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.embargos.formats.aeat.diligencias.Diligencia;
import es.commerzbank.ice.embargos.service.FileManagementService;

@Service
@Transactional
public class FileManagementServiceImpl implements FileManagementService {

	
	private static final Logger LOG = LoggerFactory.getLogger(FileManagementServiceImpl.class);
	
	@Value("${commerzbank.impuestos.files.path.monitoring}")
	private String pathMonitoring;

	@Value("${commerzbank.impuestos.files.path.processed}")
	private String pathProcessed;
	
	@Value("${commerzbank.impuestos.files.path.error}")
	private String pathError;
	
	//Agregar repositories de DWH ...
	
	
	public void cargarFicheros() {
		
		try {
			
			String[] extensionList = {"csv", "CSV"};
		
			//1. Obtener ficheros del directorio
			File dir = new File(pathMonitoring);
	
			System.out.println("Getting all files in " + dir.getCanonicalPath() + " including those in subdirectories");
			List<File> files = (List<File>) FileUtils.listFiles(dir, extensionList, false);

			//2. Procesar ficheros y moverlos a la carpeta al final del procesamiento	
			
			for (File file : files) {
				System.out.println("file: " + file.getCanonicalPath());
				
				try {
					leerFichero(file);
					
					parsearFichero(file);
				
//					moverFichero(file,pathProcessed);
					
				} catch (Exception e) {
					
					LOG.error(e.getMessage());
					
//					moverFichero(file,pathError);
				}
			}
			
		
		} catch(Exception e) {
			LOG.error("ERROR: " + e.getLocalizedMessage());
		}
		

	}
	
	public void leerFichero(File file) {
		
		
		if (file==null) {			
			throw new RuntimeException("Error al obtener el fichero");
		}

		LineIterator it = null;
		
		try {
		
			 it= FileUtils.lineIterator(file, "UTF-8");
			 
			int i=1;
		    while (it.hasNext()) {
		        String line = it.nextLine();
		        // do something with line
		        
		        System.out.println("LINEA " + i + ": " + line);
		        
		        i++;
		    }
		} catch (Exception e) {  
		  
			
			
		} finally {
			try {
				it.close();
			} catch (IOException ioe) {
				
			}
		}
	}

	public void parsearFichero(File file) {			
		URL url = getClass().getClassLoader().getResource("beanio/aeat.xml");
		File file2 = new File(url.getFile());
		
        // create a StreamFactory
        StreamFactory factory = StreamFactory.newInstance();
        // load the mapping file
        //factory.load("beanio/aeat.xml");
        factory.load(file2);
        
        // use a StreamFactory to create a BeanReader
        BeanReader in = factory.createReader("diligencias", file);
        Object record = null;
        while ((record = in.read()) != null) {
            // process the employee...
        	
        	if("diligencia".equals(in.getRecordName())) {
        		
        		Diligencia diligencia = (Diligencia) record;
        		System.out.println(diligencia.getNifDeudor());
        	}
        	
        	System.out.println(record.toString());
        }
        in.close();
		
	}
	
	
	public void moverFichero(File srcFile, String destDir) throws IOException{
		
		try {
			FileUtils.moveFileToDirectory(srcFile,new File(destDir),true);
		
		} catch (FileExistsException fee) {
			
			FileUtils.copyFileToDirectory(srcFile, new File(destDir));
			FileUtils.forceDelete(srcFile);
		}
		
	}


	
}
