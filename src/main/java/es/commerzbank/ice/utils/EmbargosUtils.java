package es.commerzbank.ice.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.datawarehouse.domain.dto.PersonType;
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
	public static String generateClaveSeguridad(String iban) {

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
	
	public static String determineRazonSocialInternaFromCustomer(CustomerDTO customerDTO) {
		
		String razonSocial = null;
		
		if (customerDTO != null) {
		
			PersonType personType = (PersonType) customerDTO.getPersonType();
			
			if (personType!=null && EmbargosConstants.PERSON_TYPE_ID_FISICA.equals(personType.getId())) {
				
				razonSocial = customerDTO.getFirstName() 
						+ (customerDTO.getMiddleName()!=null ? 
								EmbargosConstants.SEPARADOR_ESPACIO + customerDTO.getMiddleName() + EmbargosConstants.SEPARADOR_ESPACIO :
								EmbargosConstants.SEPARADOR_ESPACIO)
						+ customerDTO.getLastName();
			}
			
			else if (personType!=null && EmbargosConstants.PERSON_TYPE_ID_JURIDICA.equals(personType.getId())) {
				
				razonSocial = customerDTO.getName();
			}
			
		}
		return razonSocial;
	}
		
	public static String determineFileFormatByTipoFichero(long codTipoFichero) {
		
		String fileFormat = null;
		
		if (codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_PETICION_INFORMACION_NORMA63
		 || codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_ENVIO_INFORMACION_NORMA63
		 || codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_NORMA63
		 || codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_TRABAS_NORMA63
		 || codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_NORMA63
		 || codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_COM_RESULTADO_FINAL_NORMA63
		 ) {
		
			fileFormat = EmbargosConstants.FILE_FORMAT_NORMA63;
			
		} else if (codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_AEAT
				|| codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_TRABAS_AEAT
				|| codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_AEAT
				|| codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_ERRORES_TRABAS_ENVIADAS_AEAT
		) {
		
			fileFormat = EmbargosConstants.FILE_FORMAT_AEAT;
		
		} else if (codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_PETICION_CGPJ
				|| codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_RESPUESTA_CGPJ){

			fileFormat = EmbargosConstants.FILE_FORMAT_CGPJ;
		}
			
		else {}
			
		return fileFormat;	
	}
}
