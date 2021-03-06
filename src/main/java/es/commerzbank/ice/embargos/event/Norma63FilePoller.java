package es.commerzbank.ice.embargos.event;

import es.commerzbank.ice.comun.lib.file.exchange.FileProcessor;
import es.commerzbank.ice.comun.lib.file.exchange.FolderPoller;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.util.FileUtils;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.comun.lib.util.ValueConstants;
import es.commerzbank.ice.comun.lib.util.YAMLUtil;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.service.files.Cuaderno63LiftingService;
import es.commerzbank.ice.embargos.service.files.Cuaderno63PetitionService;
import es.commerzbank.ice.embargos.service.files.Cuaderno63SeizureService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Component
public class Norma63FilePoller
    implements FileProcessor
{
    private static final Logger LOG = LoggerFactory.getLogger(Norma63FilePoller.class);

    private static final String pollerName = "Norma63 Poller";
    @Autowired
    @Qualifier("n63FolderPoller")
    private FolderPoller n63FolderPoller;

    @Value("${commerzbank.embargos.files.configuration-poll-time-in-seconds}")
    private int pollTime;

    private Instant lastVariableUpdate;

    @Autowired
    private GeneralParametersService generalParametersService;

    @Autowired
    private Cuaderno63LiftingService cuaderno63LiftingService;

    @Autowired
    private Cuaderno63PetitionService cuaderno63PetitionService;

    @Autowired
    private Cuaderno63SeizureService cuaderno63SeizureService;

    @Autowired
    private FileControlRepository fileControlRepository;

    @Bean(name = "n63FolderPoller")
    private FolderPoller folderPoller()
    {
        FolderPoller folderPoller = new FolderPoller();
        folderPoller.setPollerName(pollerName);

        try {
            folderPoller.addAcceptedExtension(YAMLUtil.getValue(ValueConstants.APPLICATION_YAML_LOCAL_PATH, "commerzbank.embargos.files.suffix-file-filter-pet"));
            folderPoller.addAcceptedExtension(YAMLUtil.getValue(ValueConstants.APPLICATION_YAML_LOCAL_PATH, "commerzbank.embargos.files.suffix-file-filter-emb"));
            folderPoller.addAcceptedExtension(YAMLUtil.getValue(ValueConstants.APPLICATION_YAML_LOCAL_PATH, "commerzbank.embargos.files.suffix-file-filter-lev"));
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

                n63FolderPoller.setInboxFolder(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_INCOMING));
                n63FolderPoller.setProcessingFolder(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_PROCESSING));
                n63FolderPoller.setProcessedFolder(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_PROCESSED));
                n63FolderPoller.setErrorFolder(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_ERROR));

                n63FolderPoller.setStableTime(generalParametersService.loadIntegerParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_STABLE_TIME));

                n63FolderPoller.setMaxProcessingTime(generalParametersService.loadIntegerParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_MAX_PROCESSING_TIME));

                n63FolderPoller.setArchivalDate(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_ARCHIVAL_FOLDER_DATE_PATTERN, "yyyy"));

                n63FolderPoller.setFileProcessor(this);
            }

            n63FolderPoller.poll();
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
            String md5 = FileUtils.getMD5(processingFile.getCanonicalPath());
            Optional<ControlFichero> existingFile = fileControlRepository.findByNumCrc(md5);
            if (existingFile.isPresent())
                throw new Exception ("Encontrado un fichero con igual MD5 "+ md5 + " en CONTROL_FICHERO. Se descarta el proceso");

            String tipoFichero = FilenameUtils.getExtension(processingFile.getCanonicalPath()).toUpperCase();

            switch (tipoFichero) {
                case EmbargosConstants.TIPO_FICHERO_PETICIONES:
                    cuaderno63PetitionService.cargarFicheroPeticion(processingFile, originalName, processedFile);
                    break;
                case EmbargosConstants.TIPO_FICHERO_EMBARGOS:
                    cuaderno63SeizureService.cargarFicheroEmbargos(processingFile, originalName, processedFile);
                    break;
                case EmbargosConstants.TIPO_FICHERO_LEVANTAMIENTOS:
                    cuaderno63LiftingService.tratarFicheroLevantamientos(processingFile, originalName, processedFile);
                    break;
                default:
                    throw new ICEException("Tipo de fichero de norma 63 no reconocido: "+ processingFile.getName());
            }

            LOG.info(pollerName +": "+ originalName +" tratado con ??xito");

            try
            {
                n63FolderPoller.moveToProcessed(processingFile, processedFile);
            }
            catch (Exception e2)
            {
                LOG.error(pollerName +": Error mientras se mov??a a la carpeta de procesados "+ processingFile.getName(), e2);
            }
        } 
        catch (Exception e)
        {
            LOG.error(pollerName +": Excepci??n mientras se trataba "+ processingFile.getName(), e);
            try
            {
                n63FolderPoller.moveToError(processingFile);
            }
            catch (Exception e2)
            {
                LOG.error(pollerName +": Error mientras se mov??a a la carpeta de errores "+ processingFile.getName(), e2);
            }
        }
    }
}
