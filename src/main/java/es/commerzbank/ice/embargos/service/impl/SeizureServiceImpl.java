package es.commerzbank.ice.embargos.service.impl;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.comun.lib.domain.dto.AccountingNote;
import es.commerzbank.ice.comun.lib.domain.dto.GeneralParameter;
import es.commerzbank.ice.comun.lib.formats.contabilidad.RespuestaContabilidad;
import es.commerzbank.ice.comun.lib.service.AccountingNoteService;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.config.OracleDataSourceEmbargosConfig;
import es.commerzbank.ice.embargos.domain.dto.SeizedBankAccountDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureActionDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureDTO;
import es.commerzbank.ice.embargos.domain.dto.SeizureStatusDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.CuentaTrabaActuacion;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import es.commerzbank.ice.embargos.domain.entity.EstadoTraba;
import es.commerzbank.ice.embargos.domain.entity.HCuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.HTraba;
import es.commerzbank.ice.embargos.domain.entity.Peticion;
import es.commerzbank.ice.embargos.domain.entity.SolicitudesEjecucion;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.embargos.domain.mapper.HSeizedBankAccountMapper;
import es.commerzbank.ice.embargos.domain.mapper.HSeizureMapper;
import es.commerzbank.ice.embargos.domain.mapper.SeizedBankAccountActionMapper;
import es.commerzbank.ice.embargos.domain.mapper.SeizedBankAccountMapper;
import es.commerzbank.ice.embargos.domain.mapper.SeizureMapper;
import es.commerzbank.ice.embargos.domain.mapper.SeizureStatusMapper;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.HSeizedBankAccountRepository;
import es.commerzbank.ice.embargos.repository.HSeizedRepository;
import es.commerzbank.ice.embargos.repository.SeizedBankAccountActionRepository;
import es.commerzbank.ice.embargos.repository.SeizedBankAccountRepository;
import es.commerzbank.ice.embargos.repository.SeizedRepository;
import es.commerzbank.ice.embargos.repository.SeizureRepository;
import es.commerzbank.ice.embargos.repository.SeizureStatusRepository;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.SeizureService;
import es.commerzbank.ice.utils.EmbargosConstants;
import es.commerzbank.ice.utils.EmbargosUtils;
import es.commerzbank.ice.utils.ICEDateUtils;
import es.commerzbank.ice.utils.ResourcesUtil;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
@Transactional(transactionManager="transactionManager")
public class SeizureServiceImpl implements SeizureService {
	
	private static final Logger logger = LoggerFactory.getLogger(SeizureServiceImpl.class);

	@Autowired
	private SeizureMapper seizureMapper;
	
	@Autowired
	private HSeizureMapper hSeizureMapper;
	
	@Autowired
	private SeizedBankAccountMapper seizedBankAccountMapper;
	
	@Autowired
	private HSeizedBankAccountMapper hSeizedBankAccountMapper;
	
	@Autowired
	private SeizedBankAccountActionMapper seizedBankAccountActionMapper;
	
	@Autowired
	private SeizureStatusMapper seizureStatusMapper;
	
	@Autowired
	private FileControlService fileControlService;
	
	@Autowired
	GeneralParametersService generalParametersService;
	
	@Autowired
	private AccountingNoteService accountingNoteService;
	
	@Autowired
	private SeizureRepository seizureRepository;
	
	@Autowired
	private SeizedRepository seizedRepository;
	
	@Autowired
	private HSeizedRepository hSeizedRepository;
	
	@Autowired
	private SeizedBankAccountRepository seizedBankAccountRepository;
	
	@Autowired
	private HSeizedBankAccountRepository hSeizedBankAccountRepository;

	@Autowired
	private SeizedBankAccountActionRepository seizedBankAccountActionRepository;
	
	@Autowired
	private SeizureStatusRepository seizureStatusRepository;
	
	@Autowired
	private FileControlRepository fileControlRepository;
	
	@Autowired
	private OracleDataSourceEmbargosConfig oracleDataSourceEmbargos;
	
	@Override
	public List<SeizureDTO> getSeizureListByCodeFileControl(Long codeFileControl) {
		logger.info("SeizureServiceImpl - getSeizureListByCodeFileControl - start");
		
		List<SeizureDTO> seizureDTOList = new ArrayList<>();
		
		ControlFichero controlFichero = new ControlFichero();
		controlFichero.setCodControlFichero(codeFileControl);
		
		List<Embargo> embargoList = seizureRepository.findAllByControlFichero(controlFichero);
		
		for (Embargo embargo : embargoList) {
			
			SeizureDTO seizureDTO = seizureMapper.toSeizureDTO(embargo);
			seizureDTOList.add(seizureDTO);
		}
		
		logger.info("SeizureServiceImpl - getSeizureListByCodeFileControl - end");
		return seizureDTOList;
	}
	
	@Override
	public SeizureDTO getSeizureById(Long idSeizure) {
		logger.info("SeizureServiceImpl - getSeizureById - start");
		SeizureDTO seizureDTO = null;
		
		//Optional<Embargo> embargoOpt = seizureRepository.findById(idSeizure);
		
		//if(embargoOpt.isPresent()) {
			
		//	seizureDTO = seizureMapper.toSeizureDTO(embargoOpt.get());
		//}
		
		Optional<Traba> trabaOpt = seizedRepository.findById(idSeizure);
		
		if(trabaOpt.isPresent()) {
			
			seizureDTO = seizureMapper.toSeizureDTO(trabaOpt.get().getEmbargo());
		}
		
		logger.info("SeizureServiceImpl - getSeizureById - end");
		return seizureDTO;
	}

	@Override
	public List<SeizedBankAccountDTO> getBankAccountListBySeizure(Long codeFileControl,
			Long idSeizure) {
		logger.info("SeizureServiceImpl - getBankAccountListBySeizure - start");
		List<SeizedBankAccountDTO> seizedBankAccountDTOList = new ArrayList<>();
		
		Traba traba = new Traba();
		traba.setCodTraba(idSeizure);
		
		List<CuentaTraba> cuentaTrabaList = seizedBankAccountRepository.findAllByTrabaOrderByNumeroOrdenCuentaAsc(traba);
		
		for(CuentaTraba cuentaTraba : cuentaTrabaList) {
			
			SeizedBankAccountDTO seizedBankAccountDTO = seizedBankAccountMapper.toSeizedBankAccountDTO(cuentaTraba);
			seizedBankAccountDTOList.add(seizedBankAccountDTO);
		}
		
		logger.info("SeizureServiceImpl - getBankAccountListBySeizure - end");
		return seizedBankAccountDTOList;
	}
	
	
	@Override
	public List<SeizureActionDTO> getSeizureActions() {		
		logger.info("SeizureServiceImpl - getSeizureActions - start");
		List<SeizureActionDTO> seizureActionDTOList = new ArrayList<>();
				
		List<CuentaTrabaActuacion> cuentaTrabaActuacionList = seizedBankAccountActionRepository.findAll();
		
		for(CuentaTrabaActuacion cuentaTrabaActuacion : cuentaTrabaActuacionList) {
			
			SeizureActionDTO seizedBankAccountDTO = seizedBankAccountActionMapper.toSeizureActionDTO(cuentaTrabaActuacion);
			seizureActionDTOList.add(seizedBankAccountDTO);
		}
		
		logger.info("SeizureServiceImpl - getSeizureActions - end");
		return seizureActionDTOList;
	}
	
	@Override
	public List<SeizureStatusDTO> getSeizureStatusList() {
		logger.info("SeizureServiceImpl - getSeizureStatusList - start");
		List<SeizureStatusDTO> seizureStatusDTOList = new ArrayList<>();
		
		List<EstadoTraba> estadoTrabaList = seizureStatusRepository.findAllVisibleToUser();
		
		for(EstadoTraba estadoTraba : estadoTrabaList) {
			
			SeizureStatusDTO seizureStatusDTO = seizureStatusMapper.toSeizureStatusDTO(estadoTraba);
			seizureStatusDTOList.add(seizureStatusDTO);
		}
		
		logger.info("SeizureServiceImpl - getSeizureStatusList - end");
		return seizureStatusDTOList;

	}

	@Override
	public boolean updateSeizedBankAccountList(Long codeFileControl, Long idSeizure,
			List<SeizedBankAccountDTO> seizedBankAccountDTOList, String userModif) {
		logger.info("SeizureServiceImpl - updateSeizedBankAccountList - start");

		Optional<Traba> trabaOpt = seizedRepository.findById(idSeizure);
		
		if (trabaOpt.isPresent()) {
		
			//Actualizacion usuario y fecha de ultima modificacion de la traba:
			Traba traba = trabaOpt.get();
			BigDecimal fechaActualBigDec = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
			traba.setUsuarioUltModificacion(userModif);
			traba.setFUltimaModificacion(fechaActualBigDec);
			
			//Actualizar usuario y fecha ultima modificacion del Embargo (para que se inserte registro en el historico de embargos):			
			Embargo embargo = traba.getEmbargo();
			embargo.setUsuarioUltModificacion(userModif);
			embargo.setFUltimaModificacion(fechaActualBigDec);
			
			//Calculo del importe trabado: sumar los importes trabados de cada cuenta:
			BigDecimal importeTrabado = new BigDecimal(0);
			for (SeizedBankAccountDTO seizedBankAccountDTO : seizedBankAccountDTOList) {
				BigDecimal importeTrabadoBankAccount = seizedBankAccountDTO.getAmount()!=null ? seizedBankAccountDTO.getAmount() :BigDecimal.valueOf(0);
				importeTrabado = importeTrabado.add(importeTrabadoBankAccount);
			}
			traba.setImporteTrabado(importeTrabado);
			
			seizedRepository.save(traba);
		}
		
		for (SeizedBankAccountDTO seizedBankAccountDTO : seizedBankAccountDTOList) {
			
			Optional<CuentaTraba> cuentaTrabaOpt = seizedBankAccountRepository.findById(seizedBankAccountDTO.getIdSeizedBankAccount());
			
			if (cuentaTrabaOpt.isPresent()) {
				
				//Actualizar los campos informados
				CuentaTraba cuentaTraba = seizedBankAccountMapper.toCuentaTrabaForUpdate(seizedBankAccountDTO, cuentaTrabaOpt.get(), userModif);
								
				seizedBankAccountRepository.save(cuentaTraba);
				
			}
		}
		
		logger.info("SeizureServiceImpl - updateSeizedBankAccountList - end");
		return true;
	}
	
	
	@Override
	public boolean updateSeizedBankStatus(CuentaTraba cuentaTraba, Long codEstado, String userModif) {
		logger.info("SeizureServiceImpl - updateSeizedBankStatus - start");
		Traba traba = cuentaTraba.getTraba();
		
		BigDecimal fechaActualBigDec = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
		
		traba.setUsuarioUltModificacion(userModif);
		traba.setFUltimaModificacion(fechaActualBigDec);
		
		//Actualizar usuario y fecha ultima modificacion del Embargo (para que se inserte registro en el historico de embargos):			
		Embargo embargo = traba.getEmbargo();
		embargo.setUsuarioUltModificacion(userModif);
		embargo.setFUltimaModificacion(fechaActualBigDec);
		
		seizedRepository.save(traba);
		
		//Estado de la cuenta traba:
		EstadoTraba estadoTraba = new EstadoTraba();
		estadoTraba.setCodEstado(codEstado);
		
		cuentaTraba.setEstadoTraba(estadoTraba);
		cuentaTraba.setUsuarioUltModificacion(userModif);
		cuentaTraba.setFUltimaModificacion(fechaActualBigDec);
								
		seizedBankAccountRepository.save(cuentaTraba);	
		
		logger.info("SeizureServiceImpl - updateSeizedBankStatus - end");
		return true;
	}
	
	@Override
	public boolean updateSeizureStatus(Long codeFileControl, Long idSeized, SeizureStatusDTO seizureStatusDTO,
			String userModif) {
		logger.info("SeizureServiceImpl - updateSeizureStatus - start");
		Optional<Traba> trabaOpt = seizedRepository.findById(idSeized);
		
		if(!trabaOpt.isPresent()) {
			return false;
		}
		
		if(seizureStatusDTO!=null && seizureStatusDTO.getCode()!=null) {
			
			Traba traba = trabaOpt.get();
			
			//Actualizar estado:
			EstadoTraba estadoTraba = new EstadoTraba();
			estadoTraba.setCodEstado(Long.valueOf(seizureStatusDTO.getCode()));
			
			traba.setEstadoTraba(estadoTraba);
									
			//Usuario y fecha ultima modificacion de la Traba:
			BigDecimal fechaActualBigDec = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
			traba.setUsuarioUltModificacion(userModif);
			traba.setFUltimaModificacion(fechaActualBigDec);
			
			//Actualizar usuario y fecha ultima modificacion del Embargo (para que se inserte registro en el historico de embargos):			
			Embargo embargo = traba.getEmbargo();
			embargo.setUsuarioUltModificacion(userModif);
			embargo.setFUltimaModificacion(fechaActualBigDec);
			
			seizedRepository.save(traba);
		
		} else {			
			return false;
		}
		
		logger.info("SeizureServiceImpl - updateSeizureStatus - end");
		return true;
	}
	
	
	@Override
	public List<SeizureDTO> getAuditSeizure(Long codFileControl, Long idSeizure) {
		logger.info("SeizureServiceImpl - getAuditSeizure - start");
		List<SeizureDTO> seizureDTOList = new ArrayList<>();
		
		List<HTraba> hSeizureDTOList = hSeizedRepository.findByIdSeizure(idSeizure);
		
		for (HTraba hTraba : hSeizureDTOList) {
			
			SeizureDTO seizureDTO = hSeizureMapper.toSeizureDTO(hTraba.getHEmbargo());
			
			seizureDTOList.add(seizureDTO);
		}
		
		logger.info("SeizureServiceImpl - getAuditSeizure - end");
		return seizureDTOList;
	}
	
	@Override
	public List<SeizedBankAccountDTO> getAuditSeizedBankAccounts(Long codFileControl, Long idSeizure, Long codAudit) {
		logger.info("SeizureServiceImpl - getAuditSeizedBankAccounts - start");
		List<SeizedBankAccountDTO> seizureDTOList = new ArrayList<>();
		
		//Traba traba = new Traba();
		//traba.setCodTraba(idSeizure);

		//List<HCuentaTraba> hSeizedBankAccountDTOList = hSeizedBankAccountRepository.findAllByTraba(traba);
		List<HCuentaTraba> hSeizedBankAccountDTOList = hSeizedBankAccountRepository.findAllByCodTrabaAndCodAudit(BigDecimal.valueOf(idSeizure), codAudit);
		
		for (HCuentaTraba hCuentaTraba : hSeizedBankAccountDTOList) {
			
			SeizedBankAccountDTO seizedBankAccountDTO = hSeizedBankAccountMapper.toSeizedBankAccountDTO(hCuentaTraba);
			
			seizureDTOList.add(seizedBankAccountDTO);
		}
		
		logger.info("SeizureServiceImpl - getAuditSeizedBankAccounts - end");
		return seizureDTOList;
	}

	
	@Override
	public boolean sendAccounting(Long codeFileControl, String userName) throws ICEException {
		logger.info("SeizureServiceImpl - sendAccounting - start");
		//Se cambia el estado de Control Fichero a "Pendiente de contabilizacion":
		//TODO: el update deberia ser save and flush:
		fileControlService.updateFileControlStatus(codeFileControl, 
				EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PENDING_ACCOUNTING, userName);
		
		//Se obtiene el fichero de control:
		Optional<ControlFichero> fileControlOpt = fileControlRepository.findById(codeFileControl);
		if(!fileControlOpt.isPresent()) {
			return false;
		}
		ControlFichero controlFichero = fileControlOpt.get();
				
		//Dependiendo del tipo de fichero:
		boolean isCGPJ = EmbargosUtils.determineFileFormatByTipoFichero(controlFichero.getTipoFichero().getCodTipoFichero()).equals(EmbargosConstants.FILE_FORMAT_CGPJ);
		
		if (isCGPJ) {
			//Si es de CGPJ:
			sendAccountingCGPJ(controlFichero, userName);
		} else {
			//Sino sera de AEAT o de Cuaderno 63:
			sendAccountingAEATCuaderno63(controlFichero, userName);
		}
		
		//Se cambia el estado de Control Fichero a "Pendiente de respuesta contable"
		fileControlService.updateFileControlStatus(codeFileControl, 
				EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PENDING_ACCOUNTING_RESPONSE, userName);
		
		logger.info("SeizureServiceImpl - sendAccounting - end");
		return true;
	}

	private void sendAccountingAEATCuaderno63(ControlFichero controlFichero, String userName) throws ICEException {
		logger.info("SeizureServiceImpl - sendAccountingAEATCuaderno63 - start");
		String cuentaRecaudacion = determineCuentaRecaudacion();
		
		Long oficinaCuentaRecaudacion = determineOficinaCuentaRecaudacion();
		
		String contabilizacionCallbackNameParameter = EmbargosConstants.PARAMETRO_EMBARGOS_CONTABILIZACION_CALLBACK;
		
		
		//Se obtienen la trabas asociadas al fichero:
		for (Embargo embargo : controlFichero.getEmbargos()) {
			
			Traba traba = embargo.getTrabas().get(0);
			
			String reference1 = embargo.getNumeroEmbargo();
			String reference2 = "";
			String detailPayment = embargo.getDatregcomdet();
		
			for(CuentaTraba cuentaTraba : traba.getCuentaTrabas()) {
				
				AccountingNote accountingNote = new AccountingNote();
				
				double amount = cuentaTraba.getImporte()!=null ? cuentaTraba.getImporte().doubleValue() : 0;
				 			
				accountingNote.setAplication(EmbargosConstants.ID_APLICACION_EMBARGOS);
				accountingNote.setCodOffice(oficinaCuentaRecaudacion);
				//El contador lo gestiona Comunes
				//accountingNote.setContador(contador);
				accountingNote.setAmount(amount);
				accountingNote.setCodCurrency(cuentaTraba.getDivisa());
				accountingNote.setDebitAccount(cuentaTraba.getCuenta());
				accountingNote.setCreditAccount(cuentaRecaudacion);
				accountingNote.setActualDate(new Date());
				//accountingNote.setExecutionDate(new Date());
				accountingNote.setReference1(reference1);
				accountingNote.setReference2(reference2);
				accountingNote.setDetailPayment(detailPayment);
				accountingNote.setChange(cuentaTraba.getCambio());
				accountingNote.setGeneralParameter(contabilizacionCallbackNameParameter);
				
				accountingNoteService.Contabilizar(accountingNote);
				
				//Se actualiza el estado de la Cuenta Traba a "Enviada a contabilidad":
				updateSeizedBankStatus(cuentaTraba, EmbargosConstants.COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD, userName);
				
			}
			
			//Cambio de estado de la traba a "Enviada a contabilidad";
			SeizureStatusDTO seizureStatusDTO = new SeizureStatusDTO();
			seizureStatusDTO.setCode(Long.toString(EmbargosConstants.COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD));
			updateSeizureStatus(controlFichero.getCodControlFichero(), traba.getCodTraba(), seizureStatusDTO, userName);	

		}
		logger.info("SeizureServiceImpl - sendAccountingAEATCuaderno63 - end");
	}
	
	private void sendAccountingCGPJ(ControlFichero controlFichero, String userName) throws ICEException {
		logger.info("SeizureServiceImpl - sendAccountingCGPJ - start");
		String cuentaRecaudacion = determineCuentaRecaudacion();
		
		Long oficinaCuentaRecaudacion = determineOficinaCuentaRecaudacion();
		
		String contabilizacionCallbackNameParameter = EmbargosConstants.PARAMETRO_EMBARGOS_CONTABILIZACION_CALLBACK;
		
		//Se obtienen las peticiones asociadas al fichero:
		for (Peticion peticion : controlFichero.getPeticiones()) {
			
			for(SolicitudesEjecucion solicitudEjecucion : peticion.getSolicitudesEjecucions()) {
				
				Optional<Traba> trabaOpt = null;
				
				if (solicitudEjecucion.getCodTraba()!=null) {
					trabaOpt = seizedRepository.findById(solicitudEjecucion.getCodTraba().longValue());		
				}
					
				if(trabaOpt!=null && trabaOpt.isPresent()) {
					
					Traba traba = trabaOpt.get();
					
					String reference1 = "";//embargo.getNumeroEmbargo();
					String reference2 = "";
					String detailPayment = "";//embargo.getDatregcomdet();
				
					for(CuentaTraba cuentaTraba : traba.getCuentaTrabas()) {
						
						AccountingNote accountingNote = new AccountingNote();
						
						double amount = cuentaTraba.getImporte()!=null ? cuentaTraba.getImporte().doubleValue() : 0;
						 			
						accountingNote.setAplication(EmbargosConstants.ID_APLICACION_EMBARGOS);
						accountingNote.setCodOffice(oficinaCuentaRecaudacion);
						//El contador lo gestiona Comunes
						//accountingNote.setContador(contador);
						accountingNote.setAmount(amount);
						accountingNote.setCodCurrency(cuentaTraba.getDivisa());
						accountingNote.setDebitAccount(cuentaTraba.getCuenta());
						accountingNote.setCreditAccount(cuentaRecaudacion);
						accountingNote.setActualDate(new Date());
						//accountingNote.setExecutionDate(new Date());
						accountingNote.setReference1(reference1);
						accountingNote.setReference2(reference2);
						accountingNote.setDetailPayment(detailPayment);
						accountingNote.setChange(cuentaTraba.getCambio());
						accountingNote.setGeneralParameter(contabilizacionCallbackNameParameter);
						
						accountingNoteService.Contabilizar(accountingNote);
						
						//Se actualiza el estado de la Cuenta Traba a "Enviada a contabilidad":
						updateSeizedBankStatus(cuentaTraba, EmbargosConstants.COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD, userName);
						
					}
					
					//Cambio de estado de la traba a "Enviada a contabilidad";
					SeizureStatusDTO seizureStatusDTO = new SeizureStatusDTO();
					seizureStatusDTO.setCode(Long.toString(EmbargosConstants.COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD));
					updateSeizureStatus(controlFichero.getCodControlFichero(), traba.getCodTraba(), seizureStatusDTO, userName);		
				}
			}
		}
		logger.info("SeizureServiceImpl - sendAccountingCGPJ - end");
	}
	
	private Long determineOficinaCuentaRecaudacion() throws ICEException {
		logger.info("SeizureServiceImpl - determineOficinaCuentaRecaudacion - start");
		GeneralParameter oficinaCuentaRecaudacionGenParam = 
				generalParametersService.viewParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_OFICINA);	
		
		Long oficinaCuentaRecaudacion = oficinaCuentaRecaudacionGenParam!=null ? 
				Long.valueOf(oficinaCuentaRecaudacionGenParam.getValue()) : null;
		
		if (oficinaCuentaRecaudacion==null) {
			throw new ICEException("","ERROR: parameter not found: " + EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_OFICINA);
		}
		
		logger.info("SeizureServiceImpl - determineOficinaCuentaRecaudacion - end");
		return oficinaCuentaRecaudacion;
	}

	private String determineCuentaRecaudacion() throws ICEException {
		logger.info("SeizureServiceImpl - determineCuentaRecaudacion - start");
		GeneralParameter cuentaRecaudacionGenParam = 
				generalParametersService.viewParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_CUENTA);	
		
		String cuentaRecaudacion = cuentaRecaudacionGenParam!=null ? cuentaRecaudacionGenParam.getValue() :null;		
		
		if (cuentaRecaudacion==null || cuentaRecaudacion.isEmpty()) {
			throw new ICEException("","ERROR: parameter not found: " + EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_CUENTA);
		}
		
		logger.info("SeizureServiceImpl - determineCuentaRecaudacion - end");
		return cuentaRecaudacion;
	}
	
//	private String determineContabilizacionCallbackParameter() throws ICEException {	
//		GeneralParameter contabilizacionCallbackGenParam = 
//				generalParametersService.viewParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CONTABILIZACION_CALLBACK);
//		
//		String contabilizacionCallback = contabilizacionCallbackGenParam!=null ?
//				contabilizacionCallbackGenParam.getValue() :null;
//		
//		if (contabilizacionCallback==null || contabilizacionCallback.isEmpty()) {
//			throw new ICEException("","ERROR: parameter not found: " + EmbargosConstants.PARAMETRO_EMBARGOS_CONTABILIZACION_CALLBACK);
//		}
//		
//		return contabilizacionCallback;
//	}
	
	@Override
	public boolean manageAccountingNoteCallback(RespuestaContabilidad respuestaContabilidad, String userName) {
		
		//Se tomaran los campos IBS_CREDIT_ACCOUNT,TRIM(IBS_REFERENCE_1+IBS_REFERENCE_2),IBS_AMOUNT para 
		//determinar que elemento se ha contabilizado y marcar su estado a contabilizado.
		
		//1. Se obtiene la Cuenta Traba:
//		respuestaContabilidad.ge
		
		//2. Se cambia el estado de la Cuenta Traba:
		
		
		//3. Si todas las CuentaTraba asociadas a la Traba han cambiado a estado "Contabilizada", entonces: 
		// - Cambiar el estado de la Traba a "Contabilizada":
		
		
		//4. Si todas las Trabas estan contabilizadas, entonces:
		// - Cambiar el estado de Control Fichero de Embargos (control fichero de la Traba no) a estado "Pendiente de envio". 
		
				
		return true;
	}
	
	@Override
	public boolean undoAccounting(Long codeFileControl, Long idSeizure, String numAccount, String userName) {
		logger.info("SeizureServiceImpl - undoAccounting - start");
		//Solo se puede retroceder cuando este contabilizado (se haya realizado el callback) y una vez realizado
		//el retroceso, se cambiara el estado a anterior a contabilizado.
		
		EstadoTraba estadoTraba = new EstadoTraba();
		estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_CONTABILIZADA);
		
		CuentaTraba cuentaTraba = seizedBankAccountRepository.findByCodCuentaTrabaAndCuentaAndEstadoTraba(codeFileControl, idSeizure, estadoTraba);
		
		estadoTraba = new EstadoTraba();
		estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_MODIFICADA);
		cuentaTraba.setEstadoTraba(estadoTraba);
		
		seizedBankAccountRepository.save(cuentaTraba);
		
		//Cambio de estado de la Traba:
		
		
		//Cambio de estado de Control Fichero de Embargos:
		
		logger.info("SeizureServiceImpl - undoAccounting - end");
		return false;
	}
	
	@Override
	public byte[] generateJustificanteEmbargo(Integer idSeizure) throws Exception {
		logger.info("SeizureServiceImpl - generateJustificanteEmbargo - start");
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource embargosJrxml = ResourcesUtil.getFromJasperFolder("justificante_embargos.jasper");
			Resource logoImage = ResourcesUtil.getImageLogoCommerceResource();
			// Resource templateStyle = ResourcesUtil.getTemplateStyleResource();

			File image = logoImage.getFile();

			// InputStream templateStyleStream =
			// getClass().getResourceAsStream("/jasper/CommerzBankStyle.jrtx");

			parameters.put("COD_TRABA", idSeizure);
			parameters.put("IMAGE_PARAM", image.toString());
			// parameters.put("TEMPLATE_STYLE_PATH", templateStyleStream);

			InputStream justificanteInputStream = embargosJrxml.getInputStream();

			JasperPrint fillReport = JasperFillManager.fillReport(justificanteInputStream, parameters, conn);
			
			logger.info("SeizureServiceImpl - generateJustificanteEmbargo - end");
			return JasperExportManager.exportReportToPdf(fillReport);

		} catch (SQLException e) {
			throw new Exception("DB exception while generating the report", e);
		}
	}
	
	@Override
	public byte[] generateLevantamientoReport(Integer idLifting) throws Exception {
		logger.info("SeizureServiceImpl - generateLevantamientoReport - start");
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource embargosJrxml = ResourcesUtil.getFromJasperFolder("levantamiento_embargo.jasper");
			Resource logoImage = ResourcesUtil.getImageLogoCommerceResource();
			// Resource templateStyle = ResourcesUtil.getTemplateStyleResource();

			File image = logoImage.getFile();

			// InputStream templateStyleStream =
			// getClass().getResourceAsStream("/jasper/CommerzBankStyle.jrtx");

			parameters.put("COD_LEVANTAMIENTO", idLifting);
			parameters.put("IMAGE_PARAM", image.toString());
			// parameters.put("TEMPLATE_STYLE_PATH", templateStyleStream);

			InputStream justificanteInputStream = embargosJrxml.getInputStream();

			JasperPrint fillReport = JasperFillManager.fillReport(justificanteInputStream, parameters, conn);

			logger.info("SeizureServiceImpl - generateLevantamientoReport - end");
			return JasperExportManager.exportReportToPdf(fillReport);

		} catch (SQLException e) {
			System.out.println(e);
			throw new Exception("DB exception while generating the report", e);
		}
	}


	@Override
	public byte[] generarResumenTrabasF4(Integer codControlFichero) throws Exception {
		logger.info("SeizureServiceImpl - generarResumenTrabasF4 - start");
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource resumenTrabasJrxml = ResourcesUtil.getFromJasperFolder("resumen_trabas_seizures.jasper");

			Resource logoImage = ResourcesUtil.getImageLogoCommerceResource();

			File image = logoImage.getFile();

			parameters.put("IMAGE_PARAM", image.toString());
			parameters.put("COD_FILE_CONTROL", codControlFichero);

			InputStream resumenInputStream = resumenTrabasJrxml.getInputStream();

			JasperPrint fillReport = JasperFillManager.fillReport(resumenInputStream, parameters, conn);

			logger.info("SeizureServiceImpl - generarResumenTrabasF4 - end");
			return JasperExportManager.exportReportToPdf(fillReport);

		} catch (SQLException e) {
			throw new Exception("DB exception while generating the report", e);
		}
	}

	@Override
	public byte[] generarResumenTrabasF3(Integer codControlFichero) throws Exception {
		logger.info("SeizureServiceImpl - generarResumenTrabasF3 - start");
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource resumenTrabasJrxml = ResourcesUtil.getFromJasperFolder("resumen_trabas_request.jasper");
			
			Resource headerSubreport = ResourcesUtil.getReportHeaderResource();

			Resource logoImage = ResourcesUtil.getImageLogoCommerceResource();

			File image = logoImage.getFile();

			InputStream subReportHeaderInputStream = headerSubreport.getInputStream();

			JasperReport subReportHeader = (JasperReport) JRLoader.loadObject(subReportHeaderInputStream);

			parameters.put("sub_img_param", image.toString());
			parameters.put("SUBREPORT_HEADER", subReportHeader);
			parameters.put("COD_FILE_CONTROL", codControlFichero);

			InputStream resumenInputStream = resumenTrabasJrxml.getInputStream();

			JasperPrint fillReport = JasperFillManager.fillReport(resumenInputStream, parameters, conn);
			
			logger.info("SeizureServiceImpl - generarResumenTrabasF3 - end");
			return JasperExportManager.exportReportToPdf(fillReport);

		} catch (SQLException e) {
			throw new Exception("DB exception while generating the report", e);
		}

	}

	@Override
	public byte[] generarAnexo(BigDecimal cod_usuario, BigDecimal cod_traba, Integer num_anexo) throws Exception {
		logger.info("SeizureServiceImpl - generarAnexo - start");
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn_comunes = oracleDataSourceEmbargos.getComunesConnection();
				Connection conn_embargos = oracleDataSourceEmbargos.getEmbargosConnection();) {

			Resource anexoJasperFile = ResourcesUtil.getFromJasperFolder("anexo" + num_anexo + ".jasper");

			Resource importeAbonadoSubReport = ResourcesUtil.getFromJasperFolder("importe_abonado.jasper");
//			Resource simpleHeaderResource = ResourcesUtil.getSimpleHeader();
			Resource logoImage = ResourcesUtil.getImageLogoCommerceResource();

			File image = logoImage.getFile();

//			InputStream subReportSimpleHeaderInputStream = simpleHeaderResource.getInputStream();
//			JasperReport subReportSimpleHeader = (JasperReport) JRLoader.loadObject(subReportSimpleHeaderInputStream);
//		
			InputStream importeAbonadoInputStream = importeAbonadoSubReport.getInputStream();
			JasperReport importeAbonadoReport = (JasperReport) JRLoader.loadObject(importeAbonadoInputStream);

			parameters.put("IMAGE_PARAM", image.toString());
//			parameters.put("SUBREPORT_SIMPLE_HEADER", subReportSimpleHeader);
			parameters.put("REPORT_IMPORTE_ABONADO", importeAbonadoReport);
			parameters.put("COD_USUARIO", cod_usuario);
			parameters.put("COD_TRABA", cod_traba);
			parameters.put("REPORT_CONNECTION_EMB", conn_embargos);

			InputStream anexo1Input = anexoJasperFile.getInputStream();
			JasperPrint fillReport = JasperFillManager.fillReport(anexo1Input, parameters, conn_comunes);
			
			logger.info("SeizureServiceImpl - generarAnexo - end");
			return JasperExportManager.exportReportToPdf(fillReport);

		} catch (SQLException e) {
			System.out.println(e);
			throw new Exception("DB exception while generating the report", e);
		}
	}
}
