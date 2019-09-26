package es.commerzbank.ice.embargos.service.impl;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.embargos.config.OracleDataSourceEmbargosConfig;
import es.commerzbank.ice.embargos.service.SeizureSummaryService;
import es.commerzbank.ice.utils.ResourcesUtil;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
@Transactional(transactionManager = "transactionManager")
public class SeizureSummaryImpl implements SeizureSummaryService {
	
	
	@Autowired
	OracleDataSourceEmbargosConfig oracleDataSourceComunes;


}
