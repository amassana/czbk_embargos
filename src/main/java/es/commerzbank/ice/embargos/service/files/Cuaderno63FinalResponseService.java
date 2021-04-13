package es.commerzbank.ice.embargos.service.files;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.FicheroFinal;

public interface Cuaderno63FinalResponseService {
    void tramitarFicheroInformacion(ControlFichero ficheroFase3, FicheroFinal finalFile) throws Exception;
}
