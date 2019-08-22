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
	
	public static final long COD_TIPO_FICHERO_ALL_TYPES = 0;
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
		
	/** Stream names del cuaderno 63 **/
	public static final String STREAM_NAME_FASE1 = "fase1";
	public static final String STREAM_NAME_FASE2 = "fase2";
	public static final String STREAM_NAME_FASE3 = "fase3";
	public static final String STREAM_NAME_FASE4 = "fase4";
	public static final String STREAM_NAME_FASE5 = "fase5";
	public static final String STREAM_NAME_FASE6 = "fase6";
	
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
	public static final String STREAM_NAME_DILIGENCIAS = "diligencias";
	public static final String STREAM_NAME_TRABAS = "trabas";
	public static final String STREAM_NAME_RESULTADOVALIDACIONTRABAS = "resultadoValidacionTrabas";
	public static final String STREAM_NAME_LEVANTAMIENTOTRABAS = "levantamientoTrabas";
	
	/** Record names de AEAT **/
	public static final String RECORD_NAME_ENTIDADTRANSMISORA = "entidadTransmisora";
	public static final String RECORD_NAME_ENTIDADCREDITO = "entidadCredito";
	public static final String RECORD_NAME_DILIGENCIA = "diligencia";
	public static final String RECORD_NAME_FINENTIDADCREDITO = "finEntidadCredito";
	public static final String RECORD_NAME_FINENTIDADTRANSMISORA = "finEntidadTransmisora";
	public static final String RECORD_NAME_REGISTROCONTROLENTIDADTRANSMISORA = "registroControlEntidadTransmisora";
	public static final String RECORD_NAME_TRABA = "traba";
	public static final String RECORD_NAME_ERRORESTRABA = "erroresTraba";
	
	public static final String SEPARADOR_PUNTO = ".";
	public static final String SEPARADOR_GUION_BAJO = "_";	
	
	public static final String SYSTEM_USER = "system";
	
	public static final String COD_ESTADO_PRIMARIO_PETICION_ENVIANDO_PETICION_UNICA = "1000";
	
	public static final String IND_FLAG_NO = "N";
	public static final String IND_FLAG_YES = "Y";
	
	public static final String BANK_ACCOUNT_STATUS_NORMAL = "NORMAL";
	public static final String BANK_ACCOUNT_STATUS_BLOQUED = "BLOQUED";
	public static final String BANK_ACCOUNT_STATUS_CANCELLED = "CANCELLED";
}