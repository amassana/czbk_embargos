package es.commerzbank.ice.embargos.service.files.impl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

import es.commerzbank.ice.comun.lib.service.TaskService;
import es.commerzbank.ice.comun.lib.util.ICEParserException;
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
import es.commerzbank.ice.utils.EmbargosConstants;

@Service
@Transactional(transactionManager="transactionManager", propagation = Propagation.REQUIRES_NEW)
public class Cuaderno63SeizedServiceImpl implements Cuaderno63SeizedService{

	private static final Logger LOG = LoggerFactory.getLogger(Cuaderno63SeizedServiceImpl.class);

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