package es.commerzbank.ice.embargos.domain.mapper;

import es.commerzbank.ice.embargos.domain.dto.FileControlCsvDTO;
import es.commerzbank.ice.embargos.domain.dto.FileControlDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlfichero;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlficheroPK;
import es.commerzbank.ice.embargos.domain.entity.TipoFichero;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.EmbargosUtils;
import es.commerzbank.ice.embargos.utils.ICEDateUtils;
import org.mapstruct.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Mapper(componentModel="spring")
public abstract class FileControlMapper {
	
	public ControlFichero generateControlFichero(File file, Long codTipoFichero, String originalName, File processedFile) throws IOException{
		
        ControlFichero controlFichero = new ControlFichero();

        TipoFichero tipoFichero = new TipoFichero(); 
        tipoFichero.setCodTipoFichero(codTipoFichero);
        
        //EntidadComnicadora se inicializa con valor a null (se seteara su valor a posteriori):
        controlFichero.setEntidadesComunicadora(null);
        
        //Guardar registro del control del fichero de Peticion:
        controlFichero.setTipoFichero(tipoFichero);
        controlFichero.setNombreFichero(originalName);
        if (processedFile != null)
        	controlFichero.setRutaFichero(processedFile.getCanonicalPath());

        //Descripcion por defecto:
        controlFichero.setDescripcion(EmbargosConstants.CONTROL_FICHERO_DESCRIPCION_DEFAULT);
        
        //Calculo del CRC del fichero:
        if (file != null && file.exists()) {
        	controlFichero.setNumCrc(es.commerzbank.ice.comun.lib.util.FileUtils.getMD5(file.getCanonicalPath()));
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
        //EstadoCtrlfichero estadoCtrlfichero = determineInitialEstadoCtrlFicheroByCodTipoFichero(codTipoFichero);
        //controlFichero.setEstadoCtrlfichero(estadoCtrlfichero);
        
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
		/*
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
		else if (codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_FICHERO_FINAL_AEAT_INTERNAL) {

			codEstado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_FINAL_AEAT_GENERANDO;
		}
		else {
			//Estado inicial por defecto:
			codEstado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_INITIAL_STATUS_DEFAULT;
		}
		 */
		codEstado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_INITIAL_STATUS_DEFAULT;
		
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
	
	@Mappings({
		@Mapping(source = "status.code", target = "codeStatus"),
		@Mapping(source = "status.description", target = "descriptionStatus")
	})
	public abstract FileControlCsvDTO toFileControlCsvDTO (FileControlDTO controlFichero);
	
	public abstract ArrayList<FileControlCsvDTO> toFileControlCsvDTO (List<FileControlDTO> controlFichero);
	
	@AfterMapping
	protected void setFileControlDTOAfterMapping(ControlFichero controlFichero, @MappingTarget FileControlDTO fileControlDTO) {
		
		//Delivery date:
		if (controlFichero.getFechaMaximaRespuesta()!=null) {
			fileControlDTO.setDeliveryDate(ICEDateUtils.bigDecimalToDate(controlFichero.getFechaMaximaRespuesta(), ICEDateUtils.FORMAT_yyyyMMdd));
		}
		
		//Indicador de procesado (se pasa de String a Boolean):
		boolean isProcessed = controlFichero.getIndProcesado()!=null && EmbargosConstants.IND_FLAG_SI.equals(controlFichero.getIndProcesado());
		fileControlDTO.setIsProcessed(isProcessed);

		if (controlFichero.getEntidadesComunicadora() != null)
		{
			fileControlDTO.setIsCGPJ(EmbargosConstants.IND_FLAG_SI.equals(controlFichero.getEntidadesComunicadora().getIndCgpj()));
			fileControlDTO.setIsCuaderno63(EmbargosConstants.IND_FLAG_SI.equals(controlFichero.getEntidadesComunicadora().getIndNorma63()));
			fileControlDTO.setIsAEAT(EmbargosConstants.IND_FLAG_SI.equals(controlFichero.getEntidadesComunicadora().getIndFormatoAeat()));
			fileControlDTO.setIsTGSS(
					fileControlDTO.getIsCuaderno63() &&
					EmbargosConstants.PREFIJO_FICHERO_TGSS.equals(controlFichero.getEntidadesComunicadora().getPrefijoFicheros()));
		}
		
		//Fecha de ultima modificacion:
		if (controlFichero.getFUltimaModificacion()!=null) {
			fileControlDTO.setModifiedDate(ICEDateUtils.bigDecimalToDate(controlFichero.getFUltimaModificacion(), ICEDateUtils.FORMAT_yyyyMMddHHmmss));
		}
		
		
		if (controlFichero.getFechaCreacion()!=null) {
			fileControlDTO.setCreatedDate(ICEDateUtils.bigDecimalToDate(controlFichero.getFechaCreacion(), ICEDateUtils.FORMAT_yyyyMMdd));
		}
		
		if (controlFichero.getFechaGeneracionRespuesta()!=null) {
			fileControlDTO.setResponseGenerationDate(ICEDateUtils.bigDecimalToDate(controlFichero.getFechaGeneracionRespuesta(), ICEDateUtils.FORMAT_yyyyMMddHHmmss));
		}

		fileControlDTO.setHasSeizures(EmbargosConstants.IND_FLAG_SI.equals(controlFichero.getIndTieneTrabas()));
		fileControlDTO.setHasLiftings(EmbargosConstants.IND_FLAG_SI.equals(controlFichero.getIndTieneLevantamientos()));
		fileControlDTO.setHasExecutions(EmbargosConstants.IND_FLAG_SI.equals(controlFichero.getIndTieneEjecuciones()));

		try {
			if (EmbargosConstants.COD_TIPO_FICHERO_PETICION_CGPJ == controlFichero.getTipoFichero().getCodTipoFichero()) {
				try {
					String[] parts = fileControlDTO.getFileName().split("_");
					fileControlDTO.setRequestName(parts[3].substring(0, 16));
				} catch (Exception e) {
					fileControlDTO.setRequestName(fileControlDTO.getFileName());
				}
			} else {
				fileControlDTO.setRequestName(fileControlDTO.getFileName());
			}
		} catch (Exception e) {
			fileControlDTO.setRequestName(fileControlDTO.getFileName());
		}
	}
}
