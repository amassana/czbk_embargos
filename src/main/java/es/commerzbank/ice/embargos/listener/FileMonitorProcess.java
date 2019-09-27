package es.commerzbank.ice.embargos.listener;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import es.commerzbank.ice.utils.EmbargosConstants;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.embargos.service.FileManagementService;

@Component
public class FileMonitorProcess {

    private static final Logger LOG = LoggerFactory.getLogger(FileMonitorProcess.class);

    @Value("${commerzbank.embargos.files.monitoring-interval-in-seconds}")
    private long intervalInSeconds;
    
    
    @Value("${commerzbank.embargos.files.suffix-file-filter-pet}")
    private String suffixFileFilterPet;
  
    @Value("${commerzbank.embargos.files.suffix-file-filter-inf}")
    private String suffixFileFilterInf;
    
    @Value("${commerzbank.embargos.files.suffix-file-filter-emb}")
    private String suffixFileFilterEmb;
    
    @Value("${commerzbank.embargos.files.suffix-file-filter-tra}")
    private String suffixFileFilterTra;
    
    @Value("${commerzbank.embargos.files.suffix-file-filter-lev}")
    private String suffixFileFilterLev;
    
    @Value("${commerzbank.embargos.files.suffix-file-filter-err}")
    private String suffixFileFilterErr;
    
    @Value("${commerzbank.embargos.files.suffix-file-filter-fin}")
    private String suffixFileFilterFin;
    
    @Value("${commerzbank.embargos.files.suffix-file-filter-con}")
    private String suffixFileFilterCon;

    @Value("${commerzbank.embargos.files.stable-size-time-in-millis}")
    private long stableTime;

	@Autowired
	private GeneralParametersService generalParametersService;
	
    // Automatically inject business services
    @Autowired
    private FileManagementService fileManagementService;

    // Absolute path to pending file
    // ideally it should be emptied from time to time...
    HashMap<String, PendingFile> pendingFiles = new HashMap<>();

    private boolean helloWorld = false;
    private String pathIncoming;
    
    // TODO configurable..
    @Scheduled(fixedDelay = 5000)
    public void scheduledFileReading()
    {
        try
        {
        	
        	//TODO: workarround usando un solo poller, deberian haber 2 pollers: uno para AEAT y otro para NORMA63
        	if (pathIncoming == null)
        		pathIncoming = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_INCOMING);
        	
        	File dir = new File(pathIncoming);

            if (!helloWorld)
            {
                LOG.info("Starting the file polling service. Scanning "+ dir.getCanonicalPath());
                helloWorld = true;
            }

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

            List<File> currentFiles = (List<File>) FileUtils.listFiles(dir, extensionList, false);

            if (currentFiles.size() > 0)
            {
                LOG.info(currentFiles.size() + " files found at "+ dir.getCanonicalPath());
            }

            for (File currentFile : currentFiles)
            {
                long currentTime = System.currentTimeMillis();
                String currentPath = currentFile.getCanonicalPath();
                long currentLength = currentFile.length();

                PendingFile pendingFile = pendingFiles.get(currentPath);

                if (pendingFile == null)
                {
                    LOG.info("Queueing "+ currentPath);

                    pendingFile = new PendingFile();

                    pendingFile.setSnapshotTime(currentTime);
                    pendingFile.setFile(currentFile);
                    pendingFile.setLastsize(currentFile.length());

                    pendingFiles.put(currentFile.getCanonicalPath(), pendingFile);
                }
                else
                {
                    if (pendingFile.getPendingMove() != null)
                    {
                        LOG.debug("Ignoring processed file "+ pendingFile.getFile().getCanonicalFile());
                        boolean reMove = fileManagementService.pendingMove(pendingFile.getFile(), pendingFile.getPendingMove());
                        if (reMove)
                        {
                            pendingFiles.remove(currentPath);
                        }
                    }
                    else {
                        if (currentLength != pendingFile.getLastsize()) {
                            LOG.info("Size difference for " + currentPath + " " + currentLength + " vs previous " + pendingFile.getLastsize());

                            pendingFile.setLastsize(currentFile.length());
                            pendingFile.setSnapshotTime(currentTime);
                        } else {
                            long needsToWait = pendingFile.getSnapshotTime() + stableTime - currentTime;
                            if (needsToWait > 0) {
                                LOG.info(currentPath + " still needs to wait at least " + needsToWait + " more milliseconds.");

                                continue;
                            } else {
                                LOG.info("Processing " + currentPath);

                                String pendingMove = fileManagementService.procesarFichero(currentFile);

                                if (pendingMove == null) {
                                    pendingFiles.remove(currentPath);
                                }
                                else
                                {
                                    pendingFile.setPendingMove(pendingMove);
                                }
                            }
                        }
                    }
                }
            }

        }
        catch (Exception e)
        {
            LOG.error("Error while treating files", e);
        }
    }
}