package es.commerzbank.ice.embargos.listener;

import java.io.File;
import java.util.concurrent.TimeUnit;

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
 
 
	@Value("${commerzbank.impuestos.files.path.monitoring}")
	private String pathMonitoring;
        
    @Value("${commerzbank.impuestos.files.monitoring-interval-in-seconds}")
    private long intervalInSeconds;
    
    @Value("${commerzbank.impuestos.files.suffix-file-filter}")
    private String suffixFileFilter;
    
    // Automatically inject business services
    @Autowired
    private FileManagementService fileManagementService;
 
    public FileAlterationMonitor getMonitor() {
        
    	// Create a filter
        IOFileFilter directories = FileFilterUtils.and(
                FileFilterUtils.directoryFileFilter(),
                HiddenFileFilter.VISIBLE);
        
        IOFileFilter files = FileFilterUtils.and(
                FileFilterUtils.fileFileFilter(),
                FileFilterUtils.suffixFileFilter(".".concat(suffixFileFilter)));
        
        IOFileFilter filter = FileFilterUtils.or(directories, files);
 
        // Assembly filter
        FileAlterationObserver observer = new FileAlterationObserver(new File(pathMonitoring), filter);
 
        // Add a listener to the listener and inject the business service
        observer.addListener(new FileListener(fileManagementService));
 
        Long intervalInMillis = TimeUnit.SECONDS.toMillis(intervalInSeconds);
        
        // return listener
        return new FileAlterationMonitor(intervalInMillis, observer);
    }
}