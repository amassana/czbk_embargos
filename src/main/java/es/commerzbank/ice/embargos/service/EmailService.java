package es.commerzbank.ice.embargos.service;

import java.util.List;

import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.domain.entity.ErrorTraba;

public interface EmailService {

	public void sendEmailFileParserError(String fileName, String descException) throws ICEException;
	
	public void sendEmailPetitionReceived(String fileName);

	public void sendEmailFileError(List <ErrorTraba> errores, String nombreFichero, String rutaFichero) throws ICEException;
	
	public void sendEmailFileResult(String nombreFichero) throws ICEException;
}
