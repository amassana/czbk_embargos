package es.commerzbank.ice.embargos.service.impl;


import es.commerzbank.ice.comun.lib.typeutils.ICEDateUtils;
import es.commerzbank.ice.embargos.domain.dto.CGPJFiltersDTO;
import es.commerzbank.ice.embargos.domain.dto.CGPJPetitionDTO;
import es.commerzbank.ice.embargos.domain.dto.IntegradorRequestStatusDTO;
import es.commerzbank.ice.embargos.domain.entity.*;
import es.commerzbank.ice.embargos.domain.mapper.PetitionMapper;
import es.commerzbank.ice.embargos.repository.PetitionRepository;
import es.commerzbank.ice.embargos.service.CGPJService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(transactionManager="transactionManager")
public class CGPJServiceImpl
        implements CGPJService
{
    @Autowired
    private PetitionRepository petitionRepository;

    @Autowired
    private PetitionMapper petitionMapper;

    @Autowired
    private es.commerzbank.ice.embargos.repository.CGPJInternalRequestStatusRepository CGPJInternalRequestStatusRepository;

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
}