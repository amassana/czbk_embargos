package es.commerzbank.ice.embargos.scheduled;

import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import es.commerzbank.ice.embargos.domain.entity.LevantamientoTraba;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.LiftingRepository;
import es.commerzbank.ice.embargos.repository.SeizedRepository;
import es.commerzbank.ice.embargos.repository.SeizureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public class Norma63Fase6
{
    @Autowired
    SeizureRepository seizureRepository;

    @Autowired
    SeizedRepository seizedRepository;

    @Autowired
    LiftingRepository liftingRepository;

    @Autowired
    FileControlRepository fileControlRepository;

    /* to cronify
    @Scheduled("")
    public void doFase6()
    {
        // get files type XXX which expire today
        for each file
        process()
    }
    */

    public void codControl(Long codControlFichero) throws ICEException {
        ControlFichero fichero = fileControlRepository.getOne(codControlFichero);

        // type N63 F3 is assumed - value 8

        List<Embargo> embargos = seizureRepository.findAllByControlFichero(fichero);

        if (embargos == null)
            throw new ICEException("", "No seizures found for code "+ codControlFichero);

        for (Embargo embargo : embargos)
        {
            Traba traba = seizedRepository.getByEmbargo(embargo);
            //LevantamientoTraba levantamientoTraba = liftingRepository.
        }
    }
}
