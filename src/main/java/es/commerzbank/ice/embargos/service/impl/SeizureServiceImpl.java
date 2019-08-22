package es.commerzbank.ice.embargos.service.impl;

import java.io.File;
import java.io.InputStream;
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
			//Resource templateStyle = ResourcesUtil.getTemplateStyleResource();
			
			//System.out.println(templateStyle.getFile().getAbsolutePath());

			File image = logoImage.getFile();

			//InputStream templateStyleStream = getClass().getResourceAsStream("/jasper/CommerzBankStyle.jrtx");

			parameters.put("COD_TRABA", idSeizure);
			parameters.put("IMAGE_PARAM", image.toString());
			//parameters.put("TEMPLATE_STYLE_PATH", templateStyleStream);

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
			System.out.println(e);
			throw new Exception("DB exception while generating the report", e);
		}

	}

}
