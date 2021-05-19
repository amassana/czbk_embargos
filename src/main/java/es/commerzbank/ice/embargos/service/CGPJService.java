package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.embargos.domain.dto.CGPJPetitionDTO;
import es.commerzbank.ice.embargos.domain.dto.CGPJFiltersDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CGPJService {
    Page<CGPJPetitionDTO> filter(CGPJFiltersDTO filters, Pageable pageable);
}
