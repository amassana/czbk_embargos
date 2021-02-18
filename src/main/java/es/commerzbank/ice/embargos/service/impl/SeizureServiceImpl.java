package es.commerzbank.ice.embargos.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import es.commerzbank.ice.comun.lib.util.ValueConstants;
import net.sf.jasperreports.engine.*;
import org.apache.commons.codec.Charsets;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;

import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.config.OracleDataSourceEmbargosConfig;
import es.commerzbank.ice.embargos.domain.dto.SeizedBankAccountDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureActionDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureSaveDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureStatusDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.CuentaEmbargo;
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
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.HSeizedBankAccountRepository;
import es.commerzbank.ice.embargos.repository.HSeizedRepository;
import es.commerzbank.ice.embargos.repository.SeizedBankAccountActionRepository;
import es.commerzbank.ice.embargos.repository.SeizedBankAccountRepository;
import es.commerzbank.ice.embargos.repository.SeizedRepository;
import es.commerzbank.ice.embargos.repository.SeizureRepository;
import es.commerzbank.ice.embargos.repository.SeizureStatusRepository;
import es.commerzbank.ice.embargos.repository.SeizureSummaryBankAccountRepository;
import es.commerzbank.ice.embargos.service.SeizureService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.EmbargosUtils;
import es.commerzbank.ice.embargos.utils.ICEDateUtils;
import es.commerzbank.ice.embargos.utils.ResourcesUtil;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
@Transactional(transactionManager = "transactionManager")
public class SeizureServiceImpl implements SeizureService {
	
	private static final Logger logger = LoggerFactory.getLogger(SeizureServiceImpl.class);

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
	GeneralParametersService generalParametersService;

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
	private FileControlRepository fileControlRepository;
	
	@Autowired
	private OracleDataSourceEmbargosConfig oracleDataSourceEmbargos;
	
	@Autowired
	private SeizureSummaryBankAccountRepository seizureSummaryBankAccountRepository;

	
	@Override
	public List<SeizureDTO> getSeizureListByCodeFileControl(Long codeFileControl) {
		logger.info("SeizureServiceImpl - getSeizureListByCodeFileControl - start");
		
		List<SeizureDTO> seizureDTOList = new ArrayList<>();

		ControlFichero controlFichero = new ControlFichero();
		controlFichero.setCodControlFichero(codeFileControl);

		List<Embargo> embargoList = seizureRepository.findAllByControlFichero(controlFichero);

		for (Embargo embargo : embargoList) {

			SeizureDTO seizureDTO = seizureMapper.toSeizureDTO(embargo);
			seizureDTOList.add(seizureDTO);
		}
		
		logger.info("SeizureServiceImpl - getSeizureListByCodeFileControl - end");
		return seizureDTOList;
	}

	@Override
	public SeizureDTO getSeizureById(Long idSeizure) {
		logger.info("SeizureServiceImpl - getSeizureById - start");
		SeizureDTO seizureDTO = null;

		// Optional<Embargo> embargoOpt = seizureRepository.findById(idSeizure);

		// if(embargoOpt.isPresent()) {

		// seizureDTO = seizureMapper.toSeizureDTO(embargoOpt.get());
		// }

		Optional<Traba> trabaOpt = seizedRepository.findById(idSeizure);

		if (trabaOpt.isPresent()) {

			seizureDTO = seizureMapper.toSeizureDTO(trabaOpt.get().getEmbargo());
		}
		
		logger.info("SeizureServiceImpl - getSeizureById - end");
		return seizureDTO;
	}

	@Override
	public List<SeizedBankAccountDTO> getBankAccountListBySeizure(Long codeFileControl,
			Long idSeizure) {
		logger.info("SeizureServiceImpl - getBankAccountListBySeizure - start");
		
		List<SeizedBankAccountDTO> seizedBankAccountDTOList = new ArrayList<>();

		Traba traba = new Traba();
		traba.setCodTraba(idSeizure);
		
		List<CuentaTraba> cuentaTrabaList = seizedBankAccountRepository.findAllByTrabaOrderByNumeroOrdenCuentaAsc(traba);
		
		for(CuentaTraba cuentaTraba : cuentaTrabaList) {
			
			SeizedBankAccountDTO seizedBankAccountDTO = seizedBankAccountMapper.toSeizedBankAccountDTO(cuentaTraba);
			seizedBankAccountDTOList.add(seizedBankAccountDTO);
		}
		
		logger.info("SeizureServiceImpl - getBankAccountListBySeizure - end");
		return seizedBankAccountDTOList;
	}

	@Override
	public List<SeizureActionDTO> getSeizureActions(Long codeFileControl) {		
		
		logger.info("SeizureServiceImpl - getSeizureActions - start");
		List<SeizureActionDTO> seizureActionDTOList = new ArrayList<>();
		
		Optional<ControlFichero> controlFicheroOpt = fileControlRepository.findById(codeFileControl);
		
		if (!controlFicheroOpt.isPresent()) {
			return seizureActionDTOList;
		}
		
		ControlFichero controlFichero = controlFicheroOpt.get();
		
		String tipoEntidad = EmbargosUtils.determineFileFormatByTipoFichero(controlFichero.getTipoFichero().getCodTipoFichero());
		
		List<CuentaTrabaActuacion> cuentaTrabaActuacionList = seizedBankAccountActionRepository.findAllByTipoEntidadOrderByCodActuacion(tipoEntidad);
		
		for(CuentaTrabaActuacion cuentaTrabaActuacion : cuentaTrabaActuacionList) {
			
			SeizureActionDTO seizedBankAccountDTO = seizedBankAccountActionMapper.toSeizureActionDTO(cuentaTrabaActuacion);
			seizureActionDTOList.add(seizedBankAccountDTO);
		}
		
		logger.info("SeizureServiceImpl - getSeizureActions - end");
		return seizureActionDTOList;
	}

	@Override
	public List<SeizureStatusDTO> getSeizureStatusList() {
		logger.info("SeizureServiceImpl - getSeizureStatusList - start");
		List<SeizureStatusDTO> seizureStatusDTOList = new ArrayList<>();

		List<EstadoTraba> estadoTrabaList = seizureStatusRepository.findAllVisibleToUser();

		for (EstadoTraba estadoTraba : estadoTrabaList) {

			SeizureStatusDTO seizureStatusDTO = seizureStatusMapper.toSeizureStatusDTO(estadoTraba);
			seizureStatusDTOList.add(seizureStatusDTO);
		}
		
		logger.info("SeizureServiceImpl - getSeizureStatusList - end");
		
		return seizureStatusDTOList;

	}

	@Override
	public boolean updateSeizedBankAccountList(Long codeFileControl, Long idSeizure,
			SeizureSaveDTO seizureSave, String userModif) throws Exception {
		logger.info("SeizureServiceImpl - updateSeizedBankAccountList - start");

		Optional<Traba> trabaOpt = seizedRepository.findById(idSeizure);

		if (trabaOpt.isPresent()) {

			// Actualizacion usuario y fecha de ultima modificacion de la traba:
			Traba traba = trabaOpt.get();
			BigDecimal fechaActualBigDec = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
			traba.setUsuarioUltModificacion(userModif);
			traba.setFUltimaModificacion(fechaActualBigDec);
			traba.setRevisado(seizureSave.isReviewed() ? EmbargosConstants.IND_FLAG_SI : EmbargosConstants.IND_FLAG_NO);

			// Actualizar usuario y fecha ultima modificacion del Embargo (para que se
			// inserte registro en el historico de embargos):
			Embargo embargo = traba.getEmbargo();
			embargo.setUsuarioUltModificacion(userModif);
			embargo.setFUltimaModificacion(fechaActualBigDec);

			// Calculo del importe trabado: sumar los importes trabados de cada cuenta:
			BigDecimal importeTrabado = new BigDecimal(0);
			for (SeizedBankAccountDTO seizedBankAccountDTO : seizureSave.getSeizedBankAccountList()) {
				BigDecimal importeTrabadoBankAccount = seizedBankAccountDTO.getAmount() != null
						? seizedBankAccountDTO.getAmount()
						: BigDecimal.valueOf(0);
				importeTrabado = importeTrabado.add(importeTrabadoBankAccount);
			}
			traba.setImporteTrabado(importeTrabado);

			seizedRepository.save(traba);
		}

		for (SeizedBankAccountDTO seizedBankAccountDTO : seizureSave.getSeizedBankAccountList()) {

			Optional<CuentaTraba> cuentaTrabaOpt = seizedBankAccountRepository
					.findById(seizedBankAccountDTO.getIdSeizedBankAccount());

			if (cuentaTrabaOpt.isPresent()) {

				// Actualizar los campos informados
				CuentaTraba cuentaTraba = seizedBankAccountMapper.toCuentaTrabaForUpdate(seizedBankAccountDTO,
						cuentaTrabaOpt.get(), userModif);

				seizedBankAccountRepository.save(cuentaTraba);

			}
		}
		
		logger.info("SeizureServiceImpl - updateSeizedBankAccountList - end");
		return true;
	}
	
	
	@Override
	public boolean updateSeizedBankStatus(CuentaTraba cuentaTraba, Long codEstado, String userModif) {

		logger.info("SeizureServiceImpl - updateSeizedBankStatus - start");
		Traba traba = cuentaTraba.getTraba();
		
		BigDecimal fechaActualBigDec = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
		
		traba.setUsuarioUltModificacion(userModif);
		traba.setFUltimaModificacion(fechaActualBigDec);
		
		//Actualizar usuario y fecha ultima modificacion del Embargo (para que se inserte registro en el historico de embargos):			
		Embargo embargo = traba.getEmbargo();
		embargo.setUsuarioUltModificacion(userModif);
		embargo.setFUltimaModificacion(fechaActualBigDec);
		
		seizedRepository.save(traba);
		
		//Estado de la cuenta traba:
		EstadoTraba estadoTraba = new EstadoTraba();
		estadoTraba.setCodEstado(codEstado);
		
		cuentaTraba.setEstadoTraba(estadoTraba);
		cuentaTraba.setUsuarioUltModificacion(userModif);
		cuentaTraba.setFUltimaModificacion(fechaActualBigDec);
								
		seizedBankAccountRepository.save(cuentaTraba);	
		
		logger.info("SeizureServiceImpl - updateSeizedBankStatus - end");
		return true;
	}
	
	@Override
	public boolean updateSeizedBankStatusTransaction(CuentaTraba cuentaTraba, Long codEstado, String userModif) {
		
		return updateSeizedBankStatus(cuentaTraba, codEstado, userModif);
		
	}
	
	@Override
	public boolean updateSeizureStatus(Long idSeized, SeizureStatusDTO seizureStatusDTO,
			String userModif) {
		logger.info("SeizureServiceImpl - updateSeizureStatus - start");
		Optional<Traba> trabaOpt = seizedRepository.findById(idSeized);

		if (!trabaOpt.isPresent()) {
			return false;
		}

		if (seizureStatusDTO != null && seizureStatusDTO.getCode() != null) {

			Traba traba = trabaOpt.get();

			// Actualizar estado:
			EstadoTraba estadoTraba = new EstadoTraba();
			estadoTraba.setCodEstado(Long.valueOf(seizureStatusDTO.getCode()));

			traba.setEstadoTraba(estadoTraba);

			// Usuario y fecha ultima modificacion de la Traba:
			BigDecimal fechaActualBigDec = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
			traba.setUsuarioUltModificacion(userModif);
			traba.setFUltimaModificacion(fechaActualBigDec);

			// Actualizar usuario y fecha ultima modificacion del Embargo (para que se
			// inserte registro en el historico de embargos):
			Embargo embargo = traba.getEmbargo();
			embargo.setUsuarioUltModificacion(userModif);
			embargo.setFUltimaModificacion(fechaActualBigDec);

			seizedRepository.save(traba);

		} else {
			return false;
		}
		
		logger.info("SeizureServiceImpl - updateSeizureStatus - end");
		return true;
	}
	
	@Override
	public boolean updateSeizureStatusTransaction(Long idSeized, SeizureStatusDTO seizureStatusDTO,
			String userModif) {
		
		return updateSeizureStatus(idSeized, seizureStatusDTO, userModif);
		
	}	
	
	@Override
	public List<SeizureDTO> getAuditSeizure(Long codFileControl, Long idSeizure) {
		logger.info("SeizureServiceImpl - getAuditSeizure - start");
		
		List<SeizureDTO> seizureDTOList = new ArrayList<>();

		List<HTraba> hSeizureDTOList = hSeizedRepository.findByIdSeizure(idSeizure);

		for (HTraba hTraba : hSeizureDTOList) {

			SeizureDTO seizureDTO = hSeizureMapper.toSeizureDTO(hTraba.getHEmbargo());

			seizureDTOList.add(seizureDTO);
		}
		
		logger.info("SeizureServiceImpl - getAuditSeizure - end");
		return seizureDTOList;
	}

	@Override
	public List<SeizedBankAccountDTO> getAuditSeizedBankAccounts(Long codFileControl, Long idSeizure, Long codAudit) {
		logger.info("SeizureServiceImpl - getAuditSeizedBankAccounts - start");
		List<SeizedBankAccountDTO> seizureDTOList = new ArrayList<>();

		// Traba traba = new Traba();
		// traba.setCodTraba(idSeizure);

		// List<HCuentaTraba> hSeizedBankAccountDTOList =
		// hSeizedBankAccountRepository.findAllByTraba(traba);
		List<HCuentaTraba> hSeizedBankAccountDTOList = hSeizedBankAccountRepository
				.findAllByCodTrabaAndCodAudit(BigDecimal.valueOf(idSeizure), codAudit);

		for (HCuentaTraba hCuentaTraba : hSeizedBankAccountDTOList) {

			SeizedBankAccountDTO seizedBankAccountDTO = hSeizedBankAccountMapper.toSeizedBankAccountDTO(hCuentaTraba);

			seizureDTOList.add(seizedBankAccountDTO);
		}
		
		logger.info("SeizureServiceImpl - getAuditSeizedBankAccounts - end");
		
		return seizureDTOList;
	}

	@Override
	public byte[] generateSeizureLetter(Integer idSeizure) throws Exception {
		logger.info("SeizureServiceImpl - generateJustificanteEmbargo - start");
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource embargosJrxml = ResourcesUtil.getFromJasperFolder("seizureLetter.jasper");
			Resource logoImage = ResourcesUtil.getImageLogoCommerceResource();
			// Resource templateStyle = ResourcesUtil.getTemplateStyleResource();

			File image = logoImage.getFile();

			// InputStream templateStyleStream =
			// getClass().getResourceAsStream("/jasper/CommerzBankStyle.jrtx");

			parameters.put("COD_TRABA", idSeizure);
			parameters.put("IMAGE_PARAM", image.toString());
			// parameters.put("TEMPLATE_STYLE_PATH", templateStyleStream);

			parameters.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));

			InputStream justificanteInputStream = embargosJrxml.getInputStream();

			JasperPrint fillReport = JasperFillManager.fillReport(justificanteInputStream, parameters, conn);

			List<JRPrintPage> pages = fillReport.getPages();

			if (pages.size() == 0)
				return null;

			logger.info("SeizureServiceImpl - generateJustificanteEmbargo - end");
			
			return JasperExportManager.exportReportToPdf(fillReport);

		} catch (Exception e) {

			throw new Exception("DB exception while generating the report", e);
		}
	}

	@Override
	public byte[] generateSeizureResponseF4(Integer codControlFichero) throws Exception {
		logger.info("SeizureServiceImpl - generarResumenTrabasF4 - start");
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource resumenTrabasJrxml = ResourcesUtil.getFromJasperFolder("f4_seizureResponse.jasper");

			Resource logoImage = ResourcesUtil.getImageLogoCommerceResource();

			File image = logoImage.getFile();

			parameters.put("IMAGE_PARAM", image.toString());
			parameters.put("COD_FILE_CONTROL", codControlFichero);

			parameters.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));

			InputStream resumenInputStream = resumenTrabasJrxml.getInputStream();

			JasperPrint fillReport = JasperFillManager.fillReport(resumenInputStream, parameters, conn);

			List<JRPrintPage> pages = fillReport.getPages();

			if (pages.size() == 0)
				return null;

			logger.info("SeizureServiceImpl - generarResumenTrabasF4 - end");
			
			return JasperExportManager.exportReportToPdf(fillReport);

		} catch (Exception e) {
			throw new Exception("DB exception while generating the report", e);
		}
	}

	@Override
	public byte[] generateSeizureRequestF3(Integer codControlFichero) throws Exception {
		logger.info("SeizureServiceImpl - generarResumenTrabasF3 - start");
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource resumenTrabasJrxml = ResourcesUtil.getFromJasperFolder("f3_seizureRequest.jasper");

			Resource headerSubreport = ResourcesUtil.getReportHeaderResource();

			Resource logoImage = ResourcesUtil.getImageLogoCommerceResource();

			File image = logoImage.getFile();

			InputStream subReportHeaderInputStream = headerSubreport.getInputStream();

			JasperReport subReportHeader = (JasperReport) JRLoader.loadObject(subReportHeaderInputStream);

			parameters.put("sub_img_param", image.toString());
			parameters.put("SUBREPORT_HEADER", subReportHeader);
			parameters.put("COD_FILE_CONTROL", codControlFichero);

			parameters.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));

			InputStream resumenInputStream = resumenTrabasJrxml.getInputStream();

			JasperPrint fillReport = JasperFillManager.fillReport(resumenInputStream, parameters, conn);

			List<JRPrintPage> pages = fillReport.getPages();

			if (pages.size() == 0)
				return null;

			logger.info("SeizureServiceImpl - generarResumenTrabasF3 - end");
			
			return JasperExportManager.exportReportToPdf(fillReport);

		} catch (SQLException e) {
			throw new Exception("DB exception while generating the report", e);
		}

	}

	@Override
	public List<Embargo> listEmbargosTransferToTax() {
		return seizureRepository.listEmbargosTransferToTax();
	}
	
	@Override
	public boolean jobTransferToTax(String authorization, String user) throws ICEException {
		// TODO: OBTENER EL IMPORTE DE LA TABLA resultado_embargo del campo total_neto
		List<Embargo> listaEmbargos = listEmbargosTransferToTax();
		if (listaEmbargos!=null) {
			for (Embargo embargo : listaEmbargos) {
				Long importe = obtenerImporteEmbargo(embargo);
				transferEmbargoToTax(embargo, importe, authorization, user);
			}
		}
		
		return true;
	}
	
	private Long obtenerImporteEmbargo(Embargo embargo) {
		Long importe = null;
		List<Long> importes = seizureSummaryBankAccountRepository.getImporteEmbargo(embargo.getCodEmbargo());
		if (importes!=null && importes.size()>0) importe = importes.get(0);
		return importe;
	}

	private boolean transferEmbargoToTax(Embargo embargo, Long importe, String authorization, String user) throws ICEException {
		boolean result = false;
		
		HttpClient httpClient = HttpClients.custom().build();
		HttpPost request = new HttpPost(generalParametersService.loadStringParameter(ValueConstants.PARAMETRO_TSP_DOMINIO) + generalParametersService.loadStringParameter(EmbargosConstants.ENDPOINT_EMBARGOS_TO_TAX));
		
		if (importe==null || Long.valueOf(0).equals(importe)) importe = embargo.getImporte().longValue();
		
		String cuenta = null;
		List <CuentaEmbargo> listaCuentas = embargo.getCuentaEmbargos();
		if (listaCuentas!=null && listaCuentas.size()>0) {
			cuenta = listaCuentas.get(0).getCuenta();
		}
		else {
			logger.error("Embargo" + embargo.getCodEmbargo() + " sin cuentas");
			return false;
		}
		
		String message = "{\"user\": \"" + user + "\", \"nombre\": \"" + embargo.getNombre() + "\", \"cuenta\": \"" + cuenta 
				+ "\", \"nif\": \"" + embargo.getNif() + "\", \"sucursal\": \"" + embargo.getCodSucursal()
				+ "\", \"importe\": \"" + importe + "\", \"numEmbargo\": \"" + embargo.getNumeroEmbargo() + "\"}";
		
		request.setEntity(new StringEntity(message, ContentType.create(MimeTypeUtils.TEXT_XML_VALUE, Charsets.UTF_8)));
		
		try {
			request.setHeader("Content-Type", "application/json");
			request.setHeader("Authorization", authorization);
			
			HttpResponse response = null;
	        try {
	            response = httpClient.execute(request);
	        } catch (ConnectTimeoutException | SocketTimeoutException ex) {
	        	logger.error("Error comunicacions timeout ", ex);
	        } catch (IOException ex) {
	        	logger.error("Error comunicacions ", ex);
	        }
	        
	        int statusCode = response.getStatusLine().getStatusCode();
	        if (statusCode == HttpStatus.SC_OK) {
	        	result = true;
	        }
	        
		} catch (Exception e) {
			logger.error("ERROR Extracting response", e);
        }
		
		return result;
	}

}
