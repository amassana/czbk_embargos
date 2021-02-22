package es.commerzbank.ice.embargos.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

import es.commerzbank.ice.comun.lib.service.LocationService;
import es.commerzbank.ice.comun.lib.service.OfficeCService;

@Component
public class OfficeUtils {
    @Autowired
    private OfficeCService officeCService;

    @Autowired
    private LocationService locationService;

    public String getLocalidadUsuario(Authentication authentication)
    {
        Long codOffice = Long.parseLong(authentication.getDetails().toString());

        Sucursal office = officeCService.findById(codOffice);
        List<Location> allLocations = locationService.listAllLocation();

        for (Location l : allLocations)
        {
            if (office.getLocalidad().getCodLocalidad() == l.getCodLocation())
                return l.getLocation();
        }

        return "Madrid";
    }
}
