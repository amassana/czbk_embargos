package es.commerzbank.ice.embargos.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.comun.lib.domain.dto.AccountingNote;
import es.commerzbank.ice.comun.lib.domain.dto.GeneralParameter;
import es.commerzbank.ice.comun.lib.service.AccountingNoteService;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.domain.dto.SeizureStatusDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.CuentaLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import es.commerzbank.ice.embargos.domain.entity.EstadoTraba;
import es.commerzbank.ice.embargos.domain.entity.LevantamientoTraba;
import es.commerzbank.ice.embargos.domain.entity.Peticion;
import es.commerzbank.ice.embargos.domain.entity.SolicitudesEjecucion;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.SeizedBankAccountRepository;
import es.commerzbank.ice.embargos.repository.SeizedRepository;
import es.commerzbank.ice.embargos.service.AccountingService;
import es.commerzbank.ice.embargos.service.FileControlService;
import es.commerzbank.ice.embargos.service.LiftingService;
import es.commerzbank.ice.embargos.service.SeizureService;
import es.commerzbank.ice.utils.EmbargosConstants;
import es.commerzbank.ice.utils.EmbargosUtils;
import es.commerzbank.ice.utils.ICEDateUtils;

@Service
@Transactional(transactionManager="transactionManager")
public class AccountingServiceImpl implements AccountingService{
	
	private static final Logger logger = LoggerFactory.getLogger(AccountingServiceImpl.class);
	
	@Autowired
	FileControlService fileControlService;
	
	@Autowired
	SeizureService seizureService;
	
	@Autowired
	AccountingNoteService accountingNoteService;
	
	@Autowired
	GeneralParametersService generalParametersService;
	
	@Autowired
	FileControlRepository fileControlRepository;
	
	@Autowired
	SeizedRepository seizedRepository;
	
	@Autowired
	SeizedBankAccountRepository seizedBankAccountRepository;
	
	@Autowired
	LiftingService liftingService;
	
	
	@Override
	public boolean sendAccounting(Long codeFileControl, String userName) throws ICEException {
		logger.info("AccountingServiceImpl - sendAccounting - start");	
		//Se obtiene el fichero de control:
		Optional<ControlFichero> fileControlOpt = fileControlRepository.findById(codeFileControl);
		if(!fileControlOpt.isPresent()) {
			return false;
		}
		ControlFichero controlFichero = fileControlOpt.get();
				
		//Dependiendo del tipo de fichero:
		String fileFormat = EmbargosUtils.determineFileFormatByTipoFichero(controlFichero.getTipoFichero().getCodTipoFichero());
		
		boolean isCGPJ = fileFormat!=null && fileFormat.equals(EmbargosConstants.FILE_FORMAT_CGPJ);
		boolean isAEAT = fileFormat!=null && fileFormat.equals(EmbargosConstants.FILE_FORMAT_AEAT);
		boolean isCuaderno63 = fileFormat!=null && fileFormat.equals(EmbargosConstants.FILE_FORMAT_NORMA63);
		
		boolean isAccountingSent = false;
		
		long codEstadoCtrlFichero = controlFichero.getEstadoCtrlfichero().getId().getCodEstado();
		
		if (isCGPJ) {

			//Para contabilizar, el estado de ControlFichero tiene que ser previo o igual a "Pendiente de contabilizacion":
			if (codEstadoCtrlFichero != EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_RECEIVED
					&& codEstadoCtrlFichero != EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_PENDING_ACCOUNTING) {

				logger.debug(generateMessageCtrlFicheroCannotSendAccounting(controlFichero));
				
				return false;
			}
			
			//Se cambia el estado de Control Fichero a "Pendiente de contabilizacion"
			fileControlService.updateFileControlStatusTransaction(controlFichero, 
					EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_PENDING_ACCOUNTING, userName);

			isAccountingSent = sendAccountingCGPJ(controlFichero, userName);
			
			if (isAccountingSent) {	
				//Se cambia el estado de Control Fichero a "Pendiente de respuesta contable"
				fileControlService.updateFileControlStatusTransaction(controlFichero, 
						EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_PENDING_ACCOUNTING_RESPONSE, userName);
			}
		
		} else if (isAEAT){

			//Para contabilizar, el estado de ControlFichero tiene que ser previo o igual a "Pendiente de contabilizacion":
			if (codEstadoCtrlFichero != EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_RECEIVED
					&& codEstadoCtrlFichero != EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PENDING_ACCOUNTING) {
				
				logger.debug(generateMessageCtrlFicheroCannotSendAccounting(controlFichero));
				
				return false;
			}
			
			//Se cambia el estado de Control Fichero a "Pendiente de contabilizacion"
			fileControlService.updateFileControlStatusTransaction(controlFichero, 
					EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PENDING_ACCOUNTING, userName);
			
			isAccountingSent = sendAccountingAEATCuaderno63(controlFichero, userName);

			if (isAccountingSent) {		
				//Se cambia el estado de Control Fichero a "Pendiente de respuesta contable"
				fileControlService.updateFileControlStatusTransaction(controlFichero, 
						EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PENDING_ACCOUNTING_RESPONSE, userName);
			}
			
		} else if (isCuaderno63) {
			
			//Para contabilizar, el estado de ControlFichero tiene que ser previo o igual a "Pendiente de contabilizacion":
			if (codEstadoCtrlFichero != EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_RECEIVED
					&& codEstadoCtrlFichero != EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_PENDING_ACCOUNTING) {
			
				logger.debug(generateMessageCtrlFicheroCannotSendAccounting(controlFichero));
				
				return false;
			}
			
			//Se cambia el estado de Control Fichero a "Pendiente de contabilizacion"
			fileControlService.updateFileControlStatusTransaction(controlFichero, 
					EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_PENDING_ACCOUNTING, userName);
			
			isAccountingSent = sendAccountingAEATCuaderno63(controlFichero, userName);

			if (isAccountingSent) {	
				//Se cambia el estado de Control Fichero a "Pendiente de respuesta contable"
				fileControlService.updateFileControlStatusTransaction(controlFichero, 
						EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_PENDING_ACCOUNTING_RESPONSE, userName);			
			}
			
		} else {
			logger.info("AccountingServiceImpl - sendAccounting - end");
			return false;
		}
		
		logger.info("AccountingServiceImpl - sendAccounting - end");
		return true;
	}

	
	private String generateMessageCtrlFicheroCannotSendAccounting(ControlFichero controlFichero) {
		StringBuilder stb = new StringBuilder();
		stb.append("No se puede enviar a contabilidad el controlFichero con id ")
				.append(controlFichero.getCodControlFichero())
				.append(" ya que su estado es: [codEstado:")
				.append(controlFichero.getEstadoCtrlfichero().getId().getCodEstado())
				.append("; descEstado: ")
				.append(controlFichero.getEstadoCtrlfichero().getDescripcion())
				.append("]");
		return stb.toString();
	}

	private boolean sendAccountingAEATCuaderno63(ControlFichero controlFichero, String userName) throws ICEException {
		
		logger.info("AccountingServiceImpl - sendAccountingAEATCuaderno63 - start");
		String cuentaRecaudacion = determineCuentaRecaudacion();
		
		Long oficinaCuentaRecaudacion = determineOficinaCuentaRecaudacion();
		
		String contabilizacionCallbackNameParameter = EmbargosConstants.PARAMETRO_EMBARGOS_CONTABILIZACION_FASE3_CALLBACK;
				
		boolean existsTrabaNotAccounted = false;
		
		
		//Se obtienen la trabas asociadas al fichero:
		for (Embargo embargo : controlFichero.getEmbargos()) {
			
			Traba traba = embargo.getTrabas().get(0);
			
			//Para contabilizar la traba tiene que estar en estado anterior a "Enviada a Contabilidad":
			if (traba.getEstadoTraba().getCodEstado() == EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE) {
			
				String reference1 = embargo.getNumeroEmbargo();
				String reference2 = "";
				String detailPayment = embargo.getDatregcomdet();
			
				String codGroupNote = EmbargosConstants.F3 + "_"
						+ embargo.getControlFichero().getCodControlFichero() +"_"
						+ ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
				
				boolean existsCuentaTrabaNotAccounted = false;
				
				for(CuentaTraba cuentaTraba : traba.getCuentaTrabas()) {
					
					//Para contabilizar la cuentaTraba tiene que estar en estado anterior a "Enviada a Contabilidad":
					if (cuentaTraba.getEstadoTraba().getCodEstado() == EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE) {
					
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
						accountingNote.setCodGroupNote(codGroupNote);
						accountingNote.setStatus(EmbargosConstants.COD_ESTADO_APUNTE_CONTABLE_PENDIENTE_ENVIO);
						
						int resultContabilizar = accountingNoteService.contabilizar(accountingNote);
						
						//Dependiendo del resultado de contabilizar:
						if(resultContabilizar == 1) {
							//Se ha contabilizado la cuentaTraba: Se actualiza el estado de la Cuenta Traba a "Enviada a contabilidad":
							seizureService.updateSeizedBankStatusTransaction(cuentaTraba, EmbargosConstants.COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD, userName);
							
						} else {
							//No se ha contabilizado la cuentaTraba:
							existsCuentaTrabaNotAccounted = true;
						}				
					
					} else {
						
						//La cuentaTraba se encuentra en un estado donde ya ha sido enviada a Contabilidad.
						
						logger.debug("La cuentaTraba con id " + cuentaTraba.getCodCuentaTraba() 
							+ " no se puede contabilizar ya que se encuentra en estado : [codEstado=" + cuentaTraba.getEstadoTraba().getCodEstado() 
							+"; descEstado=" + cuentaTraba.getEstadoTraba().getDesEstado() + "]");
					
					}				
				}
				
				String codigoEstadoTraba = "";
							
				if (!existsCuentaTrabaNotAccounted) {
					
					//Si no existe ninguna cuentaTraba que no se haya contabilizado:
					
					//Cambio de estado de la traba a "Enviada a contabilidad":		
					codigoEstadoTraba = Long.toString(EmbargosConstants.COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD);
				
					SeizureStatusDTO seizureStatusDTO = new SeizureStatusDTO();
					seizureStatusDTO.setCode(codigoEstadoTraba);
					seizureService.updateSeizureStatusTransaction(traba.getCodTraba(), seizureStatusDTO, userName);	
				
				} else {
					//Si existe alguna cuentaTraba que no se haya contabilizado:
					
					existsTrabaNotAccounted = true;
				}
			
			} else {
				
				//La traba se encuentra en un estado donde ya ha sido enviada a Contabilidad.
				
				logger.debug("La traba con id " + traba.getCodTraba() 
				+ " no se puede contabilizar ya que se encuentra en estado : [codEstado=" + traba.getEstadoTraba().getCodEstado() 
				+"; descEstado=" + traba.getEstadoTraba().getDesEstado() + "]");
			}
		}
		logger.info("AccountingServiceImpl - sendAccountingAEATCuaderno63 - end");
		
		//Se devuelve true si no existe traba que no haya sido enviada a contabilidad:
		return !existsTrabaNotAccounted;
	}
	
	private boolean sendAccountingCGPJ(ControlFichero controlFichero, String userName) throws ICEException {
		logger.info("AccountingServiceImpl - sendAccountingCGPJ - start");
		String cuentaRecaudacion = determineCuentaRecaudacion();
		
		Long oficinaCuentaRecaudacion = determineOficinaCuentaRecaudacion();
		
		String contabilizacionCallbackNameParameter = EmbargosConstants.PARAMETRO_EMBARGOS_CONTABILIZACION_FASE3_CALLBACK;
		
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
						
						accountingNoteService.contabilizar(accountingNote);
						
						//Se actualiza el estado de la Cuenta Traba a "Enviada a contabilidad":
						seizureService.updateSeizedBankStatus(cuentaTraba, EmbargosConstants.COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD, userName);
						
					}
					
					//Cambio de estado de la traba a "Enviada a contabilidad";
					SeizureStatusDTO seizureStatusDTO = new SeizureStatusDTO();
					seizureStatusDTO.setCode(Long.toString(EmbargosConstants.COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD));
					seizureService.updateSeizureStatusTransaction(traba.getCodTraba(), seizureStatusDTO, userName);		
				}
			}
		}
		
		logger.info("AccountingServiceImpl - sendAccountingCGPJ - end");
		return true;
	}
	
	private Long determineOficinaCuentaRecaudacion() throws ICEException {
		logger.info("AccountingServiceImpl - determineOficinaCuentaRecaudacion - start");
		GeneralParameter oficinaCuentaRecaudacionGenParam = 
				generalParametersService.viewParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_OFICINA);	
		
		Long oficinaCuentaRecaudacion = oficinaCuentaRecaudacionGenParam!=null ? 
				Long.valueOf(oficinaCuentaRecaudacionGenParam.getValue()) : null;
		
		if (oficinaCuentaRecaudacion==null) {
			throw new ICEException("","ERROR: parameter not found: " + EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_OFICINA);
		}
		
		logger.info("AccountingServiceImpl - determineOficinaCuentaRecaudacion - end");
		return oficinaCuentaRecaudacion;
	}

	private String determineCuentaRecaudacion() throws ICEException {
		logger.info("AccountingServiceImpl - determineCuentaRecaudacion - start");
		GeneralParameter cuentaRecaudacionGenParam = 
				generalParametersService.viewParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_CUENTA);	
		
		String cuentaRecaudacion = cuentaRecaudacionGenParam!=null ? cuentaRecaudacionGenParam.getValue() :null;		
		
		if (cuentaRecaudacion==null || cuentaRecaudacion.isEmpty()) {
			throw new ICEException("","ERROR: parameter not found: " + EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_CUENTA);
		}
		
		logger.info("AccountingServiceImpl - determineCuentaRecaudacion - end");
		return cuentaRecaudacion;
	}
	

	@Override
	public boolean manageAccountingNoteCallback(AccountingNote accountingNote, String userName) {
		
		//Se tomaran los campos IBS_CREDIT_ACCOUNT,TRIM(IBS_REFERENCE_1+IBS_REFERENCE_2),IBS_AMOUNT para 
		//determinar que elemento se ha contabilizado y marcar su estado a contabilizado.
	
		
		//1. Se obtiene la Cuenta Traba:
		
		String cuenta = accountingNote.getDebitAccount();
		BigDecimal importe = BigDecimal.valueOf(accountingNote.getAmount());
		EstadoTraba estadoTraba = new EstadoTraba();
		estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD);
		
		CuentaTraba cuentaTraba = seizedBankAccountRepository.findByCuentaAndImporteAndEstadoTraba(cuenta, importe, estadoTraba);
		
		if(cuentaTraba==null) {
			logger.error("No se ha encontrado la cuentaTraba en estado enviada a contabilidad [cuenta:" + cuenta +";importe:" + importe + "]");;
			return false;
		}
		
		//2. Se cambia el estado de la Cuenta Traba a Contabilizada:
		
		seizureService.updateSeizedBankStatus(cuentaTraba, EmbargosConstants.COD_ESTADO_TRABA_CONTABILIZADA, userName);
		
		//3. Si todas las CuentaTraba asociadas a la Traba han cambiado a estado "Contabilizada", entonces: 
		// - Cambiar el estado de la Traba a "Contabilizada":
		
		Traba traba = cuentaTraba.getTraba();
		int numCuentaTrabasContabilizadas = 0;
		for(CuentaTraba cuentaTr : traba.getCuentaTrabas()) {		
			if (cuentaTr.getEstadoTraba().getCodEstado() == EmbargosConstants.COD_ESTADO_TRABA_CONTABILIZADA) {
				numCuentaTrabasContabilizadas++;
			}
		}
		
		boolean isAllCuentaTrabasContabilizadas = (numCuentaTrabasContabilizadas == traba.getCuentaTrabas().size());
		
		if (isAllCuentaTrabasContabilizadas) {
			
			SeizureStatusDTO seizureStatusDTO = new SeizureStatusDTO();
			seizureStatusDTO.setCode(String.valueOf(EmbargosConstants.COD_ESTADO_TRABA_CONTABILIZADA));
			
			seizureService.updateSeizureStatus(traba.getCodTraba(), seizureStatusDTO, userName);
		}
		
		
		//4. Si todas las Trabas estan contabilizadas, entonces:
		// - Cambiar el estado de Control Fichero de Embargos (control fichero de la Traba no) a estado "Pendiente de envio". 
		
		Embargo embargo = traba.getEmbargo();
		ControlFichero controlFichero = embargo.getControlFichero();
		
		int numTrabasContabilizadas = 0;
		for (Embargo emb : controlFichero.getEmbargos()) {
			
			Traba tra = emb.getTrabas().get(0);
			
			if (tra.getEstadoTraba().getCodEstado() == EmbargosConstants.COD_ESTADO_TRABA_CONTABILIZADA) {
				numTrabasContabilizadas++;
			}
			
		}
		
		boolean isAllTrabasContabilizadas = (numTrabasContabilizadas == controlFichero.getEmbargos().size());
		
		if (isAllTrabasContabilizadas) {
			
			//Dependiendo del tipo de fichero:
			String fileFormat = EmbargosUtils.determineFileFormatByTipoFichero(controlFichero.getTipoFichero().getCodTipoFichero());
			
			boolean isCGPJ = fileFormat!=null && fileFormat.equals(EmbargosConstants.FILE_FORMAT_CGPJ);
			boolean isAEAT = fileFormat!=null && fileFormat.equals(EmbargosConstants.FILE_FORMAT_AEAT);
			boolean isCuaderno63 = fileFormat!=null && fileFormat.equals(EmbargosConstants.FILE_FORMAT_NORMA63);
			
			Long estado = null;
			
			if (isCGPJ) {
				estado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_PENDING_TO_SEND;
			}
			else if(isAEAT) {
				estado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PENDING_TO_SEND;
			}
			else if (isCuaderno63) {
				estado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_PENDING_TO_SEND;
			}
			
			//Se cambia el estado de Control Fichero a "Pendiente de envio"
			fileControlService.updateFileControlStatusTransaction(controlFichero, estado, userName);

		}

		
		return true;
	}
	
	@Override
	public boolean undoAccounting(Long codeFileControl, Long idSeizure, String numAccount, String userName) throws ICEException{
		logger.info("AccountingServiceImpl - undoAccounting - start");
		//Solo se puede retroceder cuando este contabilizado (se haya realizado el callback) y una vez realizado
		//el retroceso, se cambiara el estado a anterior a contabilizado.
		
		EstadoTraba estadoTraba = new EstadoTraba();
		estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_CONTABILIZADA);
		
		CuentaTraba cuentaTraba = seizedBankAccountRepository.findByCodCuentaTrabaAndCuentaAndEstadoTraba(codeFileControl, idSeizure, estadoTraba);
		
		if(cuentaTraba == null) {
			throw new ICEException("","ERROR: no se ha encontrado la cuentaTraba [codeFileControl: " + codeFileControl + "; idSeizure: " + idSeizure + "; estadoTraba: "+ estadoTraba);
		}
		
		//Cambio de estado de la cuentaTraba:
		
		estadoTraba = new EstadoTraba();
		estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_MODIFICADA);
		cuentaTraba.setEstadoTraba(estadoTraba);
		
		seizedBankAccountRepository.save(cuentaTraba);
		
		//Cambio de estado de la traba a "MODIFICADA";
		Traba traba = cuentaTraba.getTraba();
		
		SeizureStatusDTO seizureStatusDTO = new SeizureStatusDTO();
		seizureStatusDTO.setCode(Long.toString(EmbargosConstants.COD_ESTADO_TRABA_MODIFICADA));
		
		boolean isStatusTrabaUpdated = seizureService.updateSeizureStatus(traba.getCodTraba(), seizureStatusDTO, userName);	
		
		if(!isStatusTrabaUpdated) {
			throw new ICEException("", "ERROR: no se ha actualizado el estado de la Traba con codTraba: " + traba.getCodTraba());
		}
		
		//Cambio de estado de Control Fichero de Embargos:
		Long codEstado = null;
		
		Embargo embargo = traba.getEmbargo();
		ControlFichero controlFichero = embargo.getControlFichero();
		
		//Dependiendo del tipo de fichero:
		String fileFormat = EmbargosUtils.determineFileFormatByTipoFichero(controlFichero.getTipoFichero().getCodTipoFichero());
		
		boolean isCGPJ = fileFormat!=null && fileFormat.equals(EmbargosConstants.FILE_FORMAT_CGPJ);
		boolean isAEAT = fileFormat!=null && fileFormat.equals(EmbargosConstants.FILE_FORMAT_AEAT);
		boolean isCuaderno63 = fileFormat!=null && fileFormat.equals(EmbargosConstants.FILE_FORMAT_NORMA63);
		
		//Se cambia el estado de Control Fichero a Generado:
		if (isCGPJ) {
			codEstado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_GENERATED;
		}else if (isAEAT) {
			codEstado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_GENERATED;
		}else if (isCuaderno63) {
			codEstado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_GENERATED;
		} else {
			
			throw new ICEException("","ERROR: formato de fichero no encontrado para el codigo de tipo de fichero " 
							+ controlFichero.getTipoFichero().getCodTipoFichero() +".");
		}
		
		boolean isStatusFileControlUpdated = fileControlService.updateFileControlStatus(codeFileControl, codEstado, userName);
		
		if(!isStatusFileControlUpdated) {
			throw new ICEException("", "ERROR: no se ha actualizado el estado del Control Fichero con codeFileControl: " + codeFileControl);
		}
		
		logger.info("AccountingServiceImpl - undoAccounting - end");
		return true;
	}
	
		
	@Override
	public boolean sendAccountingLifting(Long codeFileControl, String userName)  throws ICEException {
		logger.info("AccountingServiceImpl - sendAccountingLifting - start");
		boolean response = true;
		
		Optional<ControlFichero> fileControlOpt = fileControlRepository.findById(codeFileControl);
		if(!fileControlOpt.isPresent()) {
			response =  false;
		} else {
		
			String cuentaRecaudacion = determineCuentaRecaudacion();
			Long oficinaCuentaRecaudacion = determineOficinaCuentaRecaudacion();
			String contabilizacionCallbackNameParameter = EmbargosConstants.PARAMETRO_EMBARGOS_CONTABILIZACION_FASE5_CALLBACK;
			
			for (LevantamientoTraba levantamiento : fileControlOpt.get().getLevantamientoTrabas()) {
				
				String reference1 = levantamiento.getTraba().getEmbargo().getNumeroEmbargo();
				String reference2 = "";
				String detailPayment = levantamiento.getTraba().getEmbargo().getDatregcomdet();
				
				for (CuentaLevantamiento cuenta : levantamiento.getCuentaLevantamientos()) {
					AccountingNote accountingNote = new AccountingNote();
					
					double amount = cuenta.getImporte()!=null ? cuenta.getImporte().doubleValue() : 0;
		 			
					accountingNote.setAplication(EmbargosConstants.ID_APLICACION_EMBARGOS);
					accountingNote.setCodOffice(oficinaCuentaRecaudacion);
					//El contador lo gestiona Comunes
					//accountingNote.setContador(contador);
					accountingNote.setAmount(amount);
					accountingNote.setCodCurrency(cuenta.getCodDivisa());
					accountingNote.setDebitAccount(cuentaRecaudacion);
					accountingNote.setCreditAccount(cuenta.getCuenta());
					accountingNote.setActualDate(new Date());
					//accountingNote.setExecutionDate(new Date());
					accountingNote.setReference1(reference1);
					accountingNote.setReference2(reference2);
					accountingNote.setDetailPayment(detailPayment);
					accountingNote.setChange(cuenta.getCambio());
					accountingNote.setGeneralParameter(contabilizacionCallbackNameParameter);
					accountingNote.setStatus(EmbargosConstants.COD_ESTADO_APUNTE_CONTABLE_PENDIENTE_ENVIO);
					
					int resultado = accountingNoteService.contabilizar(accountingNote);
					
					if (resultado == 0) {
						response = false;
					}
					
					//Se actualiza el estado de la Cuenta Levantamiento a "Enviada a contabilidad":
					liftingService.updateLiftingBankAccountingStatus(cuenta, EmbargosConstants.COD_ESTADO_LEVANTAMIENTO_PENDIENTE_CONTABILIZACION, userName);
				}
			}
		}
		
		logger.info("AccountingServiceImpl - sendAccountingLifting - end");
		return response;
	}
	
}