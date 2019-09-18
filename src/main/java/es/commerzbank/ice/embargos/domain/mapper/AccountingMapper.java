package es.commerzbank.ice.embargos.domain.mapper;

import es.commerzbank.ice.comun.lib.domain.dto.AccountingNote;
import es.commerzbank.ice.embargos.domain.dto.FileControlTypeDTO;
import es.commerzbank.ice.embargos.domain.entity.CuentaLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.TipoFichero;
import es.commerzbank.ice.utils.EmbargosConstants;
import org.aspectj.lang.annotation.After;
import org.mapstruct.*;

import java.util.Date;

@Mapper(componentModel="spring")
public abstract class AccountingMapper {

	@Mappings({
		@Mapping(source = "codTipoFichero", target = "code"),
		@Mapping(source = "oficinaCuentaRecaudacion", target = "codOffice")
	})
	public abstract AccountingNote fromCuentaLevantamiento(CuentaLevantamiento, String oficinaCuentaRecaudacion);
	@AfterMapping
	protected void fromCuentaLevantamientoAfterMapping(@MappingTarget AccountingNote accountingNote, CuentaLevantamiento cuentaLevantamiento) {
	{
		double amount = cuentaLevantamiento.getImporte() != null ? cuentaLevantamiento.getImporte().doubleValue() : 0;

		accountingNote.setAplication(EmbargosConstants.ID_APLICACION_EMBARGOS);
		accountingNote.setCodOffice(oficinaCuentaRecaudacion);
		//El contador lo gestiona Comunes
		//accountingNote.setContador(contador);
		accountingNote.setAmount(amount);
		accountingNote.setCodCurrency(cuentaLevantamiento.getCodDivisa());
		accountingNote.setDebitAccount(cuentaLevantamiento.getCuenta());
		accountingNote.setCreditAccount(cuentaRecaudacion);
		accountingNote.setActualDate(new Date());
		//accountingNote.setExecutionDate(new Date());
		accountingNote.setReference1(reference1);
		accountingNote.setReference2(reference2);
		accountingNote.setDetailPayment(detailPayment);
		accountingNote.setChange(cuentaLevantamiento.getCambio());
		accountingNote.setGeneralParameter(contabilizacionCallbackNameParameter);
	}
}
