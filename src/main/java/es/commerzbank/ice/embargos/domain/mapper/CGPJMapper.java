package es.commerzbank.ice.embargos.domain.mapper;

import es.commerzbank.ice.datawarehouse.domain.dto.AccountDTO;
import es.commerzbank.ice.embargos.domain.entity.CuentaEmbargo;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.EstadoTraba;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.ICEDateUtils;
import org.mapstruct.*;

import java.math.BigDecimal;

@Mapper(componentModel="spring")
public abstract class CGPJMapper {
    @Mappings({
            @Mapping(source = "accountDTO.accountNum", target = "cuenta"),
            @Mapping(source = "counter", target = "numeroOrdenCuenta"),
            @Mapping(source = "accountDTO.iban", target = "iban"),
    })
    public abstract CuentaEmbargo generateCuentaEmbargo(AccountDTO accountDTO, BigDecimal counter);

    @AfterMapping
    public void generateCuentaEmbargoAfterMapping(@MappingTarget CuentaEmbargo cuentaEmbargo) {
        cuentaEmbargo.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
        cuentaEmbargo.setUsuarioUltModificacion(EmbargosConstants.USER_AUTOMATICO);
    }

    @Mappings({
            @Mapping(source = "accountDTO.accountNum", target = "cuenta"),
            @Mapping(source = "counter", target = "numeroOrdenCuenta"),
            @Mapping(source = "accountDTO.iban", target = "iban"),
            @Mapping(source = "accountDTO.status", target = "estadoCuenta"),
            @Mapping(source = "accountDTO.divisa", target = "divisa"),
            @Mapping(constant = EmbargosConstants.IND_FLAG_NO, target = "origenEmb"),
            @Mapping(constant = EmbargosConstants.IND_FLAG_YES, target = "agregarATraba"),
    })
    public abstract CuentaTraba generateCuentaTraba(AccountDTO accountDTO, BigDecimal counter);

    @AfterMapping
    public void generateCuentaTrabaAfterMapping(@MappingTarget CuentaTraba cuentaTraba) {
        EstadoTraba estadoTraba = new EstadoTraba();
        estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE);
        cuentaTraba.setEstadoTraba(estadoTraba);
        cuentaTraba.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
        cuentaTraba.setUsuarioUltModificacion(EmbargosConstants.USER_AUTOMATICO);
    }
}
