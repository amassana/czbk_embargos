package es.commerzbank.ice.embargos.domain.mapper;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.dto.FileControlDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlfichero;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlficheroPK;
import es.commerzbank.ice.embargos.domain.entity.TipoFichero;
import es.commerzbank.ice.utils.EmbargosConstants;
import es.commerzbank.ice.utils.EmbargosUtils;
import es.commerzbank.ice.utils.ICEDateUtils;

@Mapper(componentModel="spring")
public abstract class FileControlMapper {
	
	public ControlFichero generateControlFichero(File file, Long codTipoFichero, String originalName) throws IOException{
		
        ControlFichero controlFichero = new ControlFichero();

        TipoFichero tipoFichero = new TipoFichero(); 
        tipoFichero.setCodTipoFichero(codTipoFichero);
        
        //TODO debe permitir NULL:
        //Se inicializa con valor a 1 (codigo de Entidad Comunicadora tiene que existir), no puede ser null:
        EntidadesComunicadora entidadesComunicadora = new EntidadesComunicadora();
        entidadesComunicadora.setCodEntidadPresentadora(1);
        controlFichero.setEntidadesComunicadora(entidadesComunicadora);
        
        //Guardar registro del control del fichero de Peticion:
        controlFichero.setTipoFichero(tipoFichero);
        controlFichero.setNombreFichero(originalName);
        controlFichero.setRutaFichero(file.getCanonicalPath());
        //Descripcion por defecto:
        controlFichero.setDescripcion(EmbargosConstants.CONTROL_FICHERO_DESCRIPCION_DEFAULT);
        
        //Calculo del CRC del fichero:
        if (file.exists()) {
        	controlFichero.setNumCrc(Long.toString(FileUtils.checksumCRC32(file)));
        }
                
        //Fecha de incorporacion: fecha actual
        BigDecimal actualDate = ICEDateUtils.dateToBigDecimal(new Date(),ICEDateUtils.FORMAT_yyyyMMddHHmmss);
        controlFichero.setFechaIncorporacion(actualDate);
        
        //Indicadores y flags:
        String fileFormat = EmbargosUtils.determineFileFormatByTipoFichero(codTipoFichero);
        controlFichero.setInd6301(fileFormat!=null && fileFormat.equals(EmbargosConstants.FILE_FORMAT_NORMA63) ? EmbargosConstants.IND_FLAG_SI : EmbargosConstants.IND_FLAG_NO);
        controlFichero.setIndCgpj(fileFormat!=null && fileFormat.equals(EmbargosConstants.FILE_FORMAT_CGPJ) ? EmbargosConstants.IND_FLAG_SI : EmbargosConstants.IND_FLAG_NO);
        controlFichero.setIndProcesado(EmbargosConstants.IND_FLAG_NO);
        
        //Iso moneda:
        controlFichero.setIsoMoneda(EmbargosConstants.ISO_MONEDA_EUR);
        
        //ESTADO DEL FICHERO: calcular el estado inicial del fichero dependiendo del tipo de fichero:
        EstadoCtrlfichero estadoCtrlfichero = determineInitialEstadoCtrlFicheroByCodTipoFichero(codTipoFichero);
        controlFichero.setEstadoCtrlfichero(estadoCtrlfichero);
        
        //Usuario y fecha ultima modificacion:
        controlFichero.setUsuarioUltModificacion(EmbargosConstants.USER_AUTOMATICO);
        controlFichero.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
        
        return controlFichero;
	}
	
	/**
	 * Determina el estado inicial del fichero dependiendo del tipo de fichero.
	 * 
	 * @param codTipoFichero
	 * @return
	 */
	private EstadoCtrlfichero determineInitialEstadoCtrlFicheroByCodTipoFichero(long codTipoFichero) {
			
		long codEstado = 0;
		
		if (codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_PETICION_INFORMACION_NORMA63) {

			codEstado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_INFORMACION_NORMA63_LOADING;
			
		} else if (codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_ENVIO_INFORMACION_NORMA63) {
		
			codEstado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_ENVIO_INFORMACION_NORMA63_GENERATING;

		} else if (codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_NORMA63) {	

			codEstado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_LOADING;

		} else if (codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_AEAT) {	
			
			codEstado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_LOADING;
			
		} else if (codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_TRABAS_NORMA63) {		
			
			codEstado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_NORMA63_GENERATING;
		
		} else if (codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_TRABAS_AEAT) {

			codEstado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_AEAT_GENERATING;

		}  else if (codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_AEAT) {

			codEstado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_LOADING;
		}
		else if (codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_NORMA63) {

			codEstado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_LOADING;
		}
		else if (codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_COM_RESULTADO_FINAL_NORMA63) {

			codEstado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_FINAL_GENERANDO;
		}
		else {
			//Estado inicial por defecto:
			codEstado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_INITIAL_STATUS_DEFAULT;
		}
		
        EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero();
        EstadoCtrlficheroPK estadoCtrlficheroPK = new EstadoCtrlficheroPK();
        estadoCtrlficheroPK.setCodEstado(codEstado);
        estadoCtrlficheroPK.setCodTipoFichero(codTipoFichero);
        estadoCtrlfichero.setId(estadoCtrlficheroPK);
        
        return estadoCtrlfichero;
		
	}
	
	@Mappings({
		@Mapping(source = "controlFichero.codControlFichero", target = "code"),
		@Mapping(source = "controlFichero.nombreFichero", target = "fileName"),
		@Mapping(source = "controlFichero.estadoCtrlfichero.id.codEstado", target = "status.code"),
		@Mapping(source = "controlFichero.estadoCtrlfichero.descripcion", target = "status.description"),
		@Mapping(source = "controlFichero.controlFicheroRespuesta.nombreFichero", target = "fileTarget"),
		@Mapping(source = "controlFichero.tipoFichero.codTipoFichero", target = "codeFileType"),
		@Mapping(source = "controlFichero.controlFicheroOrigen.codControlFichero", target = "codeFileOrigin"),
		@Mapping(source = "controlFichero.controlFicheroRespuesta.codControlFichero", target = "codeFileResponse"),
		@Mapping(source = "controlFichero.usuarioUltModificacion", target = "modifiedUser")
	})
	public abstract FileControlDTO toFileControlDTO (ControlFichero controlFichero);
	
	
	@AfterMapping
	protected void setFileControlDTOAfterMapping(ControlFichero controlFichero, @MappingTarget FileControlDTO fileControlDTO) {
		
		//Delivery date:
		if (controlFichero.getFechaMaximaRespuesta()!=null) {
			fileControlDTO.setDeliveryDate(ICEDateUtils.bigDecimalToDate(controlFichero.getFechaMaximaRespuesta(), ICEDateUtils.FORMAT_yyyyMMdd));
		}
		
		//Indicador de procesado (se pasa de String a Boolean):
		boolean isProcessed = controlFichero.getIndProcesado()!=null && EmbargosConstants.IND_FLAG_SI.equals(controlFichero.getIndProcesado());
		fileControlDTO.setIsProcessed(isProcessed);
		
		//Indicador para saber si el fichero es de Cuaderno63 y de TGSS:
		if (controlFichero.getEntidadesComunicadora() != null) {
			
			if (controlFichero.getEntidadesComunicadora().getIndNorma63() != null) {
				boolean isCuaderno63 = EmbargosConstants.IND_FLAG_SI.equals(controlFichero.getEntidadesComunicadora().getIndNorma63());
				fileControlDTO.setIsCuaderno63(isCuaderno63);
			}
			
			if (controlFichero.getEntidadesComunicadora().getPrefijoFicheros() != null) {
				boolean isTGSS = EmbargosConstants.PREFIJO_FICHERO_TGSS.equals(controlFichero.getEntidadesComunicadora().getPrefijoFicheros());
				fileControlDTO.setIsTGSS(isTGSS);
			}
		}
		
		//Fecha de ultima modificacion:
		if (controlFichero.getFUltimaModificacion()!=null) {
			fileControlDTO.setModifiedDate(ICEDateUtils.bigDecimalToDate(controlFichero.getFUltimaModificacion(), ICEDateUtils.FORMAT_yyyyMMddHHmmss));
		}
		
	}
	
}
