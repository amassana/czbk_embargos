package es.commerzbank.ice.embargos.service.impl;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.comun.lib.typeutils.ICEDateUtils;
import es.commerzbank.ice.embargos.config.OracleDataSourceEmbargosConfig;
import es.commerzbank.ice.embargos.domain.dto.BankAccountDTO;
import es.commerzbank.ice.embargos.domain.dto.BankAccountLiftingDTO;
import es.commerzbank.ice.embargos.domain.dto.LiftingAuditDTO;
import es.commerzbank.ice.embargos.domain.dto.LiftingDTO;
import es.commerzbank.ice.embargos.domain.dto.LiftingStatusDTO;
import es.commerzbank.ice.embargos.domain.dto.PetitionCaseDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.CuentaLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.EstadoIntLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.EstadoLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.HLevantamientoTraba;
import es.commerzbank.ice.embargos.domain.entity.HPeticionInformacion;
import es.commerzbank.ice.embargos.domain.entity.LevantamientoTraba;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacion;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacionCuenta;
import es.commerzbank.ice.embargos.domain.mapper.BankAccountLiftingMapper;
import es.commerzbank.ice.embargos.domain.mapper.BankAccountMapper;
import es.commerzbank.ice.embargos.domain.mapper.LiftingAuditMapper;
import es.commerzbank.ice.embargos.domain.mapper.LiftingMapper;
import es.commerzbank.ice.embargos.domain.mapper.LiftingStatusMapper;
import es.commerzbank.ice.embargos.repository.LiftingAuditRepository;
import es.commerzbank.ice.embargos.repository.LiftingBankAccountRepository;
import es.commerzbank.ice.embargos.repository.LiftingRepository;
import es.commerzbank.ice.embargos.repository.LiftingStatusRepository;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.LiftingService;
import es.commerzbank.ice.utils.EmbargosConstants;
import es.commerzbank.ice.utils.ResourcesUtil;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
@Transactional(transactionManager = "transactionManager")
public class LiftingServiceImpl implements LiftingService {

	private static final Logger LOG = LoggerFactory.getLogger(LiftingServiceImpl.class);
	
	@Autowired
	private OracleDataSourceEmbargosConfig oracleDataSourceEmbargosConfig;
	
	@Autowired
	private FileControlService fileControlService;

	@Autowired
	private LiftingMapper liftingMapper;

	@Autowired
	private LiftingRepository liftingRepository;

	@Autowired
	private LiftingBankAccountRepository liftingBankAccountRepository;
	
	@Autowired
	private BankAccountLiftingMapper bankAccountLiftingMapper;
	
	@Autowired
	private LiftingAuditRepository liftingAuditRepository;
	
	@Autowired
	private LiftingAuditMapper liftingAuditMapper;
	
	@Autowired
	private LiftingStatusRepository liftingStatusRepository;
	
	@Autowired
	private LiftingStatusMapper liftingStatusMapper;

	@Autowired
	private OracleDataSourceEmbargosConfig oracleDataSourceEmbargos;

	@Override
	public List<LiftingDTO> getAllByControlFichero(ControlFichero controlFichero) {
		List<LevantamientoTraba> liftingList = liftingRepository.findAllByControlFichero(controlFichero);
		
		
		List<LiftingDTO> liftingDTOList = new ArrayList<>();
		for(LevantamientoTraba levantamiento : liftingList) {
			LiftingDTO lifting = liftingMapper.toLiftingDTO(levantamiento);
			lifting.setStatus(liftingStatusMapper.toLiftingStatus(levantamiento.getEstadoLevantamiento()));
			liftingDTOList.add(lifting);
		}
		
		return liftingDTOList;
	}

	@Override
	public LiftingDTO getAllByControlFicheroAndLevantamiento(Long codeFileControl, Long codeLifting) {
		List<BankAccountLiftingDTO> bankAccountList = new ArrayList<>();
		LiftingDTO response = null;
		
		Optional<LevantamientoTraba> result = liftingRepository.findById(codeLifting);
		
		if (result != null) {
			
			response = liftingMapper.toLiftingDTO(result.get());
			response.setStatus(liftingStatusMapper.toLiftingStatus(result.get().getEstadoLevantamiento()));
			
			//TODO mirar si se tiene que hacer join con PeticionInformacion para utilizar ControlFichero
			ControlFichero controlFichero = new ControlFichero();
			controlFichero.setCodControlFichero(codeFileControl);
			
			LevantamientoTraba levantamiento = new LevantamientoTraba();
			levantamiento.setCodLevantamiento(codeLifting);
			
			List<CuentaLevantamiento> cuentasLevantamiento = liftingBankAccountRepository.findByLevantamientoTraba(levantamiento);
			
			Double importeLevantado = (double) 0;
			
			for (CuentaLevantamiento cuentaLevantamiento : cuentasLevantamiento) {
				
				BankAccountLiftingDTO bankAccountDTO = bankAccountLiftingMapper.toBankAccountLiftingDTO(cuentaLevantamiento);
				bankAccountDTO.setStatus(liftingStatusMapper.toLiftingStatus(cuentaLevantamiento.getEstadoLevantamiento()));
				bankAccountList.add(bankAccountDTO);
			}
			
			response.setAccounts(bankAccountList);
		}
		
		return response;
	}

	@Override
	public boolean saveLifting(Long codeFileControl, Long codelifting, LiftingDTO lifting, String userModif) throws Exception {
		boolean response = true; 
		
		if (lifting != null) {
			LevantamientoTraba levantamiento = new LevantamientoTraba();
			levantamiento.setCodLevantamiento(codelifting);
			
			if (lifting.getAccounts() != null) {
				List<BankAccountLiftingDTO> list = lifting.getAccounts();
				for (BankAccountLiftingDTO account : list) {
					CuentaLevantamiento cuenta = bankAccountLiftingMapper.toCuentaLevantamiento(account);
					
					cuenta.setEstadoLevantamiento(liftingStatusMapper.toEstadoLevantamiento(account.getStatus()));
					cuenta.setLevantamientoTraba(levantamiento);
					cuenta.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
					cuenta.setUsuarioUltModificacion(userModif);
					
					liftingBankAccountRepository.save(cuenta);
				}
			}
		} else {
			response = false;
		}
		
		return response;
	}

	@Override
	public List<LiftingAuditDTO> getAuditByCodeLiftingCase(Long codeLiftingCase) {
		/*List<HLevantamientoTraba> hLevantamientoTrabaList = liftingAuditRepository.findByCodLevantamiento(codeLiftingCase);
		
		List<LiftingAuditDTO> liftingList = new ArrayList<>();
		for(HLevantamientoTraba hLevantamientoTraba : hLevantamientoTrabaList) {
			LiftingAuditDTO lifting = liftingAuditMapper.toInformationPetitionDTO(hLevantamientoTraba);
		
			liftingList.add(lifting);
		}
		
		return liftingList;*/
		
		return null;
	}

	@Override
	public List<LiftingStatusDTO> getListStatus() {
		List<LiftingStatusDTO> response = null;
		List<EstadoLevantamiento> list = null;
		
		list = liftingStatusRepository.findAll();
		
		if (list != null && list.size() > 0) {
			response = new ArrayList<LiftingStatusDTO>();
			
			for (EstadoLevantamiento estado : list) {
				response.add(liftingStatusMapper.toLiftingStatus(estado));
			}
		}
		
		return response;
	}

	@Override
	public boolean changeStatus(Long codeLifting, Long status, String userName) throws Exception {
		boolean response = true;
		try {
			if (status != null && codeLifting != null && codeLifting > 0) {
				liftingRepository.updateStatus(codeLifting, new BigDecimal(status), userName, ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
			} else {
				response = false;
			}
		} catch(Exception e) {
			throw new Exception();
		}
		
		return response;
	}

	@Override
	public void updateLiftingBankAccountingStatus(CuentaLevantamiento cuenta, long codEstado, String userName) {
		
		EstadoLevantamiento estado = new EstadoLevantamiento();
		estado.setCodEstado(codEstado);
		cuenta.setEstadoLevantamiento(estado);

		cuenta.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
		cuenta.setUsuarioUltModificacion(userName);
		
		liftingBankAccountRepository.save(cuenta);
	}

	@Override
	public byte[] generarResumenLevantamientoF5(Integer cod_file_control) throws Exception {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		try (Connection conn_embargos = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource resumenLevantamiento = ResourcesUtil.getFromJasperFolder("f5_seizureLifting.jasper");
			Resource headerResource = ResourcesUtil.getReportHeaderResource();
			Resource imageResource = ResourcesUtil.getImageLogoCommerceResource();

			File image = imageResource.getFile();

			InputStream subReportHeaderInputStream = headerResource.getInputStream();

			JasperReport subReportHeader = (JasperReport) JRLoader.loadObject(subReportHeaderInputStream);

			parameters.put("sub_img_param", image.toString());
			parameters.put("SUBREPORT_HEADER", subReportHeader);
			parameters.put("COD_FILE_CONTROL", cod_file_control);

			InputStream finalEmbargosIS = resumenLevantamiento.getInputStream();
			JasperPrint fillReport = JasperFillManager.fillReport(finalEmbargosIS, parameters, conn_embargos);

			List<JRPrintPage> pages = fillReport.getPages();

			if (pages.size() == 0)
				return null;

			return JasperExportManager.exportReportToPdf(fillReport);
		} catch (Exception e) {
			throw new Exception("DB exception while generating the report", e);
		}
	}

	@Override
	public byte[] generateLiftingLetter(Integer idLifting) throws Exception {
		LOG.info("SeizureServiceImpl - generateLevantamientoReport - start");
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource embargosJrxml = ResourcesUtil.getFromJasperFolder("liftingLetter.jasper");
			Resource logoImage = ResourcesUtil.getImageLogoCommerceResource();

			File image = logoImage.getFile();

			parameters.put("COD_LEVANTAMIENTO", idLifting);
			parameters.put("IMAGE_PARAM", image.toString());

			InputStream justificanteInputStream = embargosJrxml.getInputStream();

			JasperPrint fillReport = JasperFillManager.fillReport(justificanteInputStream, parameters, conn);

			List<JRPrintPage> pages = fillReport.getPages();

			if (pages.size() == 0)
				return null;

			LOG.info("SeizureServiceImpl - generateLevantamientoReport - end");

			return JasperExportManager.exportReportToPdf(fillReport);

		} catch (Exception e) {
			throw new Exception("DB exception while generating the report", e);
		}
	}


	@Override
	public void updateLiftingtatus(LevantamientoTraba levantamientoTraba, long codEstado, String userName) {
		EstadoLevantamiento estado = new EstadoLevantamiento();
		estado.setCodEstado(codEstado);
		levantamientoTraba.setEstadoLevantamiento(estado);

		levantamientoTraba.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
		levantamientoTraba.setUsuarioUltModificacion(userName);

		liftingRepository.save(levantamientoTraba);
	}
}
