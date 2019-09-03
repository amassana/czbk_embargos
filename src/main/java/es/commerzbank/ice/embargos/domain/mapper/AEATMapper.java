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
import es.commerzbank.ice.datawarehouse.domain.dto.AccountDTO;
import es.commerzbank.ice.embargos.domain.entity.CuentaEmbargo;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.CuentasInmovilizacion;
import es.commerzbank.ice.embargos.domain.entity.CuentasRecaudacion;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import es.commerzbank.ice.embargos.domain.entity.EntidadesOrdenante;
import es.commerzbank.ice.embargos.domain.entity.EstadoTraba;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.Diligencia;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.EntidadCredito;
import es.commerzbank.ice.utils.EmbargosConstants;
import es.commerzbank.ice.utils.ICEDateUtils;

@Mapper(componentModel="spring")
public abstract class AEATMapper {

	
	@Mappings({
		@Mapping(source = "diligencia.nifDeudor", target = "nif"),
		@Mapping(source = "diligencia.nombreDeudor", target = "nombre"),
		@Mapping(source = "diligencia.siglasViaPublica", target = "siglasVp"),
		@Mapping(source = "diligencia.nombreViaPublica", target = "nombreVp"),
		@Mapping(source = "diligencia.numeroPortal", target = "numero"),
		@Mapping(source = "diligencia.letraPortal", target = "letra"),
		@Mapping(source = "diligencia.escalera", target = "escalera"),
		@Mapping(source = "diligencia.piso", target = "piso"),
		@Mapping(source = "diligencia.puerta", target = "puerta"),
		@Mapping(source = "diligencia.nombreMunicipio", target = "municipio"),
		@Mapping(source = "diligencia.codigoPostal", target = "codigoPostal"),
		@Mapping(source = "diligencia.numeroDiligenciaEmbargo", target = "numeroEmbargo"),
		@Mapping(source = "diligencia.importeTotalAEmbargar", target = "importe"),
		@Mapping(source = "codControlFicheroEmbargo", target = "controlFichero.codControlFichero"),
		@Mapping(source = "entidadOrdenante", target = "entidadesOrdenante"),
		@Mapping(source = "razonSocialInterna", target = "razonSocialInterna"),
	})
	public abstract Embargo generateEmbargo(Diligencia diligencia, Long codControlFicheroEmbargo, EntidadesOrdenante entidadOrdenante, 
			String razonSocialInterna, EntidadCredito entidadCredito, Map<String, AccountDTO> customerAccountsMap);
	
	@AfterMapping
	public void generateEmbargoAfterMapping(@MappingTarget Embargo embargo, Diligencia diligencia, EntidadesOrdenante entidadOrdenante, 
			EntidadCredito entidadCredito, Map<String, AccountDTO> customerAccountsMap) {
		
		List<CuentaEmbargo> cuentaEmbargosList = new ArrayList<>();
		
		BigDecimal numeroOrden = new BigDecimal(1);
		
		BigDecimal fechaUltmaModif = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
		String usuarioModif = EmbargosConstants.USER_AUTOMATICO;
		
		//seteo de datregcomdet:
		embargo.setDatregcomdet(entidadCredito!=null ? entidadCredito.getMensajeInformativoParaDeudor() : null);
				
		//Determinacion de la fecha limite de la traba:
		Date fechaGeneracionDiligencia = diligencia.getFechaGeneracionDiligencia();
		BigDecimal diasRespuestaFase3 = new BigDecimal(0);
		if (entidadOrdenante!=null && entidadOrdenante.getEntidadesComunicadora()!=null && entidadOrdenante.getEntidadesComunicadora().getDiasRespuestaF3()!=null) {
			diasRespuestaFase3 = entidadOrdenante.getEntidadesComunicadora().getDiasRespuestaF3();
		}       				
		BigDecimal fechaLimiteTraba = null;
		if (fechaGeneracionDiligencia!=null) {
			Date fechaLimiteTrabaDate = DateUtils.convertToDate(DateUtils.convertToLocalDate(fechaGeneracionDiligencia).plusDays(diasRespuestaFase3.longValue()));
			fechaLimiteTraba = ICEDateUtils.dateToBigDecimal(fechaLimiteTrabaDate, ICEDateUtils.FORMAT_yyyyMMdd);
		}
		embargo.setFechaLimiteTraba(fechaLimiteTraba);
		
		//Fecha de generacion:
		embargo.setFechaGeneracion(ICEDateUtils.dateToBigDecimal(fechaGeneracionDiligencia, ICEDateUtils.FORMAT_yyyyMMdd));
		
		
		//SETEO DE CUENTAS:
		//Datos cuenta cliente 1:
		CuentaEmbargo cuentaEmbargo = null;
		
		if (diligencia.getCodigoCuentaCliente1()!=null && !diligencia.getCodigoCuentaCliente1().isEmpty()) {
			
			AccountDTO accountDTO = customerAccountsMap.get(diligencia.getCodigoCuentaCliente1());
			
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
		if (diligencia.getCodigoCuentaCliente2()!=null && !diligencia.getCodigoCuentaCliente2().isEmpty()) {
			
			AccountDTO accountDTO = customerAccountsMap.get(diligencia.getCodigoCuentaCliente2());
			
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
		if (diligencia.getCodigoCuentaCliente3()!=null && !diligencia.getCodigoCuentaCliente3().isEmpty()) {
			
			AccountDTO accountDTO = customerAccountsMap.get(diligencia.getCodigoCuentaCliente3());
			
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
	

	public abstract Traba generateTraba(Diligencia diligencia, Long codControlFicheroEmbargo, EntidadesOrdenante entidadOrdenante, Map<String, AccountDTO> customerAccountsMap);
	
	@AfterMapping
	public void generateTrabaAfterMapping(@MappingTarget Traba traba, Diligencia diligencia, EntidadesOrdenante entidadOrdenante, Map<String, AccountDTO> customerAccountsMap) {
		
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
		estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_INICIAL);
		traba.setEstadoTraba(estadoTraba);
		
		
		//CUENTAS TRABAS:
		
		List<CuentaTraba> cuentaTrabasList = new ArrayList<>();
		
		BigDecimal numeroOrden = new BigDecimal(1);
		
		//Datos cuenta cliente 1:
		CuentaTraba cuentaTraba = null;
		
		if (diligencia.getCodigoCuentaCliente1()!=null && !diligencia.getCodigoCuentaCliente1().isEmpty()) {
			
			AccountDTO accountDTO = customerAccountsMap.get(diligencia.getCodigoCuentaCliente1());
			
			if (accountDTO!=null) {
				cuentaTraba = new CuentaTraba();
				cuentaTraba.setTraba(traba);
				cuentaTraba.setCuenta(accountDTO.getAccountNum());
				cuentaTraba.setIban(accountDTO.getIban());
				cuentaTraba.setDivisa(accountDTO.getDivisa());
				cuentaTraba.setEstadoCuenta(accountDTO.getStatus());
				estadoTraba = new EstadoTraba();
				estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_INICIAL);
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
		if (diligencia.getCodigoCuentaCliente2()!=null && !diligencia.getCodigoCuentaCliente2().isEmpty()) {
			
			AccountDTO accountDTO = customerAccountsMap.get(diligencia.getCodigoCuentaCliente2());
			
			if (accountDTO!=null) {
				cuentaTraba = new CuentaTraba();
				cuentaTraba.setTraba(traba);
				cuentaTraba.setCuenta(accountDTO.getAccountNum());
				cuentaTraba.setIban(accountDTO.getIban());
				cuentaTraba.setDivisa(accountDTO.getDivisa());
				cuentaTraba.setEstadoCuenta(accountDTO.getStatus());
				estadoTraba = new EstadoTraba();
				estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_INICIAL);
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
		if (diligencia.getCodigoCuentaCliente3()!=null && !diligencia.getCodigoCuentaCliente3().isEmpty()) {
			
			AccountDTO accountDTO = customerAccountsMap.get(diligencia.getCodigoCuentaCliente3());
			
			if (accountDTO!=null) {
				cuentaTraba = new CuentaTraba();
				cuentaTraba.setTraba(traba);
				cuentaTraba.setCuenta(accountDTO.getAccountNum());
				cuentaTraba.setIban(accountDTO.getIban());
				cuentaTraba.setDivisa(accountDTO.getDivisa());
				cuentaTraba.setEstadoCuenta(accountDTO.getStatus());
				estadoTraba = new EstadoTraba();
				estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_INICIAL);
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
}
