package es.commerzbank.ice.embargos.service.impl;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import es.commerzbank.ice.embargos.config.OracleDataSourceEmbargosConfig;
import es.commerzbank.ice.embargos.service.SeizureService;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
@Transactional
public class SeizureServiceImpl implements SeizureService {
	
	
	@Autowired
	private OracleDataSourceEmbargosConfig oracleDataSourceEmbargos;

	@Override
	public byte[] generateJustificanteEmbargo(Integer codEmbargo) throws Exception {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		
		
				try (Connection conn = oracleDataSourceEmbargos.getEmbargosConnection()) {
					
					Resource embargosJrxml = new ClassPathResource("jasper/justificante_embargos.jasper");
					Resource logoImage = new ClassPathResource("jasper/images/commerce_bank_logo.png");
					
					File image = logoImage.getFile(); 
					
					parameters.put("COD_EMBARGO", codEmbargo);
					parameters.put("IMAGE_PARAM", image.toString());
					
					InputStream justificanteInputStream = embargosJrxml.getInputStream();
					
					
					JasperPrint fillReport = JasperFillManager.fillReport(justificanteInputStream, parameters, conn);
					
					return JasperExportManager.exportReportToPdf(fillReport);
					
				} catch (SQLException e) {
					throw new Exception("DB exception while generating the report", e);
				}
	}

}
