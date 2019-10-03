package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.domain.entity.DatosCliente;
import es.commerzbank.ice.utils.EmbargosConstants;
import es.commerzbank.ice.utils.EmbargosUtils;
import es.commerzbank.ice.utils.ICEDateUtils;

@Mapper(componentModel="spring")
public abstract class ClientDataMapper {

	@Mappings({
		@Mapping(source = "customerDTO.nif", target = "nif"),
		@Mapping(source = "customerDTO.address", target = "domicilio"),
		@Mapping(source = "customerDTO.city", target = "municipio"),
		@Mapping(source = "customerDTO.postalCode", target = "codigoPostal"),
	})
	public abstract DatosCliente toDatosCliente(CustomerDTO customerDTO);
	
	@AfterMapping
	protected void setDatosClienteAfterMapping(@MappingTarget DatosCliente datosCliente, CustomerDTO customerDTO) {

		String name = EmbargosUtils.determineRazonSocialInternaFromCustomer(customerDTO);
		
		datosCliente.setNombre(name);
		
        //Usuario y fecha ultima modificacion:
		datosCliente.setUsuarioUltModificacion(EmbargosConstants.USER_AUTOMATICO);
		datosCliente.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
	}
}
