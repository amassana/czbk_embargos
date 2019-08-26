package es.commerzbank.ice.embargos.service.impl;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import es.commerzbank.ice.embargos.config.OracleDataSourceEmbargosConfig;
import es.commerzbank.ice.embargos.service.SeizureService;
import es.commerzbank.ice.utils.ResourcesUtil;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
@Transactional
public class SeizureServiceImpl implements SeizureService {

	@Autowired
	private OracleDataSourceEmbargosConfig oracleDataSourceEmbargos;

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
	public byte[] generarResumenTrabas(Integer codControlFichero) throws Exception {

		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource resumenTrabasJrxml = ResourcesUtil.getFromJasperFolder("resumen_trabas.jasper");
			Resource totalTrabas = ResourcesUtil.getFromJasperFolder("totalTrabado.jasper");
			Resource headerSubreport = ResourcesUtil.getReportHeaderResource();

			Resource logoImage = ResourcesUtil.getImageLogoCommerceResource();

			File image = logoImage.getFile();

			InputStream subReportHeaderInputStream = headerSubreport.getInputStream();
			InputStream subReportTotalTrabasInputStream = totalTrabas.getInputStream();

			JasperReport subReportHeader = (JasperReport) JRLoader.loadObject(subReportHeaderInputStream);
			JasperReport subReportTotalTrabas = (JasperReport) JRLoader.loadObject(subReportTotalTrabasInputStream);

			parameters.put("sub_img_param", image.toString());
			parameters.put("SUBREPORT_HEADER", subReportHeader);
			parameters.put("SUBREPORT_IMPORTETOTALTRABADO", subReportTotalTrabas);
			parameters.put("COD_FILE_CONTROL", codControlFichero);

			InputStream resumenInputStream = resumenTrabasJrxml.getInputStream();

			JasperPrint fillReport = JasperFillManager.fillReport(resumenInputStream, parameters, conn);

			return JasperExportManager.exportReportToPdf(fillReport);

		} catch (SQLException e) {
			throw new Exception("DB exception while generating the report", e);
		}

	}

	@Override
	public byte[] generarAnexo(BigDecimal cod_usuario, BigDecimal cod_traba) throws Exception {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn_comunes = oracleDataSourceEmbargos.getComunesConnection();
				Connection conn_embargos = oracleDataSourceEmbargos.getEmbargosConnection();) {

			Resource anexoJasperFile = ResourcesUtil.getFromJasperFolder("anexo1.jasper");
			Resource importeAbonadoSubReport = ResourcesUtil.getFromJasperFolder("importe_abonado.jasper");
//			Resource simpleHeaderResource = ResourcesUtil.getSimpleHeader();
			Resource logoImage = ResourcesUtil.getImageLogoCommerceResource();

			File image = logoImage.getFile();

//			InputStream subReportSimpleHeaderInputStream = simpleHeaderResource.getInputStream();
//			JasperReport subReportSimpleHeader = (JasperReport) JRLoader.loadObject(subReportSimpleHeaderInputStream);
//		
			InputStream importeAbonadoInputStream = importeAbonadoSubReport.getInputStream();
			JasperReport importeAbonadoReport = (JasperReport) JRLoader.loadObject(importeAbonadoInputStream);

			parameters.put("IMAGE_PARAM", image.toString());
//			parameters.put("SUBREPORT_SIMPLE_HEADER", subReportSimpleHeader);
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

}
