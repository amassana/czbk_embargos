package es.commerzbank.ice.embargos.service.impl;

import es.commerzbank.ice.comun.lib.domain.entity.Tarea;
import es.commerzbank.ice.comun.lib.service.TaskService;
import es.commerzbank.ice.embargos.domain.entity.*;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.repository.*;
import es.commerzbank.ice.embargos.service.FinalResponseGenerationService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.ICEDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class FinalResponseGenerationServiceImpl
    implements FinalResponseGenerationService
{
    private static final Logger logger = LoggerFactory.getLogger(FinalResponseGenerationServiceImpl.class);

    @Autowired
    private FileControlRepository fileControlRepository;

    @Autowired
    FileControlMapper fileControlMapper;

    @Autowired
    private SeizureRepository seizureRepository;

    @Autowired
    private SeizedRepository seizedRepository;

    @Autowired
    private LiftingRepository liftingRepository;

    @Autowired
    private FinalResponseRepository finalResponseRepository;

    @Autowired
    private SeizureSummaryBankAccountRepository seizureSummaryBankAccountRepository;

    @Autowired
    private FinalFileRepository finalFileRepository;

    @Autowired
    private TaskService taskService;

    @Override
    public FicheroFinal calcFinalResult(ControlFichero ficheroFase3, Tarea tarea, String user)
        throws Exception
    {
        boolean isAEAT = EmbargosConstants.IND_FLAG_SI.equals(ficheroFase3.getEntidadesComunicadora().getIndFormatoAeat());

        Long tipoFichero;
        if (isAEAT) {
            tipoFichero = EmbargosConstants.COD_TIPO_FICHERO_FICHERO_FINAL_AEAT_INTERNAL;
        }
        else {
            tipoFichero = EmbargosConstants.COD_TIPO_FICHERO_COM_RESULTADO_FINAL_NORMA63;
        }

        /* Creación Control Fichero Fase 6 como punto de entrada */
        String fileNameFinal = ficheroFase3.getEntidadesComunicadora().getPrefijoFicheros() +"_"+ ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMdd) +"."+ EmbargosConstants.TIPO_FICHERO_FINAL;

        logger.info("Cerrando el ciclo de embargos "+ ficheroFase3.getCodControlFichero() +" Nombre fichero cerrado "+ fileNameFinal);

        ControlFichero ficheroFase6 =
                fileControlMapper.generateControlFichero(null, tipoFichero, fileNameFinal, null);
        ficheroFase6.setEntidadesComunicadora(ficheroFase3.getEntidadesComunicadora());
        List<ResultadoEmbargo> resultadosEmbargos = new ArrayList<>(50);
        ficheroFase6.setResultadoEmbargos(resultadosEmbargos);
        ficheroFase6.setUsuarioUltModificacion(user);
        ficheroFase6.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
        ficheroFase6.setFechaCreacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMdd));
        ficheroFase6.setControlFicheroOrigen(ficheroFase3.getControlFicheroRespuesta());
        ficheroFase6.setFechaComienzoCiclo(ficheroFase3.getFechaComienzoCiclo());
        ficheroFase6.setFechaGeneracionRespuesta(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
        ficheroFase6.setFechaMaximaRespuesta(ICEDateUtils.dateToBigDecimal(tarea.getfTarea(), ICEDateUtils.FORMAT_yyyyMMdd));
        fileControlRepository.save(ficheroFase6);

        List<Embargo> embargos = seizureRepository.findAllByControlFichero(ficheroFase3);

        BigDecimal importeLevantadoFichero = BigDecimal.ZERO;
        BigDecimal importeNetoFichero = BigDecimal.ZERO;

        for (Embargo embargo : embargos) {
            logger.info("Cerrando el embargo "+ embargo.getCodEmbargo() +" número "+ embargo.getNumeroEmbargo());

            // Recuperación de los datos del embargo
            Traba traba = seizedRepository.getByEmbargo(embargo);
            List<LevantamientoTraba> levantamientos = liftingRepository.findAllByTraba(traba);

            // Se calcula el resumen del embargo
            ResultadoEmbargo resultadoEmbargo = new ResultadoEmbargo();

            resultadosEmbargos.add(resultadoEmbargo);

            resultadoEmbargo.setUsuarioUltModificacion(user);
            resultadoEmbargo.setfUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));

            resultadoEmbargo.setTraba(traba);
            resultadoEmbargo.setEmbargo(embargo);
            resultadoEmbargo.setControlFichero(ficheroFase6);

            if (isAEAT) {
                resultadoEmbargo.setIndComunicado(EmbargosConstants.IND_FLAG_NO);
            }

            // Se calcula el resumen para cada cuenta
            List<CuentaResultadoEmbargo> cuentasResultadoEmbargo = new ArrayList<>(6);
            resultadoEmbargo.setCuentaResultadoEmbargos(cuentasResultadoEmbargo);

            BigDecimal importeLevantadoEmbargo = BigDecimal.ZERO;
            BigDecimal importeNetoEmbargo = BigDecimal.ZERO;

            for (CuentaEmbargo cuentaEmbargo : embargo.getCuentaEmbargos()) {
                CuentaResultadoEmbargo cuentaResultadoEmbargo = new CuentaResultadoEmbargo();

                cuentasResultadoEmbargo.add(cuentaResultadoEmbargo);

                cuentaResultadoEmbargo.setUsuarioUltModificacion(user);
                cuentaResultadoEmbargo.setfUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));

                cuentaResultadoEmbargo.setCuentaEmbargo(cuentaEmbargo);

                // Se busca y mapea la cuenta traba relacionada, se obtiene el importe trabado
                if (traba.getCuentaTrabas() != null) { // No debería ocurrir pero ..
                    for (CuentaTraba cuentaTraba : traba.getCuentaTrabas()) {
                        if (cuentaTraba.getCuenta() != null) {
                            if (cuentaTraba.getCuenta().equals(cuentaEmbargo.getCuenta())) {
                                cuentaResultadoEmbargo.setCuentaTraba(cuentaTraba);
                                break;
                            }
                        }
                    }
                }

                BigDecimal importeTrabadoCuenta = cuentaResultadoEmbargo.getCuentaTraba() == null ?
                        BigDecimal.ZERO : cuentaResultadoEmbargo.getCuentaTraba().getImporte() == null ?
                        BigDecimal.ZERO : cuentaResultadoEmbargo.getCuentaTraba().getImporte();

                // Se calcula el total levantado - pueden haber varios levantamientos.
                BigDecimal importeLevantadoCuenta = BigDecimal.ZERO;
                for (LevantamientoTraba levantamientoTraba : levantamientos) {
                    if (levantamientoTraba.getCuentaLevantamientos() != null) {
                        for (CuentaLevantamiento cuentaLevantamiento : levantamientoTraba.getCuentaLevantamientos()) {
                            if (cuentaLevantamiento.getCuenta().equals(cuentaEmbargo.getCuenta())) {
                                importeLevantadoCuenta = importeLevantadoCuenta.add(cuentaLevantamiento.getImporte());
                            }
                        }
                    }
                }

                // Se calcula el importe neto
                BigDecimal importeNetoCuenta = importeTrabadoCuenta.subtract(importeLevantadoCuenta);

                cuentaResultadoEmbargo.setImporteNeto(importeNetoCuenta);
                cuentaResultadoEmbargo.setImporteLevantado(importeLevantadoCuenta);

                // Se calcula el estado en función de los importes finales
                EstadoResultadoEmbargo estadoResultadoEmbargo = new EstadoResultadoEmbargo();
                if (BigDecimal.ZERO.compareTo(cuentaResultadoEmbargo.getImporteNeto()) == 0)
                    estadoResultadoEmbargo.setCodEstadoResultadoEmbargo(EmbargosConstants.ESTADO_FINAL_LEVANTAMIENTO_TOTAL);
                else if (BigDecimal.ZERO.compareTo(importeLevantadoCuenta) == 0)
                    estadoResultadoEmbargo.setCodEstadoResultadoEmbargo(EmbargosConstants.ESTADO_FINAL_SIN_ORDEN_LEVANTAMIENTO);
                else if (BigDecimal.ZERO.compareTo(cuentaResultadoEmbargo.getImporteNeto()) == -1)
                    estadoResultadoEmbargo.setCodEstadoResultadoEmbargo(EmbargosConstants.ESTADO_FINAL_LEVANTAMIENTO_PARCIAL);
                else
                    estadoResultadoEmbargo.setCodEstadoResultadoEmbargo(EmbargosConstants.ESTADO_FINAL_OTROS);
                cuentaResultadoEmbargo.setEstadoResultadoEmbargo(estadoResultadoEmbargo);

                // Se actualizan los contadores globales del resultado final del embargo
                importeLevantadoEmbargo = importeLevantadoEmbargo.add(importeLevantadoCuenta);
                importeNetoEmbargo = importeNetoEmbargo.add(importeNetoCuenta);

                logger.info("Cuenta "+ cuentaResultadoEmbargo.getCuentaEmbargo().getCuenta() +" neto "+ importeNetoCuenta);
            }

            // Se graban los totales del resultado del embargo
            resultadoEmbargo.setTotalLevantado(importeLevantadoEmbargo);
            resultadoEmbargo.setTotalNeto(importeNetoEmbargo);

            // Se calcula el estado en función de los importes finales
            EstadoResultadoEmbargo estadoResultadoEmbargo = new EstadoResultadoEmbargo();
            if (BigDecimal.ZERO.compareTo(resultadoEmbargo.getTotalNeto()) == 0)
                estadoResultadoEmbargo.setCodEstadoResultadoEmbargo(EmbargosConstants.ESTADO_FINAL_LEVANTAMIENTO_TOTAL);
            else if (BigDecimal.ZERO.compareTo(importeLevantadoEmbargo) == 0)
                estadoResultadoEmbargo.setCodEstadoResultadoEmbargo(EmbargosConstants.ESTADO_FINAL_SIN_ORDEN_LEVANTAMIENTO);
            else if (BigDecimal.ZERO.compareTo(resultadoEmbargo.getTotalNeto()) == -1)
                estadoResultadoEmbargo.setCodEstadoResultadoEmbargo(EmbargosConstants.ESTADO_FINAL_LEVANTAMIENTO_PARCIAL);
            else
                estadoResultadoEmbargo.setCodEstadoResultadoEmbargo(EmbargosConstants.ESTADO_FINAL_OTROS);
            resultadoEmbargo.setEstadoResultadoEmbargo(estadoResultadoEmbargo);

            logger.info("Importe levantado embargo: "+ importeLevantadoEmbargo +" Importe neto embargo: "+ importeNetoEmbargo);

            // Se persiste en el repositorio
            finalResponseRepository.save(resultadoEmbargo);

            for (CuentaResultadoEmbargo cuentaResultadoEmbargo : cuentasResultadoEmbargo) {
                cuentaResultadoEmbargo.setResultadoEmbargo(resultadoEmbargo);
                seizureSummaryBankAccountRepository.save(cuentaResultadoEmbargo);
            }

            // Se acumulan los totales para el fichero
            importeLevantadoFichero = importeLevantadoFichero.add(importeLevantadoEmbargo);
            importeNetoFichero = importeNetoFichero.add(importeNetoEmbargo);
        }

        logger.info("Grabando resumen Final. Importe neto total: "+ importeNetoFichero);

        FicheroFinal ficheroFinal = new FicheroFinal();

        ficheroFinal.setCodFicheroDiligencias(ficheroFase3.getCodControlFichero());
        ficheroFinal.setControlFichero(ficheroFase6);

        ficheroFinal.setImporte(importeNetoFichero);
        ficheroFinal.setImporteLevantado(importeLevantadoFichero);

        ficheroFinal.setFValor(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMdd));

        ficheroFinal.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
        ficheroFinal.setUsuarioUltModificacion(EmbargosConstants.USER_AUTOMATICO);

        //Se inicializa el estado de contabilizacion del fichero final:
        EstadoContabilizacion estadoContabilizacion = new EstadoContabilizacion();
        if (ficheroFase6.getEntidadesComunicadora() != null &&
                ficheroFase6.getEntidadesComunicadora().getCuenta() != null &&
                !ficheroFase6.getEntidadesComunicadora().getCuenta().trim().isEmpty()) {
            if (BigDecimal.ZERO.compareTo(importeNetoFichero) < 0) {
                estadoContabilizacion.setCodEstado(EmbargosConstants.COD_ESTADO_CONTABILIZACION_PENDIENTE_ENVIO_A_CONTABILIDAD);
            }
            else {
                estadoContabilizacion.setCodEstado(EmbargosConstants.COD_ESTADO_CONTABILIZACION_NOTAPPLY);
            }
        } else {
            estadoContabilizacion.setCodEstado(EmbargosConstants.COD_ESTADO_CONTABILIZACION_NOTAPPLY);
        }
        ficheroFinal.setEstadoContabilizacion(estadoContabilizacion);

        finalFileRepository.save(ficheroFinal);

        if (EmbargosConstants.IND_FLAG_SI.equals(ficheroFase3.getEntidadesComunicadora().getIndFormatoAeat())) {
            EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero(
                    EmbargosConstants.COD_ESTADO_CTRLFICHERO_FINAL_AEAT_GENERADO,
                    tipoFichero);
            ficheroFase6.setEstadoCtrlfichero(estadoCtrlfichero);
        }
        else if (EmbargosConstants.IND_FLAG_SI.equals(ficheroFase3.getEntidadesComunicadora().getIndNorma63())) {
            EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero(
                    EmbargosConstants.COD_ESTADO_CTRLFICHERO_FINAL_AEAT_PENDIENTE_FICHERO,
                    tipoFichero);
            ficheroFase6.setEstadoCtrlfichero(estadoCtrlfichero);
        }

        ficheroFase6.setUsuarioUltModificacion(user);
        ficheroFase6.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
        fileControlRepository.save(ficheroFase6);

        return ficheroFinal;
    }
}
