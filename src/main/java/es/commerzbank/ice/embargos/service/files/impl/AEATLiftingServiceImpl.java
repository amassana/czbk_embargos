package es.commerzbank.ice.embargos.service.files.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.List;

import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.comun.lib.file.generate.ContaGenExecutor;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.util.ICEParserException;
import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.CuentaLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlfichero;
import es.commerzbank.ice.embargos.domain.entity.EstadoLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.LevantamientoTraba;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.embargos.domain.mapper.AEATMapper;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.formats.aeat.levantamientotrabas.Levantamiento;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.LiftingBankAccountRepository;
import es.commerzbank.ice.embargos.repository.LiftingRepository;
import es.commerzbank.ice.embargos.repository.SeizedRepository;
import es.commerzbank.ice.embargos.repository.SeizureRepository;
import es.commerzbank.ice.embargos.service.AccountingService;
import es.commerzbank.ice.embargos.service.ClientDataService;
import es.commerzbank.ice.embargos.service.CustomerService;
import es.commerzbank.ice.embargos.service.files.AEATLiftingService;
import es.commerzbank.ice.utils.EmbargosConstants;
import es.commerzbank.ice.utils.EmbargosUtils;

@Service
@Transactional(transactionManager="transactionManager")
public class AEATLiftingServiceImpl
    implements AEATLiftingService
{
    private static final Logger LOG = LoggerFactory.getLogger(AEATLiftingServiceImpl.class);

    @Value("${commerzbank.embargos.beanio.config-path.aeat}")
    String pathFileConfigAEAT;

    //@Value("${commerzbank.embargos.beanio.config-path.aeat}")
    // TODO
    BigDecimal limiteAutomatico = new BigDecimal(50000);
    @Autowired
    FileControlMapper fileControlMapper;

	@Autowired
	private ClientDataService clientDataService;
	   
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
    AEATMapper aeatMapper;
    @Autowired
    AccountingService accountingService;
    @Autowired
    GeneralParametersService generalParametersService;
    @Autowired
    ContaGenExecutor contaGenExecutor;

    @Override
    public void tratarFicheroLevantamientos(File file, String originalName) throws IOException, ICEParserException {
        
    	BeanReader beanReader = null;
        Reader reader = null;
        
        try {
            BigDecimal importeMaximoAutomaticoDivisa =
                    generalParametersService.loadBigDecimalParameter(EmbargosConstants.PARAMETRO_EMBARGOS_LEVANTAMIENTO_IMPORTE_MAXIMO_AUTOMATICO_DIVISA);

            // Inicializar control fichero
            ControlFichero controlFicheroLevantamiento =
                    fileControlMapper.generateControlFichero(file, EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_AEAT, originalName);

            fileControlRepository.save(controlFicheroLevantamiento);

            // Inicialiar beanIO parser
            StreamFactory factory = StreamFactory.newInstance();
            factory.loadResource(pathFileConfigAEAT);
            
	        String encoding = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_ENCODING_AEAT);
			
	        reader = new InputStreamReader(new FileInputStream(file), encoding);
            beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_AEAT_LEVANTAMIENTOTRABAS, reader);

            Object currentRecord = null;

            boolean allLevantamientosContabilizados = true;
            // almacena las cuentas que se han contabilizado, para su actualización posterior de estado.
            //List<CuentaLevantamiento> cuentasContabilizadas = new ArrayList<>();

            while ((currentRecord = beanReader.read()) != null) {
                if (EmbargosConstants.RECORD_NAME_AEAT_ENTIDADTRANSMISORA.equals(beanReader.getRecordName()))
                {
                    ;
                }
                else if (EmbargosConstants.RECORD_NAME_AEAT_ENTIDADCREDITO.equals(beanReader.getRecordName()))
                {
                    ;
                }
                else if (EmbargosConstants.RECORD_NAME_AEAT_LEVANTAMIENTO.equals(beanReader.getRecordName()))
                {
                    Levantamiento levantamientoAEAT = (Levantamiento) currentRecord;

                    List<Embargo> embargos = seizureRepository.findAllByNumeroEmbargo(levantamientoAEAT.getNumeroDiligenciaEmbargo());

                    if (embargos == null || embargos.size() == 0)
                    {
                        LOG.info("No embargo found for "+ levantamientoAEAT.getNumeroDiligenciaEmbargo());
                        // TODO ERROR
                        continue;
                    }

                    Embargo embargo = EmbargosUtils.selectEmbargo(embargos);

                    Traba traba = seizedRepository.getByEmbargo(embargo);

                    if (traba == null)
                    {
                        LOG.error("Levantamiento not found for embargo "+ embargo.getCodEmbargo() +" code "+ levantamientoAEAT.getNumeroDiligenciaEmbargo());
                        continue;
                    }

                    LOG.info("Using traba "+ traba.getCodTraba() +" for embargo "+ embargo.getCodEmbargo() +" code "+ levantamientoAEAT.getNumeroDiligenciaEmbargo());

                    // recuperar account <- razon interna
                    // recuperar cod traba
                    // estado contable?
                    // estado ejecutado?
                    CustomerDTO customerDTO = customerService.findCustomerByNif(levantamientoAEAT.getNifDeudor());

                    //Se guardan los datos del cliente:
	        		clientDataService.createUpdateClientDataTransaction(customerDTO, levantamientoAEAT.getNifDeudor());
                    
                    LevantamientoTraba levantamiento = aeatMapper.generateLevantamiento(controlFicheroLevantamiento.getCodControlFichero(), levantamientoAEAT, traba, customerDTO);

                    liftingRepository.save(levantamiento);

                    boolean allCuentasLevantamientoContabilizados = true;

                    for (CuentaLevantamiento cuentaLevantamiento : levantamiento.getCuentaLevantamientos())
                    {
                        if (!EmbargosConstants.ISO_MONEDA_EUR.equals(cuentaLevantamiento.getCodDivisa()) &&
                                importeMaximoAutomaticoDivisa.compareTo(cuentaLevantamiento.getImporte()) < 0) {
                            allLevantamientosContabilizados = false;
                            allCuentasLevantamientoContabilizados = false;
                            LOG.info("Cannot perform an automatic seizure lifting: "+ cuentaLevantamiento.getCodDivisa() +" and "+ cuentaLevantamiento.getImporte().toPlainString());
                        }
                        else
                        {
                            LOG.info("Doing an automatic seizure lifting.");

                            EstadoLevantamiento estadoLevantamiento = new EstadoLevantamiento();
                            estadoLevantamiento.setCodEstado(EmbargosConstants.COD_ESTADO_LEVANTAMIENTO_PENDIENTE_RESPUESTA_CONTABILIZACION);
                            cuentaLevantamiento.setEstadoLevantamiento(estadoLevantamiento);
                        }

                        liftingBankAccountRepository.save(cuentaLevantamiento);

                        accountingService.sendAccountingLiftingBankAccount(cuentaLevantamiento, embargo, EmbargosConstants.USER_AUTOMATICO);
                    }

                    if (allCuentasLevantamientoContabilizados) {
                        EstadoLevantamiento estadoLevantamiento = new EstadoLevantamiento();
                        estadoLevantamiento.setCodEstado(EmbargosConstants.COD_ESTADO_LEVANTAMIENTO_PENDIENTE_RESPUESTA_CONTABILIZACION);
                        levantamiento.setEstadoLevantamiento(estadoLevantamiento);
                        levantamiento.setIndCasoRevisado(EmbargosConstants.IND_FLAG_YES);
                        liftingRepository.save(levantamiento);
                    }
                }
                else if (EmbargosConstants.RECORD_NAME_AEAT_FINENTIDADCREDITO.equals(beanReader.getRecordName()))
                {
                    ;
                }
                else if (EmbargosConstants.RECORD_NAME_AEAT_FINENTIDADTRANSMISORA.equals(beanReader.getRecordName()))
                {
                    ;
                }
                else if (EmbargosConstants.RECORD_NAME_AEAT_REGISTROCONTROLENTIDADTRANSMISORA.equals(beanReader.getRecordName()))
                {
                    ;
                }
                else
                   LOG.info(beanReader.getRecordName());// throw new Exception("BeanIO - Unexpected record name: "+ beanReader.getRecordName());
            }

            // cerrar y enviar la contabilización
            if (allLevantamientosContabilizados) {
                contaGenExecutor.generacionFicheroContabilidad(controlFicheroLevantamiento.getCodControlFichero());
            }

            // Actualizar control fichero

            EstadoCtrlfichero estadoCtrlfichero = null;

            if (allLevantamientosContabilizados) {
                estadoCtrlfichero = new EstadoCtrlfichero(
                        EmbargosConstants.COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_PENDING_ACCOUNTING_RESPONSE,
                        EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_NORMA63);
                
                //Cambio del indProcesado a 'S' al cambiar al estado 'Pendiente de respuesta contable':
                controlFicheroLevantamiento.setIndProcesado(EmbargosConstants.IND_FLAG_SI);
            }
            else
            {
                estadoCtrlfichero = new EstadoCtrlfichero(
                        EmbargosConstants.COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_RECEIVED,
                        EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_NORMA63);
            }

            controlFicheroLevantamiento.setEstadoCtrlfichero(estadoCtrlfichero);
        }
        catch (Exception e)
        {
            LOG.error("Error while treating AEAT LEV file", e);
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
