package es.commerzbank.ice.embargos.service.files.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
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
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
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
import es.commerzbank.ice.embargos.domain.mapper.Cuaderno63Mapper;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.domain.mapper.SeizedBankAccountMapper;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.CabeceraEmisorFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.OrdenEjecucionEmbargoComplementarioFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.OrdenEjecucionEmbargoFase3;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.OrderingEntityRepository;
import es.commerzbank.ice.embargos.repository.SeizedBankAccountRepository;
import es.commerzbank.ice.embargos.repository.SeizedRepository;
import es.commerzbank.ice.embargos.repository.SeizureBankAccountRepository;
import es.commerzbank.ice.embargos.repository.SeizureRepository;
import es.commerzbank.ice.embargos.service.CustomerService;
import es.commerzbank.ice.embargos.service.EmailService;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.files.Cuaderno63SeizureService;
import es.commerzbank.ice.utils.EmbargosConstants;
import es.commerzbank.ice.utils.EmbargosUtils;
import es.commerzbank.ice.utils.ICEDateUtils;

@Service
@Transactional(transactionManager="transactionManager", propagation = Propagation.REQUIRES_NEW)
public class Cuaderno63SeizureServiceImpl implements Cuaderno63SeizureService{

	private static final Logger LOG = LoggerFactory.getLogger(Cuaderno63SeizureServiceImpl.class);

	@Value("${commerzbank.embargos.beanio.config-path.cuaderno63}")
	String pathFileConfigCuaderno63;

	@Autowired
	private Cuaderno63Mapper cuaderno63Mapper;

	@Autowired
	private FileControlMapper fileControlMapper;

	@Autowired
	private SeizedBankAccountMapper seizedBankAccountMapper;

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
	private SeizureRepository seizureRepository;

	@Autowired
	private SeizedRepository seizedRepository;

	@Autowired
	private SeizureBankAccountRepository seizureBankAccountRepository;

	@Autowired
	private SeizedBankAccountRepository seizedBankAccountRepository;

	@Autowired
	private OrderingEntityRepository orderingEntityRepository;
	
	@Autowired
	private EmailService emailService;

	@Autowired
	private GeneralParametersService generalParametersService;
	
	@Override
	public void cargarFicheroEmbargos(File file) throws IOException, ICEException{
		
		BeanReader beanReader = null;
		Reader reader = null;
		
		ControlFichero controlFicheroEmbargo = null;
		
		String seizureFileName = null;
		
		try {
			
			seizureFileName = FilenameUtils.getName(file.getCanonicalPath());

			// create a StreamFactory
	        StreamFactory factory = StreamFactory.newInstance();
	        // load the mapping file
	        factory.loadResource(pathFileConfigCuaderno63);
	        
	        //Se guarda el registro de ControlFichero del fichero de entrada:
	        controlFicheroEmbargo = 
	        		fileControlMapper.generateControlFichero(file, EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_NORMA63);
	        
	        fileControlService.saveFileControlTransaction(controlFicheroEmbargo);
	        
	        // use a StreamFactory to create a BeanReader
	        String encoding = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_ENCODING_NORMA63);
	        
			reader = new InputStreamReader(new FileInputStream(file), encoding);
	        beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_CUADERNO63_FASE3, reader);
	        
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

			// - Se envia correo del error del parseo del fichero de embargo:
			emailService.sendEmailFileParserError(seizureFileName, e.getMessage()); 
			
			throw e;

		} finally {

			if (reader!=null) {
				reader.close();
			}
			if (beanReader != null) {
				beanReader.close();
			}
		}

	}

}
