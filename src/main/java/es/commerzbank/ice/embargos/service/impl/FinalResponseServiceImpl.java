package es.commerzbank.ice.embargos.service.impl;

import es.commerzbank.ice.comun.lib.domain.entity.Tarea;
import es.commerzbank.ice.comun.lib.repository.TaskRepo;
import es.commerzbank.ice.comun.lib.service.GeneralParametersService;
import es.commerzbank.ice.comun.lib.service.TaskService;
import es.commerzbank.ice.comun.lib.typeutils.ICEDateUtils;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.comun.lib.util.ValueConstants;
import es.commerzbank.ice.embargos.config.OracleDataSourceEmbargosConfig;
import es.commerzbank.ice.embargos.domain.dto.FinalResponseBankAccountDTO;
import es.commerzbank.ice.embargos.domain.dto.FinalResponseDTO;
import es.commerzbank.ice.embargos.domain.dto.FinalResponsePendingDTO;
import es.commerzbank.ice.embargos.domain.entity.*;
import es.commerzbank.ice.embargos.domain.mapper.CommunicatingEntityMapper;
import es.commerzbank.ice.embargos.domain.mapper.FileControlMapper;
import es.commerzbank.ice.embargos.domain.mapper.FinalResponseBankAccountMapper;
import es.commerzbank.ice.embargos.domain.mapper.FinalResponseMapper;
import es.commerzbank.ice.embargos.repository.FileControlRepository;
import es.commerzbank.ice.embargos.repository.FinalFileRepository;
import es.commerzbank.ice.embargos.repository.FinalResponseBankAccountRepository;
import es.commerzbank.ice.embargos.repository.FinalResponseRepository;
import es.commerzbank.ice.embargos.service.FinalResponseGenerationService;
import es.commerzbank.ice.embargos.service.FinalResponseService;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.ResourcesUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.codec.Charsets;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@Service
@Transactional(transactionManager = "transactionManager")
public class FinalResponseServiceImpl implements FinalResponseService {
	private static final Logger logger = LoggerFactory.getLogger(FinalResponseServiceImpl.class);

	// Repositorio FINAL_FILE
	@Autowired
	private FinalFileRepository finalFileRepository;

	@Autowired
	private FinalResponseMapper finalResponseMapper;

	@Autowired
	private FinalResponseBankAccountRepository bankAccountRepository;

	@Autowired
	private FinalResponseBankAccountMapper bankAccountMapper;

	@Autowired
	OracleDataSourceEmbargosConfig oracleDataSourceEmbargos;

	@Autowired
	private FinalResponseRepository finalResponseRepository;

	@Autowired
	private FinalResponseGenerationService finalResponseGenerationService;

	@Autowired
	private FileControlRepository fileControlRepository;

	@Autowired
	private TaskService taskService;

	@Autowired
	private FileControlMapper fileControlMapper;

	@Autowired
	private CommunicatingEntityMapper communicatingEntityMapper;

	@Autowired
	private TaskRepo taskRepo;

	@Autowired
	private GeneralParametersService generalParametersService;

	@Override
	public List<FinalResponseDTO> getAllByControlFichero(ControlFichero controlFichero) {
		List<ResultadoEmbargo> result = finalResponseRepository.findAllByControlFichero(controlFichero);

		List<FinalResponseDTO> response = new ArrayList<FinalResponseDTO>();
		for (ResultadoEmbargo resultado : result) {
			FinalResponseDTO element = finalResponseMapper.toFinalResponse(resultado);
			response.add(element);
		}

		return response;
	}

	@Override
	public FinalResponseDTO AddBankAccountList(Long codeFileControl, Long codeFinalResponse) {
		List<FinalResponseBankAccountDTO> list = new ArrayList<>();
		FinalResponseDTO response = null;

		Optional<ResultadoEmbargo> result = finalResponseRepository.findById(codeFinalResponse);

		if (result != null) {
			response = finalResponseMapper.toFinalResponse(result.get());

			ResultadoEmbargo resultado = new ResultadoEmbargo();
			resultado.setCodResultadoEmbargo(codeFinalResponse);

			List<CuentaResultadoEmbargo> cuentas = bankAccountRepository.findByResultadoEmbargo(resultado);

			for (CuentaResultadoEmbargo cuenta : cuentas) {
				FinalResponseBankAccountDTO account = bankAccountMapper.toBankAccount(cuenta);
				if (account.getAmountLocked()!=null && account.getAmountTransfer()!=null)
					account.setAmountRaised(account.getAmountLocked() - account.getAmountTransfer());
				list.add(account);
			}

			response.setList(list);
		}

		return response;
	}

	@Override
	public byte[] generarAnexo(BigDecimal cod_usuario, BigDecimal cod_traba, Integer num_anexo) throws Exception {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn_embargos = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource anexoJasperFile = ResourcesUtil.getFromJasperFolder("TGSSAnexo" + num_anexo + ".jasper");

			Resource importeAbonadoSubReport = ResourcesUtil.getFromJasperFolder("importe_abonado.jasper");

			Resource logoImage = ResourcesUtil.getImageLogoCommerceResource();

			File image = logoImage.getFile();

			InputStream importeAbonadoInputStream = importeAbonadoSubReport.getInputStream();
			JasperReport importeAbonadoReport = (JasperReport) JRLoader.loadObject(importeAbonadoInputStream);

			parameters.put("IMAGE_PARAM", image.toString());
			parameters.put("REPORT_IMPORTE_ABONADO", importeAbonadoReport);
			parameters.put("COD_USUARIO", cod_usuario);
			parameters.put("COD_TRABA", cod_traba);

			parameters.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));
	

			InputStream anexo1Input = anexoJasperFile.getInputStream();
			JasperPrint fillReport = JasperFillManager.fillReport(anexo1Input, parameters, conn_embargos);

			List<JRPrintPage> pages = fillReport.getPages();

			if (pages.size() == 0)
				return null;

			return JasperExportManager.exportReportToPdf(fillReport);

		} catch (SQLException e) {
			throw new Exception("DB exception while generating the report", e);
		}
	}

	@Override
	public byte[] generarRespuestaFinalEmbargo(Integer cod_file_control) throws Exception {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn_embargos = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource respFinalEmbargoResource = ResourcesUtil.getFromJasperFolder("f6_finalization.jasper");
			Resource headerResource = ResourcesUtil.getReportHeaderResource();
			Resource imageResource = ResourcesUtil.getImageLogoCommerceResource();

			File image = imageResource.getFile();

			InputStream subReportHeaderInputStream = headerResource.getInputStream();

			JasperReport subReportHeader = (JasperReport) JRLoader.loadObject(subReportHeaderInputStream);

			parameters.put("sub_img_param", image.toString());
			parameters.put("SUBREPORT_HEADER", subReportHeader);
			parameters.put("COD_FILE_CONTROL", cod_file_control);

			parameters.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));

			InputStream finalEmbargosIS = respFinalEmbargoResource.getInputStream();
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
	public byte[] generatePaymentLetterCGPJ(String cod_traba) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn_embargos = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource transOrder = ResourcesUtil.getFromJasperFolder("paymentLetterCGPJ.jasper");
			Resource imageLogo = ResourcesUtil.getImageLogoCommerceResource();

			File logoFile = imageLogo.getFile();

			parameters.put("IMAGE_PARAM", logoFile.toString());
			parameters.put("COD_TRABA", cod_traba);

			parameters.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));

			InputStream isOrdenTransferencia = transOrder.getInputStream();
			JasperPrint transOrderJP = JasperFillManager.fillReport(isOrdenTransferencia, parameters, conn_embargos);

			List<JRPrintPage> pages = transOrderJP.getPages();

			if (pages.size() == 0)
				return null;

			return JasperExportManager.exportReportToPdf(transOrderJP);

		} catch (Exception e) {
			throw new Exception("DB exception while generating the report", e);
		}

	}
	

	@Override
	public byte[] generatePaymentLetterN63(String cod_control_fichero) throws Exception {

		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try (Connection conn_embargos = oracleDataSourceEmbargos.getEmbargosConnection()) {

			Resource transOrder = ResourcesUtil.getFromJasperFolder("paymentLetterN63.jasper");
			Resource imageLogo = ResourcesUtil.getImageLogoCommerceResource();

			File logoFile = imageLogo.getFile();

			parameters.put("IMAGE_PARAM", logoFile.toString());
			parameters.put("COD_CONTROL_FICHERO", cod_control_fichero);

			parameters.put(JRParameter.REPORT_LOCALE, new Locale("es", "ES"));

			InputStream isOrdenTransferencia = transOrder.getInputStream();
			JasperPrint transOrderJP = JasperFillManager.fillReport(isOrdenTransferencia, parameters, conn_embargos);
			
			List<JRPrintPage> pages = transOrderJP.getPages();
			 
			 if (pages.size() == 0)  return null;

			return JasperExportManager.exportReportToPdf(transOrderJP);

		} catch (SQLException e) {
			throw new Exception("DB exception while generating the report", e);
		}

	}

	@Override
	public boolean updateFinalFileAccountingStatus(FicheroFinal ficheroFinal, Long codEstadoContabilizacion,
												   String userName) {

		EstadoContabilizacion estadoContabilizacion = new EstadoContabilizacion();
		estadoContabilizacion.setCodEstado(codEstadoContabilizacion);

		ficheroFinal.setEstadoContabilizacion(estadoContabilizacion);

		//Usuario y fecha de ultima modificacion:
		ficheroFinal.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
		ficheroFinal.setUsuarioUltModificacion(userName);

		finalFileRepository.save(ficheroFinal);

		return false;
	}

	@Override
	public Long calcFinalResult(Long codeFileControlFase3, String user)
		throws Exception
	{
		ControlFichero ficheroFase3 = fileControlRepository.getOne(codeFileControlFase3);
		EntidadesComunicadora entidadComunicadora = ficheroFase3.getEntidadesComunicadora();

		ControlFichero ficheroFase4 = ficheroFase3.getControlFicheroRespuesta();

		List<Tarea> tasks = taskService.getTaskPendingByExternalIdLike(EmbargosConstants.EXTERNAL_ID_F6_N63 + ficheroFase4.getCodControlFichero());

		if (tasks == null || tasks.size() == 0) {
			logger.error("No se ha encontrado la tarea pendiente para ejecutar el fin de ciclo. Fichero F3 indicado: "+ codeFileControlFase3);
			return null;
		}
		if (tasks.size() != 1) {
			logger.error("Se han encontrado "+ tasks.size() +" tareas pendientes para ejecutar el fin de ciclo, se esperaba una sola. Fichero F3 indicado: "+ codeFileControlFase3);
			return null;
		}

		Tarea tarea = tasks.get(0);

		logger.info("Ejecutando fin de ciclo "+ entidadComunicadora.getDesEntidad() +" fichero F3 "+ codeFileControlFase3 +" F4 "+ ficheroFase4.getCodControlFichero());

		// Generar el contenido del cierre
		FicheroFinal finalFile = finalResponseGenerationService.calcFinalResult(ficheroFase3, user);

		ControlFichero ficheroFase6 = finalFile.getControlFichero();

		logger.info("Fin de ciclo - generado F6 "+ ficheroFase6);

		taskService.changeStatus(tarea.getCodTarea(), ValueConstants.STATUS_TASK_FINISH, user);
		
		return ficheroFase6.getCodControlFichero();
	}

	@Override
	public List<FinalResponsePendingDTO> listPendingCyclesNorma63() throws Exception {
		List<FinalResponsePendingDTO> result = null;
		List<Tarea> tareas = taskService.getTaskPendingByExternalIdLike(EmbargosConstants.EXTERNAL_ID_F6_N63);

		if (tareas == null || tareas.size() == 0) {
			logger.debug("No se han encontrado tareas de F6 pendientes.");
		}
		else {
			logger.debug("Se han encontrado " + tareas.size() + " tareas de F6 pendientes.");
			result = new ArrayList<FinalResponsePendingDTO>();

			for (Tarea tarea : tareas) {
				try {
					if (tarea.getExternalId() == null) {
						logger.error("Tarea " + tarea.getCodTarea() + " sin identificador externo");
						continue;
					}

					String[] partes = tarea.getExternalId().split("_");

					if (partes.length != 2) {
						logger.error(
								"Formato de identificador externo de la tarea " + tarea.getCodTarea() + " no reconocido: "
										+ tarea.getExternalId());
						continue;
					}

					String codControlFichero = partes[1];

					Optional<ControlFichero> controlFicheroOptF4 = fileControlRepository.findById(Long.parseLong(codControlFichero));
					if (!controlFicheroOptF4.isPresent()) {
						logger.error("ControlFichero F4 " + codControlFichero + " no encontrado");
						continue;
					}
					ControlFichero controlFicheroF4 = controlFicheroOptF4.get();

					ControlFichero controlFicheroF3 = null;
					if (controlFicheroF4 != null && controlFicheroF4.getControlFicheroOrigen() != null) {
						controlFicheroF3 = controlFicheroF4.getControlFicheroOrigen();
					} else {
						logger.error("ControlFichero F3 origen de " + codControlFichero + " no encontrado");
					}

					FinalResponsePendingDTO finalResponsePendingDTO = new FinalResponsePendingDTO();
					finalResponsePendingDTO.setLastDateResponse(tarea.getfTarea());
					if (controlFicheroF4 != null) {
						finalResponsePendingDTO.setFileControlDTOF4(fileControlMapper.toFileControlDTO(controlFicheroF4));
						if (controlFicheroF4.getEntidadesComunicadora() != null)
							finalResponsePendingDTO.setCommunicatingEntity(communicatingEntityMapper.toCommunicatingEntity(controlFicheroF4.getEntidadesComunicadora()));
					}
					if (controlFicheroF3 != null)
						finalResponsePendingDTO.setFileControlDTOF3(fileControlMapper.toFileControlDTO(controlFicheroF3));

					result.add(finalResponsePendingDTO);
				} catch (Exception e) {
					logger.error("Error mientras se recuperaba la tareas de F6 " + tarea.getCodTarea(), e);
				}
			}
		}

		return result;
	}

	@Override
	public boolean jobTransferToTax(String authorization, String user) throws ICEException {
		// TODO: OBTENER EL IMPORTE DE LA TABLA resultado_embargo del campo total_neto

		try {
			List<Tarea> tareas = taskService.getTareasPendientesByExternalIdLike(EmbargosConstants.EXTERNAL_ID_F6_AEAT);

			if (tareas!=null && tareas.size()>0) {
				logger.info("Se han encontrado "+ tareas.size() +" tareas de F6 AEAT pendientes cuya fecha de expiración es hoy o anterior.");
				String uri = generalParametersService.loadStringParameter(ValueConstants.PARAMETRO_TSP_DOMINIO) + generalParametersService.loadStringParameter(EmbargosConstants.ENDPOINT_EMBARGOS_TO_TAX);

				for (Tarea tarea : tareas) {
					try {
						if (tarea.getExternalId() == null) {
							logger.error("Tarea " + tarea.getCodTarea() + " sin identificador externo");
							continue;
						}

						String[] partes = tarea.getExternalId().split("_");

						if (partes.length != 2) {
							logger.error(
									"Formato de identificador externo de la tarea " + tarea.getCodTarea() + " no reconocido: "
											+ tarea.getExternalId());
							continue;
						}

						String codControlFichero = partes[1];

						Optional<ControlFichero> controlFicheroOptF4 = fileControlRepository.findById(Long.parseLong(codControlFichero));
						if (!controlFicheroOptF4.isPresent())
						{
							logger.error("ControlFichero F4 " + codControlFichero + " no encontrado");
							continue;
						}
						ControlFichero controlFicheroF4 = controlFicheroOptF4.get();

						ControlFichero controlFicheroF3 = null;
						if (controlFicheroF4!=null && controlFicheroF4.getControlFicheroOrigen()!=null) {
							controlFicheroF3 = controlFicheroF4.getControlFicheroOrigen();

							// Calcula los datos para realizar el cierre
							logger.info("Calculando los datos para realizar el cierre para el fichero: " + controlFicheroF3.getCodControlFichero());

							FicheroFinal ficheroFinal = finalResponseGenerationService.calcFinalResult(controlFicheroF3, user);

							List<ResultadoEmbargo> resultadoEmbargos = finalResponseRepository.findAllByControlFichero(ficheroFinal.getControlFichero());

							for (ResultadoEmbargo resultadoEmbargo : resultadoEmbargos)
							{
								logger.info("Traspasando el resultado embargo cod " + resultadoEmbargo.getCodResultadoEmbargo() + " embargo "+
										resultadoEmbargo.getEmbargo().getNumeroEmbargo() +" a impuesto");

								for (CuentaResultadoEmbargo cuentaResultadoEmbargo : resultadoEmbargo.getCuentaResultadoEmbargos())
								{
									if (BigDecimal.ZERO.compareTo(cuentaResultadoEmbargo.getImporteNeto()) < 0) {
										logger.info("Cargando en " + cuentaResultadoEmbargo.getCodCuentaResultadoEmbargo() + " cuenta " +
												cuentaResultadoEmbargo.getCuentaTraba().getCuenta() +" importe "+ cuentaResultadoEmbargo.getImporteNeto());
										boolean result = transferEmbargoToTax(resultadoEmbargo, cuentaResultadoEmbargo, uri, authorization, user);
										if (!result) {
											logger.error("Error cargando en " + cuentaResultadoEmbargo.getCodCuentaResultadoEmbargo() + " cuenta " +
													cuentaResultadoEmbargo.getCuentaTraba().getCuenta() +" importe "+ cuentaResultadoEmbargo.getImporteNeto());
										}
									}
								}
							}

							tarea.setEstado(ValueConstants.STATUS_TASK_FINISH);
							tarea.setfTarea(new Timestamp(new Date().getTime()));
							taskRepo.save(tarea);
						}
						else {
							logger.error("ControlFichero F3 origen de " + codControlFichero + " no encontrado");
						}
					}
					catch (Exception e)
					{
						logger.error("Error mientras se recuperaba la tareas de F6 AEAT "+ tarea.getCodTarea(), e);
					}
				}
			}
			else {
				logger.info("No se han encontrado tareas de F6 AEAT pendientes.");
			}

		} catch (Exception e) {
			logger.error("ERROR in jobTransferToTax: ", e);
		}

		return true;
	}

	private boolean transferEmbargoToTax(ResultadoEmbargo resultadoEmbargo, CuentaResultadoEmbargo cuentaResultadoEmbargo,
										 String uri, String authorization, String user)
	{
		boolean result = false;

		HttpClient httpClient = HttpClients.custom().build();
		HttpPost request = new HttpPost(uri);

		String message = "{\"user\": \"" + user +
				"\", \"nombre\": \"" + resultadoEmbargo.getEmbargo().getRazonSocialInterna() +
				"\", \"cuenta\": \"" + cuentaResultadoEmbargo.getCuentaTraba().getCuenta() +
				"\", \"nif\": \"" + resultadoEmbargo.getEmbargo().getNif() +
				"\", \"sucursal\": \"" + EmbargosConstants.SUCURSAL_CREACION_IMPUESTOS +
				"\", \"importe\": \"" + cuentaResultadoEmbargo.getImporteNeto() +
				"\", \"numEmbargo\": \"" + resultadoEmbargo.getEmbargo().getNumeroEmbargo() + "\"}";

		request.setEntity(new StringEntity(message, ContentType.create(MimeTypeUtils.APPLICATION_JSON_VALUE, Charsets.UTF_8)));

		try {
			request.setHeader("Content-Type", "application/json");
			request.setHeader("Authorization", authorization);

			HttpResponse response = null;
			try {
				response = httpClient.execute(request);
			} catch (ConnectTimeoutException | SocketTimeoutException ex) {
				logger.error("Error comunicacions timeout ", ex);
			} catch (IOException ex) {
				logger.error("Error comunicacions ", ex);
			}

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				result = true;
			}

		} catch (Exception e) {
			logger.error("ERROR transferEmbargoToTax para cod cuenta resultado " + cuentaResultadoEmbargo.getCodCuentaResultadoEmbargo(), e);
		}

		return result;
	}
}
