package es.commerzbank.ice.embargos.service.files.impl;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.service.files.Cuaderno63LiftingService;
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
public class Cuaderno63LiftingServiceImpl
    implements Cuaderno63LiftingService
{

    private static final Logger LOG = LoggerFactory.getLogger(Cuaderno63LiftingServiceImpl.class);

    @Value("${commerzbank.embargos.beanio.config-path.cuaderno63}")
    String pathFileConfigCuaderno63;

    @Autowired
    FileControlMapper fileControlMapper;
    @Autowired
    FileControlRepository fileControlRepository;
    @Override
    public void tratarFicheroLevantamientos(File file)
        throws IOException
    {
        BeanReader beanReader = null;

        try {
            // Inicializar control fichero
            ControlFichero controlFicheroEmbargo =
                    fileControlMapper.generateControlFichero(file, EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_NORMA63);

            fileControlRepository.save(controlFicheroEmbargo);

            // Inicialiar beanIO parser
            StreamFactory factory = StreamFactory.newInstance();
            factory.loadResource(pathFileConfigCuaderno63);
            beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_CUADERNO63_FASE5, file);

            Object currentRow = null;

            while ((currentRow = beanReader.read()) != null) {
                LOG.info("Reading "+ beanReader.getRecordName());
            }
        }
        catch (Exception e)
        {
            LOG.error("Error while trating NORMA63 LEV file", e);
        }
    }
}
