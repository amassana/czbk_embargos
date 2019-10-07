package es.commerzbank.ice.embargos.filePoller;

import es.commerzbank.ice.comun.lib.file.exchange.FileProcessor;
import es.commerzbank.ice.comun.lib.file.exchange.FolderPoller;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.util.ValueConstants;
import es.commerzbank.ice.comun.lib.util.YAMLUtil;
import es.commerzbank.ice.embargos.service.files.*;
import es.commerzbank.ice.utils.EmbargosConstants;
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

@Component
public class AEAT63FilePoller
    implements FileProcessor
{
    private static final Logger LOG = LoggerFactory.getLogger(AEAT63FilePoller.class);

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
        }
        catch (IOException ioe) {LOG.error(pollerName +": accepted extensions can't be fully configured");}

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
    public void processFile(String originalName, File file)
    {
        try {
            String tipoFichero = FilenameUtils.getExtension(file.getCanonicalPath()).toUpperCase();

            switch (tipoFichero) {
                case EmbargosConstants.TIPO_FICHERO_EMBARGOS:
                    aeatSeizureService.tratarFicheroDiligenciasEmbargo(file, originalName);
                    break;
                case EmbargosConstants.TIPO_FICHERO_LEVANTAMIENTOS:
                    aeatLiftingService.tratarFicheroLevantamientos(file, originalName);
                    break;
                case EmbargosConstants.TIPO_FICHERO_ERRORES:
                    aeatSeizedResultService.tratarFicheroErrores(file, originalName);
                    break;
                default:
            }

            LOG.info(pollerName +": "+ originalName +" tratado con éxito");

            try
            {
                aeatFolderPoller.moveToProcessed(file);
            }
            catch (Exception e2)
            {
                LOG.error(pollerName +": Error mientras se movía a la carpeta de errores "+ file.getName(), e2);
            }
        }
        catch (Exception e)
        {
            LOG.error(pollerName +": Excepción mientras se trataba "+ file.getName(), e);
            try
            {
                aeatFolderPoller.moveToError(file);
            }
            catch (Exception e2)
            {
                LOG.error(pollerName +": Error mientras se movía a la carpeta de errores "+ file.getName(), e2);
            }
        }
    }
}
