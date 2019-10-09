package es.commerzbank.ice.embargos.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.commerzbank.ice.comun.lib.domain.dto.ICEEmail;
import es.commerzbank.ice.comun.lib.service.ClientEmailService;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.service.EmailService;
import es.commerzbank.ice.utils.EmbargosConstants;

@Service
public class EmailServiceImpl implements EmailService {
	
	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
	
	@Autowired
	private ClientEmailService clientEmailService;
	
	@Autowired
	private GeneralParametersService generalParametersService;
	
	@Override
	public void sendEmailFileParserError(String fileName, String descException) throws ICEException {
		
		ICEEmail iceEmail = new ICEEmail();
		
		List<String> recipientsTo = new ArrayList<>(); 
		
		String emailAddressesTo = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_EMAIL_TO);		
		recipientsTo.add(emailAddressesTo);
		iceEmail.setRecipientsTo(recipientsTo);
		
		String emailAddressFrom = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_EMAIL_FROM);
		iceEmail.setEmailAddressFrom(emailAddressFrom);
		
		iceEmail.setSubject("Error processing the file " + fileName);
		
		List<String> paragraphTextList = new ArrayList<>();
		String paragraph1 = "The following error occurred while processing the file " + fileName + ":";

		paragraphTextList.add(paragraph1);
		paragraphTextList.add(descException);
		
		iceEmail.setParagraphTextList(paragraphTextList);
		
		iceEmail.setFooterText(EmbargosConstants.EMAIL_DEFAULT_FOOTER_TEXT);
		
		try {
		
			clientEmailService.sendEmailWithAttachment(iceEmail);
		
		} catch (Exception e) {
			
			logger.error("ERROR al enviar el email de 'Error al procesar el fichero: " + fileName + "'", e);
		}
	}

	@Override
	public void sendEmailPetitionReceived(String fileName) {
		
		ICEEmail iceEmail = new ICEEmail();
		
		List<String> recipientsTo = new ArrayList<>(); 
		
		recipientsTo.add("commerzbank.alten@google.com");
		
		iceEmail.setRecipientsTo(recipientsTo);
		iceEmail.setSubject("Fichero de peticiones recibido: " + fileName);
		
		List<String> paragraphTextList = new ArrayList<>();
		String paragraph1 = "Se ha recibido y procesado correctamente el siguiente fichero de peticiones:" + fileName + ":";

		paragraphTextList.add(paragraph1);
		
		iceEmail.setParagraphTextList(paragraphTextList);
		
		iceEmail.setFooterText(EmbargosConstants.EMAIL_DEFAULT_FOOTER_TEXT);
		
		try {
		
			clientEmailService.sendEmailWithAttachment(iceEmail);
		
		} catch (Exception e) {
			
			logger.error("ERROR al enviar el email de 'Fichero de peticiones recibido: " + fileName + "'", e);
		}
	}
}
