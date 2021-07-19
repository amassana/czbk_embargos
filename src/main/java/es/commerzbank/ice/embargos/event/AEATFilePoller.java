package es.commerzbank.ice.embargos.event;

import com.google.common.io.Files;
import es.commerzbank.ice.comun.lib.file.exchange.FileProcessor;
import es.commerzbank.ice.comun.lib.file.exchange.FolderPoller;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.util.FileUtils;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.comun.lib.util.ValueConstants;
import es.commerzbank.ice.comun.lib.util.YAMLUtil;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.DiligenciaFase3;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.service.files.AEATLiftingService;
import es.commerzbank.ice.embargos.service.files.AEATSeizedResultService;
import es.commerzbank.ice.embargos.service.files.AEATSeizureService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import org.apache.commons.io.FilenameUtils;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Component
public class AEATFilePoller
    implements FileProcessor
{
    private static final Logger LOG = LoggerFactory.getLogger(AEATFilePoller.class);

    @Value("${commerzbank.embargos.beanio.config-path.aeat}")
	String pathFileConfigAEAT;
    
    private static final String pollerName = "AEAT Poller";
    @Autowired
    @Qualifier("aeatFolderPoller")
    private FolderPoller aeatFolderPoller;

    @Value("${commerzbank.embargos.files.configuration-poll-time-in-seconds}")
    private int pollTime;

    private Instant lastVariableUpdate;

    @Autowired
    private GeneralParametersService generalParametersService;

    @Autowired
    private AEATSeizureService aeatSeizureService;

    @Autowired
    private AEATSeizedResultService aeatSeizedResultService;

    @Autowired
    private AEATLiftingService aeatLiftingService;

    @Autowired
    private FileControlRepository fileControlRepository;

    @Bean(name = "aeatFolderPoller")
    private FolderPoller folderPoller()
    {
        FolderPoller folderPoller = new FolderPoller();
        folderPoller.setPollerName(pollerName);

        try {
            folderPoller.addAcceptedExtension(YAMLUtil.getValue(ValueConstants.APPLICATION_YAML_LOCAL_PATH, "commerzbank.embargos.files.suffix-file-filter-pet"));
            folderPoller.addAcceptedExtension(YAMLUtil.getValue(ValueConstants.APPLICATION_YAML_LOCAL_PATH, "commerzbank.embargos.files.suffix-file-filter-emb"));
            folderPoller.addAcceptedExtension(YAMLUtil.getValue(ValueConstants.APPLICATION_YAML_LOCAL_PATH, "commerzbank.embargos.files.suffix-file-filter-lev"));
            folderPoller.addAcceptedExtension(YAMLUtil.getValue(ValueConstants.APPLICATION_YAML_LOCAL_PATH, "commerzbank.embargos.files.suffix-file-filter-err"));
            folderPoller.addAcceptedExtension(YAMLUtil.getValue(ValueConstants.APPLICATION_YAML_LOCAL_PATH, "commerzbank.embargos.files.suffix-file-filter-res"));
        }
        catch (IOException ioe) {LOG.error(pollerName +": accepted extensions can't be fully configured", ioe);}

        return folderPoller;
    }

    @Scheduled(fixedRateString = "${commerzbank.embargos.files.monitoring-interval-in-milliseconds}")
    public void scheduledFileReading()
    {
        try {
            if (lastVariableUpdate == null || Duration.between(lastVariableUpdate, Instant.now()).getSeconds() >= pollTime) {
                lastVariableUpdate = Instant.now();

                aeatFolderPoller.setInboxFolder(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_AEAT_INCOMING));
                aeatFolderPoller.setProcessingFolder(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_AEAT_PROCESSING));
                aeatFolderPoller.setProcessedFolder(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_AEAT_PROCESSED));
                aeatFolderPoller.setErrorFolder(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_AEAT_ERROR));

                aeatFolderPoller.setStableTime(generalParametersService.loadIntegerParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_STABLE_TIME));

                aeatFolderPoller.setMaxProcessingTime(generalParametersService.loadIntegerParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_MAX_PROCESSING_TIME));

                aeatFolderPoller.setArchivalDate(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_ARCHIVAL_FOLDER_DATE_PATTERN, "yyyy"));

                aeatFolderPoller.setFileProcessor(this);
            }

            aeatFolderPoller.poll();
        }
        catch (Exception e)
        {
            // TODO: ALARMA?
            LOG.error("Error en "+ pollerName, e);
        }
    }

    @Async
    public void processFile(String originalName, File processingFile, File processedFile)
    {
        try {
            if (isIgnorableFile(processingFile)) {
                LOG.info("El fichero no es de un tipo procesable: emb o lev y empieza por 9. Se descarta su proceso.");
                aeatFolderPoller.moveToProcessed(processingFile, processedFile);
                return;
            }

            String md5 = FileUtils.getMD5(processingFile.getCanonicalPath());
            Optional<ControlFichero> existingFile = fileControlRepository.findByNumCrc(md5);
            if (existingFile.isPresent())
                throw new Exception ("Encontrado un fichero con igual MD5 "+ md5 + " en CONTROL_FICHERO. Se descarta el proceso");

            String tipoFichero = FilenameUtils.getExtension(processingFile.getCanonicalPath()).toUpperCase();

            switch (tipoFichero) {
                case EmbargosConstants.TIPO_FICHERO_EMBARGOS:
                	
                	boolean isEmbargo = false;
                	
                	Reader reader = null;
                	BeanReader beanReader = null;
                	FileInputStream fileInputStream = null;
                	
                	try {
	        	        // create a StreamFactory
	        	        StreamFactory factory = StreamFactory.newInstance();
	        	        // load the mapping file
	        	        factory.loadResource(pathFileConfigAEAT);
	        	        
	        	        fileInputStream = new FileInputStream(processingFile);
	        	        reader = new InputStreamReader(fileInputStream); 
	        	        beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_AEAT_DILIGENCIAS, reader);
	        	        
	        	        Object record = null;
	
	        	        while ((record = beanReader.read()) != null && !isEmbargo) {
	        	        	if(EmbargosConstants.RECORD_NAME_AEAT_DILIGENCIA.equals(beanReader.getRecordName())) {
	        	        		DiligenciaFase3 diligenciaFase3 = (DiligenciaFase3) record;
	        	        		if (diligenciaFase3!=null) isEmbargo = true;
	        	        	}
	        	        }
                	} catch (Exception e) {
                		LOG.error("No es tracta d'un arxiu de embargos, per tant serà un arxiu de resposta: " + originalName);
            		} finally {
            			if(reader!=null) {
            				reader.close();
            			}
            			if (beanReader!=null) {
            				beanReader.close();
            			}
            			if (fileInputStream!=null) fileInputStream.close();
            		}
                	
                	if (isEmbargo) {
                		LOG.info("Es tracta d'un arxiu de embargos: " + originalName);
                		aeatSeizureService.tratarFicheroDiligenciasEmbargo(processingFile, originalName, processedFile);	
                	}
                	else {
                		LOG.info("No es tracta d'un arxiu de embargos, per tant serà un arxiu de resposta: " + originalName);
                		aeatSeizedResultService.tratarFicheroErrores(processingFile, originalName, processedFile);
                	}
                	
                    break;
                case EmbargosConstants.TIPO_FICHERO_LEVANTAMIENTOS:
                    aeatLiftingService.tratarFicheroLevantamientos(processingFile, originalName, processedFile);
                    break;
                case EmbargosConstants.TIPO_FICHERO_ERRORES:
                    aeatSeizedResultService.tratarFicheroErrores(processingFile, originalName, processedFile);
                    break;
                //case EmbargosConstants.TIPO_FICHERO_RESULTADO:
                    //aeatSeizedResultService.tratarFicheroErrores(processingFile, originalName, processedFile);
                    //break;
                default:
            }

            LOG.info(pollerName +": "+ originalName +" tratado con éxito");

            try
            {
                aeatFolderPoller.moveToProcessed(processingFile, processedFile);
            }
            catch (Exception e2)
            {
                LOG.error(pollerName +": Error mientras se movía a la carpeta de procesados "+ processingFile.getName(), e2);
            }
        }
        catch (Exception e)
        {
            LOG.error(pollerName +": Excepción mientras se trataba "+ processingFile.getName(), e);
            try
            {
                aeatFolderPoller.moveToError(processingFile);
            }
            catch (Exception e2)
            {
                LOG.error(pollerName +": Error mientras se movía a la carpeta de errores "+ processingFile.getName(), e2);
            }
        }
    }

    // solo los .emb y .lev se deben chechear. el resto no son ignorables.
    // si el primer carácter es un 9, se ignora
    private boolean isIgnorableFile(File processingFile) throws ICEException, IOException {
        String tipoFichero = FilenameUtils.getExtension(processingFile.getCanonicalPath()).toUpperCase();

        if (!tipoFichero.equals(EmbargosConstants.TIPO_FICHERO_EMBARGOS) && !tipoFichero.equals(EmbargosConstants.TIPO_FICHERO_LEVANTAMIENTOS))
            return false;

        String encoding = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_ENCODING_AEAT);
        boolean result;

        try (BufferedReader br = Files.newReader(processingFile, Charset.forName(encoding))) {
            String line = br.readLine();

            if (line == null)
                throw new ICEException("Could not read from the file "+ processingFile.getName());

            result = line.startsWith("9");
        }

        return result;
    }
}
