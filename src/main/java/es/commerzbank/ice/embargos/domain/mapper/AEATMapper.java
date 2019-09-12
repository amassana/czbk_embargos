package es.commerzbank.ice.embargos.domain.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import es.commerzbank.ice.datawarehouse.domain.dto.AccountDTO;
import es.commerzbank.ice.embargos.domain.entity.CuentaEmbargo;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.CuentasInmovilizacion;
import es.commerzbank.ice.embargos.domain.entity.CuentasRecaudacion;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import es.commerzbank.ice.embargos.domain.entity.EntidadesOrdenante;
import es.commerzbank.ice.embargos.domain.entity.EstadoTraba;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.DiligenciaFase3;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.EntidadCreditoFase3;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.EntidadTransmisoraFase3;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.FinEntidadCreditoFase3;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.FinEntidadTransmisoraFase3;
import es.commerzbank.ice.embargos.formats.aeat.trabas.EntidadCreditoFase4;
import es.commerzbank.ice.embargos.formats.aeat.trabas.EntidadTransmisoraFase4;
import es.commerzbank.ice.embargos.formats.aeat.trabas.FinEntidadCreditoFase4;
import es.commerzbank.ice.embargos.formats.aeat.trabas.FinEntidadTransmisoraFase4;
import es.commerzbank.ice.embargos.formats.aeat.trabas.TrabaFase4;
import es.commerzbank.ice.utils.EmbargosConstants;
import es.commerzbank.ice.utils.ICEDateUtils;

@Mapper(componentModel="spring")
public abstract class AEATMapper {

	
	@Mappings({
		@Mapping(source = "diligenciaFase3.nifDeudor", target = "nif"),
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
		@Mapping(source = "entidadOrdenante", target = "entidadesOrdenante"),
		@Mapping(source = "razonSocialInterna", target = "razonSocialInterna"),
	})
	public abstract Embargo generateEmbargo(DiligenciaFase3 diligenciaFase3, Long codControlFicheroEmbargo, EntidadesOrdenante entidadOrdenante, 
			String razonSocialInterna, EntidadCreditoFase3 entidadCreditoFase3, Map<String, AccountDTO> customerAccountsMap);
	
	@AfterMapping
	public void generateEmbargoAfterMapping(@MappingTarget Embargo embargo, DiligenciaFase3 diligenciaFase3, EntidadesOrdenante entidadOrdenante, 
			EntidadCreditoFase3 entidadCreditoFase3, Map<String, AccountDTO> customerAccountsMap) {
		
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
			
			AccountDTO accountDTO = customerAccountsMap.get(diligenciaFase3.getCodigoCuentaCliente1());
			
			if (accountDTO!=null) {
				cuentaEmbargo = new CuentaEmbargo();
				cuentaEmbargo.setEmbargo(embargo);
				cuentaEmbargo.setCuenta(accountDTO.getAccountNum());
				cuentaEmbargo.setImporte(BigDecimal.valueOf(0));
				cuentaEmbargo.setIban(accountDTO.getIban());
				cuentaEmbargo.setNumeroOrdenCuenta(numeroOrden);
				numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
				cuentaEmbargo.setUsuarioUltModificacion(usuarioModif);
				cuentaEmbargo.setFUltimaModificacion(fechaUltmaModif);
				cuentaEmbargosList.add(cuentaEmbargo);
			}
		}
		
		//Datos cuenta cliente 2:		
		if (diligenciaFase3.getCodigoCuentaCliente2()!=null && !diligenciaFase3.getCodigoCuentaCliente2().isEmpty()) {
			
			AccountDTO accountDTO = customerAccountsMap.get(diligenciaFase3.getCodigoCuentaCliente2());
			
			if (accountDTO!=null) {
				cuentaEmbargo = new CuentaEmbargo();
				cuentaEmbargo.setEmbargo(embargo);
				cuentaEmbargo.setCuenta(accountDTO.getAccountNum());
				cuentaEmbargo.setImporte(BigDecimal.valueOf(0));
				cuentaEmbargo.setIban(accountDTO.getIban());
				cuentaEmbargo.setNumeroOrdenCuenta(numeroOrden);
				numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
				cuentaEmbargo.setUsuarioUltModificacion(usuarioModif);
				cuentaEmbargo.setFUltimaModificacion(fechaUltmaModif);
				cuentaEmbargosList.add(cuentaEmbargo);
			}
		}
		
		//Datos cuenta cliente 3:	
		if (diligenciaFase3.getCodigoCuentaCliente3()!=null && !diligenciaFase3.getCodigoCuentaCliente3().isEmpty()) {
			
			AccountDTO accountDTO = customerAccountsMap.get(diligenciaFase3.getCodigoCuentaCliente3());
			
			if (accountDTO!=null) {
				cuentaEmbargo = new CuentaEmbargo();
				cuentaEmbargo.setEmbargo(embargo);
				cuentaEmbargo.setCuenta(accountDTO.getAccountNum());
				cuentaEmbargo.setImporte(BigDecimal.valueOf(0));
				cuentaEmbargo.setIban(accountDTO.getIban());
				cuentaEmbargo.setNumeroOrdenCuenta(numeroOrden);
				cuentaEmbargo.setUsuarioUltModificacion(usuarioModif);
				cuentaEmbargo.setFUltimaModificacion(fechaUltmaModif);
				cuentaEmbargosList.add(cuentaEmbargo);
			}
		}
		
		embargo.setCuentaEmbargos(cuentaEmbargosList);
		
        //Usuario y fecha ultima modificacion:
		embargo.setUsuarioUltModificacion(usuarioModif);
		embargo.setFUltimaModificacion(fechaUltmaModif);
		
	}
	

	public abstract Traba generateTraba(DiligenciaFase3 diligenciaFase3, Long codControlFicheroEmbargo, EntidadesOrdenante entidadOrdenante, Map<String, AccountDTO> customerAccountsMap);
	
	@AfterMapping
	public void generateTrabaAfterMapping(@MappingTarget Traba traba, DiligenciaFase3 diligenciaFase3, EntidadesOrdenante entidadOrdenante, Map<String, AccountDTO> customerAccountsMap) {
		
		BigDecimal fechaUltmaModif = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
		String usuarioModif = EmbargosConstants.USER_AUTOMATICO;
		
		CuentasRecaudacion cuentaRecaudacion = entidadOrdenante.getEntidadesComunicadora().getCuentasRecaudacion();	
		traba.setCuentasRecaudacion(cuentaRecaudacion);
		
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
			
			AccountDTO accountDTO = customerAccountsMap.get(diligenciaFase3.getCodigoCuentaCliente1());
			
			if (accountDTO!=null) {
				cuentaTraba = new CuentaTraba();
				cuentaTraba.setTraba(traba);
				cuentaTraba.setCuenta(accountDTO.getAccountNum());
				cuentaTraba.setIban(accountDTO.getIban());
				cuentaTraba.setDivisa(accountDTO.getDivisa());
				cuentaTraba.setEstadoCuenta(accountDTO.getStatus());
				estadoTraba = new EstadoTraba();
				estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE);
				cuentaTraba.setEstadoTraba(estadoTraba);
				cuentaTraba.setOrigenEmb(EmbargosConstants.IND_FLAG_YES);
				cuentaTraba.setNumeroOrdenCuenta(numeroOrden);
				numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
				cuentaTraba.setUsuarioUltModificacion(usuarioModif);
				cuentaTraba.setFUltimaModificacion(fechaUltmaModif);
				cuentaTrabasList.add(cuentaTraba);
			}
		}
		
		//Datos cuenta cliente 2:
		if (diligenciaFase3.getCodigoCuentaCliente2()!=null && !diligenciaFase3.getCodigoCuentaCliente2().isEmpty()) {
			
			AccountDTO accountDTO = customerAccountsMap.get(diligenciaFase3.getCodigoCuentaCliente2());
			
			if (accountDTO!=null) {
				cuentaTraba = new CuentaTraba();
				cuentaTraba.setTraba(traba);
				cuentaTraba.setCuenta(accountDTO.getAccountNum());
				cuentaTraba.setIban(accountDTO.getIban());
				cuentaTraba.setDivisa(accountDTO.getDivisa());
				cuentaTraba.setEstadoCuenta(accountDTO.getStatus());
				estadoTraba = new EstadoTraba();
				estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE);
				cuentaTraba.setEstadoTraba(estadoTraba);
				cuentaTraba.setOrigenEmb(EmbargosConstants.IND_FLAG_YES);
				cuentaTraba.setNumeroOrdenCuenta(numeroOrden);
				numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
				cuentaTraba.setUsuarioUltModificacion(usuarioModif);
				cuentaTraba.setFUltimaModificacion(fechaUltmaModif);
				cuentaTrabasList.add(cuentaTraba);
			}
		}
		
		//Datos cuenta cliente 3:
		if (diligenciaFase3.getCodigoCuentaCliente3()!=null && !diligenciaFase3.getCodigoCuentaCliente3().isEmpty()) {
			
			AccountDTO accountDTO = customerAccountsMap.get(diligenciaFase3.getCodigoCuentaCliente3());
			
			if (accountDTO!=null) {
				cuentaTraba = new CuentaTraba();
				cuentaTraba.setTraba(traba);
				cuentaTraba.setCuenta(accountDTO.getAccountNum());
				cuentaTraba.setIban(accountDTO.getIban());
				cuentaTraba.setDivisa(accountDTO.getDivisa());
				cuentaTraba.setEstadoCuenta(accountDTO.getStatus());
				estadoTraba = new EstadoTraba();
				estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE);
				cuentaTraba.setEstadoTraba(estadoTraba);
				cuentaTraba.setOrigenEmb(EmbargosConstants.IND_FLAG_YES);
				cuentaTraba.setNumeroOrdenCuenta(numeroOrden);
				cuentaTraba.setUsuarioUltModificacion(usuarioModif);
				cuentaTraba.setFUltimaModificacion(fechaUltmaModif);
				cuentaTrabasList.add(cuentaTraba);
			}
		}
		
		traba.setCuentaTrabas(cuentaTrabasList);
		
        //Usuario y fecha ultima modificacion:
		traba.setUsuarioUltModificacion(usuarioModif);
		traba.setFUltimaModificacion(fechaUltmaModif);
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
		@Mapping(source = "embargo.nif", target = "nifDeudor"),
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
}
