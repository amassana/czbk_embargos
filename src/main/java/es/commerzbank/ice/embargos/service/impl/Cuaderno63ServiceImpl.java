package es.commerzbank.ice.embargos.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.beanio.BeanReader;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.commerzbank.ice.comun.lib.typeutils.DateUtils;
import es.commerzbank.ice.comun.lib.typeutils.VB6Date;
import es.commerzbank.ice.comun.lib.util.ICEParserException;
import es.commerzbank.ice.embargos.domain.dto.BankAccountDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.CuentaEmbargo;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora;
import es.commerzbank.ice.embargos.domain.entity.EntidadesOrdenante;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlfichero;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlficheroPK;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacion;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacionCuenta;
import es.commerzbank.ice.embargos.domain.mapper.Cuaderno63Mapper;
import es.commerzbank.ice.embargos.domain.mapper.InformationPetitionBankAccountMapper;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.CabeceraEmisorFase1;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.FinFicheroFase1;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.SolicitudInformacionFase1;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase2.CabeceraEmisorFase2;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase2.FinFicheroFase2;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase2.RespuestaSolicitudInformacionFase2;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.CabeceraEmisorFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.OrdenEjecucionEmbargoComplementarioFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.OrdenEjecucionEmbargoFase3;
import es.commerzbank.ice.embargos.repository.CommunicatingEntityRepository;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.InformationPetitionBankAccountRepository;
import es.commerzbank.ice.embargos.repository.InformationPetitionRepository;
import es.commerzbank.ice.embargos.repository.OrderingEntityRepository;
import es.commerzbank.ice.embargos.repository.PetitionRepository;
import es.commerzbank.ice.embargos.repository.SeizureAccountRepository;
import es.commerzbank.ice.embargos.repository.SeizureRepository;
import es.commerzbank.ice.embargos.service.Cuaderno63Service;
import es.commerzbank.ice.embargos.service.CustomerService;
import es.commerzbank.ice.utils.EmbargosConstants;
import es.commerzbank.ice.utils.EmbargosUtils;

@Service
@Transactional
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
	InformationPetitionBankAccountMapper informationPetitionBankAccountMapper;
	
	@Autowired
	CustomerService customerService;
	
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
	SeizureAccountRepository seizureAccountRepository;
	
	@Autowired
	OrderingEntityRepository orderingEntityRepository;
	
	@Autowired
	CommunicatingEntityRepository communicatingEntityRepository;
	
	
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
	        		cuaderno63Mapper.generateControlFichero(file, EmbargosConstants.COD_TIPO_FICHERO_PETICION_INFORMACION_NORMA63);
	        
	        fileControlRepository.save(controlFicheroPeticion);
	                        
	        // use a StreamFactory to create a BeanReader
	        beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_FASE1, file);
	        	        
	        CabeceraEmisorFase1 cabeceraEmisor = null;
	        EntidadesComunicadora entidadComunicadora = null;
	        Date fechaObtencionFicheroOrganismo = null;
	        
	        Object record = null;
	        while ((record = beanReader.read()) != null) {
	        
	        	if(EmbargosConstants.RECORD_NAME_SOLICITUDINFORMACION.equals(beanReader.getRecordName())) {
	        		
	        		//Registro de detalle:
	        		
	        		SolicitudInformacionFase1 solicitudInformacion = (SolicitudInformacionFase1) record;
	        		LOG.debug(solicitudInformacion.getNifDeudor());
	 		        
	        		//Obtener las cuentas del nif:
	        		List<BankAccountDTO> listBankAccount = customerService.listAllAccountsByNif(solicitudInformacion.getNifDeudor());
	        		
	        		//Tratar solamente los clientes en los que se han encontrado cuentas:
	        		if(listBankAccount != null && !listBankAccount.isEmpty()) {
	        		        			
		        		//Se guarda la PeticionInformacion en bbdd:
		        		PeticionInformacion peticionInformacion = cuaderno63Mapper.generatePeticionInformacion(solicitudInformacion, 
		        				controlFicheroPeticion.getCodControlFichero(), listBankAccount);
		        		
		        		informationPetitionRepository.save(peticionInformacion);
		        			        		
	        			//Se guardan todas las cuentas del nif en la tabla PETICION_INFORMACION_CUENTAS:
		        		for(BankAccountDTO bankAccountDTO : listBankAccount) {
		        			
		        			PeticionInformacionCuenta peticionInformacionCuenta = 
		        					informationPetitionBankAccountMapper.toPeticionInformacionCuenta(bankAccountDTO, 
		        							peticionInformacion.getCodPeticion());
		        			
		        			informationPetitionBankAccountRepository.save(peticionInformacionCuenta);
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
			BigDecimal creationDateVB6 = fechaObtencionFicheroOrganismo!=null ?  BigDecimal.valueOf(VB6Date.dateToInt(fechaObtencionFicheroOrganismo)) : null;
			controlFicheroPeticion.setFechaCreacion(creationDateVB6);
	       
			//- Se guarda la fecha maxima de respuesta (now + dias de margen)
			long diasRespuestaF1 = entidadComunicadora.getDiasRespuestaF1()!=null ? entidadComunicadora.getDiasRespuestaF1().longValue() : 0;
			Date lastDateResponse = DateUtils.convertToDate(LocalDate.now().plusDays(diasRespuestaF1));
			BigDecimal limitResponseDateVB6 = BigDecimal.valueOf(VB6Date.dateToInt(lastDateResponse));
			controlFicheroPeticion.setFechaMaximaRespuesta(limitResponseDateVB6);
			
			//Cambio de estado de CtrlFichero a: RECIBIDO
	        EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero();
	        EstadoCtrlficheroPK estadoCtrlficheroPK = new EstadoCtrlficheroPK();
	        estadoCtrlficheroPK.setCodEstado(EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_INFORMACION_NORMA63_RECEIVED);
	        estadoCtrlficheroPK.setCodTipoFichero(EmbargosConstants.COD_TIPO_FICHERO_PETICION_INFORMACION_NORMA63);
	        estadoCtrlfichero.setId(estadoCtrlficheroPK);
	        controlFicheroPeticion.setEstadoCtrlfichero(estadoCtrlfichero);
			
			fileControlRepository.save(controlFicheroPeticion);

		} catch (Exception e) {
			
			//Si hay excepcion y se ha generado controlFicheroPeticion (tiene seteada la fecha 
			//de ultima modificacion): guardar estado de controlFicheroPeticion como ERROR:
			if (controlFicheroPeticion!=null && controlFicheroPeticion.getFUltimaModificacion()!=null) {
				
		        EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero();
		        EstadoCtrlficheroPK estadoCtrlficheroPK = new EstadoCtrlficheroPK();
		        estadoCtrlficheroPK.setCodEstado(EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_INFORMACION_NORMA63_ERROR);
		        estadoCtrlficheroPK.setCodTipoFichero(EmbargosConstants.COD_TIPO_FICHERO_PETICION_INFORMACION_NORMA63);
		        estadoCtrlfichero.setId(estadoCtrlficheroPK);
		        controlFicheroPeticion.setEstadoCtrlfichero(estadoCtrlfichero);
				
				fileControlRepository.save(controlFicheroPeticion);
			}
			
			
			//TODO Rollbacks si proceden
			
			throw e;
			
		} finally {
			
			if(beanReader!=null) {
				beanReader.close();
			}
		}
		
	}
	
	public void tramitarFicheroInformacion(Long codControlFicheroPeticion) throws IOException {
			
		
		BeanReader beanReader = null;
		BeanWriter beanWriter = null;
		
		try {
		
			// create a StreamFactory
	        StreamFactory factory = StreamFactory.newInstance();
	        // load the mapping file
	        factory.loadResource(pathFileConfigCuaderno63);        
	        
	        //Se obtiene el ControlFichero de la Peticion:
	        ControlFichero controlFicheroPeticion = fileControlRepository.getOne(codControlFicheroPeticion);
	        
	        //Fichero de entrada (Peticion):
	        String fileNamePeticion = controlFicheroPeticion.getNombreFichero();
	        File ficheroEntrada = new File(pathProcessed + "\\" + fileNamePeticion);
	        
	        //TODO: comprobar que si no existe el fichero de entrada, mostrar error.
	        
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
	        ControlFichero controlFicheroInformacion = 
	        		cuaderno63Mapper.generateControlFichero(ficheroSalida, EmbargosConstants.COD_TIPO_FICHERO_ENVIO_INFORMACION_NORMA63);
	        
	        //Actualizacion estado del fichero: GENERANDO
	        EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero();
	        EstadoCtrlficheroPK estadoCtrlficheroPK = new EstadoCtrlficheroPK();
	        estadoCtrlficheroPK.setCodEstado(EmbargosConstants.COD_ESTADO_CTRLFICHERO_ENVIO_INFORMACION_NORMA63_GENERATING);
	        estadoCtrlficheroPK.setCodTipoFichero(EmbargosConstants.COD_TIPO_FICHERO_ENVIO_INFORMACION_NORMA63);
	        estadoCtrlfichero.setId(estadoCtrlficheroPK);
	        controlFicheroInformacion.setEstadoCtrlfichero(estadoCtrlfichero);
	        
	        fileControlRepository.save(controlFicheroInformacion);
	                
	        // use a StreamFactory to create a BeanReader
	        beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_FASE1, ficheroEntrada);
	        beanWriter = factory.createWriter(EmbargosConstants.STREAM_NAME_FASE2, ficheroSalida);
	        
	        Object record = null;
	        while ((record = beanReader.read()) != null) {
	        
	        	if(EmbargosConstants.RECORD_NAME_SOLICITUDINFORMACION.equals(beanReader.getRecordName())) {
	        		
	        		SolicitudInformacionFase1 solicitudInformacion = (SolicitudInformacionFase1) record;
	        		LOG.debug(solicitudInformacion.getNifDeudor());
	 		               		
	        		//Se obtiene la peticionInformacion a partir del correspondiente ControlFichero y NIF:
	        		PeticionInformacion peticionInformacion = 
	        				informationPetitionRepository.findByControlFicheroAndNif(controlFicheroPeticion,solicitudInformacion.getNifDeudor());		
	        		
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
	
	    
	        //Se actualiza el CRC del registro de ControlFichero del fichero de salida:
	        controlFicheroInformacion.setNumCrc(Long.toString(FileUtils.checksumCRC32(ficheroSalida)));
	        
	        //Actualizacion estado del fichero: GENERADO
	        estadoCtrlfichero = new EstadoCtrlfichero();
	        estadoCtrlficheroPK = new EstadoCtrlficheroPK();
	        estadoCtrlficheroPK.setCodEstado(EmbargosConstants.COD_ESTADO_CTRLFICHERO_ENVIO_INFORMACION_NORMA63_GENERATED);
	        estadoCtrlficheroPK.setCodTipoFichero(EmbargosConstants.COD_TIPO_FICHERO_ENVIO_INFORMACION_NORMA63);
	        estadoCtrlfichero.setId(estadoCtrlficheroPK);
	        controlFicheroInformacion.setEstadoCtrlfichero(estadoCtrlfichero);
	        
	        fileControlRepository.save(controlFicheroInformacion);
        
		} catch (Exception e) {
			
			throw e;
			
		} finally {
			if (beanReader!=null) {
				beanReader.close();
			}
			if (beanWriter!=null) {
				beanWriter.close();
			}
		}
	}
	
	
	public void cargarFicheroEmbargos(File file) throws IOException{
		
		BeanReader beanReader = null;
		
		try {
		
			// create a StreamFactory
	        StreamFactory factory = StreamFactory.newInstance();
	        // load the mapping file
	        factory.loadResource(pathFileConfigCuaderno63);
	        
	        //Se guarda el registro de ControlFichero del fichero de entrada:
	        ControlFichero controlFicheroEmbargo = 
	        		cuaderno63Mapper.generateControlFichero(file, EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_NORMA63);
	        
	        fileControlRepository.save(controlFicheroEmbargo);
	        
	        // use a StreamFactory to create a BeanReader
	        beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_FASE3, file);
	        
	        Object record = null;
	    	
	        CabeceraEmisorFase3 cabeceraEmisor = null;
	        OrdenEjecucionEmbargoFase3 ordenEjecucionEmbargo = null;
	    	OrdenEjecucionEmbargoComplementarioFase3 ordenEjecucionEmbargoComp = null;
	    	boolean isRecordOrdenEjecEmbargo;
	    	EntidadesOrdenante entidadOrdenante = null;
	    	
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
	        		
	        		//TODO TRATAR SI entidadOrdenante ES NULO -> NO SE PUEDE CREAR REGISTRO EN EMBARGO...
	        		
	        		
	        	} else if(EmbargosConstants.RECORD_NAME_FINFICHERO.equals(beanReader.getRecordName())) {
	        		
	        	}
	        	
	        	//Tratamiento de los registros principal y complementario de embargos:
	        	if (ordenEjecucionEmbargo!=null && !isRecordOrdenEjecEmbargo) {
	        		
	        		Embargo embargo = null;
	        		
	        		if (ordenEjecucionEmbargoComp!=null 
	        				&& ordenEjecucionEmbargo.getNifDeudor().equals(ordenEjecucionEmbargoComp.getNifDeudor())) {
	        			
	        			embargo = cuaderno63Mapper.generateEmbargo(ordenEjecucionEmbargo, ordenEjecucionEmbargoComp, controlFicheroEmbargo.getCodControlFichero(), entidadOrdenante);
	        			
	        		} else {
	        			embargo = cuaderno63Mapper.generateEmbargo(ordenEjecucionEmbargo, new OrdenEjecucionEmbargoComplementarioFase3(), controlFicheroEmbargo.getCodControlFichero(), entidadOrdenante);
	        		}
	        		
	        		
	        		//Tratamiento de embargo.....
	        		seizureRepository.save(embargo);
	        		
	        		//- Se guardan las cuentas del embargo:
	        		for (CuentaEmbargo cuentaEmbargo : embargo.getCuentaEmbargos()) {
	        			
	        			//TODO mirar si se tiene que comprobar si es nulo el Iban o bien la "cuenta":
	        			if (cuentaEmbargo.getIban()!=null && !cuentaEmbargo.getIban().isEmpty()) {
	        				seizureAccountRepository.save(cuentaEmbargo);
	        			}
	        		}
	        		
	        		//inicializar a null:
	        		ordenEjecucionEmbargo = null;
	        		ordenEjecucionEmbargoComp = null;
	        	}
	        	
	        }
        
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

}
