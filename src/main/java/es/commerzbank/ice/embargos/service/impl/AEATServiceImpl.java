package es.commerzbank.ice.embargos.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.comun.lib.domain.dto.TaskAndEvent;
import es.commerzbank.ice.comun.lib.service.TaskService;
import es.commerzbank.ice.comun.lib.typeutils.DateUtils;
import es.commerzbank.ice.comun.lib.util.ICEParserException;
import es.commerzbank.ice.datawarehouse.domain.dto.AccountDTO;
import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.CuentaEmbargo;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora;
import es.commerzbank.ice.embargos.domain.entity.EntidadesOrdenante;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlfichero;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlficheroPK;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.embargos.domain.mapper.AEATMapper;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.domain.mapper.SeizedBankAccountMapper;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.Diligencia;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.EntidadCredito;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.EntidadTransmisora;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.InformationPetitionRepository;
import es.commerzbank.ice.embargos.repository.OrderingEntityRepository;
import es.commerzbank.ice.embargos.repository.SeizedBankAccountRepository;
import es.commerzbank.ice.embargos.repository.SeizedRepository;
import es.commerzbank.ice.embargos.repository.SeizureBankAccountRepository;
import es.commerzbank.ice.embargos.repository.SeizureRepository;
import es.commerzbank.ice.embargos.service.AEATService;
import es.commerzbank.ice.embargos.service.CustomerService;
import es.commerzbank.ice.utils.EmbargosConstants;
import es.commerzbank.ice.utils.EmbargosUtils;
import es.commerzbank.ice.utils.ICEDateUtils;

@Service
@Transactional(transactionManager="transactionManager")
public class AEATServiceImpl implements AEATService{

	private static final Logger LOG = LoggerFactory.getLogger(AEATServiceImpl.class);
	
	@Value("${commerzbank.embargos.beanio.config-path.aeat}")
	String pathFileConfigAEAT;
	
	@Autowired
	AEATMapper aeatMapper;
	
	@Autowired
	FileControlMapper fileControlMapper;
	
	@Autowired
	SeizedBankAccountMapper seizedBankAccountMapper;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	TaskService taskService;

	@Autowired
	FileControlRepository fileControlRepository;
	
	@Autowired
	InformationPetitionRepository informationPetitionRepository;
	
	@Autowired
	OrderingEntityRepository orderingEntityRepository;
	
	@Autowired
	SeizureRepository seizureRepository;
	
	@Autowired
	SeizedRepository seizedRepository;
	
	@Autowired
	SeizureBankAccountRepository seizureBankAccountRepository;
	
	@Autowired
	SeizedBankAccountRepository seizedBankAccountRepository;
	
	public void tratarFicheroDiligenciasEmbargo(File file) {
		
		BeanReader beanReader = null;
		
		try {
		
	        // create a StreamFactory
	        StreamFactory factory = StreamFactory.newInstance();
	        // load the mapping file
	        factory.loadResource(pathFileConfigAEAT);
	        
	        //Se guarda el registro de ControlFichero del fichero de entrada:
	        ControlFichero controlFicheroEmbargo = 
	        		fileControlMapper.generateControlFichero(file, EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_AEAT);
	        
	        fileControlRepository.save(controlFicheroEmbargo);
	
	        
	        // use a StreamFactory to create a BeanReader
	        beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_AEAT_DILIGENCIAS, file);
	        Object record = null;
	        boolean isEntidadTransmisoraCommerzbank = false;
	        
	        EntidadTransmisora entidadTransmisora = null;
	        EntidadCredito entidadCredito = null;
	        Diligencia diligencia = null;
	        
	        EntidadesOrdenante entidadOrdenante = null;
	        Date fechaObtencionFicheroOrganismo = null;
	        
	        while ((record = beanReader.read()) != null) {
	        	
	        	if(EmbargosConstants.RECORD_NAME_AEAT_ENTIDADTRANSMISORA.equals(beanReader.getRecordName())) {      	
	        		
	        		entidadTransmisora = (EntidadTransmisora) record;
	        		
	        		if (entidadTransmisora.getCodigoEntidadTransmisora() != null 
	        				&& entidadTransmisora.getCodigoEntidadTransmisora().intValue() == 159) {   			
	        			isEntidadTransmisoraCommerzbank = true;
	        		} else {
	        			isEntidadTransmisoraCommerzbank = false;
	        		}
	        	}
	        	
	        	if (isEntidadTransmisoraCommerzbank) {
	        	
		        	if(EmbargosConstants.RECORD_NAME_AEAT_ENTIDADCREDITO.equals(beanReader.getRecordName())) {
		            	
		        		entidadCredito = (EntidadCredito) record;
		        		
		        		String identificadorEntidad = entidadCredito.getDelegacionAgenciaEmisora()!=null ? Integer.toString(entidadCredito.getDelegacionAgenciaEmisora()) : null;
		        		
		        		entidadOrdenante = orderingEntityRepository.findByIdentificadorEntidad(identificadorEntidad);
		        		
		        		if (entidadOrdenante == null) {
		        			throw new ICEParserException("01", "No se puede procesar el fichero '" + file.getName() +
		        					"': Entidad Ordenante con identificadorEntidad " + identificadorEntidad + " no encontrada.");
		        		}
		        	}
		        	
		        	if(EmbargosConstants.RECORD_NAME_AEAT_DILIGENCIA.equals(beanReader.getRecordName())) {
		        		
		        		diligencia = (Diligencia) record;
		        		
		        		Embargo embargo = null;
		        		Traba traba = null;
		        		
		        		//- Se obtienen de Datawarehouse los datos del cliente y sus cuentas a partir del NIF del cliente:
		        		CustomerDTO customerDTO = customerService.findCustomerByNif(diligencia.getNifDeudor());
		        		
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
		        			
		        		embargo = aeatMapper.generateEmbargo(diligencia, controlFicheroEmbargo.getCodControlFichero(), entidadOrdenante, razonSocialInterna, fechaLimiteTraba);
		        			
		        		traba =  aeatMapper.generateTraba(diligencia, controlFicheroEmbargo.getCodControlFichero(), entidadOrdenante);        			
		        		traba.setEmbargo(embargo);
	
	       			        		
		        		//Guardar los datos del embargo y traba:
		        		seizureRepository.save(embargo);
		        		
		        		seizedRepository.save(traba);
		        		
		        		//- Se guardan las cuentas obtenidas del fichero de embargo en CUENTA_EMBARGO:
		        		for (CuentaEmbargo cuentaEmbargo : embargo.getCuentaEmbargos()) {
		        			
		        			//TODO mirar si se tiene que comprobar si es nulo el Iban o bien la "cuenta":
		        			if (cuentaEmbargo.getIban()!=null && !cuentaEmbargo.getIban().isEmpty()) {
		        				
		        				//Seteo de datos obtenidos de DWH en cuentaEmbargo:
		        				AccountDTO accountDTO = customerAccountsMap.get(cuentaEmbargo.getIban());     				
		        				if(accountDTO!=null) {
		        					cuentaEmbargo.setCuenta(accountDTO.getAccountNum());
		        				}
		        				
		        				seizureBankAccountRepository.save(cuentaEmbargo);
		        			}
		        		}
		        		
		        		
		        		//- Se guardan en CUENTA_TRABA las cuentas obtenidas de Datawarehouse:
		        		if(customerDTO!=null) {
		        			
		        			BigDecimal numeroOrdenCuenta = new BigDecimal(0);
		        			
		        			for (AccountDTO accountDTO : customerDTO.getBankAccounts()) {
			
		        					numeroOrdenCuenta = numeroOrdenCuenta.add(BigDecimal.valueOf(1));
		        					
		        					CuentaTraba cuentaTrabaExtra = seizedBankAccountMapper.accountDTOToCuentaTraba(accountDTO, numeroOrdenCuenta, traba);
		        					seizedBankAccountRepository.save(cuentaTrabaExtra);
		        			}
		        		}
		        	}       	
	        	}
	        }
	        
	        
	        //Datos a guardar en ControlFichero una vez procesado el fichero:
	        
			//- Se guarda el codigo de la Entidad comunicadora en ControlFichero:
	        EntidadesComunicadora entidadComunicadora = entidadOrdenante.getEntidadesComunicadora();
	        controlFicheroEmbargo.setEntidadesComunicadora(entidadOrdenante.getEntidadesComunicadora());
			
			//- Se guarda la fecha de la cabecera en el campo fechaCreacion de ControlFichero:
			BigDecimal fechaObtencionFicheroOrganismoBigDec = fechaObtencionFicheroOrganismo!=null ? ICEDateUtils.dateToBigDecimal(fechaObtencionFicheroOrganismo, ICEDateUtils.FORMAT_yyyyMMdd) : null;
			controlFicheroEmbargo.setFechaCreacion(fechaObtencionFicheroOrganismoBigDec);
			controlFicheroEmbargo.setFechaComienzoCiclo(fechaObtencionFicheroOrganismoBigDec);
	       
			//- Se guarda la fecha maxima de respuesta (now + dias de margen)
			long diasRespuestaF1 = entidadComunicadora.getDiasRespuestaF1()!=null ? entidadComunicadora.getDiasRespuestaF1().longValue() : 0;
			Date lastDateResponse = DateUtils.convertToDate(LocalDate.now().plusDays(diasRespuestaF1));
			BigDecimal limitResponseDate = ICEDateUtils.dateToBigDecimal(lastDateResponse, ICEDateUtils.FORMAT_yyyyMMdd);
			controlFicheroEmbargo.setFechaMaximaRespuesta(limitResponseDate);
			
			//Cambio de estado de CtrlFichero a: RECIBIDO
	        EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero();
	        EstadoCtrlficheroPK estadoCtrlficheroPK = new EstadoCtrlficheroPK();
	        estadoCtrlficheroPK.setCodEstado(EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_RECEIVED);
	        estadoCtrlficheroPK.setCodTipoFichero(EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_AEAT);
	        estadoCtrlfichero.setId(estadoCtrlficheroPK);
	        controlFicheroEmbargo.setEstadoCtrlfichero(estadoCtrlfichero);
			
	        //CALENDARIO:
	        // - Se agrega la tarea al calendario:
	        TaskAndEvent task = new TaskAndEvent();
	        task.setDescription("Embargo " + controlFicheroEmbargo.getNombreFichero());
	        task.setDate(ICEDateUtils.bigDecimalToDate(controlFicheroEmbargo.getFechaMaximaRespuesta(), ICEDateUtils.FORMAT_yyyyMMdd));
	        task.setCodCalendar(1L);
	        task.setType("T");
	        task.setCodOffice("1");
	        //
	        task.setAction("0");
	        task.setState("P");
	        task.setIndActive("Y");
	        Long codTarea = taskService.addCalendarTask(task);
	        
	        // - Se guarda el codigo de tarea del calendario:
	        controlFicheroEmbargo.setCodTarea(BigDecimal.valueOf(codTarea));
	        	        
			fileControlRepository.save(controlFicheroEmbargo);


		} catch (Exception e) {
	        
			throw e;
			
		} finally {
			
			if (beanReader!=null) {
				beanReader.close();
			}
		}
	
		
	}
	
	public void tratarFicheroLevantamientos(File file) {
		
	}
	
	public void tratarFicheroErrores(File file) {
		
	}

}
