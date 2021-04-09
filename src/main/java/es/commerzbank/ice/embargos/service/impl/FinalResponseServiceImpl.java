package es.commerzbank.ice.embargos.service.impl;

import es.commerzbank.ice.comun.lib.typeutils.ICEDateUtils;
import es.commerzbank.ice.embargos.config.OracleDataSourceEmbargosConfig;
import es.commerzbank.ice.embargos.domain.dto.FinalResponseBankAccountDTO;
import es.commerzbank.ice.embargos.domain.dto.FinalResponseDTO;
import es.commerzbank.ice.embargos.domain.entity.*;
import es.commerzbank.ice.embargos.domain.mapper.FinalResponseBankAccountMapper;
import es.commerzbank.ice.embargos.domain.mapper.FinalResponseMapper;
import es.commerzbank.ice.embargos.repository.FinalFileRepository;
import es.commerzbank.ice.embargos.repository.FinalResponseBankAccountRepository;
import es.commerzbank.ice.embargos.repository.FinalResponseRepository;
import es.commerzbank.ice.embargos.service.FinalResponseService;
import es.commerzbank.ice.embargos.utils.ResourcesUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@Service
@Transactional(transactionManager = "transactionManager")
public class FinalResponseServiceImpl implements FinalResponseService {
	private static final Logger logger = LoggerFactory.getLogger(FinalResponseServiceImpl.class);

	// Repositorio RESULTADO_EMBARGO
	@Autowired
	private FinalResponseRepository finalResponseRepository;

	// Repositorio FINAL_FILE
	@Autowired
	private FinalFileRepository finalFileRepository;

	@Autowired
	private FinalResponseMapper finalResponseMapper;

	@Autowired
	private FinalResponseBankAccountRepository bankAccountRepository;

	@Autowired
	private FinalResponseBankAccountMapper bankAccountMapper;

	@Autowired
	OracleDataSourceEmbargosConfig oracleDataSourceEmbargos;

	@Override
	public List<FinalResponseDTO> getAllByControlFichero(ControlFichero controlFichero) {
		List<ResultadoEmbargo> result = finalResponseRepository.findAllByControlFichero(controlFichero);

		List<FinalResponseDTO> response = new ArrayList<FinalResponseDTO>();
		for (ResultadoEmbargo resultado : result) {
			FinalResponseDTO element = finalResponseMapper.toFinalResponse(resultado);
			response.add(element);
		}

		return response;
	}

	@Override
	public FinalResponseDTO AddBankAccountList(Long codeFileControl, Long codeFinalResponse) {
		List<FinalResponseBankAccountDTO> list = new ArrayList<>();
		FinalResponseDTO response = null;

		Optional<ResultadoEmbargo> result = finalResponseRepository.findById(codeFinalResponse);

		if (result != null) {
			response = finalResponseMapper.toFinalResponse(result.get());

			ResultadoEmbargo resultado = new ResultadoEmbargo();
			resultado.setCodResultadoEmbargo(codeFinalResponse);

			List<CuentaResultadoEmbargo> cuentas = bankAccountRepository.findByResultadoEmbargo(resultado);

			for (CuentaResultadoEmbargo cuenta : cuentas) {
				FinalResponseBankAccountDTO account = bankAccountMapper.toBankAccount(cuenta);
				if (account.getAmountLocked()!=null && account.getAmountTransfer()!=null)
					account.setAmountRaised(account.getAmountLocked() - account.getAmountTransfer());
				list.add(account);
			}

			response.setList(list);
		}

		return response;
	}

	@Override
	public byte[] generarAnexo(BigDecimal cod_usuario, BigDecimal cod_traba, Integer num_anexo) throws Exception {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn_embargos = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource anexoJasperFile = ResourcesUtil.getFromJasperFolder("TGSSAnexo" + num_anexo + ".jasper");

			Resource importeAbonadoSubReport = ResourcesUtil.getFromJasperFolder("importe_abonado.jasper");

			Resource logoImage = ResourcesUtil.getImageLogoCommerceResource();

			File image = logoImage.getFile();

			InputStream importeAbonadoInputStream = importeAbonadoSubReport.getInputStream();
			JasperReport importeAbonadoReport = (JasperReport) JRLoader.loadObject(importeAbonadoInputStream);

			parameters.put("IMAGE_PARAM", image.toString());
			parameters.put("REPORT_IMPORTE_ABONADO", importeAbonadoReport);
			parameters.put("COD_USUARIO", cod_usuario);
			parameters.put("COD_TRABA", cod_traba);

			parameters.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));
	

			InputStream anexo1Input = anexoJasperFile.getInputStream();
			JasperPrint fillReport = JasperFillManager.fillReport(anexo1Input, parameters, conn_embargos);

			List<JRPrintPage> pages = fillReport.getPages();

			if (pages.size() == 0)
				return null;

			return JasperExportManager.exportReportToPdf(fillReport);

		} catch (SQLException e) {
			throw new Exception("DB exception while generating the report", e);
		}
	}

	@Override
	public byte[] generarRespuestaFinalEmbargo(Integer cod_file_control) throws Exception {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn_embargos = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource respFinalEmbargoResource = ResourcesUtil.getFromJasperFolder("f6_finalization.jasper");
			Resource headerResource = ResourcesUtil.getReportHeaderResource();
			Resource imageResource = ResourcesUtil.getImageLogoCommerceResource();

			File image = imageResource.getFile();

			InputStream subReportHeaderInputStream = headerResource.getInputStream();

			JasperReport subReportHeader = (JasperReport) JRLoader.loadObject(subReportHeaderInputStream);

			parameters.put("sub_img_param", image.toString());
			parameters.put("SUBREPORT_HEADER", subReportHeader);
			parameters.put("COD_FILE_CONTROL", cod_file_control);

			parameters.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));

			InputStream finalEmbargosIS = respFinalEmbargoResource.getInputStream();
			JasperPrint fillReport = JasperFillManager.fillReport(finalEmbargosIS, parameters, conn_embargos);

			List<JRPrintPage> pages = fillReport.getPages();

			if (pages.size() == 0)
				return null;

			return JasperExportManager.exportReportToPdf(fillReport);
		} catch (Exception e) {
			throw new Exception("DB exception while generating the report", e);
		}
	}

	@Override
	public byte[] generatePaymentLetterCGPJ(String cod_traba) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn_embargos = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource transOrder = ResourcesUtil.getFromJasperFolder("paymentLetterCGPJ.jasper");
			Resource imageLogo = ResourcesUtil.getImageLogoCommerceResource();

			File logoFile = imageLogo.getFile();

			parameters.put("IMAGE_PARAM", logoFile.toString());
			parameters.put("COD_TRABA", cod_traba);

			parameters.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));

			InputStream isOrdenTransferencia = transOrder.getInputStream();
			JasperPrint transOrderJP = JasperFillManager.fillReport(isOrdenTransferencia, parameters, conn_embargos);

			List<JRPrintPage> pages = transOrderJP.getPages();

			if (pages.size() == 0)
				return null;

			return JasperExportManager.exportReportToPdf(transOrderJP);

		} catch (Exception e) {
			throw new Exception("DB exception while generating the report", e);
		}

	}
	

	@Override
	public byte[] generatePaymentLetterN63(String cod_control_fichero) throws Exception {

		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn_embargos = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource transOrder = ResourcesUtil.getFromJasperFolder("paymentLetterN63.jasper");
			Resource imageLogo = ResourcesUtil.getImageLogoCommerceResource();

			File logoFile = imageLogo.getFile();

			parameters.put("IMAGE_PARAM", logoFile.toString());
			parameters.put("COD_CONTROL_FICHERO", cod_control_fichero);

			parameters.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));

			InputStream isOrdenTransferencia = transOrder.getInputStream();
			JasperPrint transOrderJP = JasperFillManager.fillReport(isOrdenTransferencia, parameters, conn_embargos);
			
			List<JRPrintPage> pages = transOrderJP.getPages();
			 
			 if (pages.size() == 0)  return null;

			return JasperExportManager.exportReportToPdf(transOrderJP);

		} catch (SQLException e) {
			throw new Exception("DB exception while generating the report", e);
		}

	}

	@Override
	public boolean updateFinalFileAccountingStatus(FicheroFinal ficheroFinal, Long codEstadoContabilizacion,
												   String userName) {

		EstadoContabilizacion estadoContabilizacion = new EstadoContabilizacion();
		estadoContabilizacion.setCodEstado(codEstadoContabilizacion);

		ficheroFinal.setEstadoContabilizacion(estadoContabilizacion);

		//Usuario y fecha de ultima modificacion:
		ficheroFinal.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
		ficheroFinal.setUsuarioUltModificacion(userName);

		finalFileRepository.save(ficheroFinal);

		return false;
	}
}
