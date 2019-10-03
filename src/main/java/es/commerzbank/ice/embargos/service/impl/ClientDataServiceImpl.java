package es.commerzbank.ice.embargos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.domain.entity.DatosCliente;
import es.commerzbank.ice.embargos.domain.mapper.ClientDataMapper;
import es.commerzbank.ice.embargos.repository.ClientDataRepository;
import es.commerzbank.ice.embargos.service.ClientDataService;
import es.commerzbank.ice.utils.EmbargosConstants;
import es.commerzbank.ice.utils.ICEDateUtils;

@Service
@Transactional(transactionManager="transactionManager")
public class ClientDataServiceImpl implements ClientDataService {

	@Autowired
	private ClientDataMapper clientDataMapper;
	
	@Autowired
	private ClientDataRepository clientDataRepository; 
	
	@Override
	@Transactional(transactionManager="transactionManager", propagation = Propagation.REQUIRES_NEW)
	public void createUpdateClientDataTransaction(CustomerDTO customerDTO, String nif) {
		
		DatosCliente datosCliente = null;

		//Se comprueba que si customerDTO es null, entonces no se ha obtenido los datos de DWH del cliente a partir del NIF. En ese
		//caso, se tiene que guardar el registro en DatosCliente, donde se informara el NIF que viene por parametro.
		if (customerDTO!=null) {

			datosCliente = clientDataMapper.toDatosCliente(customerDTO);
		
		} else {
			
			//De los datos del cliente solo se setea el NIF en el registro:
			datosCliente = new DatosCliente();
			datosCliente.setNif(nif);
			
			datosCliente.setUsuarioUltModificacion(EmbargosConstants.USER_AUTOMATICO);
			datosCliente.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
		}
		
		//En la creacion/actualizacion de DatosCliente, el campo indPendienteDwh se setea siempre a 'N':
		datosCliente.setIndPendienteDwh(EmbargosConstants.IND_FLAG_NO);
		
		clientDataRepository.save(datosCliente);
	}
}
