package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.comun.lib.domain.entity.Tarea;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.FicheroFinal;

public interface FinalResponseGenerationService
{
    FicheroFinal calcFinalResult(ControlFichero ficheroFase3, Tarea tarea, String user) throws Exception;
}
