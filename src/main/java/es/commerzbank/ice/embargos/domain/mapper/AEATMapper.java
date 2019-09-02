package es.commerzbank.ice.embargos.domain.mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.entity.CuentaEmbargo;
import es.commerzbank.ice.embargos.domain.entity.CuentaTraba;
import es.commerzbank.ice.embargos.domain.entity.CuentasInmovilizacion;
import es.commerzbank.ice.embargos.domain.entity.CuentasRecaudacion;
import es.commerzbank.ice.embargos.domain.entity.Embargo;
import es.commerzbank.ice.embargos.domain.entity.EntidadesOrdenante;
import es.commerzbank.ice.embargos.domain.entity.EstadoTraba;
import es.commerzbank.ice.embargos.domain.entity.Traba;
import es.commerzbank.ice.embargos.formats.aeat.diligencias.Diligencia;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.OrdenEjecucionEmbargoComplementarioFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.OrdenEjecucionEmbargoFase3;
import es.commerzbank.ice.utils.EmbargosConstants;
import es.commerzbank.ice.utils.ICEDateUtils;

@Mapper(componentModel="spring")
public abstract class AEATMapper {

	
	@Mappings({
		@Mapping(source = "ordenEjecucionEmbargo.nifDeudor", target = "nif"),
		@Mapping(source = "ordenEjecucionEmbargo.nombreDeudor", target = "nombre"),
		@Mapping(source = "ordenEjecucionEmbargo.domicilioDeudor", target = "domicilio"),
		@Mapping(source = "ordenEjecucionEmbargo.municipio", target = "municipio"),
		@Mapping(source = "ordenEjecucionEmbargo.codigoPostal", target = "codigoPostal"),
		@Mapping(source = "ordenEjecucionEmbargo.identificadorDeuda", target = "numeroEmbargo"),
		@Mapping(source = "ordenEjecucionEmbargo.importeTotalAEmbargar", target = "importe"),
		@Mapping(source = "ordenEjecucionEmbargo.codigoDeuda", target = "codDeudaDeudor"),
		@Mapping(source = "codControlFicheroEmbargo", target = "controlFichero.codControlFichero"),
		@Mapping(source = "entidadOrdenante", target = "entidadesOrdenante"),
		@Mapping(source = "razonSocialInterna", target = "razonSocialInterna"),
		@Mapping(source = "fechaLimiteTraba", target = "fechaLimiteTraba")
	})
	public abstract Embargo generateEmbargo(Diligencia diligencia, Long codControlFicheroEmbargo, EntidadesOrdenante entidadOrdenante, String razonSocialInterna, BigDecimal fechaLimiteTraba);
	
	@AfterMapping
	public void generateEmbargoAfterMapping(@MappingTarget Embargo embargo, Diligencia diligencia) {
		
		List<CuentaEmbargo> cuentaEmbargosList = new ArrayList<>();
		
		BigDecimal numeroOrden = new BigDecimal(1);
		
		BigDecimal fechaUltmaModif = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
		String usuarioModif = EmbargosConstants.USER_AUTOMATICO;
		
		//seteo de datregcomdet:
		StringBuilder datregcomdet = new StringBuilder();
		datregcomdet.append(ordenEjecucionEmbargoComp.getTextoLibre1());
		datregcomdet.append(EmbargosConstants.SEPARADOR_ESPACIO);
		datregcomdet.append(ordenEjecucionEmbargoComp.getTextoLibre2());
		datregcomdet.append(EmbargosConstants.SEPARADOR_ESPACIO);
		datregcomdet.append(ordenEjecucionEmbargoComp.getTextoLibre3());
		
		embargo.setDatregcomdet(datregcomdet.toString());
				
		
		//SETEO DE CUENTAS:
		//Datos iban1:
		CuentaEmbargo cuentaEmbargo = new CuentaEmbargo();
		
		if (ordenEjecucionEmbargo.getIbanCuenta1()!=null && !ordenEjecucionEmbargo.getIbanCuenta1().isEmpty()) {
			cuentaEmbargo.setEmbargo(embargo);
			cuentaEmbargo.setIban(ordenEjecucionEmbargo.getIbanCuenta1());
			cuentaEmbargo.setClaveSeguridad(ordenEjecucionEmbargo.getClaveSeguridadIban1());
			cuentaEmbargo.setNumeroOrdenCuenta(numeroOrden);
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
			cuentaEmbargo.setUsuarioUltModificacion(usuarioModif);
			cuentaEmbargo.setFUltimaModificacion(fechaUltmaModif);
			cuentaEmbargosList.add(cuentaEmbargo);
		}

		//Datos iban2:
		if (ordenEjecucionEmbargo.getIbanCuenta2()!=null && !ordenEjecucionEmbargo.getIbanCuenta2().isEmpty()) {
			cuentaEmbargo = new CuentaEmbargo();
			cuentaEmbargo.setEmbargo(embargo);
			cuentaEmbargo.setIban(ordenEjecucionEmbargo.getIbanCuenta2());
			cuentaEmbargo.setClaveSeguridad(ordenEjecucionEmbargo.getClaveSeguridadIban2());
			cuentaEmbargo.setNumeroOrdenCuenta(numeroOrden);
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
			cuentaEmbargo.setUsuarioUltModificacion(usuarioModif);
			cuentaEmbargo.setFUltimaModificacion(fechaUltmaModif);
			cuentaEmbargosList.add(cuentaEmbargo);
		}
		
		//Datos iban3:
		if (ordenEjecucionEmbargo.getIbanCuenta3()!=null && !ordenEjecucionEmbargo.getIbanCuenta3().isEmpty()) {
			cuentaEmbargo = new CuentaEmbargo();
			cuentaEmbargo.setEmbargo(embargo);
			cuentaEmbargo.setIban(ordenEjecucionEmbargo.getIbanCuenta3());
			cuentaEmbargo.setClaveSeguridad(ordenEjecucionEmbargo.getClaveSeguridadIban3());
			cuentaEmbargo.setNumeroOrdenCuenta(numeroOrden);
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
			cuentaEmbargo.setUsuarioUltModificacion(usuarioModif);
			cuentaEmbargo.setFUltimaModificacion(fechaUltmaModif);
			cuentaEmbargosList.add(cuentaEmbargo);
		}
		
		//Datos iban4:
		if (ordenEjecucionEmbargo.getIbanCuenta4()!=null && !ordenEjecucionEmbargo.getIbanCuenta4().isEmpty()) {
			cuentaEmbargo = new CuentaEmbargo();
			cuentaEmbargo.setEmbargo(embargo);
			cuentaEmbargo.setIban(ordenEjecucionEmbargo.getIbanCuenta4());
			cuentaEmbargo.setClaveSeguridad(ordenEjecucionEmbargo.getClaveSeguridadIban4());
			cuentaEmbargo.setNumeroOrdenCuenta(numeroOrden);
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
			cuentaEmbargo.setUsuarioUltModificacion(usuarioModif);
			cuentaEmbargo.setFUltimaModificacion(fechaUltmaModif);
			cuentaEmbargosList.add(cuentaEmbargo);
		}
		
		//Datos iban5:
		if (ordenEjecucionEmbargo.getIbanCuenta5()!=null && !ordenEjecucionEmbargo.getIbanCuenta5().isEmpty()) {
			cuentaEmbargo = new CuentaEmbargo();
			cuentaEmbargo.setEmbargo(embargo);
			cuentaEmbargo.setIban(ordenEjecucionEmbargo.getIbanCuenta5());
			cuentaEmbargo.setClaveSeguridad(ordenEjecucionEmbargo.getClaveSeguridadIban5());
			cuentaEmbargo.setNumeroOrdenCuenta(numeroOrden);
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
			cuentaEmbargo.setUsuarioUltModificacion(usuarioModif);
			cuentaEmbargo.setFUltimaModificacion(fechaUltmaModif);
			cuentaEmbargosList.add(cuentaEmbargo);
		}
		
		//Datos iban6:
		if (ordenEjecucionEmbargo.getIbanCuenta6()!=null && !ordenEjecucionEmbargo.getIbanCuenta6().isEmpty()) {
			cuentaEmbargo = new CuentaEmbargo();
			cuentaEmbargo.setEmbargo(embargo);
			cuentaEmbargo.setIban(ordenEjecucionEmbargo.getIbanCuenta6());
			cuentaEmbargo.setClaveSeguridad(ordenEjecucionEmbargo.getClaveSeguridadIban6());
			cuentaEmbargo.setNumeroOrdenCuenta(numeroOrden);
			cuentaEmbargo.setUsuarioUltModificacion(usuarioModif);
			cuentaEmbargo.setFUltimaModificacion(fechaUltmaModif);
			cuentaEmbargosList.add(cuentaEmbargo);
		}
		
		embargo.setCuentaEmbargos(cuentaEmbargosList);
		
        //Usuario y fecha ultima modificacion:
		embargo.setUsuarioUltModificacion(usuarioModif);
		embargo.setFUltimaModificacion(fechaUltmaModif);
		
	}
	

	public abstract Traba generateTraba(OrdenEjecucionEmbargoFase3 ordenEjecucionEmbargo, 
			OrdenEjecucionEmbargoComplementarioFase3 ordenEjecucionEmbargoComp, Long codControlFicheroEmbargo, EntidadesOrdenante entidadOrdenante);
	
	@AfterMapping
	public void generateTrabaAfterMapping(@MappingTarget Traba traba, OrdenEjecucionEmbargoFase3 ordenEjecucionEmbargo, 
			OrdenEjecucionEmbargoComplementarioFase3 ordenEjecucionEmbargoComp, EntidadesOrdenante entidadOrdenante) {
		
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
		
		//Datos iban1:
		CuentaTraba cuentaTraba = new CuentaTraba();
		
		if (ordenEjecucionEmbargo.getIbanCuenta1()!=null && !ordenEjecucionEmbargo.getIbanCuenta1().isEmpty()) {
			cuentaTraba.setTraba(traba);
			cuentaTraba.setIban(ordenEjecucionEmbargo.getIbanCuenta1());
			cuentaTraba.setNumeroOrdenCuenta(numeroOrden);
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
			//TODO: estado cuenta traba tiene que ser el de DWH
			estadoTraba = new EstadoTraba();
			estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_INICIAL);
			cuentaTraba.setEstadoTraba(estadoTraba);
			cuentaTraba.setOrigenEmb(EmbargosConstants.IND_FLAG_YES);
			cuentaTraba.setUsuarioUltModificacion(usuarioModif);
			cuentaTraba.setFUltimaModificacion(fechaUltmaModif);
			cuentaTrabasList.add(cuentaTraba);
		}

		//Datos iban2:
		if (ordenEjecucionEmbargo.getIbanCuenta2()!=null && !ordenEjecucionEmbargo.getIbanCuenta2().isEmpty()) {
			cuentaTraba = new CuentaTraba();
			cuentaTraba.setTraba(traba);
			cuentaTraba.setIban(ordenEjecucionEmbargo.getIbanCuenta2());
			cuentaTraba.setNumeroOrdenCuenta(numeroOrden);
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
			estadoTraba = new EstadoTraba();
			estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_INICIAL);
			cuentaTraba.setEstadoTraba(estadoTraba);
			cuentaTraba.setOrigenEmb(EmbargosConstants.IND_FLAG_YES);
			cuentaTraba.setUsuarioUltModificacion(usuarioModif);
			cuentaTraba.setFUltimaModificacion(fechaUltmaModif);
			cuentaTrabasList.add(cuentaTraba);
		}
		
		//Datos iban3:
		if (ordenEjecucionEmbargo.getIbanCuenta3()!=null && !ordenEjecucionEmbargo.getIbanCuenta3().isEmpty()) {
			cuentaTraba = new CuentaTraba();
			cuentaTraba.setTraba(traba);
			cuentaTraba.setIban(ordenEjecucionEmbargo.getIbanCuenta3());
			cuentaTraba.setNumeroOrdenCuenta(numeroOrden);
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
			estadoTraba = new EstadoTraba();
			estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_INICIAL);
			cuentaTraba.setEstadoTraba(estadoTraba);
			cuentaTraba.setOrigenEmb(EmbargosConstants.IND_FLAG_YES);
			cuentaTraba.setUsuarioUltModificacion(usuarioModif);
			cuentaTraba.setFUltimaModificacion(fechaUltmaModif);
			cuentaTrabasList.add(cuentaTraba);
		}
		
		//Datos iban4:
		if (ordenEjecucionEmbargo.getIbanCuenta4()!=null && !ordenEjecucionEmbargo.getIbanCuenta4().isEmpty()) {
			cuentaTraba = new CuentaTraba();
			cuentaTraba.setTraba(traba);
			cuentaTraba.setIban(ordenEjecucionEmbargo.getIbanCuenta4());
			cuentaTraba.setNumeroOrdenCuenta(numeroOrden);
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
			estadoTraba = new EstadoTraba();
			estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_INICIAL);
			cuentaTraba.setEstadoTraba(estadoTraba);
			cuentaTraba.setOrigenEmb(EmbargosConstants.IND_FLAG_YES);
			cuentaTraba.setUsuarioUltModificacion(usuarioModif);
			cuentaTraba.setFUltimaModificacion(fechaUltmaModif);
			cuentaTrabasList.add(cuentaTraba);
		}
		
		//Datos iban5:
		if (ordenEjecucionEmbargo.getIbanCuenta5()!=null && !ordenEjecucionEmbargo.getIbanCuenta5().isEmpty()) {
			cuentaTraba = new CuentaTraba();
			cuentaTraba.setTraba(traba);
			cuentaTraba.setIban(ordenEjecucionEmbargo.getIbanCuenta5());
			cuentaTraba.setNumeroOrdenCuenta(numeroOrden);
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
			estadoTraba = new EstadoTraba();
			estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_INICIAL);
			cuentaTraba.setEstadoTraba(estadoTraba);
			cuentaTraba.setOrigenEmb(EmbargosConstants.IND_FLAG_YES);
			cuentaTraba.setUsuarioUltModificacion(usuarioModif);
			cuentaTraba.setFUltimaModificacion(fechaUltmaModif);
			cuentaTrabasList.add(cuentaTraba);
		}
		
		//Datos iban6:
		if (ordenEjecucionEmbargo.getIbanCuenta6()!=null && !ordenEjecucionEmbargo.getIbanCuenta6().isEmpty()) {
			cuentaTraba = new CuentaTraba();
			cuentaTraba.setTraba(traba);
			cuentaTraba.setIban(ordenEjecucionEmbargo.getIbanCuenta6());
			cuentaTraba.setNumeroOrdenCuenta(numeroOrden);
			estadoTraba = new EstadoTraba();
			estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_INICIAL);
			cuentaTraba.setEstadoTraba(estadoTraba);
			cuentaTraba.setOrigenEmb(EmbargosConstants.IND_FLAG_YES);
			cuentaTraba.setUsuarioUltModificacion(usuarioModif);
			cuentaTraba.setFUltimaModificacion(fechaUltmaModif);
			cuentaTrabasList.add(cuentaTraba);
		}
		
		traba.setCuentaTrabas(cuentaTrabasList);
		
        //Usuario y fecha ultima modificacion:
		traba.setUsuarioUltModificacion(usuarioModif);
		traba.setFUltimaModificacion(fechaUltmaModif);
	}
}
