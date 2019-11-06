package es.commerzbank.ice.embargos.domain.mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import es.commerzbank.ice.comun.lib.typeutils.DateUtils;
import es.commerzbank.ice.comun.lib.util.BankAccountUtils;
import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.datawarehouse.domain.dto.AccountDTO;
import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.domain.dto.ClientDataDTO;
import es.commerzbank.ice.embargos.domain.entity.CuentaEmbargo;
import es.commerzbank.ice.embargos.domain.entity.CuentaLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.CuentaTrabaActuacion;
import es.commerzbank.ice.embargos.domain.entity.CuentasInmovilizacion;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import es.commerzbank.ice.embargos.domain.entity.EntidadesOrdenante;
import es.commerzbank.ice.embargos.domain.entity.EstadoLevantamiento;
import es.commerzbank.ice.embargos.domain.entity.EstadoTraba;
import es.commerzbank.ice.embargos.domain.entity.LevantamientoTraba;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.DiligenciaFase3;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.EntidadCreditoFase3;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.EntidadTransmisoraFase3;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.FinEntidadCreditoFase3;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.FinEntidadTransmisoraFase3;
import es.commerzbank.ice.embargos.formats.aeat.levantamientotrabas.Levantamiento;
import es.commerzbank.ice.embargos.formats.aeat.trabas.EntidadCreditoFase4;
import es.commerzbank.ice.embargos.formats.aeat.trabas.EntidadTransmisoraFase4;
import es.commerzbank.ice.embargos.formats.aeat.trabas.FinEntidadCreditoFase4;
import es.commerzbank.ice.embargos.formats.aeat.trabas.FinEntidadTransmisoraFase4;
import es.commerzbank.ice.embargos.formats.aeat.trabas.TrabaFase4;
import es.commerzbank.ice.utils.EmbargosConstants;
import es.commerzbank.ice.utils.ICEDateUtils;

@Mapper(componentModel="spring")
public abstract class AEATMapper {
	LevantamientoHelperMapper levantamientoHelperMapper = new LevantamientoHelperMapper();
	
	@Mappings({
		@Mapping(source = "diligenciaFase3.nifDeudor", target = "datosCliente.nif"),
		@Mapping(source = "diligenciaFase3.nombreDeudor", target = "nombre"),
		@Mapping(source = "diligenciaFase3.siglasViaPublica", target = "siglasVp"),
		@Mapping(source = "diligenciaFase3.nombreViaPublica", target = "nombreVp"),
		@Mapping(source = "diligenciaFase3.numeroPortal", target = "numero"),
		@Mapping(source = "diligenciaFase3.letraPortal", target = "letra"),
		@Mapping(source = "diligenciaFase3.escalera", target = "escalera"),
		@Mapping(source = "diligenciaFase3.piso", target = "piso"),
		@Mapping(source = "diligenciaFase3.puerta", target = "puerta"),
		@Mapping(source = "diligenciaFase3.nombreMunicipio", target = "municipio"),
		@Mapping(source = "diligenciaFase3.codigoPostal", target = "codigoPostal"),
		@Mapping(source = "diligenciaFase3.numeroDiligenciaEmbargo", target = "numeroEmbargo"),
		@Mapping(source = "diligenciaFase3.importeTotalAEmbargar", target = "importe"),
		@Mapping(source = "codControlFicheroEmbargo", target = "controlFichero.codControlFichero"),
		@Mapping(source = "entidadOrdenante", target = "entidadesOrdenante")
	})
	public abstract Embargo generateEmbargo(DiligenciaFase3 diligenciaFase3, Long codControlFicheroEmbargo, EntidadesOrdenante entidadOrdenante, 
			EntidadCreditoFase3 entidadCreditoFase3, Map<String, AccountDTO> customerAccountsMap) throws ICEException;
	
	@AfterMapping
	public void generateEmbargoAfterMapping(@MappingTarget Embargo embargo, DiligenciaFase3 diligenciaFase3, EntidadesOrdenante entidadOrdenante, 
			EntidadCreditoFase3 entidadCreditoFase3, Map<String, AccountDTO> customerAccountsMap) throws ICEException {
		
		List<CuentaEmbargo> cuentaEmbargosList = new ArrayList<>();
		
		BigDecimal numeroOrden = new BigDecimal(1);
		
		BigDecimal fechaUltmaModif = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
		String usuarioModif = EmbargosConstants.USER_AUTOMATICO;
		
		//seteo de datregcomdet:
		embargo.setDatregcomdet(entidadCreditoFase3!=null ? entidadCreditoFase3.getMensajeInformativoParaDeudor() : null);
				
		//Determinacion de la fecha limite de la traba:
		Date fechaGeneracionDiligencia = diligenciaFase3.getFechaGeneracionDiligencia();
		BigDecimal fechaLimiteTraba = determineFechaLimiteTraba(entidadOrdenante, fechaGeneracionDiligencia);
		embargo.setFechaLimiteTraba(fechaLimiteTraba);
		
		//Fecha de generacion:
		embargo.setFechaGeneracion(ICEDateUtils.dateToBigDecimal(fechaGeneracionDiligencia, ICEDateUtils.FORMAT_yyyyMMdd));
		
		
		//SETEO DE CUENTAS:
		//Datos cuenta cliente 1:
		CuentaEmbargo cuentaEmbargo = null;
		
		if (diligenciaFase3.getCodigoCuentaCliente1()!=null && !diligenciaFase3.getCodigoCuentaCliente1().isEmpty()) {
			
			String codigoCuentaCliente1 = diligenciaFase3.getCodigoCuentaCliente1();
			
			String ibanFromCCC1 = BankAccountUtils.convertToIBAN(codigoCuentaCliente1);
			
			AccountDTO accountDTO = customerAccountsMap.get(ibanFromCCC1);
			
			cuentaEmbargo = setCuentaEmbargoFromAccountDTO(accountDTO, ibanFromCCC1, embargo, numeroOrden, fechaUltmaModif, usuarioModif);
						
			cuentaEmbargosList.add(cuentaEmbargo);

			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
			
		}
		
		//Datos cuenta cliente 2:		
		if (diligenciaFase3.getCodigoCuentaCliente2()!=null && !diligenciaFase3.getCodigoCuentaCliente2().isEmpty()) {
			
			String codigoCuentaCliente2 = diligenciaFase3.getCodigoCuentaCliente2();
			
			String ibanFromCCC2 = BankAccountUtils.convertToIBAN(codigoCuentaCliente2);
			
			AccountDTO accountDTO = customerAccountsMap.get(ibanFromCCC2);
			
			cuentaEmbargo = setCuentaEmbargoFromAccountDTO(accountDTO, ibanFromCCC2, embargo, numeroOrden, fechaUltmaModif, usuarioModif);
			
			cuentaEmbargosList.add(cuentaEmbargo);

			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
		}
		
		//Datos cuenta cliente 3:	
		if (diligenciaFase3.getCodigoCuentaCliente3()!=null && !diligenciaFase3.getCodigoCuentaCliente3().isEmpty()) {
			
			String codigoCuentaCliente3 = diligenciaFase3.getCodigoCuentaCliente3();
			
			String ibanFromCCC3 = BankAccountUtils.convertToIBAN(codigoCuentaCliente3);
			
			AccountDTO accountDTO = customerAccountsMap.get(ibanFromCCC3);
			
			cuentaEmbargo = setCuentaEmbargoFromAccountDTO(accountDTO, ibanFromCCC3, embargo, numeroOrden, fechaUltmaModif, usuarioModif);
			
			cuentaEmbargosList.add(cuentaEmbargo);

		}
		
		embargo.setCuentaEmbargos(cuentaEmbargosList);
		
        //Usuario y fecha ultima modificacion:
		embargo.setUsuarioUltModificacion(usuarioModif);
		embargo.setFUltimaModificacion(fechaUltmaModif);
		
	}

	private CuentaEmbargo setCuentaEmbargoFromAccountDTO(AccountDTO accountDTO, String ibanCalculatedFromDiligenciaFase3, Embargo embargo, 
			BigDecimal numeroOrden,	BigDecimal fechaUltmaModif, String usuarioModif) {
		
		CuentaEmbargo cuentaEmbargo = new CuentaEmbargo();
		
		String cuenta = null;
		String iban = null;
		
		if (accountDTO!=null) {
			//Cuenta encontrada en DWH -> seteo de datos de DWH a partir del accountDTO:
			cuenta = accountDTO.getAccountNum();
			iban = accountDTO.getIban();
		} else {
			//Cuenta no encontrada en DWH -> setear el iban calculado a partir 
			//del codigo cuenta cliente que viene del fichero de diligencias de embargo:
			cuenta = null;
			iban = ibanCalculatedFromDiligenciaFase3;
		}

		cuentaEmbargo.setEmbargo(embargo);
		cuentaEmbargo.setCuenta(cuenta);
		cuentaEmbargo.setImporte(BigDecimal.valueOf(0));
		cuentaEmbargo.setIban(iban);
		//Clave de seguridad del IBAN no viene en el fichero de AEAT (no tiene fase 1 y 2) -> no se setea.
		cuentaEmbargo.setNumeroOrdenCuenta(numeroOrden);
		cuentaEmbargo.setUsuarioUltModificacion(usuarioModif);
		cuentaEmbargo.setFUltimaModificacion(fechaUltmaModif);
		
		return cuentaEmbargo;
	}
	

	public abstract Traba generateTraba(DiligenciaFase3 diligenciaFase3, Long codControlFicheroEmbargo, EntidadesOrdenante entidadOrdenante, Map<String, AccountDTO> customerAccountsMap) throws ICEException;
	
	@AfterMapping
	public void generateTrabaAfterMapping(@MappingTarget Traba traba, DiligenciaFase3 diligenciaFase3, EntidadesOrdenante entidadOrdenante, Map<String, AccountDTO> customerAccountsMap) throws ICEException {
		
		BigDecimal fechaUltmaModif = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
		String usuarioModif = EmbargosConstants.USER_AUTOMATICO;

		/*
		MAPEO ELIMINADO
		CuentasRecaudacion cuentaRecaudacion = entidadOrdenante.getEntidadesComunicadora().getCuentasRecaudacion();	
		traba.setCuentasRecaudacion(cuentaRecaudacion);
		*/
		
		//TODO revisar --> Cuenta de inmovilizacion: la asignada por defecto (sucursal de Madrid con codigo=1):
		CuentasInmovilizacion cuentaInmovilizacion = new CuentasInmovilizacion();
		cuentaInmovilizacion.setCodCuentaInmovilizacion(EmbargosConstants.COD_CUENTA_INMOVILIZACION_DEFAULT);
		traba.setCuentasInmovilizacion(cuentaInmovilizacion);
		
		//Estado inicial de la traba al generarse:
		EstadoTraba estadoTraba = new EstadoTraba();
		estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE);
		traba.setEstadoTraba(estadoTraba);
			
		//Determinacion de la fecha limite de la traba:
		Date fechaGeneracionDiligencia = diligenciaFase3.getFechaGeneracionDiligencia();
		BigDecimal fechaLimiteTraba = determineFechaLimiteTraba(entidadOrdenante, fechaGeneracionDiligencia);
		traba.setFechaLimite(fechaLimiteTraba);
		
		//CUENTAS TRABAS:
		
		List<CuentaTraba> cuentaTrabasList = new ArrayList<>();
		
		BigDecimal numeroOrden = new BigDecimal(1);
		
		//Datos cuenta cliente 1:
		CuentaTraba cuentaTraba = null;
		
		if (diligenciaFase3.getCodigoCuentaCliente1()!=null && !diligenciaFase3.getCodigoCuentaCliente1().isEmpty()) {

			String codigoCuentaCliente1 = diligenciaFase3.getCodigoCuentaCliente1();
			
			String ibanFromCCC1 = BankAccountUtils.convertToIBAN(codigoCuentaCliente1);
			
			AccountDTO accountDTO = customerAccountsMap.get(ibanFromCCC1);
		
			cuentaTraba = setCuentaTrabaFromAccountDTO(accountDTO, ibanFromCCC1, traba, numeroOrden, fechaUltmaModif, usuarioModif);
			
			cuentaTrabasList.add(cuentaTraba);
			
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
		}
		
		//Datos cuenta cliente 2:
		if (diligenciaFase3.getCodigoCuentaCliente2()!=null && !diligenciaFase3.getCodigoCuentaCliente2().isEmpty()) {
			
			String codigoCuentaCliente2 = diligenciaFase3.getCodigoCuentaCliente2();
			
			String ibanFromCCC2 = BankAccountUtils.convertToIBAN(codigoCuentaCliente2);
			
			AccountDTO accountDTO = customerAccountsMap.get(ibanFromCCC2);
		
			cuentaTraba = setCuentaTrabaFromAccountDTO(accountDTO, ibanFromCCC2, traba, numeroOrden, fechaUltmaModif, usuarioModif);
			
			cuentaTrabasList.add(cuentaTraba);
			
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
		}
		
		//Datos cuenta cliente 3:
		if (diligenciaFase3.getCodigoCuentaCliente3()!=null && !diligenciaFase3.getCodigoCuentaCliente3().isEmpty()) {
			
			String codigoCuentaCliente3 = diligenciaFase3.getCodigoCuentaCliente3();
			
			String ibanFromCCC3 = BankAccountUtils.convertToIBAN(codigoCuentaCliente3);
			
			AccountDTO accountDTO = customerAccountsMap.get(ibanFromCCC3);
		
			cuentaTraba = setCuentaTrabaFromAccountDTO(accountDTO, ibanFromCCC3, traba, numeroOrden, fechaUltmaModif, usuarioModif);
			
			cuentaTrabasList.add(cuentaTraba);

		}
		
		traba.setCuentaTrabas(cuentaTrabasList);
		
        //Usuario y fecha ultima modificacion:
		traba.setUsuarioUltModificacion(usuarioModif);
		traba.setFUltimaModificacion(fechaUltmaModif);
	}

	private CuentaTraba setCuentaTrabaFromAccountDTO(AccountDTO accountDTO, String ibanCalculatedFromDiligenciaFase3, Traba traba,
			BigDecimal numeroOrden, BigDecimal fechaUltmaModif, String usuarioModif) {
		
		EstadoTraba estadoTraba;
		CuentaTraba cuentaTraba;
		cuentaTraba = new CuentaTraba();
		
		if (accountDTO!=null) {
			//Seteo de datos que vienen de DWH:
			cuentaTraba.setCuenta(accountDTO.getAccountNum());
			cuentaTraba.setIban(accountDTO.getIban());
			cuentaTraba.setDivisa(accountDTO.getDivisa());
			cuentaTraba.setEstadoCuenta(accountDTO.getStatus());
			
			//Por defecto informar la actuacion (motivo) de la cuentaTraba a 'Sin actuacion':
			CuentaTrabaActuacion cuentaTrabaActuacion = new CuentaTrabaActuacion();
			cuentaTrabaActuacion.setCodActuacion(EmbargosConstants.CODIGO_ACTUACION_SIN_ACTUACION_AEAT);
			cuentaTraba.setCuentaTrabaActuacion(cuentaTrabaActuacion);
		
		} else {
			//Cuenta no encontrada en DWH:
			cuentaTraba.setCuenta(null);
			cuentaTraba.setEstadoCuenta(EmbargosConstants.BANK_ACCOUNT_STATUS_NOTFOUND);
			
			//Iban calculado a partir del 'Codigo de Cuenta Cliente' que viene en el fichero de diligencias de embargos:
			cuentaTraba.setIban(ibanCalculatedFromDiligenciaFase3);
			
			//Indicar la actuacion (motivo) de la cuentaTraba a inexistente:
			CuentaTrabaActuacion cuentaTrabaActuacion = new CuentaTrabaActuacion();
			cuentaTrabaActuacion.setCodActuacion(EmbargosConstants.CODIGO_ACTUACION_CUENTA_INEXISTENTE_O_CANCELADA_AEAT);
			cuentaTraba.setCuentaTrabaActuacion(cuentaTrabaActuacion);
		}

		cuentaTraba.setTraba(traba);
		estadoTraba = new EstadoTraba();
		estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE);
		cuentaTraba.setEstadoTraba(estadoTraba);
		cuentaTraba.setOrigenEmb(EmbargosConstants.IND_FLAG_YES);
		cuentaTraba.setAgregarATraba(EmbargosConstants.IND_FLAG_YES);
		cuentaTraba.setNumeroOrdenCuenta(numeroOrden);
		cuentaTraba.setUsuarioUltModificacion(usuarioModif);
		cuentaTraba.setFUltimaModificacion(fechaUltmaModif);
		return cuentaTraba;
	}

	private BigDecimal determineFechaLimiteTraba(EntidadesOrdenante entidadOrdenante, Date fechaGeneracionDiligencia) {

		BigDecimal fechaLimiteTraba = null;
		BigDecimal diasRespuestaFase3 = new BigDecimal(0);
		
		//Sumar los dias de respuesta de la fase 3:
		if (entidadOrdenante!=null && entidadOrdenante.getEntidadesComunicadora()!=null && entidadOrdenante.getEntidadesComunicadora().getDiasRespuestaF3()!=null) {
			diasRespuestaFase3 = entidadOrdenante.getEntidadesComunicadora().getDiasRespuestaF3();
		}       				
		if (fechaGeneracionDiligencia!=null) {
			Date fechaLimiteTrabaDate = DateUtils.convertToDate(DateUtils.convertToLocalDate(fechaGeneracionDiligencia).plusDays(diasRespuestaFase3.longValue()));
			fechaLimiteTraba = ICEDateUtils.dateToBigDecimal(fechaLimiteTrabaDate, ICEDateUtils.FORMAT_yyyyMMdd);
		}
		
		return fechaLimiteTraba;
	}
	
	
	// GENERACION FICHERO TRABAS:
	
	@Mappings({
		@Mapping(source = "delegacionAgenciaEmisora", target = "delegacionAgenciaReceptora"),
		@Mapping(expression = "java(new java.util.Date())", target="fechaCreacionFicheroTrabas")
	})
	public abstract EntidadTransmisoraFase4 generateEntidadTransmisoraFase4(EntidadTransmisoraFase3 entidadTransmisoraFase3);
	
	@Mappings({
		@Mapping(source = "delegacionAgenciaEmisora", target = "delegacionAgenciaReceptora"),
	})
	public abstract EntidadCreditoFase4 generateEntidadCreditoFase4(EntidadCreditoFase3 entidadCreditoFase3);
	
	@Mappings({
		@Mapping(constant = EmbargosConstants.CODIGO_REGISTRO_AEAT_TRABA_FASE4, target = "indicadorRegistro"),
		@Mapping(source = "embargo.datosCliente.nif", target = "nifDeudor"),
		@Mapping(source = "embargo.nombre", target = "nombreDeudor"),
		@Mapping(source = "embargo.siglasVp", target = "siglasViaPublica"),
		@Mapping(source = "embargo.nombreVp", target = "nombreViaPublica"),
		@Mapping(source = "embargo.numero", target = "numeroPortal"),
		@Mapping(source = "embargo.letra", target = "letraPortal"),
		@Mapping(source = "embargo.escalera", target = "escalera"),
		@Mapping(source = "embargo.piso", target = "piso"),
		@Mapping(source = "embargo.puerta", target = "puerta"),
		@Mapping(source = "embargo.municipio", target = "nombreMunicipio"),
		@Mapping(source = "embargo.codigoPostal", target = "codigoPostal"),
		@Mapping(source = "embargo.numeroEmbargo", target = "numeroDiligenciaEmbargo"),
		@Mapping(source = "embargo.importe", target = "importeTotalAEmbargar"),
		@Mapping(source = "traba.importeTrabado", target = "importeTotalTrabado"),
		@Mapping(target = "fechaTraba", ignore=true),
	})
	public abstract TrabaFase4 generateTrabaFase4(Embargo embargo, Traba traba, List<CuentaTraba> cuentaTrabaOrderedList);
	
	@AfterMapping
	public void generateTrabaFase4AfterMapping(@MappingTarget TrabaFase4 trabaFase4,
			Embargo embargo, Traba traba, List<CuentaTraba> cuentaTrabaOrderedList) {

		if (traba.getFechaTraba()!=null) {
			trabaFase4.setFechaTraba(ICEDateUtils.bigDecimalToDate(traba.getFechaTraba(), ICEDateUtils.FORMAT_yyyyMMdd));
		}
		
//		private String fechaGeneracionDiligencia;
//		private String indicadorExisteMasCuentas;
//		private Date fechaLimiteIngresoImporteTrabado;

		
	}
	
	@Mappings({
		@Mapping(source = "finEntidadCreditoFase3.delegacionAgenciaEmisora", target = "delegacionAgenciaReceptora"),
		@Mapping(source = "importeTotalTrabado", target = "importeTotalTrabado"),
	})
	public abstract FinEntidadCreditoFase4 generateFinEntidadCreditoFase4(FinEntidadCreditoFase3 finEntidadCreditoFase3, BigDecimal importeTotalTrabado);
	
	@Mappings({
		@Mapping(expression = "java(new java.util.Date())", target="fechaCreacionFicheroTrabas")
	})
	public abstract FinEntidadTransmisoraFase4 generateFinEntidadTransmisoraFase4(FinEntidadTransmisoraFase3 finEntidadTransmisoraFase3);

	@Mappings({
			@Mapping(source = "codControlFichero", target = "controlFichero.codControlFichero"),
			@Mapping(source = "levantamientoAEAT.nifDeudor", target = "datosCliente.nif"),
	})
	public abstract LevantamientoTraba generateLevantamiento(Long codControlFichero, Levantamiento levantamientoAEAT, Traba traba, CustomerDTO DWHCustomer)
			throws ICEException;

	@AfterMapping
	public void generateLevantamientoAfterMapping(@MappingTarget LevantamientoTraba levantamiento, Levantamiento levantamientoAEAT, Traba traba, CustomerDTO DWHCustomer)
		throws ICEException
	{
		BigDecimal fechaUltmaModif = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
		String zeroCC = "00000000000000000000";
		String usuarioModif = EmbargosConstants.USER_AUTOMATICO;

		levantamiento.setUsuarioUltModificacion(usuarioModif);
		levantamiento.setFUltimaModificacion(fechaUltmaModif);
		levantamiento.setTraba(traba);

		EstadoLevantamiento estadoLevantamiento = new EstadoLevantamiento();
		estadoLevantamiento.setCodEstado(EmbargosConstants.COD_ESTADO_LEVANTAMIENTO_PENDIENTE);
		levantamiento.setEstadoLevantamiento(estadoLevantamiento);
		//  TODO TO BE DEFINED
		levantamiento.setEstadoEjecutado(BigDecimal.ZERO);
		levantamiento.setEstadoContable(BigDecimal.ZERO);
		levantamiento.setIndCasoRevisado(EmbargosConstants.IND_FLAG_NO);
		// sempre a null levantamiento.setCodDeudaDeudor();

		List<CuentaLevantamiento> cuentas = new ArrayList<>(6);
		levantamiento.setCuentaLevantamientos(cuentas);

		if (levantamientoAEAT.getCodigoCuentaCliente1() != null && !levantamientoAEAT.getCodigoCuentaCliente1().isEmpty() && !zeroCC.equals(levantamientoAEAT.getCodigoCuentaCliente1())) {
			CuentaLevantamiento cuentaLevantamiento1 = levantamientoHelperMapper.mapCuentaLevantamiento(levantamiento, BankAccountUtils.convertToIBAN(levantamientoAEAT.getCodigoCuentaCliente1()), levantamientoAEAT.getImporteALevantarCC1(), DWHCustomer, traba, usuarioModif, fechaUltmaModif);
			cuentaLevantamiento1.setNumeroOrdenCuenta(new BigDecimal(1));
			cuentas.add(cuentaLevantamiento1);
		}
		if (levantamientoAEAT.getCodigoCuentaCliente2() != null && !levantamientoAEAT.getCodigoCuentaCliente2().isEmpty() && !zeroCC.equals(levantamientoAEAT.getCodigoCuentaCliente2())) {
			CuentaLevantamiento cuentaLevantamiento2 = levantamientoHelperMapper.mapCuentaLevantamiento(levantamiento, BankAccountUtils.convertToIBAN(levantamientoAEAT.getCodigoCuentaCliente2()), levantamientoAEAT.getImporteALevantarCC2(), DWHCustomer, traba, usuarioModif, fechaUltmaModif);
			cuentaLevantamiento2.setNumeroOrdenCuenta(new BigDecimal(2));
			cuentas.add(cuentaLevantamiento2);
		}
		if (levantamientoAEAT.getCodigoCuentaCliente3() != null && !levantamientoAEAT.getCodigoCuentaCliente3().isEmpty() && !zeroCC.equals(levantamientoAEAT.getCodigoCuentaCliente3())) {
			CuentaLevantamiento cuentaLevantamiento3 = levantamientoHelperMapper.mapCuentaLevantamiento(levantamiento, BankAccountUtils.convertToIBAN(levantamientoAEAT.getCodigoCuentaCliente3()), levantamientoAEAT.getImporteALevantarCC3(), DWHCustomer, traba, usuarioModif, fechaUltmaModif);
			cuentaLevantamiento3.setNumeroOrdenCuenta(new BigDecimal(3));
			cuentas.add(cuentaLevantamiento3);
		}
		if (levantamientoAEAT.getCodigoCuentaCliente4() != null && !levantamientoAEAT.getCodigoCuentaCliente4().isEmpty() && !zeroCC.equals(levantamientoAEAT.getCodigoCuentaCliente4())) {
			CuentaLevantamiento cuentaLevantamiento4 = levantamientoHelperMapper.mapCuentaLevantamiento(levantamiento, BankAccountUtils.convertToIBAN(levantamientoAEAT.getCodigoCuentaCliente4()), levantamientoAEAT.getImporteALevantarCC4(), DWHCustomer, traba, usuarioModif, fechaUltmaModif);
			cuentaLevantamiento4.setNumeroOrdenCuenta(new BigDecimal(4));
			cuentas.add(cuentaLevantamiento4);
		}
		if (levantamientoAEAT.getCodigoCuentaCliente5() != null && !levantamientoAEAT.getCodigoCuentaCliente5().isEmpty() && !zeroCC.equals(levantamientoAEAT.getCodigoCuentaCliente5())) {
			CuentaLevantamiento cuentaLevantamiento5 = levantamientoHelperMapper.mapCuentaLevantamiento(levantamiento, BankAccountUtils.convertToIBAN(levantamientoAEAT.getCodigoCuentaCliente5()), levantamientoAEAT.getImporteALevantarCC5(), DWHCustomer, traba, usuarioModif, fechaUltmaModif);
			cuentaLevantamiento5.setNumeroOrdenCuenta(new BigDecimal(5));
			cuentas.add(cuentaLevantamiento5);
		}
		if (levantamientoAEAT.getCodigoCuentaCliente6() != null && !levantamientoAEAT.getCodigoCuentaCliente6().isEmpty() && !zeroCC.equals(levantamientoAEAT.getCodigoCuentaCliente6())) {
			CuentaLevantamiento cuentaLevantamiento6 = levantamientoHelperMapper.mapCuentaLevantamiento(levantamiento, BankAccountUtils.convertToIBAN(levantamientoAEAT.getCodigoCuentaCliente6()), levantamientoAEAT.getImporteALevantarCC6(), DWHCustomer, traba, usuarioModif, fechaUltmaModif);
			cuentaLevantamiento6.setNumeroOrdenCuenta(new BigDecimal(6));
			cuentas.add(cuentaLevantamiento6);
		}

		BigDecimal importeLevantado = BigDecimal.ZERO;

		for (CuentaLevantamiento cuentaLevantamiento : cuentas)
		{
			if (cuentaLevantamiento.getImporte() != null)
				importeLevantado = importeLevantado.add(cuentaLevantamiento.getImporte());
		}

		levantamiento.setImporteLevantado(importeLevantado);
	}
}
