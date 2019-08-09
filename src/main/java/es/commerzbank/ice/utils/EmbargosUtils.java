package es.commerzbank.ice.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import es.commerzbank.ice.embargos.domain.entity.PeticionInformacion;

public class EmbargosUtils {

	/**
	 * Setea en peticionInformacion el calculo las claves de seguridad. 
	 * 
	 * @param peticionInformacion
	 */
	public static void calculateClavesSeguridadInPeticionInformacion(PeticionInformacion peticionInformacion) {
	
		peticionInformacion.setClaveSeguridad1(generateClaveSeguridad(peticionInformacion.getIban1()));
		peticionInformacion.setClaveSeguridad2(generateClaveSeguridad(peticionInformacion.getIban2()));
		peticionInformacion.setClaveSeguridad3(generateClaveSeguridad(peticionInformacion.getIban3()));
		peticionInformacion.setClaveSeguridad4(generateClaveSeguridad(peticionInformacion.getIban4()));
		peticionInformacion.setClaveSeguridad5(generateClaveSeguridad(peticionInformacion.getIban5()));
		peticionInformacion.setClaveSeguridad6(generateClaveSeguridad(peticionInformacion.getIban6()));
	}
	
	/**
	 * Genera las claves de seguridad de las cuentas
	 * 
	 * @param iban
	 * @return
	 */
	private static String generateClaveSeguridad(String iban) {

		//La clave consiste en la fecha del proceso de generacion en formato aaaammdd, seguido
		//de los 4 primeros digitos de la cuenta y rellenando con ceros el resto del campo:
		
		if(iban==null || iban.length() < 4) {
			return null;
		}
		
		StringBuilder result = new StringBuilder();
	
		String actualLocalDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		
		result.append(actualLocalDate).append(iban.substring(0, 4));
		
		return result.toString();
		
	}
}
