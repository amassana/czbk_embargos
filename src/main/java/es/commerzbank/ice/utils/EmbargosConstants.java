package es.commerzbank.ice.utils;

public class EmbargosConstants {

	public static final String AEAT = "AEAT";
	
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
	public static final String FILE_FORMAT_CUADERNO63 = "CUADERNO63";
	public static final String FILE_FORMAT_CGPJ = "CGPJ";
	
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
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_PROCESSING = 3;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_NORMA63_PROCESSED = 4;

	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_ERROR = 0;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_LOADING = 1;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_RECEIVED = 2;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PROCESSING = 3;
	public static final long COD_ESTADO_CTRLFICHERO_DILIGENCIAS_EMBARGO_AEAT_PROCESSED = 4;
	
	
	public static final long COD_ESTADO_CTRLFICHERO_INITIAL_STATUS_DEFAULT = 1;

	/** Estados de la Traba **/
	public static final long COD_ESTADO_TRABA_INICIAL = 1;
	public static final long COD_ESTADO_TRABA_MODIFICADA = 2;
	public static final long COD_ESTADO_TRABA_CONTABILIZADA = 3;
	public static final long COD_ESTADO_TRABA_MODIFICADA_CON_EXTORNO = 4;
	public static final long COD_ESTADO_TRABA_MODIFICADA_POR_LEVANTAMIENTO_SIN_EXTORNO = 5;
	public static final long COD_ESTADO_TRABA_MODIFICADA_POR_LEVANTAMIENTO_CON_EXTORNO = 6;
	public static final long COD_ESTADO_TRABA_PASADA_A_IMPUESTO = 7;
	public static final long COD_ESTADO_TRABA_ESTADO_ERRONEO = 8;
	
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
	public static final String STREAM_NAME_AEAT_DILIGENCIAS = "diligencias";
	public static final String STREAM_NAME_AEAT_TRABAS = "trabas";
	public static final String STREAM_NAME_AEAT_RESULTADOVALIDACIONTRABAS = "resultadoValidacionTrabas";
	public static final String STREAM_NAME_AEAT_LEVANTAMIENTOTRABAS = "levantamientoTrabas";
	
	/** Record names de AEAT **/
	public static final String RECORD_NAME_AEAT_ENTIDADTRANSMISORA = "entidadTransmisora";
	public static final String RECORD_NAME_AEAT_ENTIDADCREDITO = "entidadCredito";
	public static final String RECORD_NAME_AEAT_DILIGENCIA = "diligencia";
	public static final String RECORD_NAME_AEAT_FINENTIDADCREDITO = "finEntidadCredito";
	public static final String RECORD_NAME_AEAT_FINENTIDADTRANSMISORA = "finEntidadTransmisora";
	public static final String RECORD_NAME_AEAT_REGISTROCONTROLENTIDADTRANSMISORA = "registroControlEntidadTransmisora";
	public static final String RECORD_NAME_AEAT_TRABA = "traba";
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
	
	public static final long COD_CUENTA_INMOVILIZACION_DEFAULT = 1;
	
	public static final String PERSON_TYPE_ID_FISICA = "F";
	public static final String PERSON_TYPE_ID_JURIDICA = "J";
	
	public static final String CODIGO_ENTIDAD_TRANSMISORA_COMMERZBANK = "0159";
	
	//Tabla Apoderados
	public static final String NAME_APODERADO = "name";
	public static final String POSITION_APODERADO = "position";
	
}
