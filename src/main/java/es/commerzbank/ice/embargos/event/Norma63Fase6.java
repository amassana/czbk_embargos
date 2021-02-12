package es.commerzbank.ice.embargos.event;

import es.commerzbank.ice.comun.lib.file.exchange.FileWriterHelper;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.domain.entity.*;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.DiligenciaFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.OrdenEjecucionEmbargoFase3;
import es.commerzbank.ice.embargos.repository.*;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.ICEDateUtils;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(transactionManager="transactionManager")
public class Norma63Fase6
{
    private static final Logger LOG = LoggerFactory.getLogger(Norma63Fase6.class);

    @Value("${commerzbank.embargos.beanio.config-path.cuaderno63}")
    String pathFileConfigCuaderno63;

    @Value("${commerzbank.embargos.beanio.config-path.aeat}")
    String pathFileConfigAEAT;

    @Autowired
    SeizureRepository seizureRepository;

    @Autowired
    SeizedRepository seizedRepository;

    @Autowired
    LiftingRepository liftingRepository;

    @Autowired
    FileControlRepository fileControlRepository;

    @Autowired
    SeizureSummaryRepository seizureSummaryRepository;

    @Autowired
    SeizureSummaryBankAccountRepository seizureSummaryBankAccountRepository;
    @Autowired
    FileControlMapper fileControlMapper;
    @Autowired
    FinalFileRepository finalFileRepository;
    @Autowired
    SeizureBankAccountRepository seizureBankAccountRepository;
 
	@Autowired
	private GeneralParametersService generalParametersService;

    @Autowired
    private FileWriterHelper fileWriterHelper;

    /* to cronify
    @Scheduled("")
    public void doFase6()
    {
        // get files type XXX which expire today
        for each file
        process()
    }
    */

    public void generarFase6(Long codControlFichero, String user) throws ICEException, IOException {
        ControlFichero ficheroFase3 = fileControlRepository.getOne(codControlFichero);

        List<Embargo> embargos = seizureRepository.findAllByControlFichero(ficheroFase3);

        if (embargos == null)
            throw new ICEException("No seizures found for code "+ codControlFichero);

        if (ficheroFase3.getTipoFichero().getCodTipoFichero() == EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_NORMA63)
            generarFase6Norma63(ficheroFase3, embargos, user);
        else if (ficheroFase3.getTipoFichero().getCodTipoFichero() == EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_AEAT)
            generarFase6AEAT(ficheroFase3, embargos, user);
    }

    private void generarFase6Norma63(ControlFichero ficheroFase3, List<Embargo> embargos, String user) throws ICEException, IOException
    {
        BeanReader beanReader = null;

        try {
            // Guardar fase 6
        	String pathGenerated = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_GENERATED);
        	
            String fileNameFinal = ficheroFase3.getEntidadesComunicadora().getPrefijoFicheros() +"_"+ ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMdd) +"."+ EmbargosConstants.TIPO_FICHERO_FINAL;

            File fase6File = fileWriterHelper.getGeneratedFile(pathGenerated, fileNameFinal);

            //Se guarda el registro de ControlFichero del fichero de salida:
            ControlFichero ficheroFase6 =
                    fileControlMapper.generateControlFichero(fase6File, EmbargosConstants.COD_TIPO_FICHERO_COM_RESULTADO_FINAL_NORMA63, fileNameFinal, fase6File);
            ficheroFase6.setEntidadesComunicadora(ficheroFase3.getEntidadesComunicadora());

            ficheroFase6.setUsuarioUltModificacion(user);
            ficheroFase6.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
            fileControlRepository.save(ficheroFase6);

            // Inicialiar beanIO parser
            StreamFactory factory = StreamFactory.newInstance();
            factory.loadResource(pathFileConfigCuaderno63);
	        
            beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_CUADERNO63_FASE3, (new File(ficheroFase3.getRutaFichero()).getCanonicalFile()));

            Object currentF3Record = null;

            BigDecimal importeTotalNeto = BigDecimal.ZERO;

            while ((currentF3Record = beanReader.read()) != null)
            {
                if(EmbargosConstants.RECORD_NAME_CABECERAEMISOR.equals(beanReader.getRecordName())) {
                    ;
                }
                else if(EmbargosConstants.RECORD_NAME_ORDENEJECUCIONEMBARGOCOMPLEMENTARIO.equals(beanReader.getRecordName())) {
                    ; // skip?
                }
                else if(EmbargosConstants.RECORD_NAME_FINFICHERO.equals(beanReader.getRecordName())) {
                    ;
                }
                else if(EmbargosConstants.RECORD_NAME_ORDENEJECUCIONEMBARGO.equals(beanReader.getRecordName())) {

                    OrdenEjecucionEmbargoFase3 ordenEjecucionEmbargo = (OrdenEjecucionEmbargoFase3) currentF3Record;

                    LOG.info("Treating "+ ordenEjecucionEmbargo.getCodigoDeuda() +" - "+ ordenEjecucionEmbargo.getIdentificadorDeuda());

                    Embargo embargo = findByNumeroEmbargo(embargos, ordenEjecucionEmbargo.getIdentificadorDeuda());
                    Traba traba = seizedRepository.getByEmbargo(embargo);
                    List<LevantamientoTraba> levantamientos = liftingRepository.findAllByTraba(traba);

                    BigDecimal importeTotalLevantadoEmbargo = BigDecimal.ZERO;

                    ResultadoEmbargo resultadoEmbargo = new ResultadoEmbargo();
                    resultadoEmbargo.setTraba(traba);
                    resultadoEmbargo.setEmbargo(embargo);

                    resultadoEmbargo.setControlFichero(ficheroFase6);

                    List<CuentaResultadoEmbargo> cuentasResultadoEmbargo = new ArrayList<>(6);
                    resultadoEmbargo.setCuentaResultadoEmbargos(cuentasResultadoEmbargo);

                    for (CuentaEmbargo cuentaEmbargo : embargo.getCuentaEmbargos()) {
                        CuentaResultadoEmbargo cuentaResultadoEmbargo = new CuentaResultadoEmbargo();

                        cuentaResultadoEmbargo.setCuentaEmbargo(cuentaEmbargo);

                        for (CuentaTraba cuentaTraba : traba.getCuentaTrabas()) {
                            if (cuentaTraba.getCuenta().equals(cuentaEmbargo.getCuenta())) {
                                cuentaResultadoEmbargo.setCuentaTraba(cuentaTraba);
                                break;
                            }
                        }

                        if (cuentaResultadoEmbargo.getCuentaTraba() != null) {
                            BigDecimal importeLevantadoCuenta = BigDecimal.ZERO;

                            if (levantamientos != null) {
                                for (LevantamientoTraba levantamientoTraba : levantamientos) {
                                    for (CuentaLevantamiento cuentaLevantamiento : levantamientoTraba.getCuentaLevantamientos()) {
                                        if (cuentaLevantamiento.getCuenta().equals(cuentaEmbargo.getCuenta())) {
                                            // TODO: DIVISA
                                            importeLevantadoCuenta = importeLevantadoCuenta.add(cuentaLevantamiento.getImporte());
                                        }
                                    }
                                }
                            }

                            LOG.info("Importe cuenta traba " + cuentaResultadoEmbargo.getCuentaTraba().getCodCuentaTraba() + " " + (cuentaResultadoEmbargo.getCuentaTraba().getImporte() == null) + " " + importeLevantadoCuenta);

                            if (BigDecimal.ZERO.compareTo(importeLevantadoCuenta) == 0)
                                cuentaResultadoEmbargo.setImporteNeto(BigDecimal.ZERO);
                            else
                            {
                                // TODO what if get importe == 0? null? error
                                if (cuentaResultadoEmbargo.getCuentaTraba().getImporte() != null)
                                    cuentaResultadoEmbargo.setImporteNeto(cuentaResultadoEmbargo.getCuentaTraba().getImporte().subtract(importeLevantadoCuenta));

                                importeTotalLevantadoEmbargo = importeTotalLevantadoEmbargo.add(importeLevantadoCuenta);
                            }

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
                        }

                        cuentasResultadoEmbargo.add(cuentaResultadoEmbargo);
                    }

                    resultadoEmbargo.setTotalLevantado(importeTotalLevantadoEmbargo);

                    if (traba.getImporteTrabado() == null) {
                        resultadoEmbargo.setTotalNeto(BigDecimal.ZERO);
                    }
                    else {
                        resultadoEmbargo.setTotalNeto(traba.getImporteTrabado().subtract(importeTotalLevantadoEmbargo));
                        importeTotalNeto = importeTotalNeto.add(resultadoEmbargo.getTotalNeto());
                    }
                    EstadoResultadoEmbargo estadoResultadoEmbargo = new EstadoResultadoEmbargo();
                    if (BigDecimal.ZERO.compareTo(resultadoEmbargo.getTotalNeto()) == 0)
                        estadoResultadoEmbargo.setCodEstadoResultadoEmbargo(EmbargosConstants.ESTADO_FINAL_LEVANTAMIENTO_TOTAL);
                    else if (BigDecimal.ZERO.compareTo(importeTotalLevantadoEmbargo) == 0)
                        estadoResultadoEmbargo.setCodEstadoResultadoEmbargo(EmbargosConstants.ESTADO_FINAL_SIN_ORDEN_LEVANTAMIENTO);
                    else if (BigDecimal.ZERO.compareTo(resultadoEmbargo.getTotalNeto()) == -1)
                        estadoResultadoEmbargo.setCodEstadoResultadoEmbargo(EmbargosConstants.ESTADO_FINAL_LEVANTAMIENTO_PARCIAL);
                    else
                        estadoResultadoEmbargo.setCodEstadoResultadoEmbargo(EmbargosConstants.ESTADO_FINAL_OTROS);
                    resultadoEmbargo.setEstadoResultadoEmbargo(estadoResultadoEmbargo);

                    seizureSummaryRepository.save(resultadoEmbargo);

                    for (CuentaResultadoEmbargo cuentaResultadoEmbargo : cuentasResultadoEmbargo) {
                        cuentaResultadoEmbargo.setResultadoEmbargo(resultadoEmbargo);
                        seizureSummaryBankAccountRepository.save(cuentaResultadoEmbargo);
                    }
                }
            }

            // TODO: ESTADO LEVANTAMIENTO RECHAZADO?

            // TODO: move to mapper ......
            // TODO: rounding @ big decimal? scale?
            FicheroFinal ficheroFinal = new FicheroFinal();
            ficheroFinal.setCodFicheroDiligencias(BigDecimal.valueOf(ficheroFase3.getCodControlFichero()));
            ficheroFinal.setControlFichero(ficheroFase6);
            ficheroFinal.setImporte(importeTotalNeto);
            ficheroFinal.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
            ficheroFinal.setUsuarioUltModificacion(EmbargosConstants.USER_AUTOMATICO);
            // TODO: calc?
            ficheroFinal.setFValor(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMdd));

            //Se inicializa el estado de contabilizacion del fichero final:
            EstadoContabilizacion estadoContabilidadInicial = determineEstadoContabilizacionInicial(ficheroFase6.getEntidadesComunicadora());
            ficheroFinal.setEstadoContabilizacion(estadoContabilidadInicial);
            
            finalFileRepository.save(ficheroFinal);

            EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero(
                    EmbargosConstants.COD_ESTADO_CTRLFICHERO_FINAL_GENERADO,
                    EmbargosConstants.COD_TIPO_FICHERO_COM_RESULTADO_FINAL_NORMA63);
            ficheroFase6.setEstadoCtrlfichero(estadoCtrlfichero);

            ficheroFase6.setUsuarioUltModificacion(user);
            ficheroFase6.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
            fileControlRepository.save(ficheroFase6);

            // Mover a outbox
            /*
            String outboxGenerated = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_OUTBOX);
            fileWriterHelper.transferToOutbox(fase6File, outboxGenerated, fileNameFinal);
             */
        }
        catch (Exception e)
        {
            LOG.error("Error while creating Norma63 - fase 6 contents.", e);
            throw e;
        }
        finally {
            if (beanReader != null)
                beanReader.close();
        }
    }

    private void generarFase6AEAT(ControlFichero ficheroFase3, List<Embargo> embargos, String user) throws ICEException, IOException
    {
        BeanReader beanReader = null;

        try {
            // Guardar fase 6
           // String pathGenerated = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_GENERATED);

            //String fileNameFinal = ficheroFase3.getEntidadesComunicadora().getPrefijoFicheros() +"_"+ ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMdd) +"."+ EmbargosConstants.TIPO_FICHERO_FINAL;

            //File fase6File = fileWriterHelper.getGeneratedFile(pathGenerated, fileNameFinal);

            //Se guarda el registro de ControlFichero del fichero de salida:
            ControlFichero ficheroFase6 =
                    fileControlMapper.generateControlFichero(null, EmbargosConstants.COD_TIPO_FICHERO_FICHERO_FINAL_AEAT_INTERNAL, null, null);
            ficheroFase6.setEntidadesComunicadora(ficheroFase3.getEntidadesComunicadora());

            ficheroFase6.setUsuarioUltModificacion(user);
            ficheroFase6.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
            fileControlRepository.save(ficheroFase6);

            // Inicialiar beanIO parser
            StreamFactory factory = StreamFactory.newInstance();
            factory.loadResource(pathFileConfigAEAT);

            beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_AEAT_DILIGENCIAS, (new File(ficheroFase3.getRutaFichero()).getCanonicalFile()));

            Object currentF3Record = null;

            BigDecimal importeTotalNeto = BigDecimal.ZERO;

            while ((currentF3Record = beanReader.read()) != null)
            {
                if(EmbargosConstants.RECORD_NAME_AEAT_ENTIDADTRANSMISORA.equals(beanReader.getRecordName())) {
                    ;
                }
                else if(EmbargosConstants.RECORD_NAME_AEAT_ENTIDADCREDITO.equals(beanReader.getRecordName())) {
                    ; // skip?
                }
                else if(EmbargosConstants.RECORD_NAME_AEAT_DILIGENCIA.equals(beanReader.getRecordName())) {

                    DiligenciaFase3 diligenciaFase3 = (DiligenciaFase3) currentF3Record;

                    LOG.info("Treating "+ diligenciaFase3.getNumeroDiligenciaEmbargo());

                    Embargo embargo = findByNumeroEmbargo(embargos, diligenciaFase3.getNumeroDiligenciaEmbargo());
                    Traba traba = seizedRepository.getByEmbargo(embargo);
                    List<LevantamientoTraba> levantamientos = liftingRepository.findAllByTraba(traba);

                    BigDecimal importeTotalLevantadoEmbargo = BigDecimal.ZERO;

                    ResultadoEmbargo resultadoEmbargo = new ResultadoEmbargo();
                    resultadoEmbargo.setTraba(traba);
                    resultadoEmbargo.setEmbargo(embargo);

                    resultadoEmbargo.setControlFichero(ficheroFase6);

                    List<CuentaResultadoEmbargo> cuentasResultadoEmbargo = new ArrayList<>(6);
                    resultadoEmbargo.setCuentaResultadoEmbargos(cuentasResultadoEmbargo);

                    for (CuentaEmbargo cuentaEmbargo : embargo.getCuentaEmbargos()) {
                        CuentaResultadoEmbargo cuentaResultadoEmbargo = new CuentaResultadoEmbargo();

                        cuentaResultadoEmbargo.setCuentaEmbargo(cuentaEmbargo);

                        for (CuentaTraba cuentaTraba : traba.getCuentaTrabas()) {
                            if (cuentaTraba.getCuenta().equals(cuentaEmbargo.getCuenta())) {
                                cuentaResultadoEmbargo.setCuentaTraba(cuentaTraba);
                                break;
                            }
                        }

                        if (cuentaResultadoEmbargo.getCuentaTraba() != null) {
                            BigDecimal importeLevantadoCuenta = BigDecimal.ZERO;

                            if (levantamientos != null) {
                                for (LevantamientoTraba levantamientoTraba : levantamientos) {
                                    for (CuentaLevantamiento cuentaLevantamiento : levantamientoTraba.getCuentaLevantamientos()) {
                                        if (cuentaLevantamiento.getCuenta().equals(cuentaEmbargo.getCuenta())) {
                                            // TODO: DIVISA
                                            importeLevantadoCuenta = importeLevantadoCuenta.add(cuentaLevantamiento.getImporte());
                                        }
                                    }
                                }
                            }

                            LOG.info("Importe cuenta traba " + cuentaResultadoEmbargo.getCuentaTraba().getCodCuentaTraba() + " " + (cuentaResultadoEmbargo.getCuentaTraba().getImporte() == null) + " " + importeLevantadoCuenta);

                            if (BigDecimal.ZERO.compareTo(importeLevantadoCuenta) == 0)
                                cuentaResultadoEmbargo.setImporteNeto(BigDecimal.ZERO);
                            else
                            {
                                // TODO what if get importe == 0? null? error
                                if (cuentaResultadoEmbargo.getCuentaTraba().getImporte() != null)
                                    cuentaResultadoEmbargo.setImporteNeto(cuentaResultadoEmbargo.getCuentaTraba().getImporte().subtract(importeLevantadoCuenta));

                                importeTotalLevantadoEmbargo = importeTotalLevantadoEmbargo.add(importeLevantadoCuenta);
                            }

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
                        }

                        cuentasResultadoEmbargo.add(cuentaResultadoEmbargo);
                    }

                    resultadoEmbargo.setTotalLevantado(importeTotalLevantadoEmbargo);

                    if (traba.getImporteTrabado() == null) {
                        resultadoEmbargo.setTotalNeto(BigDecimal.ZERO);
                    }
                    else {
                        resultadoEmbargo.setTotalNeto(traba.getImporteTrabado().subtract(importeTotalLevantadoEmbargo));
                        importeTotalNeto = importeTotalNeto.add(resultadoEmbargo.getTotalNeto());
                    }
                    EstadoResultadoEmbargo estadoResultadoEmbargo = new EstadoResultadoEmbargo();
                    if (BigDecimal.ZERO.compareTo(resultadoEmbargo.getTotalNeto()) == 0)
                        estadoResultadoEmbargo.setCodEstadoResultadoEmbargo(EmbargosConstants.ESTADO_FINAL_LEVANTAMIENTO_TOTAL);
                    else if (BigDecimal.ZERO.compareTo(importeTotalLevantadoEmbargo) == 0)
                        estadoResultadoEmbargo.setCodEstadoResultadoEmbargo(EmbargosConstants.ESTADO_FINAL_SIN_ORDEN_LEVANTAMIENTO);
                    else if (BigDecimal.ZERO.compareTo(resultadoEmbargo.getTotalNeto()) == -1)
                        estadoResultadoEmbargo.setCodEstadoResultadoEmbargo(EmbargosConstants.ESTADO_FINAL_LEVANTAMIENTO_PARCIAL);
                    else
                        estadoResultadoEmbargo.setCodEstadoResultadoEmbargo(EmbargosConstants.ESTADO_FINAL_OTROS);
                    resultadoEmbargo.setEstadoResultadoEmbargo(estadoResultadoEmbargo);

                    seizureSummaryRepository.save(resultadoEmbargo);

                    for (CuentaResultadoEmbargo cuentaResultadoEmbargo : cuentasResultadoEmbargo) {
                        cuentaResultadoEmbargo.setResultadoEmbargo(resultadoEmbargo);
                        seizureSummaryBankAccountRepository.save(cuentaResultadoEmbargo);
                    }
                }
            }

            // TODO: ESTADO LEVANTAMIENTO RECHAZADO?

            // TODO: move to mapper ......
            // TODO: rounding @ big decimal? scale?
            FicheroFinal ficheroFinal = new FicheroFinal();
            ficheroFinal.setCodFicheroDiligencias(BigDecimal.valueOf(ficheroFase3.getCodControlFichero()));
            ficheroFinal.setControlFichero(ficheroFase6);
            ficheroFinal.setImporte(importeTotalNeto);
            ficheroFinal.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
            ficheroFinal.setUsuarioUltModificacion(EmbargosConstants.USER_AUTOMATICO);
            // TODO: calc?
            ficheroFinal.setFValor(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMdd));

            //Se inicializa el estado de contabilizacion del fichero final:
            EstadoContabilizacion estadoContabilidadInicial = determineEstadoContabilizacionInicial(ficheroFase6.getEntidadesComunicadora());
            ficheroFinal.setEstadoContabilizacion(estadoContabilidadInicial);

            finalFileRepository.save(ficheroFinal);

            EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero(
                    EmbargosConstants.COD_ESTADO_CTRLFICHERO_FINAL_AEAT_GENERADO,
                    EmbargosConstants.COD_TIPO_FICHERO_FICHERO_FINAL_AEAT_INTERNAL);
            ficheroFase6.setEstadoCtrlfichero(estadoCtrlfichero);

            ficheroFase6.setUsuarioUltModificacion(user);
            ficheroFase6.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
            fileControlRepository.save(ficheroFase6);

            /*
            // Mover a outbox
            String outboxGenerated = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_OUTBOX);
            fileWriterHelper.transferToOutbox(fase6File, outboxGenerated, fileNameFinal);
            */
        }
        catch (Exception e)
        {
            LOG.error("Error while creating AEAT - Final Phase contents.", e);
            throw e;
        }
        finally {
            if (beanReader != null)
                beanReader.close();
        }
    }

    private Embargo findByNumeroEmbargo(List<Embargo> embargos, String codigoDeuda)
    {
        Embargo embargo = null;

        for (Embargo currentEmbargo : embargos)
        {
            if (codigoDeuda.equals(currentEmbargo.getNumeroEmbargo()))
                embargo = currentEmbargo;
        }

        return embargo;
    }
    
    
    private EstadoContabilizacion determineEstadoContabilizacionInicial(EntidadesComunicadora entidadComunicadora) {
    	
    	//Determinacion del estado inicial de contabilizacion:
    	//Si la Entidad Comunicadora:
        // - tiene cuenta de commerzbank -> cambiar al estado 'Pendiente de envio a contabilidad'.
        // - en caso contrario -> cambia al estado 'No aplica'.
    	
    	EstadoContabilizacion estadoContabilizacion = new EstadoContabilizacion();
    	
    	if (entidadComunicadora!=null && entidadComunicadora.getCuenta()!=null && !entidadComunicadora.getCuenta().trim().isEmpty()) {
    		
    		estadoContabilizacion.setCodEstado(EmbargosConstants.COD_ESTADO_CONTABILIZACION_PENDIENTE_ENVIO_A_CONTABILIDAD);
    	
    	} else {
    		estadoContabilizacion.setCodEstado(EmbargosConstants.COD_ESTADO_CONTABILIZACION_NOTAPPLY);
    	}
    	
    	return estadoContabilizacion;
    }
}
/*

		try {
			String fileNameInformacion = "";
			GeneralParameter folderName = generalParameterService.viewParameter(ValueConstants.FOLDER_NAME_FILE_CON);
			File folder = new File(folderName.getValue());
			List<RegistroApunteContable> list = accountingNoteService.listAccountingNoteStatusPending(ValueConstants.STATUS_TASK_PENDING);

			StreamFactory factory = StreamFactory.newInstance();
			factory.loadResource(pathFileConfigContabilidad);

			File ficheroSalida = null;
			int pos = existsFile(folder.list());

			if (pos == -1) {
				String pattern = "yyyyMMdd_HHmmss";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

				Date date = new Date();
				fileNameInformacion = simpleDateFormat.format(date) + ValueConstants.TIPO_FICHERO_CONTABILIZACION; //Preguntar como se genera el nombre del fichero
				ficheroSalida = new File(folderName.getValue() + "\\" + fileNameInformacion);

			} else  {
				ficheroSalida = folder.listFiles()[pos];
			}

			if (list != null && list.size() > 0) {

				beanWriter = factory.createWriter(ValueConstants.STREAM_NAME_CONTABILIDAD, ficheroSalida);

				for (RegistroApunteContable r : list) {
					beanWriter.write(ValueConstants.RECORD_NAME_APUNTE_CONTABLE, r);
					accountingNoteService.updateNombreFichero(fileNameInformacion, r.getAplicacion(), r.getFechaActual(), r.getContador());
				}

				beanWriter.flush();
				accountingNoteService.updateStatusByList(list);
			}
		} catch (Exception e) {
			logger.error("Error Generacion Contabilidad - " + e);
			throw new Exception(e);
		} finally {
			if (beanWriter != null) {
				beanWriter.close();

			}
		}
 */