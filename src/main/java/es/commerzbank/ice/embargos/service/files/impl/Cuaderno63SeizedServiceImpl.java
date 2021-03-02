package es.commerzbank.ice.embargos.service.files.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import es.commerzbank.ice.comun.lib.file.exchange.FileWriterHelper;
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

import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.service.TaskService;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.domain.dto.SeizureDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlfichero;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.embargos.domain.mapper.Cuaderno63Mapper;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.CabeceraEmisorFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.FinFicheroFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase4.CabeceraEmisorFase4;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase4.ComunicacionResultadoRetencionFase4;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase4.FinFicheroFase4;
import es.commerzbank.ice.embargos.repository.CommunicatingEntityRepository;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.SeizedBankAccountRepository;
import es.commerzbank.ice.embargos.repository.SeizureRepository;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.SeizureService;
import es.commerzbank.ice.embargos.service.files.Cuaderno63SeizedService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.ICEDateUtils;

@Service
public class Cuaderno63SeizedServiceImpl implements Cuaderno63SeizedService{

	private static final Logger LOG = LoggerFactory.getLogger(Cuaderno63SeizedServiceImpl.class);

	@Value("${commerzbank.embargos.beanio.config-path.cuaderno63}")
	String pathFileConfigCuaderno63;

	@Autowired
	private Cuaderno63Mapper cuaderno63Mapper;

	@Autowired
	private FileControlMapper fileControlMapper;

	@Autowired
	private FileControlService fileControlService;

	@Autowired
	private SeizureService seizureService;
	
	@Autowired
	private TaskService taskService;

	//Agregar repositories de DWH ...
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

	@Override
	@Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
	public void tramitarTrabas(Long codControlFicheroEmbargo, String usuarioTramitador) throws IOException, ICEException {

		BeanReader beanReader = null;
		BeanWriter beanWriter = null;
		
		Reader reader = null;
		Writer writer = null;
		
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
	        
	        String pathProcessed = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_PROCESSED);
	        
	        File ficheroEmbargo = new File(controlFicheroEmbargo.getRutaFichero());
	        
	        //Comprobar que el fichero de embargos exista:
	        if (!ficheroEmbargo.exists()) {
	        	throw new ICEException("ERROR: no se ha encontrado el fichero fisico de Embargos.");
	        }
	        
	        //Comprobar que el CRC del fichero de Embargos sea el mismo que el guardado en ControlFichero:
	        if (!controlFicheroEmbargo.getNumCrc().equals(es.commerzbank.ice.comun.lib.util.FileUtils.getMD5(ficheroEmbargo.getCanonicalPath()))){
	        	throw new ICEException("ERROR: el CRC del fichero de Embargos no coincide con el guardado en ControlFichero.");
	        }
	        
	        //Se actualiza el estado de controlFicheroEmbargo a Pendiente de envio:
	        EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero(
	        		EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_PENDING_TO_SEND,
	        		EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_NORMA63);
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
	        
	        
	        String pathGenerated = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_GENERATED);

			File ficheroSalida = fileWriterHelper.getGeneratedFile(pathGenerated, fileNameTrabas);
	        
	        //Se guarda el registro de ControlFichero del fichero de salida:
	        controlFicheroTrabas = 
	        		fileControlMapper.generateControlFichero(ficheroSalida, EmbargosConstants.COD_TIPO_FICHERO_TRABAS_NORMA63, fileNameTrabas, ficheroSalida);
	        
	        //Usuario que realiza la tramitacion:
	        controlFicheroTrabas.setUsuarioUltModificacion(usuarioTramitador);
	        controlFicheroTrabas.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
            
	        //fileControlRepository.save(controlFicheroTrabas);
	        fileControlService.saveFileControlTransaction(controlFicheroTrabas);
	                
	        //Creacion del BeanWriter para generar el fichero de salida:
	        String encoding = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_ENCODING_NORMA63);
			
	        writer = new OutputStreamWriter(new FileOutputStream(ficheroSalida), encoding);
	        beanWriter = factory.createWriter(EmbargosConstants.STREAM_NAME_CUADERNO63_FASE4, writer);
	        	        
	        //GENERACION DEL FICHERO DE TRABAS:    
	        // 1.- Leer la cabecera y el registro final del fichero de Embargos de fase 3:
	   
	        CabeceraEmisorFase3 cabeceraEmisorFase3 = null;
	        FinFicheroFase3 finFicheroFase3 = null;
	        
	        reader = new InputStreamReader(new FileInputStream(ficheroEmbargo), encoding);
	        beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_CUADERNO63_FASE3, reader);	
	        
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
	        	throw new ICEException("ERROR: No se ha encontrado el registro de cabecera del fichero de Embargos '"+ fileNameEmbargo + "'.");
	        }
	        
	        if (finFicheroFase3==null) {
	        	throw new ICEException("ERROR: No se ha encontrado el registro fin del fichero de Embargos '"+ fileNameEmbargo + "'.");
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
	        
	        BigDecimal importeTotalTrabado = new BigDecimal(0);
	        
	        for (Embargo embargo : embargoList) {
	        	
	    		Traba traba = null;
	    		
	    		if (embargo.getTrabas()!=null) {
	    			traba = embargo.getTrabas().get(0);
	    		} else {
	    			throw new ICEException("ERROR: el embargo no tiene una traba asociada.");
	    		}
	        	
	        	//Cuentas
	    		List<CuentaTraba> cuentaTrabaOrderedList = seizedBankAccountRepository.findAllByTrabaOrderByNumeroOrdenCuentaAsc(traba);
	        	
		        ComunicacionResultadoRetencionFase4 comunicacionResultadoRetencionFase4 = 
		        		cuaderno63Mapper.generateComunicacionResultadoRetencionFase4(embargo, traba, cuentaTrabaOrderedList);
		        
		        beanWriter.write(EmbargosConstants.RECORD_NAME_COMUNICACIONRESULTADORETENCION, comunicacionResultadoRetencionFase4);
		        numeroRegistrosFichero++;
	        	
		        importeTotalTrabado = importeTotalTrabado.add(traba.getImporteTrabado()!=null ? traba.getImporteTrabado() : BigDecimal.valueOf(0));
		        
		        beanWriter.flush();
	        } 

	        
	        // 4.- Generar FIN de fichero indicando el numero de registros:
	        numeroRegistrosFichero++;
	        FinFicheroFase4 finFicheroFase4 = cuaderno63Mapper.generateFinFicheroFase4(finFicheroFase3, numeroRegistrosFichero, importeTotalTrabado);
	        beanWriter.write(EmbargosConstants.RECORD_NAME_FINFICHERO, finFicheroFase4);

	        beanWriter.flush();
	
	        //ACTUALIZACIONES DEL FICHERO DE SALIDA (controlFicheroTrabas):
	        //1.- Se actualiza el CRC:
	        controlFicheroTrabas.setNumCrc(es.commerzbank.ice.comun.lib.util.FileUtils.getMD5(ficheroSalida.getCanonicalPath()));

			//2.- Se guarda el codigo de la Entidad comunicadora:
	        controlFicheroTrabas.setEntidadesComunicadora(entidadComunicadora);
	        
	        //3.- Se actualiza el estado del fichero de Trabas a GENERADO:     
	        estadoCtrlfichero = new EstadoCtrlfichero(
	        				EmbargosConstants.COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_NORMA63_GENERATED,
	        				EmbargosConstants.COD_TIPO_FICHERO_TRABAS_NORMA63);
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
    				EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_GENERATED,
    				EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_NORMA63);
	        controlFicheroEmbargo.setEstadoCtrlfichero(estadoCtrlfichero);
	        
	        controlFicheroEmbargo.setUsuarioUltModificacion(usuarioTramitador);
	        controlFicheroEmbargo.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
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

			// Mover a outbox
			String outboxGenerated = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_OUTBOX);
			fileWriterHelper.transferToOutbox(ficheroSalida, outboxGenerated, fileNameTrabas);
	        
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
		}
		
	}
}
