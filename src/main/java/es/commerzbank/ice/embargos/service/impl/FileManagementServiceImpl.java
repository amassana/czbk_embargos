package es.commerzbank.ice.embargos.service.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.util.ICEException;
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
	
	@Autowired
	private GeneralParametersService generalParametersService;

	private static final String PENDING_TO_SUCCESS = "PENDING_TO_SUCCESS";
	private static final String PENDING_TO_ERROR = "PENDING_TO_ERROR";

	public String procesarFichero(File file) throws ICEException
	{
		
		String pathProcessed = null;
		String pathError = null;
		
		try {
			//Si fichero es de la AEAT (tiene prefijo "AEAT_")
			if (FilenameUtils.getBaseName(file.getCanonicalPath()).toLowerCase().contains(EmbargosConstants.AEAT.toLowerCase())) {

				parsearFicheroAEAT(file);

				pathProcessed = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_AEAT_PROCESSED);
				pathError = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_AEAT_ERROR);
				
			} else {

				parsearFicheroCuaderno63(file);
				
				pathProcessed = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_PROCESSED);
				pathError = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_ERROR);
			}


			
			boolean moveResult = moverFichero(file, pathProcessed);

			if (moveResult)
			{
				LOG.debug("The file " + file.getName() + " has been moved to the path " + pathProcessed);
				return null;
			}
			else
			{
				LOG.info("Failed to move the file The file " + file.getName() + " to the path " + pathProcessed);
				return PENDING_TO_SUCCESS;
			}
		} catch (Exception e) {
			LOG.error("Error while processing "+ file.getName(), e);

			boolean moveResult = moverFichero(file, pathError);

			if (moveResult) {
				LOG.debug("The file " + file.getName() + " has been moved to the path " + pathError);
				return null;
			}
			else
			{
				LOG.info("Failed to move the file The file " + file.getName() + " to the path " + pathProcessed);
				return PENDING_TO_ERROR;
			}
		}
	}


	private void parsearFicheroAEAT(File file) throws IOException, ICEException {
		
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

	
	private void parsearFicheroCuaderno63(File file) throws IOException, ICEException {
		
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

	public boolean pendingMove(File srcFile, String pendingAction) throws ICEException, IOException
	{
		
		String pathProcessed = null;
		String pathError = null;
		
		//Determinacion de la rutas pathProcessed y pathError, segun el tipo de fichero:
		if (FilenameUtils.getBaseName(srcFile.getCanonicalPath()).toLowerCase().contains(EmbargosConstants.AEAT.toLowerCase())) {
			
			//Fichero AEAT:
			pathProcessed = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_AEAT_PROCESSED);
			pathError = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_AEAT_ERROR);
			
		} else {
			//Fichero Norma63:
			pathProcessed = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_PROCESSED);
			pathError = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_ERROR);
		}
		
		
		if (PENDING_TO_SUCCESS.equals(pendingAction))
		{
			return moverFichero(srcFile, pathProcessed);
		}
		else
		{
			return moverFichero(srcFile, pathError);
		}
	}

	private boolean moverFichero(File srcFile, String destDir)
	{
		try {
			FileUtils.moveFileToDirectory(srcFile,new File(destDir),true);
			return true;
		} catch (IOException e) {
			try {
				LOG.error("Could not move the file " + srcFile.getName() + " to the destination " + destDir +": "+ e.getMessage());
				FileUtils.copyFileToDirectory(srcFile, new File(destDir));
				FileUtils.forceDelete(srcFile);
				return true;
			} catch (IOException e2) {
				LOG.error("Could not move the file " + srcFile.getName() + " to the destination " + destDir +": "+ e2.getMessage());
				return false;
			}
		}
	}
}
