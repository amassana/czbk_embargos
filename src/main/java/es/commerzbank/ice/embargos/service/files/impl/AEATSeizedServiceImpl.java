package es.commerzbank.ice.embargos.service.files.impl;

import es.commerzbank.ice.comun.lib.domain.dto.Element;
import es.commerzbank.ice.comun.lib.domain.dto.TaskAndEvent;
import es.commerzbank.ice.comun.lib.file.exchange.FileWriterHelper;
import es.commerzbank.ice.comun.lib.service.FestiveService;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.service.TaskService;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.domain.dto.SeizureDTO;
import es.commerzbank.ice.embargos.domain.entity.*;
import es.commerzbank.ice.embargos.domain.mapper.AEATMapper;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.EntidadCreditoFase3;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.EntidadTransmisoraFase3;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.FinEntidadCreditoFase3;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.FinEntidadTransmisoraFase3;
import es.commerzbank.ice.embargos.formats.aeat.trabas.*;
import es.commerzbank.ice.embargos.repository.*;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.SeizureService;
import es.commerzbank.ice.embargos.service.files.AEATSeizedService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.ICEDateUtils;
import org.beanio.BeanReader;
import org.beanio.BeanWriter;
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
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AEATSeizedServiceImpl implements AEATSeizedService{

	private static final Logger logger = LoggerFactory.getLogger(AEATSeizedServiceImpl.class);
	
	@Value("${commerzbank.embargos.beanio.config-path.aeat}")
	String pathFileConfigAEAT;
			
	@Autowired
	private FestiveService festiveService;
	
	@Autowired
	private AEATMapper aeatMapper;
	
	@Autowired
	private FileControlMapper fileControlMapper;
	
	@Autowired
	private SeizureService seizureService;
	
	@Autowired
	private TaskService taskService;

	@Autowired
	private FileControlService fileControlService;
	
	@Autowired
	private FileControlRepository fileControlRepository;
	
	@Autowired
	private SeizureRepository seizureRepository;
	
	@Autowired
	private SeizedBankAccountRepository seizedBankAccountRepository;
	
	@Autowired
	private CommunicatingEntityRepository communicatingEntityRepository;

	@Autowired
	private GeneralParametersService generalParametersService;

	@Autowired
	private FileWriterHelper fileWriterHelper;
	
	@Autowired
	private SeizedRepository seizedRepository;
	
	@Override
	@Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
	public void tramitarTrabas(Long codControlFicheroEmbargo, String usuarioTramitador) throws Exception {
		logger.info("AEATSeizureServiceImpl - tramitarTrabas - start");
		
		BeanReader beanReader = null;
		BeanWriter beanWriter = null;
		FileInputStream fileInputStream = null;
		
		Reader reader = null;
		Writer writer = null;
		
		ControlFichero controlFicheroEmbargo = null;
		ControlFichero controlFicheroTrabas = null;
		
		try {
			boolean tieneTrabasRealizadas = false;

			// create a StreamFactory
	        StreamFactory factory = StreamFactory.newInstance();
	        // load the mapping file
	        factory.loadResource(pathFileConfigAEAT);        
	        
	        //Se obtiene el ControlFichero de Embargos:
	        controlFicheroEmbargo = fileControlRepository.getOne(codControlFicheroEmbargo);

	        //Fichero de embargos:
	        String fileNameEmbargo = controlFicheroEmbargo.getNombreFichero();

	        String pathProcessed = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_AEAT_PROCESSED);
	        
	        File ficheroEmbargo = new File(controlFicheroEmbargo.getRutaFichero());
	        
	        //Comprobar que el fichero de embargos exista:
	        if (!ficheroEmbargo.exists()) {
	        	throw new ICEException("ERROR: no se ha encontrado el fichero fisico de Embargos.");
	        }
	        
	        //Comprobar que el CRC del fichero de Embargos sea el mismo que el guardado en ControlFichero:
	        if (!controlFicheroEmbargo.getNumCrc().equals(es.commerzbank.ice.comun.lib.util.FileUtils.getMD5(ficheroEmbargo.getCanonicalPath()))){
	        	throw new ICEException("ERROR: el CRC del fichero de Embargos no coincide con el guardado en ControlFichero.");
	        }
	        
	        //Comprobar que el fichero está en el estado adecuado
	        if (controlFicheroEmbargo.getEstadoCtrlfichero().getId().getCodEstado()!=EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PENDING_TO_SEND  &&
	        		controlFicheroEmbargo.getEstadoCtrlfichero().getId().getCodEstado()!=EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_RECEIVED) {
	        	throw new ICEException("ERROR estado no adecuado");
	        }
	        
	        //Se actualiza el estado de controlFicheroEmbargo a Pendiente de envio:
	        EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero(
	        		EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PENDING_TO_SEND,
	        		EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_AEAT);
	        controlFicheroEmbargo.setEstadoCtrlfichero(estadoCtrlfichero);
	        	        
	        controlFicheroEmbargo.setUsuarioUltModificacion(usuarioTramitador);
	        controlFicheroEmbargo.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
            
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
		        	throw new ICEException("ERROR: no se ha encontrado la entidad comunicadora asociada al codControlFichero=" + controlFicheroEmbargo.getCodControlFichero());
			
				}
	        }
	        // - se obtiene la fecha local actual:
	        LocalDate localDate = LocalDate.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	        String fechaNombreFichero = localDate.format(formatter);
	        String fileNameTrabas = prefijoFichero + EmbargosConstants.SEPARADOR_GUION_BAJO + fechaNombreFichero 
	            	+ EmbargosConstants.SEPARADOR_PUNTO + EmbargosConstants.TIPO_FICHERO_TRABAS;
	        
	        String pathGenerated = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_AEAT_GENERATED);
	        
	        File ficheroSalida = fileWriterHelper.getGeneratedFile(pathGenerated, fileNameTrabas);
	        
	        //Se guarda el registro de ControlFichero del fichero de salida:
	        controlFicheroTrabas = 
	        		fileControlMapper.generateControlFichero(ficheroSalida, EmbargosConstants.COD_TIPO_FICHERO_TRABAS_AEAT, fileNameTrabas, ficheroSalida);
	        
	        //Usuario que realiza la tramitacion:
	        controlFicheroTrabas.setUsuarioUltModificacion(usuarioTramitador);
	        controlFicheroTrabas.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
	        
	        //fileControlRepository.save(controlFicheroTrabas);
	        fileControlService.saveFileControlTransaction(controlFicheroTrabas);
	                
	        // use a StreamFactory to create a BeanWriter
	        String encoding = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_ENCODING_AEAT);
			
	        writer = new OutputStreamWriter(new FileOutputStream(ficheroSalida), encoding);
	        beanWriter = factory.createWriter(EmbargosConstants.STREAM_NAME_AEAT_TRABAS, writer);
	        	        
	        //GENERACION DEL FICHERO DE TRABAS:    
	        // 1.- Leer las cabeceras y el registros finales del fichero de diligencias de Embargos de fase 3:
	   
	        EntidadTransmisoraFase3 entidadTransmisoraFase3 = null;
	        EntidadCreditoFase3 entidadCreditoFase3 = null;
	        FinEntidadCreditoFase3 finEntidadCreditoFase3 = null;
	        FinEntidadTransmisoraFase3 finEntidadTransmisoraFase3 = null;
	        
	        fileInputStream = new FileInputStream(ficheroEmbargo);
	        reader = new InputStreamReader(fileInputStream, encoding);
	        beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_AEAT_DILIGENCIAS, reader);	
	        
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
	        	throw new ICEException("ERROR: No se ha encontrado el registro de entidadTransmisora del fichero de Embargos '"+ fileNameEmbargo + "'.");
	        }
	        
	        if (entidadCreditoFase3==null) {
	        	throw new ICEException("ERROR: No se ha encontrado el registro entidadCredito del fichero de Embargos '"+ fileNameEmbargo + "'.");
	        }
	        
	        if (finEntidadCreditoFase3==null) {
	        	throw new ICEException("ERROR: No se ha encontrado el registro finEntidadCredito del fichero de Embargos '"+ fileNameEmbargo + "'.");
	        }
	        
	        if (finEntidadTransmisoraFase3==null) {
	        	throw new ICEException("ERROR: No se ha encontrado el registro finEntidadTransmisora del fichero de Embargos '"+ fileNameEmbargo + "'.");
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

			//- Se guarda la fecha maxima de respuesta fase6 (now + dias de margen)
			//- La fecha base es la fecha de contabilización. Se toma la menor fecha de contabilización (por si hubiera cambios de estado y similares)

			LocalDate fechaTraba = null;
			for (Embargo embargo : embargoList) {
				Traba traba = embargo.getTrabas().get(0);

				if (traba.getEstadoTraba().getCodEstado() != EmbargosConstants.COD_ESTADO_TRABA_CONTABILIZADA)
					continue;

				tieneTrabasRealizadas = true;

				if (traba.getFechaTraba() == null)
					continue;

				LocalDate fechaTrabaActual = es.commerzbank.ice.comun.lib.typeutils.ICEDateUtils.numericDateToLocalDate(traba.getFechaTraba());

				if (fechaTraba == null) {
					fechaTraba = fechaTrabaActual;
				}

				if (fechaTrabaActual.isBefore(fechaTraba)) {
					fechaTraba = fechaTrabaActual;
				}
			}

			Date lastDateResponse = null;

			if (tieneTrabasRealizadas) {
				// Para la Agencia Tributaria la el paso de Embargos a Impuestos se produce
				// 21 días naturales desde el día siguiente a la realización de la traba (cuando se adeuda la traba).
				int diasRespuestaF6 = entidadComunicadora.getDiasRespuestaF6() != null ? entidadComunicadora.getDiasRespuestaF6().intValue() : 0;
				lastDateResponse = Date.from(fechaTraba.plusDays(diasRespuestaF6).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			}  else {
				lastDateResponse = new Date();
			}

	        for (Embargo embargo : embargoList) {
	        	
	    		Traba traba = null;
	    		
	    		if (embargo.getTrabas()!=null) {
	    			traba = embargo.getTrabas().get(0);
	    		} else {
	    			throw new ICEException("ERROR: el embargo no tiene una traba asociada.");
	    		}
	        	
	        	//Cuentas
	    		List<CuentaTraba> cuentaTrabaOrderedList = seizedBankAccountRepository.findAllByTrabaOrderByNumeroOrdenCuentaAsc(traba);
	        	
		        TrabaFase4 trabaFase4 = 
		        		aeatMapper.generateTrabaFase4(embargo, traba, cuentaTrabaOrderedList, lastDateResponse);
		        
		        beanWriter.write(EmbargosConstants.RECORD_NAME_AEAT_TRABA, trabaFase4);
		        numeroRegistrosFichero++;
		        
		        importeTotalTrabado = importeTotalTrabado.add(traba.getImporteTrabado()!=null ? traba.getImporteTrabado() : BigDecimal.valueOf(0));
	        	
		        beanWriter.flush();
		        
		        // Actualizar estado traba a finalizado
				EstadoTraba estadoTraba = new EstadoTraba();
				estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_FINALIZADA);
		        traba.setEstadoTraba(estadoTraba);
		        traba.setUsuarioUltModificacion(usuarioTramitador);
		        traba.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
		        seizedRepository.save(traba);
		        
		        // Actualizar el estado de las cuenta traba a finalizado
		        if (cuentaTrabaOrderedList!=null && cuentaTrabaOrderedList.size()>0) {
		        	for (CuentaTraba cuentaTraba : cuentaTrabaOrderedList) {
						cuentaTraba.setEstadoTraba(estadoTraba);
						cuentaTraba.setUsuarioUltModificacion(usuarioTramitador);
						cuentaTraba.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
						seizedBankAccountRepository.save(cuentaTraba);		        		
		        	}
		        }
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
	        controlFicheroTrabas.setNumCrc(es.commerzbank.ice.comun.lib.util.FileUtils.getMD5(ficheroSalida.getCanonicalPath()));

			//2.- Se guarda el codigo de la Entidad comunicadora:
	        controlFicheroTrabas.setEntidadesComunicadora(entidadComunicadora);
	        
	        //3.- Se actualiza el estado del fichero de Trabas a GENERADO:
	        estadoCtrlfichero = new EstadoCtrlfichero(
	        		EmbargosConstants.COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_AEAT_GENERATED,
	        		EmbargosConstants.COD_TIPO_FICHERO_TRABAS_AEAT);
	        controlFicheroTrabas.setEstadoCtrlfichero(estadoCtrlfichero);
	        
	        //4.- Actualizacion del fichero de origen:
	        controlFicheroTrabas.setControlFicheroOrigen(controlFicheroEmbargo);
	        
	        controlFicheroTrabas.setUsuarioUltModificacion(usuarioTramitador);
	        controlFicheroTrabas.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
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

			// Se marca como pendiente el envío de cartas.
			controlFichero.setIndEnvioCarta(EmbargosConstants.IND_FLAG_NO);
	        
	        controlFicheroEmbargo.setUsuarioUltModificacion(usuarioTramitador);
	        controlFicheroEmbargo.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
	        controlFicheroEmbargo.setFechaGeneracionRespuesta(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
	        fileControlRepository.save(controlFicheroEmbargo);
        
	        //Cerrar la tarea:
	        boolean closed = false;
	        if (controlFicheroEmbargo.getCodTarea()!=null) {
	        	
	        	Long codTarea = controlFicheroEmbargo.getCodTarea().longValue();	        	
	        	closed = taskService.closeCalendarTask(codTarea);
		      
	        	if(!closed) {
	        		logger.error("ERROR: No se ha cerrado la Tarea");
		        	//TODO: lanzar excepcion si no se ha cerrado la tarea
		        }
	        } else {
	        	logger.error("ERROR al cerrar la tarea: No se ha encontrado el codigo de la Tarea");
	        	//TODO: lanzar excepcion si no se ha encontrado el codigo de tarea
	        }

	        // Mover a outbox
			String outboxGenerated = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_AEAT_OUTBOX);
			fileWriterHelper.transferToOutbox(ficheroSalida, outboxGenerated, fileNameTrabas);

			//CALENDARIO:
			// - Se agrega la tarea al calendario:
			if (tieneTrabasRealizadas) {
				TaskAndEvent task = new TaskAndEvent();
				task.setDescription("Fase 6 programada " + controlFicheroEmbargo.getNombreFichero());
				task.setDate(lastDateResponse);
				task.setCodCalendar(1L);
				task.setType("T");
				Element office = new Element();
				office.setCode(1L);
				task.setOffice(office);
				//
				task.setAction("0");
				task.setStatus("P");
				task.setIndActive(true);
				task.setExternalId(EmbargosConstants.EXTERNAL_ID_F6_AEAT + controlFicheroTrabas.getCodControlFichero());
				task.setApplication(EmbargosConstants.ID_APLICACION_EMBARGOS);
				Long codTarea = taskService.addCalendarTask(task);

				// - Se guarda el codigo de tarea del calendario:
				controlFicheroTrabas.setCodTarea(BigDecimal.valueOf(codTarea));
			}

	        controlFicheroTrabas.setControlFicheroOrigen(controlFicheroEmbargo);
	        controlFicheroTrabas.setUsuarioUltModificacion(usuarioTramitador);
	        controlFicheroTrabas.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
			fileControlRepository.save(controlFicheroTrabas);
			
		} catch (Exception e) {
			
			//TODO Estado de ERROR pendiente de ser eliminado, se comenta:
			/*
			//Transaccion para cambiar el estado de controlFicheroEmbargo a ERROR:
			fileControlService.updateFileControlStatusTransaction(controlFicheroEmbargo, 
					EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_ERROR);
			
			
			//Transaccion para cambiar el estado de controlFicheroTrabas a ERROR:
			fileControlService.updateFileControlStatusTransaction(controlFicheroTrabas, 
					EmbargosConstants.COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_AEAT_ERROR);
			*/
			
			logger.error("ERROR: ", e);
			
			throw e;
			
		} finally {

			if(reader!=null) {
				reader.close();
			}
			if (beanReader != null) {
				beanReader.close();
			}
			if(writer!=null) {
				writer.close();
			}
			if (beanWriter != null) {
				beanWriter.close();
			}
			if (fileInputStream!=null) fileInputStream.close();
		}
		
	}

}
