package es.commerzbank.ice.embargos.service.files.impl;

import es.commerzbank.ice.comun.lib.file.exchange.FileWriterHelper;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.embargos.domain.entity.*;
import es.commerzbank.ice.embargos.domain.mapper.Cuaderno63Mapper;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.CabeceraEmisorFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.FinFicheroFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.OrdenEjecucionEmbargoComplementarioFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.OrdenEjecucionEmbargoFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase6.CabeceraEmisorFase6;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase6.FinFicheroFase6;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase6.ResultadoFinalEmbargoFase6;
import es.commerzbank.ice.embargos.repository.*;
import es.commerzbank.ice.embargos.service.files.Cuaderno63FinalResponseService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.EmbargosUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.beanio.BeanReader;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.List;

public class Cuaderno63FinalResponseServiceImpl
    implements Cuaderno63FinalResponseService
{
    private static final Logger logger = LoggerFactory.getLogger(Cuaderno63FinalResponseServiceImpl.class);

    @Value("${commerzbank.embargos.beanio.config-path.cuaderno63}")
    private String pathFileConfigCuaderno63;

    @Autowired
    private GeneralParametersService generalParametersService;

    @Autowired
    private FileWriterHelper fileWriterHelper;

    @Autowired
    private FileControlRepository fileControlRepository;

    @Autowired
    private SeizureRepository seizureRepository;

    @Autowired
    private SeizedRepository seizedRepository;

    @Autowired
    private LiftingRepository liftingRepository;

    @Autowired
    private Cuaderno63Mapper cuaderno63Mapper;

    @Autowired
    private FinalResponseRepository finalResponseRepository;

    @Override
    public void tramitarFicheroInformacion(ControlFichero ficheroFase3, FicheroFinal finalFile)
        throws Exception
    {
        ControlFichero ficheroFase6 = finalFile.getControlFichero();

        BeanReader beanReader = null;
        BeanWriter beanWriter = null;
        FileInputStream fileInputStream = null;

        Reader reader = null;
        Writer writer = null;

        try {
            // Inicialización ficheros
            String pathGenerated = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_GENERATED);
            File fase6File = fileWriterHelper.getGeneratedFile(pathGenerated, ficheroFase6.getNombreFichero());

            String encoding = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_ENCODING_NORMA63);

            StreamFactory factory = StreamFactory.newInstance();
            factory.loadResource(pathFileConfigCuaderno63);

            writer = new OutputStreamWriter(new FileOutputStream(fase6File), encoding);
            beanWriter = factory.createWriter(EmbargosConstants.STREAM_NAME_CUADERNO63_FASE6, writer);

            fileInputStream = new FileInputStream(ficheroFase3.getRutaFichero());
            reader = new InputStreamReader(fileInputStream, encoding);
            beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_CUADERNO63_FASE3, reader);

            // Generación de contenido
            int numeroRegistrosFichero = 0;

            Object readRecord = null;
            OrdenEjecucionEmbargoFase3 ordenEjecucionEmbargoFase3 = null;
            OrdenEjecucionEmbargoComplementarioFase3 ordenEjecucionEmbargoComplementarioFase3 = null;
            while ((readRecord = beanReader.read()) != null) {
                if (EmbargosConstants.RECORD_NAME_CABECERAEMISOR.equals(beanReader.getRecordName()))
                {
                    CabeceraEmisorFase3 cabeceraEmisorFase3 = (CabeceraEmisorFase3) readRecord;

                    CabeceraEmisorFase6 cabeceraEmisorFase6 = cuaderno63Mapper.generateCabeceraEmisorFase6(cabeceraEmisorFase3);

                    beanWriter.write(EmbargosConstants.RECORD_NAME_CABECERAEMISOR, cabeceraEmisorFase6);
                    beanWriter.flush();

                    numeroRegistrosFichero++;
                }
                else if (EmbargosConstants.RECORD_NAME_FINFICHERO.equals(beanReader.getRecordName()))
                {
                    FinFicheroFase3 finFicheroFase3 = (FinFicheroFase3) readRecord;

                    numeroRegistrosFichero++;

                    FinFicheroFase6 finFicheroFase6 = cuaderno63Mapper.generateFinFicheroFase6(finFicheroFase3, numeroRegistrosFichero, finalFile.getImporteLevantado(),
                            finalFile.getImporte());

                    beanWriter.write(EmbargosConstants.RECORD_NAME_FINFICHERO, finFicheroFase6);
                    beanWriter.flush();
                }
                else if (EmbargosConstants.RECORD_NAME_ORDENEJECUCIONEMBARGO.equals(beanReader.getRecordName()))
                {
                    ordenEjecucionEmbargoFase3 = (OrdenEjecucionEmbargoFase3) readRecord;
                }
                else if (EmbargosConstants.RECORD_NAME_ORDENEJECUCIONEMBARGOCOMPLEMENTARIO.equals(beanReader.getRecordName()))
                {
                    ordenEjecucionEmbargoComplementarioFase3 = (OrdenEjecucionEmbargoComplementarioFase3) readRecord;
                }

                if (ordenEjecucionEmbargoFase3 != null && ordenEjecucionEmbargoComplementarioFase3 != null) {
                    Pair<Embargo, Traba> laParella = findEmbargoTraba(ordenEjecucionEmbargoFase3.getIdentificadorDeuda());

                    Embargo embargo = laParella.getLeft();
                    Traba traba = laParella.getRight();
                    List<LevantamientoTraba> levantamientos = liftingRepository.findAllByTraba(traba);

                    ResultadoEmbargo resultadoEmbargo = finalResponseRepository.findByTraba(traba);

                    ResultadoFinalEmbargoFase6 resultadoFinalEmbargoFase6 =
                            cuaderno63Mapper.generateResultadoFinalEmbargoFase6(ordenEjecucionEmbargoFase3, ordenEjecucionEmbargoComplementarioFase3,
                                    embargo, traba, levantamientos, resultadoEmbargo);

                    beanWriter.write(EmbargosConstants.RECORD_NAME_RESULTADOFINALEMBARGO, resultadoFinalEmbargoFase6);
                    beanWriter.flush();

                    numeroRegistrosFichero++;

                    ordenEjecucionEmbargoFase3 = null;
                    ordenEjecucionEmbargoComplementarioFase3 = null;
                }
            }

            ficheroFase6.setNumCrc(es.commerzbank.ice.comun.lib.util.FileUtils.getMD5(fase6File.getCanonicalPath()));
            ficheroFase6.setRutaFichero(fase6File.getCanonicalPath());
            fileControlRepository.save(ficheroFase6);

            // TODO Cambiar el estado del fichero
            // TODO falta mapeo campo tipo levantamiento

            // Mover a outbox
            String outboxGenerated = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_OUTBOX);
            fileWriterHelper.transferToOutbox(fase6File, outboxGenerated, ficheroFase6.getNombreFichero());
        }
        catch (Exception e)
        {
            logger.error("Error Creando el fichero de fase 6", e);
            throw e;
        }
        finally
        {
            // Los BeanReader / Writer no son autocloseable.
            if(reader!=null) {
                reader.close();
            }
            if (beanReader != null) {
                beanReader.close();
            }
            if(writer!=null) {
                writer.close();
            }
            if (beanWriter != null) {
                beanWriter.close();
            }

            if (fileInputStream!=null) fileInputStream.close();
        }
    }

    private Pair<Embargo, Traba> findEmbargoTraba(String identificadorDeuda) {
        List<Embargo> embargos = seizureRepository.findAllByNumeroEmbargo(identificadorDeuda);

        if (embargos == null || embargos.size() == 0)
        {
            logger.error("No embargo found for "+ identificadorDeuda);
            return null;
        }

        Embargo embargo = EmbargosUtils.selectEmbargo(embargos);

        Traba traba = seizedRepository.getByEmbargo(embargo);

        if (traba == null)
        {
            logger.error("Levantamiento not found for embargo "+ embargo.getCodEmbargo() +" code "+ identificadorDeuda);
            return null;
        }

        return Pair.of(embargo, traba);
    }
}