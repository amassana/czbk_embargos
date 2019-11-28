package es.commerzbank.ice.embargos.service.files.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.List;

import es.commerzbank.ice.comun.lib.util.ICEException;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import es.commerzbank.ice.embargos.domain.entity.EntidadesOrdenante;
import es.commerzbank.ice.embargos.domain.entity.ErrorTraba;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.formats.aeat.validaciontrabas.EntidadCreditoValidacionFase4;
import es.commerzbank.ice.embargos.formats.aeat.validaciontrabas.EntidadTransmisoraValidacionFase4;
import es.commerzbank.ice.embargos.formats.aeat.validaciontrabas.ErroresTrabaValidacionFase4;
import es.commerzbank.ice.embargos.repository.ErrorRepository;
import es.commerzbank.ice.embargos.repository.OrderingEntityRepository;
import es.commerzbank.ice.embargos.repository.SeizedErrorRepository;
import es.commerzbank.ice.embargos.repository.SeizureRepository;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.files.AEATSeizedResultService;
import es.commerzbank.ice.utils.EmbargosConstants;
import es.commerzbank.ice.utils.EmbargosUtils;

@Service
@Transactional(transactionManager="transactionManager", propagation = Propagation.REQUIRES_NEW)
public class AEATSeizedResultServiceImpl implements AEATSeizedResultService{

	private static final Logger logger = LoggerFactory.getLogger(AEATSeizedResultServiceImpl.class);
	
	@Value("${commerzbank.embargos.beanio.config-path.aeat}")
	String pathFileConfigAEAT;
			
	@Autowired
	private FileControlMapper fileControlMapper;

	@Autowired
	private FileControlService fileControlService;
	
	@Autowired
	private OrderingEntityRepository orderingEntityRepository;
	
	@Autowired
	private SeizureRepository seizureRepository;
	
	@Autowired
	private SeizedErrorRepository seizedErrorRepository;
	
	@Autowired
	private ErrorRepository errorRepository;
	
	@Autowired
	private GeneralParametersService generalParametersService;	
	
	@Override
	public void tratarFicheroErrores(File processingFile, String originalName, File processedFile)  throws IOException {

		logger.info("AEATSeizureServiceImpl - tratarFicheroErrores - start");
		
		BeanReader beanReader = null;
		Reader reader = null;
		
		ControlFichero controlFicheroErrores = null;
		
		try {
		
	        // create a StreamFactory
	        StreamFactory factory = StreamFactory.newInstance();
	        // load the mapping file
	        factory.loadResource(pathFileConfigAEAT);
	        
	        //Se guarda el registro de ControlFichero del fichero de entrada:
	        controlFicheroErrores = 
	        		fileControlMapper.generateControlFichero(processingFile, EmbargosConstants.COD_TIPO_FICHERO_ERRORES_TRABAS_ENVIADAS_AEAT, originalName, processedFile);
	        
	        fileControlService.saveFileControlTransaction(controlFicheroErrores);
	
	        
	        // use a StreamFactory to create a BeanReader
	        String encoding = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_ENCODING_AEAT);
	        
			reader = new InputStreamReader(new FileInputStream(processingFile), encoding);
	        beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_AEAT_RESULTADOVALIDACIONTRABAS, reader);
	        
	        Object record = null;
	        boolean isEntidadTransmisoraCommerzbank = false;
	        
	        EntidadTransmisoraValidacionFase4 entidadTransmisoraValidacionFase4 = null;
	        EntidadCreditoValidacionFase4 entidadCreditoValidacionFase4 = null;
	        ErroresTrabaValidacionFase4 erroresTrabaValidacionFase4 = null;
	        
	        EntidadesOrdenante entidadOrdenante = null;
	        //Date fechaObtencionFicheroOrganismo = null;

	        while ((record = beanReader.read()) != null) {
	        	
	        	if(EmbargosConstants.RECORD_NAME_AEAT_ENTIDADTRANSMISORA.equals(beanReader.getRecordName())) {      	
	        		
	        		entidadTransmisoraValidacionFase4 = (EntidadTransmisoraValidacionFase4) record;
	        		
	        		//Si la entidad transmisora es Commerzbank:
	        		if (entidadTransmisoraValidacionFase4.getCodigoEntidadTransmisora() != null 
	        				&& entidadTransmisoraValidacionFase4.getCodigoEntidadTransmisora().equals(EmbargosConstants.CODIGO_NRBE_COMMERZBANK)) {   			
	        			
	        			isEntidadTransmisoraCommerzbank = true;
	        			
	        		} else {	
	        			isEntidadTransmisoraCommerzbank = false;
	        		}
	        	}
	        	
	        	//Tratar registros solo si la entidad transmisora es Commerzbank:
	        	if (isEntidadTransmisoraCommerzbank) {
	        	
		        	if(EmbargosConstants.RECORD_NAME_AEAT_ENTIDADCREDITO.equals(beanReader.getRecordName())) {
		            	
		        		entidadCreditoValidacionFase4 = (EntidadCreditoValidacionFase4) record;
		        		
		        		String identificadorEntidad = entidadCreditoValidacionFase4.getDelegacionAgenciaEmisora()!=null ? 
		        				entidadCreditoValidacionFase4.getDelegacionAgenciaEmisora() : null;
		        		
		        		entidadOrdenante = orderingEntityRepository.findByIdentificadorEntidad(identificadorEntidad);
		        		
		        		if (entidadOrdenante == null) {
		        			throw new ICEException("No se puede procesar el fichero '" + processingFile.getName() +
		        					"': Entidad Ordenante con identificadorEntidad " + identificadorEntidad + " no encontrada.");
		        		}
		        	}
		        	
		        	if(EmbargosConstants.RECORD_NAME_AEAT_ERRORESTRABA.equals(beanReader.getRecordName())) {
		        		
		        		erroresTrabaValidacionFase4 = (ErroresTrabaValidacionFase4) record;
		        		
		        		//Se obtiene el embargo:
		        		
		        		String numeroEmbargo = erroresTrabaValidacionFase4.getNumeroDiligenciaEmbargo();
		        		
		        		List<Embargo> embargosList = seizureRepository.findAllByNumeroEmbargo(numeroEmbargo);

		        		Embargo embargo = EmbargosUtils.selectEmbargo(embargosList);
		        			        		
		        		Traba traba = embargo.getTrabas().get(0);
		        		
		        		//Guardar los errores de la traba:
		        		
		        		for (int i=1; i<20; i++) {
		        		
		        			ErrorTraba errorTraba = generateErrorTrabaByNumeroCampo(i, controlFicheroErrores, traba,
		        					erroresTrabaValidacionFase4);
		        		
		        			if (errorTraba!=null) {
		        				seizedErrorRepository.save(errorTraba);
		        			}
		        			
		        		}
		        	}
		        }       	
	        }
	        

		} catch (Exception e) {
	        
			logger.error("Error while treating seized result file", e);
			//throw e;
			
		} finally {
			if (reader!=null) {
				reader.close();
			}
			if (beanReader!=null) {
				beanReader.close();
			}
		}
	
		logger.info("AEATSeizureServiceImpl - tratarFicheroErrores - end");
	}

	private ErrorTraba generateErrorTrabaByNumeroCampo(int fieldNumber, ControlFichero controlFichero, Traba traba,
			ErroresTrabaValidacionFase4 erroresTrabaValidacionFase4) {
		
		ErrorTraba errorTraba = null;
		
		Integer codigoError = null;
		Integer numeroCampo = null;
		
		switch (fieldNumber) {
			case 1:			
				codigoError = erroresTrabaValidacionFase4.getCodigoError1();
				numeroCampo = erroresTrabaValidacionFase4.getNumeroCampoError1();
				break;
			case 2:			
				codigoError = erroresTrabaValidacionFase4.getCodigoError2();
				numeroCampo = erroresTrabaValidacionFase4.getNumeroCampoError2();
				break;
			case 3:			
				codigoError = erroresTrabaValidacionFase4.getCodigoError3();
				numeroCampo = erroresTrabaValidacionFase4.getNumeroCampoError3();
				break;
			case 4:			
				codigoError = erroresTrabaValidacionFase4.getCodigoError4();
				numeroCampo = erroresTrabaValidacionFase4.getNumeroCampoError4();
				break;
			case 5:			
				codigoError = erroresTrabaValidacionFase4.getCodigoError5();
				numeroCampo = erroresTrabaValidacionFase4.getNumeroCampoError5();
				break;
			case 6:			
				codigoError = erroresTrabaValidacionFase4.getCodigoError6();
				numeroCampo = erroresTrabaValidacionFase4.getNumeroCampoError6();
				break;
			case 7:			
				codigoError = erroresTrabaValidacionFase4.getCodigoError7();
				numeroCampo = erroresTrabaValidacionFase4.getNumeroCampoError7();
				break;
			case 8:			
				codigoError = erroresTrabaValidacionFase4.getCodigoError8();
				numeroCampo = erroresTrabaValidacionFase4.getNumeroCampoError8();
				break;
			case 9:			
				codigoError = erroresTrabaValidacionFase4.getCodigoError9();
				numeroCampo = erroresTrabaValidacionFase4.getNumeroCampoError9();
				break;
			case 10:			
				codigoError = erroresTrabaValidacionFase4.getCodigoError10();
				numeroCampo = erroresTrabaValidacionFase4.getNumeroCampoError10();
				break;
			case 11:			
				codigoError = erroresTrabaValidacionFase4.getCodigoError11();
				numeroCampo = erroresTrabaValidacionFase4.getNumeroCampoError11();
				break;
			case 12:			
				codigoError = erroresTrabaValidacionFase4.getCodigoError12();
				numeroCampo = erroresTrabaValidacionFase4.getNumeroCampoError12();
				break;
			case 13:			
				codigoError = erroresTrabaValidacionFase4.getCodigoError13();
				numeroCampo = erroresTrabaValidacionFase4.getNumeroCampoError13();
				break;
			case 14:			
				codigoError = erroresTrabaValidacionFase4.getCodigoError1();
				numeroCampo = erroresTrabaValidacionFase4.getNumeroCampoError14();
				break;
			case 15:			
				codigoError = erroresTrabaValidacionFase4.getCodigoError15();
				numeroCampo = erroresTrabaValidacionFase4.getNumeroCampoError15();
				break;
			case 16:			
				codigoError = erroresTrabaValidacionFase4.getCodigoError16();
				numeroCampo = erroresTrabaValidacionFase4.getNumeroCampoError16();
				break;
			case 17:			
				codigoError = erroresTrabaValidacionFase4.getCodigoError17();
				numeroCampo = erroresTrabaValidacionFase4.getNumeroCampoError17();
				break;
			case 18:			
				codigoError = erroresTrabaValidacionFase4.getCodigoError18();
				numeroCampo = erroresTrabaValidacionFase4.getNumeroCampoError18();
				break;
			case 19:			
				codigoError = erroresTrabaValidacionFase4.getCodigoError19();
				numeroCampo = erroresTrabaValidacionFase4.getNumeroCampoError19();
				break;	
			case 20:			
				codigoError = erroresTrabaValidacionFase4.getCodigoError20();
				numeroCampo = erroresTrabaValidacionFase4.getNumeroCampoError20();
				break;	
			default:
		}
		
		if (codigoError!=null && numeroCampo!=null && codigoError.compareTo(Integer.valueOf(0)) > 0) {
			
			errorTraba = new ErrorTraba();
			
			es.commerzbank.ice.embargos.domain.entity.Error error = errorRepository.findByNumeroError(Integer.toString(codigoError));
			
			if(error!=null) {
				errorTraba.setError(error);
				errorTraba.setNumeroCampo(new BigDecimal(numeroCampo));
				errorTraba.setControlFichero(controlFichero);
				errorTraba.setTraba(traba);
			} else {
				//TODO lanzar excepcion si numero de error no se ha encontrado en bbdd en la tabla de ERRORES.
			}
		}
		
		return errorTraba;
	}

}
