package es.commerzbank.ice.embargos.service.files.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlfichero;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacion;
import es.commerzbank.ice.embargos.domain.mapper.Cuaderno63Mapper;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.CabeceraEmisorFase1;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.FinFicheroFase1;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.SolicitudInformacionFase1;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase2.CabeceraEmisorFase2;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase2.FinFicheroFase2;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase2.RespuestaSolicitudInformacionFase2;
import es.commerzbank.ice.embargos.repository.CommunicatingEntityRepository;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.InformationPetitionRepository;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.files.Cuaderno63InformationService;
import es.commerzbank.ice.utils.EmbargosConstants;
import es.commerzbank.ice.utils.EmbargosUtils;

@Service
@Transactional(transactionManager="transactionManager", propagation = Propagation.REQUIRES_NEW)
public class Cuaderno63InformationServiceImpl implements Cuaderno63InformationService{

	private static final Logger LOG = LoggerFactory.getLogger(Cuaderno63InformationServiceImpl.class);
	
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
	private TaskService taskService;

	//Agregar repositories de DWH ...
	@Autowired
	private FileControlRepository fileControlRepository;

	@Autowired
	private InformationPetitionRepository informationPetitionRepository;

	@Autowired
	private CommunicatingEntityRepository communicatingEntityRepository;
	

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

}