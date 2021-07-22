package es.commerzbank.ice.embargos.service.impl;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import es.commerzbank.ice.comun.lib.service.AccountingNoteService;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.typeutils.ICEDateUtils;
import es.commerzbank.ice.comun.lib.util.SQLUtils;
import es.commerzbank.ice.comun.lib.util.jasper.ReportHelper;
import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.datawarehouse.service.AccountService;
import es.commerzbank.ice.embargos.config.OracleDataSourceEmbargosConfig;
import es.commerzbank.ice.embargos.domain.dto.*;
import es.commerzbank.ice.embargos.domain.entity.*;
import es.commerzbank.ice.embargos.domain.mapper.*;
import es.commerzbank.ice.embargos.repository.*;
import es.commerzbank.ice.embargos.service.AccountingService;
import es.commerzbank.ice.embargos.service.CustomerService;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.LiftingService;
import es.commerzbank.ice.embargos.service.files.AEATManualLiftingService;
import es.commerzbank.ice.embargos.service.files.Cuaderno63ManualLiftingService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.ResourcesUtil;
import net.sf.jasperreports.engine.*;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.*;

@Service
@Transactional(transactionManager = "transactionManager")
public class LiftingServiceImpl
		implements LiftingService
{

	private static final Logger LOG = LoggerFactory.getLogger(LiftingServiceImpl.class);
	
	@Autowired
	private LiftingMapper liftingMapper;

	@Autowired
	private LiftingRepository liftingRepository;

	@Autowired
	private LiftingBankAccountRepository liftingBankAccountRepository;
	
	@Autowired
	private BankAccountLiftingMapper bankAccountLiftingMapper;
	
	@Autowired
	private LiftingStatusRepository liftingStatusRepository;
	
	@Autowired
	private LiftingStatusMapper liftingStatusMapper;

	@Autowired
	private OracleDataSourceEmbargosConfig oracleDataSourceEmbargos;

	@Autowired
	private GeneralParametersService generalParametersService;
	
    @Autowired
    private FileControlMapper fileControlMapper;
    
    @Autowired
    private FileControlRepository fileControlRepository;
    
    @Autowired
    private SeizureRepository seizureRepository;
    
    @Autowired
    private Cuaderno63Mapper cuaderno63Mapper;
    
    @Autowired
    private SeizedRepository seizedRepository;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private AccountingService accountingService;
        
    @Autowired
    private OrderingEntityRepository orderingEntityRepository;

	@Autowired
	private CommunicatingEntityRepository communicatingEntityRepository;
    
    @Autowired
	private AccountingNoteService accountingNoteService;

	@Autowired
	AccountService accountService;

	@Autowired
	private ReportHelper reportHelper;

	@Autowired
	private FileControlService fileControlService;

	@Autowired
	private AEATManualLiftingService aeatManualLiftingService;

	@Autowired
	private Cuaderno63ManualLiftingService cuaderno63ManualLiftingService;
    
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
			
			//Double importeLevantado = (double) 0;
			
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
		boolean response = false;

		if (status != null && codeLifting != null && codeLifting > 0) {
			Optional<LevantamientoTraba> levantamientoOpt = liftingRepository.findById(codeLifting);
			if (levantamientoOpt.isPresent()) {
				LevantamientoTraba levantamiento = levantamientoOpt.get();
				levantamiento.setUsuarioUltModificacion(userName);
				levantamiento.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
				
				EstadoLevantamiento estadoLevantamiento = new EstadoLevantamiento();
				estadoLevantamiento.setCodEstado(status);
				levantamiento.setEstadoLevantamiento(estadoLevantamiento);
				
				liftingRepository.save(levantamiento);

				//4. Si todos los levantamientos del fichero están contabilziados, avanzar el estado del fichero

				ControlFichero controlFichero = levantamiento.getControlFichero();

				boolean allLevantamientosContabilizados = true;

				for (LevantamientoTraba currentLevantamientoTraba : controlFichero.getLevantamientoTrabas()) {
					if (currentLevantamientoTraba.getEstadoLevantamiento().getCodEstado() != EmbargosConstants.COD_ESTADO_LEVANTAMIENTO_CONTABILIZADO)
					{
						allLevantamientosContabilizados = false;
						break;
					}
				}

				if (allLevantamientosContabilizados)
				{
					Log.info("ControlFichero con id " + controlFichero.getCodControlFichero() + " cambia a estado 'Contabilizado'");

					Long estado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_ACCOUNTED;

					fileControlService.updateFileControlStatus(levantamiento.getControlFichero().getCodControlFichero(), estado,
							userName, "LEVANTAMIENTOS");
				}

				response = true;
			}
			
			//liftingRepository.updateStatus(codeLifting, new BigDecimal(status), userName, ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
		}

		return response;
	}

	@Override
	public boolean updateAccountLiftingStatus(Long idAccount, AccountStatusLiftingDTO accountStatusLifting,
			String userModif) throws Exception {
		
		//Optional<LevantamientoTraba> levantamientoOpt = liftingRepository.findById(codeLifting);
		Optional<CuentaLevantamiento> cuentaLevantamientoOpt = liftingBankAccountRepository.findById(idAccount);

		if (!cuentaLevantamientoOpt.isPresent()) {
			return false;
		}
		
		if (accountStatusLifting != null && accountStatusLifting.getCode() != null) {

			//LevantamientoTraba levantamiento = levantamientoOpt.get();
			CuentaLevantamiento cuentaLevantamiento = cuentaLevantamientoOpt.get();

			// Actualizar estado:
			EstadoLevantamiento estadoLevantamiento = new EstadoLevantamiento();
			estadoLevantamiento.setCodEstado(Long.valueOf(accountStatusLifting.getCode()));
			cuentaLevantamiento.setEstadoLevantamiento(estadoLevantamiento);

			// Usuario y fecha ultima modificacion de la Traba:
			BigDecimal fechaActualBigDec = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
			cuentaLevantamiento.setUsuarioUltModificacion(userModif);
			cuentaLevantamiento.setFUltimaModificacion(fechaActualBigDec);

			liftingBankAccountRepository.save(cuentaLevantamiento);

			// Si todas las cuentas levantamiento del levantamiento están contabilizadas, avanzar el estado del levantamiento
			LevantamientoTraba levantamientoTraba = cuentaLevantamiento.getLevantamientoTraba();
			boolean isAllCuentaLevantamientoContabilizados = true;
			for(CuentaLevantamiento currentCuentaLevantamiento : levantamientoTraba.getCuentaLevantamientos()) {
				if (currentCuentaLevantamiento.getEstadoLevantamiento().getCodEstado() != EmbargosConstants.COD_ESTADO_LEVANTAMIENTO_CONTABILIZADO) {
					isAllCuentaLevantamientoContabilizados = false;
					break;
				}
			}

			if (isAllCuentaLevantamientoContabilizados) {
				Log.info("Actualizando el levantamiento " + levantamientoTraba.getCodLevantamiento() + " a estado 'Contabilizado'");
				changeStatus(levantamientoTraba.getCodLevantamiento(), EmbargosConstants.COD_ESTADO_LEVANTAMIENTO_CONTABILIZADO, userModif);
			}
		} else {
			return false;
		}

		return true;
	}

	@Override
	public byte[] previewContable(Long codeFileControl)
			throws Exception
	{
		HashMap<String, Object> parameters = new HashMap<>();

		try (Connection conn_embargos = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource informe = ResourcesUtil.getFromJasperFolder("precontable.jasper");
			Resource imageLogo = ResourcesUtil.getImageLogoCommerceResource();

			parameters.put("img_param", imageLogo.getFile().toString());

			parameters.put("CUENTA_TRABA_EXPRESSION", SQLUtils.calcInExpression(new ArrayList<>(), "ct.cod_cuenta_traba"));

			List<Long> pendientes = accountingService.levantamientoListaAContabilizar(codeFileControl);

			parameters.put("CUENTA_LEVANTAMIENTO_EXPRESSION", SQLUtils.calcInExpression(pendientes, "cl.COD_CUENTA_LEVANTAMIENTO"));
			parameters.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));

			InputStream isInforme = informe.getInputStream();
			JasperPrint reporteLleno =  JasperFillManager.fillReport(isInforme, parameters, conn_embargos);
			return JasperExportManager.exportReportToPdf(reporteLleno);
		} catch (Exception e) {
			throw new Exception("DB exception while generating the report", e);
		}
	}
	/*
	@Override
	public void updateLiftingBankAccountingStatus(CuentaLevantamiento cuenta, long codEstado, String userName) {
		
		EstadoLevantamiento estado = new EstadoLevantamiento();
		estado.setCodEstado(codEstado);
		cuenta.setEstadoLevantamiento(estado);

		cuenta.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
		cuenta.setUsuarioUltModificacion(userName);
		
		liftingBankAccountRepository.save(cuenta);
	}
	*/

	@Override
	public byte[] generarResumenLevantamientoF5(Integer codControlFichero, String oficina) throws Exception {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		try (Connection conn_embargos = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource resumenLevantamiento = ResourcesUtil.getFromJasperFolder("F5_levantamiento.jasper");

			Resource logoRes = ResourcesUtil.getImageLogoCommerceResource();

			parameters.put("img_param", logoRes.getFile().toString());
			parameters.put("COD_FILE_CONTROL", codControlFichero);
			parameters.put("NOMBRE_SUCURSAL", oficina);

			parameters.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));

			InputStream resumenInputStream = resumenLevantamiento.getInputStream();

			JasperPrint fillReport = JasperFillManager.fillReport(resumenInputStream, parameters, conn_embargos);

			List<JRPrintPage> pages = fillReport.getPages();

			if (pages.size() == 0)
				return null;

			return JasperExportManager.exportReportToPdf(fillReport);
		} catch (Exception e) {
			throw new Exception("DB exception while generating the report", e);
		}
	}

	@Override
	public byte[] generateLiftingLetter(Long idLifting) throws Exception {
		JasperPrint fillReport = reportLiftingLetterInternal(idLifting);

		if (fillReport == null)
			return null;

		List<JRPrintPage> pages = fillReport.getPages();

		if (pages.size() == 0)
			return null;

		return JasperExportManager.exportReportToPdf(fillReport);
	}

	private JasperPrint reportLiftingLetterInternal(Long idLifting) throws Exception {
		HashMap<String, Object> parameters = new HashMap<>();

		Optional<LevantamientoTraba> optLevantamiento = liftingRepository.findById(idLifting);

		if (!optLevantamiento.isPresent()) {
			throw new Exception("Levantamiento "+ idLifting +" no encontrado");
		}

		LevantamientoTraba levantamiento = optLevantamiento.get();

		try (Connection conn = oracleDataSourceEmbargos.getEmbargosConnection()) {

			ControlFichero controlFichero = levantamiento.getControlFichero();
			EntidadesComunicadora entidadesComunicadora  = controlFichero.getEntidadesComunicadora();

			Resource report = ResourcesUtil.getFromJasperFolder("cartaLevantamiento.jasper");
			Resource logoRes = es.commerzbank.ice.comun.lib.util.jasper.ResourcesUtil.getImageLogoCommerceResource();

			CustomerDTO customer = accountService.getCustomerByNIF(levantamiento.getTraba().getEmbargo().getNif());

			if (customer != null) { // se permite un preview aunque no haya la dirección
				parameters.put("nombre_titular", customer.getName());
				parameters.put("addres_titular", customer.getAddress());
				parameters.put("codigo_postal_titular", customer.getPostalCode());
				parameters.put("ciudad_titular", customer.getCity());
			}

			parameters.put("CODIGO", idLifting);
			parameters.put("ENTIDAD", entidadesComunicadora.getDesEntidad());
			parameters.put("logo_image", logoRes.getFile().toString());

			parameters.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));

			InputStream justificanteInputStream = report.getInputStream();

			return JasperFillManager.fillReport(justificanteInputStream, parameters, conn);

		} catch (Exception e) {

			throw new Exception("DB exception while generating the report", e);
		}
	}

	@Override
	public void generateLiftingLetters(ControlFichero controlFichero) throws Exception {
		List<LevantamientoTraba> liftings = liftingRepository.findAllByControlFichero(controlFichero);

		if (liftings != null && liftings.size() > 0)
		{
			File temporaryFile = reportHelper.getTemporaryFile("cartas-levantamiento-"+ controlFichero.getCodControlFichero(), ReportHelper.PDF_EXTENSION);
			PdfDocument outDoc = new PdfDocument(new PdfWriter(temporaryFile));

			int pageCount = 1;

			for (LevantamientoTraba levantamiento : liftings)
			{
				JasperPrint filledReport = reportLiftingLetterInternal(levantamiento.getCodLevantamiento());

				if (filledReport != null) {
					reportHelper.dumpReport(outDoc, filledReport, pageCount);
					pageCount++;
				}
			}

			LOG.info("Se han acumulado "+ outDoc.getNumberOfPages() +" páginas de cartas a enviar");

			try {
				// Se prefiere el catch a numPages > 0 por si el outDoc debe igualmente hacer alguna acción en el close.
				outDoc.close();

				reportHelper.moveToPrintFolder(temporaryFile);
			} catch (PdfException e) {
				if ("Document has no pages.".equals(e.getMessage())) ;
				else throw e;
			}
		}
	}

	@Override
	@Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
	public String manualLifting(LiftingManualDTO liftingManualDTO, String userModif) throws Exception
	{
		Optional<EntidadesComunicadora> comunicatingEntity = communicatingEntityRepository.findById(liftingManualDTO.getEntity().getCodCommunicatingEntity());

		if (!comunicatingEntity.isPresent()) {
			throw new Exception("Entidad comunicadora "+ liftingManualDTO.getEntity().getCodCommunicatingEntity() +" no encontrada");
		}

		// Se recibe una lista de levantamientos, puede contener nifs repetidos. se reagrupan.
		Map<String, List<ClientLiftingManualDTO>> ordenesPorCliente = new HashMap<>();

		if (liftingManualDTO!=null && liftingManualDTO.getClients()!=null) {
			for (ClientLiftingManualDTO clientDTO : liftingManualDTO.getClients()) {

				List<ClientLiftingManualDTO> ordenes = ordenesPorCliente.get(clientDTO.getNif());

				if (ordenes == null) {
					ordenes = new ArrayList<>();
					ordenesPorCliente.put(clientDTO.getNif(), ordenes);
				}

				ordenes.add(clientDTO);
			}
		}

		if (ordenesPorCliente.size() == 0)
			throw new Exception("No orders were found");

		if (liftingManualDTO.getEntity().isIndAeat()) {
			return aeatManualLiftingService.crearFicheroLevantamientos(comunicatingEntity.get(), ordenesPorCliente);
		}
		else if (liftingManualDTO.getEntity().isIndNorma63()) {
			return cuaderno63ManualLiftingService.crearFicheroLevantamientos(comunicatingEntity.get(), ordenesPorCliente);
		}
		else {
			throw new Exception("Cannot process this entity type");
		}
	}
}
