package es.commerzbank.ice.embargos.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.io.FilenameUtils;
import org.beanio.BeanReader;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacion;
import es.commerzbank.ice.embargos.domain.entity.TipoFichero;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.CabeceraEmisorFase1;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.FinFicheroFase1;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.SolicitudInformacionFase1;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase2.CabeceraEmisorFase2;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase2.FinFicheroFase2;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase2.RespuestaSolicitudInformacionFase2;
import es.commerzbank.ice.embargos.repository.ControlFicheroRepository;
import es.commerzbank.ice.embargos.repository.PeticionInformacionRepository;
import es.commerzbank.ice.embargos.service.Cuaderno63Service;
import es.commerzbank.ice.utils.Cuaderno63Helper;
import es.commerzbank.ice.utils.EmbargosConstants;

@Service
@Transactional
public class Cuaderno63ServiceImpl implements Cuaderno63Service{

	private static final Logger LOG = LoggerFactory.getLogger(Cuaderno63ServiceImpl.class);
	
	@Value("${commerzbank.embargos.beanio.config-path.cuaderno63}")
	String pathFileConfigCuaderno63;
	
	@Value("${commerzbank.embargos.files.path.monitoring}")
	private String pathMonitoring;
	
	@Value("${commerzbank.embargos.files.path.generated}")
	private String pathGenerated;
	
	//Agregar repositories de DWH ...
	@Autowired
	ControlFicheroRepository controlFicheroRepository;
	
	@Autowired
	PeticionInformacionRepository peticionInformacionRepository;
	
	
	public void tratarFicheroPeticion(File file) throws IOException {	

		// create a StreamFactory
        StreamFactory factory = StreamFactory.newInstance();
        // load the mapping file
        factory.loadResource(pathFileConfigCuaderno63);
       
        String fileNamePeticion = FilenameUtils.getName(file.getCanonicalPath());
        TipoFichero tipoFichero = new TipoFichero(); 
        tipoFichero.setCodTipoFichero(EmbargosConstants.COD_TIPO_FICHERO_PETICIONES);
        EntidadesComunicadora entidadesComunicadora = new EntidadesComunicadora();
        entidadesComunicadora.setCodEntidadPresentadora(1);
        
        //Guardar registro del control del fichero de Peticion:
        ControlFichero controlFicheroPeticion= new ControlFichero();
        controlFicheroPeticion.setTipoFichero(tipoFichero);
        controlFicheroPeticion.setNombreFichero(fileNamePeticion);
        controlFicheroPeticion.setEntidadesComunicadora(entidadesComunicadora);
        controlFicheroRepository.save(controlFicheroPeticion);
        
        //Fichero salida:
        String fileNameInformacion = FilenameUtils.getBaseName(file.getCanonicalPath()) 
            	+ EmbargosConstants.SEPARADOR_PUNTO + EmbargosConstants.TIPO_FICHERO_INFORMACION.toLowerCase();
        File ficheroSalida = new File(pathGenerated + "\\" + fileNameInformacion);
        
        tipoFichero = new TipoFichero();
        tipoFichero.setCodTipoFichero(EmbargosConstants.COD_TIPO_FICHERO_INFORMACION);
        
        //Guardar registro del control del fichero de Informacion (Fichero de salida):
        ControlFichero controlFicheroInformacion= new ControlFichero();
        controlFicheroInformacion.setTipoFichero(tipoFichero);
        controlFicheroInformacion.setNombreFichero(fileNameInformacion);
        controlFicheroInformacion.setEntidadesComunicadora(entidadesComunicadora);
        controlFicheroRepository.save(controlFicheroInformacion);
        
        // use a StreamFactory to create a BeanReader
        BeanReader beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_FASE1, file);
        BeanWriter beanWriter = factory.createWriter(EmbargosConstants.STREAM_NAME_FASE2, ficheroSalida);
        
        Object record = null;
        while ((record = beanReader.read()) != null) {
        
        	if(EmbargosConstants.RECORD_NAME_SOLICITUDINFORMACION.equals(beanReader.getRecordName())) {
        		
        		SolicitudInformacionFase1 solicitudInformacion = (SolicitudInformacionFase1) record;
        		LOG.debug(solicitudInformacion.getNifDeudor());
        		
        		//Se guarda en base de datos:
        		PeticionInformacion peticionInformacion = Cuaderno63Helper.generatePeticionInformacion(solicitudInformacion, 
        				controlFicheroPeticion.getCodControlFichero(), controlFicheroInformacion.getCodControlFichero());
        		peticionInformacionRepository.save(peticionInformacion);
        		
        		
        		//Escribir en fichero respuesta:
        		Map<String,String> ibanClavesSeguridadMap = new LinkedHashMap<>();
        		
        		RespuestaSolicitudInformacionFase2 respuesta = 
        				Cuaderno63Helper.generateRespuestaSolicitudInformacionFase2(solicitudInformacion, ibanClavesSeguridadMap);
        		
        		beanWriter.write(EmbargosConstants.RECORD_NAME_RESPUESTASOLICITUDINFORMACION, respuesta);
        	
        	} else if(EmbargosConstants.RECORD_NAME_CABECERAEMISOR.equals(beanReader.getRecordName())) {
 
        		CabeceraEmisorFase1 cabeceraEmisorFase1 = (CabeceraEmisorFase1) record;
        		LOG.debug(cabeceraEmisorFase1.getNombreOrganismoEmisor());
        		
        		Date fechaObtencionFicheroEntidadDeDeposito = new Date();
        		
        		CabeceraEmisorFase2 cabeceraEmisorFase2 = Cuaderno63Helper.generateCabeceraEmisorFase2(cabeceraEmisorFase1, 
        				fechaObtencionFicheroEntidadDeDeposito);
        		
        		beanWriter.write(EmbargosConstants.RECORD_NAME_CABECERAEMISOR, cabeceraEmisorFase2);
        		
        	} else if(EmbargosConstants.RECORD_NAME_FINFICHERO.equals(beanReader.getRecordName())) {
        		
        		FinFicheroFase1 finFicheroFase1 = (FinFicheroFase1) record;
        		LOG.debug(finFicheroFase1.getNombreOrganismoEmisor());
        		
        		FinFicheroFase2 finFicheroFase2 = Cuaderno63Helper.generateFinFicheroFase2(finFicheroFase1);
        		
        		beanWriter.write(EmbargosConstants.RECORD_NAME_FINFICHERO, finFicheroFase2);
        	}
        	
        	LOG.debug(record.toString());
        	
        	beanWriter.flush();
        	
        }
        beanReader.close();
        beanWriter.close();
		
	}
	
	public void tratarFicheroEmbargos(File file) {
		
	}
	
	public void tratarFicheroLevantamientos(File file) {
		
	}

}
