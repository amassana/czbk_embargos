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
import java.util.Date;
import java.util.HashMap;
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
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.EmbargosUtils;
import es.commerzbank.ice.embargos.utils.ICEDateUtils;

@Service
public class Cuaderno63InformationServiceImpl implements Cuaderno63InformationService{

	private static final Logger LOG = LoggerFactory.getLogger(Cuaderno63InformationServiceImpl.class);
	
	@Value("${commerzbank.embargos.beanio.config-path.cuaderno63}")
	String pathFileConfigCuaderno63;

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
	
	@Autowired
	private GeneralParametersService generalParametersService;

	@Autowired
	private FileWriterHelper fileWriterHelper;

	@Override
	@Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
	public void tramitarFicheroInformacion(Long codControlFicheroPeticion, String usuarioTramitador) throws IOException, ICEException {
			
		
		BeanReader beanReader = null;
		BeanWriter beanWriter = null;
		
		Reader reader = null;
		Writer writer = null;
		
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
	        File ficheroEntrada = new File(controlFicheroPeticion.getRutaFichero());
	        
	        //Comprobar que el fichero de peticiones exista:
	        if (!ficheroEntrada.exists()) {
	        	throw new ICEException("ERROR: no se ha encontrado el fichero fisico de Embargos.");
	        }
	        
	        //Comprobar que el CRC del fichero de Peticiones sea el mismo que el guardado en ControlFichero:
	        if (!controlFicheroPeticion.getNumCrc().equals(es.commerzbank.ice.comun.lib.util.FileUtils.getMD5(ficheroEntrada.getCanonicalPath()))) {
	        	throw new ICEException("ERROR: el CRC del fichero de Embargos no coincide con el guardado en ControlFichero.");
	        }     
	        
	        //Se actualiza el estado de controlFicheroPeticion a TRAMITANDO:
			/*
	        EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero(
	        		EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_INFORMACION_NORMA63_PROCESSING,
	        		EmbargosConstants.COD_TIPO_FICHERO_PETICION_INFORMACION_NORMA63);
	        controlFicheroPeticion.setEstadoCtrlfichero(estadoCtrlfichero);
	        */
	        
	        //Actualizacion del flag IND_PROCESADO al iniciar la tramitacion:
	        controlFicheroPeticion.setIndProcesado(EmbargosConstants.IND_FLAG_SI);
	        
	        controlFicheroPeticion.setUsuarioUltModificacion(usuarioTramitador);
	        controlFicheroPeticion.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
            
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
	            	+ EmbargosConstants.SEPARADOR_PUNTO + EmbargosConstants.TIPO_FICHERO_INFORMACION;
	        
	        String pathGenerated = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_GENERATED);
	        
	        File ficheroSalida = fileWriterHelper.getGeneratedFile(pathGenerated, fileNameInformacion);
	        
	        //Se guarda el registro de ControlFichero del fichero de salida:
	        controlFicheroInformacion = 
	        		fileControlMapper.generateControlFichero(ficheroSalida, EmbargosConstants.COD_TIPO_FICHERO_ENVIO_INFORMACION_NORMA63, fileNameInformacion, ficheroSalida);
	        
	        //Usuario que realiza la tramitacion:
	        controlFicheroInformacion.setUsuarioUltModificacion(usuarioTramitador);
	        controlFicheroInformacion.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
	        
	        //fileControlRepository.save(controlFicheroInformacion);
	        fileControlService.saveFileControlTransaction(controlFicheroInformacion);
	                
	        // use a StreamFactory to create a BeanReader
	        String encoding = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_ENCODING_NORMA63);
			
	        reader = new InputStreamReader(new FileInputStream(ficheroEntrada), encoding);
	        beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_CUADERNO63_FASE1, reader);
	        
	        writer = new OutputStreamWriter(new FileOutputStream(ficheroSalida), encoding);
	        beanWriter = factory.createWriter(EmbargosConstants.STREAM_NAME_CUADERNO63_FASE2, writer);
	        
	        EntidadesComunicadora entidadComunicadora = null;

			HashMap<String, PeticionInformacion> peticiones = new HashMap<>();
			{ // carga con scope local.
				List<PeticionInformacion> listaPeticiones = informationPetitionRepository.findAllByControlFichero(controlFicheroPeticion);
				for (PeticionInformacion peticion : listaPeticiones) {
					peticiones.put(peticion.getNif() + "-" + peticion.getNumeroEmbargo(), peticion);
				}
			}

	        Object record = null;
	        while ((record = beanReader.read()) != null) {
	        
	        	if(EmbargosConstants.RECORD_NAME_SOLICITUDINFORMACION.equals(beanReader.getRecordName())) {
	        		
	        		SolicitudInformacionFase1 solicitudInformacion = (SolicitudInformacionFase1) record;

	        		//Se obtiene la peticionInformacion a partir del correspondiente ControlFichero y NIF:
	        		PeticionInformacion peticionInformacion = peticiones.get(solicitudInformacion.getNifDeudor()+"-"+solicitudInformacion.getIdentificadorDeuda());
					RespuestaSolicitudInformacionFase2 respuesta = null;

					if(peticionInformacion!=null) {
	        			//Se guardan:
	        			//- El codigo del fichero respuesta:
	        			peticionInformacion.setCodFicheroRespuesta(BigDecimal.valueOf(controlFicheroInformacion.getCodControlFichero()));
	        			
	        			//- El calculo de las claves de seguridad de cada cuenta (el calculo se realiza en la fase 2, al tramitar, y no antes):
	        			EmbargosUtils.calculateClavesSeguridadInPeticionInformacion(peticionInformacion);
	        			
	        			peticionInformacion.setUsuarioUltModificacion(usuarioTramitador);
	        	        peticionInformacion.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
	        			informationPetitionRepository.save(peticionInformacion);

						//Se genera la respuesta
						respuesta =
								cuaderno63Mapper.generateRespuestaSolicitudInformacionFase2_cliente(solicitudInformacion, peticionInformacion);
	        			
	        		} else {
	        			//Si peticionInformacion es nulo: inicializar el objeto vacio:
	        			peticionInformacion = new PeticionInformacion();

						//Se genera la respuesta
						respuesta =
								cuaderno63Mapper.generateRespuestaSolicitudInformacionFase2_noCliente(solicitudInformacion, peticionInformacion);
	        		}
	        		
	        		beanWriter.write(EmbargosConstants.RECORD_NAME_RESPUESTASOLICITUDINFORMACION, respuesta);
	        	
	        	}
	        	else if(EmbargosConstants.RECORD_NAME_CABECERAEMISOR.equals(beanReader.getRecordName())) {
	 
	        		CabeceraEmisorFase1 cabeceraEmisorFase1 = (CabeceraEmisorFase1) record;
	        		
	        		String nifOrganismoEmisor = cabeceraEmisorFase1.getNifOrganismoEmisor();
	        		
	        		entidadComunicadora = communicatingEntityRepository.findByNifEntidad(nifOrganismoEmisor);
	        		
	        		Date fechaObtencionFicheroEntidadDeDeposito = new Date();
	        		
	        		CabeceraEmisorFase2 cabeceraEmisorFase2 = cuaderno63Mapper.generateCabeceraEmisorFase2(cabeceraEmisorFase1, 
	        				fechaObtencionFicheroEntidadDeDeposito);
	        		
	        		beanWriter.write(EmbargosConstants.RECORD_NAME_CABECERAEMISOR, cabeceraEmisorFase2);
	        		
	        	}
	        	else if(EmbargosConstants.RECORD_NAME_FINFICHERO.equals(beanReader.getRecordName())) {
	        		
	        		FinFicheroFase1 finFicheroFase1 = (FinFicheroFase1) record;
	        		
	        		FinFicheroFase2 finFicheroFase2 = cuaderno63Mapper.generateFinFicheroFase2(finFicheroFase1);
	        		
	        		beanWriter.write(EmbargosConstants.RECORD_NAME_FINFICHERO, finFicheroFase2);
	        	}
	        	
	        	beanWriter.flush();
	        	
	        }
	
	        //ACTUALIZACIONES DEL FICHERO DE SALIDA (controlFicheroInformacion):
	        //1.- Se actualiza el CRC:
	        controlFicheroInformacion.setNumCrc(es.commerzbank.ice.comun.lib.util.FileUtils.getMD5(ficheroSalida.getCanonicalPath()));

			//2.- Se guarda el codigo de la Entidad comunicadora:
	        controlFicheroInformacion.setEntidadesComunicadora(entidadComunicadora);
	        
	        //3.- Se actualiza el estado de controlFicheroInformacion a GENERADO:
			EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero(
	        		EmbargosConstants.COD_ESTADO_CTRLFICHERO_ENVIO_INFORMACION_NORMA63_GENERATED,
	        		EmbargosConstants.COD_TIPO_FICHERO_ENVIO_INFORMACION_NORMA63);
	        controlFicheroInformacion.setEstadoCtrlfichero(estadoCtrlfichero);
	        
	        //4.- Actualizacion del fichero de origen:
	        controlFicheroInformacion.setControlFicheroOrigen(controlFicheroPeticion);
	        
	        controlFicheroInformacion.setUsuarioUltModificacion(usuarioTramitador);
	        controlFicheroInformacion.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
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
	        
	        controlFicheroPeticion.setUsuarioUltModificacion(usuarioTramitador);
	        controlFicheroPeticion.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
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

			// Mover a outbox
			String outboxGenerated = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_OUTBOX);
			fileWriterHelper.transferToOutbox(ficheroSalida, outboxGenerated, fileNameInformacion);
	        
		} catch (Exception e) {
			LOG.error("Error in .INF File Service", e);
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
