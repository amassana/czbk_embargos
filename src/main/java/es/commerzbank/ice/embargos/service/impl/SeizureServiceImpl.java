package es.commerzbank.ice.embargos.service.impl;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import es.commerzbank.ice.comun.lib.domain.dto.Location;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.service.LocationService;
import es.commerzbank.ice.comun.lib.service.TaskService;
import es.commerzbank.ice.comun.lib.util.SQLUtils;
import es.commerzbank.ice.comun.lib.util.jasper.ReportHelper;
import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.datawarehouse.service.AccountService;
import es.commerzbank.ice.datawarehouse.util.DWHUtils;
import es.commerzbank.ice.embargos.config.OracleDataSourceEmbargosConfig;
import es.commerzbank.ice.embargos.domain.dto.*;
import es.commerzbank.ice.embargos.domain.entity.*;
import es.commerzbank.ice.embargos.domain.mapper.*;
import es.commerzbank.ice.embargos.repository.*;
import es.commerzbank.ice.embargos.service.*;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.EmbargosUtils;
import es.commerzbank.ice.embargos.utils.ICEDateUtils;
import es.commerzbank.ice.embargos.utils.ResourcesUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
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
import java.sql.SQLException;
import java.util.*;

import static es.commerzbank.ice.comun.lib.util.ValueConstants.MADRID_PREFIJO_OFICINA;
import static es.commerzbank.ice.embargos.utils.EmbargosConstants.*;

@Service
@Transactional(transactionManager = "transactionManager")
public class SeizureServiceImpl
		implements SeizureService
{
	
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
	GeneralParametersService generalParametersService;

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

	@Autowired
	AccountService accountService;

	@Autowired
	private ReportHelper reportHelper;
	
	@Autowired
	private TaskService taskService;

	@Autowired
	private FileControlService fileControlService;
		
	@Autowired
	private FinalResponseGenerationService finalResponseGenerationService;

	@Autowired
	private AccountingService accountingService;

	@Autowired
	private LocationService locationService;

	@Autowired
	private PetitionService petitionService;
	
	@Override
	public List<SeizureDTO> getSeizureListByCodeFileControl(Long codeFileControl) {
		
		List<SeizureDTO> seizureDTOList = new ArrayList<>();

		ControlFichero controlFichero = new ControlFichero();
		controlFichero.setCodControlFichero(codeFileControl);

		List<Embargo> embargoList = seizureRepository.findAllByControlFichero(controlFichero);

		for (Embargo embargo : embargoList) {

			SeizureDTO seizureDTO = seizureMapper.toSeizureDTO(embargo);
			seizureDTOList.add(seizureDTO);
		}

		return seizureDTOList;
	}

	@Override
	public SeizureDTO getSeizureById(Long idSeizure) {
		SeizureDTO seizureDTO = null;

		// Optional<Embargo> embargoOpt = seizureRepository.findById(idSeizure);

		// if(embargoOpt.isPresent()) {

		// seizureDTO = seizureMapper.toSeizureDTO(embargoOpt.get());
		// }

		Optional<Traba> trabaOpt = seizedRepository.findById(idSeizure);

		if (trabaOpt.isPresent()) {

			seizureDTO = seizureMapper.toSeizureDTO(trabaOpt.get().getEmbargo());
		}

		return seizureDTO;
	}

	@Override
	public List<SeizedBankAccountDTO> getBankAccountListBySeizure(Long codeFileControl,
			Long idSeizure) {
		
		List<SeizedBankAccountDTO> seizedBankAccountDTOList = new ArrayList<>();

		Traba traba = new Traba();
		traba.setCodTraba(idSeizure);
		
		List<CuentaTraba> cuentaTrabaList = seizedBankAccountRepository.findAllByTrabaOrderByNumeroOrdenCuentaAsc(traba);
		
		for(CuentaTraba cuentaTraba : cuentaTrabaList) {
			
			SeizedBankAccountDTO seizedBankAccountDTO = seizedBankAccountMapper.toSeizedBankAccountDTO(cuentaTraba);
			seizedBankAccountDTOList.add(seizedBankAccountDTO);
		}

		return seizedBankAccountDTOList;
	}

	@Override
	public List<SeizureActionDTO> getSeizureActions(Long codeFileControl) {
		List<SeizureActionDTO> seizureActionDTOList = new ArrayList<>();
		
		Optional<ControlFichero> controlFicheroOpt = fileControlRepository.findById(codeFileControl);
		
		if (!controlFicheroOpt.isPresent()) {
			return seizureActionDTOList;
		}
		
		ControlFichero controlFichero = controlFicheroOpt.get();
		
		String tipoEntidad = EmbargosUtils.determineFileFormatByTipoFichero(controlFichero.getTipoFichero().getCodTipoFichero());
		
		List<CuentaTrabaActuacion> cuentaTrabaActuacionList = seizedBankAccountActionRepository.findAllByTipoEntidadOrderByCodActuacion(tipoEntidad);
		
		for(CuentaTrabaActuacion cuentaTrabaActuacion : cuentaTrabaActuacionList) {
			
			SeizureActionDTO seizedBankAccountDTO = seizedBankAccountActionMapper.toSeizureActionDTO(cuentaTrabaActuacion);
			seizureActionDTOList.add(seizedBankAccountDTO);
		}

		return seizureActionDTOList;
	}

	@Override
	public List<SeizureStatusDTO> getSeizureStatusList() {
		List<SeizureStatusDTO> seizureStatusDTOList = new ArrayList<>();

		List<EstadoTraba> estadoTrabaList = seizureStatusRepository.findAllVisibleToUser();

		for (EstadoTraba estadoTraba : estadoTrabaList) {

			SeizureStatusDTO seizureStatusDTO = seizureStatusMapper.toSeizureStatusDTO(estadoTraba);
			seizureStatusDTOList.add(seizureStatusDTO);
		}

		
		return seizureStatusDTOList;

	}

	@Override
	public boolean updateSeizedBankAccountList(Long codeFileControl, Long idSeizure,
			SeizureSaveDTO seizureSave, String userModif) throws Exception {
		logger.info("SeizureServiceImpl - updateSeizedBankAccountList - start "+ codeFileControl +" "+ idSeizure);

		Optional<Traba> trabaOpt = seizedRepository.findById(idSeizure);

		if (trabaOpt.isPresent()) {

			// Actualizacion usuario y fecha de ultima modificacion de la traba:
			Traba traba = trabaOpt.get();
			BigDecimal fechaActualBigDec = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
			traba.setUsuarioUltModificacion(userModif);
			traba.setFUltimaModificacion(fechaActualBigDec);
			traba.setRevisado(seizureSave.isReviewed() ? EmbargosConstants.IND_FLAG_SI : EmbargosConstants.IND_FLAG_NO);

			// Actualizar usuario y fecha ultima modificacion del Embargo (para que se
			// inserte registro en el historico de embargos):
			Embargo embargo = traba.getEmbargo();
			embargo.setUsuarioUltModificacion(userModif);
			embargo.setFUltimaModificacion(fechaActualBigDec);

			// Calculo del importe trabado: sumar los importes trabados de cada cuenta:
			BigDecimal importeTrabado = new BigDecimal(0);
			for (SeizedBankAccountDTO seizedBankAccountDTO : seizureSave.getSeizedBankAccountList()) {
				BigDecimal importeTrabadoBankAccount = seizedBankAccountDTO.getAmount() != null
						? seizedBankAccountDTO.getAmount()
						: BigDecimal.valueOf(0);
				importeTrabado = importeTrabado.add(importeTrabadoBankAccount);
			}
			traba.setImporteTrabado(importeTrabado);

			seizedRepository.save(traba);
		}

		for (SeizedBankAccountDTO seizedBankAccountDTO : seizureSave.getSeizedBankAccountList()) {

			Optional<CuentaTraba> cuentaTrabaOpt = seizedBankAccountRepository
					.findById(seizedBankAccountDTO.getIdSeizedBankAccount());

			if (cuentaTrabaOpt.isPresent()) {

				// Actualizar los campos informados
				CuentaTraba cuentaTraba = seizedBankAccountMapper.toCuentaTrabaForUpdate(seizedBankAccountDTO,
						cuentaTrabaOpt.get(), userModif);

				seizedBankAccountRepository.save(cuentaTraba);

			}
		}

		return true;
	}
	
	
	@Override
	public boolean updateSeizedBankStatus(CuentaTraba cuentaTraba, Long codEstado, String userModif) throws Exception {
		Traba traba = cuentaTraba.getTraba();
		
		BigDecimal fechaActualBigDec = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
		
		traba.setUsuarioUltModificacion(userModif);
		traba.setFUltimaModificacion(fechaActualBigDec);
		
		//Actualizar usuario y fecha ultima modificacion del Embargo (para que se inserte registro en el historico de embargos):			
		Embargo embargo = traba.getEmbargo();
		embargo.setUsuarioUltModificacion(userModif);
		embargo.setFUltimaModificacion(fechaActualBigDec);
		
		seizedRepository.save(traba);
		
		/*
			Grabar la propia actualización
		 */
		EstadoTraba estadoTraba = new EstadoTraba();
		estadoTraba.setCodEstado(codEstado);

		if (codEstado == EmbargosConstants.COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD) {
			cuentaTraba.setFechaValor(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMdd));
		}
		cuentaTraba.setEstadoTraba(estadoTraba);
		cuentaTraba.setUsuarioUltModificacion(userModif);
		cuentaTraba.setFUltimaModificacion(fechaActualBigDec);
								
		seizedBankAccountRepository.save(cuentaTraba);

		/*
			Si todas las cuentas del caso están completadas, avanzar el estado del caso.
		 */
		String fileFormat = EmbargosUtils.determineFileFormatByTipoFichero(embargo.getControlFichero().getTipoFichero().getCodTipoFichero());

		boolean isCGPJ = fileFormat!=null && fileFormat.equals(EmbargosConstants.FILE_FORMAT_CGPJ);
		boolean isAEAT = fileFormat!=null && fileFormat.equals(EmbargosConstants.FILE_FORMAT_AEAT);
		boolean isCuaderno63 = fileFormat!=null && fileFormat.equals(EmbargosConstants.FILE_FORMAT_NORMA63);

		if (isCGPJ) {
			boolean isTrabasCompletadas = isTrabaCompletada_CGPJ(traba);

			SeizureStatusDTO seizureStatusDTO = new SeizureStatusDTO();

			if (!isTrabasCompletadas)
				seizureStatusDTO.setCode(String.valueOf(COD_ESTADO_TRABA_PENDIENTE));
			else {
				if (algunaCuentaTieneTraba(traba))
					seizureStatusDTO.setCode(String.valueOf(COD_ESTADO_TRABA_CONTABILIZADA));
				else
					seizureStatusDTO.setCode(String.valueOf(COD_ESTADO_TRABA_FINALIZADA));
			}

			this.updateSeizureStatus(traba.getCodTraba(), seizureStatusDTO, userModif);
		}
		else if(isAEAT || isCuaderno63) {
			boolean isTrabasCompletadas = isTrabaCompletada_AEAT_N63(embargo.getTrabas().get(0));

			if (isTrabasCompletadas) {
				logger.info("Todas las cuentas de la traba " + traba.getCodTraba() + " se han tratado. Cambiando el estado a contabilizada");

				SeizureStatusDTO seizureStatusDTO = new SeizureStatusDTO();
				seizureStatusDTO.setCode(String.valueOf(COD_ESTADO_TRABA_CONTABILIZADA));

				this.updateSeizureStatus(traba.getCodTraba(), seizureStatusDTO, userModif);
			}
		}

		return true;
	}
	/*
	@Override
	public boolean updateSeizedBankStatusTransaction(CuentaTraba cuentaTraba, Long codEstado, String userModif) {
		
		return updateSeizedBankStatus(cuentaTraba, codEstado, userModif);
		
	}
	*/
	
	@Override
	public boolean updateSeizureStatus(Long idSeized, SeizureStatusDTO seizureStatusDTO,
			String userModif) throws Exception {
		Optional<Traba> trabaOpt = seizedRepository.findById(idSeized);

		if (!trabaOpt.isPresent()) {
			return false;
		}

		if (seizureStatusDTO != null && seizureStatusDTO.getCode() != null) {

			Traba traba = trabaOpt.get();

			// Actualizar estado:
			EstadoTraba estadoTraba = new EstadoTraba();
			estadoTraba.setCodEstado(Long.valueOf(seizureStatusDTO.getCode()));

			traba.setEstadoTraba(estadoTraba);

			if (estadoTraba.getCodEstado() == COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD) {
				traba.setFechaTraba(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMdd));
			}

			// Usuario y fecha ultima modificacion de la Traba:
			BigDecimal fechaActualBigDec = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
			traba.setUsuarioUltModificacion(userModif);
			traba.setFUltimaModificacion(fechaActualBigDec);

			// Actualizar usuario y fecha ultima modificacion del Embargo (para que se
			// inserte registro en el historico de embargos):
			Embargo embargo = traba.getEmbargo();
			embargo.setUsuarioUltModificacion(userModif);
			embargo.setFUltimaModificacion(fechaActualBigDec);

			//Dependiendo del tipo de fichero:
			String fileFormat = EmbargosUtils.determineFileFormatByTipoFichero(embargo.getControlFichero().getTipoFichero().getCodTipoFichero());

			boolean isCGPJ = fileFormat!=null && fileFormat.equals(EmbargosConstants.FILE_FORMAT_CGPJ);
			boolean isAEAT = fileFormat!=null && fileFormat.equals(EmbargosConstants.FILE_FORMAT_AEAT);
			boolean isCuaderno63 = fileFormat!=null && fileFormat.equals(EmbargosConstants.FILE_FORMAT_NORMA63);

			Long estado = null;
			boolean isTrabasCompletadas = false;

			if (isCGPJ) {
				estado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_PENDING_TO_SEND;
				isTrabasCompletadas = isAllTrabasCompletadas_CGPJ(embargo.getControlFichero());
			}
			else if(isAEAT) {
				estado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PENDING_TO_SEND;
				isTrabasCompletadas = isAllTrabasCompletadas_AEAT_N63(embargo.getControlFichero());
			}
			else if (isCuaderno63) {
				estado = EmbargosConstants.COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_PENDING_TO_SEND;
				isTrabasCompletadas = isAllTrabasCompletadas_AEAT_N63(embargo.getControlFichero());
			}

			if (isTrabasCompletadas) {
				fileControlService.updateFileControlStatus(embargo.getControlFichero().getCodControlFichero(), estado,
						userModif, "TRABAS");

				if (isCGPJ) {
					petitionService.synchPetitionStatus(embargo.getControlFichero().getPeticiones().get(0));
				}
			}

			seizedRepository.save(traba);

		} else {
			return false;
		}

		return true;
	}

	private boolean isAllTrabasCompletadas_AEAT_N63(ControlFichero cf) {
		boolean isAllTrabasCompletadas = true;

		for (Embargo emb : cf.getEmbargos()) {
			Traba currentTraba = emb.getTrabas().get(0);
			if (!isTrabaCompletada_AEAT_N63(currentTraba)) {
				isAllTrabasCompletadas = false;
				break;
			}
		}

		return isAllTrabasCompletadas;
	}

	private boolean isTrabaCompletada_AEAT_N63(Traba traba) {
		boolean isAllTrabasContabilizadas = true;

		if (traba.getEstadoTraba().getCodEstado() == COD_ESTADO_TRABA_CONTABILIZADA ||
				traba.getEstadoTraba().getCodEstado() == COD_ESTADO_TRABA_FINALIZADA) {
			; // está contabilizada o finalizada. ok
		}
		else {
			isAllTrabasContabilizadas = false;
		}

		return isAllTrabasContabilizadas;
	}

	private boolean isAllTrabasCompletadas_CGPJ(ControlFichero cf) {
		boolean isCompleted = true;

		if (cf.getPeticiones() == null || cf.getPeticiones().size() != 1) {
			logger.error("El control fichero del consejo " + cf.getCodControlFichero() + " debería tener una única petición asociada");
			return false;
		}

		for (SolicitudesTraba solicitudTraba : cf.getPeticiones().get(0).getSolicitudesTrabas()) {
			Traba traba = solicitudTraba.getTraba();

			if (!isTrabaCompletada_CGPJ(traba)) {
				isCompleted = false;
				break;
			}
		}

		return isCompleted;
	}

	private boolean isTrabaCompletada_CGPJ(Traba traba) {
		boolean isCompleted = true;

		// 1- SI NO ESTÁ REVISADO, LA TRABA NO ESTÁ COMPLETADA
		if (!IND_FLAG_SI.equals(traba.getRevisado())) {
			isCompleted = false;
		}
		// 2- SI LA TRABA ESTÁ CONTABILIZADA, ENTONCES ES CORRECTO
		else if (traba.getEstadoTraba().getCodEstado() == EmbargosConstants.COD_ESTADO_TRABA_CONTABILIZADA) {
			;
		}
		else {
			// 3- MIRAMOS SUS CUENTAS. O BIEN ESTÁ PENDIENTE Y TODAS SUS CUENTAS TIENEN UN MOTIVO DEL RECHAZO distinto de 00 - sin actuación 200x trabas
			// 3.a- debe tener alguna cuenta revisada
			// 3.b- no debe tener cuentas pendientes de contabilización
			// 3.c- en las cuentas revisadas, debe tener en alguna el motivo de rechazo seteado
			boolean tieneAlgunaCuentaRevisada = false;
			boolean tieneAlgunaCuentaRechazadaConMotivo = false;
			boolean tieneCuentasPendientesDeContabilizacion = false;
			for (CuentaTraba cuenta : traba.getCuentaTrabas()) {
				if (IND_FLAG_YES.equals(cuenta.getAgregarATraba())) {
					tieneAlgunaCuentaRevisada = true;
					if (CGPJ_MOTIVO_TRABA_TOTAL.equals(cuenta.getCuentaTrabaActuacion().getCodExternoActuacion())
							||
							CGPJ_MOTIVO_TRABA_PARCIAL.equals(cuenta.getCuentaTrabaActuacion().getCodExternoActuacion())
					) {
						if (cuenta.getEstadoTraba().getCodEstado() != COD_ESTADO_TRABA_CONTABILIZADA) {
							tieneCuentasPendientesDeContabilizacion = true;
						}
					} else {
						if (cuenta.getCuentaTrabaActuacion().getCodExternoActuacion() != null) {
							tieneAlgunaCuentaRechazadaConMotivo = true;
						}
					}
				}
			}
			if (!tieneAlgunaCuentaRevisada) {
				isCompleted = false;
			}
			if (tieneCuentasPendientesDeContabilizacion) {
				isCompleted = false;
			}
			if (!tieneAlgunaCuentaRechazadaConMotivo) {
				isCompleted = false;
			}
		}

		return isCompleted;
	}

	private boolean algunaCuentaTieneTraba(Traba traba) {
		boolean aTrabar = false;

		for (CuentaTraba cuenta : traba.getCuentaTrabas()) {
			if (CGPJ_MOTIVO_TRABA_TOTAL.equals(cuenta.getCuentaTrabaActuacion().getCodExternoActuacion())
					||
				CGPJ_MOTIVO_TRABA_PARCIAL.equals(cuenta.getCuentaTrabaActuacion().getCodExternoActuacion())
			) {
				aTrabar = true;
				break;
			}
		}

		return aTrabar;
	}
	
	@Override
	public boolean updateSeizureStatusTransaction(Long idSeized, SeizureStatusDTO seizureStatusDTO,
			String userModif) throws Exception {
		
		return updateSeizureStatus(idSeized, seizureStatusDTO, userModif);
		
	}	
	
	@Override
	public boolean updateAccountSeizureStatus(Long idAccount, Long idSeizure, AccountStatusSeizedDTO accountStatusSeized,
			String userModif) throws Exception {
		
		Optional<CuentaTraba> cuentaTrabaOpt = seizedBankAccountRepository.findById(idAccount);

		if (!cuentaTrabaOpt.isPresent()) {
			return false;
		}

		updateSeizedBankStatus(cuentaTrabaOpt.get(), Long.valueOf(accountStatusSeized.getCode()), userModif);

		return true;
	}

	@Override
	public boolean undoAccounting(Long codeFileControl, Long idSeizure, Long idAccount, String userName)
		throws Exception
	{
		Optional<ControlFichero> controlFicheroOpt = fileControlRepository.findById(codeFileControl);

		if (!controlFicheroOpt.isPresent()) {
			throw new Exception("Fichero "+ codeFileControl +" inexistente");
		}

		Optional<Traba> trabaOpt = seizedRepository.findById(idSeizure);

		if (!trabaOpt.isPresent()) {
			throw new Exception("Traba "+ idSeizure +" inexistente");
		}

		Optional<CuentaTraba> cuentaTrabaOpt = seizedBankAccountRepository.findById(idAccount);

		if (!cuentaTrabaOpt.isPresent()) {
			throw new Exception("Cuenta "+ idAccount +" inexistente");
		}

		ControlFichero controlFichero = controlFicheroOpt.get();
		Traba traba = trabaOpt.get();
		CuentaTraba cuentaTraba = cuentaTrabaOpt.get();

		Long estadoTrabaActual = cuentaTraba.getEstadoTraba().getCodEstado();

		if (!Long.valueOf(COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD).equals(estadoTrabaActual) &&
			!Long.valueOf(COD_ESTADO_TRABA_CONTABILIZADA).equals(estadoTrabaActual)) {
			// No se puede invocar en este estado. Devolver conflict.
			return false;
		}

		//Si el estado del caso, antes del cambio, era Contabilizado, deshacer el movimiento contable
		if (estadoTrabaActual != null && estadoTrabaActual.equals(Long.valueOf(COD_ESTADO_TRABA_CONTABILIZADA))) {
			logger.info("Realizando extorno");
			accountingService.extornoContabilizar(controlFichero, traba, cuentaTraba, userName);
		}

		// Retrocesos de estado
		BigDecimal fechaActualBigDec = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);

		//Se actualiza el estado de controlFichero a Recibido
		String tipoEntidad = EmbargosUtils.determineFileFormatByTipoFichero(controlFichero.getTipoFichero().getCodTipoFichero());
		EstadoCtrlfichero estadoCtrlfichero = null;
		if (FILE_FORMAT_NORMA63.equals(tipoEntidad)) {
			estadoCtrlfichero = new EstadoCtrlfichero(COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_RECEIVED, COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_NORMA63);
		} else if (FILE_FORMAT_AEAT.equals(tipoEntidad)) {
			estadoCtrlfichero = new EstadoCtrlfichero(COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_RECEIVED, COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_AEAT);
		} else if (FILE_FORMAT_CGPJ.equals(tipoEntidad)) {
			estadoCtrlfichero = new EstadoCtrlfichero(COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_RECEIVED, COD_TIPO_FICHERO_PETICION_CGPJ);
		}

		fileControlService.updateFileControlStatus(controlFichero.getCodControlFichero(), estadoCtrlfichero.getId().getCodEstado(), userName, "TRABAS");

		/*
			Si es del Consejo y la petición estaba Procesada, se revierte a Inicial
		 */
		if (FILE_FORMAT_CGPJ.equals(tipoEntidad)) {
			petitionService.revertStatusToInitial(controlFichero.getPeticiones().get(0));
		}

		//Se actualiza el estado del idSeizure a Pendiente
		EstadoTraba estadoTraba = new EstadoTraba();
		estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE);

		traba.setEstadoTraba(estadoTraba);

		traba.setUsuarioUltModificacion(userName);
		traba.setFUltimaModificacion(fechaActualBigDec);

		seizedRepository.save(traba);

		// Se cambia el estado a Pendiente para permitir su edición.
		// Se retroceden también las cuentas trabas que estuvieran en estado finalizado (es decir, no contabilizadas)
		// para permitir al usuario modificar su acción o contabilizar de ellas.
		for (CuentaTraba cuentaTrabaActual : traba.getCuentaTrabas()) {
			boolean retroceder = false;

			if (cuentaTrabaActual.getCodCuentaTraba() == cuentaTraba.getCodCuentaTraba())
				retroceder = true;
			else if (cuentaTrabaActual.getEstadoTraba().getCodEstado() == COD_ESTADO_TRABA_FINALIZADA)
				retroceder = true;
			else if (BigDecimal.ZERO.compareTo(cuentaTrabaActual.getImporte()) == 0)
				retroceder = true;

			if (retroceder) {
				EstadoTraba estadoCuentaTraba = new EstadoTraba();
				estadoCuentaTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE);

				cuentaTrabaActual.setEstadoTraba(estadoCuentaTraba);

				cuentaTrabaActual.setUsuarioUltModificacion(userName);
				cuentaTrabaActual.setFUltimaModificacion(fechaActualBigDec);

				seizedBankAccountRepository.save(cuentaTrabaActual);
			}
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

			List<Long> pendientes = accountingService.embargoListaAContabilizar(codeFileControl);

			parameters.put("CUENTA_TRABA_EXPRESSION", SQLUtils.calcInExpression(pendientes, "ct.cod_cuenta_traba"));
			parameters.put("CUENTA_LEVANTAMIENTO_EXPRESSION", SQLUtils.calcInExpression(new ArrayList<>(), "cl.COD_CUENTA_LEVANTAMIENTO"));
			parameters.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));

			InputStream isInforme = informe.getInputStream();
			JasperPrint reporteLleno =  JasperFillManager.fillReport(isInforme, parameters, conn_embargos);
			return JasperExportManager.exportReportToPdf(reporteLleno);
		} catch (Exception e) {
			throw new Exception("DB exception while generating the report", e);
		}
	}

	@Override
	public List<SeizureDTO> getAuditSeizure(Long codFileControl, Long idSeizure) {
		
		List<SeizureDTO> seizureDTOList = new ArrayList<>();

		List<HTraba> hSeizureDTOList = hSeizedRepository.findByIdSeizure(idSeizure);

		for (HTraba hTraba : hSeizureDTOList) {

			SeizureDTO seizureDTO = hSeizureMapper.toSeizureDTO(hTraba.getHEmbargo());

			seizureDTOList.add(seizureDTO);
		}

		return seizureDTOList;
	}

	@Override
	public List<SeizedBankAccountDTO> getAuditSeizedBankAccounts(Long codFileControl, Long idSeizure, Long codAudit) {
		List<SeizedBankAccountDTO> seizureDTOList = new ArrayList<>();

		// Traba traba = new Traba();
		// traba.setCodTraba(idSeizure);

		// List<HCuentaTraba> hSeizedBankAccountDTOList =
		// hSeizedBankAccountRepository.findAllByTraba(traba);
		List<HCuentaTraba> hSeizedBankAccountDTOList = hSeizedBankAccountRepository
				.findAllByCodTrabaAndCodAudit(BigDecimal.valueOf(idSeizure), codAudit);

		for (HCuentaTraba hCuentaTraba : hSeizedBankAccountDTOList) {

			SeizedBankAccountDTO seizedBankAccountDTO = hSeizedBankAccountMapper.toSeizedBankAccountDTO(hCuentaTraba);

			seizureDTOList.add(seizedBankAccountDTO);
		}
		
		return seizureDTOList;
	}

	@Override
	public byte[] reportSeizureLetter(Long idSeizure) throws Exception {

		Location location = locationService.getLocationByOfficePrefix(MADRID_PREFIJO_OFICINA);

		JasperPrint fillReport = reportSeizureLetterInternal(idSeizure, location);

		if (fillReport == null)
			return null;

		List<JRPrintPage> pages = fillReport.getPages();

		if (pages.size() == 0)
			return null;

		return JasperExportManager.exportReportToPdf(fillReport);
	}

	private JasperPrint reportSeizureLetterInternal(Long idSeizure, Location location) throws Exception {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		Optional<Embargo> optEmbargo = seizureRepository.findById(idSeizure);

		if (!optEmbargo.isPresent()) {
			throw new Exception("Embargo "+ idSeizure +" no encontrado");
		}

		Embargo embargo = optEmbargo.get();

		if (embargo.getTrabas().size() != 1) {
			throw new Exception("Embargo "+ idSeizure +" sin traba");
		}

		Traba traba = embargo.getTrabas().get(0);

		// Si hay algun importe trabado, enviar la carta

		boolean hasImporteTrabado = false;
		for (CuentaTraba cuentaTraba : traba.getCuentaTrabas()) {
			if (cuentaTraba.getImporte() != null && BigDecimal.ZERO.compareTo(cuentaTraba.getImporte()) < 0) {
				hasImporteTrabado = true;
				break;
			}
		}

		if (!hasImporteTrabado) {
			return null;
		}

		try (Connection conn = oracleDataSourceEmbargos.getEmbargosConnection()) {

			//ControlFichero controlFichero = embargo.getControlFichero();
			//EntidadesComunicadora entidadesComunicadora  = controlFichero.getEntidadesComunicadora();

			Resource report = ResourcesUtil.getFromJasperFolder("cartaEmbargo.jasper");
			Resource logoRes = es.commerzbank.ice.comun.lib.util.jasper.ResourcesUtil.getImageLogoCommerceResource();

			CustomerDTO customer = accountService.getCustomerByNIF(embargo.getNif());

			if (customer != null) {
				parameters.put("DESTINATARIO", DWHUtils.getDestinatario(customer));
			}

			parameters.put("CODIGO", traba.getCodTraba());
			parameters.put("LOCALIDAD", location.getLocation());
			//parameters.put("ENTIDAD", entidadesComunicadora.getDesEntidad());
			parameters.put("logo_image", logoRes.getFile().toString());
			parameters.put("TIPO", "EMBARGO");

			parameters.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));

			InputStream justificanteInputStream = report.getInputStream();

			return JasperFillManager.fillReport(justificanteInputStream, parameters, conn);

		} catch (Exception e) {

			throw new Exception("DB exception while generating the report", e);
		}
	}

	@Override
	public byte[] reportSeizureResponseF4(Integer codControlFichero, String oficina) throws Exception {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource resumenTrabasJrxml = ResourcesUtil.getFromJasperFolder("F4_trabas.jasper");

			Resource logoRes = ResourcesUtil.getImageLogoCommerceResource();

			Resource subreportLeyendaResource = ResourcesUtil.getLeyendaMotivos();
			InputStream subreportLeyendaInputStream = subreportLeyendaResource.getInputStream();
			JasperReport subreportLeyenda = (JasperReport) JRLoader.loadObject(subreportLeyendaInputStream);
			parameters.put("SUBREPORT_LEYENDA", subreportLeyenda);

			parameters.put("img_param", logoRes.getFile().toString());
			parameters.put("COD_FILE_CONTROL", codControlFichero);
			parameters.put("NOMBRE_SUCURSAL", oficina);

			parameters.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));

			InputStream resumenInputStream = resumenTrabasJrxml.getInputStream();

			JasperPrint fillReport = JasperFillManager.fillReport(resumenInputStream, parameters, conn);

			List<JRPrintPage> pages = fillReport.getPages();

			if (pages.size() == 0)
				return null;
			
			return JasperExportManager.exportReportToPdf(fillReport);

		} catch (Exception e) {
			throw new Exception("DB exception while generating the report", e);
		}
	}

	@Override
	public byte[] reportSeizureRequestF3(Integer codControlFichero, String oficina) throws Exception {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource resumenTrabasJrxml = ResourcesUtil.getFromJasperFolder("F3_diligenciasEmbargo.jasper");

			Resource logoRes = ResourcesUtil.getImageLogoCommerceResource();

			parameters.put("img_param", logoRes.getFile().toString());
			parameters.put("COD_FILE_CONTROL", codControlFichero);
			parameters.put("NOMBRE_SUCURSAL", oficina);

			parameters.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));

			InputStream resumenInputStream = resumenTrabasJrxml.getInputStream();

			JasperPrint fillReport = JasperFillManager.fillReport(resumenInputStream, parameters, conn);

			List<JRPrintPage> pages = fillReport.getPages();

			if (pages.size() == 0)
				return null;
			
			return JasperExportManager.exportReportToPdf(fillReport);

		} catch (SQLException e) {
			throw new Exception("DB exception while generating the report", e);
		}

	}

	@Override
	public void generateSeizureLetters(ControlFichero controlFichero) throws Exception {
		List<Embargo> seizures = seizureRepository.findAllByControlFichero(controlFichero);

		Location location = locationService.getLocationByOfficePrefix(MADRID_PREFIJO_OFICINA);

		if (seizures != null && seizures.size() > 0)
		{
			File temporaryFile = reportHelper.getTemporaryFile("cartas-embargo-"+ controlFichero.getCodControlFichero(), ReportHelper.PDF_EXTENSION);
			PdfDocument outDoc = new PdfDocument(new PdfWriter(temporaryFile));

			int pageCount = 1;

			for (Embargo embargo : seizures)
			{
				JasperPrint filledReport = reportSeizureLetterInternal(embargo.getCodEmbargo(), location);

				if (filledReport != null) {
					reportHelper.dumpReport(outDoc, filledReport, pageCount);
					pageCount++;
				}
			}

			logger.info("Se han acumulado "+ outDoc.getNumberOfPages() +" páginas de cartas a enviar");

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
}
