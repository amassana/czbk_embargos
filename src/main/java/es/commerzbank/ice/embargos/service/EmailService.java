package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.domain.dto.CGPJPetitionDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.ErrorTraba;

import java.util.List;

public interface EmailService {

	void sendEmailFileParserError(ControlFichero controlFichero, String descException) throws ICEException;
	
	void sendEmailPetitionReceived(ControlFichero controlFichero) throws ICEException;

	void sendEmailFileError(List <ErrorTraba> errores, String nombreFichero, String rutaFichero) throws ICEException;
	
	void sendEmailUnreadFiles(List<ControlFichero> listFicheros) throws ICEException;
	
	void sendEmailFileResult(ControlFichero controlFichero) throws ICEException;

    void sendCGPJPending(List<CGPJPetitionDTO> content, String emailAddressesTo, String emailAddressFrom, String emailDefaultFooterText)
            throws ICEException;
}
