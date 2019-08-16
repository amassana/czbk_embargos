package es.commerzbank.ice.embargos.service.impl;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.embargos.service.ConsejoGeneralPoderJudicialService;

@Service
@Transactional(transactionManager="transactionManager")
public class ConsejoGeneralPoderJudicialServiceImpl implements ConsejoGeneralPoderJudicialService{

}
