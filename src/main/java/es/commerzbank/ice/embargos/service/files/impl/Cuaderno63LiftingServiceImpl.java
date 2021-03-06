package es.commerzbank.ice.embargos.service.files.impl;

import es.commerzbank.ice.comun.lib.domain.dto.TaskAndEvent;
import es.commerzbank.ice.comun.lib.service.EventService;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.service.TaskService;
import es.commerzbank.ice.comun.lib.typeutils.DateUtils;
import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.domain.entity.*;
import es.commerzbank.ice.embargos.domain.mapper.Cuaderno63Mapper;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.formats.cuaderno63.common.Levantamiento;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase5.CabeceraEmisorFase5;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase5.OrdenLevantamientoRetencionFase5;
import es.commerzbank.ice.embargos.repository.*;
import es.commerzbank.ice.embargos.service.AccountingService;
import es.commerzbank.ice.embargos.service.CustomerService;
import es.commerzbank.ice.embargos.service.EmailService;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.files.Cuaderno63LiftingService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.EmbargosUtils;
import es.commerzbank.ice.embargos.utils.ICEDateUtils;
import org.apache.commons.io.FilenameUtils;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/*
TODO:
 FileControlMapper.determineInitialEstadoCtrlFicheroByCodTipoFichero -> afegir constants.
 Cargar: Raz??n Social interna
 */
@Service
public class Cuaderno63LiftingServiceImpl
    implements Cuaderno63LiftingService
{

    private static final Logger LOG = LoggerFactory.getLogger(Cuaderno63LiftingServiceImpl.class);

    @Value("${commerzbank.embargos.beanio.config-path.cuaderno63}")
    String pathFileConfigCuaderno63;

    @Autowired
	private EventService eventService;
    
    @Autowired
    private FileControlMapper fileControlMapper;
    
    @Autowired
    private FileControlRepository fileControlRepository;
    
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
    private Cuaderno63Mapper cuaderno63Mapper;
    
    @Autowired
    private GeneralParametersService generalParametersService;
    
    @Autowired
	private EmailService emailService;
    
    @Autowired
	private FileControlService fileControlService;
    
    @Autowired
	private OrderingEntityRepository orderingEntityRepository;
    
    @Autowired
	private TaskService taskService;

    @Autowired
    private AccountingService accountingService;
    
    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void tratarFicheroLevantamientos(File processingFile, String originalName, File processedFile)
    		throws Exception
    {
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
            factory.loadResource(pathFileConfigCuaderno63);
            
            // Inicializar control fichero
            controlFicheroLevantamiento =
                    fileControlMapper.generateControlFichero(processingFile, EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_NORMA63, originalName, processedFile);

            fileControlService.saveFileControlTransaction(controlFicheroLevantamiento);
            
	        String encoding = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_ENCODING_NORMA63);
			
	        fileInputStream = new FileInputStream(processingFile);
	        reader = new InputStreamReader(fileInputStream, encoding);
            beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_CUADERNO63_FASE5, reader);

            Object currentRecord = null;

            BigDecimal importeMaximoAutomaticoDivisa =
                    generalParametersService.loadBigDecimalParameter(EmbargosConstants.PARAMETRO_EMBARGOS_LEVANTAMIENTO_IMPORTE_MAXIMO_AUTOMATICO_DIVISA);
            
            CabeceraEmisorFase5 cabeceraEmisor = null;
	    	EntidadesOrdenante entidadOrdenante = null;
	    	Date fechaObtencionFicheroOrganismo = null;

            boolean puedeSerContabilizado = true;
            boolean tieneAlgoAContabilizar = false;

            while ((currentRecord = beanReader.read()) != null) {
                if (EmbargosConstants.RECORD_NAME_CABECERAEMISOR.equals(beanReader.getRecordName())) {
                	cabeceraEmisor = (CabeceraEmisorFase5) currentRecord;
	        		
	        		String nifOrganismoEmisor = cabeceraEmisor.getNifOrganismoEmisor();
	        		
	        		entidadOrdenante = orderingEntityRepository.findByNifEntidad(nifOrganismoEmisor);
	        		
	        		fechaObtencionFicheroOrganismo = cabeceraEmisor.getFechaObtencionFicheroOrganismo();
                }
                else if (EmbargosConstants.RECORD_NAME_ORDENLEVANTAMIENTORETENCION.equals(beanReader.getRecordName()))
                {
                    OrdenLevantamientoRetencionFase5 ordenLevantamientoRetencionFase5 = (OrdenLevantamientoRetencionFase5) currentRecord;

                    Embargo embargo = EmbargosUtils.selectEmbargo(seizureRepository, entidadOrdenante, new Levantamiento(ordenLevantamientoRetencionFase5));

                    if (embargo == null)
                    {
                        LOG.error("No embargo found for "+ ordenLevantamientoRetencionFase5.getIdentificadorDeuda());
                        throw new Exception("No embargo found for "+ ordenLevantamientoRetencionFase5.getIdentificadorDeuda());
                    }

                    Traba traba = seizedRepository.getByEmbargo(embargo);

                    if (traba == null)
                    {
                        LOG.error("Traba not found for embargo "+ embargo.getCodEmbargo() +" code "+ ordenLevantamientoRetencionFase5.getIdentificadorDeuda());
                        throw new Exception("Traba not found for embargo "+ embargo.getCodEmbargo() +" code "+ ordenLevantamientoRetencionFase5.getIdentificadorDeuda());
                    }

                    LOG.info("Using traba "+ traba.getCodTraba() +" for embargo "+ embargo.getCodEmbargo() +" code "+ ordenLevantamientoRetencionFase5.getIdentificadorDeuda());

                    // recuperar account <- razon interna
                    // recuperar cod traba
                    // estado contable?
                    // estado ejecutado?
                    CustomerDTO customerDTO = customerService.findCustomerByNif(ordenLevantamientoRetencionFase5.getNifDeudor(), true);

                    LevantamientoTraba levantamiento = cuaderno63Mapper.generateLevantamiento(controlFicheroLevantamiento.getCodControlFichero(), ordenLevantamientoRetencionFase5, traba, customerDTO);

                    liftingRepository.save(levantamiento);

                    for (CuentaLevantamiento cuentaLevantamiento : levantamiento.getCuentaLevantamientos())
                    {

                        cuentaLevantamiento.setUsuarioUltModificacion(EmbargosConstants.USER_AUTOMATICO);
                        cuentaLevantamiento.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
                        liftingBankAccountRepository.save(cuentaLevantamiento);

                        if (cuentaLevantamiento.getCuenta().endsWith(EmbargosConstants.ISO_MONEDA_EUR)) {
                            tieneAlgoAContabilizar = true;
                        }
                        else {
                            // Si el contravalor en euros supera el l??mite..
                            if (importeMaximoAutomaticoDivisa.compareTo(cuentaLevantamiento.getImporte()) <= 0) {
                                LOG.info("El contravalor en euros del levantamiento "+ cuentaLevantamiento.getCodCuentaLevantamiento() +" supera el l??mite permitido para contabilizar autom??ticamente.");
                                puedeSerContabilizado = false;
                            }
                            else {
                                tieneAlgoAContabilizar = true;
                            }
                        }
                    }
                }
            }

            // Actualizar control fichero

            //- Se guarda el codigo de la Entidad comunicadora en ControlFichero:
			//EntidadesComunicadora entidadComunicadora = entidadOrdenante.getEntidadesComunicadora();
			controlFicheroLevantamiento.setEntidadesComunicadora(entidadOrdenante.getEntidadesComunicadora());

			//- Se guarda la fecha de la cabecera en el campo fechaCreacion de ControlFichero:
			BigDecimal fechaObtencionFicheroOrganismoBigDec = fechaObtencionFicheroOrganismo != null ? ICEDateUtils.dateToBigDecimal(fechaObtencionFicheroOrganismo, ICEDateUtils.FORMAT_yyyyMMdd) : null;
			controlFicheroLevantamiento.setFechaCreacion(fechaObtencionFicheroOrganismoBigDec);
			controlFicheroLevantamiento.setFechaComienzoCiclo(fechaObtencionFicheroOrganismoBigDec);
			
            EstadoCtrlfichero estadoCtrlfichero = null;

            if (puedeSerContabilizado && tieneAlgoAContabilizar) {
                estadoCtrlfichero = new EstadoCtrlfichero(
                        EmbargosConstants.COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_PENDING_AUTOMATIC_ACCOUNTING,
                        EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_NORMA63);
            } else {
                estadoCtrlfichero = new EstadoCtrlfichero(
                        EmbargosConstants.COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_RECEIVED,
                        EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_NORMA63);
            }

            controlFicheroLevantamiento.setEstadoCtrlfichero(estadoCtrlfichero);

            fileControlRepository.saveAndFlush(controlFicheroLevantamiento);
            
            //CALENDARIO:
	        // - Se agrega la tarea al calendario:
	        TaskAndEvent task = new TaskAndEvent();
	        task.setDescription("Levantamiento " + controlFicheroLevantamiento.getNombreFichero());
	        task.setDate(DateUtils.convertToDate(LocalDate.now()));
	        task.setCodCalendar(1L);
	        task.setType("T");
	        task.setAction("0");
	        task.setStatus("P");
	        task.setIndActive(true);
	        task.setApplication(EmbargosConstants.ID_APLICACION_EMBARGOS);
	        task.setExternalId("LEV_"+ controlFicheroLevantamiento.getCodControlFichero());
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
	        LOG.info("Evento de recepci??n creado");
	        
	        // - Se guarda el codigo de tarea del calendario:
	        controlFicheroLevantamiento.setCodTarea(BigDecimal.valueOf(codTarea));
	        
	        controlFicheroLevantamiento.setUsuarioUltModificacion(EmbargosConstants.USER_AUTOMATICO);
            controlFicheroLevantamiento.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
            fileControlRepository.save(controlFicheroLevantamiento);
            
            // - Se envia correo de la recepcion del fichero
	        emailService.sendEmailPetitionReceived(controlFicheroLevantamiento);
        }
        catch (Exception e)
        {
            LOG.error("Error while treating NORMA63 LEV file", e);
            // TODO error treatment
            
	        // - Se envia correo del error del parseo del fichero de embargo:
	        emailService.sendEmailFileParserError(controlFicheroLevantamiento, e.getMessage());
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
    }
}
