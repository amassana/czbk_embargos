package es.commerzbank.ice.embargos.scheduled;

import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.domain.entity.*;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.OrdenEjecucionEmbargoFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase6.ResultadoFinalEmbargoFase6;
import es.commerzbank.ice.embargos.repository.*;
import es.commerzbank.ice.utils.EmbargosConstants;
import org.beanio.BeanReader;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
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

    @Value("${commerzbank.embargos.files.path.processed}")
    private String pathProcessed;

    /* to cronify
    @Scheduled("")
    public void doFase6()
    {
        // get files type XXX which expire today
        for each file
        process()
    }
    */

    public void generarFase6(Long codControlFichero) throws ICEException, IOException {
        ControlFichero ficheroFase3 = fileControlRepository.getOne(codControlFichero);

        // type N63 F3 is assumed - value 8

        List<Embargo> embargos = seizureRepository.findAllByControlFichero(ficheroFase3);

        if (embargos == null)
            throw new ICEException("", "No seizures found for code "+ codControlFichero);

        BeanReader beanReader = null;

        try {
            // Inicialiar beanIO parser
            StreamFactory factory = StreamFactory.newInstance();
            factory.loadResource(pathFileConfigCuaderno63);
            // TODO: NUEVO CAMPO en control fichero: RUTA INTERNA
            beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_CUADERNO63_FASE3, (new File(pathProcessed + ficheroFase3.getNombreFichero()).getCanonicalFile()));

            Object currentF3Record = null;

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

                    BigDecimal importeTotalLevantado = BigDecimal.ZERO;

                    ResultadoEmbargo resultadoEmbargo = new ResultadoEmbargo();
                    resultadoEmbargo.setTraba(traba);
                    resultadoEmbargo.setEmbargo(embargo);
                    //resultadoEmbargo.setEstadoResultadoEmbargo();
                    //resultadoEmbargo.set

                    List<CuentaResultadoEmbargo> cuentasResultadoEmbargo = new ArrayList<>(6);
                    resultadoEmbargo.setCuentaResultadoEmbargos(cuentasResultadoEmbargo);

                    for (CuentaEmbargo cuentaEmbargo : embargo.getCuentaEmbargos())
                    {
                        CuentaResultadoEmbargo cuentaResultadoEmbargo = new CuentaResultadoEmbargo();

                        cuentaResultadoEmbargo.setCuentaEmbargo(cuentaEmbargo);

                        for (CuentaTraba cuentaTraba : traba.getCuentaTrabas())
                        {
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

                            LOG.info("Importe cuenta traba "+ cuentaResultadoEmbargo.getCuentaTraba().getCodCuentaTraba() +" "+ (cuentaResultadoEmbargo.getCuentaTraba().getImporte() == null) +" "+ importeLevantadoCuenta);

                            if (BigDecimal.ZERO.compareTo(importeLevantadoCuenta) != 0) {
                                // TODO what if get importe == 0? null? error
                                if (cuentaResultadoEmbargo.getCuentaTraba().getImporte() != null)
                                    cuentaResultadoEmbargo.setImporteNeto(cuentaResultadoEmbargo.getCuentaTraba().getImporte().subtract(importeLevantadoCuenta));

                                importeTotalLevantado = importeTotalLevantado.add(importeLevantadoCuenta);
                            }
                            // TODO cuentaResultadoEmbargo.setEstadoResultadoEmbargo();
                        }

                        cuentasResultadoEmbargo.add(cuentaResultadoEmbargo);
                    }

                    resultadoEmbargo.setTotalLevantado(importeTotalLevantado);
                    if (traba.getImporteTrabado() != null)
                        resultadoEmbargo.setTotalNeto(traba.getImporteTrabado().subtract(importeTotalLevantado));

                    // TODO resultadoEmbargo.setEstadoResultadoEmbargo();

                    seizureSummaryRepository.save(resultadoEmbargo);

                    for (CuentaResultadoEmbargo cuentaResultadoEmbargo : cuentasResultadoEmbargo) {
                        cuentaResultadoEmbargo.setResultadoEmbargo(resultadoEmbargo);
                        seizureSummaryBankAccountRepository.save(cuentaResultadoEmbargo);
                    }

                    /* TODO probar qué hacer con los levantamientos levantados más de una vez. Agregar? por ahora sí*/
                }
            }
        }
        catch (Exception e)
        {
            LOG.error("Error while creating Norma63 - fase 6 file.", e);
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
