package es.commerzbank.ice.embargos.filePoller;

import es.commerzbank.ice.comun.lib.file.exchange.FileProcessor;
import es.commerzbank.ice.comun.lib.file.exchange.FolderPoller;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.embargos.service.files.Cuaderno63LiftingService;
import es.commerzbank.ice.embargos.service.files.Cuaderno63PetitionService;
import es.commerzbank.ice.embargos.service.files.Cuaderno63SeizureService;
import es.commerzbank.ice.utils.EmbargosConstants;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.Duration;
import java.time.Instant;

@Component
public class Norma63FilePoller
    implements FileProcessor
{
    private static final Logger LOG = LoggerFactory.getLogger(Norma63FilePoller.class);

    private static final String pollerName = "Norma63 Poller";
    @Autowired
    private FolderPoller folderPoller;

    @Value("${commerzbank.embargos.files.suffix-file-filter-pet}")
    private String suffixFileFilterPet;

    @Value("${commerzbank.embargos.files.suffix-file-filter-emb}")
    private String suffixFileFilterEmb;

    @Value("${commerzbank.embargos.files.suffix-file-filter-lev}")
    private String suffixFileFilterLev;

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

    @Bean
    private FolderPoller folderPoller()
    {
        FolderPoller folderPoller = new FolderPoller();
        folderPoller.setPollerName(pollerName);

        folderPoller.addAcceptedExtension(suffixFileFilterPet);
        folderPoller.addAcceptedExtension(suffixFileFilterEmb);
        folderPoller.addAcceptedExtension(suffixFileFilterLev);

        folderPoller.setArchivalDate(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_ARCHIVAL_FOLDER_DATE_PATTERN, "yyyy"));

        return folderPoller;
    }

    @Scheduled(fixedRateString = "${commerzbank.embargos.files.monitoring-interval-in-millis}")
    public void scheduledFileReading()
    {
        try {
            if (lastVariableUpdate == null || Duration.between(lastVariableUpdate, Instant.now()).getSeconds() >= pollTime) {
                lastVariableUpdate = Instant.now();

                folderPoller.setInboxFolder(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_INCOMING));
                folderPoller.setProcessingFolder(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_PROCESSING));
                folderPoller.setProcessedFolder(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_PROCESSED));
                folderPoller.setErrorFolder(generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_ERROR));

                folderPoller.setStableTime(generalParametersService.loadIntegerParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_STABLE_TIME));

                folderPoller.setMaxProcessingTime(generalParametersService.loadIntegerParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_MAX_PROCESSING_TIME));

                folderPoller.setFileProcessor(this);
            }

            if (!folderPoller.allFoldersExist()) {
                lastVariableUpdate = Instant.now();
                throw new Exception("Not all the working folders exist. Check the configuration.");
            }

            folderPoller.poll();
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

            LOG.info(pollerName +": "+ originalName +" tratado con éxito");

            try
            {
                folderPoller.moveToError(file);
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
                folderPoller.moveToError(file);
            }
            catch (Exception e2)
            {
                LOG.error(pollerName +": Error mientras se movía a la carpeta de errores "+ file.getName(), e2);
            }
        }
    }
}
