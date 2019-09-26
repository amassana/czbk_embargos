package es.commerzbank.ice.embargos.service;

public interface EmailService {

	public void sendEmailFileParserError(String fileName, String descException);
	
	public void sendEmailPetitionReceived(String fileName);
}
