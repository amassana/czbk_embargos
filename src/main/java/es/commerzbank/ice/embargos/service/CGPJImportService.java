package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.Peticion;

import java.util.List;

public interface CGPJImportService {
    List<ControlFichero> listPending();

    void importCGPJ(ControlFichero controlFichero, Peticion peticion) throws Exception;
}
