package es.commerzbank.ice.embargos.domain.mapper;

import es.commerzbank.ice.datawarehouse.domain.dto.AccountDTO;
import es.commerzbank.ice.embargos.domain.entity.*;
import es.commerzbank.ice.embargos.utils.CGPJUtils;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.ICEDateUtils;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.text.ParseException;

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
    }

    @Mappings({
            @Mapping(source = "levantamientoTraba", target = "levantamientoTraba"),
            @Mapping(source = "cuentaTraba.cuenta", target = "cuenta"),
            @Mapping(constant = "", target = "actuacion"),
            @Mapping(constant = "1", target = "numeroOrdenCuenta"),
            @Mapping(source = "cuentaTraba.cambio", target = "cambio"),
            @Mapping(constant = EmbargosConstants.IND_FLAG_NO, target = "indContabilizado"),
            @Mapping(source = "cuentaTraba.iban", target = "iban"),
            @Mapping(source = "cuentaTraba.divisa", target = "codDivisa"),
            @Mapping(source = "cuentaTraba.estadoCuenta", target = "estadoCuenta"),
            @Mapping(source = "cuentaTraba.fechaValor", target = "fechaValor"),
            @Mapping(constant = "", target = "FUltimaModificacion"),    // se setean abajo
            @Mapping(constant = "", target = "usuarioUltModificacion"),    // se setean abajo
    })
    public abstract CuentaLevantamiento generateCuentaLevantamiento(
            LevantamientoTraba levantamientoTraba, SolicitudesLevantamiento solicitudLevantamiento, CuentaTraba cuentaTraba)
            throws ParseException;

    @AfterMapping
    public void generateCuentaLevantamientoAfterMapping(
            @MappingTarget CuentaLevantamiento cuentaLevantamiento,
            LevantamientoTraba levantamientoTraba, SolicitudesLevantamiento solicitudLevantamiento) throws ParseException
    {
        cuentaLevantamiento.setImporte(CGPJUtils.parse(solicitudLevantamiento.getImporteSolicitud()));
        EstadoLevantamiento estadoLevantamiento = new EstadoLevantamiento();
        estadoLevantamiento.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE);
        cuentaLevantamiento.setEstadoLevantamiento(estadoLevantamiento);
        cuentaLevantamiento.setUsuarioUltModificacion(EmbargosConstants.USER_AUTOMATICO);
        cuentaLevantamiento.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));

    }

    public abstract CuentaTrabaCGPJCopy generateCuentaTrabaCGPJCopy(CuentaTraba cuentaTraba);

    public abstract CuentaTraba generateCuentaTraba(CuentaTrabaCGPJCopy cuentaTrabaCGPJCopy);
}
