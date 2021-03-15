package es.commerzbank.ice.embargos.service.files.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.comun.lib.domain.dto.Element;
import es.commerzbank.ice.comun.lib.domain.dto.TaskAndEvent;
import es.commerzbank.ice.comun.lib.service.EventService;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.service.TaskService;
import es.commerzbank.ice.comun.lib.typeutils.DateUtils;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.CuentaLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import es.commerzbank.ice.embargos.domain.entity.EntidadesOrdenante;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlfichero;
import es.commerzbank.ice.embargos.domain.entity.EstadoLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.LevantamientoTraba;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.embargos.domain.mapper.AEATMapper;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.formats.aeat.levantamientotrabas.EntidadCredito;
import es.commerzbank.ice.embargos.formats.aeat.levantamientotrabas.EntidadTransmisora;
import es.commerzbank.ice.embargos.formats.aeat.levantamientotrabas.Levantamiento;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.LiftingBankAccountRepository;
import es.commerzbank.ice.embargos.repository.LiftingRepository;
import es.commerzbank.ice.embargos.repository.OrderingEntityRepository;
import es.commerzbank.ice.embargos.repository.SeizedRepository;
import es.commerzbank.ice.embargos.repository.SeizureRepository;
import es.commerzbank.ice.embargos.service.CustomerService;
import es.commerzbank.ice.embargos.service.EmailService;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.files.AEATLiftingService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.EmbargosUtils;
import es.commerzbank.ice.embargos.utils.ICEDateUtils;

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
	private FileControlRepository fileControlRepository;
    
    @Autowired
	private TaskService taskService;
    
    @Autowired
	private EventService eventService;
    
    @Autowired
    private FileControlMapper fileControlMapper;
    
    @Autowired
    private LiftingRepository liftingRepository;
    
    @Autowired
    private SeizureRepository seizureRepository;
    
    @Autowired
    private SeizedRepository seizedRepository;
    
    @Autowired
    private LiftingBankAccountRepository liftingBankAccountRepository;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private AEATMapper aeatMapper;
    
    @Autowired
    private GeneralParametersService generalParametersService;
    
    @Autowired
	private EmailService emailService;
    
    @Autowired
	private FileControlService fileControlService;
    
    @Autowired
	private OrderingEntityRepository orderingEntityRepository;
    
    @Override
    public void tratarFicheroLevantamientos(File processingFile, String originalName, File processedFile) throws Exception {
    	
    	LOG.info("AEATLiftingServiceImpl - tratarFicheroLevantamientos - start");
    	
    	BeanReader beanReader = null;
        Reader reader = null;
        FileInputStream fileInputStream = null;
        
        ControlFichero controlFicheroLevantamiento = null;
        
        String levFileName = null;
        
        try {
        	
        	levFileName = FilenameUtils.getName(processingFile.getCanonicalPath());
        	
        	// create a StreamFactory
	        StreamFactory factory = StreamFactory.newInstance();
	        // load the mapping file
	        factory.loadResource(pathFileConfigAEAT);

            //Se guarda el registro de ControlFichero del fichero de entrada:
            controlFicheroLevantamiento =
                    fileControlMapper.generateControlFichero(processingFile, EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_AEAT, originalName, processedFile);

            fileControlService.saveFileControlTransaction(controlFicheroLevantamiento);
            
            
            // use a StreamFactory to create a BeanReader
	        String encoding = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_ENCODING_AEAT);
			
	        fileInputStream = new FileInputStream(processingFile);
	        reader = new InputStreamReader(fileInputStream, encoding);
            beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_AEAT_LEVANTAMIENTOTRABAS, reader);

            BigDecimal importeMaximoAutomaticoDivisa =
                    generalParametersService.loadBigDecimalParameter(EmbargosConstants.PARAMETRO_EMBARGOS_LEVANTAMIENTO_IMPORTE_MAXIMO_AUTOMATICO_DIVISA);
            
            Object currentRecord = null;

            boolean isEntidadTransmisoraCommerzbank = false;
	        
	        EntidadTransmisora entidadTransmisora = null;
	        EntidadCredito entidadCredito = null;
            EntidadesOrdenante entidadOrdenante = null;
	        
            boolean allLevantamientosContabilizados = true;
            
            // almacena las cuentas que se han contabilizado, para su actualización posterior de estado.
            //List<CuentaLevantamiento> cuentasContabilizadas = new ArrayList<>();

            while ((currentRecord = beanReader.read()) != null) {
            	
            	if(EmbargosConstants.RECORD_NAME_AEAT_ENTIDADTRANSMISORA.equals(beanReader.getRecordName())) {      	
	        		
            		entidadTransmisora = (EntidadTransmisora) currentRecord;
	        		
	        		//Si la entidad transmisora es Commerzbank:
	        		if (entidadTransmisora.getCodigoEntidadTransmisora() != null 
	        				&& entidadTransmisora.getCodigoEntidadTransmisora().equals(EmbargosConstants.CODIGO_NRBE_COMMERZBANK)) {   			
	        			
	        			isEntidadTransmisoraCommerzbank = true;
	        			
	        		} else {	
	        			isEntidadTransmisoraCommerzbank = false;
	        		}
	        	}

            	//Tratar registros solo si la entidad transmisora es Commerzbank:
	        	if (isEntidadTransmisoraCommerzbank) {
	        		
	        		if(EmbargosConstants.RECORD_NAME_AEAT_ENTIDADCREDITO.equals(beanReader.getRecordName())) {
		            	
	        			entidadCredito = (EntidadCredito) currentRecord;
		        		
		        		String identificadorEntidad = entidadCredito.getDelegacionAgenciaReceptora()!=null ? entidadCredito.getDelegacionAgenciaReceptora() : null;
		        		
		        		entidadOrdenante = orderingEntityRepository.findByIdentificadorEntidad(identificadorEntidad);
		        		
		        		if (entidadOrdenante == null) {
		        			throw new ICEException("No se puede procesar el fichero '" + processingFile.getName() +
		        					"': Entidad Ordenante con identificadorEntidad " + identificadorEntidad + " no encontrada.");
		        		}
		        	}

	                if (EmbargosConstants.RECORD_NAME_AEAT_LEVANTAMIENTO.equals(beanReader.getRecordName()))
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
	                    CustomerDTO customerDTO = customerService.findCustomerByNif(levantamientoAEAT.getNifDeudor(), true);
	
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
	
	                        cuentaLevantamiento.setUsuarioUltModificacion(EmbargosConstants.USER_AUTOMATICO);
	                        cuentaLevantamiento.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
	                        liftingBankAccountRepository.save(cuentaLevantamiento);
	
	                        // NO SE HACE AQUI LA CONTABILIDAD
	                        //controlFichero = accountingService.sendAccountingLiftingBankAccount(cuentaLevantamiento, embargo, EmbargosConstants.USER_AUTOMATICO);
	                    }
	
	                    if (allCuentasLevantamientoContabilizados) {
	                    	LOG.info("All seizure lifting count");
	                        EstadoLevantamiento estadoLevantamiento = new EstadoLevantamiento();
	                        estadoLevantamiento.setCodEstado(EmbargosConstants.COD_ESTADO_LEVANTAMIENTO_PENDIENTE_RESPUESTA_CONTABILIZACION);
	                        levantamiento.setEstadoLevantamiento(estadoLevantamiento);
	                        levantamiento.setIndCasoRevisado(EmbargosConstants.IND_FLAG_YES);
	                        
	                        levantamiento.setUsuarioUltModificacion(EmbargosConstants.USER_AUTOMATICO);
	                		levantamiento.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
	                        liftingRepository.save(levantamiento);
	                    }
	                }
	        	}
            }

            // cerrar y enviar la contabilización
            // NO SE HACE AQUI LA CONTABILIDAD
            //if (allLevantamientosContabilizados && controlFichero!=null) {
            	//accountingNoteService.generacionFicheroContabilidad(controlFichero);
            //}

            // Actualizar control fichero

            //- Se guarda el codigo de la Entidad comunicadora en ControlFichero:
	        //EntidadesComunicadora entidadComunicadora = entidadOrdenante.getEntidadesComunicadora();
	        controlFicheroLevantamiento.setEntidadesComunicadora(entidadOrdenante.getEntidadesComunicadora());
			
			//- Fechas de creacion y de comienzo de ciclo:
			Date fechaCreacionFicheroTransmision = entidadTransmisora.getFechaCreacionFicheroTrabas();
	        BigDecimal fechaCreacionFicheroTransmisionBigDec = fechaCreacionFicheroTransmision!=null ? ICEDateUtils.dateToBigDecimal(fechaCreacionFicheroTransmision, ICEDateUtils.FORMAT_yyyyMMdd) : null;
	        controlFicheroLevantamiento.setFechaCreacion(fechaCreacionFicheroTransmisionBigDec);
			
			Date fechaInicioCiclo = entidadTransmisora.getFechaInicioCiclo();
			BigDecimal fechaInicioCicloBigDec = fechaInicioCiclo!=null ? ICEDateUtils.dateToBigDecimal(fechaInicioCiclo, ICEDateUtils.FORMAT_yyyyMMdd) : null;
			controlFicheroLevantamiento.setFechaComienzoCiclo(fechaInicioCicloBigDec);
            
            EstadoCtrlfichero estadoCtrlfichero = null;

            if (allLevantamientosContabilizados) {
                estadoCtrlfichero = new EstadoCtrlfichero(
                        EmbargosConstants.COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_PENDING_ACCOUNTING_RESPONSE,
                        EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_AEAT);
                
                //Cambio del indProcesado a 'S' al cambiar al estado 'Pendiente de respuesta contable':
                controlFicheroLevantamiento.setIndProcesado(EmbargosConstants.IND_FLAG_SI);
            }
            else
            {
                estadoCtrlfichero = new EstadoCtrlfichero(
                        EmbargosConstants.COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_RECEIVED,
                        EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_AEAT);
            }

            controlFicheroLevantamiento.setEstadoCtrlfichero(estadoCtrlfichero);
            
            //CALENDARIO:
	        // - Se agrega la tarea al calendario:
	        TaskAndEvent task = new TaskAndEvent();
	        task.setDescription("Levantamiento " + controlFicheroLevantamiento.getNombreFichero());
	        task.setDate(DateUtils.convertToDate(LocalDate.now()));
	        task.setCodCalendar(1L);
	        task.setType("T");
	        Element office = new Element();
	        office.setCode(1L);
	        task.setOffice(office);
	        //
	        task.setAction("0");
	        task.setStatus("P");
	        task.setIndActive(true);
	        task.setApplication(EmbargosConstants.ID_APLICACION_EMBARGOS);
	        Long codTarea = taskService.addCalendarTask(task);
	        
            // Se crea el evento
	        TaskAndEvent event = new TaskAndEvent();
	        event.setDescription("Levantamiento recibido " + controlFicheroLevantamiento.getNombreFichero());
	        event.setDate(DateUtils.convertToDate(LocalDate.now()));
	        event.setCodCalendar(1L);
	        event.setType("E");
	        event.setAction("0");
	        event.setIndActive(true);
	        event.setIndVisualizarCalendario(true);
	        event.setApplication(EmbargosConstants.ID_APLICACION_EMBARGOS);
	        eventService.createOrUpdateEvent(event, EmbargosConstants.USER_AUTOMATICO);
	        LOG.info("Evento de recepción creado");
	        
	        // - Se guarda el codigo de tarea del calendario:
	        controlFicheroLevantamiento.setCodTarea(BigDecimal.valueOf(codTarea));
	        	        
	        controlFicheroLevantamiento.setUsuarioUltModificacion(EmbargosConstants.USER_AUTOMATICO);
	        controlFicheroLevantamiento.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
			fileControlRepository.save(controlFicheroLevantamiento);
        }
        catch (Exception e)
        {
            LOG.error("Error while treating AEAT LEV file", e);
            // TODO error treatment
            
            // - Se envia correo del error del parseo del fichero de levantamiento
	        emailService.sendEmailFileParserError(levFileName, e.getMessage()); 
	        throw e;
        }
        finally
        {
            if(reader!=null) {
            	reader.close();
            }
            
        	if (beanReader != null)
                beanReader.close();
        	
        	if (fileInputStream!=null) fileInputStream.close();
        }
        
        LOG.info("AEATLiftingServiceImpl - tratarFicheroLevantamientos - end");
    }
}
