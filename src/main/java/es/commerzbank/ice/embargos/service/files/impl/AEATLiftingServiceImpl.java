package es.commerzbank.ice.embargos.service.files.impl;

import es.commerzbank.ice.comun.lib.util.ICEParserException;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.service.files.AEATLiftingService;
import es.commerzbank.ice.utils.EmbargosConstants;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;

@Service
@Transactional(transactionManager="transactionManager")
public class AEATLiftingServiceImpl
    implements AEATLiftingService
{
    private static final Logger LOG = LoggerFactory.getLogger(AEATLiftingServiceImpl.class);

    @Value("${commerzbank.embargos.beanio.config-path.aeat}")
    String pathFileConfigAEAT;

    @Autowired
    FileControlMapper fileControlMapper;

    @Autowired
    FileControlRepository fileControlRepository;

    @Override
    public void tratarFicheroLevantamientos(File file) throws IOException, ICEParserException {
        BeanReader beanReader = null;

        try {
            // Crear una entrada en Control Fichero
            ControlFichero controlFicheroEmbargo =
                    fileControlMapper.generateControlFichero(file, EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_AEAT);
            fileControlRepository.save(controlFicheroEmbargo);

            // Inicializar el parseador de la AEAT para Levantamientos
            StreamFactory factory = StreamFactory.newInstance();
            factory.loadResource(pathFileConfigAEAT);
            beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_AEAT_LEVANTAMIENTOTRABAS, file);

            Object currentRow = null;
            while ((currentRow = beanReader.read()) != null)
            {

            }
        }
        catch (Exception e)
        {
            ;
        }
    }
}
