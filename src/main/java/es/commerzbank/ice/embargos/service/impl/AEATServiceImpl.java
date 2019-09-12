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
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.comun.lib.domain.dto.TaskAndEvent;
import es.commerzbank.ice.comun.lib.service.TaskService;
import es.commerzbank.ice.comun.lib.typeutils.DateUtils;
import es.commerzbank.ice.comun.lib.util.ICEParserException;
import es.commerzbank.ice.datawarehouse.domain.dto.AccountDTO;
import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureDTO;
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
import es.commerzbank.ice.embargos.formats.aeat.diligencias.DiligenciaFase3;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.EntidadCreditoFase3;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.EntidadTransmisoraFase3;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.FinEntidadCreditoFase3;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.FinEntidadTransmisoraFase3;
import es.commerzbank.ice.embargos.formats.aeat.trabas.EntidadCreditoFase4;
import es.commerzbank.ice.embargos.formats.aeat.trabas.EntidadTransmisoraFase4;
import es.commerzbank.ice.embargos.formats.aeat.trabas.FinEntidadCreditoFase4;
import es.commerzbank.ice.embargos.formats.aeat.trabas.FinEntidadTransmisoraFase4;
import es.commerzbank.ice.embargos.formats.aeat.trabas.TrabaFase4;
import es.commerzbank.ice.embargos.repository.CommunicatingEntityRepository;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.InformationPetitionRepository;
import es.commerzbank.ice.embargos.repository.OrderingEntityRepository;
import es.commerzbank.ice.embargos.repository.SeizedBankAccountRepository;
import es.commerzbank.ice.embargos.repository.SeizedRepository;
import es.commerzbank.ice.embargos.repository.SeizureBankAccountRepository;
import es.commerzbank.ice.embargos.repository.SeizureRepository;
import es.commerzbank.ice.embargos.service.AEATService;
import es.commerzbank.ice.embargos.service.CustomerService;
import es.commerzbank.ice.embargos.service.SeizureService;
import es.commerzbank.ice.utils.EmbargosConstants;
import es.commerzbank.ice.utils.EmbargosUtils;
import es.commerzbank.ice.utils.ICEDateUtils;

@Service
@Transactional(transactionManager="transactionManager")
public class AEATServiceImpl implements AEATService{

	private static final Logger LOG = LoggerFactory.getLogger(AEATServiceImpl.class);
	
	@Value("${commerzbank.embargos.beanio.config-path.aeat}")
	String pathFileConfigAEAT;
	
	@Value("${commerzbank.embargos.files.path.processed}")
	private String pathProcessed;
	
	@Value("${commerzbank.embargos.files.path.generated}")
	private String pathGenerated;
	
	@Autowired
	AEATMapper aeatMapper;
	
	@Autowired
	FileControlMapper fileControlMapper;
	
	@Autowired
	SeizureService seizureService;
	
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
	
	@Autowired
	CommunicatingEntityRepository communicatingEntityRepository;
	
	@Override
	public void tratarFicheroDiligenciasEmbargo(File file) throws IOException, ICEParserException{
		
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
		        			throw new ICEParserException("01", "No se puede procesar el fichero '" + file.getName() +
		        					"': Entidad Ordenante con identificadorEntidad " + identificadorEntidad + " no encontrada.");
		        		}
		        	}
		        	
		        	if(EmbargosConstants.RECORD_NAME_AEAT_DILIGENCIA.equals(beanReader.getRecordName())) {
		        		
		        		diligenciaFase3 = (DiligenciaFase3) record;
		        		
		        		Embargo embargo = null;
		        		Traba traba = null;
		        		
		        		//- Se obtienen de Datawarehouse los datos del cliente y sus cuentas a partir del NIF del cliente:
		        		CustomerDTO customerDTO = customerService.findCustomerByNif(diligenciaFase3.getNifDeudor());
		        		
		        		//- Se almacenan las cuentas obtenidas de Datawarehouse en una Hash donde la key es el AccountNum:
		        		Map<String, AccountDTO> customerAccountsMap = new HashMap<>(); 
		        		if (customerDTO!=null) {
			        		for (AccountDTO accountDTO : customerDTO.getBankAccounts()) {    		
			        			customerAccountsMap.put(accountDTO.getAccountNum(), accountDTO);
			        		}
		        		}
		        				        		
		        		//Razon social interna (obtenida del customerDTO de datawarehouse):
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
			long diasRespuestaF3 = entidadComunicadora.getDiasRespuestaF3()!=null ? entidadComunicadora.getDiasRespuestaF3().longValue() : 0;
			Date lastDateResponse = DateUtils.convertToDate(LocalDate.now().plusDays(diasRespuestaF3));
			BigDecimal limitResponseDate = ICEDateUtils.dateToBigDecimal(lastDateResponse, ICEDateUtils.FORMAT_yyyyMMdd);
			controlFicheroEmbargo.setFechaMaximaRespuesta(limitResponseDate);
			
			//Cambio de estado de CtrlFichero a: RECIBIDO
	        EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero(
	        		EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_RECEIVED,
	        		EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_AEAT);
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

	@Override
	public void tramitarTrabas(Long codControlFicheroEmbargo, String usuarioTramitador) throws IOException, ICEParserException {

		BeanReader beanReader = null;
		BeanWriter beanWriter = null;
		
		try {
		
			// create a StreamFactory
	        StreamFactory factory = StreamFactory.newInstance();
	        // load the mapping file
	        factory.loadResource(pathFileConfigAEAT);        
	        
	        //Se obtiene el ControlFichero de Embargos:
	        ControlFichero controlFicheroEmbargo = fileControlRepository.getOne(codControlFicheroEmbargo);
	        
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
	        ControlFichero controlFicheroTrabas = 
	        		fileControlMapper.generateControlFichero(ficheroSalida, EmbargosConstants.COD_TIPO_FICHERO_TRABAS_AEAT);
	        
	        //Usuario que realiza la tramitacion:
	        controlFicheroTrabas.setUsuarioUltModificacion(usuarioTramitador);
	        	        
	        fileControlRepository.save(controlFicheroTrabas);
	                
	        // use a StreamFactory to create a BeanReader
	        beanWriter = factory.createWriter(EmbargosConstants.STREAM_NAME_AEAT_TRABAS, ficheroSalida);
	        	        
	        //GENERACION DEL FICHERO DE TRABAS:    
	        // 1.- Leer las cabeceras y el registros finales del fichero de diligencias de Embargos de fase 3:
	   
	        EntidadTransmisoraFase3 entidadTransmisoraFase3 = null;
	        EntidadCreditoFase3 entidadCreditoFase3 = null;
	        FinEntidadCreditoFase3 finEntidadCreditoFase3 = null;
	        FinEntidadTransmisoraFase3 finEntidadTransmisoraFase3 = null;
	        
	        beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_AEAT_DILIGENCIAS, ficheroEmbargo);	
	        
	        Object record = null; 
	        while ((record = beanReader.read()) != null) {
		        if(EmbargosConstants.RECORD_NAME_AEAT_ENTIDADTRANSMISORA.equals(beanReader.getRecordName())) {
		        
		        	entidadTransmisoraFase3 = (EntidadTransmisoraFase3) record;
		        }
		        
		        if(EmbargosConstants.RECORD_NAME_AEAT_ENTIDADCREDITO.equals(beanReader.getRecordName())) {
			        
		        	entidadCreditoFase3 = (EntidadCreditoFase3) record;
		        }
		        
		        if(EmbargosConstants.RECORD_NAME_AEAT_FINENTIDADCREDITO.equals(beanReader.getRecordName())) {
			        
		        	finEntidadCreditoFase3 = (FinEntidadCreditoFase3) record;
		        }
		        
		        if(EmbargosConstants.RECORD_NAME_AEAT_FINENTIDADTRANSMISORA.equals(beanReader.getRecordName())) {
			        
		        	finEntidadTransmisoraFase3 = (FinEntidadTransmisoraFase3) record;
		        }
	        }
		        
	        if (entidadTransmisoraFase3==null) {
	        	throw new ICEParserException("11","ERROR: No se ha encontrado el registro de entidadTransmisora del fichero de Embargos '"+ fileNameEmbargo + "'.");
	        }
	        
	        if (entidadCreditoFase3==null) {
	        	throw new ICEParserException("12","ERROR: No se ha encontrado el registro entidadCredito del fichero de Embargos '"+ fileNameEmbargo + "'.");
	        }
	        
	        if (finEntidadCreditoFase3==null) {
	        	throw new ICEParserException("12","ERROR: No se ha encontrado el registro finEntidadCredito del fichero de Embargos '"+ fileNameEmbargo + "'.");
	        }
	        
	        if (finEntidadTransmisoraFase3==null) {
	        	throw new ICEParserException("12","ERROR: No se ha encontrado el registro finEntidadTransmisora del fichero de Embargos '"+ fileNameEmbargo + "'.");
	        }
	        
	        int numeroRegistrosFichero = 0;
	        
	        // 2.- Generar y escribir registro de CABECERA de fase 4:
	        EntidadTransmisoraFase4 entidadTransmisoraFase4 = aeatMapper.generateEntidadTransmisoraFase4(entidadTransmisoraFase3);
	        beanWriter.write(EmbargosConstants.RECORD_NAME_AEAT_ENTIDADTRANSMISORA, entidadTransmisoraFase4);
	        numeroRegistrosFichero++;
	        
	        // 2.- Generar y escribir registro de CABECERA de fase 4:
	        EntidadCreditoFase4 entidadCreditoFase4 = aeatMapper.generateEntidadCreditoFase4(entidadCreditoFase3);
	        beanWriter.write(EmbargosConstants.RECORD_NAME_AEAT_ENTIDADCREDITO, entidadCreditoFase4);
	        numeroRegistrosFichero++;
	        
	        // 3.- Generar registros del DETALLE:
	        
	        //Se recuperan los embargos/trabas y sus cuentas asociadas:
	        List<SeizureDTO> seizureDTOList = seizureService.getSeizureListByCodeFileControl(codControlFicheroEmbargo);
	        
			ControlFichero controlFichero = new ControlFichero();
			controlFichero.setCodControlFichero(codControlFicheroEmbargo);
			
	        List<Embargo> embargoList = seizureRepository.findAllByControlFichero(controlFichero);
	        
	        BigDecimal importeTotalTrabado = new BigDecimal(0);
	        
	        for (Embargo embargo : embargoList) {
	        	
	    		Traba traba = null;
	    		
	    		if (embargo.getTrabas()!=null) {
	    			traba = embargo.getTrabas().get(0);
	    		} else {
	    			throw new ICEParserException("", "ERROR: el embargo no tiene una traba asociada.");
	    		}
	        	
	        	//Cuentas
	    		List<CuentaTraba> cuentaTrabaOrderedList = seizedBankAccountRepository.findAllByTrabaOrderByNumeroOrdenCuentaAsc(traba);
	        	
		        TrabaFase4 trabaFase4 = 
		        		aeatMapper.generateTrabaFase4(embargo, traba, cuentaTrabaOrderedList);
		        
		        beanWriter.write(EmbargosConstants.RECORD_NAME_AEAT_TRABA, trabaFase4);
		        numeroRegistrosFichero++;
		        
		        importeTotalTrabado = importeTotalTrabado.add(traba.getImporteTrabado()!=null ? traba.getImporteTrabado() : BigDecimal.valueOf(0));
	        	
		        beanWriter.flush();
	        } 

	        
	        // 4.- Generar FIN de fichero indicando el numero de registros:

	        // 2.- Generar y escribir registro de CABECERA de fase 4:
	        numeroRegistrosFichero++;
	        FinEntidadCreditoFase4 finEntidadCreditoFase4 = aeatMapper.generateFinEntidadCreditoFase4(finEntidadCreditoFase3, importeTotalTrabado);
	        beanWriter.write(EmbargosConstants.RECORD_NAME_AEAT_FINENTIDADCREDITO, finEntidadCreditoFase4);
	        
	        // 2.- Generar y escribir registro de CABECERA de fase 4:
	        FinEntidadTransmisoraFase4 finEntidadTransmisoraFase4 = aeatMapper.generateFinEntidadTransmisoraFase4(finEntidadTransmisoraFase3);
	        beanWriter.write(EmbargosConstants.RECORD_NAME_AEAT_FINENTIDADTRANSMISORA, finEntidadTransmisoraFase4);


	        beanWriter.flush();
	
	        //ACTUALIZACIONES DEL FICHERO DE SALIDA (controlFicheroTrabas):
	        //1.- Se actualiza el CRC:
	        controlFicheroTrabas.setNumCrc(Long.toString(FileUtils.checksumCRC32(ficheroSalida)));

			//2.- Se guarda el codigo de la Entidad comunicadora:
	        controlFicheroTrabas.setEntidadesComunicadora(entidadComunicadora);
	        
	        //3.- Se actualiza el estado del fichero de Trabas a GENERADO:
	        EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero(
	        		EmbargosConstants.COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_AEAT_GENERATED,
	        		EmbargosConstants.COD_TIPO_FICHERO_TRABAS_AEAT);
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
	        		EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_GENERATED,
	        		EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_AEAT);
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
			
			LOG.error("ERROR: ", e);
			throw e;
			
		} finally {

			if (beanWriter!=null) {
				beanWriter.close();
			}
		}
		
	}
	
	@Override
	public void tratarFicheroLevantamientos(File file) {
		
	}
	@Override
	public void tratarFicheroErrores(File file) {
		
	}


}
