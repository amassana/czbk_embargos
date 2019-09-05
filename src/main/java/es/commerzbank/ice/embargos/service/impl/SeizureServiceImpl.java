package es.commerzbank.ice.embargos.service.impl;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.embargos.config.OracleDataSourceEmbargosConfig;
import es.commerzbank.ice.embargos.domain.dto.SeizedBankAccountDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureActionDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureStatusDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.CuentaTrabaActuacion;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import es.commerzbank.ice.embargos.domain.entity.EstadoTraba;
import es.commerzbank.ice.embargos.domain.entity.HCuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.HTraba;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.embargos.domain.mapper.HSeizedBankAccountMapper;
import es.commerzbank.ice.embargos.domain.mapper.HSeizureMapper;
import es.commerzbank.ice.embargos.domain.mapper.SeizedBankAccountActionMapper;
import es.commerzbank.ice.embargos.domain.mapper.SeizedBankAccountMapper;
import es.commerzbank.ice.embargos.domain.mapper.SeizureMapper;
import es.commerzbank.ice.embargos.domain.mapper.SeizureStatusMapper;
import es.commerzbank.ice.embargos.repository.HSeizedBankAccountRepository;
import es.commerzbank.ice.embargos.repository.HSeizedRepository;
import es.commerzbank.ice.embargos.repository.SeizedBankAccountActionRepository;
import es.commerzbank.ice.embargos.repository.SeizedBankAccountRepository;
import es.commerzbank.ice.embargos.repository.SeizedRepository;
import es.commerzbank.ice.embargos.repository.SeizureRepository;
import es.commerzbank.ice.embargos.repository.SeizureStatusRepository;
import es.commerzbank.ice.embargos.service.SeizureService;
import es.commerzbank.ice.utils.ICEDateUtils;
import es.commerzbank.ice.utils.ResourcesUtil;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
@Transactional(transactionManager="transactionManager")
public class SeizureServiceImpl implements SeizureService {

	@Autowired
	private SeizureMapper seizureMapper;
	
	@Autowired
	private HSeizureMapper hSeizureMapper;
	
	@Autowired
	private SeizedBankAccountMapper seizedBankAccountMapper;
	
	@Autowired
	private HSeizedBankAccountMapper hSeizedBankAccountMapper;
	
	@Autowired
	private SeizedBankAccountActionMapper seizedBankAccountActionMapper;
	
	@Autowired
	private SeizureStatusMapper seizureStatusMapper;
	
	@Autowired
	private SeizureRepository seizureRepository;
	
	@Autowired
	private SeizedRepository seizedRepository;
	
	@Autowired
	private HSeizedRepository hSeizedRepository;
	
	@Autowired
	private SeizedBankAccountRepository seizedBankAccountRepository;
	
	@Autowired
	private HSeizedBankAccountRepository hSeizedBankAccountRepository;

	@Autowired
	private SeizedBankAccountActionRepository seizedBankAccountActionRepository;
	
	@Autowired
	private SeizureStatusRepository seizureStatusRepository;
	
	@Autowired
	private OracleDataSourceEmbargosConfig oracleDataSourceEmbargos;
	
	@Override
	public List<SeizureDTO> getSeizureListByCodeFileControl(Long codeFileControl) {
		
		List<SeizureDTO> seizureDTOList = new ArrayList<>();
		
		ControlFichero controlFichero = new ControlFichero();
		controlFichero.setCodControlFichero(codeFileControl);
		
		List<Embargo> embargoList = seizureRepository.findAllByControlFichero(controlFichero);
		
		for (Embargo embargo : embargoList) {
			
			SeizureDTO seizureDTO = seizureMapper.toSeizureDTO(embargo);
			seizureDTOList.add(seizureDTO);
		}
		
		return seizureDTOList;
	}
	
	@Override
	public SeizureDTO getSeizureById(Long idSeizure) {

		SeizureDTO seizureDTO = null;
		
		//Optional<Embargo> embargoOpt = seizureRepository.findById(idSeizure);
		
		//if(embargoOpt.isPresent()) {
			
		//	seizureDTO = seizureMapper.toSeizureDTO(embargoOpt.get());
		//}
		
		Optional<Traba> trabaOpt = seizedRepository.findById(idSeizure);
		
		if(trabaOpt.isPresent()) {
			
			seizureDTO = seizureMapper.toSeizureDTO(trabaOpt.get().getEmbargo());
		}
		
		return seizureDTO;
	}

	@Override
	public List<SeizedBankAccountDTO> getBankAccountListBySeizure(Long codeFileControl,
			Long idSeizure) {
		
		List<SeizedBankAccountDTO> seizedBankAccountDTOList = new ArrayList<>();
		
		Traba traba = new Traba();
		traba.setCodTraba(idSeizure);
		
		List<CuentaTraba> cuentaTrabaList = seizedBankAccountRepository.findAllByTraba(traba);
		
		for(CuentaTraba cuentaTraba : cuentaTrabaList) {
			
			SeizedBankAccountDTO seizedBankAccountDTO = seizedBankAccountMapper.toSeizedBankAccountDTO(cuentaTraba);
			seizedBankAccountDTOList.add(seizedBankAccountDTO);
		}
		
		return seizedBankAccountDTOList;
	}
	
	
	@Override
	public List<SeizureActionDTO> getSeizureActions() {		
		
		List<SeizureActionDTO> seizureActionDTOList = new ArrayList<>();
				
		List<CuentaTrabaActuacion> cuentaTrabaActuacionList = seizedBankAccountActionRepository.findAll();
		
		for(CuentaTrabaActuacion cuentaTrabaActuacion : cuentaTrabaActuacionList) {
			
			SeizureActionDTO seizedBankAccountDTO = seizedBankAccountActionMapper.toSeizureActionDTO(cuentaTrabaActuacion);
			seizureActionDTOList.add(seizedBankAccountDTO);
		}
		
		return seizureActionDTOList;
	}
	
	@Override
	public List<SeizureStatusDTO> getSeizureStatusList() {
		
		List<SeizureStatusDTO> seizureStatusDTOList = new ArrayList<>();
		
		List<EstadoTraba> estadoTrabaList = seizureStatusRepository.findAllVisibleToUser();
		
		for(EstadoTraba estadoTraba : estadoTrabaList) {
			
			SeizureStatusDTO seizureStatusDTO = seizureStatusMapper.toSeizureStatusDTO(estadoTraba);
			seizureStatusDTOList.add(seizureStatusDTO);
		}
		
		return seizureStatusDTOList;

	}

	@Override
	public boolean updateSeizedBankAccountList(Long codeFileControl, Long idSeizure,
			List<SeizedBankAccountDTO> seizedBankAccountDTOList, String userModif) {


		Optional<Traba> trabaOpt = seizedRepository.findById(idSeizure);
		
		if (trabaOpt.isPresent()) {
		
			//Actualizacion usuario y fecha de ultima modificacion de la traba:
			Traba traba = trabaOpt.get();
			BigDecimal fechaActualBigDec = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
			traba.setUsuarioUltModificacion(userModif);
			traba.setFUltimaModificacion(fechaActualBigDec);
			
			//Actualizar usuario y fecha ultima modificacion del Embargo (para que se inserte registro en el historico de embargos):			
			Embargo embargo = traba.getEmbargo();
			embargo.setUsuarioUltModificacion(userModif);
			embargo.setFUltimaModificacion(fechaActualBigDec);
			
			//Calculo del importe trabado: sumar los importes trabados de cada cuenta:
			BigDecimal importeTrabado = new BigDecimal(0);
			for (SeizedBankAccountDTO seizedBankAccountDTO : seizedBankAccountDTOList) {
				BigDecimal importeTrabadoBankAccount = seizedBankAccountDTO.getAmount()!=null ? seizedBankAccountDTO.getAmount() :BigDecimal.valueOf(0);
				importeTrabado = importeTrabado.add(importeTrabadoBankAccount);
			}
			traba.setImporteTrabado(importeTrabado);
			
			seizedRepository.save(traba);
		}
		
		for (SeizedBankAccountDTO seizedBankAccountDTO : seizedBankAccountDTOList) {
			
			Optional<CuentaTraba> cuentaTrabaOpt = seizedBankAccountRepository.findById(seizedBankAccountDTO.getIdSeizedBankAccount());
			
			if (cuentaTrabaOpt.isPresent()) {
				
				//Actualizar los campos informados
				CuentaTraba cuentaTraba = seizedBankAccountMapper.toCuentaTrabaForUpdate(seizedBankAccountDTO, cuentaTrabaOpt.get(), userModif);
								
				seizedBankAccountRepository.save(cuentaTraba);
				
			}
		}
		
		
		return true;
	}
	
	
	@Override
	public boolean updateSeizureStatus(Long codeFileControl, Long idSeized, SeizureStatusDTO seizureStatusDTO,
			String userModif) {

		Optional<Traba> trabaOpt = seizedRepository.findById(idSeized);
		
		if(!trabaOpt.isPresent()) {
			return false;
		}
		
		if(seizureStatusDTO!=null && seizureStatusDTO.getCode()!=null) {
			
			Traba traba = trabaOpt.get();
			
			//Actualizar estado:
			EstadoTraba estadoTraba = new EstadoTraba();
			estadoTraba.setCodEstado(Long.valueOf(seizureStatusDTO.getCode()));
			
			traba.setEstadoTraba(estadoTraba);
									
			//Usuario y fecha ultima modificacion de la Traba:
			BigDecimal fechaActualBigDec = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
			traba.setUsuarioUltModificacion(userModif);
			traba.setFUltimaModificacion(fechaActualBigDec);
			
			//Actualizar usuario y fecha ultima modificacion del Embargo (para que se inserte registro en el historico de embargos):			
			Embargo embargo = traba.getEmbargo();
			embargo.setUsuarioUltModificacion(userModif);
			embargo.setFUltimaModificacion(fechaActualBigDec);
			
			seizedRepository.save(traba);
		
		} else {			
			return false;
		}
		
		return true;
	}
	
	
	@Override
	public List<SeizureDTO> getAuditSeizure(Long codFileControl, Long idSeizure) {
		
		List<SeizureDTO> seizureDTOList = new ArrayList<>();
		
		List<HTraba> hSeizureDTOList = hSeizedRepository.findByIdSeizure(idSeizure);
		
		for (HTraba hTraba : hSeizureDTOList) {
			
			SeizureDTO seizureDTO = hSeizureMapper.toSeizureDTO(hTraba.getHEmbargo());
			
			seizureDTOList.add(seizureDTO);
		}
		
		return seizureDTOList;
	}
	
	@Override
	public List<SeizedBankAccountDTO> getAuditSeizedBankAccounts(Long codFileControl, Long idSeizure, Long codAudit) {

		List<SeizedBankAccountDTO> seizureDTOList = new ArrayList<>();
		
		//Traba traba = new Traba();
		//traba.setCodTraba(idSeizure);

		//List<HCuentaTraba> hSeizedBankAccountDTOList = hSeizedBankAccountRepository.findAllByTraba(traba);
		List<HCuentaTraba> hSeizedBankAccountDTOList = hSeizedBankAccountRepository.findAllByCodTrabaAndCodAudit(BigDecimal.valueOf(idSeizure), codAudit);
		
		for (HCuentaTraba hCuentaTraba : hSeizedBankAccountDTOList) {
			
			SeizedBankAccountDTO seizedBankAccountDTO = hSeizedBankAccountMapper.toSeizedBankAccountDTO(hCuentaTraba);
			
			seizureDTOList.add(seizedBankAccountDTO);
		}
		
		return seizureDTOList;
	}

	
	
	@Override
	public byte[] generateJustificanteEmbargo(Integer idSeizure) throws Exception {

		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource embargosJrxml = ResourcesUtil.getFromJasperFolder("justificante_embargos.jasper");
			Resource logoImage = ResourcesUtil.getImageLogoCommerceResource();
			// Resource templateStyle = ResourcesUtil.getTemplateStyleResource();

			File image = logoImage.getFile();

			// InputStream templateStyleStream =
			// getClass().getResourceAsStream("/jasper/CommerzBankStyle.jrtx");

			parameters.put("COD_TRABA", idSeizure);
			parameters.put("IMAGE_PARAM", image.toString());
			// parameters.put("TEMPLATE_STYLE_PATH", templateStyleStream);

			InputStream justificanteInputStream = embargosJrxml.getInputStream();

			JasperPrint fillReport = JasperFillManager.fillReport(justificanteInputStream, parameters, conn);

			return JasperExportManager.exportReportToPdf(fillReport);

		} catch (SQLException e) {
			throw new Exception("DB exception while generating the report", e);
		}
	}
	
	@Override
	public byte[] generateLevantamientoReport(Integer idLifting) throws Exception {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource embargosJrxml = ResourcesUtil.getFromJasperFolder("levantamiento_embargo.jasper");
			Resource logoImage = ResourcesUtil.getImageLogoCommerceResource();

			File image = logoImage.getFile();

			parameters.put("COD_LEVANTAMIENTO", idLifting);
			parameters.put("IMAGE_PARAM", image.toString());

			InputStream justificanteInputStream = embargosJrxml.getInputStream();

			JasperPrint fillReport = JasperFillManager.fillReport(justificanteInputStream, parameters, conn);

			return JasperExportManager.exportReportToPdf(fillReport);

		} catch (SQLException e) {
			System.out.println(e);
			throw new Exception("DB exception while generating the report", e);
		}
	}


	@Override
	public byte[] generarResumenTrabasF4(Integer codControlFichero) throws Exception {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource resumenTrabasJrxml = ResourcesUtil.getFromJasperFolder("resumen_trabas_seizures.jasper");

			Resource logoImage = ResourcesUtil.getImageLogoCommerceResource();

			File image = logoImage.getFile();

			parameters.put("IMAGE_PARAM", image.toString());
			parameters.put("COD_FILE_CONTROL", codControlFichero);

			InputStream resumenInputStream = resumenTrabasJrxml.getInputStream();

			JasperPrint fillReport = JasperFillManager.fillReport(resumenInputStream, parameters, conn);

			return JasperExportManager.exportReportToPdf(fillReport);

		} catch (SQLException e) {
			throw new Exception("DB exception while generating the report", e);
		}
	}

	@Override
	public byte[] generarResumenTrabasF3(Integer codControlFichero) throws Exception {

		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource resumenTrabasJrxml = ResourcesUtil.getFromJasperFolder("resumen_trabas_request.jasper");
			
			Resource headerSubreport = ResourcesUtil.getReportHeaderResource();

			Resource logoImage = ResourcesUtil.getImageLogoCommerceResource();

			File image = logoImage.getFile();

			InputStream subReportHeaderInputStream = headerSubreport.getInputStream();

			JasperReport subReportHeader = (JasperReport) JRLoader.loadObject(subReportHeaderInputStream);

			parameters.put("sub_img_param", image.toString());
			parameters.put("SUBREPORT_HEADER", subReportHeader);
			parameters.put("COD_FILE_CONTROL", codControlFichero);

			InputStream resumenInputStream = resumenTrabasJrxml.getInputStream();

			JasperPrint fillReport = JasperFillManager.fillReport(resumenInputStream, parameters, conn);

			return JasperExportManager.exportReportToPdf(fillReport);

		} catch (SQLException e) {
			throw new Exception("DB exception while generating the report", e);
		}

	}

	@Override
	public byte[] generarAnexo(BigDecimal cod_usuario, BigDecimal cod_traba, Integer num_anexo) throws Exception {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn_comunes = oracleDataSourceEmbargos.getComunesConnection();
				Connection conn_embargos = oracleDataSourceEmbargos.getEmbargosConnection();) {

			Resource anexoJasperFile = ResourcesUtil.getFromJasperFolder("anexo" + num_anexo + ".jasper");

			Resource importeAbonadoSubReport = ResourcesUtil.getFromJasperFolder("importe_abonado.jasper");

			Resource logoImage = ResourcesUtil.getImageLogoCommerceResource();

			File image = logoImage.getFile();


	
			InputStream importeAbonadoInputStream = importeAbonadoSubReport.getInputStream();
			JasperReport importeAbonadoReport = (JasperReport) JRLoader.loadObject(importeAbonadoInputStream);

			parameters.put("IMAGE_PARAM", image.toString());
			parameters.put("REPORT_IMPORTE_ABONADO", importeAbonadoReport);
			parameters.put("COD_USUARIO", cod_usuario);
			parameters.put("COD_TRABA", cod_traba);
			parameters.put("REPORT_CONNECTION_EMB", conn_embargos);

			InputStream anexo1Input = anexoJasperFile.getInputStream();
			JasperPrint fillReport = JasperFillManager.fillReport(anexo1Input, parameters, conn_comunes);

			return JasperExportManager.exportReportToPdf(fillReport);

		} catch (SQLException e) {
			System.out.println(e);
			throw new Exception("DB exception while generating the report", e);
		}
	}
	
	@Override
	public byte[] generarRespuestaFinalEmbargo(Integer cod_file_control) throws Exception {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn_embargos = oracleDataSourceEmbargos.getEmbargosConnection()) {
			
			Resource respFinalEmbargoResource = ResourcesUtil.getFromJasperFolder("respuestaFinalEmbargos.jasper");
			Resource headerResource = ResourcesUtil.getReportHeaderResource();
			Resource imageResource = ResourcesUtil.getImageLogoCommerceResource();
			
			File image = imageResource.getFile();
			
			InputStream subReportHeaderInputStream = headerResource.getInputStream();

			JasperReport subReportHeader = (JasperReport) JRLoader.loadObject(subReportHeaderInputStream);
			
			parameters.put("sub_img_param", image.toString());
			parameters.put("SUBREPORT_HEADER", subReportHeader);
			parameters.put("COD_FILE_CONTROL", cod_file_control);
			
			
			InputStream finalEmbargosIS = respFinalEmbargoResource.getInputStream();
			JasperPrint fillReport = JasperFillManager.fillReport(finalEmbargosIS, parameters, conn_embargos);
			
			
			
			return JasperExportManager.exportReportToPdf(fillReport);
		} catch (SQLException e) {
			throw new Exception("DB exception while generating the report", e);
		}
	}

	@Override
	public byte[] generarResumenLevantamiento(Integer cod_file_control) throws Exception {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		try (Connection conn_embargos = oracleDataSourceEmbargos.getEmbargosConnection()) {
			
			Resource resumenLevantamiento = ResourcesUtil.getFromJasperFolder("resumen_levantamientos.jasper");
			Resource headerResource = ResourcesUtil.getReportHeaderResource();
			Resource imageResource = ResourcesUtil.getImageLogoCommerceResource();
			
			File image = imageResource.getFile();
			
			InputStream subReportHeaderInputStream = headerResource.getInputStream();

			JasperReport subReportHeader = (JasperReport) JRLoader.loadObject(subReportHeaderInputStream);
			
			parameters.put("sub_img_param", image.toString());
			parameters.put("SUBREPORT_HEADER", subReportHeader);
			parameters.put("COD_FILE_CONTROL", cod_file_control);
			
			
			InputStream finalEmbargosIS = resumenLevantamiento.getInputStream();
			JasperPrint fillReport = JasperFillManager.fillReport(finalEmbargosIS, parameters, conn_embargos);
			
			
			
			return JasperExportManager.exportReportToPdf(fillReport);
		} catch (SQLException e) {
			throw new Exception("DB exception while generating the report", e);
		}
	}
}
