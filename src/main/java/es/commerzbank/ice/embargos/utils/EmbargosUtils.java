package es.commerzbank.ice.embargos.utils;

import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.datawarehouse.domain.dto.PersonType;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import es.commerzbank.ice.embargos.domain.entity.EntidadesOrdenante;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacion;
import es.commerzbank.ice.embargos.formats.common.Levantamiento;
import es.commerzbank.ice.embargos.repository.SeizureRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static es.commerzbank.ice.embargos.utils.ICEDateUtils.FORMAT_yyyyMMddHHmmss;

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
		
		result.append(actualLocalDate).append(iban.substring(14, 18));
		
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

	/*
		criterio: el embargo más reciente tal que
		tenga la fecha de incorporación más reciente
		con el mismo numero de embargo
		misma entidad presentadora
		mismo importe a embargar
		mismo importe trabado
	*/
	public static Embargo selectEmbargo(SeizureRepository seizureRepository, EntidadesOrdenante entidadOrdenante, Levantamiento levantamiento)
	{
		BigDecimal fechaDesde = ICEDateUtils.localdateToBigDecimal(LocalDate.now().minusDays(entidadOrdenante.getEntidadesComunicadora().getDiasRespuestaF3().intValue() + entidadOrdenante.getEntidadesComunicadora().getDiasRespuestaF6().intValue() + 1), FORMAT_yyyyMMddHHmmss);

		List<Embargo> embargos = seizureRepository.findAllByNumeroEmbargo(levantamiento.getNumeroEmbargo(), fechaDesde);

		Embargo embargo = null;

		for (Embargo currentEmbargo : embargos)
		{
			if (currentEmbargo.getControlFichero().getEntidadesComunicadora().getCodEntidadPresentadora() ==
				entidadOrdenante.getEntidadesComunicadora().getCodEntidadPresentadora()
			&&
					(currentEmbargo.getImporte().compareTo(levantamiento.getImporteTotalAEmbargar()) == 0)
			&&
					(currentEmbargo.getTrabas().get(0).getImporteTrabado().compareTo(levantamiento.getImporteTotalTrabado())) == 0)
			{
				return currentEmbargo;
			}
		}

		return embargo;
	}
}
