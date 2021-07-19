package es.commerzbank.ice.embargos.service.impl;

import es.commerzbank.ice.comun.lib.domain.entity.Sucursal;
import es.commerzbank.ice.embargos.config.OracleDataSourceEmbargosConfig;
import es.commerzbank.ice.embargos.domain.dto.FileControlDTO;
import es.commerzbank.ice.embargos.domain.dto.PetitionCaseDTO;
import es.commerzbank.ice.embargos.domain.dto.PetitionDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.EstadoIntPeticion;
import es.commerzbank.ice.embargos.domain.entity.Peticion;
import es.commerzbank.ice.embargos.domain.mapper.PetitionMapper;
import es.commerzbank.ice.embargos.repository.PetitionRepository;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.InformationPetitionService;
import es.commerzbank.ice.embargos.service.PetitionService;
import es.commerzbank.ice.embargos.utils.OfficeUtils;
import es.commerzbank.ice.embargos.utils.ResourcesUtil;
import net.sf.jasperreports.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.sql.Connection;
import java.util.*;

@Service
@Transactional(transactionManager="transactionManager")
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
	private PetitionRepository petitionRepository;

	@Autowired
	private OfficeUtils officeUtils;
	
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
			
			FileControlDTO fileControlDTO = fileControlService.getByCodeFileControl(codeFileControl, null);
			petitionDTO.setFileControl(fileControlDTO);
		
		}
		
		return petitionDTO;
	}

	@Override
	public byte[] generateF1PettitionRequest(Integer codeFileControl) throws Exception {
		List<Sucursal> sucursales = officeUtils.getSucursalesActivas();
		Collections.sort(sucursales, Comparator.comparing(Sucursal::getCodSucursal));
		
		JasperPrint report = null;

		for (Sucursal sucursal : sucursales) {
			JasperPrint currentReport = generateF1PettitionRequest(codeFileControl, sucursal);

			if (currentReport.getPages().size() > 0) {
				if (report == null) report = currentReport;
				else report.addPage(currentReport.getPages().get(0));
			}
		}

		return JasperExportManager.exportReportToPdf(report);
	}

	private JasperPrint generateF1PettitionRequest(Integer codeFileControl, Sucursal sucursal) throws Exception {
		HashMap<String, Object> parameters = new HashMap<>();

		try (Connection connEmbargos = oracleDataSourceEmbargosConfig.getEmbargosConnection()) {

			Resource jrxmlResource = ResourcesUtil.getFromJasperFolder("F1_peticionInformacion.jasper");
			Resource logoRes = ResourcesUtil.getImageLogoCommerceResource();

			parameters.put("img_param", logoRes.getFile().toString());
			parameters.put("cod_control_fichero", codeFileControl);
			parameters.put("NOMBRE_SUCURSAL", sucursal.getNombre());
			parameters.put("PREFIJO_CUENTA", sucursal.getNumeroSucursal().toString() + "%");

			parameters.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));

			InputStream resourceInputStream = jrxmlResource.getInputStream();

			JasperPrint reporteLleno = JasperFillManager.fillReport(resourceInputStream, parameters, connEmbargos);

			return reporteLleno;
		} catch (Exception ex) {
			throw new Exception("Error in generateF1PettitionRequest()", ex);
		}
	}

	@Override
	public byte[] generateF2PettitionResponse(Integer codeFileControl, String oficina) throws Exception {

		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection connEmbargos = oracleDataSourceEmbargosConfig.getEmbargosConnection()) {

			Resource jrxmlResource = ResourcesUtil.getFromJasperFolder("F2_envioInformacion.jasper");
			Resource logoRes = ResourcesUtil.getImageLogoCommerceResource();

			//Resource subReportResource = ResourcesUtil.getReportHeaderResource();
			//Resource imageReport = ResourcesUtil.getImageLogoCommerceResource();

			//File image = imageReport.getFile();
			//InputStream subResourceInputStream = subReportResource.getInputStream();

			//JasperReport subReport = (JasperReport) JRLoader.loadObject(subResourceInputStream);

			//parameters.put("img_param", image.toString());
			parameters.put("img_param", logoRes.getFile().toString());
			parameters.put("cod_control_fichero", codeFileControl);
			parameters.put("NOMBRE_SUCURSAL", oficina);
			//parameters.put("file_param", subReport);

			parameters.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));


			InputStream resourceInputStream = jrxmlResource.getInputStream();

			JasperPrint reporteLleno = JasperFillManager.fillReport(resourceInputStream, parameters, connEmbargos);
			
			List<JRPrintPage> pages = reporteLleno.getPages();
			 
			 if (pages.size() == 0)  return null;

			return JasperExportManager.exportReportToPdf(reporteLleno);
		} catch (Exception ex) {
			throw new Exception("Error in generateF2PettitionResponse()", ex);
		}
	}

	@Override
	public void changeStatus(Peticion peticion, Long cgpjEstadoInternoProcesado) {
		EstadoIntPeticion estadoInterno = new EstadoIntPeticion();
		estadoInterno.setCodEstadoIntPeticion(cgpjEstadoInternoProcesado);
		peticion.setEstadoIntPeticion(estadoInterno);
		petitionRepository.save(peticion);
	}
}
