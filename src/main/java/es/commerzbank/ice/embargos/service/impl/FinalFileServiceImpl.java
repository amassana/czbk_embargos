package es.commerzbank.ice.embargos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.comun.lib.typeutils.ICEDateUtils;
import es.commerzbank.ice.embargos.domain.entity.EstadoContabilizacion;
import es.commerzbank.ice.embargos.domain.entity.FicheroFinal;
import es.commerzbank.ice.embargos.repository.FinalFileRepository;
import es.commerzbank.ice.embargos.service.FinalFileService;

@Service
@Transactional(transactionManager="transactionManager")
public class FinalFileServiceImpl implements FinalFileService{
	
	@Autowired
	private FinalFileRepository finalFileRepository;
	
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
