package es.commerzbank.ice.embargos.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.comun.lib.util.ICEParserException;
import es.commerzbank.ice.embargos.config.OracleDataSourceEmbargosConfig;
import es.commerzbank.ice.embargos.domain.dto.FileControlDTO;
import es.commerzbank.ice.embargos.domain.dto.FileControlFiltersDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlfichero;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlficheroPK;
import es.commerzbank.ice.embargos.domain.entity.HControlFichero;
import es.commerzbank.ice.embargos.domain.entity.TipoFichero;
import es.commerzbank.ice.embargos.domain.mapper.FileControlAuditMapper;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.domain.specification.FileControlSpecification;
import es.commerzbank.ice.embargos.repository.FileControlAuditRepository;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.FileControlStatusRepository;
import es.commerzbank.ice.embargos.service.AEATService;
import es.commerzbank.ice.embargos.service.Cuaderno63Service;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.InformationPetitionService;
import es.commerzbank.ice.utils.ICEDateUtils;
import es.commerzbank.ice.utils.ResourcesUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
@Transactional(transactionManager="transactionManager")
public class FileControlServiceImpl implements FileControlService{
	
	private static final Logger logger = LoggerFactory.getLogger(FileControlServiceImpl.class);

	@Autowired
	InformationPetitionService informationPetitionService;
	
	@Autowired
	Cuaderno63Service cuaderno63Service;
	
	@Autowired
	AEATService aeatService;
	
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

	@Override
	public Page<FileControlDTO> fileSearch(FileControlFiltersDTO fileControlFiltersDTO, Pageable pageable) throws Exception{
		logger.info("FileControlServiceImpl - fileSearch - start");
		List<FileControlDTO> fileSearchResponseDTOList = new ArrayList<>();
				
		Date startDate = fileControlFiltersDTO.getStartDate();
		Date endDate = fileControlFiltersDTO.getEndDate();
		
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
		
			FileControlDTO fileSearchResponseDTO = fileControlMapper.toFileControlDTO(controlFichero);
			
			fileSearchResponseDTOList.add(fileSearchResponseDTO);
		
		}
		
		logger.info("FileControlServiceImpl - fileSearch - end");
		return new PageImpl<>(fileSearchResponseDTOList, pageable, controlFicheroList.getTotalElements());
		
	}
	
	private boolean validateDates(Date startDate, Date endDate) {
		
		return (startDate != null && endDate!=null && startDate.compareTo(endDate) <= 0);
	}

	@Override
	public FileControlDTO getByCodeFileControl(Long codeFileControl) {
		logger.info("FileControlServiceImpl - getByCodeFileControl - start");
		Optional<ControlFichero> controlFicheroOpt = fileControlRepository.findById(codeFileControl);
		
		if(!controlFicheroOpt.isPresent()) {
			return null;
		}
		logger.info("FileControlServiceImpl - getByCodeFileControl - end");
		return fileControlMapper.toFileControlDTO(controlFicheroOpt.get());
	}

	@Override
	public boolean tramitarFicheroInformacion(Long codeFileControl, String usuarioTramitador) throws IOException, ICEException, ICEParserException {
		logger.info("FileControlServiceImpl - tramitarFicheroInformacion - start");
		//Obtener el codigo del fichero de control:
		Optional<ControlFichero> controlFicheroOpt = fileControlRepository.findById(codeFileControl);
		
		if(!controlFicheroOpt.isPresent()) {
			throw new ICEException("", "ERROR: no se ha encontrado el ControlFichero con id: " +codeFileControl);
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
			
			cuaderno63Service.tramitarFicheroInformacion(codeFileControl, usuarioTramitador);
		
		} else {
			
			String msg = "";
			
			if (countPendingPetitionCases > 0) {
				msg = "No se puede tramitar -> numero de peticiones de informacion pendientes de ser revisadas: " + countPendingPetitionCases;
			
			} else if (countReviewedPetitionCases.compareTo(countPetitionCases)!=0) {
				msg = "No se puede tramitar: no se ha revisado todas las peticiones de informacion.";
			}
			
			//throw new ICEException("", "ERROR: " + msg);
			logger.error("ERROR: "+ msg);
			
			return false;
		}
		
		logger.info("FileControlServiceImpl - tramitarFicheroInformacion - end");
		return true;
	}

	@Override
	public boolean tramitarTrabasCuaderno63(Long codeFileControl, String usuarioTramitador) throws IOException, ICEParserException {
		logger.info("FileControlServiceImpl - tramitarTrabasCuaderno63 - start");
		
		cuaderno63Service.tramitarTrabas(codeFileControl, usuarioTramitador);
		
		logger.info("FileControlServiceImpl - tramitarTrabasCuaderno63 - end");
		return true;
	}

	@Override
	public boolean tramitarTrabasAEAT(Long codeFileControl, String usuarioTramitador) throws IOException, ICEParserException {
		logger.info("FileControlServiceImpl - tramitarTrabasAEAT - start");
		
		aeatService.tramitarTrabas(codeFileControl, usuarioTramitador);
		
		logger.info("FileControlServiceImpl - tramitarTrabasAEAT - end");
		return true;
	}
	
	@Override
	public boolean updateFileControl(Long codeFileControl, FileControlDTO fileControlDTO, String userModif) {
		logger.info("FileControlServiceImpl - updateFileControl - start");
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
		
		fileControlRepository.save(controlFichero);
		
		logger.info("FileControlServiceImpl - updateFileControl - end");
		return true;
	}
	
	@Override
	public boolean updateFileControlStatus(Long codeFileControl, Long codFileControlStatus, String userModif) {
		logger.info("FileControlServiceImpl - updateFileControlStatus - start");
		Optional<ControlFichero> fileControlOpt = fileControlRepository.findById(codeFileControl);
		
		if(!fileControlOpt.isPresent()) {
			return false;
		}
		
		if(codFileControlStatus!=null) {
			
			ControlFichero controlFichero = fileControlOpt.get();
			
			EstadoCtrlficheroPK estadoCtrlficheroPK = new EstadoCtrlficheroPK();
			estadoCtrlficheroPK.setCodEstado(codFileControlStatus);
			estadoCtrlficheroPK.setCodTipoFichero(controlFichero.getTipoFichero().getCodTipoFichero());
			
			Optional<EstadoCtrlfichero> estadoCtrlficheroOpt = fileControlStatusRepository.findById(estadoCtrlficheroPK);
			
			if(!estadoCtrlficheroOpt.isPresent()) {
				return false;
			}
			
			controlFichero.setEstadoCtrlfichero(estadoCtrlficheroOpt.get());
			
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
	
	@Override
	@Transactional(transactionManager="transactionManager", propagation = Propagation.REQUIRES_NEW)
	public void updateFileControlStatusTransaction(ControlFichero controlFichero, Long codEstado) {
		
		if (controlFichero!=null && controlFichero.getFUltimaModificacion()!=null) {
			
			TipoFichero tipoFichero = controlFichero.getTipoFichero();
		logger.info("FileControlServiceImpl - updateFileControlStatusTransaction - start");
		
	        EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero(codEstado, tipoFichero.getCodTipoFichero());
	        controlFichero.setEstadoCtrlfichero(estadoCtrlfichero);
				        
			fileControlRepository.save(controlFichero);
		}
	}

	@Override
	@Transactional(transactionManager="transactionManager", propagation = Propagation.REQUIRES_NEW)
	public void updateFileControlStatusTransaction(ControlFichero controlFichero, Long codEstado, String userModif) {
					
		if (controlFichero!=null && controlFichero.getFUltimaModificacion()!=null) {
			
			TipoFichero tipoFichero = controlFichero.getTipoFichero();
			
	        EstadoCtrlfichero estadoCtrlfichero = new EstadoCtrlfichero(codEstado, tipoFichero.getCodTipoFichero());
	        controlFichero.setEstadoCtrlfichero(estadoCtrlfichero);
			
	        if (userModif==null) {
	        	userModif = controlFichero.getUsuarioUltModificacion()!=null ? controlFichero.getUsuarioUltModificacion() : null;
	        }       
	        controlFichero.setUsuarioUltModificacion(userModif);
	        
			fileControlRepository.save(controlFichero);
		}

		logger.info("FileControlServiceImpl - updateFileControlStatusTransaction - end");
	}
	
	@Override
	@Transactional(transactionManager="transactionManager", propagation = Propagation.REQUIRES_NEW)
	public void saveFileControlTransaction(ControlFichero controlFichero) {
		logger.info("FileControlServiceImpl - saveFileControlTransaction - start");
		fileControlRepository.save(controlFichero);
		logger.info("FileControlServiceImpl - saveFileControlTransaction - end");
	}
	
	@Override
	public List<FileControlDTO> getAuditByCodeFileControl(Long codeFileControl) {
		logger.info("FileControlServiceImpl - getAuditByCodeFileControl - start");

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

		logger.info("FileControlServiceImpl - getAuditByCodeFileControl - end");
		return fileControlDTOList;
		
		
	}

	@Override
	public byte[] generarReporteListado(Integer[] codTipoFichero,
			Integer codEstado, boolean isPending, Date fechaInicio, Date fechaFin) throws Exception {
		logger.info("FileControlServiceImpl - generarReporteListado - start");
		
		System.out.println("codTipoFichero: " + codTipoFichero +  " codEstado: " + codEstado + " isPending: " + isPending + " fechaInicio: "
				+ fechaInicio + " fechaFin: " + fechaFin);

		HashMap<String, Object> parameters = new HashMap<String, Object>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");

		String query = "WHERE";

		if (codTipoFichero != null) {
			String fileTypes = String.join(", ",
					Arrays.asList(codTipoFichero).stream().map(i -> String.valueOf(i)).collect(Collectors.toList()));
			query = query + " c.COD_TIPO_FICHERO IN (" + fileTypes + ") AND";

			if (codEstado != null) {
				query = query + " c.COD_ESTADO=" + codEstado.toString() + " AND";
			}
		}

		if (fechaInicio != null) {
			query = query + " c.FECHA_INCORPORACION>=" + ICEDateUtils.dateToBigDecimal(fechaInicio, ICEDateUtils.FORMAT_yyyyMMddHHmmss) + " AND";
		}

		if (fechaFin != null) {
			query = query + " c.FECHA_GENERACION_RESPUESTA<=" + ICEDateUtils.dateToBigDecimal(fechaFin, ICEDateUtils.FORMAT_yyyyMMddHHmmss) + " AND";
		}

		if (codTipoFichero == null && codEstado == null && fechaInicio == null && fechaFin == null) {
			query = "";
		} 
		
		
		if (query.isEmpty()) {
			query = "WHERE";
		}
		
		if (isPending) {
			query = query + " c.IND_PROCESADO<>'S'";
		} else {
			query = query + " c.IND_PROCESADO='S'";
		}
		
		
		
		
		System.out.println(query);

		parameters.put("query_param", query);
		// parameters.put("cod_user", 3);

		try (Connection connEmbargos = oracleDataSourceEmbargosConfig.getEmbargosConnection();) {
//				Connection connComunes = oracleDataSourceEmbargosConfig.getComunesConnection()
			

//			parameters.put("conn_param", connComunes);

			Resource imageReport = ResourcesUtil.getImageLogoCommerceResource();
			File image = imageReport.getFile();
			parameters.put("img_param", image.toString());

			Resource subResource = ResourcesUtil.getReportHeaderResource();
			InputStream subResourceInputStream = subResource.getInputStream();

			JasperReport subReport = (JasperReport) JRLoader.loadObject(subResourceInputStream);
			parameters.put("file_param", subReport);

			Resource resource = ResourcesUtil.getFromJasperFolder("control_ficheros.jasper");
			InputStream resourceInputStream = resource.getInputStream();

			JasperPrint reporteLleno = JasperFillManager.fillReport(resourceInputStream, parameters, connEmbargos);
			
			logger.info("FileControlServiceImpl - generarReporteListado - end");
			return JasperExportManager.exportReportToPdf(reporteLleno);
		} catch (JRException | SQLException ex) {
			throw new Exception("Error in generarReporteListado()", ex);
		}
	}


	
}
