package es.commerzbank.ice.embargos.scheduled;

import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import es.commerzbank.ice.embargos.domain.entity.LevantamientoTraba;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.OrdenEjecucionEmbargoFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase6.ResultadoFinalEmbargoFase6;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.LiftingRepository;
import es.commerzbank.ice.embargos.repository.SeizedRepository;
import es.commerzbank.ice.embargos.repository.SeizureRepository;
import es.commerzbank.ice.utils.EmbargosConstants;
import org.beanio.BeanReader;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

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

    /* to cronify
    @Scheduled("")
    public void doFase6()
    {
        // get files type XXX which expire today
        for each file
        process()
    }
    */

    public void generarFase6(Long codControlFichero) throws ICEException {
        ControlFichero ficheroFase3 = fileControlRepository.getOne(codControlFichero);

        // type N63 F3 is assumed - value 8

        List<Embargo> embargos = seizureRepository.findAllByControlFichero(ficheroFase3);

        if (embargos == null)
            throw new ICEException("", "No seizures found for code "+ codControlFichero);

        BeanWriter beanWriter = null;
        BeanReader beanReader = null;

        try {
            // Inicialiar beanIO parser
            StreamFactory factory = StreamFactory.newInstance();
            factory.loadResource(pathFileConfigCuaderno63);
            // TODO: NUEVO CAMPO en control fichero: RUTA INTERNA
            beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_CUADERNO63_FASE3, ficheroFase3.getNombreFichero());

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

                    Embargo embargo = findByNumeroEmbargo(embargos, ordenEjecucionEmbargo.getCodigoDeuda());
                    Traba traba = seizedRepository.getByEmbargo(embargo);
                    List<LevantamientoTraba> levantamientos = liftingRepository.findAllByCodTraba(traba);

                    /* TODO probar qué hacer con los levantamientos levantados más de una vez. Agregar? */
                    ResultadoFinalEmbargoFase6 resultadoFinalEmbargoFase6 =
                            cuaderno63Mapper.generateComunicacionResultadoRetencionFase4(embargo, traba, cuentaTrabaOrderedList);

                    beanWriter.write(EmbargosConstants.RECORD_NAME_RESULTADOFINALEMBARGO, ResultadoFinalEmbargoFase6);

                    beanWriter.flush();
                }
            }
        }
        catch (Exception e)
        {
            LOG.error("Error while creating Norma63 - fase 6 file.", e);
        }
        finally {
            if (beanReader != null)
                beanReader.close();
            if (beanWriter != null)
                beanWriter.close();
        }
    }

    private Embargo findByNumeroEmbargo(List<Embargo> embargos, String embargo)
    {

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
