package es.commerzbank.ice.embargos.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.commerzbank.ice.comun.lib.domain.dto.AccountingNote;
import es.commerzbank.ice.comun.lib.domain.dto.GeneralParameter;
import es.commerzbank.ice.comun.lib.domain.entity.Contador;
import es.commerzbank.ice.comun.lib.domain.entity.ContadorPK;
import es.commerzbank.ice.comun.lib.file.generate.ContaGenExecutor;
import es.commerzbank.ice.comun.lib.repository.ContadorRepo;
import es.commerzbank.ice.comun.lib.service.AccountingNoteService;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.typeutils.ICEDateUtils;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.comun.lib.util.ValueConstants;
import es.commerzbank.ice.embargos.domain.dto.SeizureStatusDTO;
import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.CuentaLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import es.commerzbank.ice.embargos.domain.entity.EntidadesComunicadora;
import es.commerzbank.ice.embargos.domain.entity.EstadoLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.EstadoTraba;
import es.commerzbank.ice.embargos.domain.entity.FicheroFinal;
import es.commerzbank.ice.embargos.domain.entity.LevantamientoTraba;
import es.commerzbank.ice.embargos.domain.entity.Peticion;
import es.commerzbank.ice.embargos.domain.entity.SolicitudesEjecucion;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.FinalFileRepository;
import es.commerzbank.ice.embargos.repository.LiftingBankAccountRepository;
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

	@Autowired
	ContaGenExecutor contaGenExecutor;
	
	@Autowired
	es.commerzbank.ice.comun.lib.service.FileControlService fileControlServiceComunes;
	
	@Autowired
	ContadorRepo contadorRepository;

	@Autowired
	LiftingBankAccountRepository liftingBankAccountRepository;
	
	@Autowired
	FinalFileRepository finalFileRepository;
	

	@Override
	public boolean sendAccounting(Long codeFileControl, String userName) throws ICEException, Exception {
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

			//Para contabilizar, el estado de ControlFichero tiene que ser previo o igual a "Recibido":
			if (codEstadoCtrlFichero != EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_RECEIVED) {

				logger.debug(generateMessageCtrlFicheroCannotSendAccounting(controlFichero));
				
				return false;
			}

			isAccountingSent = sendAccountingCGPJ(controlFichero, userName);
			
			if (isAccountingSent) {	
				//Se cambia el estado de Control Fichero a "Pendiente de respuesta contable"
				fileControlService.updateFileControlStatusTransaction(controlFichero, 
						EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_PENDING_ACCOUNTING_RESPONSE, userName);
			}
		
		} else if (isAEAT){

			//Para contabilizar, el estado de ControlFichero tiene que ser previo o igual a "Recibido":
			if (codEstadoCtrlFichero != EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_RECEIVED) {

				logger.debug(generateMessageCtrlFicheroCannotSendAccounting(controlFichero));

				return false;
			}
			
			isAccountingSent = sendAccountingAEATCuaderno63(controlFichero, userName);

			if (isAccountingSent) {		
				//Se cambia el estado de Control Fichero a "Pendiente de respuesta contable"
				fileControlService.updateFileControlStatusTransaction(controlFichero, 
						EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PENDING_ACCOUNTING_RESPONSE, userName);
			}
			
		} else if (isCuaderno63) {
			
			//Para contabilizar, el estado de ControlFichero tiene que ser previo o igual a "Recibido":
			if (codEstadoCtrlFichero != EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_RECEIVED) {
			
				logger.debug(generateMessageCtrlFicheroCannotSendAccounting(controlFichero));
				
				return false;
			}
						
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
		return isAccountingSent;
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

	private boolean sendAccountingAEATCuaderno63(ControlFichero controlFichero, String userName) throws Exception {
		
		logger.info("AccountingServiceImpl - sendAccountingAEATCuaderno63 - start");

		String cuentaRecaudacion = determineCuentaRecaudacion();
		
		Long oficinaCuentaRecaudacion = determineOficinaCuentaRecaudacion();

		boolean existsTrabaNotAccounted = false;
		
		Long codFileControl = crearControlFicheroComunes(controlFichero, userName);
		
		//Se obtienen la trabas asociadas al fichero:
		for (Embargo embargo : controlFichero.getEmbargos()) {
			
			Traba traba = embargo.getTrabas().get(0);
			
			//Para contabilizar la traba tiene que estar en estado anterior a "Enviada a Contabilidad":
			if (traba.getEstadoTraba().getCodEstado() == EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE) {
			
				String reference1 = embargo.getNumeroEmbargo();
				String reference2 = "";
				String detailPayment = embargo.getDatregcomdet();
				
				boolean existsCuentaTrabaNotAccounted = false;
				
				//Se utiliza CopyOnWriteArrayList para evitar ConcurrentModificationException en el 
				//update del estado de la cuentaTraba en la iteracion del listado de cuentaTrabas:
				List<CuentaTraba> cuentaTrabasList = new CopyOnWriteArrayList<>();	
				cuentaTrabasList.addAll(traba.getCuentaTrabas());
				
				for(CuentaTraba cuentaTraba : cuentaTrabasList) {
					
					//Para contabilizar la cuentaTraba tiene que estar en estado anterior a "Enviada a Contabilidad":
					if (cuentaTraba.getEstadoTraba().getCodEstado() == EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE) {
					
						int resultContabilizar = contabilizarCuentaTraba(cuentaTraba, cuentaTraba.getCuenta(), cuentaRecaudacion,
								oficinaCuentaRecaudacion, reference1, reference2, detailPayment, codFileControl, embargo.getDatosCliente().getNombre(), 
								embargo.getDatosCliente().getNif(), userName, EmbargosConstants.COD_ESTADO_TRABA_CONTABILIZADA);
						
						//Dependiendo del resultado de contabilizar:
						if(resultContabilizar == 1) {
							//Se ha contabilizado la cuentaTraba: Se actualiza el estado de la Cuenta Traba a "Enviada a contabilidad":
							seizureService.updateSeizedBankStatusTransaction(cuentaTraba, EmbargosConstants.COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD, userName);
							
						} else if (resultContabilizar == 0) {
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
							
				if (!existsCuentaTrabaNotAccounted) {
					
					//Si no existe ninguna cuentaTraba que no se haya contabilizado:
					
					//Cambio de estado de la traba a "Enviada a contabilidad":		
					String codigoEstadoTraba = Long.toString(EmbargosConstants.COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD);
				
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
		
		contaGenExecutor.generacionFicheroContabilidad(codFileControl);
		
		//Se devuelve true si no existe traba que no haya sido enviada a contabilidad:
		return !existsTrabaNotAccounted;
	}


	private Long crearControlFicheroComunes(ControlFichero controlFichero, String userName) throws Exception {
		Long codControlFichero = null;
		
		es.commerzbank.ice.comun.lib.domain.entity.ControlFichero entity = new es.commerzbank.ice.comun.lib.domain.entity.ControlFichero(), result = null;
		Date date = new Date();
		Contador contador = contadorRepository.existsContador(new Timestamp(date.getTime()), EmbargosConstants.ID_APLICACION_EMBARGOS, ValueConstants.APUNTE_CONTABLE_CONTADOR_TIPO);
		
		if (contador != null && contador.getContador() != null && contador.getContador().intValue() > 0) {
			int aumentar = contador.getContador().intValue() + 1;
			contador.setContador(new BigDecimal(aumentar));
		} else {
			contador = new Contador();
			contador.setContador(new BigDecimal(1));
			
			ContadorPK contadorPK = new ContadorPK();
			contadorPK.setAplicacion(EmbargosConstants.ID_APLICACION_EMBARGOS);
			contadorPK.setTipo(ValueConstants.APUNTE_CONTABLE_CONTADOR_TIPO);
			contadorPK.setFecha(new Timestamp(date.getTime()));
			contador.setId(contadorPK);
			
		}
		
		int cuenta = contador.getContador().intValue() + 1;
		entity.setContador(new BigDecimal(cuenta));
		entity.setDescripcion(controlFichero.getDescripcion());
		
		Date fechaCreacion = ICEDateUtils.bigDecimalToDate(controlFichero.getFechaCreacion(), ICEDateUtils.FORMAT_yyyyMMdd);
		entity.setFechaCreacion(new Timestamp(fechaCreacion.getTime()));
		entity.setfUltimaModificacion(new Timestamp(date.getTime()));
		entity.setUsuUltModificacion(userName);
		
		result = fileControlServiceComunes.createFileControl(entity);
		
		if (result != null) {
			codControlFichero = result.getCodControlFichero();
		} 
		
		return codControlFichero;
	}


	private boolean sendAccountingCGPJ(ControlFichero controlFichero, String userName) throws ICEException, Exception {

		logger.info("AccountingServiceImpl - sendAccountingCGPJ - start");

		String cuentaRecaudacion = determineCuentaRecaudacion();
		Long oficinaCuentaRecaudacion = determineOficinaCuentaRecaudacion();

		boolean existsTrabaNotAccounted = false;
		
		Long codFileControl = crearControlFicheroComunes(controlFichero, userName);
		
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
						Map<String,String> referencesMap = generateReferencesForCGPJ(embargo.getNumeroEmbargo());
						
						String reference1 = referencesMap.get(EmbargosConstants.IBS_REFERENCE_1);
						String reference2 = referencesMap.get(EmbargosConstants.IBS_REFERENCE_2);

						String detailPayment = "";//embargo.getDatregcomdet();

						boolean existsCuentaTrabaNotAccounted = false;

						for(CuentaTraba cuentaTraba : traba.getCuentaTrabas()) {

							//Para contabilizar la cuentaTraba tiene que estar en estado anterior a "Enviada a Contabilidad":
							if (cuentaTraba.getEstadoTraba().getCodEstado() == EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE) {
								int resultContabilizar = contabilizarCuentaTraba(cuentaTraba, cuentaTraba.getCuenta(), cuentaRecaudacion,
										oficinaCuentaRecaudacion, reference1, reference2, detailPayment, codFileControl, embargo.getDatosCliente().getNombre(), 
										embargo.getDatosCliente().getNif(), userName, EmbargosConstants.COD_ESTADO_TRABA_CONTABILIZADA);

								//Dependiendo del resultado de contabilizar:
								if(resultContabilizar == 1) {
									//Se ha contabilizado la cuentaTraba: Se actualiza el estado de la Cuenta Traba a "Enviada a contabilidad":
									seizureService.updateSeizedBankStatusTransaction(cuentaTraba, EmbargosConstants.COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD, userName);

								} else if (resultContabilizar == 0) {
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

						if (!existsCuentaTrabaNotAccounted) {

							//Si no existe ninguna cuentaTraba que no se haya contabilizado:

							//Cambio de estado de la traba a "Enviada a contabilidad":
							String codigoEstadoTraba = Long.toString(EmbargosConstants.COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD);

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

			}
		}

		logger.info("AccountingServiceImpl - sendAccountingCGPJ - end");
		
		contaGenExecutor.generacionFicheroContabilidad(codFileControl);

		//Se devuelve true si no existe traba que no haya sido enviada a contabilidad:
		return !existsTrabaNotAccounted;

	}

	private int contabilizarCuentaTraba(CuentaTraba cuentaTraba, String debitAccount, String creditAccount,
			Long oficinaCuentaRecaudacion, String reference1, String reference2, String detailPayment,
			Long codFileControl, String nombre, String nif, String userName, Long estadoImporteCero) {
		
		logger.info("AccountingServiceImpl - contabilizarCuentaTraba - start");
		
		int result = 0;
		
		AccountingNote accountingNote = new AccountingNote();

		double amount = cuentaTraba.getImporte()!=null ? cuentaTraba.getImporte().doubleValue() : 0;
		
		if (amount != 0) {
			accountingNote.setAplication(EmbargosConstants.ID_APLICACION_EMBARGOS);
			accountingNote.setCodOffice(oficinaCuentaRecaudacion);
			//El contador lo gestiona Comunes
			//accountingNote.setContador(contador);
			accountingNote.setAmount(amount);
			accountingNote.setCodCurrency(cuentaTraba.getDivisa());
			accountingNote.setDebitAccount(debitAccount);
			accountingNote.setCreditAccount(creditAccount);
			accountingNote.setActualDate(new Date());
			//accountingNote.setExecutionDate(new Date());
			accountingNote.setReference1(reference1);
			accountingNote.setReference2(reference2);
			accountingNote.setChange(cuentaTraba.getCambio());
			accountingNote.setCallback(EmbargosConstants.PARAMETRO_EMBARGOS_CONTABILIZACION_FASE3_CALLBACK);
			accountingNote.setCodFileControl(codFileControl);
			accountingNote.setStatus(EmbargosConstants.COD_ESTADO_APUNTE_CONTABLE_PENDIENTE_ENVIO);
			accountingNote.setName(nombre);
			accountingNote.setNif(nif);
			accountingNote.setDetailPayment(detailPayment);
			
			result = accountingNoteService.contabilizar(accountingNote);
		} else {
			boolean changed = seizureService.updateSeizedBankStatus(cuentaTraba, estadoImporteCero, userName);
			
			if (changed) {
				result = 2;
			}
		}
		
		logger.info("AccountingServiceImpl - contabilizarCuentaTraba - end");
		
		return result;
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
	
	private Map<String,String> generateReferencesForCGPJ(String numeroEmbargo){
		
		//- Seteo de las references:
		//En el caso del CGPJ el número de embargo tiene 19 caracteres entonces: 1:16 va al primer campo (IBS_REFERENCE_1) y
		//del 17-19 pasa al inicio del siguiente campo (IBS_REFERENCE_2) seguido de los literales: Levant./Embarg y las cuatro
		//letras que identifican el organismo emisor AEAT/CGPJ etc
		
		Map<String,String> referencesMap = new HashMap<>();
		
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
		
		referencesMap.put(EmbargosConstants.IBS_REFERENCE_1, reference1);
		referencesMap.put(EmbargosConstants.IBS_REFERENCE_2, reference2);
		
		return referencesMap;
	}

	@Override
	public boolean manageAccountingNoteSeizureCallback(AccountingNote accountingNote, String userName) {
		
		//Se tomaran los campos IBS_CREDIT_ACCOUNT,TRIM(IBS_REFERENCE_1+IBS_REFERENCE_2),IBS_AMOUNT para 
		//determinar que elemento se ha contabilizado y marcar su estado a contabilizado.

		logger.info("AccountingServiceImpl - manageAccountingNoteCallback - start");

		//1. Se obtiene la Cuenta Traba:

		String cuenta = accountingNote.getDebitAccount();
		BigDecimal importe = BigDecimal.valueOf(accountingNote.getAmount());
		EstadoTraba estadoTraba = new EstadoTraba();
		estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD);

		CuentaTraba cuentaTraba = seizedBankAccountRepository.findByCuentaAndImporteAndEstadoTraba(cuenta, importe, estadoTraba);

		if(cuentaTraba==null) {
			logger.error("No se ha encontrado la cuentaTraba en estado enviada a contabilidad [cuenta:" + cuenta +";importe:" + importe + "]");
			return false;
		}
		
		//Comprobar que:
		//- Si el tipo de fichero es AEAT o NORMA63: el Numero de Embargo informado en la Reference1 de la accountingNote, 
		//  sea el mismo que el Numero de Embargo del Embargo asociado a la cuentaTraba.
		//- Si el tipo de fichero es CGPJ: comprobar que la reference1 y reference2 de la accountingNote, sean las mismas
		//  que la generada a partir del Numbero de Embargo.
		Traba traba = cuentaTraba.getTraba();
		Embargo embargo = traba.getEmbargo();
		String numeroEmbargo = embargo.getNumeroEmbargo();
		
		ControlFichero controlFichero = embargo.getControlFichero();
		
		String formatoFichero = EmbargosUtils.determineFileFormatByTipoFichero(controlFichero.getCodControlFichero());
		
		if (EmbargosConstants.FILE_FORMAT_AEAT.equals(formatoFichero) 
				|| EmbargosConstants.FILE_FORMAT_NORMA63.equals(formatoFichero)) {
		
			//Si el formato de fichero es AEAT o NORMA63 -> comprobar reference1:
			
			if (!numeroEmbargo.equals(accountingNote.getReference1())) {
	
				logger.error("El numero de embargo '" + numeroEmbargo + "' asociado a la cuentaTraba con id " + cuentaTraba.getCodCuentaTraba()
				+ " no coincide con el numero de embargo informado en la reference1 de la accountingNote -> '" + accountingNote.getReference1() + "'");
	
				return false;
			}
			
		} else {
			
			//Si es de CGPJ -> comprobar reference1 y reference2:
			Map<String,String> referencesMap = generateReferencesForCGPJ(numeroEmbargo);
			
			String reference1 = referencesMap.get(EmbargosConstants.IBS_REFERENCE_1);
			String reference2 = referencesMap.get(EmbargosConstants.IBS_REFERENCE_2);
			
			if (!reference1.equals(accountingNote.getReference1())
					|| !reference2.equals(accountingNote.getReference2())) {

				logger.error("El numero de embargo '" + numeroEmbargo + "' (CGPJ) asociado a la cuentaTraba con id " + cuentaTraba.getCodCuentaTraba()
				+ " no coincide con el numero de embargo informado para la reference1 y reference2 de la accountingNote -> [reference1:" + accountingNote.getReference1() 
				+ "; reference2:" +  accountingNote.getReference2() + "]");
	
				return false;	
			}
		}
		
		

		//2. Se cambia el estado de la Cuenta Traba a Contabilizada:

		seizureService.updateSeizedBankStatus(cuentaTraba, EmbargosConstants.COD_ESTADO_TRABA_CONTABILIZADA, userName);

		//3. Si todas las CuentaTraba asociadas a la Traba han cambiado a estado "Contabilizada", entonces: 
		// - Cambiar el estado de la Traba a "Contabilizada":
		
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

			Log.debug("ControlFichero con id " + controlFichero.getCodControlFichero() + " cambia a estado 'Pendiente de envio");

		}

		logger.info("AccountingServiceImpl - manageAccountingNoteCallback - end");

		return true;
	}

	@Override
	public boolean manageAccountingNoteLiftingCallback(AccountingNote accountingNote, String userName)
	{
		// Localizar cuenta levantameiento
		String cuenta = accountingNote.getDebitAccount();
		BigDecimal importe = BigDecimal.valueOf(accountingNote.getAmount());
		EstadoLevantamiento estadoLevantamiento = new EstadoLevantamiento();
		estadoLevantamiento.setCodEstado(EmbargosConstants.COD_ESTADO_LEVANTAMIENTO_PENDIENTE_RESPUESTA_CONTABILIZACION);

		CuentaLevantamiento cuentaLevantamiento = liftingBankAccountRepository.findByCuentaAndImporteAndEstadoLevantamiento(cuenta, importe, estadoLevantamiento);

		if(cuentaLevantamiento == null) {
			logger.error("No se ha encontrado la Cuenta Levantamiento en estado enviada a contabilidad [cuenta:" + cuenta +";importe:" + importe + "]");
			return false;
		}

		// Comprobar que el Numero de Embargo informado en la Reference1 de la accountingNote, sea el mismo que el del Embargo asociado a la cuentaTraba:
		LevantamientoTraba levantamientoTraba = cuentaLevantamiento.getLevantamientoTraba();
		Traba traba = levantamientoTraba.getTraba();
		String numeroEmbargo = traba.getEmbargo().getNumeroEmbargo();

		if (!numeroEmbargo.equals(accountingNote.getReference1())) {

			logger.error("El numero de embargo '" + numeroEmbargo + "' asociado a la cuenta levantamiento con id " + cuentaLevantamiento.getCodCuentaLevantamiento()
					+ " no coincide con el numero de embargo informado en la reference1 de la accountingNote -> '" + accountingNote.getReference1() + "'");

			return false;
		}

		//2. Actualizar el estado de la cuenta levantamiento

		liftingService.updateLiftingBankAccountingStatus(cuentaLevantamiento, EmbargosConstants.COD_ESTADO_LEVANTAMIENTO_CONTABILIZADO, userName);

		//3. Si todas las cuentsa levantamiento del levantamiento están contabilizadas, avanzar el estado del levantamiento

		boolean isAllCuentaLevantamientoContabilizados = true;
		for(CuentaLevantamiento currentCuentaLevantamiento : levantamientoTraba.getCuentaLevantamientos()) {
			if (currentCuentaLevantamiento.getEstadoLevantamiento().getCodEstado() != EmbargosConstants.COD_ESTADO_LEVANTAMIENTO_CONTABILIZADO) {
				isAllCuentaLevantamientoContabilizados = false;
				break;
			}
		}

		if (isAllCuentaLevantamientoContabilizados) {
			liftingService.updateLiftingtatus(levantamientoTraba, EmbargosConstants.COD_ESTADO_LEVANTAMIENTO_CONTABILIZADO, userName);
		}

		//4. Revisar el estado de todos los levantamientos de este control fichero. Cambiar el estado si procede

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
			Long estado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_ACCOUNTED;

			fileControlService.updateFileControlStatusTransaction(controlFichero, estado, userName);

			Log.debug("ControlFichero con id " + controlFichero.getCodControlFichero() + " cambia a estado 'Contabilizado'");

		}

		return true;
	}

	@Override
	public boolean undoAccounting(Long codeFileControl, Long idSeizure, String numAccount, String userName) throws ICEException{

		logger.info("AccountingServiceImpl - undoAccounting - start");

		//Solo se puede retroceder cuando este contabilizado (se haya realizado el callback) y una vez realizado
		//el retroceso, se cambiara el estado a anterior a contabilizado.
		
		//Se obtiene la cuenta Traba en estado Contabilizada:
		EstadoTraba estadoTraba = new EstadoTraba();
		estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_CONTABILIZADA);
		
		CuentaTraba cuentaTraba = seizedBankAccountRepository.findByCodCuentaTrabaAndCuentaAndEstadoTraba(codeFileControl, idSeizure, estadoTraba);
		
		if(cuentaTraba == null) {
			throw new ICEException("","ERROR: no se ha encontrado la cuentaTraba [codeFileControl: " + codeFileControl + "; idSeizure: " + idSeizure + "; estadoTraba: "+ estadoTraba);
		}
		
		//Preparacion de parametros para enviar a contabilizar:

		String cuentaRecaudacion = determineCuentaRecaudacion();
		Long oficinaCuentaRecaudacion = determineOficinaCuentaRecaudacion();

		Traba traba = cuentaTraba.getTraba();
		Embargo embargo = traba.getEmbargo();

		String reference1 = embargo.getNumeroEmbargo();
		String reference2 = "";
		String detailPayment = embargo.getDatregcomdet();

		//Llamada a contabilizar para deshacer la contabilizacion, poniendo como debitAccount la cuenta
		//de recaudacion y la creditAccount la cuenta del cliente:
		int resultContabilizar = contabilizarCuentaTraba(cuentaTraba, cuentaRecaudacion, cuentaTraba.getCuenta(),
				oficinaCuentaRecaudacion, reference1, reference2, detailPayment, codeFileControl, embargo.getDatosCliente().getNombre(), 
				embargo.getDatosCliente().getNif(), userName, EmbargosConstants.COD_ESTADO_TRABA_MODIFICADA);

		//Dependiendo del resultado de contabilizar:
		if(resultContabilizar == 1) {

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
				throw new ICEException("", "ERROR: no se ha actualizado el estado de la Traba con codTraba: " + traba.getCodTraba());
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

				throw new ICEException("","ERROR: formato de fichero no encontrado para el codigo de tipo de fichero "
								+ controlFichero.getTipoFichero().getCodTipoFichero() +".");
			}

			boolean isStatusFileControlUpdated = fileControlService.updateFileControlStatus(codeFileControl, codEstado, userName);

			if(!isStatusFileControlUpdated) {
				throw new ICEException("", "ERROR: no se ha actualizado el estado del Control Fichero con codeFileControl: " + codeFileControl);
			}
		
		} else {

			Log.error("Fallo al deshacer la contabilizacion de la cuentaTraba con id " +  cuentaTraba.getCodCuentaTraba());

			logger.info("AccountingServiceImpl - undoAccounting - end");
			return false;
		}


		logger.info("AccountingServiceImpl - undoAccounting - end");
		return true;
	}
	
		
	@Override
	public boolean sendAccountingLifting(Long codeFileControl, String userName)  throws Exception {

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
				for (CuentaLevantamiento cuenta : levantamiento.getCuentaLevantamientos()) {
					sendAccountingLiftingBankAccountInternal(cuenta, levantamiento.getTraba().getEmbargo(), fileControlOpt.get(), userName, oficinaCuentaRecaudacion, cuentaRecaudacion, contabilizacionCallbackNameParameter);
				}
			}
		}
		
		logger.info("AccountingServiceImpl - sendAccountingLifting - end");
		return response;
	}

	@Override
	public boolean sendAccountingLiftingBankAccount(CuentaLevantamiento cuentaLevantamiento, Embargo embargo, String userName)
			throws Exception
	{
		boolean response = true;

		String cuentaRecaudacion = determineCuentaRecaudacion();
		Long oficinaCuentaRecaudacion = determineOficinaCuentaRecaudacion();
		String contabilizacionCallbackNameParameter = EmbargosConstants.PARAMETRO_EMBARGOS_CONTABILIZACION_FASE5_CALLBACK;

		sendAccountingLiftingBankAccountInternal(cuentaLevantamiento, embargo, embargo.getControlFichero(), userName, oficinaCuentaRecaudacion, cuentaRecaudacion, contabilizacionCallbackNameParameter);

		return response;
	}

	private boolean sendAccountingLiftingBankAccountInternal(CuentaLevantamiento cuentaLevantamiento, Embargo embargo, ControlFichero controlFichero, String userName, Long oficinaCuentaRecaudacion, String cuentaRecaudacion, String contabilizacionCallbackNameParameter)
			throws Exception {
		
		logger.info("AccountingServiceImpl - sendAccountingLiftingBankAccountInternal - start");
		
		boolean response = true;

		String reference1 = embargo.getNumeroEmbargo();
		String reference2 = "";
		String detailPayment = embargo.getDatregcomdet();

		AccountingNote accountingNote = new AccountingNote();
		
		Long codFileControl = crearControlFicheroComunes(controlFichero, userName);
		
		double amount = cuentaLevantamiento.getImporte()!=null ? cuentaLevantamiento.getImporte().doubleValue() : 0;

		int resultado = 0;
		if (amount != 0) {
			accountingNote.setAplication(EmbargosConstants.ID_APLICACION_EMBARGOS);
			accountingNote.setCodOffice(oficinaCuentaRecaudacion);
			//El contador lo gestiona Comunes
			//accountingNote.setContador(contador);
			accountingNote.setAmount(amount);
			accountingNote.setCodCurrency(cuentaLevantamiento.getCodDivisa());
			accountingNote.setDebitAccount(cuentaRecaudacion);
			accountingNote.setCreditAccount(cuentaLevantamiento.getCuenta());
			accountingNote.setActualDate(new Date());
			//accountingNote.setExecutionDate(new Date());
			accountingNote.setReference1(reference1);
			accountingNote.setReference2(reference2);
			accountingNote.setChange(cuentaLevantamiento.getCambio());
			accountingNote.setCallback(contabilizacionCallbackNameParameter);
			accountingNote.setStatus(EmbargosConstants.COD_ESTADO_APUNTE_CONTABLE_PENDIENTE_ENVIO);
			accountingNote.setCodFileControl(codFileControl);
			accountingNote.setName(embargo.getDatosCliente().getNombre());
			accountingNote.setNif(embargo.getDatosCliente().getNif());	
			accountingNote.setDetailPayment(detailPayment);
	
			resultado = accountingNoteService.contabilizar(accountingNote);
		} else {
			liftingService.updateLiftingBankAccountingStatus(cuentaLevantamiento, EmbargosConstants.COD_ESTADO_LEVANTAMIENTO_CONTABILIZADO, userName);
		}

		if (resultado == 0) {
			response = false;
		}

		//Se actualiza el estado de la Cuenta Levantamiento a "Enviada a contabilidad":
		liftingService.updateLiftingBankAccountingStatus(cuentaLevantamiento, EmbargosConstants.COD_ESTADO_LEVANTAMIENTO_PENDIENTE_RESPUESTA_CONTABILIZACION, userName);

		logger.info("AccountingServiceImpl - sendAccountingLiftingBankAccountInternal - end");
		
		return response;
	}
	
	//@Override
	public boolean sendAccountingForCommunicatingEntityWithInternalAccountInFase6(Long codeFileControl, String userName) throws ICEException, Exception {
		
		logger.info("AccountingServiceImpl - sendAccountingForOrderingEntityWithInternalAccountInFase6 - start");
		
		//Para las Entidades Comunicadoras que tengan cuenta en Commerzbank, la transferencia
		//de la Fase 6 (cierre) se hara internamente llamando a contabilizar():
		
		
		//- Se obtiene el fichero de control:
		Optional<ControlFichero> fileControlOpt = fileControlRepository.findById(codeFileControl);
		if(!fileControlOpt.isPresent()) {
			throw new ICEException("","ControlFichero not found with codeFileControl: " + codeFileControl);
		}
		ControlFichero controlFichero = fileControlOpt.get();
		
		//- Se comprueba si la Entidad comunicadora asociada al fichero, tiene cuenta interna en Commerzbank:		
		EntidadesComunicadora entidadComunicadora = controlFichero.getEntidadesComunicadora();
		if (entidadComunicadora == null) {			
			throw new ICEException("","EntidadComunicadora not found for the fileControl with codeFileControl: " + codeFileControl);
		}
		
		if (entidadComunicadora.getCuenta() == null || entidadComunicadora.getCuenta().isEmpty()) {
			logger.info("Could not be sent to accounting: account not found for the Entidad Comunicadora with NIF: " + entidadComunicadora.getNifEntidad());
			return false;
		}
		
		//Se obtiene el registro de FicheroFinal asociado al controlFichero:		
		List<FicheroFinal> ficheroFinalList = controlFichero.getFicheroFinals();
		
		if (ficheroFinalList==null || ficheroFinalList.isEmpty()) {
			logger.info("Could not be sent to accounting: Fichero Final not found for codeFileControl: " + controlFichero.getCodControlFichero());	
			return false;
		}
		
		FicheroFinal ficheroFinal = ficheroFinalList.get(0);

		
		crearControlFicheroComunes(controlFichero, userName);
		
		//Obtencion de datos para setear:
		String cuentaRecaudacion = determineCuentaRecaudacion();
		String cuentaEntidadComunicadora = entidadComunicadora.getCuenta();
		Long oficinaCuentaRecaudacion = determineOficinaCuentaRecaudacion();
		String divisa = EmbargosConstants.ISO_MONEDA_EUR;
		BigDecimal cambio = null;
		String reference1 = "";
		String reference2 = "";
		String contabilizacionCallbackNameParameter = "";//En Fase5 es: EmbargosConstants.PARAMETRO_EMBARGOS_CONTABILIZACION_FASE5_CALLBACK;

		double amount = ficheroFinal.getImporte()!=null ? ficheroFinal.getImporte().doubleValue() : 0;
		
		AccountingNote accountingNote = new AccountingNote();
		
		accountingNote.setAplication(EmbargosConstants.ID_APLICACION_EMBARGOS);
		accountingNote.setCodOffice(oficinaCuentaRecaudacion);
		accountingNote.setAmount(amount);
		accountingNote.setCodCurrency(divisa);
		accountingNote.setDebitAccount(cuentaRecaudacion);
		accountingNote.setCreditAccount(cuentaEntidadComunicadora);
		accountingNote.setActualDate(new Date());
		//accountingNote.setExecutionDate(new Date());
		accountingNote.setReference1(reference1);
		accountingNote.setReference2(reference2);
		accountingNote.setChange(cambio);
		accountingNote.setCallback(contabilizacionCallbackNameParameter);
		accountingNote.setStatus(EmbargosConstants.COD_ESTADO_APUNTE_CONTABLE_PENDIENTE_ENVIO);
		accountingNote.setCodFileControl(codeFileControl);
//		accountingNote.setName(embargo.getDatosCliente().getNombre());
//		accountingNote.setNif(embargo.getDatosCliente().getNif());	
//		accountingNote.setDetailPayment(detailPayment);

//		resultado = accountingNoteService.contabilizar(accountingNote);
		
		logger.info("AccountingServiceImpl - sendAccountingForOrderingEntityWithInternalAccountInFase6 - end");
		
		return true;
	}

}