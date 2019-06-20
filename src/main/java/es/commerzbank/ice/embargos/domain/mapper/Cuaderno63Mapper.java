package es.commerzbank.ice.embargos.domain.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import es.commerzbank.ice.embargos.domain.entity.EntidadesOrdenante;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacion;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.CabeceraEmisorFase1;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.FinFicheroFase1;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.SolicitudInformacionFase1;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase2.CabeceraEmisorFase2;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase2.FinFicheroFase2;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase2.RespuestaSolicitudInformacionFase2;

@Mapper(componentModel="spring")
public abstract class Cuaderno63Mapper {

	@Mappings({
		@Mapping(source = "solicitudInfo.nifDeudor", target = "nif"),
		@Mapping(source = "solicitudInfo.nombreDeudor", target = "razonSocial"),
		@Mapping(source = "solicitudInfo.domicilioDeudor", target = "domicilio"),
		@Mapping(source = "solicitudInfo.codigoDeuda", target = "codDeudaDeudor"),
		@Mapping(source = "codControlFicheroPeticion", target = "controlFichero.codControlFichero"),		
		@Mapping(source = "codControlFicheroInformacion", target = "codFicheroRespuesta")
	})
	public abstract PeticionInformacion generatePeticionInformacion(SolicitudInformacionFase1 solicitudInfo, 
			Long codControlFicheroPeticion, Long codControlFicheroInformacion);
	
	@AfterMapping
	protected void setPeticionInformacionAfterMapping(@MappingTarget PeticionInformacion peticionInformacion) {
		
		//Variables pendientes de cambiar:
		BigDecimal pendienteCambiarBigDec = new BigDecimal(0);
		EntidadesOrdenante pendienteCambiarEntidadesOrdenante  = new EntidadesOrdenante();
		pendienteCambiarEntidadesOrdenante.setCodEntidadOrdenante(Long.valueOf(1));
		

		//eliminar en el futuro, debe ser por sequence cuando no sea un char(10):
		peticionInformacion.setCodPeticion(Long.toString(System.currentTimeMillis()).substring(2, 12));
		
		peticionInformacion.setCodSucursal(pendienteCambiarBigDec);
		peticionInformacion.setEntidadesOrdenante(pendienteCambiarEntidadesOrdenante);
		
	}
	

	public abstract CabeceraEmisorFase2 generateCabeceraEmisorFase2(CabeceraEmisorFase1 cabeceraEmisorFase1, Date fechaObtencionFicheroEntidadDeDeposito);

	public abstract RespuestaSolicitudInformacionFase2 generateRespuestaSolicitudInformacionFase2(SolicitudInformacionFase1 solicitudInformacionFase1,
			Map<String,String> ibanClavesSeguridadMap);
	
	@AfterMapping
	protected void setRespuestaSolicitudInformacionFase2AfterMapping(@MappingTarget RespuestaSolicitudInformacionFase2 respuesta,
			Map<String,String> ibanClavesSeguridadMap) {
		
		if (ibanClavesSeguridadMap!=null) {
			
			int i = 1;
			for (Map.Entry<String,String> entry : ibanClavesSeguridadMap.entrySet()) {
				
				String ibanCuenta = entry.getKey();
				String claveSeguridad = entry.getValue();
				
				switch (i) {
					case 1:
						respuesta.setIbanCuenta1(ibanCuenta);
						respuesta.setClaveSeguridadIban1(claveSeguridad);
						break;
					case 2:
						respuesta.setIbanCuenta2(ibanCuenta);
						respuesta.setClaveSeguridadIban2(claveSeguridad);
						break;
					case 3:
						respuesta.setIbanCuenta3(ibanCuenta);
						respuesta.setClaveSeguridadIban3(claveSeguridad);
						break;
					case 4:
						respuesta.setIbanCuenta4(ibanCuenta);
						respuesta.setClaveSeguridadIban4(claveSeguridad);
						break;
					case 5:
						respuesta.setIbanCuenta5(ibanCuenta);
						respuesta.setClaveSeguridadIban5(claveSeguridad);
						break;
					case 6:
						respuesta.setIbanCuenta6(ibanCuenta);
						respuesta.setClaveSeguridadIban6(claveSeguridad);
						break;
					default:
				}
				i++;
			}
		}
	}
	
	public abstract FinFicheroFase2 generateFinFicheroFase2(FinFicheroFase1 finFicheroFase1); 
	
//	@Mappings({
//		@Mapping(source = "ordenEjecucionEmbargo.nifDeudor", target = "nif"),
//		@Mapping(source = "ordenEjecucionEmbargo.nombreDeudor", target = "nombre"),
//		@Mapping(source = "ordenEjecucionEmbargo.domicilioDeudor", target = "domicilio"),
//		@Mapping(source = "ordenEjecucionEmbargoComp.identificadorDeuda", target = "tipoDeuda"),
//		@Mapping(source = "ordenEjecucionEmbargo.importeTotalAEmbargar", target = "importe"),
//		@Mapping(source = "ordenEjecucionEmbargo.codigoDeuda", target = "codDeudaDeudor")
//	})
//	public abstract Embargo generateEmbargo(OrdenEjecucionEmbargoFase3 ordenEjecucionEmbargo, 
//			OrdenEjecucionEmbargoComplementarioFase3 ordenEjecucionEmbargoComp);
	
}
