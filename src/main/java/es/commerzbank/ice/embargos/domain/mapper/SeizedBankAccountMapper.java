package es.commerzbank.ice.embargos.domain.mapper;

import es.commerzbank.ice.datawarehouse.domain.dto.AccountDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizedBankAccountDTO;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.CuentaTrabaActuacion;
import es.commerzbank.ice.embargos.domain.entity.EstadoTraba;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.ICEDateUtils;
import org.mapstruct.*;

import java.math.BigDecimal;

@Mapper(componentModel="spring")
public abstract class  SeizedBankAccountMapper {

	@Mappings({
		@Mapping (source = "codCuentaTraba", target = "idSeizedBankAccount"),
		@Mapping (source = "iban", target = "iban"),
		@Mapping (source = "estadoCuenta", target = "bankAccountStatus"),
		@Mapping (source = "importe", target = "amount"),
		@Mapping (source = "cambio", target = "fxRate"),
		@Mapping (source = "cuentaTrabaActuacion.codActuacion", target = "seizureAction.code"),
		@Mapping (source = "cuentaTrabaActuacion.descripcion", target = "seizureAction.description"),
		@Mapping (source = "estadoTraba.codEstado", target = "seizureStatus.code"),
		@Mapping (source = "estadoTraba.desEstado", target = "seizureStatus.description"),
		@Mapping (source = "divisa", target = "bankAccountCurrency"),
		@Mapping (source = "numeroOrdenCuenta", target = "orderNumberAccount"),
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
		if (seizedBankAccountDTO.getSeizureAction()!=null && seizedBankAccountDTO.getSeizureAction().getCode()!=null) {
			CuentaTrabaActuacion cuentaTrabaActuacion = new CuentaTrabaActuacion();
			cuentaTrabaActuacion.setCodActuacion(seizedBankAccountDTO.getSeizureAction().getCode());
			cuentaTraba.setCuentaTrabaActuacion(cuentaTrabaActuacion);
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
			
			//cuentaTraba.getTraba().setEstadoTraba(estadoTraba);
			cuentaTraba.setEstadoTraba(estadoTraba);
		}
		/* front borra la currency. se ignora.
		if (seizedBankAccountDTO.getBankAccountCurrency()!=null) {

			cuentaTraba.setDivisa(seizedBankAccountDTO.getBankAccountCurrency());
		}*/
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
		@Mapping (source = "accountDTO.accountNum", target = "cuenta"),
		@Mapping (source = "accountDTO.iban", target = "iban"),
		@Mapping (source = "accountDTO.status", target = "estadoCuenta"),
		@Mapping (source = "accountDTO.divisa", target = "divisa"),
		@Mapping (source = "numeroOrdenCuenta", target = "numeroOrdenCuenta"),
		@Mapping (source = "traba", target = "traba"),
	})
	public abstract CuentaTraba accountDTOToCuentaTraba(AccountDTO accountDTO, BigDecimal numeroOrdenCuenta, Traba traba);
	
	//@AfterMapping
	//public CuentaTraba accountDTOToCuentaTrabaAfterMapping(@MappingTarget CuentaTraba cuentaTraba, AccountDTO accountDTO, Traba traba) {
	
	@AfterMapping
	public CuentaTraba accountDTOToCuentaTrabaAfterMapping(@MappingTarget CuentaTraba cuentaTraba, AccountDTO accountDTO) {
		
		//cuentaTraba.setTraba(traba);
		EstadoTraba estadoTraba = new EstadoTraba();
		estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE);
		cuentaTraba.setEstadoTraba(estadoTraba);
		cuentaTraba.setOrigenEmb(EmbargosConstants.IND_FLAG_NO);
		cuentaTraba.setUsuarioUltModificacion(EmbargosConstants.USER_AUTOMATICO);
		cuentaTraba.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
		return cuentaTraba;
	}
	
}
