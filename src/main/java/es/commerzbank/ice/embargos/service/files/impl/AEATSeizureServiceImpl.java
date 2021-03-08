package es.commerzbank.ice.embargos.service.files.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.commerzbank.ice.embargos.utils.EmbargosUtils;
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
import es.commerzbank.ice.comun.lib.service.FestiveService;
import es.commerzbank.ice.comun.lib.service.TaskService;
import es.commerzbank.ice.comun.lib.typeutils.DateUtils;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.datawarehouse.domain.dto.AccountDTO;
import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.CuentaEmbargo;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora;
import es.commerzbank.ice.embargos.domain.entity.EntidadesOrdenante;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlfichero;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.embargos.domain.mapper.AEATMapper;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.domain.mapper.SeizedBankAccountMapper;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.DiligenciaFase3;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.EntidadCreditoFase3;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.EntidadTransmisoraFase3;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.OrderingEntityRepository;
import es.commerzbank.ice.embargos.repository.SeizedBankAccountRepository;
import es.commerzbank.ice.embargos.repository.SeizedRepository;
import es.commerzbank.ice.embargos.repository.SeizureBankAccountRepository;
import es.commerzbank.ice.embargos.repository.SeizureRepository;
import es.commerzbank.ice.embargos.service.CustomerService;
import es.commerzbank.ice.embargos.service.EmailService;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.files.AEATSeizureService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.ICEDateUtils;

@Service
public class AEATSeizureServiceImpl implements AEATSeizureService{

	private static final Logger logger = LoggerFactory.getLogger(AEATSeizureServiceImpl.class);
	
	@Value("${commerzbank.embargos.beanio.config-path.aeat}")
	String pathFileConfigAEAT;
			
	@Autowired
	private AEATMapper aeatMapper;
	
	@Autowired
	private FestiveService festiveService;
	
	@Autowired
	private FileControlMapper fileControlMapper;
	
	@Autowired
	private SeizedBankAccountMapper seizedBankAccountMapper;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private TaskService taskService;

	@Autowired
	private FileControlService fileControlService;
	
	@Autowired
	private FileControlRepository fileControlRepository;
	
	@Autowired
	private OrderingEntityRepository orderingEntityRepository;
	
	@Autowired
	private SeizureRepository seizureRepository;
	
	@Autowired
	private SeizedRepository seizedRepository;
	
	@Autowired
	private SeizureBankAccountRepository seizureBankAccountRepository;
	
	@Autowired
	private SeizedBankAccountRepository seizedBankAccountRepository;
	
	@Autowired
	private EmailService emailService;

	//@Autowired
	//private GeneralParametersService generalParametersService;
	
	@Autowired
	private EventService eventService;
	
	@Override
	@Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
	public void tratarFicheroDiligenciasEmbargo(File processingFile, String originalName, File processedFile) throws IOException, ICEException {
		
		logger.info("AEATSeizureServiceImpl - tratarFicheroDiligenciasEmbargo - start");
		
		BeanReader beanReader = null;
		Reader reader = null;
		
		ControlFichero controlFicheroEmbargo = null;
		
		String seizureFileName = null;
		
		try {
			
			seizureFileName = FilenameUtils.getName(processingFile.getCanonicalPath());
		
	        // create a StreamFactory
	        StreamFactory factory = StreamFactory.newInstance();
	        // load the mapping file
	        factory.loadResource(pathFileConfigAEAT);
	        
	        //Se guarda el registro de ControlFichero del fichero de entrada:
	        controlFicheroEmbargo = 
	        		fileControlMapper.generateControlFichero(processingFile, EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_AEAT, originalName, processedFile);
	        
	        fileControlService.saveFileControlTransaction(controlFicheroEmbargo);
	
	        
	        // use a StreamFactory to create a BeanReader
	        //String encoding = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_ENCODING_AEAT);
			
	        reader = new InputStreamReader(new FileInputStream(processingFile)); 
	        beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_AEAT_DILIGENCIAS, reader);
	        
	        Object record = null;
	        boolean isEntidadTransmisoraCommerzbank = false;
	        
	        EntidadTransmisoraFase3 entidadTransmisoraFase3 = null;
	        EntidadCreditoFase3 entidadCreditoFase3 = null;
	        DiligenciaFase3 diligenciaFase3 = null;
	        
	        EntidadesOrdenante entidadOrdenante = null;
	        //Date fechaObtencionFicheroOrganismo = null;

	        while ((record = beanReader.read()) != null) {
	        	
	        	if(EmbargosConstants.RECORD_NAME_AEAT_ENTIDADTRANSMISORA.equals(beanReader.getRecordName())) {      	
	        		
	        		entidadTransmisoraFase3 = (EntidadTransmisoraFase3) record;
	        		
	        		//Si la entidad transmisora es Commerzbank:
	        		if (entidadTransmisoraFase3.getCodigoEntidadTransmisora() != null 
	        				&& entidadTransmisoraFase3.getCodigoEntidadTransmisora().equals(EmbargosConstants.CODIGO_NRBE_COMMERZBANK)) {   			
	        			
	        			isEntidadTransmisoraCommerzbank = true;
	        			
	        		} else {	
	        			isEntidadTransmisoraCommerzbank = false;
	        		}
	        	}
	        	
	        	//Tratar registros solo si la entidad transmisora es Commerzbank:
	        	if (isEntidadTransmisoraCommerzbank) {
	        	
		        	if(EmbargosConstants.RECORD_NAME_AEAT_ENTIDADCREDITO.equals(beanReader.getRecordName())) {
		            	
		        		entidadCreditoFase3 = (EntidadCreditoFase3) record;
		        		
		        		String identificadorEntidad = entidadCreditoFase3.getDelegacionAgenciaEmisora()!=null ? entidadCreditoFase3.getDelegacionAgenciaEmisora() : null;
		        		
		        		entidadOrdenante = orderingEntityRepository.findByIdentificadorEntidad(identificadorEntidad);
		        		
		        		if (entidadOrdenante == null) {
		        			throw new ICEException("No se puede procesar el fichero '" + processingFile.getName() +
		        					"': Entidad Ordenante con identificadorEntidad " + identificadorEntidad + " no encontrada.");
		        		}
		        	}
		        	
		        	if(EmbargosConstants.RECORD_NAME_AEAT_DILIGENCIA.equals(beanReader.getRecordName())) {
		        		
		        		diligenciaFase3 = (DiligenciaFase3) record;
		        		diligenciaFase3 = revisarCuentaVacias(diligenciaFase3);
		        		
		        		Embargo embargo = null;
		        		Traba traba = null;
		        		
		        		//- Se obtienen de Datawarehouse los datos del cliente y sus cuentas a partir del NIF del cliente:
		        		CustomerDTO customerDTO = customerService.findCustomerByNif(diligenciaFase3.getNifDeudor(), true);
		        		
		        		//- Se almacenan las cuentas obtenidas de Datawarehouse en una Hash donde la key es el IBAN:
		        		Map<String, AccountDTO> customerAccountsMap = new HashMap<>(); 
		        		if (customerDTO!=null) {
			        		
		        			for (AccountDTO accountDTO : customerDTO.getBankAccounts()) {    		
			        			customerAccountsMap.put(accountDTO.getIban(), accountDTO);
			        		}
		        		}

						String razonSocialInterna = EmbargosUtils.determineRazonSocialInternaFromCustomer(customerDTO);
		        		
		        		//Generacion de las instancias de Embargo y de Traba:
		        		embargo = aeatMapper.generateEmbargo(diligenciaFase3, controlFicheroEmbargo.getCodControlFichero(), entidadOrdenante, razonSocialInterna, entidadCreditoFase3, customerAccountsMap);
		        			
		        		traba =  aeatMapper.generateTraba(diligenciaFase3, controlFicheroEmbargo.getCodControlFichero(), entidadOrdenante, customerAccountsMap);        			
		        		traba.setEmbargo(embargo);
	
	       			        		
		        		//Guardar los datos del embargo y traba:
		        		seizureRepository.save(embargo);
		        		
		        		seizedRepository.save(traba);
		        		
		        		
		        		//- Se guardan las cuentas obtenidas del fichero de embargo en CUENTA_EMBARGO:
		        		for (CuentaEmbargo cuentaEmbargo : embargo.getCuentaEmbargos()) {
		        			
		        			//TODO mirar si se tiene que comprobar si es nulo el Iban o bien la "cuenta":
		        			if (cuentaEmbargo.getCuenta()!=null && !cuentaEmbargo.getCuenta().isEmpty()) {
		        						        				
		        				seizureBankAccountRepository.save(cuentaEmbargo);
		        			}
		        		}
		        		
		        		//Para almacenar los numeros de cuenta del cliente que vienen en el fichero de embargos:
		        		List<String> accountNumClienteFicheroEmbargoList = new ArrayList<>();
		        		BigDecimal numeroOrdenCuenta = new BigDecimal(0);
		        		
		        		//- Se guardan las cuentas obtenidas del fichero de embargo en CUENTA_TRABA:
		        		for (CuentaTraba cuentaTraba : traba.getCuentaTrabas()) {
		        			
		        			//TODO mirar si se tiene que comprobar si es nulo el Iban o bien la "cuenta":
		        			if (cuentaTraba.getIban()!=null && !cuentaTraba.getIban().isEmpty()) {

		        				accountNumClienteFicheroEmbargoList.add(cuentaTraba.getCuenta());
		        				
		        				numeroOrdenCuenta = (cuentaTraba.getNumeroOrdenCuenta()!=null && cuentaTraba.getNumeroOrdenCuenta().compareTo(numeroOrdenCuenta) > 0) ?
		        						cuentaTraba.getNumeroOrdenCuenta() : numeroOrdenCuenta;
		        				
		        				seizedBankAccountRepository.save(cuentaTraba);
		        			}
		        		}
		        	
		        		
		        		
		        		//- Se guardan en CUENTA_TRABA las cuentas obtenidas de Datawarehouse que no se encuentren en el fichero de Embargos:
		        		if(customerDTO!=null) {
		        			
		        			for (AccountDTO accountDTO : customerDTO.getBankAccounts()) {
			
		        				//Se guarda la cuenta si no se encuentra en el fichero de embargos:
		        				if(!accountNumClienteFicheroEmbargoList.contains(accountDTO.getAccountNum())) {
		        					
		        					numeroOrdenCuenta = numeroOrdenCuenta.add(BigDecimal.valueOf(1));
		        					
		        					CuentaTraba cuentaTrabaExtra = seizedBankAccountMapper.accountDTOToCuentaTraba(accountDTO, numeroOrdenCuenta, traba);
		        					seizedBankAccountRepository.save(cuentaTrabaExtra);
		        				}
		        			}
		        		}
		        	}       	
	        	}
	        }
	        
	        
	        //Datos a guardar en ControlFichero una vez procesado el fichero:
	        
			//- Se guarda el codigo de la Entidad comunicadora en ControlFichero:
	        EntidadesComunicadora entidadComunicadora = entidadOrdenante.getEntidadesComunicadora();
	        controlFicheroEmbargo.setEntidadesComunicadora(entidadOrdenante.getEntidadesComunicadora());
			
			//- Fechas de creacion y de comienzo de ciclo:
			Date fechaCreacionFicheroTransmision = entidadTransmisoraFase3.getFechaCreacionFicheroTransmision();
	        BigDecimal fechaCreacionFicheroTransmisionBigDec = fechaCreacionFicheroTransmision!=null ? ICEDateUtils.dateToBigDecimal(fechaCreacionFicheroTransmision, ICEDateUtils.FORMAT_yyyyMMdd) : null;
			controlFicheroEmbargo.setFechaCreacion(fechaCreacionFicheroTransmisionBigDec);
			
			Date fechaInicioCiclo = entidadTransmisoraFase3.getFechaInicioCiclo();
			BigDecimal fechaInicioCicloBigDec = fechaInicioCiclo!=null ? ICEDateUtils.dateToBigDecimal(fechaInicioCiclo, ICEDateUtils.FORMAT_yyyyMMdd) : null;
			controlFicheroEmbargo.setFechaComienzoCiclo(fechaInicioCicloBigDec);
	       
			//- Se guarda la fecha maxima de respuesta (now + dias de margen)
			int diasRespuestaF3 = entidadComunicadora.getDiasRespuestaF3()!=null ? entidadComunicadora.getDiasRespuestaF3().intValue() : 0;
			FestiveService.ValueDateCalculationParameters parameters = new FestiveService.ValueDateCalculationParameters();
			parameters.numBusinessDays = diasRespuestaF3;
			parameters.location = 1L;
			parameters.fromDate = LocalDate.now();
			LocalDate finalDate = festiveService.dateCalculation(parameters);
			Date lastDateResponse = Date.from(finalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			
			//Date lastDateResponse = DateUtils.convertToDate(LocalDate.now().plusDays(diasRespuestaF3));
			BigDecimal limitResponseDate = ICEDateUtils.dateToBigDecimal(lastDateResponse, ICEDateUtils.FORMAT_yyyyMMdd);
			controlFicheroEmbargo.setFechaMaximaRespuesta(limitResponseDate);
			
			//Cambio de estado de CtrlFichero a: RECIBIDO
	        EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero(
	        		EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_RECEIVED,
	        		EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_AEAT);
	        controlFicheroEmbargo.setEstadoCtrlfichero(estadoCtrlfichero);
			
	        if (entidadCreditoFase3!=null && entidadCreditoFase3.getNumeroEnvio()!=null) controlFicheroEmbargo.setNumEnvio(entidadCreditoFase3.getNumeroEnvio().toString());
	        
	        //CALENDARIO:
	        // - Se agrega la tarea al calendario:
	        TaskAndEvent task = new TaskAndEvent();
	        task.setDescription("Embargo " + controlFicheroEmbargo.getNombreFichero());
	        task.setDate(ICEDateUtils.bigDecimalToDate(controlFicheroEmbargo.getFechaMaximaRespuesta(), ICEDateUtils.FORMAT_yyyyMMdd));
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
	        event.setDescription("Embargo recibido " + controlFicheroEmbargo.getNombreFichero());
	        event.setDate(DateUtils.convertToDate(LocalDate.now()));
	        event.setCodCalendar(1L);
	        event.setType("E");
	        event.setAction("0");
	        event.setIndActive(true);
	        event.setIndVisualizarCalendario(true);
	        event.setApplication(EmbargosConstants.ID_APLICACION_EMBARGOS);
            eventService.createOrUpdateEvent(event, EmbargosConstants.USER_AUTOMATICO);
	        logger.info("Evento de recepción creado");
            
	        // - Se guarda el codigo de tarea del calendario:
	        controlFicheroEmbargo.setCodTarea(BigDecimal.valueOf(codTarea));
	        	        
	        controlFicheroEmbargo.setUsuarioUltModificacion(EmbargosConstants.USER_AUTOMATICO);
	        controlFicheroEmbargo.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
			fileControlRepository.save(controlFicheroEmbargo);


		} catch (Exception e) {

			logger.error("Error in aeat sizure service", e);

			//TODO Estado de ERROR pendiente de ser eliminado, se comenta:
			/*
			//Transaccion para cambiar el estado de ControlFichero a ERROR:
			fileControlService.updateFileControlStatusTransaction(controlFicheroEmbargo, 
					EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_ERROR);
			*/
			
			// - Se envia correo del error del parseo del fichero de embargo:
			emailService.sendEmailFileParserError(seizureFileName, e.getMessage());
			
			throw e;
			
		} finally {
			
			if(reader!=null) {
				reader.close();
			}
			if (beanReader!=null) {
				beanReader.close();
			}
		}
	
		logger.info("AEATSeizureServiceImpl - tratarFicheroDiligenciasEmbargo - end");
	}

	private DiligenciaFase3 revisarCuentaVacias(DiligenciaFase3 diligenciaFase3) {
		
		DiligenciaFase3 retorno = diligenciaFase3;
		
		if (retorno.getCodigoCuentaCliente1()!=null && retorno.getCodigoCuentaCliente1().length()>0) {
			try {
				if (Long.valueOf(retorno.getCodigoCuentaCliente1()).equals(Long.valueOf(0))) {
					retorno.setCodigoCuentaCliente1(null);
				}
			} catch (Exception e) {}
		}
		
		if (retorno.getCodigoCuentaCliente2()!=null && retorno.getCodigoCuentaCliente2().length()>0) {
			try {
				if (Long.valueOf(retorno.getCodigoCuentaCliente2()).equals(Long.valueOf(0))) {
					retorno.setCodigoCuentaCliente2(null);
				}
			} catch (Exception e) {}
		}
		
		if (retorno.getCodigoCuentaCliente3()!=null && retorno.getCodigoCuentaCliente3().length()>0) {
			try {
				if (Long.valueOf(retorno.getCodigoCuentaCliente3()).equals(Long.valueOf(0))) {
					retorno.setCodigoCuentaCliente3(null);
				}
			} catch (Exception e) {}
		}
		
		return retorno;
	}

}
