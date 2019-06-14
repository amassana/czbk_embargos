package es.commerzbank.ice.embargos.listener;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import es.commerzbank.ice.embargos.service.FileManagementService;

@Component
public class FileListenerFactory {
 
 
	@Value("${commerzbank.embargos.files.path.monitoring}")
	private String pathMonitoring;
        
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
    
    
    // Automatically inject business services
    @Autowired
    private FileManagementService fileManagementService;
 
    public FileAlterationMonitor getMonitor() {
        
    	
    	IOFileFilter filter = crearFiltro();
    	
        // Assembly filter
        FileAlterationObserver observer = new FileAlterationObserver(new File(pathMonitoring), filter);
 
        // Add a listener to the listener and inject the business service
        observer.addListener(new FileListener(fileManagementService));
 
        Long intervalInMillis = TimeUnit.SECONDS.toMillis(intervalInSeconds);
        
        // return listener
        return new FileAlterationMonitor(intervalInMillis, observer);
    }
    
    private IOFileFilter crearFiltro() {
    	
    	// Create a filter
        IOFileFilter directories = FileFilterUtils.and(
                FileFilterUtils.directoryFileFilter(),
                HiddenFileFilter.VISIBLE);
        
        IOFileFilter filesPet = FileFilterUtils.and(
                FileFilterUtils.fileFileFilter(),
                FileFilterUtils.suffixFileFilter(".".concat(suffixFileFilterPet), IOCase.INSENSITIVE));
        
        IOFileFilter filesInf = FileFilterUtils.and(
                FileFilterUtils.fileFileFilter(),
                FileFilterUtils.suffixFileFilter(".".concat(suffixFileFilterInf), IOCase.INSENSITIVE));
        
        IOFileFilter filesEmb = FileFilterUtils.and(
                FileFilterUtils.fileFileFilter(),
                FileFilterUtils.suffixFileFilter(".".concat(suffixFileFilterEmb), IOCase.INSENSITIVE));
        
        IOFileFilter filesTra = FileFilterUtils.and(
                FileFilterUtils.fileFileFilter(),
                FileFilterUtils.suffixFileFilter(".".concat(suffixFileFilterTra), IOCase.INSENSITIVE));
        
        IOFileFilter filesLev = FileFilterUtils.and(
                FileFilterUtils.fileFileFilter(),
                FileFilterUtils.suffixFileFilter(".".concat(suffixFileFilterLev), IOCase.INSENSITIVE));
        
        IOFileFilter filesErr = FileFilterUtils.and(
                FileFilterUtils.fileFileFilter(),
                FileFilterUtils.suffixFileFilter(".".concat(suffixFileFilterErr), IOCase.INSENSITIVE));
        
        IOFileFilter filesFin = FileFilterUtils.and(
                FileFilterUtils.fileFileFilter(),
                FileFilterUtils.suffixFileFilter(".".concat(suffixFileFilterFin), IOCase.INSENSITIVE));
        
        IOFileFilter filesCon = FileFilterUtils.and(
                FileFilterUtils.fileFileFilter(),
                FileFilterUtils.suffixFileFilter(".".concat(suffixFileFilterCon), IOCase.INSENSITIVE));
        
        return FileFilterUtils.or(directories, filesPet, filesInf, filesEmb, filesTra, filesLev, filesErr, filesFin, filesCon);

    }
    
}