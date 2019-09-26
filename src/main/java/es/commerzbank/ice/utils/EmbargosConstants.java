package es.commerzbank.ice.utils;

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
	
//	public static final long COD_TIPO_FICHERO_ALL_TYPES = 0;
//	public static final long COD_TIPO_FICHERO_PETICIONES = 1;
//	public static final long COD_TIPO_FICHERO_INFORMACION = 2;
//	public static final long COD_TIPO_FICHERO_EMBARGOS = 3;
	public static final long COD_TIPO_FICHERO_DILIGENCIAS_EMBARGO_AEAT = 1;
	public static final long COD_TIPO_FICHERO_TRABAS_AEAT = 2;
	public static final long COD_TIPO_FICHERO_LEVANTAMIENTO_TRABAS_AEAT = 3;
	public static final long COD_TIPO_FICHERO_APUNTES_CONTABLES = 4;
	public static final long COD_TIPO_FICHERO_ERRORES_TRABAS_ENVIADAS_AEAT = 5;
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
	public static final long COD_ESTADO_CTRLFICHERO_PETICION_INFORMACION_NORMA63_LOADING = 1;
	public static final long COD_ESTADO_CTRLFICHERO_PETICION_INFORMACION_NORMA63_RECEIVED = 2;
	public static final long COD_ESTADO_CTRLFICHERO_PETICION_INFORMACION_NORMA63_PROCESSING = 3;
	public static final long COD_ESTADO_CTRLFICHERO_PETICION_INFORMACION_NORMA63_PROCESSED = 4;
	
	public static final long COD_ESTADO_CTRLFICHERO_ENVIO_INFORMACION_NORMA63_ERROR = 0;
	public static final long COD_ESTADO_CTRLFICHERO_ENVIO_INFORMACION_NORMA63_GENERATING = 1;
	public static final long COD_ESTADO_CTRLFICHERO_ENVIO_INFORMACION_NORMA63_GENERATED = 2;
	public static final long COD_ESTADO_CTRLFICHERO_ENVIO_INFORMACION_NORMA63_SENT = 3;
	
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_ERROR = 0;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_LOADING = 1;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_RECEIVED = 2;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_PENDING_ACCOUNTING = 3;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_PENDING_ACCOUNTING_RESPONSE = 4;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_PENDING_TO_SEND = 5;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_GENERATED = 6;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_SENT = 7;

	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_ERROR = 0;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_LOADING = 1;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_RECEIVED = 2;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PENDING_ACCOUNTING = 3;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PENDING_ACCOUNTING_RESPONSE = 4;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PENDING_TO_SEND = 5;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_GENERATED = 6;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_SENT = 7;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_CONFIRMED = 8;
	
	public static final long COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_RECEIVED = 2;
	public static final long COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_PENDING_ACCOUNTING = 3;
	public static final long COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_PENDING_ACCOUNTING_RESPONSE = 4;
	public static final long COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_PENDING_TO_SEND = 5;
	public static final long COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_GENERATED = 6;
	public static final long COD_ESTADO_CTRLFICHERO_PETICION_CGPJ_SENT = 7;
	
	public static final long COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_NORMA63_ERROR = 0;
	public static final long COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_NORMA63_GENERATING = 1;
	public static final long COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_NORMA63_GENERATED = 2;
	public static final long COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_NORMA63_SENT = 3;
	
	public static final long COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_AEAT_ERROR = 0;
	public static final long COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_AEAT_GENERATING = 1;
	public static final long COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_AEAT_GENERATED = 2;
	public static final long COD_ESTADO_CTRLFICHERO_ENVIO_TRABAS_AEAT_SENT = 3;
	
	public static final long COD_ESTADO_CTRLFICHERO_INITIAL_STATUS_DEFAULT = 1;

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
	public static final long COD_ESTADO_LEVANTAMIENTO_PENDIENTE_CONTABILIZACION = 2;
	public static final long COD_ESTADO_LEVANTAMIENTO_CONTABILIZADO = 3;
	
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
	
	public static final String CONTROL_FICHERO_DESCRIPCION_DEFAULT = "AUTOMATICO";
	
	public static final String COD_ESTADO_PRIMARIO_PETICION_ENVIANDO_PETICION_UNICA = "1000";
	
	public static final String IND_FLAG_NO = "N";
	public static final String IND_FLAG_YES = "Y";
	public static final String IND_FLAG_SI = "S";
	
	public static final String ISO_MONEDA_EUR = "EUR";
	
	public static final String BANK_ACCOUNT_STATUS_ACTIVE = "ACTIVE";
	public static final String BANK_ACCOUNT_STATUS_BLOQUED = "BLOQUED";
	public static final String BANK_ACCOUNT_STATUS_CANCELLED = "CANCELLED";
	public static final String BANK_ACCOUNT_STATUS_NOTFOUND = "CANCELLED";
	
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
	public static final String CODIGO_ACTUACION_CUENTA_INEXISTENTE_O_CANCELADA_AEAT = "35";
	public static final String CODIGO_ACTUACION_CUENTA_INEXISTENTE_O_CANCELADA_NORMA63 = "65";
	
	/** Entradas de la tabla PARAMETROS de Comunes **/
	public static final String PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_CUENTA = "embargos.cuentaRecaudacion.cuenta";
	public static final String PARAMETRO_EMBARGOS_CUENTA_RECAUDACION_OFICINA = "embargos.cuentaRecaudacion.oficina";
	public static final String PARAMETRO_EMBARGOS_CONTABILIZACION_FASE3_CALLBACK = "embargos.contabilizacion.fase3.callback";
	public static final String PARAMETRO_EMBARGOS_CONTABILIZACION_FASE5_CALLBACK = "embargos.contabilizacion.fase5.callback";
	
	//Tabla Apoderados
	public static final String NAME_APODERADO = "name";
	public static final String POSITION_APODERADO = "position";
	
	//Levantamiento
	public static final String LIFTING = "lifting";
	public static final Object BANK_ACCOUNT_LIFTING_LIST = "bankAccountLiftingList";

	//Contabilidad
	public static final String COD_ESTADO_APUNTE_CONTABLE_PENDIENTE_ENVIO = "P";
	public static final String LITERAL_EMBARG_IBS_REFERENCE2 = "Embarg";
	public static final String LITERAL_LEVANT_IBS_REFERENCE2 = "Levant";
	
	//Emails:
	public static final String EMAIL_DEFAULT_FOOTER_TEXT = "Aplicación de Embargos";

}
