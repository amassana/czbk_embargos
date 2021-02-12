package es.commerzbank.ice.embargos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.embargos.config.OracleDataSourceEmbargosConfig;
import es.commerzbank.ice.embargos.service.SeizureSummaryService;

@Service
@Transactional(transactionManager = "transactionManager")
public class SeizureSummaryImpl implements SeizureSummaryService {
	
	
	@Autowired
	OracleDataSourceEmbargosConfig oracleDataSourceComunes;


}
