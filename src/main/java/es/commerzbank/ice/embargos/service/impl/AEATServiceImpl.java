package es.commerzbank.ice.embargos.service.impl;

import java.io.File;

import javax.transaction.Transactional;

import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.commerzbank.ice.embargos.formats.aeat.diligencias.Diligencia;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.InformationPetitionRepository;
import es.commerzbank.ice.embargos.service.AEATService;
import es.commerzbank.ice.utils.EmbargosConstants;

@Service
@Transactional
public class AEATServiceImpl implements AEATService{

	private static final Logger LOG = LoggerFactory.getLogger(AEATServiceImpl.class);
	
	@Value("${commerzbank.embargos.beanio.config-path.aeat}")
	String pathFileConfigAEAT;
	
	//Agregar repositories de DWH ...
	@Autowired
	FileControlRepository fileControlRepository;
	
	@Autowired
	InformationPetitionRepository informationPetitionRepository;
	
	public void tratarFicheroDiligenciasEmbargo(File file) {
		
        // create a StreamFactory
        StreamFactory factory = StreamFactory.newInstance();
        // load the mapping file
        factory.loadResource(pathFileConfigAEAT);
        
        // use a StreamFactory to create a BeanReader
        BeanReader in = factory.createReader(EmbargosConstants.STREAM_NAME_DILIGENCIAS, file);
        Object record = null;
        while ((record = in.read()) != null) {
        	
        	if(EmbargosConstants.RECORD_NAME_DILIGENCIA.equals(in.getRecordName())) {
        		
        		Diligencia diligencia = (Diligencia) record;
        		LOG.debug(diligencia.getNifDeudor());
        	}
        	
        	LOG.debug(record.toString());
        }
        in.close();
		
	}
	
	public void tratarFicheroLevantamientos(File file) {
		
	}
	
	public void tratarFicheroErrores(File file) {
		
	}

}
