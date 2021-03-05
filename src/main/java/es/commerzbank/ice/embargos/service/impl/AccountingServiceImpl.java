package es.commerzbank.ice.embargos.service.impl;

import es.commerzbank.ice.comun.lib.domain.dto.AccountingNote;
import es.commerzbank.ice.comun.lib.domain.entity.Sucursal;
import es.commerzbank.ice.comun.lib.repository.OfficeCRepo;
import es.commerzbank.ice.comun.lib.service.AccountingNoteService;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.datawarehouse.service.AccountService;
import es.commerzbank.ice.embargos.domain.dto.SeizureStatusDTO;
import es.commerzbank.ice.embargos.domain.entity.*;
import es.commerzbank.ice.embargos.repository.*;
import es.commerzbank.ice.embargos.service.*;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.EmbargosUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static es.commerzbank.ice.embargos.utils.EmbargosConstants.*;

@Service
@Transactional(transactionManager="transactionManager")
public class AccountingServiceImpl implements AccountingService{
	
	private static final Logger logger = LoggerFactory.getLogger(AccountingServiceImpl.class);
	
	@Autowired
	private FileControlService fileControlService;
	
	@Autowired
	private SeizureService seizureService;
	
	@Autowired
	private AccountingNoteService accountingNoteService;
	
	@Autowired
	private GeneralParametersService generalParametersService;
	
	@Autowired
	private FinalFileService finalFileService;
	
	@Autowired
	private FileControlRepository fileControlRepository;
	
	@Autowired
	private SeizedRepository seizedRepository;
	
	@Autowired
	private SeizedBankAccountRepository seizedBankAccountRepository;
	
	@Autowired
	private LiftingService liftingService;

	@Autowired
	private LiftingBankAccountRepository liftingBankAccountRepository;
	
	@Autowired
	private FinalFileRepository finalFileRepository;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private OfficeCRepo officeCRepo;

	@Override
	@Transactional(transactionManager="transactionManager")
	public void sendSeizure(Long codeFileControl, String userName) throws ICEException, Exception {
	
		logger.info("sendAccountingSeizure - start "+ codeFileControl);
		
		//Se obtiene el fichero de control:
		Optional<ControlFichero> fileControlOpt = fileControlRepository.findById(codeFileControl);
		if(!fileControlOpt.isPresent()) {
			return;
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

			//Para contabilizar, el estado de ControlFichero tiene que ser previo o igual a "Recibido":
			if (codEstadoCtrlFichero != EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_RECEIVED) {

				logger.debug(generateMessageCtrlFicheroCannotSendAccounting(controlFichero));
				
				return;
			}

			sendSeizureCGPJ(controlFichero, userName);
			fileControlService.updateFileControlStatusTransaction(controlFichero,
					EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_PENDING_ACCOUNTING_RESPONSE, userName);
		} else if (isAEAT){

			//Para contabilizar, el estado de ControlFichero tiene que ser previo o igual a "Recibido":
			if (codEstadoCtrlFichero != EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_RECEIVED) {

				logger.debug(generateMessageCtrlFicheroCannotSendAccounting(controlFichero));

				return;
			}
			
			sendSeizureAEATCuaderno63(controlFichero, userName);

			fileControlService.updateFileControlStatusTransaction(controlFichero,
					EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PENDING_ACCOUNTING_RESPONSE, userName);
		}
		else if (isCuaderno63)
		{
			
			//Para contabilizar, el estado de ControlFichero tiene que ser previo o igual a "Recibido":
			if (codEstadoCtrlFichero != EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_RECEIVED) {
			
				logger.debug(generateMessageCtrlFicheroCannotSendAccounting(controlFichero));
				
				return;
			}
						
			sendSeizureAEATCuaderno63(controlFichero, userName);

			fileControlService.updateFileControlStatusTransaction(controlFichero,
					EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_PENDING_ACCOUNTING_RESPONSE, userName);
			
		} else {
			logger.info("sendAccountingSeizure - end");
			return;
		}
		
		logger.info("sendAccountingSeizure - end");
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

	private void sendSeizureAEATCuaderno63(ControlFichero controlFichero, String userName) throws Exception {

		String oficinaRecaudacion = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_OFICINA);
		String cuentaRecaudacion = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_CUENTA);
		Long sucursal = getCodSucursal(oficinaRecaudacion);

		es.commerzbank.ice.comun.lib.domain.entity.ControlFichero fileControlFicheroComunes = null;

		//Se obtienen la trabas asociadas al fichero:
		for (Embargo embargo : controlFichero.getEmbargos()) {
			
			Traba traba = embargo.getTrabas().get(0);
			
			//Para contabilizar la traba tiene que estar en estado anterior a "Enviada a Contabilidad":
			if (traba.getEstadoTraba().getCodEstado() == EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE) {

				String reference1 = embargo.getNumeroEmbargo();
				String reference2 = "";
				String detailPayment = embargo.getDatregcomdet();

				fileControlFicheroComunes = contabilizarTraba(
						fileControlFicheroComunes,
						embargo,
						traba,
						userName, sucursal, controlFichero.getDescripcion(),
						cuentaRecaudacion, oficinaRecaudacion, reference1, reference2, detailPayment);
			} else {
				//La traba se encuentra en un estado donde ya ha sido enviada a Contabilidad.

				logger.info("La traba con id " + traba.getCodTraba()
						+ " no se puede contabilizar ya que se encuentra en estado : [codEstado=" + traba.getEstadoTraba().getCodEstado()
						+"; descEstado=" + traba.getEstadoTraba().getDesEstado() + "]");
			}
		}
		
		if (fileControlFicheroComunes != null) {
			accountingNoteService.generacionFicheroContabilidad(fileControlFicheroComunes);
		}
	}

	private void sendSeizureCGPJ(ControlFichero controlFichero, String userName) throws ICEException, Exception {
		String oficinaRecaudacion = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_OFICINA);
		String cuentaRecaudacion = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_CUENTA);
		Long sucursal = getCodSucursal(oficinaRecaudacion);
		
		es.commerzbank.ice.comun.lib.domain.entity.ControlFichero fileControlFicheroComunes = null;

		//Se obtienen las peticiones asociadas al fichero:
		for (Peticion peticion : controlFichero.getPeticiones()) {
			
			for(SolicitudesEjecucion solicitudEjecucion : peticion.getSolicitudesEjecucions()) {
				
				Optional<Traba> trabaOpt = null;
				
				if (solicitudEjecucion.getCodTraba()!=null) {
					trabaOpt = seizedRepository.findById(solicitudEjecucion.getCodTraba().longValue());		
				}
					
				if(trabaOpt!=null && trabaOpt.isPresent()) {
					
					Traba traba = trabaOpt.get();
					Embargo embargo = traba.getEmbargo();

					//Para contabilizar la traba tiene que estar en estado anterior a "Enviada a Contabilidad":
					if (traba.getEstadoTraba().getCodEstado() == EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE) {

						//- Generacion de las references para CGPJ:
						Pair<String,String> references = generateReferencesForCGPJ(embargo.getNumeroEmbargo());
						String detailPayment = "";//embargo.getDatregcomdet();

						// TODO no existe detalle para el CPGJ??
						fileControlFicheroComunes = contabilizarTraba(fileControlFicheroComunes, embargo, traba,
								userName, sucursal, "", cuentaRecaudacion, oficinaRecaudacion,
								references.getLeft(), references.getRight(), detailPayment);
					} else {

						//La traba se encuentra en un estado donde ya ha sido enviada a Contabilidad.

						logger.debug("La traba con id " + traba.getCodTraba()
						+ " no se puede contabilizar ya que se encuentra en estado : [codEstado=" + traba.getEstadoTraba().getCodEstado()
						+"; descEstado=" + traba.getEstadoTraba().getDesEstado() + "]");
					}
				}

			}
		}
		
		if (fileControlFicheroComunes != null) {
			accountingNoteService.generacionFicheroContabilidad(fileControlFicheroComunes);
		}
	}

	private es.commerzbank.ice.comun.lib.domain.entity.ControlFichero contabilizarTraba(
			es.commerzbank.ice.comun.lib.domain.entity.ControlFichero fileControlFicheroComunes,
			Embargo embargo,
			Traba traba,
			String userName, Long sucursal, String descripcion,
			String cuentaRecaudacion, String oficinaCuentaRecaudacion,
			String reference1, String reference2, String detailPayment
	) throws Exception {
		//Se utiliza CopyOnWriteArrayList para evitar ConcurrentModificationException en el
		//update del estado de la cuentaTraba en la iteracion del listado de cuentaTrabas:
		List<CuentaTraba> cuentaTrabasList = new CopyOnWriteArrayList<>();
		cuentaTrabasList.addAll(traba.getCuentaTrabas());

		for(CuentaTraba cuentaTraba : cuentaTrabasList) {
			String logMessage = "Embargo "+ embargo.getCodEmbargo() +" "+ embargo.getNumeroEmbargo() +
					" traba "+ traba.getCodTraba() +
					" cuenta "+ cuentaTraba.getCodCuentaTraba() +" "+ cuentaTraba.getCuenta() +" ";

			//Para contabilizar la cuentaTraba, se tienen que cumplir todos los casos siguientes:
			// 1.- Estar en estado anterior a "Enviada a Contabilidad" -> corresponde al estado "Pendiente".
			// 2.- Estar agregada a la Traba (tener activado el flag de agregarATraba).
			// 3.- La cuentaTraba tenga estadoCuenta con valor "ACTIVE".
			if (cuentaTraba.getEstadoTraba().getCodEstado() != EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE) {
				//La cuentaTraba se encuentra en un estado donde ya ha sido enviada a Contabilidad:
				logger.info(logMessage
						+ "no se puede contabilizar ya que se encuentra en estado : [codEstado=" + cuentaTraba.getEstadoTraba().getCodEstado()
						+"; descEstado=" + cuentaTraba.getEstadoTraba().getDesEstado() + "]");

			} else if (cuentaTraba.getAgregarATraba()==null || !(EmbargosConstants.IND_FLAG_YES.equals(cuentaTraba.getAgregarATraba())
					|| EmbargosConstants.IND_FLAG_SI.equals(cuentaTraba.getAgregarATraba()))) {
				//La cuentaTraba no se ha agregado a la Traba:
				logger.info(logMessage + "no se encuentra agregada a la Traba y no se contabiliza.");

			} else if (cuentaTraba.getEstadoCuenta()==null || !EmbargosConstants.BANK_ACCOUNT_STATUS_ACTIVE.equals(cuentaTraba.getEstadoCuenta())){
				//La cuentaTraba no esta activa:
				logger.info(logMessage + "no esta activa y no se contabiliza.");

			} else if (cuentaTraba.getImporte() != null && cuentaTraba.getImporte().compareTo(BigDecimal.ZERO) <= 0) {
				logger.info(logMessage + "No hay importe a contabilizar.");
				seizureService.updateSeizedBankStatus(cuentaTraba, EmbargosConstants.COD_ESTADO_TRABA_CONTABILIZADA, userName);
			}
			else {
				if (fileControlFicheroComunes == null) {
					fileControlFicheroComunes = accountingNoteService.crearControlFichero(userName,
							EmbargosConstants.ID_APLICACION_EMBARGOS, descripcion, sucursal, ACCOUNTING_EMBARGOS_PATTERN, ACCOUNTING_IMPUESTOS_TRABAS);
				}

				logger.info(logMessage + "se contabilizará la cantidad "+ cuentaTraba.getImporte());

				apunteContableTraba(cuentaTraba, cuentaTraba.getCuenta(), cuentaRecaudacion,
						oficinaCuentaRecaudacion, reference1, reference2, detailPayment, fileControlFicheroComunes.getCodControlFichero(), embargo.getNombre(),
						embargo.getNif());

				seizureService.updateSeizedBankStatusTransaction(cuentaTraba, EmbargosConstants.COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD, userName);
			}
		}

		String codigoEstadoTraba = Long.toString(EmbargosConstants.COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD);

		SeizureStatusDTO seizureStatusDTO = new SeizureStatusDTO();
		seizureStatusDTO.setCode(codigoEstadoTraba);
		seizureService.updateSeizureStatusTransaction(traba.getCodTraba(), seizureStatusDTO, userName);

		return fileControlFicheroComunes;
	}

	private void apunteContableTraba(
			CuentaTraba cuentaTraba, String debitAccount, String creditAccount,
			String oficinaCuentaRecaudacion, String reference1, String reference2, String detailPayment,
			Long codFileControlFicheroComunes, String nombre, String nif)
			throws Exception
	{
		if (BigDecimal.ZERO.compareTo(cuentaTraba.getImporte()) < 0)
		{
			AccountingNote accountingNote = new AccountingNote();

			accountingNote.setCodFileControl(codFileControlFicheroComunes);

			accountingNote.setActualDate(new Date());
			accountingNote.setAplication(EmbargosConstants.ID_APLICACION_EMBARGOS);
			accountingNote.setCodOffice(oficinaCuentaRecaudacion);
			// estado 2
			accountingNote.setAmount(cuentaTraba.getImporte().doubleValue());
			accountingNote.setCodCurrency(cuentaTraba.getDivisa());
			accountingNote.setDebitAccount(debitAccount);
			accountingNote.setCreditAccount(creditAccount);
			accountingNote.setDebitValueDate(new Date());
			accountingNote.setCreditValueDate(new Date());
			// cod motivo "   "
			accountingNote.setExecutionDate(new Date());
			accountingNote.setReference1(reference1);
			accountingNote.setReference2(reference2);
			accountingNote.setDetailPayment(detailPayment);

			accountingNote.setName(nombre);
			accountingNote.setNif(nif);

			accountingNote.setRecaudAccount(debitAccount);
			accountingNote.setChange(cuentaTraba.getCambio());

			accountingNote.setExtraInfo1(EmbargosConstants.APUNTES_CONTABLES_TIPO_TRABA);
			accountingNote.setExtraInfo2(String.valueOf(cuentaTraba.getCodCuentaTraba()));

			accountingNoteService.contabilizar(accountingNote);
		}
	}

	private boolean esContabilizable (CuentaTraba cuentaTraba) {
		if (cuentaTraba.getEstadoTraba().getCodEstado() != EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE)
			return false;
		else if (cuentaTraba.getAgregarATraba()==null || !(EmbargosConstants.IND_FLAG_YES.equals(cuentaTraba.getAgregarATraba())
				|| EmbargosConstants.IND_FLAG_SI.equals(cuentaTraba.getAgregarATraba())))
			return false;
		else if (cuentaTraba.getEstadoCuenta()==null || !EmbargosConstants.BANK_ACCOUNT_STATUS_ACTIVE.equals(cuentaTraba.getEstadoCuenta()))
			return false;
		else if (cuentaTraba.getImporte() != null && cuentaTraba.getImporte().compareTo(BigDecimal.ZERO) <= 0)
			return false;
		return true;
	}
	
	private Pair<String,String> generateReferencesForCGPJ(String numeroEmbargo){
		
		//- Seteo de las references:
		//En el caso del CGPJ el número de embargo tiene 19 caracteres entonces: 1:16 va al primer campo (IBS_REFERENCE_1) y
		//del 17-19 pasa al inicio del siguiente campo (IBS_REFERENCE_2) seguido de los literales: Levant./Embarg y las cuatro
		//letras que identifican el organismo emisor AEAT/CGPJ etc

		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();

		if (numeroEmbargo!=null) {
			for (int i = 0; i < numeroEmbargo.length(); i++){
				char c = numeroEmbargo.charAt(i);

				if (i < 16) {
					sb1.append(c);
				} else {
					sb2.append(c);
				}
			}
		}

		//TODO: organismoEmisor revisar
		String organismoEmisor = EmbargosConstants.FILE_FORMAT_CGPJ;

		String reference1 = sb1.toString() ;
		String reference2 = sb2.append(EmbargosConstants.SEPARADOR_ESPACIO).append(EmbargosConstants.LITERAL_EMBARG_IBS_REFERENCE2)
				.append(EmbargosConstants.SEPARADOR_ESPACIO).append(organismoEmisor).toString();

		return Pair.of(reference1, reference2);
	}
/*
	@Override
	public boolean undoAccounting(Long codeFileControl, Long idSeizure, String numAccount, String userName) throws Exception{

		logger.info("undoAccounting - start");

		//Solo se puede retroceder cuando este contabilizado (se haya realizado el callback) y una vez realizado
		//el retroceso, se cambiara el estado a anterior a contabilizado.
		
		//Se obtiene la cuenta Traba en estado Contabilizada:
		EstadoTraba estadoTraba = new EstadoTraba();
		estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_CONTABILIZADA);
		
		CuentaTraba cuentaTraba = seizedBankAccountRepository.findByCodCuentaTrabaAndCuentaAndEstadoTraba(codeFileControl, idSeizure, estadoTraba);
		
		if(cuentaTraba == null) {
			throw new ICEException("No se ha encontrado la cuentaTraba [codeFileControl: " + codeFileControl + "; idSeizure: " + idSeizure + "; estadoTraba: "+ estadoTraba);
		}
		
		//Preparacion de parametros para enviar a contabilizar:

		String oficinaRecaudacion = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_OFICINA);
		String cuentaRecaudacion = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_CUENTA);

		Traba traba = cuentaTraba.getTraba();
		Embargo embargo = traba.getEmbargo();

		String reference1 = embargo.getNumeroEmbargo();
		String reference2 = "";
		String detailPayment = embargo.getDatregcomdet();

		//Llamada a contabilizar para deshacer la contabilizacion, poniendo como debitAccount la cuenta
		//de recaudacion y la creditAccount la cuenta del cliente:
		contabilizarCuentaTraba(cuentaTraba, cuentaRecaudacion, cuentaTraba.getCuenta(),
				oficinaRecaudacion, reference1, reference2, detailPayment, codeFileControl, embargo.getNombre(),
				embargo.getNif());

		//Cambio de estado de la cuentaTraba:

		estadoTraba = new EstadoTraba();
		estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_MODIFICADA);
		cuentaTraba.setEstadoTraba(estadoTraba);

		seizedBankAccountRepository.save(cuentaTraba);

		//Cambio de estado de la traba a "MODIFICADA";
		SeizureStatusDTO seizureStatusDTO = new SeizureStatusDTO();
		seizureStatusDTO.setCode(Long.toString(EmbargosConstants.COD_ESTADO_TRABA_MODIFICADA));

		boolean isStatusTrabaUpdated = seizureService.updateSeizureStatus(traba.getCodTraba(), seizureStatusDTO, userName);

		if(!isStatusTrabaUpdated) {
			throw new ICEException("No se ha actualizado el estado de la Traba con codTraba: " + traba.getCodTraba());
		}

		//Cambio de estado de Control Fichero de Embargos:
		Long codEstado = null;

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

			throw new ICEException("Formato de fichero no encontrado para el codigo de tipo de fichero "
							+ controlFichero.getTipoFichero().getCodTipoFichero() +".");
		}

		boolean isStatusFileControlUpdated = fileControlService.updateFileControlStatus(codeFileControl, codEstado, userName);

		if(!isStatusFileControlUpdated) {
			throw new ICEException("No se ha actualizado el estado del Control Fichero con codeFileControl: " + codeFileControl);
		}


		logger.info("undoAccounting - end");
		return true;
	}

	 else {
		liftingService.updateLiftingBankAccountingStatus(cuentaLevantamiento, EmbargosConstants.COD_ESTADO_LEVANTAMIENTO_CONTABILIZADO, userName);
	}

	//Se actualiza el estado de la Cuenta Levantamiento a "Enviada a contabilidad":
		liftingService.updateLiftingBankAccountingStatus(cuentaLevantamiento, EmbargosConstants.COD_ESTADO_LEVANTAMIENTO_PENDIENTE_RESPUESTA_CONTABILIZACION, userName);
*/
	@Override
	@Transactional(transactionManager="transactionManager")
	public void sendLifting(Long codeFileControl, String userName)
		throws Exception
	{
		Optional<ControlFichero> fileControlOpt = fileControlRepository.findById(codeFileControl);

		if(!fileControlOpt.isPresent())
			throw new Exception ("FileControl "+ codeFileControl + " not found");

		String oficinaRecaudacion = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_OFICINA);
		String cuentaRecaudacion = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_CUENTA);
		Long sucursal = getCodSucursal(oficinaRecaudacion);

		es.commerzbank.ice.comun.lib.domain.entity.ControlFichero fileControlFicheroComunes = accountingNoteService.crearControlFichero(userName, EmbargosConstants.ID_APLICACION_EMBARGOS, fileControlOpt.get().getDescripcion(), sucursal, ACCOUNTING_EMBARGOS_PATTERN, ACCOUNTING_IMPUESTOS_LEVANTAMIENTOS);

		for (LevantamientoTraba levantamiento : fileControlOpt.get().getLevantamientoTrabas()) {
			for (CuentaLevantamiento cuentaLevantamiento : levantamiento.getCuentaLevantamientos()) {
				apunteContableLevantamiento(cuentaLevantamiento, levantamiento.getTraba().getEmbargo(),
						fileControlFicheroComunes.getCodControlFichero(), oficinaRecaudacion, cuentaRecaudacion);
			}
		}

		accountingNoteService.generacionFicheroContabilidad(fileControlFicheroComunes);
	}

	private void apunteContableLevantamiento(
			CuentaLevantamiento cuentaLevantamiento, Embargo embargo, Long codFileControlFicheroComunes,
			String oficinaCuentaRecaudacion, String cuentaRecaudacion)
			throws Exception {
		if (BigDecimal.ZERO.compareTo(cuentaLevantamiento.getImporte()) < 0)
		{
			String reference1 = embargo.getNumeroEmbargo();
			String reference2 = "";
			String detailPayment = embargo.getDatregcomdet();

			AccountingNote accountingNote = new AccountingNote();

			accountingNote.setAplication(EmbargosConstants.ID_APLICACION_EMBARGOS);
			accountingNote.setCodOffice(oficinaCuentaRecaudacion.toString());
			accountingNote.setAmount(cuentaLevantamiento.getImporte().doubleValue());
			accountingNote.setCodCurrency(cuentaLevantamiento.getCodDivisa());
			accountingNote.setDebitAccount(cuentaRecaudacion);
			accountingNote.setRecaudAccount(cuentaRecaudacion);
			//accountingNote.setCreditAccount(cuentaLevantamiento.getCuenta());
			accountingNote.setActualDate(new Date());
			accountingNote.setExecutionDate(new Date());
			accountingNote.setDebitValueDate(new Date());
			accountingNote.setCreditValueDate(new Date());
			accountingNote.setReference1(reference1);
			accountingNote.setReference2(reference2);
			accountingNote.setChange(cuentaLevantamiento.getCambio());

			accountingNote.setCodFileControl(codFileControlFicheroComunes);
			accountingNote.setName(embargo.getNombre());
			accountingNote.setNif(embargo.getNif());
			accountingNote.setDetailPayment(detailPayment);

			accountingNote.setCreditAccount(cuentaLevantamiento.getCuenta());
			accountingNote.setExtraInfo1(EmbargosConstants.APUNTES_CONTABLES_TIPO_LEVANTAMIENTO);
			accountingNote.setExtraInfo2(String.valueOf(cuentaLevantamiento.getCodCuentaLevantamiento()));

			accountingNoteService.contabilizar(accountingNote);
		}
	}

	@Override
	@Transactional(transactionManager="transactionManager")
	public es.commerzbank.ice.comun.lib.domain.entity.ControlFichero sendAccountingLiftingBankAccount(CuentaLevantamiento cuentaLevantamiento, Embargo embargo, String userName)
			throws Exception
	{
		return null;
		/* TODO ELIMINAR - el levantamiento se hace en sendAccountingLifting con todo el fichero en bloque
		TODO A eliminar cuando se pase por F5.
		String oficinaRecaudacion = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_OFICINA);
		String cuentaRecaudacion = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_CUENTA);
		
		Long sucursal = null;
		try {
			Optional<Sucursal> sucursalOpt = officeCRepo.findByNumeroSucursal(new BigDecimal(oficinaRecaudacion));
			if (sucursalOpt.isPresent()) {
				sucursal = sucursalOpt.get().getCodSucursal();
			}
		} catch (Exception e) {
			logger.error("sendAccountingAEATCuaderno63 - error obteniendo código de oficina", e);	
		}
		
		String contabilizacionCallbackNameParameter = EmbargosConstants.PARAMETRO_EMBARGOS_CONTABILIZACION_FASE5_CALLBACK;
		Long codFileControlFicheroComunes = null;
		es.commerzbank.ice.comun.lib.domain.entity.ControlFichero fileControlFicheroComunes = null;
		
		if (cuentaLevantamiento.getImporte().doubleValue() > 0) {
			fileControlFicheroComunes = accountingNoteService.crearControlFichero(userName, EmbargosConstants.ID_APLICACION_EMBARGOS, embargo.getControlFichero().getDescripcion(), sucursal, ACCOUNTING_EMBARGOS_PATTERN, ACCOUNTING_IMPUESTOS_LEVANTAMIENTOS);
			codFileControlFicheroComunes = fileControlFicheroComunes.getCodControlFichero();
		}
		
		if (codFileControlFicheroComunes != null) {
			apunteContableLevantamiento(cuentaLevantamiento, embargo, codFileControlFicheroComunes, userName, oficinaRecaudacion, cuentaRecaudacion);
		}

		return fileControlFicheroComunes;
		 */
	}

	@Override
	@Transactional(transactionManager="transactionManager")
	public void sendFinalFile(Long codeFileControl, String userName) throws ICEException, Exception {
		
		//Para las Entidades Comunicadoras que tengan cuenta en Commerzbank, la transferencia
		//de la Fase 6 (cierre) se hara internamente llamando a contabilizar():
		
		logger.info("sendAccountingFinalFile - start");
				
		//- Se obtiene el fichero de control:
		Optional<ControlFichero> fileControlOpt = fileControlRepository.findById(codeFileControl);
		if(!fileControlOpt.isPresent()) {
			throw new ICEException("ControlFichero not found with codeFileControl: " + codeFileControl);
		}
		ControlFichero controlFichero = fileControlOpt.get();
		
		//- Se comprueba si la Entidad comunicadora asociada al fichero, tiene cuenta interna en Commerzbank:		
		EntidadesComunicadora entidadComunicadora = controlFichero.getEntidadesComunicadora();
		if (entidadComunicadora == null) {			
			throw new ICEException("EntidadComunicadora not found for the fileControl with codeFileControl: " + codeFileControl);
		}
		
		if (entidadComunicadora.getCuenta() == null || entidadComunicadora.getCuenta().trim().isEmpty()) {
			logger.info("Could not be sent to accounting: account not found for the Entidad Comunicadora with NIF: " + entidadComunicadora.getNifEntidad());
			return;
		}
		
		//Se obtiene el registro de FicheroFinal asociado al controlFichero:		
		List<FicheroFinal> ficheroFinalList = controlFichero.getFicheroFinals();
		
		if (ficheroFinalList==null || ficheroFinalList.isEmpty()) {
			logger.info("Could not be sent to accounting: Fichero Final not found for codeFileControl: " + controlFichero.getCodControlFichero());	
			return;
		}
		
		FicheroFinal ficheroFinal = ficheroFinalList.get(0);
		
		//Obtencion de datos para setear:
		String oficinaRecaudacion = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_OFICINA);
		String cuentaRecaudacion = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_CUENTA);
		Long sucursal = getCodSucursal(oficinaRecaudacion);
		String cuentaEntidadComunicadora = entidadComunicadora.getCuenta();
		
		String divisa = EmbargosConstants.ISO_MONEDA_EUR;
		BigDecimal cambio = null;
		String contabilizacionCallbackNameParameter = EmbargosConstants.PARAMETRO_EMBARGOS_CONTABILIZACION_FASE6_CALLBACK;
		String reference1 = Long.toString(ficheroFinal.getControlFichero().getCodControlFichero());
		String reference2 = null;
		String detailPayment = EmbargosConstants.LITERAL_DETAILPAYMENT_FASE6_EMB_TRANSFERRED_TO + entidadComunicadora.getDesEntidad();
		String name = null;
		String nif = null;
		
		double amount = ficheroFinal.getImporte()!=null ? ficheroFinal.getImporte().doubleValue() : 0;
		
		AccountingNote accountingNote = new AccountingNote();
		
		if (amount != 0) {
			//Si el importe a contabilizar no es 0:

			es.commerzbank.ice.comun.lib.domain.entity.ControlFichero fileControlFicheroComunes = accountingNoteService.crearControlFichero(userName, EmbargosConstants.ID_APLICACION_EMBARGOS, controlFichero.getDescripcion(), sucursal, ACCOUNTING_EMBARGOS_PATTERN, ACCOUNTING_EMBARGO_F6);
			Long codFileControlFicheroComunes = fileControlFicheroComunes.getCodControlFichero();
			
			accountingNote.setAplication(EmbargosConstants.ID_APLICACION_EMBARGOS);
			accountingNote.setCodOffice(oficinaRecaudacion);
			accountingNote.setAmount(amount);
			accountingNote.setCodCurrency(divisa);
			accountingNote.setDebitAccount(cuentaRecaudacion);
			accountingNote.setRecaudAccount(cuentaRecaudacion);
			accountingNote.setCreditAccount(cuentaEntidadComunicadora);
			accountingNote.setActualDate(new Date());
			accountingNote.setExecutionDate(new Date());
			accountingNote.setDebitValueDate(new Date());
			accountingNote.setCreditValueDate(new Date());
			accountingNote.setReference1(reference1);
			accountingNote.setReference2(reference2);
			accountingNote.setChange(cambio);
			accountingNote.setCallback(contabilizacionCallbackNameParameter);
			accountingNote.setCodFileControl(codFileControlFicheroComunes);
			accountingNote.setName(name);
			accountingNote.setNif(nif);	
			accountingNote.setDetailPayment(detailPayment);
			accountingNote.setExtraInfo1(EmbargosConstants.APUNTES_CONTABLES_TIPO_TESORERIA);
			
			accountingNoteService.contabilizar(accountingNote);

			finalFileService.updateFinalFileAccountingStatus(ficheroFinal, EmbargosConstants.COD_ESTADO_CONTABILIZACION_ENVIADA_A_CONTABILIDAD, userName);

			accountingNoteService.generacionFicheroContabilidad(fileControlFicheroComunes);
		} else {
			
			//Si el importe es 0 -> no se llama a contabilizar y se actualiza el estado a 'Contabilizada':
			
			finalFileService.updateFinalFileAccountingStatus(ficheroFinal, EmbargosConstants.COD_ESTADO_CONTABILIZACION_CONTABILIZADA, userName);	
		}	
		
		logger.info("sendAccountingFinalFile - end");
	}

	@Override
	@Transactional(transactionManager="transactionManager")
	public void seizureCallback(Long codCuentaTraba) {
		Optional<CuentaTraba> opt = seizedBankAccountRepository.findById(codCuentaTraba);

		if (!opt.isPresent()) {
			logger.error("No se ha encontrado la cuentaTraba código "+ codCuentaTraba);
			return;
		}

		CuentaTraba cuentaTraba = opt.get();

		// Se cambia el estado de la Cuenta Traba a Contabilizada
		seizureService.updateSeizedBankStatus(cuentaTraba, EmbargosConstants.COD_ESTADO_TRABA_CONTABILIZADA, USER_AUTOMATICO);

		// Si todas las CuentaTraba asociadas a la Traba han cambiado a estado "Contabilizada", entonces:
		// Cambiar el estado de la Traba a "Contabilizada":

		Traba traba = cuentaTraba.getTraba();

		boolean isAllCuentaTrabasContabilizadas = true;
		for(CuentaTraba cuentaTr : traba.getCuentaTrabas()) {
			if (cuentaTr.getEstadoTraba().getCodEstado() == EmbargosConstants.COD_ESTADO_TRABA_CONTABILIZADA || !esContabilizable(cuentaTr)) {
				; // está contabilizada o no es contabilizada. ok
			}
			else {
				isAllCuentaTrabasContabilizadas = false;
			}
		}

		if (!isAllCuentaTrabasContabilizadas)
			return;

		logger.info("Todas las cuentas de la traba "+ traba.getCodTraba() +" se han tratado. Cambiando el estado a contabilizada");

		SeizureStatusDTO seizureStatusDTO = new SeizureStatusDTO();
		seizureStatusDTO.setCode(String.valueOf(EmbargosConstants.COD_ESTADO_TRABA_CONTABILIZADA));

		seizureService.updateSeizureStatus(traba.getCodTraba(), seizureStatusDTO, USER_AUTOMATICO);


		// Si todas las Trabas estan contabilizadas, entonces:
		// Cambiar el estado de Control Fichero de Embargos (control fichero de la Traba no) a estado "Pendiente de envio".

		Embargo embargo = traba.getEmbargo();
		ControlFichero controlFichero = embargo.getControlFichero();

		boolean isAllTrabasContabilizadas = true;
		for (Embargo emb : controlFichero.getEmbargos()) {
			Traba currentTraba = emb.getTrabas().get(0);

			if (currentTraba.getEstadoTraba().getCodEstado() != EmbargosConstants.COD_ESTADO_TRABA_CONTABILIZADA) {
				isAllTrabasContabilizadas = false;
			}
		}

		if (isAllTrabasContabilizadas)
		{
			logger.info("ControlFichero con id " + controlFichero.getCodControlFichero() + " cambia a estado 'Pendiente de envio");

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
			fileControlService.updateFileControlStatusTransaction(controlFichero, estado, USER_AUTOMATICO);
		}
	}

	@Override
	@Transactional(transactionManager="transactionManager")
	public void transferenciaFinalOrganismoCallback(Long codControlFicheroFinal) {
		
		ControlFichero controlFicheroFinal = new ControlFichero();
		controlFicheroFinal.setCodControlFichero(codControlFicheroFinal);
		
		FicheroFinal ficheroFinal = finalFileRepository.findByControlFichero(controlFicheroFinal);
		
		if (ficheroFinal == null) {
			Log.error ("FicheroFinal not found with codControlFichero=" + codControlFicheroFinal);
		}

		finalFileService.updateFinalFileAccountingStatus(ficheroFinal, EmbargosConstants.COD_ESTADO_CONTABILIZACION_CONTABILIZADA, USER_AUTOMATICO);
	}

	@Override
	@Transactional(transactionManager="transactionManager")
	public void liftingCallback(Long codCuentaLevantamiento)
	{
		Optional<CuentaLevantamiento> opt = liftingBankAccountRepository.findById(codCuentaLevantamiento);

		if (!opt.isPresent())
		{
			Log.error("No se encuentra el código de cuenta levantamiento "+ codCuentaLevantamiento);
			return;
		}

		CuentaLevantamiento	cuentaLevantamiento = opt.get();

		// Actualizar cuenta
		liftingService.updateLiftingBankAccountingStatus(cuentaLevantamiento, EmbargosConstants.COD_ESTADO_LEVANTAMIENTO_CONTABILIZADO, USER_AUTOMATICO);

		// Si todas las cuentsa levantamiento del levantamiento están contabilizadas, avanzar el estado del levantamiento
		LevantamientoTraba levantamientoTraba = cuentaLevantamiento.getLevantamientoTraba();
		boolean isAllCuentaLevantamientoContabilizados = true;
		for(CuentaLevantamiento currentCuentaLevantamiento : levantamientoTraba.getCuentaLevantamientos()) {
			if (currentCuentaLevantamiento.getEstadoLevantamiento().getCodEstado() != EmbargosConstants.COD_ESTADO_LEVANTAMIENTO_CONTABILIZADO) {
				isAllCuentaLevantamientoContabilizados = false;
				break;
			}
		}

		if (!isAllCuentaLevantamientoContabilizados)
			return;

		Log.info("Actualizando el levantamiento "+ levantamientoTraba.getCodLevantamiento() +" a estado 'Contabilizado'");
		liftingService.updateLiftingtatus(levantamientoTraba, EmbargosConstants.COD_ESTADO_LEVANTAMIENTO_CONTABILIZADO, USER_AUTOMATICO);

		//4. Si todos los levantamientos del fichero están contabilziados, avanzar el estado del fichero

		ControlFichero controlFichero = levantamientoTraba.getControlFichero();

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

			fileControlService.updateFileControlStatusTransaction(controlFichero, estado, USER_AUTOMATICO);
		}
	}

	private Long getCodSucursal (String oficinaRecaudacion) throws Exception {
		Optional<Sucursal> sucursalOpt = officeCRepo.findByNumeroSucursal(new BigDecimal(oficinaRecaudacion));
		if (sucursalOpt.isPresent()) {
			return sucursalOpt.get().getCodSucursal();
		}

		throw new Exception("Oficina con codigo "+ oficinaRecaudacion +" no definida");
	}
}