package es.commerzbank.ice.utils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import es.commerzbank.ice.embargos.domain.entity.ControlFichero;
import es.commerzbank.ice.embargos.domain.entity.EntidadesOrdenante;
import es.commerzbank.ice.embargos.domain.entity.PeticionInformacion;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.CabeceraEmisorFase1;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.FinFicheroFase1;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.SolicitudInformacionFase1;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase2.CabeceraEmisorFase2;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase2.FinFicheroFase2;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase2.RespuestaSolicitudInformacionFase2;

public class Cuaderno63Helper {

	
	public static PeticionInformacion generatePeticionInformacion(SolicitudInformacionFase1 solicitudInfo, 
			Long codControlFicheroPeticion, Long codControlFicheroInformacion) {

		//Variables pendientes de cambiar:
		String pendienteCambiarStr = "";
		BigDecimal pendienteCambiarBigDec = new BigDecimal(0);
		ControlFichero pendienteCambiarControlFichero = new ControlFichero();
		pendienteCambiarControlFichero.setCodControlFichero(codControlFicheroPeticion);
		EntidadesOrdenante pendienteCambiarEntidadesOrdenante  = new EntidadesOrdenante();
		pendienteCambiarEntidadesOrdenante.setCodEntidadOrdenante(new Long(1));
		
		PeticionInformacion peticionInformacion = new PeticionInformacion();

		//eliminar en el futuro, debe ser por sequence cuando no sea un char(10):
		peticionInformacion.setCodPeticion(Long.toString(System.currentTimeMillis()).substring(2, 12));
		
		peticionInformacion.setNif(solicitudInfo.getNifDeudor());
		peticionInformacion.setRazonSocial(solicitudInfo.getNombreDeudor());
		peticionInformacion.setDomicilio(solicitudInfo.getDomicilioDeudor());
		peticionInformacion.setMunicipio(solicitudInfo.getMunicipio());
		peticionInformacion.setCodigoPostal(solicitudInfo.getCodigoPostal());		

		peticionInformacion.setCodDeudaDeudor(solicitudInfo.getCodigoDeuda());
		peticionInformacion.setNumeroEmbargo(pendienteCambiarStr);
		peticionInformacion.setTipoDeuda(pendienteCambiarStr);
		
		peticionInformacion.setControlFichero(pendienteCambiarControlFichero);		
		peticionInformacion.setCodFicheroRespuesta(BigDecimal.valueOf(codControlFicheroInformacion));
		peticionInformacion.setCodSucursal(pendienteCambiarBigDec);
		peticionInformacion.setEntidadesOrdenante(pendienteCambiarEntidadesOrdenante);
		peticionInformacion.setIndInformacionCorrecta(pendienteCambiarStr);
		peticionInformacion.setIndMasCuentas(pendienteCambiarStr);
		
		peticionInformacion.setCuenta1(pendienteCambiarStr);
		peticionInformacion.setCuenta2(pendienteCambiarStr);
		peticionInformacion.setCuenta3(pendienteCambiarStr);
		peticionInformacion.setCuenta4(pendienteCambiarStr);
		peticionInformacion.setCuenta5(pendienteCambiarStr);
		peticionInformacion.setCuenta6(pendienteCambiarStr);
		peticionInformacion.setIban1(pendienteCambiarStr);
		peticionInformacion.setIban2(pendienteCambiarStr);
		peticionInformacion.setIban3(pendienteCambiarStr);
		peticionInformacion.setIban4(pendienteCambiarStr);
		peticionInformacion.setIban5(pendienteCambiarStr);
		peticionInformacion.setIban6(pendienteCambiarStr);		
		peticionInformacion.setClaveSeguridad1(pendienteCambiarStr);
		peticionInformacion.setClaveSeguridad2(pendienteCambiarStr);
		peticionInformacion.setClaveSeguridad3(pendienteCambiarStr);
		peticionInformacion.setClaveSeguridad4(pendienteCambiarStr);
		peticionInformacion.setClaveSeguridad5(pendienteCambiarStr);
		peticionInformacion.setClaveSeguridad6(pendienteCambiarStr);
		
		peticionInformacion.setFUltimaModificacion(pendienteCambiarBigDec);
		peticionInformacion.setUsuarioUltModificacion(pendienteCambiarStr);
		
		return peticionInformacion;
	}
	
	public static CabeceraEmisorFase2 generateCabeceraEmisorFase2(CabeceraEmisorFase1 cabeceraEmisorFase1, Date fechaObtencionFicheroEntidadDeDeposito) {
		
		CabeceraEmisorFase2 cabeceraEmisorFase2 = new CabeceraEmisorFase2();
		
		cabeceraEmisorFase2.setCodigoRegistro(cabeceraEmisorFase1.getCodigoRegistro());
		cabeceraEmisorFase2.setCodigoNRBE(cabeceraEmisorFase1.getCodigoNRBE());
		cabeceraEmisorFase2.setFase(Integer.valueOf(2));
		cabeceraEmisorFase2.setFechaObtencionFicheroOrganismo(cabeceraEmisorFase1.getFechaObtencionFicheroOrganismo());
		cabeceraEmisorFase2.setFechaObtencionFicheroEntidadDeDeposito(fechaObtencionFicheroEntidadDeDeposito);
		cabeceraEmisorFase2.setNifOrganismoEmisor(cabeceraEmisorFase1.getNifOrganismoEmisor());
		cabeceraEmisorFase2.setCodigoINEOrganismoEmisor(cabeceraEmisorFase1.getCodigoINEOrganismoEmisor());
		cabeceraEmisorFase2.setNombreOrganismoEmisor(cabeceraEmisorFase1.getNombreOrganismoEmisor());
		cabeceraEmisorFase2.setVersionCuaderno(cabeceraEmisorFase1.getVersionCuaderno());
		
		return cabeceraEmisorFase2;
	}
	
	public static RespuestaSolicitudInformacionFase2 generateRespuestaSolicitudInformacionFase2(
			SolicitudInformacionFase1 solicitudInformacionFase1,
			Map<String,String> ibanClavesSeguridadMap) {
		
		RespuestaSolicitudInformacionFase2 respuesta = new RespuestaSolicitudInformacionFase2();
				
		respuesta.setCodigoRegistro(solicitudInformacionFase1.getCodigoRegistro());
		respuesta.setNifDeudor(solicitudInformacionFase1.getNifDeudor());
		respuesta.setNombreDeudor(solicitudInformacionFase1.getNombreDeudor());
		respuesta.setDomicilioDeudor(solicitudInformacionFase1.getDomicilioDeudor());
		respuesta.setMunicipio(solicitudInformacionFase1.getMunicipio());
		respuesta.setCodigoPostal(solicitudInformacionFase1.getCodigoPostal());
		respuesta.setIdentificadorDeuda(solicitudInformacionFase1.getIdentificadorDeuda());
		respuesta.setCodigoDeuda(solicitudInformacionFase1.getCodigoDeuda());
				
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
		
		return respuesta;
	}
	
	
	public static FinFicheroFase2 generateFinFicheroFase2(FinFicheroFase1 finFicheroFase1) {
			
		FinFicheroFase2 finFicheroFase2 = new FinFicheroFase2();
		
		finFicheroFase2.setCodigoRegistro(finFicheroFase1.getCodigoRegistro());
		finFicheroFase2.setCodigoNRBE(finFicheroFase1.getCodigoNRBE());
		finFicheroFase2.setNumeroRegistrosFichero(finFicheroFase1.getNumeroRegistrosFichero());
		finFicheroFase2.setNifOrganismoEmisor(finFicheroFase1.getNifOrganismoEmisor());
		finFicheroFase2.setCodigoINEOrganismoEmisor(finFicheroFase1.getCodigoINEOrganismoEmisor());
		finFicheroFase2.setNombreOrganismoEmisor(finFicheroFase1.getNombreOrganismoEmisor());
		
		return finFicheroFase2;
		
	}
}
