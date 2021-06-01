package es.commerzbank.ice.embargos.service.impl;

import es.commerzbank.ice.comun.lib.domain.dto.AccountingNote;
import es.commerzbank.ice.comun.lib.domain.entity.Sucursal;
import es.commerzbank.ice.comun.lib.repository.OfficeCRepo;
import es.commerzbank.ice.comun.lib.service.AccountingNoteService;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.embargos.domain.dto.AccountStatusLiftingDTO;
import es.commerzbank.ice.embargos.domain.dto.AccountingPendingDTO;
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
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import static es.commerzbank.ice.embargos.utils.EmbargosConstants.*;

@Service
@Transactional(transactionManager="transactionManager")
public class AccountingServiceImpl
	implements AccountingService
{
	
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
	private FinalResponseService finalResponseService;
	
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
	
	//@Autowired
	//private AccountService accountService;
	
	@Autowired
	private OfficeCRepo officeCRepo;

	@Autowired
	private SolicitudTrabaRepository solicitudTrabaRepository;

	@Autowired
	private SolicitudLevantamientoRepository solicitudLevantamientoRepository;

	@Override
	@Transactional(transactionManager="transactionManager")
	public void embargoContabilizar(Long codeFileControl, String userName) throws ICEException, Exception {
	
		logger.info("sendAccountingSeizure - start "+ codeFileControl);
		
		//Se obtiene el fichero de control:
		Optional<ControlFichero> fileControlOpt = fileControlRepository.findById(codeFileControl);
		if(!fileControlOpt.isPresent()) {
			return;
		}
		ControlFichero controlFichero = fileControlOpt.get();
				
		//Dependiendo del tipo de fichero:
		String fileFormat = EmbargosUtils.determineFileFormatByTipoFichero(controlFichero.getTipoFichero().getCodTipoFichero());
		
		boolean isCGPJ = fileFormat!=null && fileFormat.equals(FILE_FORMAT_CGPJ);
		boolean isAEAT = fileFormat!=null && fileFormat.equals(FILE_FORMAT_AEAT);
		boolean isCuaderno63 = fileFormat!=null && fileFormat.equals(FILE_FORMAT_NORMA63);
		
		//boolean isAccountingSent = false;
		
		long codEstadoCtrlFichero = controlFichero.getEstadoCtrlfichero().getId().getCodEstado();
		
		if (isCGPJ)
			throw new ICEException("Los ficheros del Consejo no se contabilizan a nivel de fichero");

		if (isAEAT) {
			//Para contabilizar, el estado de ControlFichero tiene que ser previo o igual a "Recibido":
			if (codEstadoCtrlFichero != COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_RECEIVED) {

				logger.error(generateMessageCtrlFicheroCannotSendAccounting(controlFichero));

				throw new ICEException("ERROR estado no adecuado");
			}
			
			boolean contabilizado = sendSeizureAEATCuaderno63(controlFichero, userName);

			if (contabilizado)
				fileControlService.updateFileControlStatus(controlFichero.getCodControlFichero(),
					COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PENDING_ACCOUNTING_RESPONSE, userName, null);
			else
				fileControlService.updateFileControlStatus(controlFichero.getCodControlFichero(),
					COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PENDING_TO_SEND, userName, null);
		}
		else if (isCuaderno63)
		{
			//Para contabilizar, el estado de ControlFichero tiene que ser previo o igual a "Recibido":
			if (codEstadoCtrlFichero != COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_RECEIVED) {
			
				logger.error(generateMessageCtrlFicheroCannotSendAccounting(controlFichero));
				
				throw new ICEException("ERROR estado no adecuado");
			}

			boolean contabilizado = sendSeizureAEATCuaderno63(controlFichero, userName);

			if (contabilizado)
				fileControlService.updateFileControlStatus(controlFichero.getCodControlFichero(),
					COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_PENDING_ACCOUNTING_RESPONSE, userName, null);
			else
				fileControlService.updateFileControlStatus(controlFichero.getCodControlFichero(),
					COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_PENDING_TO_SEND, userName, null);
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

	// Devuelve true si se ha contabilizado algo
	private boolean sendSeizureAEATCuaderno63(ControlFichero controlFichero, String userName) throws Exception {

		String oficinaRecaudacion = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_OFICINA);
		String cuentaRecaudacion = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_CUENTA);
		String cuentaIntercambioDivisas = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_INTERCAMBIO_DIVISAS);

		Long sucursal = getCodSucursal(oficinaRecaudacion);

		es.commerzbank.ice.comun.lib.domain.entity.ControlFichero fileControlFicheroComunes = null;

		//Se obtienen la trabas asociadas al fichero:
		for (Embargo embargo : controlFichero.getEmbargos()) {
			
			Traba traba = embargo.getTrabas().get(0);

			String reference1 = embargo.getNumeroEmbargo();
			String reference2 = "";
			String detailPayment = embargo.getDatregcomdet();

			fileControlFicheroComunes = contabilizarTraba(
					fileControlFicheroComunes, ACCOUNTING_EMBARGOS_TRABAS,
					embargo,
					traba,
					userName, sucursal,
					cuentaRecaudacion, oficinaRecaudacion, reference1, reference2, detailPayment, cuentaIntercambioDivisas);
		}
		
		if (fileControlFicheroComunes != null) {
			accountingNoteService.generacionFicheroContabilidad(fileControlFicheroComunes);
			return true;
		}

		return false;
	}

	@Override
	public long CGPJContabilizar(List<AccountingPendingDTO> pendientes, String userName)
			throws Exception
	{
		String oficinaRecaudacion = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_OFICINA);
		String cuentaRecaudacion = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_CUENTA);
		String cuentaIntercambioDivisas = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_INTERCAMBIO_DIVISAS);
		Long sucursal = getCodSucursal(oficinaRecaudacion);
		
		es.commerzbank.ice.comun.lib.domain.entity.ControlFichero fileControlFicheroComunes = null;

		for (AccountingPendingDTO pendiente : pendientes) {

			if ("TRABA".equals(pendiente.getTipo())) {
				Optional<SolicitudesTraba> solicitudesTrabaOpt = solicitudTrabaRepository.findById(pendiente.getCodSolicitud());

				if (!solicitudesTrabaOpt.isPresent()) {
					logger.error("No se ha encontrado la solicitud de traba "+ pendiente.getCodSolicitud());
					continue;
				}

				Traba traba = solicitudesTrabaOpt.get().getTraba();
				Embargo embargo = traba.getEmbargo();

				//- Generacion de las references para CGPJ:
				Pair<String,String> references = generateReferencesForCGPJ(embargo.getNumeroEmbargo());
				String detailPayment = "";//embargo.getDatregcomdet();

				fileControlFicheroComunes = contabilizarTraba(fileControlFicheroComunes, ACCOUNTING_EMBARGOS_CGPJ, embargo, traba,
						userName, sucursal, cuentaRecaudacion, oficinaRecaudacion,
						references.getLeft(), references.getRight(), detailPayment, cuentaIntercambioDivisas);

			}
			else if ("LEVANTAMIENTO".equals(pendiente.getTipo())) {
				Optional<SolicitudesLevantamiento> solicitudesLevantamientoOpt = solicitudLevantamientoRepository.findById(pendiente.getCodSolicitud());

				if (!solicitudesLevantamientoOpt.isPresent()) {
					logger.error("No se ha encontrado la solicitud de traba "+ pendiente.getCodSolicitud());
					continue;
				}

				// TODO què fer
			}
		}
		
		if (fileControlFicheroComunes != null) {
			accountingNoteService.generacionFicheroContabilidad(fileControlFicheroComunes);
		}

		return fileControlFicheroComunes.getCodControlFichero();
	}

	private es.commerzbank.ice.comun.lib.domain.entity.ControlFichero contabilizarTraba(
			es.commerzbank.ice.comun.lib.domain.entity.ControlFichero fileControlFicheroComunes, String contentType,
			Embargo embargo,
			Traba traba,
			String userName, Long sucursal,
			String cuentaRecaudacion, String oficinaCuentaRecaudacion,
			String reference1, String reference2, String detailPayment, String cuentaIntercambioDivisas
	) throws Exception
	{
		if (traba.getEstadoTraba().getCodEstado() != EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE) {
			logger.info("La traba con id " + traba.getCodTraba()
					+ " no se puede contabilizar ya que se encuentra en estado : [codEstado=" + traba.getEstadoTraba().getCodEstado()
					+"; descEstado=" + traba.getEstadoTraba().getDesEstado() + "]");
			return null;
		}

		//Se utiliza CopyOnWriteArrayList para evitar ConcurrentModificationException en el
		//update del estado de la cuentaTraba en la iteracion del listado de cuentaTrabas:
		List<CuentaTraba> cuentaTrabasList = new CopyOnWriteArrayList<>();
		cuentaTrabasList.addAll(traba.getCuentaTrabas());

		boolean algunaCuentaContabilizada = false;
		for(CuentaTraba cuentaTraba : cuentaTrabasList)
		{
			String logMessage = "Embargo "+ embargo.getCodEmbargo() +" "+ embargo.getNumeroEmbargo() +
					" traba "+ traba.getCodTraba() +
					" cuenta "+ cuentaTraba.getCodCuentaTraba() +" "+ cuentaTraba.getCuenta() +" ";
			boolean contabilizado = false;


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

			} else if (cuentaTraba.getImporte() == null) {
				logger.info(logMessage + "El importe es nulo.");
			} else if (cuentaTraba.getImporte() != null && cuentaTraba.getImporte().compareTo(BigDecimal.ZERO) <= 0) {
				logger.info(logMessage + "No hay importe a contabilizar.");
			}
			else {
				contabilizado = true;
				algunaCuentaContabilizada = true;

				if (fileControlFicheroComunes == null) {
					fileControlFicheroComunes = accountingNoteService.crearControlFichero(userName,
							EmbargosConstants.ID_APLICACION_EMBARGOS, null, sucursal, ACCOUNTING_EMBARGOS_PATTERN, contentType, ACCOUNTING_EMBARGOS_EXTENSION);
				}

				logger.info(logMessage + "se contabilizará la cantidad "+ cuentaTraba.getImporte());

				apunteContableTraba(cuentaTraba, cuentaTraba.getCuenta(), cuentaRecaudacion,
						oficinaCuentaRecaudacion, reference1, reference2, detailPayment, fileControlFicheroComunes.getCodControlFichero(), embargo.getNombre(),
						embargo.getNif(), cuentaIntercambioDivisas);
			}

			if (contabilizado) {
				seizureService.updateSeizedBankStatus(cuentaTraba, COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD, userName);
			}
			else {
				seizureService.updateSeizedBankStatus(cuentaTraba, COD_ESTADO_TRABA_FINALIZADA, userName);
			}
		}

		String codigoEstadoTraba = null;

		if (algunaCuentaContabilizada)
			codigoEstadoTraba = Long.toString(EmbargosConstants.COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD);
		else
			codigoEstadoTraba = Long.toString(COD_ESTADO_TRABA_FINALIZADA);

		SeizureStatusDTO seizureStatusDTO = new SeizureStatusDTO();
		seizureStatusDTO.setCode(codigoEstadoTraba);
		seizureService.updateSeizureStatusTransaction(traba.getCodTraba(), seizureStatusDTO, userName);

		return fileControlFicheroComunes;
	}

	private void apunteContableTraba(
			CuentaTraba cuentaTraba, String debitAccount, String creditAccount,
			String oficinaCuentaRecaudacion, String reference1, String reference2, String detailPayment,
			Long codFileControlFicheroComunes, String nombre, String nif, String cuentaIntercambioDivisas)
			throws Exception
	{
		if (cuentaTraba.getImporte()!=null && BigDecimal.ZERO.compareTo(cuentaTraba.getImporte()) < 0)
		{
			AccountingNote accountingNote = new AccountingNote();

			accountingNote.setCodFileControl(codFileControlFicheroComunes);

			accountingNote.setActualDate(new Date());
			accountingNote.setAplication(EmbargosConstants.ID_APLICACION_EMBARGOS);
			accountingNote.setCodOffice(oficinaCuentaRecaudacion);
			accountingNote.setAmount(cuentaTraba.getImporte().doubleValue());
			accountingNote.setCodCurrency(cuentaTraba.getDivisa());
			accountingNote.setDebitAccount(debitAccount);
			accountingNote.setCreditAccount(creditAccount);
			accountingNote.setDebitValueDate(new Date());
			accountingNote.setCreditValueDate(new Date());
			accountingNote.setExecutionDate(new Date());
			accountingNote.setReference1(reference1);
			accountingNote.setReference2(reference2);
			accountingNote.setDetailPayment(detailPayment);
			accountingNote.setEsCredito(false);
			accountingNote.setChange(cuentaTraba.getCambio());

			// borrar los tres siguientes?
			accountingNote.setName(nombre);
			accountingNote.setNif(nif);
			accountingNote.setRecaudAccount(debitAccount);

			accountingNote.setExtraInfo1(EmbargosConstants.APUNTES_CONTABLES_TIPO_TRABA);
			accountingNote.setExtraInfo2(String.valueOf(cuentaTraba.getCodCuentaTraba()));

			accountingNoteService.contabilizar(accountingNote, cuentaIntercambioDivisas);
		}
	}

	private void apunteContableFicheroFinal(
			BigDecimal importe, String divisa, String debitAccount, String creditAccount,
			String oficinaCuentaRecaudacion, String reference1, String reference2, String detailPayment,
			Long codFileControlFicheroComunes, String nombre, String nif, String cuentaIntercambioDivisas, BigDecimal cambio,
			String extraInfo1, String extraInfo2)
			throws Exception
	{
		if (importe != null && BigDecimal.ZERO.compareTo(importe) < 0)
		{
			AccountingNote accountingNote = new AccountingNote();

			accountingNote.setCodFileControl(codFileControlFicheroComunes);

			accountingNote.setActualDate(new Date());
			accountingNote.setAplication(EmbargosConstants.ID_APLICACION_EMBARGOS);
			accountingNote.setCodOffice(oficinaCuentaRecaudacion);
			accountingNote.setAmount(importe.doubleValue());
			accountingNote.setCodCurrency(divisa);
			accountingNote.setDebitAccount(debitAccount);
			accountingNote.setCreditAccount(creditAccount);
			accountingNote.setDebitValueDate(new Date());
			accountingNote.setCreditValueDate(new Date());
			accountingNote.setExecutionDate(new Date());
			accountingNote.setReference1(reference1);
			accountingNote.setReference2(reference2);
			accountingNote.setDetailPayment(detailPayment);
			accountingNote.setEsCredito(false);
			accountingNote.setChange(cambio);

			// borrar los tres siguientes?
			accountingNote.setName(nombre);
			accountingNote.setNif(nif);
			accountingNote.setRecaudAccount(debitAccount);

			accountingNote.setExtraInfo1(extraInfo1);
			accountingNote.setExtraInfo2(extraInfo2);

			accountingNoteService.contabilizar(accountingNote, cuentaIntercambioDivisas);
		}
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

	@Override
	public void extornoContabilizar(ControlFichero controlFichero, Traba traba, CuentaTraba cuentaTraba, String userName)
		throws Exception
	{
		String oficinaRecaudacion = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_OFICINA);
		String cuentaRecaudacion = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_CUENTA);
		String cuentaIntercambioDivisas = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_INTERCAMBIO_DIVISAS);
		Long sucursal = getCodSucursal(oficinaRecaudacion);

		String fileFormat = EmbargosUtils.determineFileFormatByTipoFichero(controlFichero.getTipoFichero().getCodTipoFichero());
		boolean isCGPJ = fileFormat!=null && fileFormat.equals(FILE_FORMAT_CGPJ);
		
		Embargo embargo = traba.getEmbargo();

		es.commerzbank.ice.comun.lib.domain.entity.ControlFichero fileControlFicheroComunes = accountingNoteService.crearControlFichero(userName, EmbargosConstants.ID_APLICACION_EMBARGOS, null, sucursal, ACCOUNTING_EMBARGOS_PATTERN, ACCOUNTING_EMBARGOS_EXTORNO, ACCOUNTING_EMBARGOS_EXTENSION);

		String detail = null;
		String reference1 = null;
		String reference2 = null;

		if (isCGPJ) {
			Pair<String, String> references = generateReferencesForCGPJ(embargo.getNumeroEmbargo());
			reference1 = references.getLeft();
			reference2 = references.getRight();
			detail = EmbargosConstants.DETAIL_PAYMENT_UNDO_ACCOUNTING;
		}
		else {
			reference1 = embargo.getNumeroEmbargo();
			reference2 = "";
			detail = EmbargosConstants.DETAIL_PAYMENT_UNDO_ACCOUNTING +" "+ embargo.getDatregcomdet();
		}

		apunteContableExtorno(cuentaTraba, cuentaRecaudacion, cuentaTraba.getCuenta(),
				oficinaRecaudacion, reference1, reference2, detail, cuentaIntercambioDivisas,
				fileControlFicheroComunes.getCodControlFichero(), embargo.getNombre(), embargo.getNif());

		accountingNoteService.generacionFicheroContabilidad(fileControlFicheroComunes);
	}

	private void apunteContableExtorno(
			CuentaTraba cuentaTraba, String debitAccount, String creditAccount,
			String oficinaCuentaRecaudacion, String reference1, String reference2, String detailPayment, String cuentaIntercambioDivisas,
			Long codFileControlFicheroComunes, String nombre, String nif)
			throws Exception {
		
		if (cuentaTraba.getImporte()!=null && BigDecimal.ZERO.compareTo(cuentaTraba.getImporte()) < 0)
		{
			AccountingNote accountingNote = new AccountingNote();

			accountingNote.setCodFileControl(codFileControlFicheroComunes);

			accountingNote.setActualDate(new Date());
			accountingNote.setAplication(EmbargosConstants.ID_APLICACION_EMBARGOS);
			accountingNote.setCodOffice(oficinaCuentaRecaudacion);
			accountingNote.setAmount(cuentaTraba.getImporte().doubleValue());
			accountingNote.setCodCurrency(cuentaTraba.getDivisa());
			accountingNote.setDebitAccount(debitAccount);
			accountingNote.setCreditAccount(creditAccount);
			accountingNote.setDebitValueDate(new Date());
			accountingNote.setCreditValueDate(new Date());
			accountingNote.setExecutionDate(new Date());
			accountingNote.setReference1(reference1);
			accountingNote.setReference2(reference2);
			accountingNote.setDetailPayment(detailPayment);
			accountingNote.setChange(cuentaTraba.getCambio());
			accountingNote.setEsCredito(true);
			// estos sobran?
			accountingNote.setName(nombre);
			accountingNote.setNif(nif);

			accountingNote.setExtraInfo1(EmbargosConstants.APUNTES_CONTABLES_TIPO_EXTORNO);
			accountingNote.setExtraInfo2(String.valueOf(cuentaTraba.getCodCuentaTraba()));

			accountingNoteService.contabilizar(accountingNote, cuentaIntercambioDivisas);
		}
	}

	@Override
	@Transactional(transactionManager="transactionManager")
	public void extornoCallback(Long codCuentaTraba) {
		logger.info("Callback extorno "+ codCuentaTraba +" recibido - sin acción");
	}
	
	@Override
	@Transactional(transactionManager="transactionManager")
	public void levantamientoContabilizar(Long codeFileControl, String username) throws Exception {
		Optional<ControlFichero> fileControlOpt = fileControlRepository.findById(codeFileControl);

		if(!fileControlOpt.isPresent())
			throw new Exception ("FileControl "+ codeFileControl + " not found");

		ControlFichero controlFichero = fileControlOpt.get();

		String oficinaRecaudacion = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_OFICINA);
		String cuentaRecaudacion = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_CUENTA);
		String cuentaIntercambioDivisas = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_INTERCAMBIO_DIVISAS);
		Long sucursal = getCodSucursal(oficinaRecaudacion);

		es.commerzbank.ice.comun.lib.domain.entity.ControlFichero fileControlFicheroComunes = null;

		for (LevantamientoTraba levantamiento : controlFichero.getLevantamientoTrabas()) {
			fileControlFicheroComunes = contabilizarLevantamiento(username, ACCOUNTING_EMBARGOS_LEVANTAMIENTOS, fileControlFicheroComunes, levantamiento, cuentaRecaudacion, cuentaIntercambioDivisas, oficinaRecaudacion, sucursal);
		}

		accountingNoteService.generacionFicheroContabilidad(fileControlFicheroComunes);

		fileControlService.updateFileControlStatus(controlFichero.getCodControlFichero(), COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_PENDING_ACCOUNTING_RESPONSE, username, null);
	}

	private es.commerzbank.ice.comun.lib.domain.entity.ControlFichero contabilizarLevantamiento (
			String username, String contentType,
			es.commerzbank.ice.comun.lib.domain.entity.ControlFichero fileControlFicheroComunes,
			LevantamientoTraba levantamiento, String cuentaRecaudacion,
			String cuentaIntercambioDivisas, String oficinaRecaudacion, Long sucursal)
			throws Exception
	{
		for (CuentaLevantamiento cuentaLevantamiento : levantamiento.getCuentaLevantamientos()) {
			if (cuentaLevantamiento.getEstadoLevantamiento().getCodEstado() != COD_ESTADO_LEVANTAMIENTO_PENDIENTE) {
				logger.info("cuenta levantamiento "+ cuentaLevantamiento.getCodCuentaLevantamiento() + " no pendiente - ignorando");
				continue;
			}

			logger.info("cuenta levantamiento "+ cuentaLevantamiento.getCodCuentaLevantamiento() + " se contabilizará la cantidad "+ cuentaLevantamiento.getImporte());

			Traba traba = levantamiento.getTraba();
			Embargo embargo = traba.getEmbargo();

			CuentaTraba cuentaTraba = null;
			for (CuentaTraba cuentaTrabaActual : traba.getCuentaTrabas()) {
				if (cuentaLevantamiento.getCuenta().equals(cuentaTrabaActual.getCuenta())) {
					cuentaTraba = cuentaTrabaActual;
					break;
				}
			}
			if (cuentaTraba == null) {
				throw new Exception("No se encuentra la cuenta traba cuya cuenta sea igual a la cuenta de levantamiento "+ cuentaLevantamiento.getCodCuentaLevantamiento() +" "+ cuentaLevantamiento.getCuenta());
			}
			else {
				BigDecimal cambioInverso = cuentaTraba.getCambio() == null ? null : BigDecimal.ONE.divide(cuentaTraba.getCambio(), cuentaTraba.getCambio().scale(), RoundingMode.HALF_UP);

				if (!cuentaLevantamiento.getCuenta().endsWith(EmbargosConstants.ISO_MONEDA_EUR) && cambioInverso == null) {
					throw new Exception("No se encuentra el cambio de divisa a aplicar para la cuenta de levantamiento "+ cuentaLevantamiento.getCodCuentaLevantamiento() +" "+ cuentaLevantamiento.getCuenta());
				}

				if (fileControlFicheroComunes == null) {
					fileControlFicheroComunes = accountingNoteService.crearControlFichero(username, EmbargosConstants.ID_APLICACION_EMBARGOS, null, sucursal, ACCOUNTING_EMBARGOS_PATTERN, contentType, ACCOUNTING_EMBARGOS_EXTENSION);
				}

				apunteContableLevantamiento(cuentaLevantamiento, cuentaRecaudacion, cuentaLevantamiento.getCuenta(),
						oficinaRecaudacion, embargo.getNumeroEmbargo(), "", embargo.getDatregcomdet(), cuentaIntercambioDivisas,
						fileControlFicheroComunes.getCodControlFichero(), embargo.getNombre(), embargo.getNif(), cambioInverso);

				AccountStatusLiftingDTO status = new AccountStatusLiftingDTO();
				status.setCode(String.valueOf(COD_ESTADO_LEVANTAMIENTO_PENDIENTE_RESPUESTA_CONTABILIZACION));

				liftingService.updateAccountLiftingStatus(cuentaLevantamiento.getCodCuentaLevantamiento(), status, USER_AUTOMATICO);
			}
		}

		liftingService.changeStatus(levantamiento.getCodLevantamiento(), COD_ESTADO_LEVANTAMIENTO_PENDIENTE_RESPUESTA_CONTABILIZACION, USER_AUTOMATICO);

		return fileControlFicheroComunes;
	}

	private void apunteContableLevantamiento(
			CuentaLevantamiento cuentaLevantamiento, String debitAccount, String creditAccount,
			String oficinaCuentaRecaudacion, String reference1, String reference2, String detailPayment, String cuentaIntercambioDivisas,
			Long codFileControlFicheroComunes, String nombre, String nif, BigDecimal cambio)
			throws Exception {
		if (cuentaLevantamiento.getImporte()!=null && BigDecimal.ZERO.compareTo(cuentaLevantamiento.getImporte()) < 0)
		{
			/*
			String reference1 = embargo.getNumeroEmbargo();
			String reference2 = "";
			String detailPayment = embargo.getDatregcomdet();
*/
			AccountingNote accountingNote = new AccountingNote();

			accountingNote.setCodFileControl(codFileControlFicheroComunes);

			accountingNote.setActualDate(new Date());
			accountingNote.setAplication(EmbargosConstants.ID_APLICACION_EMBARGOS);
			accountingNote.setCodOffice(oficinaCuentaRecaudacion);
			accountingNote.setAmount(cuentaLevantamiento.getImporte().doubleValue());
			accountingNote.setCodCurrency(cuentaLevantamiento.getCodDivisa());
			accountingNote.setDebitAccount(debitAccount);
			accountingNote.setCreditAccount(creditAccount);
			accountingNote.setDebitValueDate(new Date());
			accountingNote.setCreditValueDate(new Date());
			accountingNote.setExecutionDate(new Date());
			accountingNote.setReference1(reference1);
			accountingNote.setReference2(reference2);
			accountingNote.setDetailPayment(detailPayment);
			accountingNote.setChange(cambio);
			accountingNote.setEsCredito(true);
			// estos sobran?
			accountingNote.setName(nombre);
			accountingNote.setNif(nif);

			accountingNote.setExtraInfo1(EmbargosConstants.APUNTES_CONTABLES_TIPO_LEVANTAMIENTO);
			accountingNote.setExtraInfo2(String.valueOf(cuentaLevantamiento.getCodCuentaLevantamiento()));

			accountingNoteService.contabilizar(accountingNote, cuentaIntercambioDivisas);
		}
	}

	@Override
	@Transactional(transactionManager="transactionManager")
	public void ficheroFinalContabilizar(FicheroFinal ficheroFinal, String userName)
		throws Exception
	{
		if (BigDecimal.ZERO.compareTo(ficheroFinal.getImporte()) >= 0)
		{	// protección adicional, aunque no debería entrar aquí por el estado de contabilización
			logger.info("Fichero final "+ ficheroFinal.getControlFichero().getCodControlFichero() +" sin importe a contabilizar.");
			return;
		}

		ControlFichero controlFichero = ficheroFinal.getControlFichero();

		EntidadesComunicadora entidadComunicadora = controlFichero.getEntidadesComunicadora();
		if (entidadComunicadora == null) {			
			throw new ICEException("EntidadComunicadora not found for the fileControl with codeFileControl: " + controlFichero.getCodControlFichero());
		}
		
		if (entidadComunicadora.getCuenta() == null || entidadComunicadora.getCuenta().trim().isEmpty()) {
			throw new ICEException("Could not be sent to accounting: account not found for the Entidad Comunicadora with NIF: " + entidadComunicadora.getNifEntidad());
		}
		
		//Obtencion de datos para setear:
		String oficinaRecaudacion = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_OFICINA);
		String cuentaRecaudacion = generalParametersService.loadStringParameter(EmbargosConstants.PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_CUENTA);
		Long sucursal = getCodSucursal(oficinaRecaudacion);

		String reference1 = Long.toString(ficheroFinal.getControlFichero().getCodControlFichero());
		String reference2 = null;
		String detailPayment = EmbargosConstants.LITERAL_DETAILPAYMENT_FASE6_EMB_TRANSFERRED_TO + entidadComunicadora.getDesEntidad();

		es.commerzbank.ice.comun.lib.domain.entity.ControlFichero fileControlFicheroComunes = accountingNoteService.crearControlFichero(userName, EmbargosConstants.ID_APLICACION_EMBARGOS, null, sucursal, ACCOUNTING_EMBARGOS_PATTERN, ACCOUNTING_EMBARGO_F6, ACCOUNTING_EMBARGOS_EXTENSION);
		Long codFileControlFicheroComunes = fileControlFicheroComunes.getCodControlFichero();

		apunteContableFicheroFinal(
				ficheroFinal.getImporte(), EmbargosConstants.ISO_MONEDA_EUR, cuentaRecaudacion, entidadComunicadora.getCuenta(),
				oficinaRecaudacion, reference1, reference2, detailPayment,
				codFileControlFicheroComunes, entidadComunicadora.getDesEntidad(), entidadComunicadora.getNifEntidad(), null, null,
				EmbargosConstants.APUNTES_CONTABLES_TIPO_FINAL, String.valueOf(ficheroFinal.getCodFicheroFinal()));

		accountingNoteService.generacionFicheroContabilidad(fileControlFicheroComunes);

		finalResponseService.updateFinalFileAccountingStatus(ficheroFinal, EmbargosConstants.COD_ESTADO_CONTABILIZACION_ENVIADA_A_CONTABILIDAD, userName);
	}

	@Override
	@Transactional(transactionManager="transactionManager")
	public void embargoCallback(Long codCuentaTraba) throws Exception {
		Optional<CuentaTraba> opt = seizedBankAccountRepository.findById(codCuentaTraba);

		if (!opt.isPresent()) {
			logger.error("No se ha encontrado la cuentaTraba código "+ codCuentaTraba);
			return;
		}

		CuentaTraba cuentaTraba = opt.get();

		if (cuentaTraba.getEstadoTraba().getCodEstado() != COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD) {
			logger.error("La cuentaTraba código "+ codCuentaTraba +" no está pendiente de respuesta contable - ignorando.");
			return;
		}

		// Se cambia el estado de la Cuenta Traba a Contabilizada
		// Se actualizará en cascada el estado de la traba y del fichero si es necesario
		seizureService.updateSeizedBankStatus(cuentaTraba, EmbargosConstants.COD_ESTADO_TRABA_CONTABILIZADA, USER_AUTOMATICO);
	}

	@Override
	@Transactional(transactionManager="transactionManager")
	public void ficheroFinalCallback(Long codControlFicheroFinal)
	{
		Optional<FicheroFinal> ficheroFinal = finalFileRepository.findById(codControlFicheroFinal);
		
		if (!ficheroFinal.isPresent()) {
			Log.error ("FicheroFinal not found with codControlFichero=" + codControlFicheroFinal);
			return;
		}

		finalResponseService.updateFinalFileAccountingStatus(ficheroFinal.get(), EmbargosConstants.COD_ESTADO_CONTABILIZACION_CONTABILIZADA, USER_AUTOMATICO);
	}

	@Override
	@Transactional(transactionManager="transactionManager")
	public void levantamientoCallback(Long codCuentaLevantamiento) throws Exception {
		Optional<CuentaLevantamiento> opt = liftingBankAccountRepository.findById(codCuentaLevantamiento);

		if (!opt.isPresent())
		{
			Log.error("No se encuentra el código de cuenta levantamiento "+ codCuentaLevantamiento);
			return;
		}

		CuentaLevantamiento	cuentaLevantamiento = opt.get();

		if (cuentaLevantamiento.getEstadoLevantamiento().getCodEstado() != COD_ESTADO_LEVANTAMIENTO_PENDIENTE_RESPUESTA_CONTABILIZACION) {
			logger.error("La cuentaLevantamiento código "+ codCuentaLevantamiento +" no está pendiente de respuesta contable - ignorando.");
			return;
		}

		// Actualizar cuenta
		AccountStatusLiftingDTO status = new AccountStatusLiftingDTO();
		status.setCode(String.valueOf(EmbargosConstants.COD_ESTADO_LEVANTAMIENTO_CONTABILIZADO));
		liftingService.updateAccountLiftingStatus(codCuentaLevantamiento, status, USER_AUTOMATICO);
	}

	private Long getCodSucursal (String oficinaRecaudacion) throws Exception {
		Optional<Sucursal> sucursalOpt = officeCRepo.findByNumeroSucursal(new BigDecimal(oficinaRecaudacion));
		if (sucursalOpt.isPresent()) {
			return sucursalOpt.get().getCodSucursal();
		}

		throw new Exception("Oficina con codigo "+ oficinaRecaudacion +" no definida");
	}
}