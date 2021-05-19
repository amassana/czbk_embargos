package es.commerzbank.ice.embargos.service.impl;


import es.commerzbank.ice.comun.lib.typeutils.DateUtils;
import es.commerzbank.ice.embargos.domain.dto.CGPJFiltersDTO;
import es.commerzbank.ice.embargos.domain.dto.CGPJPetitionDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero_;
import es.commerzbank.ice.embargos.domain.entity.EstadoIntPeticion_;
import es.commerzbank.ice.embargos.domain.entity.Peticion;
import es.commerzbank.ice.embargos.domain.entity.Peticion_;
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
import java.time.LocalDate;
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

                if (filters.getStatus() != null) {
                    predicates.add(criteriaBuilder.equal(root.join(Peticion_.estadoIntPeticion).get(EstadoIntPeticion_.codEstadoIntPeticion), filters.getStatus().getCode()));
                }

                if (filters.getStartDate() != null || filters.getEndDate() != null) {
                    LocalDate start = DateUtils.instanttoLocalDate(filters.getStartDate());
                    LocalDate end = DateUtils.instanttoLocalDate(filters.getEndDate());

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
}
