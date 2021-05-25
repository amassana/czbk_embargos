package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.embargos.domain.dto.AccountingPendingDTO;
import es.commerzbank.ice.embargos.domain.dto.CGPJFiltersDTO;
import es.commerzbank.ice.embargos.domain.dto.CGPJPetitionDTO;
import es.commerzbank.ice.embargos.domain.dto.IntegradorRequestStatusDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.util.List;

public interface CGPJService {
    Page<CGPJPetitionDTO> filter(CGPJFiltersDTO filters, Pageable pageable);

    List<IntegradorRequestStatusDTO> listStatus();

    File informeSEPA(String codPeticion) throws Exception;

    List<AccountingPendingDTO> accountingPending();
}
