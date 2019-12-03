package es.commerzbank.ice.embargos.service.impl;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.comun.lib.service.AccountingNoteService;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.typeutils.ICEDateUtils;
import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.config.OracleDataSourceEmbargosConfig;
import es.commerzbank.ice.embargos.domain.dto.BankAccountLiftingDTO;
import es.commerzbank.ice.embargos.domain.dto.ClientLiftingManualDTO;
import es.commerzbank.ice.embargos.domain.dto.LiftingAuditDTO;
import es.commerzbank.ice.embargos.domain.dto.LiftingDTO;
import es.commerzbank.ice.embargos.domain.dto.LiftingManualDTO;
import es.commerzbank.ice.embargos.domain.dto.LiftingStatusDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.CuentaLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import es.commerzbank.ice.embargos.domain.entity.EntidadesOrdenante;
import es.commerzbank.ice.embargos.domain.entity.EstadoCtrlfichero;
import es.commerzbank.ice.embargos.domain.entity.EstadoLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.LevantamientoTraba;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.embargos.domain.mapper.BankAccountLiftingMapper;
import es.commerzbank.ice.embargos.domain.mapper.Cuaderno63Mapper;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.domain.mapper.LiftingMapper;
import es.commerzbank.ice.embargos.domain.mapper.LiftingStatusMapper;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase5.OrdenLevantamientoRetencionFase5;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.LiftingBankAccountRepository;
import es.commerzbank.ice.embargos.repository.LiftingRepository;
import es.commerzbank.ice.embargos.repository.LiftingStatusRepository;
import es.commerzbank.ice.embargos.repository.OrderingEntityRepository;
import es.commerzbank.ice.embargos.repository.SeizedRepository;
import es.commerzbank.ice.embargos.repository.SeizureRepository;
import es.commerzbank.ice.embargos.service.AccountingService;
import es.commerzbank.ice.embargos.service.ClientDataService;
import es.commerzbank.ice.embargos.service.CustomerService;
import es.commerzbank.ice.embargos.service.LiftingService;
import es.commerzbank.ice.utils.EmbargosConstants;
import es.commerzbank.ice.utils.EmbargosUtils;
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
    private ClientDataService clientDataService;
    
    @Autowired
    private AccountingService accountingService;
        
    @Autowired
    private OrderingEntityRepository orderingEntityRepository;
    
    @Autowired
	private AccountingNoteService accountingNoteService;
    
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

	@Override
	@Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
	public boolean manualLifting(LiftingManualDTO liftingManualDTO, String userModif) throws Exception {
		
        es.commerzbank.ice.comun.lib.domain.entity.ControlFichero controlFichero = null;
        
        try {
            BigDecimal importeMaximoAutomaticoDivisa =
                    generalParametersService.loadBigDecimalParameter(EmbargosConstants.PARAMETRO_EMBARGOS_LEVANTAMIENTO_IMPORTE_MAXIMO_AUTOMATICO_DIVISA);

    		boolean allLevantamientosContabilizados = true;
            // almacena las cuentas que se han contabilizado, para su actualización posterior de estado.
            //List<CuentaLevantamiento> cuentasAContabilizar = new ArrayList<>();

            // Agrupa por NIF cada orden de levantamiento generado
            Map<String, OrdenLevantamientoRetencionFase5> mapOrdenLev = new HashMap<String, OrdenLevantamientoRetencionFase5>();
            if (liftingManualDTO!=null && liftingManualDTO.getClients()!=null) {
            	for (ClientLiftingManualDTO clientDTO: liftingManualDTO.getClients()) {
            		
            		if (mapOrdenLev.get(clientDTO.getNif())==null) {
            			OrdenLevantamientoRetencionFase5 ordenLev = new OrdenLevantamientoRetencionFase5();
            			ordenLev.setNifDeudor(clientDTO.getNif());
            			ordenLev.setNombreDeudor(clientDTO.getDebtor());
            			ordenLev.setIdentificadorDeuda(clientDTO.getCodLifting());
            			ordenLev.setIbanCuenta1(clientDTO.getIban());
            			ordenLev.setImporteALevantarIban1(new BigDecimal(clientDTO.getAmount().replace(',', '.')));
            			ordenLev.setFechaEjecucionRetenciones(clientDTO.getDateRetention());
            			mapOrdenLev.put(clientDTO.getNif(), ordenLev);
            		}
            		else {
            			OrdenLevantamientoRetencionFase5 ordenLev = mapOrdenLev.get(clientDTO.getNif());
            			if (ordenLev.getIbanCuenta2()==null || ordenLev.getIbanCuenta2().length()==0) {
            				ordenLev.setIbanCuenta2(clientDTO.getIban());
                			ordenLev.setImporteALevantarIban2(new BigDecimal(clientDTO.getAmount().replace(',', '.')));
            			}
            			else if (ordenLev.getIbanCuenta3()==null || ordenLev.getIbanCuenta3().length()==0) {
            				ordenLev.setIbanCuenta3(clientDTO.getIban());
                			ordenLev.setImporteALevantarIban3(new BigDecimal(clientDTO.getAmount().replace(',', '.')));
            			}
            			else if (ordenLev.getIbanCuenta4()==null || ordenLev.getIbanCuenta4().length()==0) {
            				ordenLev.setIbanCuenta4(clientDTO.getIban());
                			ordenLev.setImporteALevantarIban4(new BigDecimal(clientDTO.getAmount().replace(',', '.')));
            			}
            			else if (ordenLev.getIbanCuenta5()==null || ordenLev.getIbanCuenta5().length()==0) {
            				ordenLev.setIbanCuenta5(clientDTO.getIban());
                			ordenLev.setImporteALevantarIban5(new BigDecimal(clientDTO.getAmount().replace(',', '.')));
            			}
            			else if (ordenLev.getIbanCuenta6()==null || ordenLev.getIbanCuenta6().length()==0) {
            				ordenLev.setIbanCuenta6(clientDTO.getIban());
                			ordenLev.setImporteALevantarIban6(new BigDecimal(clientDTO.getAmount().replace(',', '.')));
            			}
            			mapOrdenLev.put(clientDTO.getNif(), ordenLev);
            		}
            	}
            }
            
            if (mapOrdenLev.size()>0) {
            	
            	String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
                
                // Inicializar control fichero
                ControlFichero controlFicheroLevantamiento =
                        fileControlMapper.generateControlFichero(null, EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_NORMA63, EmbargosConstants.USER_MANUAL+"_"+date, null);

                EntidadesOrdenante entidadOrdenante = orderingEntityRepository.findByNifEntidad(liftingManualDTO.getNif());
                if (entidadOrdenante!=null) controlFicheroLevantamiento.setEntidadesComunicadora(entidadOrdenante.getEntidadesComunicadora());
                
                controlFicheroLevantamiento.setUsuarioUltModificacion(userModif);
                controlFicheroLevantamiento.setDescripcion(EmbargosConstants.USER_MANUAL);
                fileControlRepository.save(controlFicheroLevantamiento);
            	
            	for (OrdenLevantamientoRetencionFase5 ordenLev : mapOrdenLev.values()) {
            		
                    List<Embargo> embargos = seizureRepository.findAllByNumeroEmbargo(ordenLev.getIdentificadorDeuda());

                    if (embargos == null || embargos.size() == 0)
                    {
                        LOG.info("No embargo found for "+ ordenLev.getIdentificadorDeuda());
                        // TODO ERROR
                        continue;
                    }

                    Embargo embargo = EmbargosUtils.selectEmbargo(embargos);

                    Traba traba = seizedRepository.getByEmbargo(embargo);

                    if (traba == null)
                    {
                        LOG.error("Levantamiento not found for embargo "+ embargo.getCodEmbargo() +" code "+ ordenLev.getIdentificadorDeuda());
                        continue;
                    }

                    LOG.info("Using traba "+ traba.getCodTraba() +" for embargo "+ embargo.getCodEmbargo() +" code "+ ordenLev.getIdentificadorDeuda());

                    // recuperar account <- razon interna
                    // recuperar cod traba
                    // estado contable?
                    // estado ejecutado?
                    CustomerDTO customerDTO = customerService.findCustomerByNif(ordenLev.getNifDeudor(), false);

                    //Se guardan los datos del cliente:
	        		clientDataService.createUpdateClientDataTransaction(customerDTO, ordenLev.getNifDeudor());
                    
                    LevantamientoTraba levantamiento = cuaderno63Mapper.generateLevantamiento(controlFicheroLevantamiento.getCodControlFichero(), ordenLev, traba, customerDTO);

                    liftingRepository.save(levantamiento);

                    boolean allCuentasLevantamientoContabilizados = true;

                    for (CuentaLevantamiento cuentaLevantamiento : levantamiento.getCuentaLevantamientos())
                    {
                        if (!EmbargosConstants.ISO_MONEDA_EUR.equals(cuentaLevantamiento.getCodDivisa()) &&
                                importeMaximoAutomaticoDivisa.compareTo(cuentaLevantamiento.getImporte()) < 0) {
                            allLevantamientosContabilizados = false;
                            allCuentasLevantamientoContabilizados = false;
                            LOG.info("Cannot perform an automatic seizure lifting: "+ cuentaLevantamiento.getCodDivisa() +" and "+ cuentaLevantamiento.getImporte().toPlainString());
                        }
                        else
                        {
                            LOG.info("Doing an automatic seizure lifting.");

                            EstadoLevantamiento estadoLevantamiento = new EstadoLevantamiento();
                            estadoLevantamiento.setCodEstado(EmbargosConstants.COD_ESTADO_LEVANTAMIENTO_PENDIENTE_RESPUESTA_CONTABILIZACION);
                            cuentaLevantamiento.setEstadoLevantamiento(estadoLevantamiento);
                        }

                        liftingBankAccountRepository.save(cuentaLevantamiento);

                        controlFichero = accountingService.sendAccountingLiftingBankAccount(cuentaLevantamiento, embargo, userModif);
                    }

                    if (allCuentasLevantamientoContabilizados) {
                        EstadoLevantamiento estadoLevantamiento = new EstadoLevantamiento();
                        estadoLevantamiento.setCodEstado(EmbargosConstants.COD_ESTADO_LEVANTAMIENTO_PENDIENTE_RESPUESTA_CONTABILIZACION);
                        levantamiento.setEstadoLevantamiento(estadoLevantamiento);
                        levantamiento.setIndCasoRevisado(EmbargosConstants.IND_FLAG_YES);
                        liftingRepository.save(levantamiento);
                    }
            	}
            	
            	
                // cerrar y enviar la contabilización
                if (allLevantamientosContabilizados && controlFichero!=null) {
                	accountingNoteService.generacionFicheroContabilidad(controlFichero);
                }

                // Actualizar control fichero
                EstadoCtrlfichero estadoCtrlfichero = null;

                if (allLevantamientosContabilizados) {
                    estadoCtrlfichero = new EstadoCtrlfichero(
                            EmbargosConstants.COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_PENDING_ACCOUNTING_RESPONSE,
                            EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_NORMA63);
                    
                    //Cambio del indProcesado a 'S' al cambiar al estado 'Pendiente de respuesta contable':
                    controlFicheroLevantamiento.setIndProcesado(EmbargosConstants.IND_FLAG_SI);
                }
                else
                {
                    estadoCtrlfichero = new EstadoCtrlfichero(
                            EmbargosConstants.COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_RECEIVED,
                            EmbargosConstants.COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_NORMA63);
                }

                controlFicheroLevantamiento.setEstadoCtrlfichero(estadoCtrlfichero);

                fileControlRepository.save(controlFicheroLevantamiento);
            }
        }
        catch (Exception e)
        {
            LOG.error("Error while treating NORMA63 LEV manual", e);
            throw e;
        }
		
		return true;
	}
}
