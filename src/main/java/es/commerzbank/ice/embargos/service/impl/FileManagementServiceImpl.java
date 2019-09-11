package es.commerzbank.ice.embargos.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import es.commerzbank.ice.embargos.service.files.AEATLiftingService;
import es.commerzbank.ice.embargos.service.files.Cuaderno63LiftingService;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.comun.lib.util.ICEParserException;
import es.commerzbank.ice.embargos.service.AEATService;
import es.commerzbank.ice.embargos.service.Cuaderno63Service;
import es.commerzbank.ice.embargos.service.FileManagementService;
import es.commerzbank.ice.utils.EmbargosConstants;

@Service
@Transactional("transactionManager")
public class FileManagementServiceImpl implements FileManagementService {

	
	private static final Logger LOG = LoggerFactory.getLogger(FileManagementServiceImpl.class);
	
	@Value("${commerzbank.embargos.files.path.monitoring}")
	private String pathMonitoring;

	@Value("${commerzbank.embargos.files.path.processed}")
	private String pathProcessed;
	
	@Value("${commerzbank.embargos.files.path.generated}")
	private String pathGenerated;
	
	@Value("${commerzbank.embargos.files.path.error}")
	private String pathError;

	@Autowired
	Cuaderno63Service cuaderno63Service;

	@Autowired
	Cuaderno63LiftingService cuaderno63LiftingService;

	@Autowired
	AEATService aeatService;

	@Autowired
	AEATLiftingService aeatLiftingService;
	
	public void cargarFicheros() {
		
		try {
			
			//Extensiones de los ficheros a leer:
			String[] extensionList = {
					EmbargosConstants.TIPO_FICHERO_PETICIONES,
					EmbargosConstants.TIPO_FICHERO_PETICIONES.toLowerCase(),
					EmbargosConstants.TIPO_FICHERO_EMBARGOS,
					EmbargosConstants.TIPO_FICHERO_EMBARGOS.toLowerCase(),
					EmbargosConstants.TIPO_FICHERO_LEVANTAMIENTOS,
					EmbargosConstants.TIPO_FICHERO_LEVANTAMIENTOS.toLowerCase(),
					EmbargosConstants.TIPO_FICHERO_ERRORES,
					EmbargosConstants.TIPO_FICHERO_ERRORES.toLowerCase()
			};

		
			//1. Obtener ficheros del directorio de monitoreo
			File dir = new File(pathMonitoring);
	
			LOG.debug("Getting files from the path " + dir.getCanonicalPath());
			List<File> files = (List<File>) FileUtils.listFiles(dir, extensionList, false);
			
			for (File file : files) {
				try {
					
					cargarFichero(file);
				
					moverFichero(file, pathProcessed);
					LOG.debug("The file " + FilenameUtils.getName(file.getCanonicalPath()) + " has been moved to the path " + pathProcessed);
				
				} catch (Exception e) {
					
					moverFichero(file, pathError);
					LOG.debug("The file " + FilenameUtils.getName(file.getCanonicalPath()) + " has been moved to the path " + pathError);
				}

			}	
		
		} catch(Exception e) {
			LOG.error("ERROR: " + e.getLocalizedMessage());
		}
	}

	public void cargarFichero(File file) throws IOException, ICEParserException {
		
		LOG.debug("file: " + file.getCanonicalPath());
		
		try {
			
			//- Quitar linea siguiente de leerFichero(file) (solo para pruebas):
			leerFichero(file);													
			
			//Si fichero es de la AEAT (tiene prefijo "AEAT_")
			if (FilenameUtils.getBaseName(file.getCanonicalPath()).toLowerCase().contains(EmbargosConstants.AEAT.toLowerCase())) {

				parsearFicheroAEAT(file);
			
			} else {
				
				parsearFicheroCuaderno63(file);	
			}

		} catch (Exception e) {
	
			LOG.error(e.getMessage());
			throw e;
		}
		
	}
	

	private void parsearFicheroAEAT(File file) throws IOException, ICEParserException {
		
		String tipoFichero = FilenameUtils.getExtension(file.getCanonicalPath()).toUpperCase();
				
		switch (tipoFichero) {						
			//- Tipos de ficheros AEAT:	
			case EmbargosConstants.TIPO_FICHERO_EMBARGOS:
				aeatService.tratarFicheroDiligenciasEmbargo(file);
				break;
			case EmbargosConstants.TIPO_FICHERO_LEVANTAMIENTOS:
				aeatLiftingService.tratarFicheroLevantamientos(file);
				break;
			case EmbargosConstants.TIPO_FICHERO_ERRORES:
				aeatService.tratarFicheroErrores(file);
				break;	
			default:
		}
	}

	
	private void parsearFicheroCuaderno63(File file) throws IOException, ICEParserException {
		
		String tipoFichero = FilenameUtils.getExtension(file.getCanonicalPath()).toUpperCase();

		//Tratamiento segun el tipo de fichero:
		
		switch (tipoFichero) {
			//- Tipos de ficheros del Cuaderno63:
			case EmbargosConstants.TIPO_FICHERO_PETICIONES:
				cuaderno63Service.cargarFicheroPeticion(file);
				break;
			case EmbargosConstants.TIPO_FICHERO_EMBARGOS:
				cuaderno63Service.cargarFicheroEmbargos(file);
				break;	
			case EmbargosConstants.TIPO_FICHERO_LEVANTAMIENTOS:
				cuaderno63LiftingService.tratarFicheroLevantamientos(file);
				break;			
			default:
		}
	}

	
	
	private void leerFichero(File file) {
			
		if (file==null) {			
			throw new RuntimeException("Error reading the file: the file is null.");
		}

		LineIterator it = null;
		
		try {
		
			 it= FileUtils.lineIterator(file, "UTF-8");
			 
			int i=1;
		    while (it.hasNext()) {
		        String line = it.nextLine();
		        // do something with line
		        
		        LOG.debug("LINE " + i + ": " + line);
		        
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
	
	
	private void moverFichero(File srcFile, String destDir) throws IOException{
		
		try {
			FileUtils.moveFileToDirectory(srcFile,new File(destDir),true);
		
		} catch (FileExistsException fee) {
			
			FileUtils.copyFileToDirectory(srcFile, new File(destDir));
			FileUtils.forceDelete(srcFile);
		}
		
	}



	
}
