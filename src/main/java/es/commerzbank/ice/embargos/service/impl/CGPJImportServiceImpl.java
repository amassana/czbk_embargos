package es.commerzbank.ice.embargos.service.impl;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlfichero;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.service.CGPJImportService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CGPJImportServiceImpl
    implements CGPJImportService
{
    @Autowired
    private FileControlRepository fileControlRepository;

    @Override
    public List<ControlFichero> listPending() {
        return fileControlRepository.findPendingCGPJ();
    }

    @Override
    @Transactional(transactionManager="transactionManager")
    public void importCGPJ(ControlFichero controlFichero)
    {
        EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero(
                    EmbargosConstants.COD_ESTADO_CTRLFICHERO_CGPJ_IMPORTADO,
                    EmbargosConstants.COD_TIPO_FICHERO_PETICION_CGPJ);

        controlFichero.setEstadoCtrlfichero(estadoCtrlfichero);

        fileControlRepository.saveAndFlush(controlFichero);
    }
}
