package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;

import java.util.List;

public interface CGPJImportService {
    List<ControlFichero> listPending();

    void importCGPJ(ControlFichero controlFichero) throws Exception;
}
