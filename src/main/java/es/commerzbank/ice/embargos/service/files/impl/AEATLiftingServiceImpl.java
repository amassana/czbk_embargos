package es.commerzbank.ice.embargos.service.files.impl;

import es.commerzbank.ice.comun.lib.service.AccountingNoteService;
import es.commerzbank.ice.comun.lib.util.ICEParserException;
import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.domain.entity.*;
import es.commerzbank.ice.embargos.domain.mapper.AEATMapper;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.formats.aeat.levantamientotrabas.Levantamiento;
import es.commerzbank.ice.embargos.repository.*;
import es.commerzbank.ice.embargos.service.CustomerService;
import es.commerzbank.ice.embargos.service.files.AEATLiftingService;
import es.commerzbank.ice.utils.EmbargosConstants;
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
import java.util.List;

@Service
@Transactional(transactionManager="transactionManager")
public class AEATLiftingServiceImpl
    implements AEATLiftingService
{
    private static final Logger LOG = LoggerFactory.getLogger(AEATLiftingServiceImpl.class);

    @Value("${commerzbank.embargos.beanio.config-path.aeat}")
    String pathFileConfigAEAT;

    //@Value("${commerzbank.embargos.beanio.config-path.aeat}")
    // TODO
    BigDecimal limiteAutomatico = new BigDecimal(50000);
    @Autowired
    FileControlMapper fileControlMapper;

    @Autowired
    FileControlRepository fileControlRepository;
    @Autowired
    LiftingRepository liftingRepository;
    @Autowired
    SeizureRepository seizureRepository;
    @Autowired
    SeizedRepository seizedRepository;
    @Autowired
    LiftingBankAccountRepository liftingBankAccountRepository;
    @Autowired
    CustomerService customerService;
    @Autowired
    AEATMapper aeatMapper;
    @Autowired
    AccountingNoteService accountingNoteService;
    @Override
    public void tratarFicheroLevantamientos(File file) throws IOException, ICEParserException {
        BeanReader beanReader = null;

        try {
            // Inicializar control fichero
            ControlFichero controlFicheroLevantamiento =
                    fileControlMapper.generateControlFichero(file, EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_AEAT);

            fileControlRepository.save(controlFicheroLevantamiento);

            // Inicialiar beanIO parser
            StreamFactory factory = StreamFactory.newInstance();
            factory.loadResource(pathFileConfigAEAT);
            beanReader = factory.createReader(EmbargosConstants.STREAM_NAME_AEAT_LEVANTAMIENTOTRABAS, file);

            Object currentRecord = null;

            while ((currentRecord = beanReader.read()) != null) {
                if (EmbargosConstants.RECORD_NAME_AEAT_ENTIDADTRANSMISORA.equals(beanReader.getRecordName()))
                {
                    ;
                }
                else if (EmbargosConstants.RECORD_NAME_AEAT_ENTIDADCREDITO.equals(beanReader.getRecordName()))
                {
                    ;
                }
                else if (EmbargosConstants.RECORD_NAME_AEAT_LEVANTAMIENTO.equals(beanReader.getRecordName()))
                {
                    Levantamiento levantamientoAEAT = (Levantamiento) currentRecord;

                    List<Embargo> embargos = seizureRepository.findAllByNumeroEmbargo(levantamientoAEAT.getNumeroDiligenciaEmbargo());

                    if (embargos == null || embargos.size() == 0)
                    {
                        LOG.info("No embargo found for "+ levantamientoAEAT.getNumeroDiligenciaEmbargo());
                        // TODO ERROR
                        continue;
                    }

                    Embargo embargo = selectEmbargo(embargos);

                    Traba traba = seizedRepository.getByEmbargo(embargo);

                    if (traba == null)
                    {
                        LOG.error("Levantamiento not found for embargo "+ embargo.getCodEmbargo() +" code "+ levantamientoAEAT.getNumeroDiligenciaEmbargo());
                        continue;
                    }

                    LOG.info("Using traba "+ traba.getCodTraba() +" for embargo "+ embargo.getCodEmbargo() +" code "+ levantamientoAEAT.getNumeroDiligenciaEmbargo());

                    // recuperar account <- razon interna
                    // recuperar cod traba
                    // estado contable?
                    // estado ejecutado?
                    CustomerDTO customerDTO = customerService.findCustomerByNif(levantamientoAEAT.getNifDeudor());

                    LevantamientoTraba levantamiento = aeatMapper.generateLevantamiento(controlFicheroLevantamiento.getCodControlFichero(), levantamientoAEAT, traba, customerDTO);

                    liftingRepository.save(levantamiento);

                    for (CuentaLevantamiento cuentaLevantamiento : levantamiento.getCuentaLevantamientos())
                    {
                        liftingBankAccountRepository.save(cuentaLevantamiento);

                        // TODO divisa?
                        if ("EUR".equals(cuentaLevantamiento.getCodDivisa()) &&
                        limiteAutomatico.compareTo(cuentaLevantamiento.getImporte()) == -1)
                            LOG.info("Skipping automating accounting");
                        else
                        {

                        }

                    }
                }
                else if (EmbargosConstants.RECORD_NAME_AEAT_FINENTIDADCREDITO.equals(beanReader.getRecordName()))
                {
                    ;
                }
                else if (EmbargosConstants.RECORD_NAME_AEAT_FINENTIDADTRANSMISORA.equals(beanReader.getRecordName()))
                {
                    ;
                }
                else if (EmbargosConstants.RECORD_NAME_AEAT_REGISTROCONTROLENTIDADTRANSMISORA.equals(beanReader.getRecordName()))
                {
                    ;
                }
                else
                   LOG.info(beanReader.getRecordName());// throw new Exception("BeanIO - Unexpected record name: "+ beanReader.getRecordName());
            }
        }
        catch (Exception e)
        {
            LOG.error("Error while treating AEAT LEV file", e);
            // TODO error treatment
        }
        finally
        {
            if (beanReader != null)
                beanReader.close();
        }
    }

    /* criterio: el embargo más reciente */
    private Embargo selectEmbargo(List<Embargo> embargos)
    {
        Embargo embargo = null;

        for (Embargo currentEmbargo : embargos)
        {
            if (embargo == null) {
                embargo = currentEmbargo;
                continue;
            }
            if (embargo.getFUltimaModificacion().compareTo(currentEmbargo.getFUltimaModificacion()) == -1)
                embargo = currentEmbargo;
        }

        return embargo;
    }
}
