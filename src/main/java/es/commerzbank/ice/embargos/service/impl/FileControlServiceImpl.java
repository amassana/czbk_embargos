package es.commerzbank.ice.embargos.service.impl;

import es.commerzbank.ice.comun.lib.service.OfficeCService;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.config.OracleDataSourceEmbargosConfig;
import es.commerzbank.ice.embargos.domain.dto.FileControlDTO;
import es.commerzbank.ice.embargos.domain.dto.FileControlFiltersDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlfichero;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlficheroPK;
import es.commerzbank.ice.embargos.domain.entity.HControlFichero;
import es.commerzbank.ice.embargos.domain.mapper.FileControlAuditMapper;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.domain.specification.FileControlSpecification;
import es.commerzbank.ice.embargos.repository.FileControlAuditRepository;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.FileControlStatusRepository;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.InformationPetitionService;
import es.commerzbank.ice.embargos.service.PetitionService;
import es.commerzbank.ice.embargos.service.files.AEATSeizedService;
import es.commerzbank.ice.embargos.service.files.Cuaderno63InformationService;
import es.commerzbank.ice.embargos.service.files.Cuaderno63SeizedService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.ICEDateUtils;
import es.commerzbank.ice.embargos.utils.ResourcesUtil;
import net.sf.jasperreports.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.*;

@Service
@Transactional(transactionManager="transactionManager")
public class FileControlServiceImpl
	implements FileControlService
{
	
	private static final Logger logger = LoggerFactory.getLogger(FileControlServiceImpl.class);
	
	@Autowired
	private InformationPetitionService informationPetitionService;
	
	@Autowired
	private Cuaderno63InformationService cuaderno63InformationService;
	
	@Autowired
	private Cuaderno63SeizedService cuaderno63SeizedService;
	
	@Autowired
	private AEATSeizedService aeatSeizedService;
	
	@Autowired
	private FileControlMapper fileControlMapper;
	
	@Autowired
	private FileControlAuditMapper fileControlAuditMapper;

	@Autowired
	private FileControlRepository fileControlRepository;
	
	@Autowired
	private FileControlAuditRepository fileControlAuditRepository;
	
	@Autowired
	private FileControlStatusRepository fileControlStatusRepository;

	@Autowired
	private OracleDataSourceEmbargosConfig oracleDataSourceEmbargosConfig;

	@Autowired
	private OfficeCService officeCService;

	@Autowired
	private PetitionService petitionService;

	@Override
	public Page<FileControlDTO> fileSearch(FileControlFiltersDTO fileControlFiltersDTO, Pageable pageable) throws Exception{
		List<FileControlDTO> fileSearchResponseDTOList = new ArrayList<>();
				
		//Date startDate = fileControlFiltersDTO.getStartDate();
		//Date endDate = fileControlFiltersDTO.getEndDate();
		
//		//Validar fechas:
//		if (!validateDates(startDate,endDate)) {
//			throw new Exception("ERROR: incorrect dates");
//		}
		/*
		Long tipoFicheroLong = fileControlFiltersDTO.getFileType();
		
		TipoFichero tipoFichero = new TipoFichero();
		long codTipoFichero = tipoFicheroLong!=null ? Long.valueOf(fileControlFiltersDTO.getFileType()) : 0;
		tipoFichero.setCodTipoFichero(codTipoFichero);
		*/
		Specification<ControlFichero> fileControlSpecification = new FileControlSpecification(fileControlFiltersDTO);
		
		Page<ControlFichero> controlFicheroList = fileControlRepository.findAll(fileControlSpecification, pageable);
									
		for (ControlFichero controlFichero : controlFicheroList) {
		
			FileControlDTO fileSearchResponseDTO = fileControlMapper.toFileControlDTO(controlFichero, null);
			
			fileSearchResponseDTOList.add(fileSearchResponseDTO);
		
		}

		return new PageImpl<>(fileSearchResponseDTOList, pageable, controlFicheroList.getTotalElements());
		
	}
	
	/*private boolean validateDates(Date startDate, Date endDate) {
		
		return (startDate != null && endDate!=null && startDate.compareTo(endDate) <= 0);
	}*/

	@Override
	public FileControlDTO getByCodeFileControl(Long codeFileControl, String type) {
		Optional<ControlFichero> controlFicheroOpt = fileControlRepository.findById(codeFileControl);
		
		if(!controlFicheroOpt.isPresent()) {
			return null;
		}

		return fileControlMapper.toFileControlDTO(controlFicheroOpt.get(), type);
	}

	@Override
	@Transactional(transactionManager="transactionManager")
	public boolean tramitarFicheroInformacion(Long codeFileControl, String usuarioTramitador) throws IOException, ICEException {
		logger.info("FileControlServiceImpl - tramitarFicheroInformacion - codigo "+ codeFileControl);
		//Obtener el codigo del fichero de control:
		Optional<ControlFichero> controlFicheroOpt = fileControlRepository.findById(codeFileControl);
		
		if(!controlFicheroOpt.isPresent()) {
			throw new ICEException("No se ha encontrado el ControlFichero con id: " +codeFileControl);
		}
		
		ControlFichero controlFichero = controlFicheroOpt.get();
		
	
		//Solo se puede tramitar si no hay peticiones de informacion pendientes de ser revisadas:
		
		Integer countPendingPetitionCases = 
				informationPetitionService.getCountPendingPetitionCases(controlFichero);

		Integer countReviewedPetitionCases = 
				informationPetitionService.getCountReviewedPetitionCases(controlFichero);
		
		Integer countPetitionCases = 
				informationPetitionService.getCountPetitionCases(controlFichero);
		
		if (countPendingPetitionCases == 0 && (countReviewedPetitionCases.compareTo(countPetitionCases)==0)) {
			
			cuaderno63InformationService.tramitarFicheroInformacion(codeFileControl, usuarioTramitador);
		
		} else {
			
			String msg = "";
			
			if (countPendingPetitionCases > 0) {
				msg = "No se puede tramitar -> numero de peticiones de informacion pendientes de ser revisadas: " + countPendingPetitionCases;
			
			} else if (countReviewedPetitionCases.compareTo(countPetitionCases)!=0) {
				msg = "No se puede tramitar: no se ha revisado todas las peticiones de informacion.";
			}

			logger.error("ERROR: "+ msg);
			
			return false;
		}
		
		logger.info("FileControlServiceImpl - tramitarFicheroInformacion - end");
		return true;
	}

	@Override
	public boolean tramitarTrabasCuaderno63(Long codeFileControl, String usuarioTramitador) throws IOException, ICEException {
		logger.info("FileControlServiceImpl - tramitarTrabasCuaderno63 - codigo "+ codeFileControl);
		
		cuaderno63SeizedService.tramitarTrabas(codeFileControl, usuarioTramitador);
		
		logger.info("FileControlServiceImpl - tramitarTrabasCuaderno63 - end");
		return true;
	}

	@Override
	public boolean tramitarTrabasAEAT(Long codeFileControl, String usuarioTramitador) throws Exception {
		logger.info("FileControlServiceImpl - tramitarTrabasAEAT - codigo "+ codeFileControl);
		
		aeatSeizedService.tramitarTrabas(codeFileControl, usuarioTramitador);
		
		logger.info("FileControlServiceImpl - tramitarTrabasAEAT - end");
		return true;
	}
	
	@Override
	public boolean updateFileControl(Long codeFileControl, FileControlDTO fileControlDTO, String userModif) {
		logger.info("FileControlServiceImpl - updateFileControl - codigo "+ codeFileControl);
		Optional<ControlFichero> controlFicheroOpt = fileControlRepository.findById(codeFileControl);
		
		if(!controlFicheroOpt.isPresent()) {
			return false;
		}
		
		ControlFichero controlFichero = controlFicheroOpt.get();
		
		//TODO solo actualiza el estado, pendiente resto de campos:
		
		if (fileControlDTO.getStatus()!=null && fileControlDTO.getStatus().getCode()!=null) {

			EstadoCtrlficheroPK estadoCtrlficheroPK = new EstadoCtrlficheroPK();
			estadoCtrlficheroPK.setCodEstado(fileControlDTO.getStatus().getCode());
			estadoCtrlficheroPK.setCodTipoFichero(controlFichero.getTipoFichero().getCodTipoFichero());
			
			Optional<EstadoCtrlfichero> estadoCtrlficheroOpt = fileControlStatusRepository.findById(estadoCtrlficheroPK);
			
			if(!estadoCtrlficheroOpt.isPresent()) {
				return false;
			}
			
			controlFichero.setEstadoCtrlfichero(estadoCtrlficheroOpt.get());
		}
		
		//controlFichero.setFicheroRespuesta("PRUEBA2");
		
		//TODO no se esta guardando el EstadoCtrlfichero, puede ser debido al problema en ControlFichero del "Repeated Columns" de COD_TIPO_FICHERO.

		//Usuario y fecha ultima modificacion:
		controlFichero.setUsuarioUltModificacion(userModif);
		controlFichero.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
		fileControlRepository.save(controlFichero);
		
		logger.info("FileControlServiceImpl - updateFileControl - end");
		return true;
	}
	
	@Override
	public boolean updateFileControlStatus(Long codeFileControl, Long codFileControlStatus, String userModif, String tipoDatos) throws Exception {
		logger.info("FileControlServiceImpl - updateFileControlStatus - codigo "+ codeFileControl +" estado "+ codFileControlStatus);
		Optional<ControlFichero> fileControlOpt = fileControlRepository.findById(codeFileControl);
		
		if(!fileControlOpt.isPresent()) {
			return false;
		}
		
		if (codFileControlStatus != null) {
			
			ControlFichero controlFichero = fileControlOpt.get();

			EstadoCtrlficheroPK estadoCtrlficheroPK = new EstadoCtrlficheroPK();
			estadoCtrlficheroPK.setCodEstado(codFileControlStatus);
			estadoCtrlficheroPK.setCodTipoFichero(controlFichero.getTipoFichero().getCodTipoFichero());

			Optional<EstadoCtrlfichero> estadoCtrlficheroOpt = fileControlStatusRepository.findById(estadoCtrlficheroPK);

			if (!estadoCtrlficheroOpt.isPresent()) {
				return false;
			}

			EstadoCtrlfichero estadoCtrlFichero = estadoCtrlficheroOpt.get();

			if (controlFichero.getTipoFichero().getCodTipoFichero() == EmbargosConstants.COD_TIPO_FICHERO_PETICION_CGPJ) {
				changeCGPJStatus(controlFichero, codFileControlStatus, tipoDatos);
			}
			else {
				controlFichero.setEstadoCtrlfichero(estadoCtrlFichero);

				//Indicador procesado: al cambiar de estado, determinar si el flag indProcesado tiene que cambiar:
				String indProcesado = determineIndProcesadoFromEstadoControlFichero(estadoCtrlFichero);
				controlFichero.setIndProcesado(indProcesado);
			}

			// Determinar envío carta - aplica a levantamientos, embargos se hace al tramitar
			controlFichero.setIndEnvioCarta(determineIndEnvioCarta(estadoCtrlFichero));

			//Usuario y fecha ultima modificacion:
			controlFichero.setUsuarioUltModificacion(userModif);
			controlFichero.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));

			fileControlRepository.save(controlFichero);
		
		} else {			
			return false;
		}
		
		logger.info("FileControlServiceImpl - updateFileControlStatus - end");

		return true;
	}

	private void changeCGPJStatus(ControlFichero controlFichero, Long codFileControlStatus, String tipoDatos) throws Exception {

		if (controlFichero.getPeticiones() == null || controlFichero.getPeticiones().size() != 1) {
			throw new Exception("No se puede cambiar el estado del fichero por no tener una petición asociada");
		}

		if ("TRABAS".equals(tipoDatos))
			controlFichero.setCodSubestadoTraba(codFileControlStatus);
		else if ("LEVANTAMIENTOS".equals(tipoDatos))
			controlFichero.setCodSubestadoLevantamiento(codFileControlStatus);

		// caso contabilizado, se mira si se puede pasar de inicial a procesado
		if (controlFichero.getPeticiones().get(0).getEstadoIntPeticion().getCodEstadoIntPeticion() == EmbargosConstants.CGPJ_ESTADO_INTERNO_INICIAL
			&&
			(!EmbargosConstants.IND_FLAG_SI.equals(controlFichero.getIndTieneTrabas()) ||
			(EmbargosConstants.IND_FLAG_SI.equals(controlFichero.getIndTieneTrabas()) && controlFichero.getCodSubestadoTraba() == EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_PENDING_TO_SEND))
			&&
			(!EmbargosConstants.IND_FLAG_SI.equals(controlFichero.getIndTieneLevantamientos()) ||
			(EmbargosConstants.IND_FLAG_SI.equals(controlFichero.getIndTieneLevantamientos()) && controlFichero.getCodSubestadoLevantamiento() == EmbargosConstants.COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_ACCOUNTED)))
		{
			petitionService.changeStatus(controlFichero.getPeticiones().get(0), EmbargosConstants.CGPJ_ESTADO_INTERNO_PROCESADO);
		}
		// caso extorno, se revierte de procesado a inicial
		else if (controlFichero.getPeticiones().get(0).getEstadoIntPeticion().getCodEstadoIntPeticion() == EmbargosConstants.CGPJ_ESTADO_INTERNO_PROCESADO
				&&
				(!EmbargosConstants.IND_FLAG_SI.equals(controlFichero.getIndTieneTrabas()) ||
				(EmbargosConstants.IND_FLAG_SI.equals(controlFichero.getIndTieneTrabas()) && controlFichero.getCodSubestadoTraba() == EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_RECEIVED)))
		{
			petitionService.changeStatus(controlFichero.getPeticiones().get(0), EmbargosConstants.CGPJ_ESTADO_INTERNO_INICIAL);
		}
	}

	private String determineIndEnvioCarta(EstadoCtrlfichero estadoControlFichero) {
		long codEstadoCtrlFichero = estadoControlFichero.getId().getCodEstado();
		long codTipoFichero = estadoControlFichero.getId().getCodTipoFichero();

		// Flag envío carta a NO (indicando pendiente de envío) cuando:
		// es embargo (aeat o norma63) y el nuevo estado es pendiente de envío
		// o levantamiento (aeat o norma63) y el nuevo estado es procesado
		// TODO pendiente CPGJ

		if (
				// para trabas se hace al tramitar, para evitar que retrocesos y recontabilziaciones reenvíen las cartas.
			//	(codEstadoCtrlFichero == EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_PENDING_TO_SEND && codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_NORMA63)
			//||	(codEstadoCtrlFichero == EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PENDING_TO_SEND && codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_AEAT)
				(codEstadoCtrlFichero == EmbargosConstants.COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_ACCOUNTED && codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_NORMA63)
			||	(codEstadoCtrlFichero == EmbargosConstants.COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_ACCOUNTED && codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_AEAT)
		){
			return EmbargosConstants.IND_FLAG_NO;
		}

		return null;
	}

	private String determineIndProcesadoFromEstadoControlFichero(EstadoCtrlfichero estadoControlFichero) {
	
		long codEstadoCtrlFichero = estadoControlFichero.getId().getCodEstado();
		long codTipoFichero = estadoControlFichero.getId().getCodTipoFichero();
		
		//Indicador Procesado a 'SI' cuando se cumplen los siguientes tipos de fichero y estado:
		// - Para fase 1:
		//		* NORMA63: Procesando y Procesado
		//		* AEAT: fase 1 no aplica en AEAT.
		// - Para fase 3:
		//		* NORMA63: Generado, Enviado.
		//		* AEAT: Generado, Enviado y Confirmado.
		// - Para fase 5:
		//		* NORMA63: 'Pendiente de respuesta contable' y 'Procesado'.
		//		* AEAT: 'Pendiente de respuesta contable' y 'Procesado'.
		if (
			/*(codEstadoCtrlFichero == EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_INFORMACION_NORMA63_PROCESSING
			&& codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_PETICION_INFORMACION_NORMA63)	    ||	*/
		 	(codEstadoCtrlFichero == EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_INFORMACION_NORMA63_PROCESSED
			&& codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_PETICION_INFORMACION_NORMA63)	
		 || (codEstadoCtrlFichero == EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_GENERATED
			&& codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_NORMA63)
		 || (codEstadoCtrlFichero == EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_SENT
			&& codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_NORMA63)
		 || (codEstadoCtrlFichero == EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_GENERATED
			&& codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_AEAT)
		 || (codEstadoCtrlFichero == EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_SENT
			&& codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_AEAT)
		 || (codEstadoCtrlFichero == EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_CONFIRMED
			&& codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_AEAT)
		 ||	(codEstadoCtrlFichero == EmbargosConstants.COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_PENDING_ACCOUNTING_RESPONSE
			&& codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_NORMA63)
		 ||	(codEstadoCtrlFichero == EmbargosConstants.COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_ACCOUNTED
			&& codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_NORMA63)
		 ||	(codEstadoCtrlFichero == EmbargosConstants.COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_PENDING_ACCOUNTING_RESPONSE
			&& codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_AEAT)
		 ||	(codEstadoCtrlFichero == EmbargosConstants.COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_ACCOUNTED
			&& codTipoFichero == EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_AEAT)		 
		 ){
			
			return EmbargosConstants.IND_FLAG_SI;
		}
		
		return EmbargosConstants.IND_FLAG_NO;
	}
	
	/*@Override
	@Transactional(transactionManager="transactionManager", propagation = Propagation.REQUIRES_NE W)
	public void updateFileControlStatusTransaction(ControlFichero controlFichero, Long codEstado) {
		
		if (controlFichero!=null && controlFichero.getFUltimaModificacion()!=null) {
			
			TipoFichero tipoFichero = controlFichero.getTipoFichero();
		logger.info("FileControlServiceImpl - updateFileControlStatusTransaction - start");
		
	        EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero(codEstado, tipoFichero.getCodTipoFichero());
	        controlFichero.setEstadoCtrlfichero(estadoCtrlfichero);
				        
			fileControlRepository.save(controlFichero);
		}
	}*/

	/*
	@Override
	public void updateFileControlStatusTransaction(ControlFichero controlFichero, Long codEstado, String userModif) {
					
		if (controlFichero!=null && controlFichero.getFUltimaModificacion()!=null) {
			
			TipoFichero tipoFichero = controlFichero.getTipoFichero();
			
	        EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero(codEstado, tipoFichero.getCodTipoFichero());
	        controlFichero.setEstadoCtrlfichero(estadoCtrlfichero);
			
	        if (userModif==null) {
	        	userModif = controlFichero.getUsuarioUltModificacion()!=null ? controlFichero.getUsuarioUltModificacion() : null;
	        }       
	        controlFichero.setUsuarioUltModificacion(userModif);
	        
	        controlFichero.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
	        
			fileControlRepository.save(controlFichero);
		}

		logger.info("FileControlServiceImpl - updateFileControlStatusTransaction - end");
	}
	*/
	@Override
	public void saveFileControlTransaction(ControlFichero controlFichero) {
		fileControlRepository.save(controlFichero);
	}
	
	@Override
	public List<FileControlDTO> getAuditByCodeFileControl(Long codeFileControl) {

		List<FileControlDTO> fileControlDTOList = new ArrayList<>();
		
		List<HControlFichero> hControlFicheroList = fileControlAuditRepository.findByCodControlFichero(codeFileControl);
		
		for(HControlFichero hControlFichero : hControlFicheroList) {
				
			//Se recupera la descripcion del estado de hControlFichero:
			String descEstado = null;
			if (hControlFichero.getCodEstado()!=null && hControlFichero.getCodTipoFichero()!=null) {
				EstadoCtrlficheroPK estadoCtrlficheroPK = new EstadoCtrlficheroPK();
				estadoCtrlficheroPK.setCodEstado(hControlFichero.getCodEstado().longValue());
				estadoCtrlficheroPK.setCodTipoFichero(hControlFichero.getCodTipoFichero().longValue());
				
				Optional<EstadoCtrlfichero> estadoCtrlficheroOpt = fileControlStatusRepository.findById(estadoCtrlficheroPK);
				
				if(estadoCtrlficheroOpt.isPresent()) {
					descEstado = estadoCtrlficheroOpt.get().getDescripcion();
				}
			}
			
			//Se obtiene el DTO:
			FileControlDTO fileControlDTO = fileControlAuditMapper.toFileControlDTO(hControlFichero, 
					descEstado, 
					"targetTEST", 
					new Date());
		
			fileControlDTOList.add(fileControlDTO);
		}

		return fileControlDTOList;
		
		
	}

	@Override
	public byte[] generateFileControl(FileControlFiltersDTO fileControlFilters, String oficina) throws Exception {

		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection connEmbargos = oracleDataSourceEmbargosConfig.getEmbargosConnection();) {
			Resource logoRes = ResourcesUtil.getImageLogoCommerceResource();
			parameters.put("img_param", logoRes.getFile().toString());

			parameters.put("NOMBRE_SUCURSAL", oficina);

			parameters.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));

			/*
				<parameter name="tipos_fichero" class="java.util.Collection" nestedType="java.lang.Long"/>
	<parameter name="status" class="java.util.Collection" nestedType="java.lang.Long"/>
	<parameter name="procesado" class="java.lang.String"/>
	<parameter name="procesado_query" class="java.lang.String" isForPrompting="false">
		<defaultValueExpression><![CDATA[$P{procesado} == null ? "" : " AND IND_PROCESADO = '"+ $P{procesado} +"' "]]></defaultValueExpression>
	</parameter>
	<parameter name="fecha_desde" class="java.util.Date"/>
	<parameter name="fecha_hasta" class="java.util.Date"/>
			 */

			parameters.put("tipos_fichero", fileControlFilters.getFileType());
			if (fileControlFilters.getStatus() != null)
				parameters.put("status", Arrays.asList(fileControlFilters.getStatus().getCode()));
			if (fileControlFilters.getIsPending() != null && fileControlFilters.getIsPending().booleanValue())
				parameters.put("procesado", "N");
			parameters.put("fecha_desde", fileControlFilters.getStartDate());
			parameters.put("fecha_hasta", fileControlFilters.getEndDate());

			Resource resource = ResourcesUtil.getFromJasperFolder("ficheros.jasper");
			InputStream resourceInputStream = resource.getInputStream();

			JasperPrint reporteLleno = JasperFillManager.fillReport(resourceInputStream, parameters, connEmbargos);
			
			List<JRPrintPage> pages = reporteLleno.getPages();

			 if (pages.size() == 0)  return null;

			return JasperExportManager.exportReportToPdf(reporteLleno);
		} catch (Exception ex) {
			throw new Exception("Error in generarReporteListado()", ex);
		}
	}

	@Override
	public List<ControlFichero> cartasPendientesEnvio() {
		return fileControlRepository.findByIndEnvioCarta("N");
	}

	@Override
	public void cartaEnviada(ControlFichero controlFichero) {
		controlFichero.setIndEnvioCarta("S");
		fileControlRepository.save(controlFichero);
	}

	@Override
	public List<ControlFichero> listByStatus(long estado, List<Long> tiposFichero) {
		return fileControlRepository.findFicherosByTipoFicheroByCodEstado(estado, tiposFichero);
	}
}
