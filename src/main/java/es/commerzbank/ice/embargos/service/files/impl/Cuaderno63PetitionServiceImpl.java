package es.commerzbank.ice.embargos.service.files.impl;

import es.commerzbank.ice.comun.lib.domain.dto.Element;
import es.commerzbank.ice.comun.lib.domain.dto.TaskAndEvent;
import es.commerzbank.ice.comun.lib.service.EventService;
import es.commerzbank.ice.comun.lib.service.FestiveService;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.service.TaskService;
import es.commerzbank.ice.comun.lib.typeutils.DateUtils;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.comun.lib.util.ValueConstants;
import es.commerzbank.ice.datawarehouse.domain.dto.AccountDTO;
import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.domain.entity.*;
import es.commerzbank.ice.embargos.domain.mapper.Cuaderno63Mapper;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.domain.mapper.InformationPetitionBankAccountMapper;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.CabeceraEmisorFase1;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.SolicitudInformacionFase1;
import es.commerzbank.ice.embargos.repository.CommunicatingEntityRepository;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.InformationPetitionBankAccountRepository;
import es.commerzbank.ice.embargos.repository.InformationPetitionRepository;
import es.commerzbank.ice.embargos.service.CustomerService;
import es.commerzbank.ice.embargos.service.EmailService;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.files.Cuaderno63PetitionService;
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

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
public class Cuaderno63PetitionServiceImpl implements Cuaderno63PetitionService{

	private static final Logger LOG = LoggerFactory.getLogger(Cuaderno63PetitionServiceImpl.class);
	
	@Value("${commerzbank.embargos.beanio.config-path.cuaderno63}")
	String pathFileConfigCuaderno63;

    @Autowired
	private EventService eventService;
    
    @Autowired
    private FestiveService festiveService;
    
	@Autowired
	private Cuaderno63Mapper cuaderno63Mapper;

	@Autowired
	private FileControlMapper fileControlMapper;

	@Autowired
	private InformationPetitionBankAccountMapper informationPetitionBankAccountMapper;

	@Autowired
	private FileControlService fileControlService;
	
	@Autowired
	private CustomerService customerService;

	@Autowired
	private TaskService taskService;

	//Agregar repositories de DWH ...
	@Autowired
	private FileControlRepository fileControlRepository;

	@Autowired
	private InformationPetitionRepository informationPetitionRepository;

	@Autowired
	private InformationPetitionBankAccountRepository informationPetitionBankAccountRepository;

	@Autowired
	private CommunicatingEntityRepository communicatingEntityRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private GeneralParametersService generalParametersService;
	
	@Override
	@Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
	public void cargarFicheroPeticion(File processingFile, String originalName, File processedFile) throws IOException, ICEException {

		BeanReader beanReader = null;
		Reader reader = null;

		FileInputStream fileInputStream = null;
		
		ControlFichero controlFicheroPeticion = null;

		String petitionFileName = null;
		
		try {
			
	        petitionFileName = FilenameUtils.getName(processingFile.getCanonicalPath());

			// create a StreamFactory
	        StreamFactory factory = StreamFactory.newInstance();
	        // load the mapping file
	        factory.loadResource(pathFileConfigCuaderno63);
	        
	        //Se guarda el registro de ControlFichero del fichero de entrada:
	        controlFicheroPeticion = 
	        		fileControlMapper.generateControlFichero(processingFile, EmbargosConstants.COD_TIPO_FICHERO_PETICION_INFORMACION_NORMA63, originalName, processedFile);
	        
	        //fileControlRepository.save(controlFicheroPeticion);
	        fileControlService.saveFileControlTransaction(controlFicheroPeticion);
	        
	        // use a StreamFactory to create a BeanReader

	        String encoding = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_ENCODING_NORMA63);
	        
	        fileInputStream = new FileInputStream(processingFile);
			reader = new InputStreamReader(fileInputStream, encoding);
	        beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_CUADERNO63_FASE1, reader);

	        CabeceraEmisorFase1 cabeceraEmisor = null;
	        EntidadesComunicadora entidadComunicadora = null;
	        Date fechaObtencionFicheroOrganismo = null;
	        
	        HashSet<String> listaNifs = customerService.findCustomerNifs();

	        Object record = null;
	        while ((record = beanReader.read()) != null) {
	        
	        	if(EmbargosConstants.RECORD_NAME_SOLICITUDINFORMACION.equals(beanReader.getRecordName())) {

	        		//Registro de detalle:

	        		SolicitudInformacionFase1 solicitudInformacion = (SolicitudInformacionFase1) record;


	        		//Llamada a Datawarehouse: Obtener las cuentas del cliente a partir del nif:
	        		if (listaNifs == null || !listaNifs.contains(solicitudInformacion.getNifDeudor())) {
	        			// ni siquiera en el log;
					}
	        		else {
						LOG.info("Fase1 - incorporando cliente NIF "+ solicitudInformacion.getNifDeudor());

		        		CustomerDTO customerDTO = customerService.findCustomerByNif(solicitudInformacion.getNifDeudor(), false);

		        		// Si existe el cliente:
		        		if (customerDTO == null) {
							LOG.info("Fase1 - No se ha encontrado el cliente prefiltrado "+ solicitudInformacion.getNifDeudor());
						}
		        		else {
			        		List<AccountDTO> accountList = customerDTO.getBankAccounts();

			        		//Tratar solamente los clientes en los que se han encontrado cuentas:
							if (accountList == null || accountList.isEmpty()) {
								LOG.info("Fase1 - No se ha encontrado cuentas para el cliente "+ solicitudInformacion.getNifDeudor());
							}
			        		else {
								String razonSocialInterna = EmbargosUtils.determineRazonSocialInternaFromCustomer(customerDTO);

				        		//Se guarda la PeticionInformacion en bbdd:
				        		PeticionInformacion peticionInformacion = cuaderno63Mapper.generatePeticionInformacion(solicitudInformacion,
				        				controlFicheroPeticion.getCodControlFichero(), accountList, entidadComunicadora, razonSocialInterna);

				        		informationPetitionRepository.save(peticionInformacion);

			        			//Se guardan todas las cuentas del nif en la tabla PETICION_INFORMACION_CUENTAS:
								guardarPeticionCuentas(peticionInformacion, accountList);
			        		}
		        		}
	        		}
	        		        	
	        	} else if(EmbargosConstants.RECORD_NAME_CABECERAEMISOR.equals(beanReader.getRecordName())) {
	        		
	        		//Registro cabecera de fichero:
	        		
	        		cabeceraEmisor = (CabeceraEmisorFase1) record;
	        		
	        		fechaObtencionFicheroOrganismo = cabeceraEmisor.getFechaObtencionFicheroOrganismo();
	        		
	        		String nifOrganismoEmisor = cabeceraEmisor.getNifOrganismoEmisor();
	        		
	        		entidadComunicadora = communicatingEntityRepository.findByNifEntidad(nifOrganismoEmisor);
	        		
	        		
	        		//Si entidadComunicadora es NULL -> Exception...
	        		if (entidadComunicadora == null) {
	        			throw new ICEException("No se puede procesar el fichero '" + processingFile.getName() +
	        					"': Entidad Comunicadora con NIF " + nifOrganismoEmisor + " no encontrada.");
	        		}
	        		
	        	} else if(EmbargosConstants.RECORD_NAME_FINFICHERO.equals(beanReader.getRecordName())) {
	        		
	        		//Registro fin de fichero:
	        		
	        		//Validar que sea correcta la info
	        	}
	        	
	        	LOG.debug(record.toString());
	        	        	
	        }
	        

	        
	        //Datos a guardar en ControlFichero una vez procesado el fichero:

			//- Se guarda el codigo de la Entidad comunicadora en ControlFichero:
			controlFicheroPeticion.setEntidadesComunicadora(entidadComunicadora);

			//- Se guarda la fecha de la cabecera en el campo fechaCreacion de ControlFichero:
			BigDecimal fechaObtencionFicheroOrganismoBigDec = fechaObtencionFicheroOrganismo != null ? ICEDateUtils.dateToBigDecimal(fechaObtencionFicheroOrganismo, ICEDateUtils.FORMAT_yyyyMMdd) : null;
			controlFicheroPeticion.setFechaCreacion(fechaObtencionFicheroOrganismoBigDec);
			controlFicheroPeticion.setFechaComienzoCiclo(fechaObtencionFicheroOrganismoBigDec);

			//- Se guarda la fecha maxima de respuesta (now + dias de margen)
			int diasRespuestaF1 = entidadComunicadora.getDiasRespuestaF1() != null ? entidadComunicadora.getDiasRespuestaF1().intValue() : 0;
			FestiveService.ValueDateCalculationParameters parameters = new FestiveService.ValueDateCalculationParameters();
			parameters.numDaysToAdd = diasRespuestaF1;
			parameters.location = 1L;
			parameters.fromDate = LocalDate.now();
			parameters.calculationType = FestiveService.CalculationType.FIRST_WORKING_DAY;
			LocalDate finalDate = festiveService.dateCalculation(parameters, ValueConstants.COD_LOCALIDAD_MADRID);
			Date lastDateResponse = Date.from(finalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

			BigDecimal limitResponseDate = ICEDateUtils.dateToBigDecimal(lastDateResponse, ICEDateUtils.FORMAT_yyyyMMdd);
			controlFicheroPeticion.setFechaMaximaRespuesta(limitResponseDate);

			//Cambio de estado de CtrlFichero a: RECIBIDO
	        EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero(
	        		EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_INFORMACION_NORMA63_RECEIVED,
	        		EmbargosConstants.COD_TIPO_FICHERO_PETICION_INFORMACION_NORMA63);
	        controlFicheroPeticion.setEstadoCtrlfichero(estadoCtrlfichero);
			
	        //CALENDARIO:
	        // - Se agrega la tarea al calendario:
	        TaskAndEvent task = new TaskAndEvent();
	        task.setDescription("Petición de información " + controlFicheroPeticion.getNombreFichero());
	        task.setDate(ICEDateUtils.bigDecimalToDate(controlFicheroPeticion.getFechaMaximaRespuesta(), ICEDateUtils.FORMAT_yyyyMMdd));
	        task.setCodCalendar(1L);
	        task.setType("T");
	        Element office = new Element();
	        office.setCode(1L);
	        task.setOffice(office);
	        task.setAction("0");
	        task.setStatus("P");
	        task.setIndActive(true);
	        task.setApplication(EmbargosConstants.ID_APLICACION_EMBARGOS);
	        Long codTarea = taskService.addCalendarTask(task);
	        
	        // Se crea el evento
	        TaskAndEvent event = new TaskAndEvent();
	        event.setDescription("Petición de información recibido " + controlFicheroPeticion.getNombreFichero());
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
	        controlFicheroPeticion.setCodTarea(BigDecimal.valueOf(codTarea));
	        
	        controlFicheroPeticion.setUsuarioUltModificacion(EmbargosConstants.USER_AUTOMATICO);
	        controlFicheroPeticion.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
			fileControlRepository.save(controlFicheroPeticion);

	        // - Se envia correo de la recepcion del fichero
	        emailService.sendEmailPetitionReceived(controlFicheroPeticion);
	        
		} catch (Exception e) {
			LOG.error("Error in Cuaderno63 Petition Service", e);
			//TODO Estado de ERROR pendiente de ser eliminado, se comenta:
			/*
			//Transaccion para cambiar el estado de ControlFichero a ERROR:
			fileControlService.updateFileControlStatusTransaction(controlFicheroPeticion, 
					EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_INFORMACION_NORMA63_ERROR);			
			*/

			// - Se envia correo del error del parseo del fichero de peticiones:
			emailService.sendEmailFileParserError(controlFicheroPeticion, e.getMessage());
			
			throw e;

		} finally {
			if (reader != null)
				reader.close();
			if (beanReader != null)
				beanReader.close();
			if (fileInputStream != null)
				fileInputStream.close();
		}

	}

	private void guardarPeticionCuentas(PeticionInformacion peticionInformacion, List<AccountDTO> accountList)
	{
		// se preserva la precedencia de Cuaderno63Mapper.setPreloadedBankAccounts
		ArrayList<AccountDTO> accountWorkingCopy = new ArrayList(accountList);
		int maxOrden = Integer.MIN_VALUE;
		// Se recupera el número de orden preasignado
		for(AccountDTO accountDTO : accountList) {

			Integer orden = null;
			if (accountDTO.getAccountNum().equals(peticionInformacion.getCuenta1()))
				orden = 1;
			else if (accountDTO.getAccountNum().equals(peticionInformacion.getCuenta2()))
				orden = 2;
			else if (accountDTO.getAccountNum().equals(peticionInformacion.getCuenta3()))
				orden = 3;
			else if (accountDTO.getAccountNum().equals(peticionInformacion.getCuenta4()))
				orden = 4;
			else if (accountDTO.getAccountNum().equals(peticionInformacion.getCuenta5()))
				orden = 5;
			else if (accountDTO.getAccountNum().equals(peticionInformacion.getCuenta6()))
				orden = 6;

			if (orden != null) {
				PeticionInformacionCuenta peticionInformacionCuenta =
						informationPetitionBankAccountMapper.toPeticionInformacionCuenta(accountDTO,
								peticionInformacion.getCodPeticion(), orden);
				informationPetitionBankAccountRepository.save(peticionInformacionCuenta);
				accountWorkingCopy.remove(accountDTO);
				maxOrden = Integer.max(orden, maxOrden);
			}
		}
		// Cuentas sin preasignar
		for(AccountDTO accountDTO : accountWorkingCopy) {
			maxOrden = maxOrden + 1;
			PeticionInformacionCuenta peticionInformacionCuenta =
					informationPetitionBankAccountMapper.toPeticionInformacionCuenta(accountDTO,
							peticionInformacion.getCodPeticion(), maxOrden);
			informationPetitionBankAccountRepository.save(peticionInformacionCuenta);
		}
	}
}
