package es.commerzbank.ice.embargos.service.files.impl;

import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.embargos.domain.dto.CommunicatingEntity;
import es.commerzbank.ice.embargos.domain.dto.ManualLiftingDTO;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase5.CabeceraEmisorFase5;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase5.FinFicheroFase5;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase5.OrdenLevantamientoRetencionFase5;
import es.commerzbank.ice.embargos.service.files.Cuaderno63ManualLiftingService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;

import static es.commerzbank.ice.comun.lib.util.ValueConstants.PARAMETRO_TEMPFOLDER;
import static es.commerzbank.ice.embargos.utils.EmbargosConstants.TIPO_FICHERO_LEVANTAMIENTOS;

@Service
public class Cuaderno63ManualLiftingServiceImpl
    implements Cuaderno63ManualLiftingService
{
    @Autowired
    private GeneralParametersService generalParametersService;

    @Value("${commerzbank.embargos.beanio.config-path.cuaderno63}")
    private String beanioResource;

    @Override
    public String crearFicheroLevantamientos(ManualLiftingDTO manualLiftingDTO)
            throws Exception
    {
        String encoding = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_ENCODING_NORMA63);
        String tempFolder = generalParametersService.loadStringParameter(PARAMETRO_TEMPFOLDER);
        String inboxFolder = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_INCOMING);

        Writer writer = null;
        BeanWriter beanWriter = null;

        try {
            String fileName = manualLiftingDTO.getCommunicatingEntity().getFilePrefix() + "_Manual_" + RandomStringUtils.randomAlphanumeric(5) +"."+ TIPO_FICHERO_LEVANTAMIENTOS;
            File ficheroSalida = new File(tempFolder, fileName);

            writer = new OutputStreamWriter(new FileOutputStream(ficheroSalida), encoding);

            // create a StreamFactory
            StreamFactory factory = StreamFactory.newInstance();
            // load the mapping file
            factory.loadResource(beanioResource);

            beanWriter = factory.createWriter(EmbargosConstants.STREAM_NAME_CUADERNO63_FASE5, writer);

            writeCabeceraEmisor(beanWriter, manualLiftingDTO.getCommunicatingEntity());

            writeOrdenLevantamientoRetencion(beanWriter, manualLiftingDTO);

            writeFinFichero(beanWriter);

            beanWriter.flush();

            beanWriter.close();
            writer.close();

            FileUtils.moveFile(ficheroSalida, new File(inboxFolder, fileName));

            return fileName;
        }
        catch (Exception e) {
            if(writer!=null) {
                writer.close();
            }
            if (beanWriter != null) {
                beanWriter.close();
            }
            throw e;
        }
    }

    private void writeOrdenLevantamientoRetencion(BeanWriter beanWriter, ManualLiftingDTO ordenesCliente) {
        OrdenLevantamientoRetencionFase5 levantamiento = new OrdenLevantamientoRetencionFase5();
        levantamiento.setCodigoRegistro(6);

        levantamiento.setNifDeudor(ordenesCliente.getSeizureCase().getNIF());
        levantamiento.setNombreDeudor(ordenesCliente.getSeizureCase().getNameInternal());
        levantamiento.setIdentificadorDeuda(ordenesCliente.getSeizureCase().getSeizureNumber());

        levantamiento.setCodigoTipoLevantamientoARealizar(ordenesCliente.getLiftedAmount().compareTo(ordenesCliente.getSeizureCase().getSeizedAmount()) == 0 ? "1" : "2");

        levantamiento.setImporteTotalAEmbargar(ordenesCliente.getSeizureCase().getRequestedAmount());
        levantamiento.setImporteTotalRetencionesEfectuadas(ordenesCliente.getSeizureCase().getSeizedAmount());

        levantamiento.setIbanCuenta1(ordenesCliente.getSeizedBankAccount().getIban());
        levantamiento.setCodigoTipoLevantamientoIban1(ordenesCliente.getLiftedAmount().compareTo(ordenesCliente.getSeizureCase().getSeizedAmount()) == 0 ? "1" : "2");
        levantamiento.setImporteALevantarIban1(ordenesCliente.getLiftedAmount());

        beanWriter.write(EmbargosConstants.RECORD_NAME_ORDENLEVANTAMIENTORETENCION, levantamiento);
    }

    private void writeCabeceraEmisor(BeanWriter beanWriter, CommunicatingEntity entity) {
        CabeceraEmisorFase5 cabeceraEmisor = new CabeceraEmisorFase5();
        cabeceraEmisor.setCodigoRegistro(4);
        cabeceraEmisor.setNifOrganismoEmisor(entity.getNif());
        cabeceraEmisor.setFechaObtencionFicheroOrganismo(new Date());
        beanWriter.write(EmbargosConstants.RECORD_NAME_CABECERAEMISOR, cabeceraEmisor);
    }

    private void writeFinFichero(BeanWriter beanWriter) {
        FinFicheroFase5 finFichero = new FinFicheroFase5();
        finFichero.setCodigoRegistro(8);
        beanWriter.write(EmbargosConstants.RECORD_NAME_FINFICHERO, finFichero);
    }

}
