package es.commerzbank.ice.embargos.service.impl;

import es.commerzbank.ice.comun.lib.domain.dto.ICEEmail;
import es.commerzbank.ice.comun.lib.service.ClientEmailService;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.comun.lib.util.ValueConstants;
import es.commerzbank.ice.embargos.domain.dto.CGPJPetitionDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.ErrorTraba;
import es.commerzbank.ice.embargos.service.EmailService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {
	
	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
	
	@Autowired
	private ClientEmailService clientEmailService;
	
	@Autowired
	private GeneralParametersService generalParametersService;
	
	@Override
	public void sendEmailFileError(List<ErrorTraba> errores, String nombreFichero, String rutaFichero) throws ICEException {

		logger.info("EmailServiceImpl - sendEmailFileError - start");

		if (!generalParametersService.loadBooleanParameter(ValueConstants.EMAIL_SMTP_ENABLED, true))
			return;

		ICEEmail iceEmail = new ICEEmail();

		List<String> recipientsTo = new ArrayList<>(); 
		
		String emailAddressesTo = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_EMAIL_TO);		
		recipientsTo.add(emailAddressesTo);
		iceEmail.setRecipientsTo(recipientsTo);
		
		String emailAddressFrom = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_EMAIL_FROM);
		iceEmail.setEmailAddressFrom(emailAddressFrom);

		iceEmail.setSubject("Incoming error file " + nombreFichero);

		List<String> paragraphTextList = new ArrayList<>();

		paragraphTextList.add("Received error file ("+ nombreFichero +"). The file is attached.");

		for (ErrorTraba errorTraba : errores) {
			if (errorTraba!=null && errorTraba.getError()!=null)
				paragraphTextList.add(errorTraba.getError().getCodError() + " " + errorTraba.getError().getDescripcionError());
		}
			
		iceEmail.setParagraphTextList(paragraphTextList);

		iceEmail.setFooterText(EmbargosConstants.EMAIL_DEFAULT_FOOTER_TEXT);

		iceEmail.addAttachment(nombreFichero, rutaFichero);

		try {
			clientEmailService.sendEmailWithAttachment(iceEmail);
		} catch (Exception e) {
			logger.error("ERROR al enviar el email de 'Fichero de errores recibido AEAT'", e);
		}

		logger.info("EmailServiceImpl - sendEmailFileError - end");
	}
	
	@Override
	public void sendEmailFileParserError(String fileName, String descException) throws ICEException {

		if (!generalParametersService.loadBooleanParameter(ValueConstants.EMAIL_SMTP_ENABLED, true))
			return;

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
	public void sendEmailPetitionReceived(String fileName) throws ICEException {

		logger.info("EmailServiceImpl - sendEmailPetitionReceived - start");
		
		if (!generalParametersService.loadBooleanParameter(ValueConstants.EMAIL_SMTP_ENABLED, true))
			return;

		ICEEmail iceEmail = new ICEEmail();
		
		List<String> recipientsTo = new ArrayList<>(); 
		
		String emailAddressesTo = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_EMAIL_TO);		
		recipientsTo.add(emailAddressesTo);
		iceEmail.setRecipientsTo(recipientsTo);
		
		String emailAddressFrom = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_EMAIL_FROM);
		iceEmail.setEmailAddressFrom(emailAddressFrom);
		
		iceEmail.setSubject("Incoming file: " + fileName);
		
		List<String> paragraphTextList = new ArrayList<>();
		
		paragraphTextList.add("Se ha recibido y procesado correctamente el siguiente fichero: " + fileName);
		
		iceEmail.setParagraphTextList(paragraphTextList);
		
		iceEmail.setFooterText(EmbargosConstants.EMAIL_DEFAULT_FOOTER_TEXT);
		
		try {
			clientEmailService.sendEmailWithAttachment(iceEmail);
		} catch (Exception e) {
			logger.error("ERROR al enviar el email de 'Fichero recibido: " + fileName + "'", e);
		}
		
		logger.info("EmailServiceImpl - sendEmailPetitionReceived - end");
	}

	@Override
	public void sendEmailFileResult(String nombreFichero) throws ICEException {
		
		logger.info("EmailServiceImpl - sendEmailFileResult - start");

		if (!generalParametersService.loadBooleanParameter(ValueConstants.EMAIL_SMTP_ENABLED, true))
			return;

		ICEEmail iceEmail = new ICEEmail();

		List<String> recipientsTo = new ArrayList<>(); 
		
		String emailAddressesTo = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_EMAIL_TO);		
		recipientsTo.add(emailAddressesTo);
		iceEmail.setRecipientsTo(recipientsTo);
		
		String emailAddressFrom = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_EMAIL_FROM);
		iceEmail.setEmailAddressFrom(emailAddressFrom);

		iceEmail.setSubject("Incoming result file " + nombreFichero);

		List<String> paragraphTextList = new ArrayList<>();

		paragraphTextList.add("Received result file ("+ nombreFichero +").");
			
		iceEmail.setParagraphTextList(paragraphTextList);

		iceEmail.setFooterText(EmbargosConstants.EMAIL_DEFAULT_FOOTER_TEXT);

		try {
			clientEmailService.sendEmailWithAttachment(iceEmail);
		} catch (Exception e) {
			logger.error("ERROR al enviar el email de 'Fichero de resultado recibido AEAT'", e);
		}

		logger.info("EmailServiceImpl - sendEmailFileResult - end");
	}

	@Override
	public void sendEmailUnreadFiles(List<ControlFichero> listFicheros) throws ICEException {
		
		logger.info("EmailServiceImpl - sendEmailUnreadFiles - start");

		if (!generalParametersService.loadBooleanParameter(ValueConstants.EMAIL_SMTP_ENABLED, true))
			return;

		ICEEmail iceEmail = new ICEEmail();

		List<String> recipientsTo = new ArrayList<>(); 
		
		String emailAddressesTo = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_EMAIL_TO);		
		recipientsTo.add(emailAddressesTo);
		iceEmail.setRecipientsTo(recipientsTo);
		
		String emailAddressFrom = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_EMAIL_FROM);
		iceEmail.setEmailAddressFrom(emailAddressFrom);

		iceEmail.setSubject("Outbox unread files");

		List<String> paragraphTextList = new ArrayList<>();

		paragraphTextList.add("Outbox unread files:");

		for (ControlFichero controlFichero : listFicheros) {
			if (controlFichero!=null)
				paragraphTextList.add(controlFichero.getCodControlFichero() + " " + controlFichero.getNombreFichero());
		}
			
		iceEmail.setParagraphTextList(paragraphTextList);

		iceEmail.setFooterText(EmbargosConstants.EMAIL_DEFAULT_FOOTER_TEXT);

		try {
			clientEmailService.sendEmailWithAttachment(iceEmail);
		} catch (Exception e) {
			logger.error("ERROR al enviar el email de 'Fichero de errores recibido AEAT'", e);
		}

		logger.info("EmailServiceImpl - sendEmailUnreadFiles - end");
		
	}

	@Override
	public void sendCGPJPending(List<CGPJPetitionDTO> content, String emailAddressesTo, String emailAddressFrom, String emailDefaultFooterText)
			throws ICEException
	{
		if (content == null || content.size() == 0)
			return;

		logger.info("ClientEmailServiceImpl - sendCGPJPending - start");

		if (!generalParametersService.loadBooleanParameter(ValueConstants.EMAIL_SMTP_ENABLED, true))
			return;

		ICEEmail iceEmail = new ICEEmail();

		List<String> recipientsTo = new ArrayList<>();

		recipientsTo.add(emailAddressesTo);
		iceEmail.setRecipientsTo(recipientsTo);

		iceEmail.setEmailAddressFrom(emailAddressFrom);

		iceEmail.setSubject("Peticiones CGPJ Pendientes");

		List<String> paragraphTextList = new ArrayList<>();

		if (content.size() == 1)
			paragraphTextList.add("Hay 1 petición del consejo pendiente de respuesta");
		else
			paragraphTextList.add("Hay "+ content.size() +" peticiones del consejo pendientes de respuesta");

		iceEmail.setParagraphTextList(paragraphTextList);

		iceEmail.setFooterText(emailDefaultFooterText);

		try {
			clientEmailService.sendEmailWithAttachment(iceEmail);
		} catch (Exception e) {
			logger.error("ERROR al enviar el email de 'Peticiones del Consejo pendientes'", e);
		}

		logger.info("ClientEmailServiceImpl - sendCGPJPending - end");

	}
}
