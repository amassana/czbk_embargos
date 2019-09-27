package es.commerzbank.ice.embargos.service.files.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.CuentaLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlfichero;
import es.commerzbank.ice.embargos.domain.entity.LevantamientoTraba;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.embargos.domain.mapper.Cuaderno63Mapper;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase5.CabeceraEmisorFase5;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase5.FinFicheroFase5;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase5.OrdenLevantamientoRetencionFase5;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.LiftingBankAccountRepository;
import es.commerzbank.ice.embargos.repository.LiftingRepository;
import es.commerzbank.ice.embargos.repository.SeizedRepository;
import es.commerzbank.ice.embargos.repository.SeizureRepository;
import es.commerzbank.ice.embargos.service.CustomerService;
import es.commerzbank.ice.embargos.service.files.Cuaderno63LiftingService;
import es.commerzbank.ice.utils.EmbargosConstants;
import es.commerzbank.ice.utils.EmbargosUtils;

/*
TODO:
 FileControlMapper.determineInitialEstadoCtrlFicheroByCodTipoFichero -> afegir constants.
 Cargar: Razón Social interna
 */
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
    @Autowired
    LiftingRepository liftingRepository;
    @Autowired
    SeizureRepository seizureRepository;
    @Autowired
    SeizedRepository seizedRepository;
    @Autowired
    LiftingBankAccountRepository liftingBankAccountRepository;
    @Autowired
    CustomerService customerService;
    @Autowired
    Cuaderno63Mapper cuaderno63Mapper;
    
	@Autowired
	private GeneralParametersService generalParametersService;
	
    @Override
    public void tratarFicheroLevantamientos(File file)
        throws IOException
    {
        BeanReader beanReader = null;
        Reader reader = null;

        try {
            // Inicializar control fichero
            ControlFichero controlFicheroLevantamiento =
                    fileControlMapper.generateControlFichero(file, EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_NORMA63);

            fileControlRepository.save(controlFicheroLevantamiento);

            // Inicialiar beanIO parser
            StreamFactory factory = StreamFactory.newInstance();
            factory.loadResource(pathFileConfigCuaderno63);
            
	        String encoding = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_ENCODING_NORMA63);
			
	        reader = new InputStreamReader(new FileInputStream(file), encoding);
            beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_CUADERNO63_FASE5, reader);

            Object currentRecord = null;

            while ((currentRecord = beanReader.read()) != null) {
                if (EmbargosConstants.RECORD_NAME_CABECERAEMISOR.equals(beanReader.getRecordName())) {
                    CabeceraEmisorFase5 cabeceraEmisorFase5 = (CabeceraEmisorFase5) currentRecord;
                    // Recuperar organisme emissor?
                }
                else if (EmbargosConstants.RECORD_NAME_FINFICHERO.equals(beanReader.getRecordName())) {
                    FinFicheroFase5 finFicheroFase5 = (FinFicheroFase5) currentRecord;
                    // validar num línies
                    // validar sum amount
                }
                else if (EmbargosConstants.RECORD_NAME_ORDENLEVANTAMIENTORETENCION.equals(beanReader.getRecordName()))
                {
                    OrdenLevantamientoRetencionFase5 ordenLevantamientoRetencionFase5 = (OrdenLevantamientoRetencionFase5) currentRecord;

                    List<Embargo> embargos = seizureRepository.findAllByNumeroEmbargo(ordenLevantamientoRetencionFase5.getIdentificadorDeuda());

                    if (embargos == null || embargos.size() == 0)
                    {
                        LOG.info("No embargo found for "+ ordenLevantamientoRetencionFase5.getIdentificadorDeuda());
                        // TODO ERROR
                        continue;
                    }

                    Embargo embargo = EmbargosUtils.selectEmbargo(embargos);

                    Traba traba = seizedRepository.getByEmbargo(embargo);

                    if (traba == null)
                    {
                        LOG.error("Levantamiento not found for embargo "+ embargo.getCodEmbargo() +" code "+ ordenLevantamientoRetencionFase5.getIdentificadorDeuda());
                        continue;
                    }

                    LOG.info("Using traba "+ traba.getCodTraba() +" for embargo "+ embargo.getCodEmbargo() +" code "+ ordenLevantamientoRetencionFase5.getIdentificadorDeuda());

                    // recuperar account <- razon interna
                    // recuperar cod traba
                    // estado contable?
                    // estado ejecutado?
                    CustomerDTO customerDTO = customerService.findCustomerByNif(ordenLevantamientoRetencionFase5.getNifDeudor());

                    LevantamientoTraba levantamiento = cuaderno63Mapper.generateLevantamiento(controlFicheroLevantamiento.getCodControlFichero(), ordenLevantamientoRetencionFase5, traba, customerDTO);

                    liftingRepository.save(levantamiento);

                    for (CuentaLevantamiento cuentaLevantamiento : levantamiento.getCuentaLevantamientos())
                    {
                        liftingBankAccountRepository.save(cuentaLevantamiento);
                    }
                }
                else
                    throw new Exception("BeanIO - Unexpected record name: "+ beanReader.getRecordName());
            }

            //Cambio de estado de CtrlFichero a: RECIBIDO
            EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero(
                    EmbargosConstants.COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_RECEIVED,
                    EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_NORMA63);
            controlFicheroLevantamiento.setEstadoCtrlfichero(estadoCtrlfichero);

            fileControlRepository.save(controlFicheroLevantamiento);
        }
        catch (Exception e)
        {
            LOG.error("Error while treating NORMA63 LEV file", e);
            // TODO error treatment
        }
        finally
        {
            if(reader!=null) {
            	reader.close();
            }
        	
        	if (beanReader != null)
                beanReader.close();
        }
    }
}
