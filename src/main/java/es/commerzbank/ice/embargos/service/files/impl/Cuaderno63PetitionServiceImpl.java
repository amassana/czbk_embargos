package es.commerzbank.ice.embargos.service.files.impl;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.beanio.BeanReader;
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
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlfichero;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacion;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacionCuenta;
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
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.files.Cuaderno63PetitionService;
import es.commerzbank.ice.utils.EmbargosConstants;
import es.commerzbank.ice.utils.EmbargosUtils;
import es.commerzbank.ice.utils.ICEDateUtils;

@Service
@Transactional(transactionManager="transactionManager", propagation = Propagation.REQUIRES_NEW)
public class Cuaderno63PetitionServiceImpl implements Cuaderno63PetitionService{

	private static final Logger LOG = LoggerFactory.getLogger(Cuaderno63PetitionServiceImpl.class);
	
	@Value("${commerzbank.embargos.beanio.config-path.cuaderno63}")
	String pathFileConfigCuaderno63;

	@Value("${commerzbank.embargos.files.path.monitoring}")
	private String pathMonitoring;

	@Value("${commerzbank.embargos.files.path.processed}")
	private String pathProcessed;

	@Value("${commerzbank.embargos.files.path.generated}")
	private String pathGenerated;

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
	
	@Override
	//Se comenta '@transactional' ya que se utilizara a nivel de clase:
	//@Transactional(transactionManager="transactionManager", propagation = Propagation.REQUIRES_NEW)
	public void cargarFicheroPeticion(File file) throws IOException, ICEParserException {

		BeanReader beanReader = null;
		Reader reader = null;

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

			reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
	        beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_CUADERNO63_FASE1, reader);

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
			if (reader != null)
				reader.close();
			if (beanReader != null)
				beanReader.close();
		}

	}

}
