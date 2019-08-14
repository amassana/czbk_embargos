package es.commerzbank.ice.embargos.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import es.commerzbank.ice.embargos.config.OracleDataSourceEmbargosConfig;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import es.commerzbank.ice.embargos.domain.dto.FileControlDTO;
import es.commerzbank.ice.embargos.domain.dto.PetitionCaseDTO;
import es.commerzbank.ice.embargos.domain.dto.PetitionDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.Peticion;
import es.commerzbank.ice.embargos.domain.mapper.PetitionMapper;
import es.commerzbank.ice.embargos.repository.PetitionRepository;
import es.commerzbank.ice.embargos.service.Cuaderno63Service;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.InformationPetitionService;
import es.commerzbank.ice.embargos.service.PetitionService;
import es.commerzbank.ice.utils.ResourcesUtil;

@Service
@Transactional
public class PetitionServiceImpl implements PetitionService{

	private static final Logger LOG = LoggerFactory.getLogger(PetitionServiceImpl.class);

	@Autowired
	private OracleDataSourceEmbargosConfig oracleDataSourceEmbargosConfig;

	@Autowired
	private FileControlService fileControlService;
	
	@Autowired
	private InformationPetitionService informationPetitionService;
	
	@Autowired
	private PetitionMapper petitionMapper;
		
	@Autowired
	private Cuaderno63Service cuaderno63Service;
	
	@Autowired
	private PetitionRepository petitionRepository;
	
	@Override
	public PetitionDTO getByCodeFileControl(Long codeFileControl) {
		
		
		ControlFichero controlFichero = new ControlFichero();
		controlFichero.setCodControlFichero(codeFileControl);
	
		Peticion peticion = petitionRepository.findByControlFichero(controlFichero);

		PetitionDTO petitionDTO = petitionMapper.toPetitionDTO(peticion);
		
		if (peticion!=null) {
			
			List<PetitionCaseDTO> peticionInformacionList = 
					informationPetitionService.getAllByControlFichero(controlFichero);
			
			petitionDTO.setInformationPetitionList(peticionInformacionList);
			
			FileControlDTO fileControlDTO = fileControlService.getByCodeFileControl(codeFileControl);
			petitionDTO.setFileControl(fileControlDTO);
		
		}
		
		return petitionDTO;
	}

	@Override
	public byte[] generateJasperPDF(Integer codeFileControl) throws Exception {

		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (
				Connection connEmbargos = oracleDataSourceEmbargosConfig.getEmbargosConnection();
//				Connection connComunes = oracleDataSourceEmbargosConfig.getComunesConnection();
		) {

			Resource jrxmlResource = ResourcesUtil.getFromJasperFolder("peticiones_informacion.jasper");
			Resource subReportResource = ResourcesUtil.getReportHeaderResource();
			Resource imageReport = ResourcesUtil.getImageLogoCommerceResource();

			File image = imageReport.getFile();
			InputStream subResourceInputStream = subReportResource.getInputStream();

			JasperReport subReport = (JasperReport) JRLoader.loadObject(subResourceInputStream);

			parameters.put("img_param", image.toString());
			parameters.put("cod_control_fichero", codeFileControl);
			// parameters.put("cod_user", 3);
			parameters.put("file_param", subReport);
//			parameters.put("conn_param", connComunes);

			InputStream resourceInputStream = jrxmlResource.getInputStream();

			JasperPrint reporteLleno = JasperFillManager.fillReport(resourceInputStream, parameters, connEmbargos);

			return JasperExportManager.exportReportToPdf(reporteLleno);
		} catch (JRException | SQLException ex) {
			throw new Exception("Error in generarReporteListado()", ex);
		}
	}
}
