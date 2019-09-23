package es.commerzbank.ice.embargos.service.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.comun.lib.util.ICEParserException;
import es.commerzbank.ice.embargos.service.FileManagementService;
import es.commerzbank.ice.embargos.service.files.AEATLiftingService;
import es.commerzbank.ice.embargos.service.files.AEATSeizedResultService;
import es.commerzbank.ice.embargos.service.files.AEATSeizureService;
import es.commerzbank.ice.embargos.service.files.Cuaderno63LiftingService;
import es.commerzbank.ice.embargos.service.files.Cuaderno63PetitionService;
import es.commerzbank.ice.embargos.service.files.Cuaderno63SeizureService;
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
	Cuaderno63LiftingService cuaderno63LiftingService;

	@Autowired
	AEATLiftingService aeatLiftingService;
	
	@Autowired
	private Cuaderno63PetitionService cuaderno63PetitionService;
	
	@Autowired
	private Cuaderno63SeizureService cuaderno63SeizureService;
	
	@Autowired
	private AEATSeizureService aeatSeizureService;
	
	@Autowired
	private AEATSeizedResultService aeatSeizedResultService;
	
	public void procesarFichero(File file)
	{
			try {
				//Si fichero es de la AEAT (tiene prefijo "AEAT_")
				if (FilenameUtils.getBaseName(file.getCanonicalPath()).toLowerCase().contains(EmbargosConstants.AEAT.toLowerCase())) {

					parsearFicheroAEAT(file);

				} else {

					parsearFicheroCuaderno63(file);
				}

				moverFichero(file, pathProcessed);
				LOG.debug("The file " + FilenameUtils.getName(file.getCanonicalPath()) + " has been moved to the path " + pathProcessed);

			} catch (Exception e) {
				try {
					moverFichero(file, pathError);
					LOG.debug("The file " + FilenameUtils.getName(file.getCanonicalPath()) + " has been moved to the path " + pathError);
				} catch (IOException ioe) { LOG.error("Could not move the file "+ file + " to the target path "+ pathError); }
			}
	}


	private void parsearFicheroAEAT(File file) throws IOException, ICEParserException {
		
		String tipoFichero = FilenameUtils.getExtension(file.getCanonicalPath()).toUpperCase();
				
		switch (tipoFichero) {						
			//- Tipos de ficheros AEAT:	
			case EmbargosConstants.TIPO_FICHERO_EMBARGOS:
				aeatSeizureService.tratarFicheroDiligenciasEmbargo(file);
				break;
			case EmbargosConstants.TIPO_FICHERO_LEVANTAMIENTOS:
				aeatLiftingService.tratarFicheroLevantamientos(file);
				break;
			case EmbargosConstants.TIPO_FICHERO_ERRORES:
				aeatSeizedResultService.tratarFicheroErrores(file);
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
				cuaderno63PetitionService.cargarFicheroPeticion(file);
				break;
			case EmbargosConstants.TIPO_FICHERO_EMBARGOS:
				cuaderno63SeizureService.cargarFicheroEmbargos(file);
				break;	
			case EmbargosConstants.TIPO_FICHERO_LEVANTAMIENTOS:
				cuaderno63LiftingService.tratarFicheroLevantamientos(file);
				break;			
			default:
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
