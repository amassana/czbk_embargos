package es.commerzbank.ice.embargos.service.impl;

import es.commerzbank.ice.embargos.domain.entity.*;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.repository.*;
import es.commerzbank.ice.embargos.service.FinalResponseGenerationService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.ICEDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    private SeizureSummaryRepository seizureSummaryRepository;

    @Autowired
    private SeizureSummaryBankAccountRepository seizureSummaryBankAccountRepository;

    @Autowired
    private FinalFileRepository finalFileRepository;

    @Override
    public FicheroFinal calcFinalResult(ControlFichero ficheroFase3, String user)
        throws Exception
    {
        /* Creación Control Fichero Fase 6 como punto de entrada */
        String fileNameFinal = ficheroFase3.getEntidadesComunicadora().getPrefijoFicheros() +"_"+ ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMdd) +"."+ EmbargosConstants.TIPO_FICHERO_FINAL;

        logger.info("Cerrando el ciclo de embargos "+ ficheroFase3.getCodControlFichero() +" Nombre fichero cerrado "+ fileNameFinal);

        ControlFichero ficheroFase6 =
                fileControlMapper.generateControlFichero(null, EmbargosConstants.COD_TIPO_FICHERO_COM_RESULTADO_FINAL_NORMA63, fileNameFinal, null);
        ficheroFase6.setEntidadesComunicadora(ficheroFase3.getEntidadesComunicadora());
        List<ResultadoEmbargo> resultadosEmbargos = new ArrayList<>(50);
        ficheroFase6.setResultadoEmbargos(resultadosEmbargos);
        ficheroFase6.setUsuarioUltModificacion(user);
        ficheroFase6.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
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
                for (CuentaTraba cuentaTraba : traba.getCuentaTrabas()) {
                    if (cuentaTraba.getCuenta().equals(cuentaEmbargo.getCuenta())) {
                        cuentaResultadoEmbargo.setCuentaTraba(cuentaTraba);
                        break;
                    }
                }

                BigDecimal importeTrabadoCuenta = cuentaResultadoEmbargo.getCuentaTraba() == null ?
                        BigDecimal.ZERO : cuentaResultadoEmbargo.getCuentaTraba().getImporte();

                // Se calcula el total levantado - pueden haber varios levantamientos.
                BigDecimal importeLevantadoCuenta = BigDecimal.ZERO;
                for (LevantamientoTraba levantamientoTraba : levantamientos) {
                    for (CuentaLevantamiento cuentaLevantamiento : levantamientoTraba.getCuentaLevantamientos()) {
                        if (cuentaLevantamiento.getCuenta().equals(cuentaEmbargo.getCuenta())) {
                            importeLevantadoCuenta = importeLevantadoCuenta.add(cuentaLevantamiento.getImporte());
                        }
                    }
                }

                // Se calcula el importe neto
                BigDecimal importeNetoCuenta = importeTrabadoCuenta.subtract(importeLevantadoCuenta);

                cuentaResultadoEmbargo.setImporteNeto(importeNetoCuenta);

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
            seizureSummaryRepository.save(resultadoEmbargo);

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

        ficheroFinal.setCodFicheroDiligencias(BigDecimal.valueOf(ficheroFase3.getCodControlFichero()));
        ficheroFinal.setControlFichero(ficheroFase6);

        ficheroFinal.setImporte(importeNetoFichero);
        ficheroFinal.setImporteLevantado(importeLevantadoFichero);

        ficheroFinal.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
        ficheroFinal.setUsuarioUltModificacion(EmbargosConstants.USER_AUTOMATICO);

        // ficheroFinal.setFValor(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMdd));

        //Se inicializa el estado de contabilizacion del fichero final:
        EstadoContabilizacion estadoContabilizacion = new EstadoContabilizacion();
        if (ficheroFase6.getEntidadesComunicadora() != null &&
                ficheroFase6.getEntidadesComunicadora().getCuenta() != null &&
                !ficheroFase6.getEntidadesComunicadora().getCuenta().trim().isEmpty()) {
            estadoContabilizacion.setCodEstado(EmbargosConstants.COD_ESTADO_CONTABILIZACION_PENDIENTE_ENVIO_A_CONTABILIDAD);
        } else {
            estadoContabilizacion.setCodEstado(EmbargosConstants.COD_ESTADO_CONTABILIZACION_NOTAPPLY);
        }
        ficheroFinal.setEstadoContabilizacion(estadoContabilizacion);

        finalFileRepository.save(ficheroFinal);

        return ficheroFinal;
    }
}
