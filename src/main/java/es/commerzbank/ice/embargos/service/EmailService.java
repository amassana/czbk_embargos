package es.commerzbank.ice.embargos.service;

import es.commerzbank.ice.comun.lib.util.ICEException;

public interface EmailService {

	public void sendEmailFileParserError(String fileName, String descException) throws ICEException;
	
	public void sendEmailPetitionReceived(String fileName);
}
