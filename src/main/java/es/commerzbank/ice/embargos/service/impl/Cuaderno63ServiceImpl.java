package es.commerzbank.ice.embargos.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.beanio.BeanReader;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.comun.lib.domain.dto.Element;
import es.commerzbank.ice.comun.lib.domain.dto.TaskAndEvent;
import es.commerzbank.ice.comun.lib.service.TaskService;
import es.commerzbank.ice.comun.lib.typeutils.DateUtils;
import es.commerzbank.ice.comun.lib.util.ICEParserException;
import es.commerzbank.ice.datawarehouse.domain.dto.AccountDTO;
import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.domain.dto.FileControlDTO;
import es.commerzbank.ice.embargos.domain.dto.FileControlStatusDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.CuentaEmbargo;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.CuentaTrabaActuacion;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora;
import es.commerzbank.ice.embargos.domain.entity.EntidadesOrdenante;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlfichero;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacion;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacionCuenta;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.embargos.domain.mapper.Cuaderno63Mapper;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.domain.mapper.InformationPetitionBankAccountMapper;
import es.commerzbank.ice.embargos.domain.mapper.SeizedBankAccountMapper;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.CabeceraEmisorFase1;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.FinFicheroFase1;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.SolicitudInformacionFase1;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase2.CabeceraEmisorFase2;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase2.FinFicheroFase2;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase2.RespuestaSolicitudInformacionFase2;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.CabeceraEmisorFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.FinFicheroFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.OrdenEjecucionEmbargoComplementarioFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.OrdenEjecucionEmbargoFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase4.CabeceraEmisorFase4;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase4.ComunicacionResultadoRetencionFase4;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase4.FinFicheroFase4;
import es.commerzbank.ice.embargos.repository.CommunicatingEntityRepository;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.InformationPetitionBankAccountRepository;
import es.commerzbank.ice.embargos.repository.InformationPetitionRepository;
import es.commerzbank.ice.embargos.repository.OrderingEntityRepository;
import es.commerzbank.ice.embargos.repository.PetitionRepository;
import es.commerzbank.ice.embargos.repository.SeizedBankAccountRepository;
import es.commerzbank.ice.embargos.repository.SeizedRepository;
import es.commerzbank.ice.embargos.repository.SeizureBankAccountRepository;
import es.commerzbank.ice.embargos.repository.SeizureRepository;
import es.commerzbank.ice.embargos.service.Cuaderno63Service;
import es.commerzbank.ice.embargos.service.CustomerService;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.SeizureService;
import es.commerzbank.ice.utils.EmbargosConstants;
import es.commerzbank.ice.utils.EmbargosUtils;
import es.commerzbank.ice.utils.ICEDateUtils;

@Service
@Transactional(transactionManager="transactionManager", propagation = Propagation.REQUIRES_NEW)
public class Cuaderno63ServiceImpl implements Cuaderno63Service{
	private static final Logger LOG = LoggerFactory.getLogger(Cuaderno63ServiceImpl.class);

	@Value("${commerzbank.embargos.beanio.config-path.cuaderno63}")
	String pathFileConfigCuaderno63;

	@Value("${commerzbank.embargos.files.path.monitoring}")
	private String pathMonitoring;

	@Value("${commerzbank.embargos.files.path.processed}")
	private String pathProcessed;

	@Value("${commerzbank.embargos.files.path.generated}")
	private String pathGenerated;

	@Autowired
	Cuaderno63Mapper cuaderno63Mapper;

	@Autowired
	FileControlMapper fileControlMapper;

	@Autowired
	InformationPetitionBankAccountMapper informationPetitionBankAccountMapper;

	@Autowired
	SeizedBankAccountMapper seizedBankAccountMapper;

	@Autowired
	FileControlService fileControlService;
	
	@Autowired
	CustomerService customerService;

	@Autowired
	SeizureService seizureService;
	
	@Autowired
	TaskService taskService;

	//Agregar repositories de DWH ...
	@Autowired
	FileControlRepository fileControlRepository;

	@Autowired
	PetitionRepository petitionRepository;

	@Autowired
	InformationPetitionRepository informationPetitionRepository;

	@Autowired
	InformationPetitionBankAccountRepository informationPetitionBankAccountRepository;

	@Autowired
	SeizureRepository seizureRepository;

	@Autowired
	SeizedRepository seizedRepository;

	@Autowired
	SeizureBankAccountRepository seizureBankAccountRepository;

	@Autowired
	SeizedBankAccountRepository seizedBankAccountRepository;

	@Autowired
	OrderingEntityRepository orderingEntityRepository;

	@Autowired
	CommunicatingEntityRepository communicatingEntityRepository;

	@Override
	//Se comenta '@transactional' ya que se utilizara a nivel de clase:
	//@Transactional(transactionManager="transactionManager", propagation = Propagation.REQUIRES_NEW)
	public void cargarFicheroPeticion(File file) throws IOException, ICEParserException {

		BeanReader beanReader = null;

		ControlFichero controlFicheroPeticion = null;

		try {

			// create a StreamFactory
	        StreamFactory factory = StreamFactory.newInstance();
	        // load the mapping file
	        factory.loadResource(pathFileConfigCuaderno63);
	        
	        //Se guarda el registro de ControlFichero del fichero de entrada:
	        controlFicheroPeticion = 
	        		fileControlMapper.generateControlFichero(file, EmbargosConstants.COD_TIPO_FICHERO_PETICION_INFORMACION_NORMA63);
	        
	        //fileControlRepository.save(controlFicheroPeticion);
	        fileControlService.saveFileControlTransaction(controlFicheroPeticion);
	                        
	        // use a StreamFactory to create a BeanReader
	        beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_CUADERNO63_FASE1, file);
	        	        
	        CabeceraEmisorFase1 cabeceraEmisor = null;
	        EntidadesComunicadora entidadComunicadora = null;
	        Date fechaObtencionFicheroOrganismo = null;
	        
	        Object record = null;
	        while ((record = beanReader.read()) != null) {
	        
	        	if(EmbargosConstants.RECORD_NAME_SOLICITUDINFORMACION.equals(beanReader.getRecordName())) {
	        		
	        		//Registro de detalle:
	        		
	        		SolicitudInformacionFase1 solicitudInformacion = (SolicitudInformacionFase1) record;
	        		LOG.debug(solicitudInformacion.getNifDeudor());
	 		        
	        		//Llamada a Datawarehouse: Obtener las cuentas del cliente a partir del nif:        		
	        		CustomerDTO customerDTO = customerService.findCustomerByNif(solicitudInformacion.getNifDeudor());
	        		
	        		//Si existe el cliente:
	        		if (customerDTO!=null) {
	        		
		        		List<AccountDTO> accountList = customerDTO.getBankAccounts();
		        		
		        		//Tratar solamente los clientes en los que se han encontrado cuentas:
		        		if(accountList != null && !accountList.isEmpty()) {
		        		    	        			
		        			String razonSocialInterna = EmbargosUtils.determineRazonSocialInternaFromCustomer(customerDTO);
		        			
			        		//Se guarda la PeticionInformacion en bbdd:
			        		PeticionInformacion peticionInformacion = cuaderno63Mapper.generatePeticionInformacion(solicitudInformacion, 
			        				controlFicheroPeticion.getCodControlFichero(), accountList, razonSocialInterna);
			        		
			        		informationPetitionRepository.save(peticionInformacion);
			        			        		
		        			//Se guardan todas las cuentas del nif en la tabla PETICION_INFORMACION_CUENTAS:
			        		for(AccountDTO accountDTO : accountList) {
			        			
			        			PeticionInformacionCuenta peticionInformacionCuenta = 
			        					informationPetitionBankAccountMapper.toPeticionInformacionCuenta(accountDTO, 
			        							peticionInformacion.getCodPeticion());
			        			
			        			informationPetitionBankAccountRepository.save(peticionInformacionCuenta);
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
	        			throw new ICEParserException("01", "No se puede procesar el fichero '" + file.getName() +
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
			long diasRespuestaF1 = entidadComunicadora.getDiasRespuestaF1() != null ? entidadComunicadora.getDiasRespuestaF1().longValue() : 0;
			Date lastDateResponse = DateUtils.convertToDate(LocalDate.now().plusDays(diasRespuestaF1));
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
	        
	        // - Se guarda el codigo de tarea del calendario:
	        controlFicheroPeticion.setCodTarea(BigDecimal.valueOf(codTarea));
	        	        
			fileControlRepository.save(controlFicheroPeticion);

		} catch (Exception e) {
			
			//TODO Estado de ERROR pendiente de ser eliminado, se comenta:
			/*
			//Transaccion para cambiar el estado de ControlFichero a ERROR:
			fileControlService.updateFileControlStatusTransaction(controlFicheroPeticion, 
					EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_INFORMACION_NORMA63_ERROR);			
			*/

			throw e;

		} finally {

			if (beanReader != null) {
				beanReader.close();
			}
		}

	}

	@Override
	public void tramitarFicheroInformacion(Long codControlFicheroPeticion, String usuarioTramitador) throws IOException, ICEParserException {
			
		
		BeanReader beanReader = null;
		BeanWriter beanWriter = null;
		
		ControlFichero controlFicheroPeticion = null;
		ControlFichero controlFicheroInformacion = null;

		try {

			// create a StreamFactory
	        StreamFactory factory = StreamFactory.newInstance();
	        // load the mapping file
	        factory.loadResource(pathFileConfigCuaderno63);        
	        
	        //Se obtiene el ControlFichero de la Peticion:
	        controlFicheroPeticion = fileControlRepository.getOne(codControlFicheroPeticion);
	        
	        //Fichero de entrada (Peticion):
	        String fileNamePeticion = controlFicheroPeticion.getNombreFichero();
	        File ficheroEntrada = new File(pathProcessed + "\\" + fileNamePeticion);
	        
	        //Comprobar que el fichero de peticiones exista:
	        if (!ficheroEntrada.exists()) {
	        	throw new ICEParserException("","ERROR: no se ha encontrado el fichero fisico de Embargos.");
	        }
	        
	        //Comprobar que el CRC del fichero de Peticiones sea el mismo que el guardado en ControlFichero:
	        if (!controlFicheroPeticion.getNumCrc().equals(Long.toString(FileUtils.checksumCRC32(ficheroEntrada)))){
	        	throw new ICEParserException("","ERROR: el CRC del fichero de Embargos no coincide con el guardado en ControlFichero.");
	        }     
	        
	        //Se actualiza el estado de controlFicheroPeticion a TRAMITANDO:
	        EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero(
	        		EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_INFORMACION_NORMA63_PROCESSING,
	        		EmbargosConstants.COD_TIPO_FICHERO_PETICION_INFORMACION_NORMA63);
	        controlFicheroPeticion.setEstadoCtrlfichero(estadoCtrlfichero);
	        
	        //Actualizacion del flag IND_PROCESADO al iniciar la tramitacion:
	        controlFicheroPeticion.setIndProcesado(EmbargosConstants.IND_FLAG_SI);
	        
	        //fileControlRepository.saveAndFlush(controlFicheroPeticion);
	        fileControlService.saveFileControlTransaction(controlFicheroPeticion);
	        
	        //Para determinar el nombre del fichero de salida (Informacion):
	        // - se obtiene el prefijo indicado a partir de la entidad comunicadora:
	        String prefijoFichero = "";
	        if (controlFicheroPeticion.getEntidadesComunicadora()!=null) {
		        Optional<EntidadesComunicadora> entidadComunicadoraOpt = communicatingEntityRepository.findById(controlFicheroPeticion.getEntidadesComunicadora().getCodEntidadPresentadora());
		        if (entidadComunicadoraOpt.isPresent()) {
		        	prefijoFichero = entidadComunicadoraOpt.get().getPrefijoFicheros();
				}
	        }
	        // - se obtiene la fecha local actual:
	        LocalDate localDate = LocalDate.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	        String fechaNombreFichero = localDate.format(formatter);
	        String fileNameInformacion = prefijoFichero + EmbargosConstants.SEPARADOR_GUION_BAJO + fechaNombreFichero 
	            	+ EmbargosConstants.SEPARADOR_PUNTO + EmbargosConstants.TIPO_FICHERO_INFORMACION.toLowerCase();
	        File ficheroSalida = new File(pathGenerated + "\\" + fileNameInformacion);
	        
	        //Se guarda el registro de ControlFichero del fichero de salida:
	        controlFicheroInformacion = 
	        		fileControlMapper.generateControlFichero(ficheroSalida, EmbargosConstants.COD_TIPO_FICHERO_ENVIO_INFORMACION_NORMA63);
	        
	        //Usuario que realiza la tramitacion:
	        controlFicheroInformacion.setUsuarioUltModificacion(usuarioTramitador);
	        	        
	        //fileControlRepository.save(controlFicheroInformacion);
	        fileControlService.saveFileControlTransaction(controlFicheroInformacion);
	                
	        // use a StreamFactory to create a BeanReader
	        beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_CUADERNO63_FASE1, ficheroEntrada);
	        beanWriter = factory.createWriter(EmbargosConstants.STREAM_NAME_CUADERNO63_FASE2, ficheroSalida);
	        
	        EntidadesComunicadora entidadComunicadora = null;
	        
	        Object record = null;
	        while ((record = beanReader.read()) != null) {
	        
	        	if(EmbargosConstants.RECORD_NAME_SOLICITUDINFORMACION.equals(beanReader.getRecordName())) {
	        		
	        		SolicitudInformacionFase1 solicitudInformacion = (SolicitudInformacionFase1) record;
	        		LOG.debug(solicitudInformacion.getNifDeudor());
	 		               		
	        		//Se obtiene la peticionInformacion a partir del correspondiente ControlFichero y NIF:
	        		PeticionInformacion peticionInformacion = 
	        				informationPetitionRepository.findByControlFicheroAndNif(controlFicheroPeticion,
	        						solicitudInformacion.getNifDeudor());		
	        		
	        		if(peticionInformacion!=null) {
	        			//Se guardan:
	        			//- El codigo del fichero respuesta:
	        			peticionInformacion.setCodFicheroRespuesta(BigDecimal.valueOf(controlFicheroInformacion.getCodControlFichero()));
	        			
	        			//- El calculo de las claves de seguridad de cada cuenta (el calculo se realiza en la fase 2, al tramitar, y no antes):
	        			EmbargosUtils.calculateClavesSeguridadInPeticionInformacion(peticionInformacion);
	        			
	        			informationPetitionRepository.save(peticionInformacion);
	        			
	        		} else {
	        			//Si peticionInformacion es nulo: inicializar el objeto vacio:
	        			peticionInformacion = new PeticionInformacion();
	        		}
	        		
	        		//Se genera la respuesta
	        		RespuestaSolicitudInformacionFase2 respuesta = 
	        				cuaderno63Mapper.generateRespuestaSolicitudInformacionFase2(solicitudInformacion, peticionInformacion);
	        		
	        		beanWriter.write(EmbargosConstants.RECORD_NAME_RESPUESTASOLICITUDINFORMACION, respuesta);
	        	
	        	} else if(EmbargosConstants.RECORD_NAME_CABECERAEMISOR.equals(beanReader.getRecordName())) {
	 
	        		CabeceraEmisorFase1 cabeceraEmisorFase1 = (CabeceraEmisorFase1) record;
	        		LOG.debug(cabeceraEmisorFase1.getNombreOrganismoEmisor());
	        		
	        		String nifOrganismoEmisor = cabeceraEmisorFase1.getNifOrganismoEmisor();
	        		
	        		entidadComunicadora = communicatingEntityRepository.findByNifEntidad(nifOrganismoEmisor);
	        		
	        		Date fechaObtencionFicheroEntidadDeDeposito = new Date();
	        		
	        		CabeceraEmisorFase2 cabeceraEmisorFase2 = cuaderno63Mapper.generateCabeceraEmisorFase2(cabeceraEmisorFase1, 
	        				fechaObtencionFicheroEntidadDeDeposito);
	        		
	        		beanWriter.write(EmbargosConstants.RECORD_NAME_CABECERAEMISOR, cabeceraEmisorFase2);
	        		
	        	} else if(EmbargosConstants.RECORD_NAME_FINFICHERO.equals(beanReader.getRecordName())) {
	        		
	        		FinFicheroFase1 finFicheroFase1 = (FinFicheroFase1) record;
	        		LOG.debug(finFicheroFase1.getNombreOrganismoEmisor());
	        		
	        		FinFicheroFase2 finFicheroFase2 = cuaderno63Mapper.generateFinFicheroFase2(finFicheroFase1);
	        		
	        		beanWriter.write(EmbargosConstants.RECORD_NAME_FINFICHERO, finFicheroFase2);
	        	}
	        	
	        	LOG.debug(record.toString());
	        	
	        	beanWriter.flush();
	        	
	        }
	
	        //ACTUALIZACIONES DEL FICHERO DE SALIDA (controlFicheroInformacion):
	        //1.- Se actualiza el CRC:
	        controlFicheroInformacion.setNumCrc(Long.toString(FileUtils.checksumCRC32(ficheroSalida)));

			//2.- Se guarda el codigo de la Entidad comunicadora:
	        controlFicheroInformacion.setEntidadesComunicadora(entidadComunicadora);
	        
	        //3.- Se actualiza el estado de controlFicheroInformacion a GENERADO:
	        estadoCtrlfichero = new EstadoCtrlfichero(
	        		EmbargosConstants.COD_ESTADO_CTRLFICHERO_ENVIO_INFORMACION_NORMA63_GENERATED,
	        		EmbargosConstants.COD_TIPO_FICHERO_ENVIO_INFORMACION_NORMA63);
	        controlFicheroInformacion.setEstadoCtrlfichero(estadoCtrlfichero);
	        
	        //4.- Actualizacion del fichero de origen:
	        controlFicheroInformacion.setControlFicheroOrigen(controlFicheroPeticion);
	        
	        fileControlRepository.save(controlFicheroInformacion);
	        
	        //ACTUALIZACIONES DEL FICHERO DE ENTRADA (controlFicheroPeticion):
	        //1.- Actualizacion del flag IND_PROCESADO:
	        controlFicheroPeticion.setIndProcesado(EmbargosConstants.IND_FLAG_SI);
	        
	        //2.- Actualizacion del fichero de respuesta:
	        controlFicheroPeticion.setControlFicheroRespuesta(controlFicheroInformacion);
	        
	        //3.- Se actualiza el estado de controlFicheroPeticion a TRAMITADO:
	        estadoCtrlfichero = new EstadoCtrlfichero(
	        		EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_INFORMACION_NORMA63_PROCESSED,
	        		EmbargosConstants.COD_TIPO_FICHERO_PETICION_INFORMACION_NORMA63);
	        controlFicheroPeticion.setEstadoCtrlfichero(estadoCtrlfichero);
	        
	        fileControlRepository.save(controlFicheroPeticion);
        
	        //Cerrar la tarea:
	        boolean closed = false;
	        if (controlFicheroPeticion.getCodTarea()!=null) {
	        	
	        	Long codTarea = controlFicheroPeticion.getCodTarea().longValue();	        	
	        	closed = taskService.closeCalendarTask(codTarea);
		      
	        	if(!closed) {
		        	LOG.error("ERROR: No se ha cerrado la Tarea");
		        	//TODO: lanzar excepcion si no se ha cerrado la tarea
		        }
	        } else {
	        	LOG.error("ERROR al cerrar la tarea: No se ha encontrado el codigo de la Tarea");
	        	//TODO: lanzar excepcion si no se ha encontrado el codigo de tarea
	        }

	        
	        
		} catch (Exception e) {
			
			//TODO Estado de ERROR pendiente de ser eliminado, se comenta:
			/*
			//Transaccion para cambiar el estado de controlFicheroPeticion a ERROR:
			fileControlService.updateFileControlStatusTransaction(controlFicheroPeticion, 
					EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_INFORMACION_NORMA63_ERROR);
			
			//Transaccion para cambiar el estado de controlFicheroInformacion a ERROR:
			fileControlService.updateFileControlStatusTransaction(controlFicheroInformacion, 
					EmbargosConstants.COD_ESTADO_CTRLFICHERO_ENVIO_INFORMACION_NORMA63_ERROR);
			*/

			throw e;

		} finally {
			if (beanReader != null) {
				beanReader.close();
			}
			if (beanWriter != null) {
				beanWriter.close();
			}
		}
	}

	@Override
	public void cargarFicheroEmbargos(File file) throws IOException{
		
		BeanReader beanReader = null;
		
		ControlFichero controlFicheroEmbargo = null;
		
		try {

			// create a StreamFactory
	        StreamFactory factory = StreamFactory.newInstance();
	        // load the mapping file
	        factory.loadResource(pathFileConfigCuaderno63);
	        
	        //Se guarda el registro de ControlFichero del fichero de entrada:
	        controlFicheroEmbargo = 
	        		fileControlMapper.generateControlFichero(file, EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_NORMA63);
	        
	        fileControlService.saveFileControlTransaction(controlFicheroEmbargo);
	        
	        // use a StreamFactory to create a BeanReader
	        beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_CUADERNO63_FASE3, file);
	        
	        Object record = null;
	    	
	        CabeceraEmisorFase3 cabeceraEmisor = null;
	        OrdenEjecucionEmbargoFase3 ordenEjecucionEmbargo = null;
	    	OrdenEjecucionEmbargoComplementarioFase3 ordenEjecucionEmbargoComp = null;
	    	boolean isRecordOrdenEjecEmbargo;
	    	EntidadesOrdenante entidadOrdenante = null;
	    	Date fechaObtencionFicheroOrganismo = null;
	    	
	        while ((record = beanReader.read()) != null) {
	               
	        	isRecordOrdenEjecEmbargo = EmbargosConstants.RECORD_NAME_ORDENEJECUCIONEMBARGO.equals(beanReader.getRecordName());
	        	
	        	if(isRecordOrdenEjecEmbargo) {
	
	        		ordenEjecucionEmbargo = (OrdenEjecucionEmbargoFase3) record;
	        
	        	} else if(EmbargosConstants.RECORD_NAME_ORDENEJECUCIONEMBARGOCOMPLEMENTARIO.equals(beanReader.getRecordName())) {
	        		
	        		ordenEjecucionEmbargoComp = (OrdenEjecucionEmbargoComplementarioFase3) record;
	        		
	        		
	        	} else if(EmbargosConstants.RECORD_NAME_CABECERAEMISOR.equals(beanReader.getRecordName())) {
	        		
	        		cabeceraEmisor = (CabeceraEmisorFase3) record;
	        		
	        		String nifOrganismoEmisor = cabeceraEmisor.getNifOrganismoEmisor();
	        		
	        		entidadOrdenante = orderingEntityRepository.findByNifEntidad(nifOrganismoEmisor);
	        		
	        		fechaObtencionFicheroOrganismo = cabeceraEmisor.getFechaObtencionFicheroOrganismo();
	        		
	        		//TODO TRATAR SI entidadOrdenante ES NULO -> NO SE PUEDE CREAR REGISTRO EN EMBARGO...
	        		
	        		
	        	} else if(EmbargosConstants.RECORD_NAME_FINFICHERO.equals(beanReader.getRecordName())) {
	        		
	        	}
	        	
	        	//Tratamiento de los registros principal y complementario de embargos:
	        	if (ordenEjecucionEmbargo!=null && !isRecordOrdenEjecEmbargo) {
	        		
	        		Embargo embargo = null;
	        		Traba traba = null;
	        		
	        		//- Se obtienen de Datawarehouse los datos del cliente y sus cuentas a partir del NIF del cliente:
	        		CustomerDTO customerDTO = customerService.findCustomerByNif(ordenEjecucionEmbargoComp.getNifDeudor());
	        		
	        		//- Se almacenan las cuentas obtenidas de Datawarehouse en una Hash donde la key es el IBAN:
	        		Map<String, AccountDTO> customerAccountsMap = new HashMap<>(); 
	        		if (customerDTO!=null) {
		        		for (AccountDTO accountDTO : customerDTO.getBankAccounts()) {    		
		        			customerAccountsMap.put(accountDTO.getIban(), accountDTO);
		        		}
	        		}
	        		
	        		//Determinacion de la fecha limite de la traba:
	        		BigDecimal diasRespuestaFase3 = new BigDecimal(0);
	        		if (entidadOrdenante!=null && entidadOrdenante.getEntidadesComunicadora()!=null && entidadOrdenante.getEntidadesComunicadora().getDiasRespuestaF3()!=null) {
	        			diasRespuestaFase3 = entidadOrdenante.getEntidadesComunicadora().getDiasRespuestaF3();
	        		}       				
	        		BigDecimal fechaLimiteTraba = null;
	        		if (fechaObtencionFicheroOrganismo!=null) {
	        			Date fechaLimiteTrabaDate = DateUtils.convertToDate(DateUtils.convertToLocalDate(fechaObtencionFicheroOrganismo).plusDays(diasRespuestaFase3.longValue()));
	        			fechaLimiteTraba = ICEDateUtils.dateToBigDecimal(fechaLimiteTrabaDate, ICEDateUtils.FORMAT_yyyyMMdd);
	        		}
	        		
	        		//Razon social interna (obtenida del customerDTO de datawarehouse):
	        		String razonSocialInterna = EmbargosUtils.determineRazonSocialInternaFromCustomer(customerDTO);
	        		
	        		//Generacion de las instancias de Embargo y de Traba:
	        		if (ordenEjecucionEmbargoComp!=null 
	        				&& ordenEjecucionEmbargo.getNifDeudor().equals(ordenEjecucionEmbargoComp.getNifDeudor())) {
	        			
	        			embargo = cuaderno63Mapper.generateEmbargo(ordenEjecucionEmbargo, ordenEjecucionEmbargoComp, controlFicheroEmbargo.getCodControlFichero(), entidadOrdenante, razonSocialInterna, fechaLimiteTraba, customerAccountsMap);
	        			
	        			traba =  cuaderno63Mapper.generateTraba(ordenEjecucionEmbargo, ordenEjecucionEmbargoComp, controlFicheroEmbargo.getCodControlFichero(), entidadOrdenante, fechaLimiteTraba, customerAccountsMap);        			
	        			traba.setEmbargo(embargo);
	        			
	        		} else {
	        			embargo = cuaderno63Mapper.generateEmbargo(ordenEjecucionEmbargo, new OrdenEjecucionEmbargoComplementarioFase3(), controlFicheroEmbargo.getCodControlFichero(), entidadOrdenante, razonSocialInterna, fechaLimiteTraba, customerAccountsMap);

	        			traba =  cuaderno63Mapper.generateTraba(ordenEjecucionEmbargo, ordenEjecucionEmbargoComp, controlFicheroEmbargo.getCodControlFichero(), entidadOrdenante, fechaLimiteTraba, customerAccountsMap);        			
	        			traba.setEmbargo(embargo);
	        			
	        		}
       			        		
	        		//Guardar los datos del embargo y traba:
	        		seizureRepository.save(embargo);
	        		
	        		seizedRepository.save(traba);
	        		
	        		//- Se guardan las cuentas obtenidas del fichero de embargo en CUENTA_EMBARGO:
	        		for (CuentaEmbargo cuentaEmbargo : embargo.getCuentaEmbargos()) {
	        			
	        			//TODO mirar si se tiene que comprobar si es nulo el Iban o bien la "cuenta":
	        			if (cuentaEmbargo.getIban()!=null && !cuentaEmbargo.getIban().isEmpty()) {
	        					        				
	        				seizureBankAccountRepository.save(cuentaEmbargo);
	        			}
	        		}
	        		
	        		//Para almacenar los numeros de cuenta del cliente que vienen en el fichero de embargos:
	        		List<String> ibanClienteFicheroEmbargoList = new ArrayList<>();
	        		BigDecimal numeroOrdenCuenta = new BigDecimal(0);
	        		
	        		//- Se guardan las cuentas obtenidas del fichero de embargo en CUENTA_TRABA:
	        		for (CuentaTraba cuentaTraba : traba.getCuentaTrabas()) {
	        			
	        			//TODO mirar si se tiene que comprobar si es nulo el Iban o bien la "cuenta":
	        			if (cuentaTraba.getIban()!=null && !cuentaTraba.getIban().isEmpty()) {

	        				ibanClienteFicheroEmbargoList.add(cuentaTraba.getIban());
	        				
	        				numeroOrdenCuenta = (cuentaTraba.getNumeroOrdenCuenta()!=null && cuentaTraba.getNumeroOrdenCuenta().compareTo(numeroOrdenCuenta) > 0) ?
	        						cuentaTraba.getNumeroOrdenCuenta() : numeroOrdenCuenta;
	        						
	        				seizedBankAccountRepository.save(cuentaTraba);
	        			}
	        		}
	        		
	        		
	        		//- Se guardan en CUENTA_TRABA el resto de cuentas obtenidas de Datawarehouse que no se encuentran en el fichero de Embargos (cuentas extra):
	        		if(customerDTO!=null) {
	        			
	        			for (AccountDTO accountDTO : customerDTO.getBankAccounts()) {
	        			
	        				//Se guarda la cuenta si no se encuentra en el fichero de embargos:
	        				if(!ibanClienteFicheroEmbargoList.contains(accountDTO.getIban())) {
	        					
	        					numeroOrdenCuenta = numeroOrdenCuenta.add(BigDecimal.valueOf(1));
	        					
	        					CuentaTraba cuentaTrabaExtra = seizedBankAccountMapper.accountDTOToCuentaTraba(accountDTO, numeroOrdenCuenta, traba);
	        					seizedBankAccountRepository.save(cuentaTrabaExtra);
	        				}
	        			}
	        		}
	        		
	        		
	        		//inicializar a null para la siguiente iteracion:
	        		ordenEjecucionEmbargo = null;
	        		ordenEjecucionEmbargoComp = null;
	        	}
	        	
	        }
	        
	        
	        //Datos a guardar en ControlFichero una vez procesado el fichero:
	        
			//- Se guarda el codigo de la Entidad comunicadora en ControlFichero:
			EntidadesComunicadora entidadComunicadora = entidadOrdenante.getEntidadesComunicadora();
			controlFicheroEmbargo.setEntidadesComunicadora(entidadOrdenante.getEntidadesComunicadora());

			//- Se guarda la fecha de la cabecera en el campo fechaCreacion de ControlFichero:
			BigDecimal fechaObtencionFicheroOrganismoBigDec = fechaObtencionFicheroOrganismo != null ? ICEDateUtils.dateToBigDecimal(fechaObtencionFicheroOrganismo, ICEDateUtils.FORMAT_yyyyMMdd) : null;
			controlFicheroEmbargo.setFechaCreacion(fechaObtencionFicheroOrganismoBigDec);
			controlFicheroEmbargo.setFechaComienzoCiclo(fechaObtencionFicheroOrganismoBigDec);

			//- Se guarda la fecha maxima de respuesta (now + dias de margen)
			long diasRespuestaF3 = entidadComunicadora.getDiasRespuestaF3() != null ? entidadComunicadora.getDiasRespuestaF3().longValue() : 0;
			Date lastDateResponse = DateUtils.convertToDate(LocalDate.now().plusDays(diasRespuestaF3));
			BigDecimal limitResponseDate = ICEDateUtils.dateToBigDecimal(lastDateResponse, ICEDateUtils.FORMAT_yyyyMMdd);
			controlFicheroEmbargo.setFechaMaximaRespuesta(limitResponseDate);

			//Cambio de estado de CtrlFichero a: RECIBIDO
	        EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero(
	        		EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_RECEIVED,
	        		EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_NORMA63);
	        controlFicheroEmbargo.setEstadoCtrlfichero(estadoCtrlfichero);
			
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
	        
	        // - Se guarda el codigo de tarea del calendario:
	        controlFicheroEmbargo.setCodTarea(BigDecimal.valueOf(codTarea));

			fileControlRepository.save(controlFicheroEmbargo);

		} catch (Exception e) {

			//TODO Estado de ERROR pendiente de ser eliminado, se comenta:
			/*
			//Transaccion para cambiar el estado de controlFicheroPeticion a ERROR:
			fileControlService.updateFileControlStatusTransaction(controlFicheroEmbargo, 
					EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_ERROR);
			*/

			throw e;

		} finally {

			if (beanReader != null) {
				beanReader.close();
			}
		}

	}

	@Override
	public void tramitarTrabas(Long codControlFicheroEmbargo, String usuarioTramitador) throws IOException, ICEParserException {

		BeanReader beanReader = null;
		BeanWriter beanWriter = null;
		
		ControlFichero controlFicheroEmbargo = null;
		ControlFichero controlFicheroTrabas = null;
		
		try {
		
			// create a StreamFactory
	        StreamFactory factory = StreamFactory.newInstance();
	        // load the mapping file
	        factory.loadResource(pathFileConfigCuaderno63);        
	        
	        //Se obtiene el ControlFichero de Embargos:
	        controlFicheroEmbargo = fileControlRepository.getOne(codControlFicheroEmbargo);
	        
	        //Fichero de embargos:
	        String fileNameEmbargo = controlFicheroEmbargo.getNombreFichero();
	        File ficheroEmbargo = new File(pathProcessed + "\\" + fileNameEmbargo);
	        
	        //Comprobar que el fichero de embargos exista:
	        if (!ficheroEmbargo.exists()) {
	        	throw new ICEParserException("","ERROR: no se ha encontrado el fichero fisico de Embargos.");
	        }
	        
	        //Comprobar que el CRC del fichero de Embargos sea el mismo que el guardado en ControlFichero:
	        if (!controlFicheroEmbargo.getNumCrc().equals(Long.toString(FileUtils.checksumCRC32(ficheroEmbargo)))){
	        	throw new ICEParserException("","ERROR: el CRC del fichero de Embargos no coincide con el guardado en ControlFichero.");
	        }
	        
	        //Se actualiza el estado de controlFicheroEmbargo a Pendiente de envio:
	        EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero(
	        		EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_PENDING_TO_SEND,
	        		EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_NORMA63);
	        controlFicheroEmbargo.setEstadoCtrlfichero(estadoCtrlfichero);
	        
	        //Actualizacion del flag IND_PROCESADO al iniciar la tramitacion:
	        controlFicheroEmbargo.setIndProcesado(EmbargosConstants.IND_FLAG_SI);
	        
	        fileControlService.saveFileControlTransaction(controlFicheroEmbargo);
	        
	        //Para determinar el nombre del fichero de salida (Informacion):
	        // - se obtiene el prefijo indicado a partir de la entidad comunicadora:
	        String prefijoFichero = "";
	        EntidadesComunicadora entidadComunicadora = null;
	        if (controlFicheroEmbargo.getEntidadesComunicadora()!=null) {
		        Optional<EntidadesComunicadora> entidadComunicadoraOpt = communicatingEntityRepository.findById(controlFicheroEmbargo.getEntidadesComunicadora().getCodEntidadPresentadora());
		        if (entidadComunicadoraOpt.isPresent()) {
		        	entidadComunicadora = entidadComunicadoraOpt.get();
		        	prefijoFichero = entidadComunicadora.getPrefijoFicheros();
				} else {					
		        	throw new ICEParserException("","ERROR: no se ha encontrado la entidad comunicadora asociada al codControlFichero=" + controlFicheroEmbargo.getCodControlFichero());
			
				}
	        }
	        // - se obtiene la fecha local actual:
	        LocalDate localDate = LocalDate.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	        String fechaNombreFichero = localDate.format(formatter);
	        String fileNameTrabas = prefijoFichero + EmbargosConstants.SEPARADOR_GUION_BAJO + fechaNombreFichero 
	            	+ EmbargosConstants.SEPARADOR_PUNTO + EmbargosConstants.TIPO_FICHERO_TRABAS.toLowerCase();
	        File ficheroSalida = new File(pathGenerated + "\\" + fileNameTrabas);
	        
	        //Se guarda el registro de ControlFichero del fichero de salida:
	        controlFicheroTrabas = 
	        		fileControlMapper.generateControlFichero(ficheroSalida, EmbargosConstants.COD_TIPO_FICHERO_TRABAS_NORMA63);
	        
	        //Usuario que realiza la tramitacion:
	        controlFicheroTrabas.setUsuarioUltModificacion(usuarioTramitador);
	        	        
	        //fileControlRepository.save(controlFicheroTrabas);
	        fileControlService.saveFileControlTransaction(controlFicheroTrabas);
	                
	        // use a StreamFactory to create a BeanReader
	        beanWriter = factory.createWriter(EmbargosConstants.STREAM_NAME_CUADERNO63_FASE4, ficheroSalida);
	        	        
	        //GENERACION DEL FICHERO DE TRABAS:    
	        // 1.- Leer la cabecera y el registro final del fichero de Embargos de fase 3:
	   
	        CabeceraEmisorFase3 cabeceraEmisorFase3 = null;
	        FinFicheroFase3 finFicheroFase3 = null;
	        
	        beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_CUADERNO63_FASE3, ficheroEmbargo);	
	        
	        Object record = null; 
	        while ((record = beanReader.read()) != null) {
		        if(EmbargosConstants.RECORD_NAME_CABECERAEMISOR.equals(beanReader.getRecordName())) {
		        
		        	cabeceraEmisorFase3 = (CabeceraEmisorFase3) record;
		        }
		        
		        if(EmbargosConstants.RECORD_NAME_FINFICHERO.equals(beanReader.getRecordName())) {
			        
		        	finFicheroFase3 = (FinFicheroFase3) record;
		        }
	        }
		        
	        if (cabeceraEmisorFase3==null) {
	        	throw new ICEParserException("11","ERROR: No se ha encontrado el registro de cabecera del fichero de Embargos '"+ fileNameEmbargo + "'.");
	        }
	        
	        if (finFicheroFase3==null) {
	        	throw new ICEParserException("12","ERROR: No se ha encontrado el registro fin del fichero de Embargos '"+ fileNameEmbargo + "'.");
	        }
	        
	        int numeroRegistrosFichero = 0;
	        
	        // 2.- Generar y escribir registro de CABECERA de fase 4:
	        CabeceraEmisorFase4 cabeceraEmisorFase4 = cuaderno63Mapper.generateCabeceraEmisorFase4(cabeceraEmisorFase3);
	        beanWriter.write(EmbargosConstants.RECORD_NAME_CABECERAEMISOR, cabeceraEmisorFase4);
	        numeroRegistrosFichero++;
	        
	        // 3.- Generar registros del DETALLE:
	        
	        //Se recuperan los embargos/trabas y sus cuentas asociadas:
	        List<SeizureDTO> seizureDTOList = seizureService.getSeizureListByCodeFileControl(codControlFicheroEmbargo);
	        
			ControlFichero controlFichero = new ControlFichero();
			controlFichero.setCodControlFichero(codControlFicheroEmbargo);
			
	        List<Embargo> embargoList = seizureRepository.findAllByControlFichero(controlFichero);
	        
	        for (Embargo embargo : embargoList) {
	        	
	    		Traba traba = null;
	    		
	    		if (embargo.getTrabas()!=null) {
	    			traba = embargo.getTrabas().get(0);
	    		} else {
	    			throw new ICEParserException("", "ERROR: el embargo no tiene una traba asociada.");
	    		}
	        	
	        	//Cuentas
	    		List<CuentaTraba> cuentaTrabaOrderedList = seizedBankAccountRepository.findAllByTrabaOrderByNumeroOrdenCuentaAsc(traba);
	        	
		        ComunicacionResultadoRetencionFase4 comunicacionResultadoRetencionFase4 = 
		        		cuaderno63Mapper.generateComunicacionResultadoRetencionFase4(embargo, traba, cuentaTrabaOrderedList);
		        
		        beanWriter.write(EmbargosConstants.RECORD_NAME_COMUNICACIONRESULTADORETENCION, comunicacionResultadoRetencionFase4);
		        numeroRegistrosFichero++;
	        	
		        beanWriter.flush();
	        } 

	        
	        // 4.- Generar FIN de fichero indicando el numero de registros:
	        numeroRegistrosFichero++;
	        FinFicheroFase4 finFicheroFase4 = cuaderno63Mapper.generateFinFicheroFase4(finFicheroFase3, numeroRegistrosFichero);
	        beanWriter.write(EmbargosConstants.RECORD_NAME_FINFICHERO, finFicheroFase4);

	        beanWriter.flush();
	
	        //ACTUALIZACIONES DEL FICHERO DE SALIDA (controlFicheroTrabas):
	        //1.- Se actualiza el CRC:
	        controlFicheroTrabas.setNumCrc(Long.toString(FileUtils.checksumCRC32(ficheroSalida)));

			//2.- Se guarda el codigo de la Entidad comunicadora:
	        controlFicheroTrabas.setEntidadesComunicadora(entidadComunicadora);
	        
	        //3.- Se actualiza el estado del fichero de Trabas a GENERADO:     
	        estadoCtrlfichero = new EstadoCtrlfichero(
	        				EmbargosConstants.COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_NORMA63_GENERATED,
	        				EmbargosConstants.COD_TIPO_FICHERO_TRABAS_NORMA63);
	        controlFicheroTrabas.setEstadoCtrlfichero(estadoCtrlfichero);
	        
	        //4.- Actualizacion del fichero de origen:
	        controlFicheroTrabas.setControlFicheroOrigen(controlFicheroEmbargo);
	        
	        fileControlRepository.save(controlFicheroTrabas);
	        
	        //ACTUALIZACIONES DEL FICHERO DE ENTRADA (controlFicheroEmbargo):
	        //1.- Actualizacion del flag IND_PROCESADO:
	        controlFicheroEmbargo.setIndProcesado(EmbargosConstants.IND_FLAG_SI);
	        
	        //2.- Actualizacion del fichero de respuesta:
	        controlFicheroEmbargo.setControlFicheroRespuesta(controlFicheroTrabas);
	        
	        //3.- Se actualiza el estado del fichero de Embargos a GENERADO:
	        estadoCtrlfichero = new EstadoCtrlfichero(
    				EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_GENERATED,
    				EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_NORMA63);
	        controlFicheroEmbargo.setEstadoCtrlfichero(estadoCtrlfichero);
	        
	        fileControlRepository.save(controlFicheroEmbargo);
        
	        //Cerrar la tarea:
	        boolean closed = false;
	        if (controlFicheroEmbargo.getCodTarea()!=null) {
	        	
	        	Long codTarea = controlFicheroEmbargo.getCodTarea().longValue();	        	
	        	closed = taskService.closeCalendarTask(codTarea);
		      
	        	if(!closed) {
		        	LOG.error("ERROR: No se ha cerrado la Tarea");
		        	//TODO: lanzar excepcion si no se ha cerrado la tarea
		        }
	        } else {
	        	LOG.error("ERROR al cerrar la tarea: No se ha encontrado el codigo de la Tarea");
	        	//TODO: lanzar excepcion si no se ha encontrado el codigo de tarea
	        }

	        
	        
		} catch (Exception e) {
			
			//TODO Estado de ERROR pendiente de ser eliminado, se comenta:
			/*
			//Transaccion para cambiar el estado de controlFicheroEmbargo a ERROR:
			fileControlService.updateFileControlStatusTransaction(controlFicheroEmbargo, 
					EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_ERROR);
			
			
			//Transaccion para cambiar el estado de controlFicheroTrabas a ERROR:
			fileControlService.updateFileControlStatusTransaction(controlFicheroTrabas, 
					EmbargosConstants.COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_NORMA63_ERROR);
			*/
			
			LOG.error("ERROR: ", e);
			
			throw e;
			
		} finally {

			if (beanWriter!=null) {
				beanWriter.close();
			}
		}
		
	}
}
