package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.embargos.domain.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.util.List;

public interface CGPJService {
    Page<CGPJPetitionDTO> filter(CGPJFiltersDTO filters, Pageable pageable);

    List<IntegradorRequestStatusDTO> listStatus();

    File informeSEPA(String codPeticion) throws Exception;

    byte[] informePeticion(String codPeticion) throws Exception;

    List<AccountingPendingDTO> accountingPending();

    long contabilizar(List<AccountingPendingDTO> pendientes, String username) throws Exception;

    CGPJReplyDTO reply(List<String> codPeticiones, String username);

    byte[] informePrecontable(List<AccountingPendingDTO> pendientes)
            throws Exception;

    CGPJReplyDTO redeliver(List<String> codPeticiones);
}
