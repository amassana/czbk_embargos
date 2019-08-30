package es.commerzbank.ice.embargos.domain.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import es.commerzbank.ice.datawarehouse.domain.dto.AccountDTO;
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
		@Mapping (source = "estadoCuenta", target = "bankAccountStatus"),
		@Mapping (source = "importe", target = "amount"),
		@Mapping (source = "cambio", target = "fxRate"),
		@Mapping (source = "actuacion", target = "seizureAction.code"),
		@Mapping (source = "traba.estadoTraba.codEstado", target = "seizureStatus.code"),
		@Mapping (source = "divisa", target = "bankAccountCurrency"),
	})
	public abstract SeizedBankAccountDTO toSeizedBankAccountDTO (CuentaTraba cuentaTraba);

	@AfterMapping
	protected void setSeizedBankAccountDTAfterMapping(@MappingTarget SeizedBankAccountDTO seizedBankAccountDTO, CuentaTraba cuentaTraba) {

		
		boolean originEmb = cuentaTraba.getOrigenEmb()!=null && EmbargosConstants.IND_FLAG_YES.equals(cuentaTraba.getOrigenEmb());
		seizedBankAccountDTO.setOriginEmb(originEmb);
		
		boolean addToSeized = cuentaTraba.getAgregarATraba()!=null && EmbargosConstants.IND_FLAG_YES.equals(cuentaTraba.getAgregarATraba());
		seizedBankAccountDTO.setAddToSeized(addToSeized);

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

			cuentaTraba.setDivisa(seizedBankAccountDTO.getBankAccountCurrency());
		}
		if (seizedBankAccountDTO.getBankAccountStatus()!=null) {
			
			cuentaTraba.setEstadoCuenta(seizedBankAccountDTO.getBankAccountStatus());	
			
		}
		//Campo readonly, se comenta:
		//if (seizedBankAccountDTO.getOriginEmb()!=null) {
		//	String origenEmb = seizedBankAccountDTO.getOriginEmb() ? EmbargosConstants.IND_FLAG_YES : EmbargosConstants.IND_FLAG_NO;
		//	cuentaTraba.setOrigenEmb(origenEmb);
		//}
		if (seizedBankAccountDTO.getAddToSeized()!=null) {
			String addToSeized = seizedBankAccountDTO.getAddToSeized() ? EmbargosConstants.IND_FLAG_YES : EmbargosConstants.IND_FLAG_NO;
			cuentaTraba.setAgregarATraba(addToSeized);
		}
		
		cuentaTraba.setUsuarioUltModificacion(userModif);
		cuentaTraba.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
		
		return cuentaTraba;
	}
	
	@Mappings({
		@Mapping (source = "accountNum", target = "cuenta"),
		@Mapping (source = "iban", target = "iban"),
		@Mapping (source = "status", target = "estadoCuenta"),
		@Mapping (source = "divisa", target = "divisa"),
	})
	public abstract CuentaTraba accountDTOToCuentaTraba(AccountDTO accountDTO);
	
}
