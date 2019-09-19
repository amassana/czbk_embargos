package es.commerzbank.ice.embargos.service.impl;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.comun.lib.domain.dto.AccountingNote;
import es.commerzbank.ice.comun.lib.domain.dto.GeneralParameter;
import es.commerzbank.ice.comun.lib.formats.contabilidad.RespuestaContabilidad;
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
		boolean isCuaderno63 = fileFormat!=null && fileFormat.equals(EmbargosConstants.FILE_FORMAT_CUADERNO63);
		
		if (isCGPJ) {

			//Se cambia el estado de Control Fichero a "Pendiente de contabilidad"
			fileControlService.updateFileControlStatusTransaction(controlFichero, 
					EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_PENDING_ACCOUNTING, userName);

			sendAccountingCGPJ(controlFichero, userName);
			
			//Se cambia el estado de Control Fichero a "Pendiente de respuesta contable"
			fileControlService.updateFileControlStatusTransaction(controlFichero, 
					EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_PENDING_ACCOUNTING_RESPONSE, userName);
		
		} else if (isAEAT){

			//Se cambia el estado de Control Fichero a "Pendiente de contabilidad"
			fileControlService.updateFileControlStatusTransaction(controlFichero, 
					EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PENDING_ACCOUNTING, userName);
			
			sendAccountingAEATCuaderno63(controlFichero, userName);

			//Se cambia el estado de Control Fichero a "Pendiente de respuesta contable"
			fileControlService.updateFileControlStatusTransaction(controlFichero, 
					EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PENDING_ACCOUNTING_RESPONSE, userName);
			
		} else if (isCuaderno63) {
			
			//Se cambia el estado de Control Fichero a "Pendiente de contabilidad"
			fileControlService.updateFileControlStatusTransaction(controlFichero, 
					EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_PENDING_ACCOUNTING, userName);
			
			sendAccountingAEATCuaderno63(controlFichero, userName);

			//Se cambia el estado de Control Fichero a "Pendiente de respuesta contable"
			fileControlService.updateFileControlStatusTransaction(controlFichero, 
					EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_PENDING_ACCOUNTING_RESPONSE, userName);			
		
		} else {
			logger.info("AccountingServiceImpl - sendAccounting - end");
			return false;
		}
		
		logger.info("AccountingServiceImpl - sendAccounting - end");
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
			String contabilizacionCallbackNameParameter = EmbargosConstants.PARAMETRO_EMBARGOS_CONTABILIZACION_CALLBACK;
			
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

	private void sendAccountingAEATCuaderno63(ControlFichero controlFichero, String userName) throws ICEException {
		logger.info("AccountingServiceImpl - sendAccountingAEATCuaderno63 - start");
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
				
				accountingNoteService.contabilizar(accountingNote);
				
				//Se actualiza el estado de la Cuenta Traba a "Enviada a contabilidad":
				seizureService.updateSeizedBankStatus(cuentaTraba, EmbargosConstants.COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD, userName);
				
			}
			
			//Cambio de estado de la traba a "Enviada a contabilidad";
			SeizureStatusDTO seizureStatusDTO = new SeizureStatusDTO();
			seizureStatusDTO.setCode(Long.toString(EmbargosConstants.COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD));
			seizureService.updateSeizureStatus(controlFichero.getCodControlFichero(), traba.getCodTraba(), seizureStatusDTO, userName);	

		}
		logger.info("AccountingServiceImpl - sendAccountingAEATCuaderno63 - end");
	}
	
	private void sendAccountingCGPJ(ControlFichero controlFichero, String userName) throws ICEException {
		logger.info("AccountingServiceImpl - sendAccountingCGPJ - start");
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
						
						accountingNoteService.contabilizar(accountingNote);
						
						//Se actualiza el estado de la Cuenta Traba a "Enviada a contabilidad":
						seizureService.updateSeizedBankStatus(cuentaTraba, EmbargosConstants.COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD, userName);
						
					}
					
					//Cambio de estado de la traba a "Enviada a contabilidad";
					SeizureStatusDTO seizureStatusDTO = new SeizureStatusDTO();
					seizureStatusDTO.setCode(Long.toString(EmbargosConstants.COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD));
					seizureService.updateSeizureStatus(controlFichero.getCodControlFichero(), traba.getCodTraba(), seizureStatusDTO, userName);		
				}
			}
		}
		logger.info("AccountingServiceImpl - sendAccountingCGPJ - end");
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
		
		estadoTraba = new EstadoTraba();
		estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_MODIFICADA);
		cuentaTraba.setEstadoTraba(estadoTraba);
		
		seizedBankAccountRepository.save(cuentaTraba);
		
		//Cambio de estado de la Traba:
		Traba traba = cuentaTraba.getTraba();
		traba.setEstadoTraba(estadoTraba);
		
		//Cambio de estado de Control Fichero de Embargos:
		Long codEstado = null;
		
		Embargo embargo = traba.getEmbargo();
		ControlFichero controlFichero = embargo.getControlFichero();
		
		//Dependiendo del tipo de fichero:
		String fileFormat = EmbargosUtils.determineFileFormatByTipoFichero(controlFichero.getTipoFichero().getCodTipoFichero());
		
		boolean isCGPJ = fileFormat!=null && fileFormat.equals(EmbargosConstants.FILE_FORMAT_CGPJ);
		boolean isAEAT = fileFormat!=null && fileFormat.equals(EmbargosConstants.FILE_FORMAT_AEAT);
		boolean isCuaderno63 = fileFormat!=null && fileFormat.equals(EmbargosConstants.FILE_FORMAT_CUADERNO63);
		
		//Se cambia el estado de Control Fichero a Generado:
		if (isCGPJ) {
			codEstado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_GENERATED;
		}else if (isAEAT) {
			codEstado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_GENERATED;
		}else if (isCuaderno63) {
			codEstado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_GENERATED;
		} else {
			
			throw new ICEException("","ERROR: formato de fichero no encontrado para el codigo de tipo de fichero " + controlFichero.getTipoFichero().getCodTipoFichero() +".");
		}
		
		fileControlService.updateFileControlStatusTransaction(controlFichero, codEstado, userName);
		
		logger.info("AccountingServiceImpl - undoAccounting - end");
		return true;
	}
	
}