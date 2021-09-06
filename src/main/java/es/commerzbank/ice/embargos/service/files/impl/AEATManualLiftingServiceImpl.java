package es.commerzbank.ice.embargos.service.files.impl;

import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.embargos.domain.dto.ManualLiftingDTO;
import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora;
import es.commerzbank.ice.embargos.formats.aeat.levantamientotrabas.*;
import es.commerzbank.ice.embargos.repository.CommunicatingEntityRepository;
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
import java.util.Date;

import static es.commerzbank.ice.comun.lib.util.ValueConstants.PARAMETRO_TEMPFOLDER;
import static es.commerzbank.ice.embargos.utils.EmbargosConstants.TIPO_FICHERO_LEVANTAMIENTOS;

@Service
public class AEATManualLiftingServiceImpl
        implements AEATManualLiftingService
{
    @Autowired
    private GeneralParametersService generalParametersService;

    @Value("${commerzbank.embargos.beanio.config-path.aeat}")
    private String beanioResource;

    @Autowired
    private CommunicatingEntityRepository communicatingEntityRepository;

    @Override
    public String crearFicheroLevantamientos(ManualLiftingDTO manualLiftingDTO)
            throws Exception
    {
        String encoding = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_ENCODING_AEAT);
        String tempFolder = generalParametersService.loadStringParameter(PARAMETRO_TEMPFOLDER);
        String inboxFolder = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_FILES_PATH_AEAT_INCOMING);

        Writer writer = null;
        BeanWriter beanWriter = null;

        EntidadesComunicadora entidadComunicadora = communicatingEntityRepository.getOne(manualLiftingDTO.getCommunicatingEntity().getCodCommunicatingEntity());

        try {
            String randomIdentifier = RandomStringUtils.randomAlphanumeric(5);
            String fileName = manualLiftingDTO.getCommunicatingEntity().getFilePrefix() + "_Manual_" + randomIdentifier +"."+ TIPO_FICHERO_LEVANTAMIENTOS;

            File ficheroSalida = new File(tempFolder, fileName);

            writer = new OutputStreamWriter(new FileOutputStream(ficheroSalida), encoding);

            // create a StreamFactory
            StreamFactory factory = StreamFactory.newInstance();
            // load the mapping file
            factory.loadResource(beanioResource);

            beanWriter = factory.createWriter(EmbargosConstants.STREAM_NAME_AEAT_LEVANTAMIENTOTRABAS, writer);

            writeEntidadTransmisora(beanWriter, manualLiftingDTO.getFileControlDTOF4().getCreatedDate());
            writeEntidadCredito(beanWriter, entidadComunicadora);

            writeLevantamiento(beanWriter, manualLiftingDTO);

            writeFinEntidadCredito(beanWriter);
            writeFinEntidadTransmisora(beanWriter);
            writeRegistroControlEntidadTransmisora(beanWriter, randomIdentifier);

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

    private void writeEntidadTransmisora(BeanWriter beanWriter, Date fechaInicioCiclo) {
        EntidadTransmisora entidadTransmisora = new EntidadTransmisora();
        entidadTransmisora.setIndicadorRegistro("0");
        entidadTransmisora.setCodigoEntidadTransmisora(EmbargosConstants.CODIGO_NRBE_COMMERZBANK);
        entidadTransmisora.setFechaInicioCiclo(fechaInicioCiclo);
        beanWriter.write(EmbargosConstants.RECORD_NAME_AEAT_ENTIDADTRANSMISORA, entidadTransmisora);
    }

    private void writeEntidadCredito(BeanWriter beanWriter, EntidadesComunicadora entity) {
        EntidadCredito entidadCredito = new EntidadCredito();
        entidadCredito.setIndicadorRegistro("1");
        entidadCredito.setDelegacionAgenciaReceptora(entity.getIdentificadorEntidad());
        beanWriter.write(EmbargosConstants.RECORD_NAME_AEAT_ENTIDADCREDITO, entidadCredito);
    }

    private void writeLevantamiento(BeanWriter beanWriter, ManualLiftingDTO ordenesCliente) {
        Levantamiento levantamiento = new Levantamiento();
        levantamiento.setIndicadorRegistro("2");

        levantamiento.setNifDeudor(ordenesCliente.getSeizureCase().getNIF());
        levantamiento.setNombreDeudor(ordenesCliente.getSeizureCase().getNameInternal());
        levantamiento.setNumeroDiligenciaEmbargo(ordenesCliente.getSeizureCase().getSeizureNumber());
        /*
        levantamiento.setFechaGeneracionDiligencia(new Date());
        levantamiento.setFechaTraba(new Date());
        levantamiento.setFechaLimiteIngresoImporteTrabado(new Date());
        */

        levantamiento.setImporteTotalAEmbargar(ordenesCliente.getSeizureCase().getRequestedAmount());
        levantamiento.setImporteTotalTrabado(ordenesCliente.getSeizureCase().getSeizedAmount());

        // Se usa el primer slot, aunque no corresponda con el fichero
        levantamiento.setCodigoCuentaCliente1(ordenesCliente.getSeizedBankAccount().getIban().substring(4));
        levantamiento.setCodigoTipoLevantamientoEmbargoCC1(ordenesCliente.getLiftedAmount().compareTo(ordenesCliente.getSeizureCase().getSeizedAmount()) == 0 ? "01" : "02");
        levantamiento.setImporteALevantarCC1(ordenesCliente.getLiftedAmount());

        beanWriter.write(EmbargosConstants.RECORD_NAME_AEAT_LEVANTAMIENTO, levantamiento);
    }

    private void writeFinEntidadCredito(BeanWriter beanWriter) {
        FinEntidadCredito finEntidadCredito = new FinEntidadCredito();
        finEntidadCredito.setIndicadorRegistro("3");
        beanWriter.write(EmbargosConstants.RECORD_NAME_AEAT_FINENTIDADCREDITO, finEntidadCredito);
    }

    private void writeFinEntidadTransmisora(BeanWriter beanWriter) {
        FinEntidadTransmisora finEntidadTransmisora = new FinEntidadTransmisora();
        finEntidadTransmisora.setIndicadorRegistro("8");
        beanWriter.write(EmbargosConstants.RECORD_NAME_AEAT_FINENTIDADTRANSMISORA, finEntidadTransmisora);
    }

    private void writeRegistroControlEntidadTransmisora(BeanWriter beanWriter, String randomIdentifier) {
        RegistroControlEntidadTransmisora registroControlEntidadTransmisora = new RegistroControlEntidadTransmisora();
        registroControlEntidadTransmisora.setIndicadorRegistro("9");
        registroControlEntidadTransmisora.setDelegacionAgenciaReceptora(randomIdentifier.substring(0, 2));
        registroControlEntidadTransmisora.setCodigoEntidadTransmisora(randomIdentifier.substring(2));
        beanWriter.write(EmbargosConstants.RECORD_NAME_AEAT_REGISTROCONTROLENTIDADTRANSMISORA, registroControlEntidadTransmisora);
    }
}
