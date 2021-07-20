package es.commerzbank.ice.embargos.service.files.impl;

import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.embargos.domain.dto.ClientLiftingManualDTO;
import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora;
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
import java.util.List;
import java.util.Map;

import static es.commerzbank.ice.comun.lib.util.ValueConstants.PARAMETRO_TEMPFOLDER;

@Service
public class Cuaderno63ManualLiftingServiceImpl
    implements Cuaderno63ManualLiftingService
{
    @Autowired
    private GeneralParametersService generalParametersService;

    @Value("${commerzbank.embargos.beanio.config-path.cuaderno63}")
    private String beanioResource;

    @Override
    public void crearFicheroLevantamientos(EntidadesComunicadora entity, Map<String, List<ClientLiftingManualDTO>> ordenesPorCliente)
            throws Exception
    {
        String encoding = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_ENCODING_NORMA63);
        String tempFolder = generalParametersService.loadStringParameter(PARAMETRO_TEMPFOLDER);
        String inboxFolder = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_AEAT_INCOMING);

        Writer writer = null;
        BeanWriter beanWriter = null;

        try {
            String fileName = "LevManualAEAT_" + RandomStringUtils.randomAlphanumeric(5) + ".tmp";
            File ficheroSalida = new File(tempFolder, fileName);

            writer = new OutputStreamWriter(new FileOutputStream(ficheroSalida), encoding);

            // create a StreamFactory
            StreamFactory factory = StreamFactory.newInstance();
            // load the mapping file
            factory.loadResource(beanioResource);

            beanWriter = factory.createWriter(EmbargosConstants.STREAM_NAME_CUADERNO63_FASE5, writer);

            writeCabeceraEmisor(beanWriter, entity);

            for (List<ClientLiftingManualDTO> ordenesCliente : ordenesPorCliente.values()) {
                writeOrdenLevantamientoRetencion(beanWriter, ordenesCliente);
            }

            writeFinFichero(beanWriter);

            beanWriter.flush();

            beanWriter.close();
            writer.close();

            FileUtils.moveFile(ficheroSalida, new File(inboxFolder, fileName));
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

    private void writeOrdenLevantamientoRetencion(BeanWriter beanWriter, List<ClientLiftingManualDTO> ordenesCliente) {
        OrdenLevantamientoRetencionFase5 levantamiento = new OrdenLevantamientoRetencionFase5();

        ClientLiftingManualDTO ref = ordenesCliente.get(0);

        levantamiento.setNifDeudor(ref.getNif());
        levantamiento.setIdentificadorDeuda(ref.getCodLifting());
        levantamiento.setCodigoTipoLevantamientoARealizar("Total".equals(ref.getType()) ? "1" : "2");

        int slotDisponible = 1;
        for (ClientLiftingManualDTO ordenLevantamiento : ordenesCliente) {
            if (slotDisponible == 1) {
                levantamiento.setIbanCuenta1(ordenLevantamiento.getIban());
                levantamiento.setCodigoTipoLevantamientoIban1("Total".equals(ordenLevantamiento.getType()) ? "1" : "2");
                levantamiento.setImporteALevantarIban1(ordenLevantamiento.getAmount());
            }
            else if (slotDisponible == 2) {
                levantamiento.setIbanCuenta2(ordenLevantamiento.getIban());
                levantamiento.setCodigoTipoLevantamientoIban2("Total".equals(ordenLevantamiento.getType()) ? "1" : "2");
                levantamiento.setImporteALevantarIban2(ordenLevantamiento.getAmount());
            }
            else if (slotDisponible == 3) {
                levantamiento.setIbanCuenta3(ordenLevantamiento.getIban());
                levantamiento.setCodigoTipoLevantamientoIban3("Total".equals(ordenLevantamiento.getType()) ? "1" : "2");
                levantamiento.setImporteALevantarIban3(ordenLevantamiento.getAmount());
            }
            else if (slotDisponible == 4) {
                levantamiento.setIbanCuenta4(ordenLevantamiento.getIban());
                levantamiento.setCodigoTipoLevantamientoIban4("Total".equals(ordenLevantamiento.getType()) ? "1" : "2");
                levantamiento.setImporteALevantarIban4(ordenLevantamiento.getAmount());
            }
            else if (slotDisponible == 5) {
                levantamiento.setIbanCuenta5(ordenLevantamiento.getIban());
                levantamiento.setCodigoTipoLevantamientoIban5("Total".equals(ordenLevantamiento.getType()) ? "1" : "2");
                levantamiento.setImporteALevantarIban5(ordenLevantamiento.getAmount());
            }
            else if (slotDisponible == 6) {
                levantamiento.setIbanCuenta6(ordenLevantamiento.getIban());
                levantamiento.setCodigoTipoLevantamientoIban6("Total".equals(ordenLevantamiento.getType()) ? "1" : "2");
                levantamiento.setImporteALevantarIban6(ordenLevantamiento.getAmount());
            }
            slotDisponible ++;
        }

        beanWriter.write(EmbargosConstants.RECORD_NAME_AEAT_ENTIDADTRANSMISORA, levantamiento);
    }

    private void writeCabeceraEmisor(BeanWriter beanWriter, EntidadesComunicadora entity) {
        CabeceraEmisorFase5 cabeceraEmisor = new CabeceraEmisorFase5();
        cabeceraEmisor.setNifOrganismoEmisor(entity.getNifEntidad());
        cabeceraEmisor.setFechaObtencionFicheroOrganismo(new Date());
        beanWriter.write(EmbargosConstants.RECORD_NAME_AEAT_ENTIDADTRANSMISORA, cabeceraEmisor);
    }

    private void writeFinFichero(BeanWriter beanWriter) {
        FinFicheroFase5 finFichero = new FinFicheroFase5();
        beanWriter.write(EmbargosConstants.RECORD_NAME_AEAT_ENTIDADTRANSMISORA, finFichero);
    }

}
