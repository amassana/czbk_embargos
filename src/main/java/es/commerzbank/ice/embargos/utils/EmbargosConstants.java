package es.commerzbank.ice.embargos.utils;

public class EmbargosConstants {

	public static final String AEAT = "AEAT";
	
	public static final String ID_APLICACION_EMBARGOS =  "EM";
	
	/** Tipos de ficheros **/
	public static final String TIPO_FICHERO_PETICIONES = "PET";
	public static final String TIPO_FICHERO_INFORMACION = "INF";
	public static final String TIPO_FICHERO_EMBARGOS = "EMB";
	public static final String TIPO_FICHERO_TRABAS = "TRA";
	public static final String TIPO_FICHERO_LEVANTAMIENTOS = "LEV";
	public static final String TIPO_FICHERO_ERRORES = "ERR";
	public static final String TIPO_FICHERO_FINAL = "FIN";
	public static final String TIPO_FICHERO_APUNTES_CONTABLES = "CON";
	public static final String TIPO_FICHERO_RESULTADO = "RES";
	
//	public static final long COD_TIPO_FICHERO_ALL_TYPES = 0;
//	public static final long COD_TIPO_FICHERO_PETICIONES = 1;
//	public static final long COD_TIPO_FICHERO_INFORMACION = 2;
//	public static final long COD_TIPO_FICHERO_EMBARGOS = 3;
	public static final long COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_AEAT = 1;
	public static final long COD_TIPO_FICHERO_TRABAS_AEAT = 2;
	public static final long COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_AEAT = 3;
	public static final long COD_TIPO_FICHERO_APUNTES_CONTABLES = 4;
	public static final long COD_TIPO_FICHERO_ERRORES_TRABAS_ENVIADAS_AEAT = 5;
	public static final long COD_TIPO_FICHERO_FICHERO_FINAL_AEAT_INTERNAL = 14;
	public static final long COD_TIPO_FICHERO_PETICION_INFORMACION_NORMA63 = 6;
	public static final long COD_TIPO_FICHERO_ENVIO_INFORMACION_NORMA63 = 7;
	public static final long COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_NORMA63 = 8;
	public static final long COD_TIPO_FICHERO_TRABAS_NORMA63 = 9;
	public static final long COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_NORMA63 = 10;
	public static final long COD_TIPO_FICHERO_COM_RESULTADO_FINAL_NORMA63 = 11;
	public static final long COD_TIPO_FICHERO_PETICION_CGPJ = 12;
	public static final long COD_TIPO_FICHERO_RESPUESTA_CGPJ = 13;
	
	/** Formatos de fichero **/
	public static final String FILE_FORMAT_AEAT = "AEAT";
	public static final String FILE_FORMAT_NORMA63 = "NORMA63";
	public static final String FILE_FORMAT_CGPJ = "CGPJ";
	
	/** Prefijos de fichero **/
	public static final String PREFIJO_FICHERO_TGSS = "TGSS";
	
	/** Estados de ControlFichero **/
	public static final long COD_ESTADO_CTRLFICHERO_PETICION_INFORMACION_NORMA63_ERROR = 0;
	//delete estados transitorios public static final long COD_ESTADO_CTRLFICHERO_PETICION_INFORMACION_NORMA63_LOADING = 1;
	public static final long COD_ESTADO_CTRLFICHERO_PETICION_INFORMACION_NORMA63_RECEIVED = 2;
	//delete estados transitorios public static final long COD_ESTADO_CTRLFICHERO_PETICION_INFORMACION_NORMA63_PROCESSING = 3;
	public static final long COD_ESTADO_CTRLFICHERO_PETICION_INFORMACION_NORMA63_PROCESSED = 4;
	
	public static final long COD_ESTADO_CTRLFICHERO_ENVIO_INFORMACION_NORMA63_ERROR = 0;
	//delete estados transitorios public static final long COD_ESTADO_CTRLFICHERO_ENVIO_INFORMACION_NORMA63_GENERATING = 1;
	public static final long COD_ESTADO_CTRLFICHERO_ENVIO_INFORMACION_NORMA63_GENERATED = 2;
	public static final long COD_ESTADO_CTRLFICHERO_ENVIO_INFORMACION_NORMA63_SENT = 3;
	
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_ERROR = 0;
	//delete estados transitorios public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_LOADING = 1;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_RECEIVED = 2;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_PENDING_ACCOUNTING = 3;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_PENDING_ACCOUNTING_RESPONSE = 4;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_PENDING_TO_SEND = 5;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_GENERATED = 6;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_SENT = 7;

	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_ERROR = 0;
	//delete estados transitorios public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_LOADING = 1;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_RECEIVED = 2;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PENDING_ACCOUNTING = 3;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PENDING_ACCOUNTING_RESPONSE = 4;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PENDING_TO_SEND = 5;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_GENERATED = 6;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_SENT = 7;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_CONFIRMED = 8;

	//delete estados transitorios public static final long COD_ESTADO_CTRLFICHERO_FINAL_AEAT_GENERANDO = 1;
	public static final long COD_ESTADO_CTRLFICHERO_FINAL_AEAT_GENERADO = 6;
	public static final long COD_ESTADO_CTRLFICHERO_FINAL_AEAT_PENDIENTE_FICHERO = 11;
	
	public static final long COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_RECEIVED = 2;
	public static final long COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_PENDING_ACCOUNTING = 3;
	public static final long COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_PENDING_ACCOUNTING_RESPONSE = 4;
	public static final long COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_PENDING_TO_SEND = 5;
	public static final long COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_GENERATED = 6;
	public static final long COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_SENT = 7;
	
	public static final long COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_NORMA63_ERROR = 0;
	//delete estados transitorios public static final long COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_NORMA63_GENERATING = 1;
	public static final long COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_NORMA63_GENERATED = 2;
	public static final long COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_NORMA63_SENT = 3;
	
	public static final long COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_AEAT_ERROR = 0;
	//delete estados transitorios public static final long COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_AEAT_GENERATING = 1;
	public static final long COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_AEAT_GENERATED = 2;
	public static final long COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_AEAT_SENT = 3;

	public static final long COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_AEAT_ERROR = 0;
	//delete estados transitorios public static final long COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_LOADING = 1;
	public static final long COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_RECEIVED = 2;
	public static final long COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_PENDING_AUTOMATIC_ACCOUNTING = 5;
	public static final long COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_PENDING_ACCOUNTING_RESPONSE = 3;
	public static final long COD_ESTADO_CTRLFICHERO_LEVANTAMIENTO_ACCOUNTED = 4;

	public static final long COD_ESTADO_CTRLFICHERO_FINAL_ERROR = 0;
	//delete estados transitorios public static final long COD_ESTADO_CTRLFICHERO_FINAL_GENERANDO = 1;
	public static final long COD_ESTADO_CTRLFICHERO_FINAL_GENERADO = 2;
	public static final long COD_ESTADO_CTRLFICHERO_FINAL_ENVIADO = 4;

	public static final long COD_ESTADO_CTRLFICHERO_INITIAL_STATUS_DEFAULT = 1;
	
	public static final long COD_ESTADO_CONTROL_FICHERO_GENERADO_SCHEDULED = 2;


	public static final long COD_ESTADO_CTRLFICHERO_CGPJ_IMPORTADO = 20;

	/** Estados de la Traba **/
	public static final long COD_ESTADO_TRABA_PENDIENTE = 1;
	public static final long COD_ESTADO_TRABA_MODIFICADA = 2;
	public static final long COD_ESTADO_TRABA_CONTABILIZADA = 3;
	public static final long COD_ESTADO_TRABA_MODIFICADA_CON_EXTORNO = 4;
	public static final long COD_ESTADO_TRABA_MODIFICADA_POR_LEVANTAMIENTO_SIN_EXTORNO = 5;
	public static final long COD_ESTADO_TRABA_MODIFICADA_POR_LEVANTAMIENTO_CON_EXTORNO = 6;
	public static final long COD_ESTADO_TRABA_FINALIZADA = 7;
	public static final long COD_ESTADO_TRABA_ESTADO_ERRONEO = 8;
	public static final long COD_ESTADO_TRABA_ENVIADA_A_CONTABILIDAD = 9;
	
	/** Estados del Levantamiento **/
	public static final long COD_ESTADO_LEVANTAMIENTO_PENDIENTE = 1;
	public static final long COD_ESTADO_LEVANTAMIENTO_PENDIENTE_RESPUESTA_CONTABILIZACION = 2;
	public static final long COD_ESTADO_LEVANTAMIENTO_CONTABILIZADO = 3;
	
	/** Estados de la tabla ESTADO_CONTABILIZACION **/
	public static final long COD_ESTADO_CONTABILIZACION_NOTAPPLY = 0;
	public static final long COD_ESTADO_CONTABILIZACION_PENDIENTE_ENVIO_A_CONTABILIDAD = 1;
	public static final long COD_ESTADO_CONTABILIZACION_ENVIADA_A_CONTABILIDAD = 2;
	public static final long COD_ESTADO_CONTABILIZACION_CONTABILIZADA  = 3;
	public static final long COD_ESTADO_CONTABILIZACION_CONTABILIZACION_AUTOMATICA_ABORTADA = 4;
	
	/** Stream names del cuaderno 63 **/
	public static final String STREAM_NAME_CUADERNO63_FASE1 = "fase1";
	public static final String STREAM_NAME_CUADERNO63_FASE2 = "fase2";
	public static final String STREAM_NAME_CUADERNO63_FASE3 = "fase3";
	public static final String STREAM_NAME_CUADERNO63_FASE4 = "fase4";
	public static final String STREAM_NAME_CUADERNO63_FASE5 = "fase5";
	public static final String STREAM_NAME_CUADERNO63_FASE6 = "fase6";
	
	/** Record names del cuaderno 63 **/
	public static final String RECORD_NAME_CABECERAEMISOR = "cabeceraEmisor";
	public static final String RECORD_NAME_SOLICITUDINFORMACION = "solicitudInformacion";
	public static final String RECORD_NAME_FINFICHERO = "finFichero";
	public static final String RECORD_NAME_RESPUESTASOLICITUDINFORMACION = "respuestaSolicitudInformacion";
	public static final String RECORD_NAME_ORDENEJECUCIONEMBARGO = "ordenEjecucionEmbargo";
	public static final String RECORD_NAME_ORDENEJECUCIONEMBARGOCOMPLEMENTARIO = "ordenEjecucionEmbargoComplementario";
	public static final String RECORD_NAME_COMUNICACIONRESULTADORETENCION = "comunicacionResultadoRetencion";
	public static final String RECORD_NAME_ORDENLEVANTAMIENTORETENCION = "ordenLevantamientoRetencion";
	public static final String RECORD_NAME_RESULTADOFINALEMBARGO = "resultadoFinalEmbargo";
	
	/** Stream names de AEAT **/
	public static final String STREAM_NAME_AEAT_DILIGENCIAS = "diligenciasFase3";
	public static final String STREAM_NAME_AEAT_TRABAS = "trabasFase4";
	public static final String STREAM_NAME_AEAT_RESULTADOVALIDACIONTRABAS = "resultadoValidacionTrabasFase4";
	public static final String STREAM_NAME_AEAT_LEVANTAMIENTOTRABAS = "levantamientoTrabas";
	
	/** Record names de AEAT **/
	public static final String RECORD_NAME_AEAT_ENTIDADTRANSMISORA = "entidadTransmisora";
	public static final String RECORD_NAME_AEAT_ENTIDADCREDITO = "entidadCredito";
	public static final String RECORD_NAME_AEAT_DILIGENCIA = "diligencia";
	public static final String RECORD_NAME_AEAT_FINENTIDADCREDITO = "finEntidadCredito";
	public static final String RECORD_NAME_AEAT_FINENTIDADTRANSMISORA = "finEntidadTransmisora";
	public static final String RECORD_NAME_AEAT_REGISTROCONTROLENTIDADTRANSMISORA = "registroControlEntidadTransmisora";
	public static final String RECORD_NAME_AEAT_TRABA = "traba";
	public static final String RECORD_NAME_AEAT_LEVANTAMIENTO = "levantamiento";
	public static final String RECORD_NAME_AEAT_ERRORESTRABA = "erroresTraba";
	
	public static final String SEPARADOR_PUNTO = ".";
	public static final String SEPARADOR_GUION_BAJO = "_";
	public static final String SEPARADOR_ESPACIO = " ";
	
	public static final String USER_SYSTEM = "system";
	public static final String USER_AUTOMATICO = "AUTOMATICO";
	public static final String USER_MANUAL = "MANUAL";
	
	public static final String CONTROL_FICHERO_DESCRIPCION_DEFAULT = "AUTOMATICO";
	
	public static final String COD_ESTADO_PRIMARIO_PETICION_ENVIANDO_PETICION_UNICA = "1000";
	public static final String COD_ESTADO_PRIMARIO_RESPUESTA_PROCESADA_TOTALMENTE = "0000";
	
	public static final String IND_FLAG_NO = "N";
	public static final String IND_FLAG_YES = "Y";
	public static final String IND_FLAG_SI = "S";
	
	public static final String ISO_MONEDA_EUR = "EUR";
	
	public static final String BANK_ACCOUNT_STATUS_ACTIVE = "ACTIVE";
	public static final String BANK_ACCOUNT_STATUS_BLOCKED = "BLOCKED";
	public static final String BANK_ACCOUNT_STATUS_CANCELLED = "CANCELLED";
	public static final String BANK_ACCOUNT_STATUS_NOTFOUND = "NOTFOUND";
	
	public static final long COD_CUENTA_INMOVILIZACION_DEFAULT = 1;
	
	public static final String PERSON_TYPE_ID_FISICA = "F";
	public static final String PERSON_TYPE_ID_JURIDICA = "J";
	
	public static final String CODIGO_NRBE_COMMERZBANK = "0159";
	
	public static final int COD_FASE_1 = 1;
	public static final int COD_FASE_2 = 2;
	public static final int COD_FASE_3 = 3;
	public static final int COD_FASE_4 = 4;
	public static final int COD_FASE_5 = 5;
	public static final int COD_FASE_6 = 6;
	
	public static final String F1 = "F1";
	public static final String F2 = "F2";
	public static final String F3 = "F3";
	public static final String F4 = "F4";
	public static final String F5 = "F5";
	public static final String F6 = "F6";
	
	/** Codigos de registro de los ficheros **/
	public static final int CODIGO_REGISTRO_CUADERNO63_COMUNICACION_RESULTADO_RETENCION_FASE4 = 6;	
	public static final String CODIGO_REGISTRO_AEAT_TRABA_FASE4 = "2";
	
	/** Codigos actuacion de CuentaTrabaActuacion **/
	public static final String CODIGO_ACTUACION_SIN_ACTUACION_AEAT = "30";
	public static final String CODIGO_ACTUACION_SIN_ACTUACION_NORMA63 = "60";
	public static final String CODIGO_ACTUACION_CUENTA_INEXISTENTE_O_CANCELADA_AEAT = "35";
	public static final String CODIGO_ACTUACION_CUENTA_INEXISTENTE_O_CANCELADA_NORMA63 = "65";
	
	/** Entradas de la tabla PARAMETROS de Comunes **/
	public static final String PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_CUENTA = "embargos.cuentaRecaudacion.cuenta";
	public static final String PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_OFICINA = "embargos.cuentaRecaudacion.oficina";
	public static final String PARAMETRO_EMBARGOS_CUENTA_INTERCAMBIO_DIVISAS = "embargos.cuentaIntercambioDivisas";
	public static final String PARAMETRO_EMBARGOS_CONTABILIZACION_FASE3_CALLBACK = "embargos.contabilizacion.fase3.callback";
	public static final String PARAMETRO_EMBARGOS_CONTABILIZACION_FASE5_CALLBACK = "embargos.contabilizacion.fase5.callback";
	public static final String PARAMETRO_EMBARGOS_CONTABILIZACION_FASE6_CALLBACK = "embargos.contabilizacion.fase6.callback";
	public static final String PARAMETRO_EMBARGOS_CONTABILIZACION_SKIP = "embargos.contabilizacion.skip";
	public static final String PARAMETRO_EMBARGOS_LEVANTAMIENTO_IMPORTE_MAXIMO_AUTOMATICO_DIVISA = "embargos.levantamiento.importeMaximoAutomaticoDivisa";
	
	//Tabla Apoderados
	public static final String NAME_APODERADO = "name";
	public static final String POSITION_APODERADO = "position";
	
	//Levantamiento
	public static final String LIFTING = "lifting";
	public static final Object BANK_ACCOUNT_LIFTING_LIST = "bankAccountLiftingList";

	//Contabilidad
	public static final String LITERAL_EMBARG_IBS_REFERENCE2 = "Embarg";
	public static final String LITERAL_LEVANT_IBS_REFERENCE2 = "Levant";
	public static final String IBS_REFERENCE_1 = "IBS_REFERENCE_1";
	public static final String IBS_REFERENCE_2 = "IBS_REFERENCE_2";
	public static final String LITERAL_DETAILPAYMENT_FASE6_EMB_TRANSFERRED_TO = "EMB Transferencia a ";
	
	//Emails:
	public static final String EMAIL_DEFAULT_FOOTER_TEXT = "Aplicaci??n de Embargos";

	public static final long ESTADO_FINAL_SIN_ORDEN_LEVANTAMIENTO = 0;
	public static final long ESTADO_FINAL_LEVANTAMIENTO_TOTAL = 1;
	public static final long ESTADO_FINAL_LEVANTAMIENTO_PARCIAL = 2;
	public static final long ESTADO_FINAL_LEVANTAMIENTO_RECHAZADO = 3;
	public static final long ESTADO_FINAL_OTROS = 4;

	public static final String PROFILE_LOCAL = "LOCAL";
	public static final String PROFILE_YAMLDB = "YAMLDB";

	//Definicion de Nombres de la tabla PARAMETROS:
	//public static final String PARAMETRO_DWH_ENDPOINT_CLIENTACCOUNT = "datawarehouse.endpoint.client-account";
	public static final String PARAMETRO_TSP_JASPER_TEMP = "tsp.jasper.temp";

	public static final String PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_INCOMING = "embargos.files.path.norma63.incoming";
	public static final String PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_PROCESSING = "embargos.files.path.norma63.processing";
	public static final String PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_PROCESSED = "embargos.files.path.norma63.processed";
	public static final String PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_GENERATED = "embargos.files.path.norma63.generated";
	public static final String PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_OUTBOX = "embargos.files.path.norma63.outbox";
	public static final String PARAMETRO_EMBARGOS_FILES_PATH_NORMA63_ERROR = "embargos.files.path.norma63.error";

	public static final String PARAMETRO_EMBARGOS_FILES_PATH_AEAT_INCOMING = "embargos.files.path.aeat.incoming";
	public static final String PARAMETRO_EMBARGOS_FILES_PATH_AEAT_PROCESSED = "embargos.files.path.aeat.processed";
	public static final String PARAMETRO_EMBARGOS_FILES_PATH_AEAT_PROCESSING = "embargos.files.path.aeat.processing";
	public static final String PARAMETRO_EMBARGOS_FILES_PATH_AEAT_GENERATED = "embargos.files.path.aeat.generated";
	public static final String PARAMETRO_EMBARGOS_FILES_PATH_AEAT_OUTBOX = "embargos.files.path.aeat.outbox";
	public static final String PARAMETRO_EMBARGOS_FILES_PATH_AEAT_ERROR = "embargos.files.path.aeat.error";

	public static final String PARAMETRO_EMBARGOS_FILES_ENCODING_NORMA63 = "embargos.files.encoding.norma63";
	public static final String PARAMETRO_EMBARGOS_FILES_ENCODING_AEAT = "embargos.files.encoding.aeat";
	public static final String PARAMETRO_EMBARGOS_EMAIL_TO = "embargos.email.to";
	public static final String PARAMETRO_EMBARGOS_EMAIL_FROM = "embargos.email.from";
	public static final String PARAMETRO_EMBARGOS_FILES_STABLE_TIME = "embargos.files.stableTime";
	public static final String PARAMETRO_EMBARGOS_FILES_MAX_PROCESSING_TIME = "embargos.files.maxProcessingTime";

	public static final String PARAMETRO_EMBARGOS_FILES_ARCHIVAL_FOLDER_DATE_PATTERN = "embargos.files.archivalFolderDatePattern";

	public static final String PARAMETRO_EMBARGOS_DIAS_EMAIL_NOLECTURA_FICHEROS = "embargos.dias.email.nolectura.ficheros";
	
	//Definicion del valor por defecto de los PARAMETROS:
	public static final String PARAMETRO_DWH_ENDPOINT_CLIENTACCOUNT_DEFAULT_VALUE = "/datawarehouse/client-account";

	//JOB de embargos a impuestos
	public static final String CRON_JOB_EMBARGOS_TO_TAX = "embargos.job.cron.embargo.to.tax";
	public static final String ENDPOINT_EMBARGOS_TO_TAX = "embargos.endpoint.to.impuesto";
	public static final String JOB_EMBARGOS_TO_TAX_USER = "embargos.job.embargo.to.tax.user";
	
	//JOB tareas pendientes
	public static final String CRON_JOB_EMBARGOS_PENDING_TASK_DATE = "embargos.job.cron.pending.task.date";
	public static final String PARAMETRO_EMBARGOS_DIAS_TAREAS_PENDIENTES = "embargos.dias.cron.pending.task";

	public static final Object IND_AEAT_ENTIDAD = "AEAT";
	
	public static final String TAREA_PENDIENTE = "P";

	//Auditoria Tablas
	public static final String TABLA_APODERADOS = "APODERADOS";
	public static final String TABLA_ENTIDADES_COMUNICADORAS = "ENTIDADES_COMUNICADORAS";
	public static final String TABLA_ENTIDADES_ORDENANTES = "ENTIDADES_ORDENANTES";
	public static final String TABLA_CONTROL_FICHERO = "CONTROL_FICHERO";
	public static final String TABLA_PETICION_INFORMACION = "PETICION_INFORMACION";
	public static final String TABLA_LEVANTAMIENTO_TRABA = "LEVANTAMIENTO_TRABA";
	public static final String TABLA_EMBARGO = "EMBARGO";
	public static final String TABLA_CUENTA_TRABA = "CUENTA_TRABA";
	public static final String TABLA_CUENTA_LEVANTAMIENTO = "CUENTA_LEVANTAMIENTO";
	public static final String TABLA_RESULTADO_EMBARGO = "RESULTADO_EMBARGO";
	public static final String TABLA_CUENTA_RESULTADO_EMBARGO = "CUENTA_RESULTADO_EMBARGO";

	/****
	 ACCOUNTING
	 */

	public static final String ACCOUNTING_EMBARGOS_PATTERN = "'CONTABLE_EMBARGOS_'yyyyMMdd_HHmmss";
	public static final String ACCOUNTING_EMBARGOS_EXTENSION = "CON";
	public static final String ACCOUNTING_EMBARGO_F6 = "FIN_CICLO";
	public static final String ACCOUNTING_EMBARGOS_TRABAS = "TRABAS";
	public static final String ACCOUNTING_EMBARGOS_LEVANTAMIENTOS = "LEVANTAMIENTOS";
	public static final String ACCOUNTING_EMBARGOS_EXTORNO = "EXTORNO";
	public static final String ACCOUNTING_EMBARGOS_CGPJ = "CGPJ";
	
	public static final String APUNTES_CONTABLES_TIPO_TRABA = "TRABA";
	public static final String APUNTES_CONTABLES_TIPO_LEVANTAMIENTO = "LEVANTAMIENTO";
	public static final String APUNTES_CONTABLES_TIPO_EXTORNO = "EXTORNO";
	public static final String APUNTES_CONTABLES_TIPO_FINAL = "FINAL";
	
	public static final String EXTERNAL_ID_F6_N63 = "SEIZUREN63_";
	public static final String EXTERNAL_ID_F6_AEAT = "SEIZUREAEAT_";

	public static final String SUCURSAL_CREACION_IMPUESTOS = "1";
	
	public static final String DETAIL_PAYMENT_UNDO_ACCOUNTING = "Extorno";
	public static final String DETAIL_PAYMENT_SEIZE = "Traba";
	public static final String DETAIL_PAYMENT_LIFT = "Levantamiento";

	public static final Long CGPJ_ESTADO_INTERNO_INICIAL = 1L;
	public static final Long CGPJ_ESTADO_INTERNO_PROCESADO = 2L;
	public static final Long CGPJ_ESTADO_INTERNO_SOLICITUD_PENDIENTE_ENVIAR = 3L;
	public static final Long CGPJ_ESTADO_INTERNO_SOLICITUD_ENVIADA = 4L;
	public static final Long CGPJ_ESTADO_INTERNO_SOLICITUD_ERROR_DE_TRANSMISION = 5L;
	public static final Long CGPJ_ESTADO_INTERNO_SOLICITUD_ERROR_DE_PROCESO = 6L;

	public static final Long CGPJ_ESTADO_INTERNO_TRABA_PROCESADA = 3L;

	public static final Long CGPJ_ESTADO_INTERNO_LEVANTAMIENTO_INICIAL = 1L;
	public static final Long CGPJ_ESTADO_INTERNO_LEVANTAMIENTO_PENDIENTE_CONTABILIZAR = 2L;
	public static final Long CGPJ_ESTADO_INTERNO_LEVANTAMIENTO_PROCESADO = 3L;

	public static final String PARAMETRO_CGPJ_EMAIL_PENDIENTE_HORAS = "embargos.cgpj.email.pendienteHoras";

	public static final String CGPJ_MOTIVO_TRABA_SIN_ACTUACION = "00";
	public static final String CGPJ_MOTIVO_TRABA_TOTAL = "2000";
	public static final String CGPJ_MOTIVO_TRABA_PARCIAL = "2001";
	public static final String AEAT_TRABA = "31";
	public static final String N63_TRABA = "61";

    public static final int LEVANTAMIENTO_CGPJ_ESTADO_INT_PDTE_CONTA = 2;
	public static final int LEVANTAMIENTO_CGPJ_ESTADO_RESP_LEV = 2003;
}
