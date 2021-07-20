package es.commerzbank.ice.embargos.service.files.impl;

import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.embargos.domain.dto.ClientLiftingManualDTO;
import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora;
import es.commerzbank.ice.embargos.formats.aeat.levantamientotrabas.*;
import es.commerzbank.ice.embargos.service.files.AEATManualLiftingService;
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
import java.util.List;
import java.util.Map;

import static es.commerzbank.ice.comun.lib.util.ValueConstants.PARAMETRO_TEMPFOLDER;

@Service
public class AEATManualLiftingServiceImpl
        implements AEATManualLiftingService
{
    @Autowired
    private GeneralParametersService generalParametersService;

    @Value("${commerzbank.embargos.beanio.config-path.aeat}")
    private String beanioResource;

    @Override
    public void crearFicheroLevantamientos(EntidadesComunicadora entity, Map<String, List<ClientLiftingManualDTO>> ordenesPorCliente)
            throws Exception
    {
        String encoding = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_ENCODING_AEAT);
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

            beanWriter = factory.createWriter(EmbargosConstants.STREAM_NAME_AEAT_LEVANTAMIENTOTRABAS, writer);

            writeEntidadTransmisora(beanWriter);
            writeEntidadCredito(beanWriter, entity);

            for (List<ClientLiftingManualDTO> ordenesCliente : ordenesPorCliente.values()) {
                writeLevantamiento(beanWriter, ordenesCliente);
            }

            writeFinEntidadCredito(beanWriter);
            writeFinEntidadTransmisora(beanWriter);
            writeRegistroControlEntidadTransmisora(beanWriter);

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

    private void writeEntidadTransmisora(BeanWriter beanWriter) {
        EntidadTransmisora entidadTransmisora = new EntidadTransmisora();
        entidadTransmisora.setCodigoEntidadTransmisora(EmbargosConstants.CODIGO_NRBE_COMMERZBANK);
        beanWriter.write(EmbargosConstants.RECORD_NAME_AEAT_ENTIDADTRANSMISORA, entidadTransmisora);
    }

    private void writeEntidadCredito(BeanWriter beanWriter, EntidadesComunicadora entity) {
        EntidadCredito entidadCredito = new EntidadCredito();
        entidadCredito.setDelegacionAgenciaReceptora(entity.getIdentificadorEntidad());
        beanWriter.write(EmbargosConstants.RECORD_NAME_AEAT_ENTIDADCREDITO, entidadCredito);
    }

    private void writeLevantamiento(BeanWriter beanWriter, List<ClientLiftingManualDTO> ordenesCliente) {
        Levantamiento levantamiento = new Levantamiento();
        //levantamiento.setIndicadorRegistro("");
        ClientLiftingManualDTO ref = ordenesCliente.get(0);
        levantamiento.setNifDeudor(ref.getNif());
        levantamiento.setNombreDeudor(ref.getDebtor());
        levantamiento.setNumeroDiligenciaEmbargo(ref.getCodLifting());
        /*
        levantamiento.setImporteTotalAEmbargar(new BigDecimal("0"));
        levantamiento.setFechaGeneracionDiligencia(new Date());
        levantamiento.setImporteTotalTrabado(new BigDecimal("0"));
        levantamiento.setFechaTraba(new Date());
        levantamiento.setFechaLimiteIngresoImporteTrabado(new Date());
        */
        int slotDisponible = 1;
        for (ClientLiftingManualDTO ordenLevantamiento : ordenesCliente) {
            if (slotDisponible == 1) {
                levantamiento.setCodigoCuentaCliente1(ordenLevantamiento.getIban());
                levantamiento.setCodigoTipoLevantamientoEmbargoCC1("Total".equals(ordenLevantamiento.getType()) ? "01" : "02");
                levantamiento.setImporteALevantarCC1(ordenLevantamiento.getAmount());
            }
            else if (slotDisponible == 2) {
                levantamiento.setCodigoCuentaCliente2(ordenLevantamiento.getIban());
                levantamiento.setCodigoTipoLevantamientoEmbargoCC2("Total".equals(ordenLevantamiento.getType()) ? "01" : "02");
                levantamiento.setImporteALevantarCC2(ordenLevantamiento.getAmount());
            }
            else if (slotDisponible == 3) {
                levantamiento.setCodigoCuentaCliente3(ordenLevantamiento.getIban());
                levantamiento.setCodigoTipoLevantamientoEmbargoCC3("Total".equals(ordenLevantamiento.getType()) ? "01" : "02");
                levantamiento.setImporteALevantarCC3(ordenLevantamiento.getAmount());
            }
            else if (slotDisponible == 4) {
                levantamiento.setCodigoCuentaCliente4(ordenLevantamiento.getIban());
                levantamiento.setCodigoTipoLevantamientoEmbargoCC4("Total".equals(ordenLevantamiento.getType()) ? "01" : "02");
                levantamiento.setImporteALevantarCC4(ordenLevantamiento.getAmount());
            }
            else if (slotDisponible == 5) {
                levantamiento.setCodigoCuentaCliente5(ordenLevantamiento.getIban());
                levantamiento.setCodigoTipoLevantamientoEmbargoCC5("Total".equals(ordenLevantamiento.getType()) ? "01" : "02");
                levantamiento.setImporteALevantarCC5(ordenLevantamiento.getAmount());
            }
            else if (slotDisponible == 6) {
                levantamiento.setCodigoCuentaCliente6(ordenLevantamiento.getIban());
                levantamiento.setCodigoTipoLevantamientoEmbargoCC6("Total".equals(ordenLevantamiento.getType()) ? "01" : "02");
                levantamiento.setImporteALevantarCC6(ordenLevantamiento.getAmount());
            }
            slotDisponible ++;
        }

        beanWriter.write(EmbargosConstants.RECORD_NAME_AEAT_LEVANTAMIENTO, levantamiento);
    }

    private void writeFinEntidadCredito(BeanWriter beanWriter) {
        FinEntidadCredito finEntidadCredito = new FinEntidadCredito();
        beanWriter.write(EmbargosConstants.RECORD_NAME_AEAT_FINENTIDADCREDITO, finEntidadCredito);
    }

    private void writeFinEntidadTransmisora(BeanWriter beanWriter) {
        FinEntidadTransmisora finEntidadTransmisora = new FinEntidadTransmisora();
        beanWriter.write(EmbargosConstants.RECORD_NAME_AEAT_FINENTIDADTRANSMISORA, finEntidadTransmisora);
    }

    private void writeRegistroControlEntidadTransmisora(BeanWriter beanWriter) {
        RegistroControlEntidadTransmisora registroControlEntidadTransmisora = new RegistroControlEntidadTransmisora();
        beanWriter.write(EmbargosConstants.RECORD_NAME_AEAT_REGISTROCONTROLENTIDADTRANSMISORA, registroControlEntidadTransmisora);
    }
}
