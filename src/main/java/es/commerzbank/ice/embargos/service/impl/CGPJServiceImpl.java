package es.commerzbank.ice.embargos.service.impl;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import es.commerzbank.ice.comun.lib.typeutils.ICEDateUtils;
import es.commerzbank.ice.comun.lib.util.jasper.ReportHelper;
import es.commerzbank.ice.embargos.config.OracleDataSourceEmbargosConfig;
import es.commerzbank.ice.embargos.domain.dto.AccountingPendingDTO;
import es.commerzbank.ice.embargos.domain.dto.CGPJFiltersDTO;
import es.commerzbank.ice.embargos.domain.dto.CGPJPetitionDTO;
import es.commerzbank.ice.embargos.domain.dto.IntegradorRequestStatusDTO;
import es.commerzbank.ice.embargos.domain.entity.*;
import es.commerzbank.ice.embargos.domain.mapper.PetitionMapper;
import es.commerzbank.ice.embargos.repository.PetitionRepository;
import es.commerzbank.ice.embargos.service.AccountingService;
import es.commerzbank.ice.embargos.service.CGPJService;
import es.commerzbank.ice.embargos.utils.ResourcesUtil;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.*;

import static es.commerzbank.ice.embargos.utils.EmbargosConstants.CGPJ_ESTADO_INTERNO_PENDIENTE_ENVIAR;

@Service
@Transactional(transactionManager="transactionManager")
public class CGPJServiceImpl
        implements CGPJService
{
    private static final Logger logger = LoggerFactory.getLogger(CGPJServiceImpl.class);

    @Autowired
    private PetitionRepository petitionRepository;

    @Autowired
    private PetitionMapper petitionMapper;

    @Autowired
    private es.commerzbank.ice.embargos.repository.CGPJInternalRequestStatusRepository CGPJInternalRequestStatusRepository;

    @Autowired
    private OracleDataSourceEmbargosConfig oracleDataSourceEmbargos;

    @Autowired
    private ReportHelper reportHelper;

    @Autowired
    private AccountingService accountingService;

    @Override
    public Page<CGPJPetitionDTO> filter(CGPJFiltersDTO filters, Pageable pageable)
    {
        Page<Peticion> result = petitionRepository.findAll(filterSpecification(filters), pageable);
        List<CGPJPetitionDTO> list = new ArrayList<>();

        for (Peticion peticion : result)
        {
            CGPJPetitionDTO CGPJPetitionDTO = petitionMapper.toCGPJPetitionDTO(peticion);

            list.add(CGPJPetitionDTO);
        }

        return new PageImpl<>(list, pageable, result.getTotalElements());
    }

    private Specification<Peticion> filterSpecification(CGPJFiltersDTO filters) {
        return new Specification<Peticion>() {

            @Override
            public Predicate toPredicate(Root<Peticion> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                if (filters.getStatuses() != null) {
                    CriteriaBuilder.In<Long> inClause = criteriaBuilder.in(root.join(Peticion_.estadoIntPeticion).get(EstadoIntPeticion_.codEstadoIntPeticion));

                    for (Long status : filters.getStatuses()) {
                        inClause.value(status);
                    }

                    predicates.add(inClause);
                }

                if (filters.getStartDate() != null || filters.getEndDate() != null) {
                    BigDecimal start = ICEDateUtils.instantDateToBigDecimal(filters.getStartDate());
                    BigDecimal end = ICEDateUtils.instantDateToBigDecimal(filters.getEndDate());

                    if (start != null && end != null) {
                        predicates.add(criteriaBuilder.between(
                                root.join(Peticion_.controlFichero).get(ControlFichero_.FECHA_CREACION), start, end));
                    } else if (start != null) {
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.join(Peticion_.controlFichero).get(ControlFichero_.FECHA_CREACION),start));
                    } else {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.join(Peticion_.controlFichero).get(ControlFichero_.FECHA_CREACION), end));
                    }
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    @Override
    public List<IntegradorRequestStatusDTO> listStatus()
    {
        List<EstadoIntPeticion> statuses = CGPJInternalRequestStatusRepository.findAll();
        List<IntegradorRequestStatusDTO> list = new ArrayList<>();

        for (EstadoIntPeticion status : statuses)
        {
            IntegradorRequestStatusDTO integradorRequestStatusDTO = petitionMapper.toEstadoIntPeticion(status);

            list.add(integradorRequestStatusDTO);
        }

        return list;
    }

    @Override
    public File informeSEPA(String codPeticion) throws Exception {
        Optional<Peticion> optPeticion = petitionRepository.findById(codPeticion);

        if (!optPeticion.isPresent())
            throw new Exception("CodPeticion " + codPeticion + " no encontrado");

        Peticion peticion = optPeticion.get();

        File temporaryFile = reportHelper.getTemporaryFile("cgpj-sepa-"+ codPeticion, ReportHelper.PDF_EXTENSION);
        PdfDocument outDoc = new PdfDocument(new PdfWriter(temporaryFile));
        int pageCount = 1;

        try {
            for (SolicitudesEjecucion solicitudEjecucion : peticion.getSolicitudesEjecucions()) {
                JasperPrint filledReport = imprimirSEPASolicitud(solicitudEjecucion);

                if (filledReport != null) {
                    reportHelper.dumpReport(outDoc, filledReport, pageCount);
                    pageCount++;
                }
            }

            outDoc.close();
        }
        catch (Exception e) {
            try { outDoc.close(); } catch (Exception e2) {;}
            throw e;
        }

        return temporaryFile;
    }

    @Override
    public List<AccountingPendingDTO> accountingPending() {
        return petitionRepository.findAccountingPending();
    }

    @Override
    public long contabilizar(List<AccountingPendingDTO> pendientes, String username) throws Exception {
        return accountingService.CGPJContabilizar(pendientes, username);
    }

    @Override
    public boolean reply(List<String> codPeticiones, String username) {
        boolean allReplied = true;

        EstadoIntPeticion estadoInterno = new EstadoIntPeticion();
        estadoInterno.setCodEstadoIntPeticion(CGPJ_ESTADO_INTERNO_PENDIENTE_ENVIAR);

        for (String codPeticion : codPeticiones) {
            Optional<Peticion> peticionOpt = petitionRepository.findById(codPeticion);

            if (!peticionOpt.isPresent()) {
                logger.error("Peticion de respuesta "+ codPeticion +" no encontrado. skipping...");
                allReplied = false;
                continue;
            }

            Peticion peticion = peticionOpt.get();

            peticion.setEstadoIntPeticion(estadoInterno);

            petitionRepository.save(peticion);
        }

        return allReplied;
    }

    private JasperPrint imprimirSEPASolicitud(SolicitudesEjecucion solicitudEjecucion)
            throws Exception
    {
        HashMap<String, Object> parameters = new HashMap<>();

        try (Connection conn_embargos = oracleDataSourceEmbargos.getEmbargosConnection()) {

            Resource transOrder = ResourcesUtil.getFromJasperFolder("paymentLetterCGPJ.jasper");
            Resource imageLogo = ResourcesUtil.getImageLogoCommerceResource();

            File logoFile = imageLogo.getFile();

            parameters.put("IMAGE_PARAM", logoFile.toString());
            parameters.put("COD_SOLICITUD_EJECUCION", solicitudEjecucion.getCodSolicitudEjecucion());

            parameters.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));

            InputStream isOrdenTransferencia = transOrder.getInputStream();
            return JasperFillManager.fillReport(isOrdenTransferencia, parameters, conn_embargos);
        } catch (Exception e) {
            throw new Exception("DB exception while generating the report", e);
        }
    }
}
