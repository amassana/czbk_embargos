package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.SeizedBankAccountDTO;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.EstadoTraba;
import es.commerzbank.ice.utils.EmbargosConstants;
import es.commerzbank.ice.utils.ICEDateUtils;

@Mapper(componentModel="spring")
public abstract class  SeizedBankAccountMapper {

	@Mappings({
		@Mapping (source = "codCuentaTraba", target = "idSeizedBankAccount"),
		@Mapping (source = "iban", target = "iban"),
		@Mapping (source = "estadoTraba.codEstado", target = "bankAccountStatus"),
		@Mapping (source = "importe", target = "amount"),
		@Mapping (source = "cambio", target = "fxRate"),
		@Mapping (source = "actuacion", target = "seizureAction.code"),
		@Mapping (source = "traba.estadoTraba.codEstado", target = "seizureStatus.code"),
	})
	public abstract SeizedBankAccountDTO toSeizedBankAccountDTO (CuentaTraba cuentaTraba);

	@AfterMapping
	protected void setSeizedBankAccountDTAfterMapping(@MappingTarget SeizedBankAccountDTO seizedBankAccountDTO, CuentaTraba cuentaTraba) {

		seizedBankAccountDTO.setBankAccountCurrency(EmbargosConstants.ISO_MONEDA_EUR);
		
	}
	
	
	
	public CuentaTraba toCuentaTrabaForUpdate(SeizedBankAccountDTO seizedBankAccountDTO, CuentaTraba cuentaTraba, String userModif) {
			
		if (seizedBankAccountDTO.getAmount()!=null) {
			cuentaTraba.setImporte(seizedBankAccountDTO.getAmount());
		}		
		if (seizedBankAccountDTO.getSeizureAction()!=null) {
			cuentaTraba.setActuacion(seizedBankAccountDTO.getSeizureAction().getCode());
		}
		if (seizedBankAccountDTO.getFxRate()!=null) {
			cuentaTraba.setCambio(seizedBankAccountDTO.getFxRate());
		}
		if (seizedBankAccountDTO.getIban()!=null) {
			cuentaTraba.setIban(seizedBankAccountDTO.getIban());
		}
		if (seizedBankAccountDTO.getSeizureStatus()!=null && seizedBankAccountDTO.getSeizureStatus().getCode()!=null) {
			
			//Estado de la traba:
			EstadoTraba estadoTraba = new EstadoTraba();
			estadoTraba.setCodEstado(Long.valueOf(seizedBankAccountDTO.getSeizureStatus().getCode()));
			
			cuentaTraba.getTraba().setEstadoTraba(estadoTraba);
		}	
		if (seizedBankAccountDTO.getBankAccountCurrency()!=null) {

			//TODO: Pendiente de agregar campo del tipo de divisa en la cuenta de la traba.
		}
		if (seizedBankAccountDTO.getBankAccountStatus()!=null) {		
			
			//Estado de la cuenta:
			EstadoTraba estadoTraba = new EstadoTraba();
			estadoTraba.setCodEstado(Long.valueOf(seizedBankAccountDTO.getBankAccountStatus()));
			
			cuentaTraba.setEstadoTraba(estadoTraba);	
			
		}
		
		cuentaTraba.setUsuarioUltModificacion(userModif);
		cuentaTraba.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
		
		return cuentaTraba;
	}
	
}
