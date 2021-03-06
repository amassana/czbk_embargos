package es.commerzbank.ice.embargos.domain.mapper;

import es.commerzbank.ice.comun.lib.util.ICEException;
import es.commerzbank.ice.datawarehouse.domain.dto.AccountDTO;
import es.commerzbank.ice.datawarehouse.domain.dto.CustomerDTO;
import es.commerzbank.ice.embargos.domain.entity.*;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.CabeceraEmisorFase1;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.FinFicheroFase1;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase1.SolicitudInformacionFase1;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase2.CabeceraEmisorFase2;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase2.FinFicheroFase2;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase2.RespuestaSolicitudInformacionFase2;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.CabeceraEmisorFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.FinFicheroFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.OrdenEjecucionEmbargoComplementarioFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase3.OrdenEjecucionEmbargoFase3;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase4.CabeceraEmisorFase4;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase4.ComunicacionResultadoRetencionFase4;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase4.FinFicheroFase4;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase5.OrdenLevantamientoRetencionFase5;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase6.CabeceraEmisorFase6;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase6.FinFicheroFase6;
import es.commerzbank.ice.embargos.formats.cuaderno63.fase6.ResultadoFinalEmbargoFase6;
import es.commerzbank.ice.embargos.utils.EmbargosConstants;
import es.commerzbank.ice.embargos.utils.EmbargosUtils;
import es.commerzbank.ice.embargos.utils.ICEDateUtils;
import org.mapstruct.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

@Mapper(componentModel="spring")
public abstract class Cuaderno63Mapper {

	private static final Logger LOG = LoggerFactory.getLogger(Cuaderno63Mapper.class);
	
	LevantamientoHelperMapper levantamientoHelperMapper = new LevantamientoHelperMapper();
	
	
//	@Mappings({
//		@Mapping(source = "codControlFicheroPeticion", target = "controlFichero.codControlFichero")
//	})
//	public abstract Peticion generatePeticion(Long codControlFicheroPeticion);
//
//	
//	@AfterMapping
//	protected void setPeticionAfterMapping(@MappingTarget Peticion peticion) {
//			
//		//eliminar en el futuro, debe ser por sequence cuando no sea un char(16):
//		peticion.setCodPeticion(Long.toString(System.currentTimeMillis()));
//		
//		EstadoPrimarioPeticion estadoPrimarioPeticion = new EstadoPrimarioPeticion();
//		estadoPrimarioPeticion.setCodEstadoPrimarioPeticion(EmbargosConstants.COD_ESTADO_PRIMARIO_PETICION_ENVIANDO_PETICION_UNICA);	
//		peticion.setEstadoPrimarioPeticion(estadoPrimarioPeticion);
//		
//		EstadoIntPeticion estadoIntPeticion = new EstadoIntPeticion();
//		estadoIntPeticion.setCodEstadoIntPeticion(1L);
//		peticion.setEstadoIntPeticion(estadoIntPeticion);
//		
//		peticion.setNumElementos(BigDecimal.valueOf(1L));
//		
//        //Usuario y fecha ultima modificacion:
//		peticion.setUsuarioUltModificacion(EmbargosConstants.SYSTEM_USER);
//		peticion.setFUltimaModificacion(BigDecimal.valueOf(System.currentTimeMillis()));
//		
//	}

	@Mappings({
		@Mapping(source = "solicitudInfo.nifDeudor", target = "nif"),
		@Mapping(source = "solicitudInfo.nombreDeudor", target = "razonSocial"),
		@Mapping(source = "razonSocialInterna", target = "razonSocialInterna"),
		@Mapping(source = "solicitudInfo.domicilioDeudor", target = "domicilio"),
		@Mapping(source = "solicitudInfo.identificadorDeuda", target = "numeroEmbargo"),
		@Mapping(source = "solicitudInfo.codigoDeuda", target = "codDeudaDeudor"),
		@Mapping(source = "codControlFicheroPeticion", target = "controlFichero.codControlFichero")
	})
	public abstract PeticionInformacion generatePeticionInformacion(
			SolicitudInformacionFase1 solicitudInfo, Long codControlFicheroPeticion,
			List<AccountDTO> listBankAccount, EntidadesComunicadora entidadComunicadora, String razonSocialInterna)
		throws ICEException;
	
	@AfterMapping
	protected void setPeticionInformacionAfterMapping(@MappingTarget PeticionInformacion peticionInformacion, 
			List<AccountDTO> listBankAccount, EntidadesComunicadora entidadComunicadora) throws ICEException {
		
		//Comprobar que si la entidadComunicadora es NULL -> Exception...
		if (entidadComunicadora == null) {
			throw new ICEException("No se puede procesar el fichero con codControlFichero '"
					+ peticionInformacion.getControlFichero().getCodControlFichero() +
					"': Entidad Comunicadora con valor NULL.");
		}
		//Comprobar que la entidad comunicadora tenga entidad Ordenante asociada:
		if (entidadComunicadora.getEntidadesOrdenantes() == null || entidadComunicadora.getEntidadesOrdenantes().isEmpty()) {
			throw new ICEException("No se puede procesar el fichero con codControlFichero '"
					+ peticionInformacion.getControlFichero().getCodControlFichero() +
					"': La entidad Comunicadora con nif '" + entidadComunicadora.getNifEntidad() + "' no tiene asocidada una entidad ordenante.");
		}
		
		peticionInformacion.setEntidadesOrdenante(entidadComunicadora.getEntidadesOrdenantes().get(0));

		//TODO: aplicar la sucursal por defecto?
		peticionInformacion.setCodSucursal(null);
		
		// Se guardaran las primeras cuentas en estado NORMAL y EUR del listado de cuentas, hasta un maximo de 6 cuentas:
		setPreloadedBankAccounts(peticionInformacion,listBankAccount);
		
        //Usuario y fecha ultima modificacion:
		peticionInformacion.setUsuarioUltModificacion(EmbargosConstants.USER_AUTOMATICO);
        peticionInformacion.setFUltimaModificacion(ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss));
		
	}
	
	/**
	 * Setea las primeras cuentas en estado NORMAL del listado de cuentas 'listAccount', hasta un maximo de seis cuentas.
	 * 
	 * @param peticionInformacion
	 * @param listAccount
	 */
	private void setPreloadedBankAccounts(PeticionInformacion peticionInformacion, List<AccountDTO> listAccount) {
	
		List<String> listAccounts = new ArrayList<String>();
		//Iterating by the bank accounts:
		int i=1;
		for (AccountDTO accountDTO : listAccount) {
			//LOG.info("For 1 Cuenta " + accountDTO.getStatus() + " " + accountDTO.getDivisa());
			//Solo se setean las cuentas que tengan estado ACTIVE y moneda EUR
			if (EmbargosConstants.BANK_ACCOUNT_STATUS_ACTIVE.equals(accountDTO.getStatus()) && EmbargosConstants.ISO_MONEDA_EUR.equals(accountDTO.getDivisa())) {
				switch(i) {
					case 1:
						//LOG.info("Cuenta 1 " + accountDTO.getStatus() + " " + accountDTO.getDivisa());
						listAccounts.add(accountDTO.getAccountNum());
						peticionInformacion.setCuenta1(accountDTO.getAccountNum());
						peticionInformacion.setIban1(accountDTO.getIban());
						break;
					case 2:
						//LOG.info("Cuenta 2 " + accountDTO.getStatus() + " " + accountDTO.getDivisa());
						listAccounts.add(accountDTO.getAccountNum());
						peticionInformacion.setCuenta2(accountDTO.getAccountNum());
						peticionInformacion.setIban2(accountDTO.getIban());
						break;
					case 3:
						//LOG.info("Cuenta 3 " + accountDTO.getStatus() + " " + accountDTO.getDivisa());
						listAccounts.add(accountDTO.getAccountNum());
						peticionInformacion.setCuenta3(accountDTO.getAccountNum());
						peticionInformacion.setIban3(accountDTO.getIban());
						break;
					case 4:
						//LOG.info("Cuenta 4 " + accountDTO.getStatus() + " " + accountDTO.getDivisa());
						listAccounts.add(accountDTO.getAccountNum());
						peticionInformacion.setCuenta4(accountDTO.getAccountNum());
						peticionInformacion.setIban4(accountDTO.getIban());
						break;
					case 5:
						//LOG.info("Cuenta 5 " + accountDTO.getStatus() + " " + accountDTO.getDivisa());
						listAccounts.add(accountDTO.getAccountNum());
						peticionInformacion.setCuenta5(accountDTO.getAccountNum());
						peticionInformacion.setIban5(accountDTO.getIban());
						break;
					case 6:
						//LOG.info("Cuenta 6 " + accountDTO.getStatus() + " " + accountDTO.getDivisa());
						listAccounts.add(accountDTO.getAccountNum());
						peticionInformacion.setCuenta6(accountDTO.getAccountNum());
						peticionInformacion.setIban6(accountDTO.getIban());
						break;
					default:
				}
				i++;
			}
		}
		
		//Si no ha habido suficientes de EUR se coge del resto
		for (AccountDTO accountDTO : listAccount) {
			//LOG.info("For 2 Cuenta " + accountDTO.getStatus() + " " + accountDTO.getDivisa());
			if (EmbargosConstants.BANK_ACCOUNT_STATUS_ACTIVE.equals(accountDTO.getStatus()) && !listAccounts.contains(accountDTO.getAccountNum())) {
				switch(i) {
					case 1:
						//LOG.info("Cuenta 1 " + accountDTO.getStatus() + " " + accountDTO.getDivisa());
						peticionInformacion.setCuenta1(accountDTO.getAccountNum());
						peticionInformacion.setIban1(accountDTO.getIban());
						break;
					case 2:
						//LOG.info("Cuenta 2 " + accountDTO.getStatus() + " " + accountDTO.getDivisa());
						peticionInformacion.setCuenta2(accountDTO.getAccountNum());
						peticionInformacion.setIban2(accountDTO.getIban());
						break;
					case 3:
						//LOG.info("Cuenta 3 " + accountDTO.getStatus() + " " + accountDTO.getDivisa());
						peticionInformacion.setCuenta3(accountDTO.getAccountNum());
						peticionInformacion.setIban3(accountDTO.getIban());
						break;
					case 4:
						//LOG.info("Cuenta 4 " + accountDTO.getStatus() + " " + accountDTO.getDivisa());
						peticionInformacion.setCuenta4(accountDTO.getAccountNum());
						peticionInformacion.setIban4(accountDTO.getIban());
						break;
					case 5:
						//LOG.info("Cuenta 5 " + accountDTO.getStatus() + " " + accountDTO.getDivisa());
						peticionInformacion.setCuenta5(accountDTO.getAccountNum());
						peticionInformacion.setIban5(accountDTO.getIban());
						break;
					case 6:
						//LOG.info("Cuenta 6 " + accountDTO.getStatus() + " " + accountDTO.getDivisa());
						peticionInformacion.setCuenta6(accountDTO.getAccountNum());
						peticionInformacion.setIban6(accountDTO.getIban());
						break;
					default:
				}
				i++;
			}
		}
	}
	@Mappings({
			@Mapping(target = "fase", constant = ""+EmbargosConstants.COD_FASE_2),
			@Mapping(target = "codigoINEOrganismoEmisor", source="codigoINE"),
	})
	public abstract CabeceraEmisorFase2 generateCabeceraEmisorFase2(CabeceraEmisorFase1 cabeceraEmisorFase1, Date fechaObtencionFicheroEntidadDeDeposito, String codigoINE);


	// Se hacen los defaults a 0 de los IBAN y las claves de seguridad. No se puede hacer en beanio
	// para no afectar a las cuentas de fase 1-2 que no son del banco - deben permanecer como espacios en blanco

	@Mappings({
			@Mapping(source = "solicitudInformacionFase1.codigoRegistro", target = "codigoRegistro"),
			@Mapping(source = "solicitudInformacionFase1.nifDeudor", target = "nifDeudor"),
			@Mapping(source = "solicitudInformacionFase1.nombreDeudor", target = "nombreDeudor"),
			@Mapping(source = "solicitudInformacionFase1.domicilioDeudor", target = "domicilioDeudor"),
			@Mapping(source = "solicitudInformacionFase1.municipio", target = "municipio"),
			@Mapping(source = "solicitudInformacionFase1.codigoPostal", target = "codigoPostal"),
			@Mapping(source = "solicitudInformacionFase1.identificadorDeuda", target = "identificadorDeuda"),
			@Mapping(source = "solicitudInformacionFase1.codigoDeuda", target = "codigoDeuda"),
			@Mapping(source = "peticionInformacion.iban1", target = "ibanCuenta1", defaultValue = "000000000000000000000000"),
			@Mapping(source = "peticionInformacion.iban2", target = "ibanCuenta2", defaultValue = "000000000000000000000000"),
			@Mapping(source = "peticionInformacion.iban3", target = "ibanCuenta3", defaultValue = "000000000000000000000000"),
			@Mapping(source = "peticionInformacion.iban4", target = "ibanCuenta4", defaultValue = "000000000000000000000000"),
			@Mapping(source = "peticionInformacion.iban5", target = "ibanCuenta5", defaultValue = "000000000000000000000000"),
			@Mapping(source = "peticionInformacion.iban6", target = "ibanCuenta6", defaultValue = "000000000000000000000000"),
			@Mapping(source = "peticionInformacion.claveSeguridad1", target = "claveSeguridadIban1", defaultValue = "000000000000"),
			@Mapping(source = "peticionInformacion.claveSeguridad2", target = "claveSeguridadIban2", defaultValue = "000000000000"),
			@Mapping(source = "peticionInformacion.claveSeguridad3", target = "claveSeguridadIban3", defaultValue = "000000000000"),
			@Mapping(source = "peticionInformacion.claveSeguridad4", target = "claveSeguridadIban4", defaultValue = "000000000000"),
			@Mapping(source = "peticionInformacion.claveSeguridad5", target = "claveSeguridadIban5", defaultValue = "000000000000"),
			@Mapping(source = "peticionInformacion.claveSeguridad6", target = "claveSeguridadIban6", defaultValue = "000000000000")
	})
	public abstract RespuestaSolicitudInformacionFase2 generateRespuestaSolicitudInformacionFase2_cliente(SolicitudInformacionFase1 solicitudInformacionFase1,
																								  PeticionInformacion peticionInformacion);

	@Mappings({
			@Mapping(source = "solicitudInformacionFase1.codigoRegistro", target = "codigoRegistro"),
			@Mapping(source = "solicitudInformacionFase1.nifDeudor", target = "nifDeudor"),
			@Mapping(source = "solicitudInformacionFase1.nombreDeudor", target = "nombreDeudor"),
			@Mapping(source = "solicitudInformacionFase1.domicilioDeudor", target = "domicilioDeudor"),
			@Mapping(source = "solicitudInformacionFase1.municipio", target = "municipio"),
			@Mapping(source = "solicitudInformacionFase1.codigoPostal", target = "codigoPostal"),
			@Mapping(source = "solicitudInformacionFase1.identificadorDeuda", target = "identificadorDeuda"),
			@Mapping(source = "solicitudInformacionFase1.codigoDeuda", target = "codigoDeuda"),
			@Mapping(source = "peticionInformacion.iban1", target = "ibanCuenta1"),
			@Mapping(source = "peticionInformacion.iban2", target = "ibanCuenta2"),
			@Mapping(source = "peticionInformacion.iban3", target = "ibanCuenta3"),
			@Mapping(source = "peticionInformacion.iban4", target = "ibanCuenta4"),
			@Mapping(source = "peticionInformacion.iban5", target = "ibanCuenta5"),
			@Mapping(source = "peticionInformacion.iban6", target = "ibanCuenta6"),
			@Mapping(source = "peticionInformacion.claveSeguridad1", target = "claveSeguridadIban1"),
			@Mapping(source = "peticionInformacion.claveSeguridad2", target = "claveSeguridadIban2"),
			@Mapping(source = "peticionInformacion.claveSeguridad3", target = "claveSeguridadIban3"),
			@Mapping(source = "peticionInformacion.claveSeguridad4", target = "claveSeguridadIban4"),
			@Mapping(source = "peticionInformacion.claveSeguridad5", target = "claveSeguridadIban5"),
			@Mapping(source = "peticionInformacion.claveSeguridad6", target = "claveSeguridadIban6")
	})
	public abstract RespuestaSolicitudInformacionFase2 generateRespuestaSolicitudInformacionFase2_noCliente(SolicitudInformacionFase1 solicitudInformacionFase1,
																								  PeticionInformacion peticionInformacion);

	@Mappings({
			@Mapping(target = "codigoINEOrganismoEmisor", source="codigoINE"),
	})
	public abstract FinFicheroFase2 generateFinFicheroFase2(FinFicheroFase1 finFicheroFase1, String codigoINE);
	
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
	public abstract Embargo generateEmbargo(OrdenEjecucionEmbargoFase3 ordenEjecucionEmbargo, 
			OrdenEjecucionEmbargoComplementarioFase3 ordenEjecucionEmbargoComp, Long codControlFicheroEmbargo, EntidadesOrdenante entidadOrdenante, String razonSocialInterna, BigDecimal fechaLimiteTraba,  Map<String, AccountDTO> customerAccountsMap);
	
	@AfterMapping
	public void generateEmbargoAfterMapping(@MappingTarget Embargo embargo, OrdenEjecucionEmbargoFase3 ordenEjecucionEmbargo, 
			OrdenEjecucionEmbargoComplementarioFase3 ordenEjecucionEmbargoComp,  Map<String, AccountDTO> customerAccountsMap) {
		
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
		CuentaEmbargo cuentaEmbargo = null;
		
		if (ordenEjecucionEmbargo.getIbanCuenta1()!=null && !ordenEjecucionEmbargo.getIbanCuenta1().isEmpty()) {
			
			AccountDTO accountDTO = customerAccountsMap.get(ordenEjecucionEmbargo.getIbanCuenta1());

			String claveSeguridadIban1 = ordenEjecucionEmbargo.getClaveSeguridadIban1();
			
			cuentaEmbargo = setCuentaEmbargoFromAccountDTO(accountDTO, embargo, numeroOrden, claveSeguridadIban1, fechaUltmaModif, usuarioModif, ordenEjecucionEmbargo.getIbanCuenta1());
			
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
			
			cuentaEmbargosList.add(cuentaEmbargo);
		}

		//Datos iban2:
		if (ordenEjecucionEmbargo.getIbanCuenta2()!=null && !ordenEjecucionEmbargo.getIbanCuenta2().isEmpty()) {

			AccountDTO accountDTO = customerAccountsMap.get(ordenEjecucionEmbargo.getIbanCuenta2());

			String claveSeguridadIban2 = ordenEjecucionEmbargo.getClaveSeguridadIban2();
			
			cuentaEmbargo = setCuentaEmbargoFromAccountDTO(accountDTO, embargo, numeroOrden, claveSeguridadIban2, fechaUltmaModif, usuarioModif, ordenEjecucionEmbargo.getIbanCuenta2());
			
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
			
			cuentaEmbargosList.add(cuentaEmbargo);
		}
		
		//Datos iban3:
		if (ordenEjecucionEmbargo.getIbanCuenta3()!=null && !ordenEjecucionEmbargo.getIbanCuenta3().isEmpty()) {

			AccountDTO accountDTO = customerAccountsMap.get(ordenEjecucionEmbargo.getIbanCuenta3());

			String claveSeguridadIban3 = ordenEjecucionEmbargo.getClaveSeguridadIban3();
			
			cuentaEmbargo = setCuentaEmbargoFromAccountDTO(accountDTO, embargo, numeroOrden, claveSeguridadIban3, fechaUltmaModif, usuarioModif, ordenEjecucionEmbargo.getIbanCuenta3());
			
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
			
			cuentaEmbargosList.add(cuentaEmbargo);
		}
		
		//Datos iban4:
		if (ordenEjecucionEmbargo.getIbanCuenta4()!=null && !ordenEjecucionEmbargo.getIbanCuenta4().isEmpty()) {

			AccountDTO accountDTO = customerAccountsMap.get(ordenEjecucionEmbargo.getIbanCuenta4());

			String claveSeguridadIban4 = ordenEjecucionEmbargo.getClaveSeguridadIban4();
			
			cuentaEmbargo = setCuentaEmbargoFromAccountDTO(accountDTO, embargo, numeroOrden, claveSeguridadIban4, fechaUltmaModif, usuarioModif, ordenEjecucionEmbargo.getIbanCuenta4());
			
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
			
			cuentaEmbargosList.add(cuentaEmbargo);
		}
		
		//Datos iban5:
		if (ordenEjecucionEmbargo.getIbanCuenta5()!=null && !ordenEjecucionEmbargo.getIbanCuenta5().isEmpty()) {

			AccountDTO accountDTO = customerAccountsMap.get(ordenEjecucionEmbargo.getIbanCuenta5());

			String claveSeguridadIban5 = ordenEjecucionEmbargo.getClaveSeguridadIban5();
			
			cuentaEmbargo = setCuentaEmbargoFromAccountDTO(accountDTO, embargo, numeroOrden, claveSeguridadIban5, fechaUltmaModif, usuarioModif, ordenEjecucionEmbargo.getIbanCuenta5());
			
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));
			
			cuentaEmbargosList.add(cuentaEmbargo);
		}
		
		//Datos iban6:
		if (ordenEjecucionEmbargo.getIbanCuenta6()!=null && !ordenEjecucionEmbargo.getIbanCuenta6().isEmpty()) {

			AccountDTO accountDTO = customerAccountsMap.get(ordenEjecucionEmbargo.getIbanCuenta6());

			String claveSeguridadIban6 = ordenEjecucionEmbargo.getClaveSeguridadIban6();
			
			cuentaEmbargo = setCuentaEmbargoFromAccountDTO(accountDTO, embargo, numeroOrden, claveSeguridadIban6, fechaUltmaModif, usuarioModif, ordenEjecucionEmbargo.getIbanCuenta6());
			
			cuentaEmbargosList.add(cuentaEmbargo);
		}
		
		embargo.setCuentaEmbargos(cuentaEmbargosList);
		
        //Usuario y fecha ultima modificacion:
		embargo.setUsuarioUltModificacion(usuarioModif);
		embargo.setFUltimaModificacion(fechaUltmaModif);
		
	}
	
	
	private CuentaEmbargo setCuentaEmbargoFromAccountDTO(AccountDTO accountDTO, Embargo embargo, 
			BigDecimal numeroOrden, String claveSeguridadIban, BigDecimal fechaUltmaModif, String usuarioModif, String ibanFromOrdenEjecucionEmbargo) {
		
		CuentaEmbargo cuentaEmbargo = new CuentaEmbargo();
		
		if(accountDTO!=null) {
			cuentaEmbargo.setCuenta(accountDTO.getAccountNum());
			cuentaEmbargo.setIban(accountDTO.getIban());
		} else {
			//Sino, si la cuenta no se encuentra en DWH: indicar motivo de cuenta embargo a inexistente
			cuentaEmbargo.setActuacion(EmbargosConstants.CODIGO_ACTUACION_CUENTA_INEXISTENTE_O_CANCELADA_NORMA63);
			//Iban obtenido del fichero de embargos:
			cuentaEmbargo.setIban(ibanFromOrdenEjecucionEmbargo);
		}

		cuentaEmbargo.setClaveSeguridad(claveSeguridadIban);
		cuentaEmbargo.setEmbargo(embargo);
		cuentaEmbargo.setNumeroOrdenCuenta(numeroOrden);

		cuentaEmbargo.setUsuarioUltModificacion(usuarioModif);
		cuentaEmbargo.setFUltimaModificacion(fechaUltmaModif);
		
		return cuentaEmbargo;
	}

	@Mappings({
		@Mapping(source = "fechaLimiteTraba", target = "fechaLimite"),
	})
	public abstract Traba generateTraba(OrdenEjecucionEmbargoFase3 ordenEjecucionEmbargo, OrdenEjecucionEmbargoComplementarioFase3 ordenEjecucionEmbargoComp,
			 Long codControlFicheroEmbargo, EntidadesOrdenante entidadOrdenante, BigDecimal fechaLimiteTraba, Map<String, AccountDTO> customerAccountsMap);



	@AfterMapping
	public void generateTrabaAfterMapping(@MappingTarget Traba traba, OrdenEjecucionEmbargoFase3 ordenEjecucionEmbargo, 
			OrdenEjecucionEmbargoComplementarioFase3 ordenEjecucionEmbargoComp, EntidadesOrdenante entidadOrdenante, Map<String, AccountDTO> customerAccountsMap) {
		
		BigDecimal fechaUltmaModif = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
		String usuarioModif = EmbargosConstants.USER_AUTOMATICO;

		traba.setFechaTraba(ICEDateUtils.dateToBigDecimal(new Date(), ICEDateUtils.FORMAT_yyyyMMdd));
		
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
		
		
		//CUENTAS TRABAS:
		
		List<CuentaTraba> cuentaTrabasList = new ArrayList<>();
		
		BigDecimal numeroOrden = new BigDecimal(1);
		
		//Datos iban1:
		CuentaTraba cuentaTraba = null;
		
		if (ordenEjecucionEmbargo.getIbanCuenta1()!=null && !ordenEjecucionEmbargo.getIbanCuenta1().isEmpty()) {
			
			String iban1FromOrdenEjecEmb = ordenEjecucionEmbargo.getIbanCuenta1();
			
			AccountDTO accountDTO = customerAccountsMap.get(iban1FromOrdenEjecEmb);
			
			cuentaTraba = setCuentaTrabaFromAccountDTO(accountDTO, traba, numeroOrden, iban1FromOrdenEjecEmb, fechaUltmaModif, usuarioModif);
			
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));

			cuentaTrabasList.add(cuentaTraba);
		}

		//Datos iban2:
		if (ordenEjecucionEmbargo.getIbanCuenta2()!=null && !ordenEjecucionEmbargo.getIbanCuenta2().isEmpty()) {

			String iban2FromOrdenEjecEmb = ordenEjecucionEmbargo.getIbanCuenta2();
			
			AccountDTO accountDTO = customerAccountsMap.get(iban2FromOrdenEjecEmb);
			
			cuentaTraba = setCuentaTrabaFromAccountDTO(accountDTO, traba, numeroOrden, iban2FromOrdenEjecEmb, fechaUltmaModif, usuarioModif);
			
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));

			cuentaTrabasList.add(cuentaTraba);
		}
		
		//Datos iban3:
		if (ordenEjecucionEmbargo.getIbanCuenta3()!=null && !ordenEjecucionEmbargo.getIbanCuenta3().isEmpty()) {

			String iban3FromOrdenEjecEmb = ordenEjecucionEmbargo.getIbanCuenta3();
			
			AccountDTO accountDTO = customerAccountsMap.get(iban3FromOrdenEjecEmb);
			
			cuentaTraba = setCuentaTrabaFromAccountDTO(accountDTO, traba, numeroOrden, iban3FromOrdenEjecEmb, fechaUltmaModif, usuarioModif);
			
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));

			cuentaTrabasList.add(cuentaTraba);
		}
		
		//Datos iban4:
		if (ordenEjecucionEmbargo.getIbanCuenta4()!=null && !ordenEjecucionEmbargo.getIbanCuenta4().isEmpty()) {

			String iban4FromOrdenEjecEmb = ordenEjecucionEmbargo.getIbanCuenta4();
			
			AccountDTO accountDTO = customerAccountsMap.get(iban4FromOrdenEjecEmb);
			
			cuentaTraba = setCuentaTrabaFromAccountDTO(accountDTO, traba, numeroOrden, iban4FromOrdenEjecEmb, fechaUltmaModif, usuarioModif);
			
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));

			cuentaTrabasList.add(cuentaTraba);
		}
		
		//Datos iban5:
		if (ordenEjecucionEmbargo.getIbanCuenta5()!=null && !ordenEjecucionEmbargo.getIbanCuenta5().isEmpty()) {

			String iban5FromOrdenEjecEmb = ordenEjecucionEmbargo.getIbanCuenta5();
			
			AccountDTO accountDTO = customerAccountsMap.get(iban5FromOrdenEjecEmb);
			
			cuentaTraba = setCuentaTrabaFromAccountDTO(accountDTO, traba, numeroOrden, iban5FromOrdenEjecEmb, fechaUltmaModif, usuarioModif);
			
			numeroOrden = numeroOrden.add(BigDecimal.valueOf(1));

			cuentaTrabasList.add(cuentaTraba);
		}
		
		//Datos iban6:
		if (ordenEjecucionEmbargo.getIbanCuenta6()!=null && !ordenEjecucionEmbargo.getIbanCuenta6().isEmpty()) {
			
			String iban6FromOrdenEjecEmb = ordenEjecucionEmbargo.getIbanCuenta6();
			
			AccountDTO accountDTO = customerAccountsMap.get(iban6FromOrdenEjecEmb);
			
			cuentaTraba = setCuentaTrabaFromAccountDTO(accountDTO, traba, numeroOrden, iban6FromOrdenEjecEmb, fechaUltmaModif, usuarioModif);

			cuentaTrabasList.add(cuentaTraba);
		}
		
		traba.setCuentaTrabas(cuentaTrabasList);
		
        //Usuario y fecha ultima modificacion:
		traba.setUsuarioUltModificacion(usuarioModif);
		traba.setFUltimaModificacion(fechaUltmaModif);
	}
	
	private CuentaTraba setCuentaTrabaFromAccountDTO(AccountDTO accountDTO, Traba traba,
			BigDecimal numeroOrden, String ibanFromOrdenEjecucionEmbargo,
			BigDecimal fechaUltmaModif, String usuarioModif) {
	
		CuentaTraba cuentaTraba = new CuentaTraba();
		
		if(accountDTO!=null) {
			
			cuentaTraba.setIban(accountDTO.getIban());
			cuentaTraba.setCuenta(accountDTO.getAccountNum());
			cuentaTraba.setDivisa(accountDTO.getDivisa());
			cuentaTraba.setEstadoCuenta(accountDTO.getStatus());
			
			if (EmbargosConstants.BANK_ACCOUNT_STATUS_CANCELLED.equals(accountDTO.getStatus())) {
				//Indicar la actuacion (motivo) de la cuentaTraba a cancelada:
				CuentaTrabaActuacion cuentaTrabaActuacion = new CuentaTrabaActuacion();
				cuentaTrabaActuacion.setCodActuacion(EmbargosConstants.CODIGO_ACTUACION_CUENTA_INEXISTENTE_O_CANCELADA_NORMA63);
				cuentaTraba.setCuentaTrabaActuacion(cuentaTrabaActuacion);
			}
			else {
				//Por defecto informar la actuacion (motivo) de la cuentaTraba a 'Sin actuacion':
				CuentaTrabaActuacion cuentaTrabaActuacion = new CuentaTrabaActuacion();
				cuentaTrabaActuacion.setCodActuacion(EmbargosConstants.CODIGO_ACTUACION_SIN_ACTUACION_NORMA63);
				cuentaTraba.setCuentaTrabaActuacion(cuentaTrabaActuacion);
			}
		
		} else {
			//Sino, si la cuenta no se encuentra en DWH: indicar la actuacion (motivo) de la cuentaTraba a inexistente:
			CuentaTrabaActuacion cuentaTrabaActuacion = new CuentaTrabaActuacion();
			cuentaTrabaActuacion.setCodActuacion(EmbargosConstants.CODIGO_ACTUACION_CUENTA_INEXISTENTE_O_CANCELADA_NORMA63);
			cuentaTraba.setCuentaTrabaActuacion(cuentaTrabaActuacion);
			
			//El estado de la cuenta se setea a no encontrada:
			cuentaTraba.setEstadoCuenta(EmbargosConstants.BANK_ACCOUNT_STATUS_NOTFOUND);

			//Iban obtenido del fichero de embargos:
			cuentaTraba.setIban(ibanFromOrdenEjecucionEmbargo);
		}
		
		
		cuentaTraba.setTraba(traba);
		cuentaTraba.setNumeroOrdenCuenta(numeroOrden);
		EstadoTraba estadoTraba = new EstadoTraba();
		estadoTraba.setCodEstado(EmbargosConstants.COD_ESTADO_TRABA_PENDIENTE);
		cuentaTraba.setEstadoTraba(estadoTraba);
		cuentaTraba.setOrigenEmb(EmbargosConstants.IND_FLAG_YES);
		cuentaTraba.setAgregarATraba(EmbargosConstants.IND_FLAG_YES);
			
		cuentaTraba.setUsuarioUltModificacion(usuarioModif);
		cuentaTraba.setFUltimaModificacion(fechaUltmaModif);
		
		return cuentaTraba;
		
	}
	
	
	public abstract CabeceraEmisorFase4 generateCabeceraEmisorFase4(CabeceraEmisorFase3 cabeceraEmisorFase3);
	
	@AfterMapping
	public void setCabeceraEmisorFase4AfterMapping(@MappingTarget CabeceraEmisorFase4 cabeceraEmisorFase4) {
	
		cabeceraEmisorFase4.setFase(EmbargosConstants.COD_FASE_4);
		cabeceraEmisorFase4.setFechaObtencionFicheroEntidadDeDeposito(cabeceraEmisorFase4.getFechaObtencionFicheroOrganismo());
	}
	
	@Mappings({
		@Mapping(source = "embargo.nif", target = "nifDeudor"),
		@Mapping(source = "embargo.nombre", target = "nombreDeudor"),
		@Mapping(source = "embargo.domicilio", target = "domicilioDeudor"),
		@Mapping(source = "embargo.municipio", target = "municipio"),
		@Mapping(source = "embargo.codigoPostal", target = "codigoPostal"),
		@Mapping(source = "embargo.numeroEmbargo", target = "identificadorDeuda"),
		@Mapping(source = "embargo.importe", target = "importeTotalAEmbargar"),
		@Mapping(source = "embargo.codDeudaDeudor", target = "codigoDeuda"),
		@Mapping(source = "traba.importeTrabado", target = "importeTotalRetencionesEfectuadas"),
	})
	public abstract ComunicacionResultadoRetencionFase4 generateComunicacionResultadoRetencionFase4(Embargo embargo, Traba traba, List<CuentaTraba> cuentaTrabaOrderedList);
	
	@AfterMapping
	public void setComunicacionResultadoRetencionFase4AfterMapping(@MappingTarget ComunicacionResultadoRetencionFase4 comunicacionResultadoRetencionFase4,
			Embargo embargo, Traba traba, List<CuentaTraba> cuentaTrabaOrderedList) {
		
		comunicacionResultadoRetencionFase4.setFechaEjecucionRetenciones(ICEDateUtils.bigDecimalToDate(traba.getFechaTraba(), ICEDateUtils.FORMAT_yyyyMMdd));
		
		//Obligatorio setear el 'codigo de registro' en el registro de Comunicacion del Resultado de la Retencion:
		comunicacionResultadoRetencionFase4.setCodigoRegistro(EmbargosConstants.CODIGO_REGISTRO_CUADERNO63_COMUNICACION_RESULTADO_RETENCION_FASE4);
		
		//TODO: Si en el mapeo el domicilio es nulo, lo generamos con los campos relacionados al domicilio (calle, puerta, etc.)

		//TODO: falta setear --> fechaEjecucionRetenciones
		
		int cont = 1;
		
		for (CuentaTraba cuentaTraba : cuentaTrabaOrderedList) {
			if (EmbargosConstants.IND_FLAG_YES.equals(cuentaTraba.getAgregarATraba())) {

				String claveSeguridad = getCodigoSeguridad(traba, cuentaTraba);
				
				String resultadoRetencion = cuentaTraba.getCuentaTrabaActuacion() != null ? 
						cuentaTraba.getCuentaTrabaActuacion().getCodExternoActuacion() : null;

				switch (cont) {
					case 1:
						comunicacionResultadoRetencionFase4.setIbanCuenta1(cuentaTraba.getIban());
						comunicacionResultadoRetencionFase4.setClaveSeguridadIban1(claveSeguridad);
						comunicacionResultadoRetencionFase4.setImporteRetenidoCuenta1(cuentaTraba.getImporte());
						comunicacionResultadoRetencionFase4.setCodigoResultadoRetencionCuenta1(resultadoRetencion);
						break;
					case 2:
						comunicacionResultadoRetencionFase4.setIbanCuenta2(cuentaTraba.getIban());
						comunicacionResultadoRetencionFase4.setClaveSeguridadIban2(claveSeguridad);
						comunicacionResultadoRetencionFase4.setImporteRetenidoCuenta2(cuentaTraba.getImporte());
						comunicacionResultadoRetencionFase4.setCodigoResultadoRetencionCuenta2(resultadoRetencion);
						break;
					case 3:
						comunicacionResultadoRetencionFase4.setIbanCuenta3(cuentaTraba.getIban());
						comunicacionResultadoRetencionFase4.setClaveSeguridadIban3(claveSeguridad);
						comunicacionResultadoRetencionFase4.setImporteRetenidoCuenta3(cuentaTraba.getImporte());
						comunicacionResultadoRetencionFase4.setCodigoResultadoRetencionCuenta3(resultadoRetencion);
						break;
					case 4:
						comunicacionResultadoRetencionFase4.setIbanCuenta4(cuentaTraba.getIban());
						comunicacionResultadoRetencionFase4.setClaveSeguridadIban4(claveSeguridad);
						comunicacionResultadoRetencionFase4.setImporteRetenidoCuenta4(cuentaTraba.getImporte());
						comunicacionResultadoRetencionFase4.setCodigoResultadoRetencionCuenta4(resultadoRetencion);
						break;
					case 5:
						comunicacionResultadoRetencionFase4.setIbanCuenta5(cuentaTraba.getIban());
						comunicacionResultadoRetencionFase4.setClaveSeguridadIban5(claveSeguridad);
						comunicacionResultadoRetencionFase4.setImporteRetenidoCuenta5(cuentaTraba.getImporte());
						comunicacionResultadoRetencionFase4.setCodigoResultadoRetencionCuenta5(resultadoRetencion);
						break;
					case 6:
						comunicacionResultadoRetencionFase4.setIbanCuenta6(cuentaTraba.getIban());
						comunicacionResultadoRetencionFase4.setClaveSeguridadIban6(claveSeguridad);
						comunicacionResultadoRetencionFase4.setImporteRetenidoCuenta6(cuentaTraba.getImporte());
						comunicacionResultadoRetencionFase4.setCodigoResultadoRetencionCuenta6(resultadoRetencion);
						break;
					default:
				}

				cont++;
			}
		}
	}

	private String getCodigoSeguridad(Traba traba, CuentaTraba cuentaTraba) {
		String codSeguridad = null;
		Embargo embargo = traba.getEmbargo();
		for (CuentaEmbargo cuentaEmbargo: embargo.getCuentaEmbargos()) {
			if (cuentaTraba.getCuenta() != null && cuentaTraba.getCuenta().equals(cuentaEmbargo.getCuenta())) {
				if (cuentaEmbargo.getClaveSeguridad() != null) {
					codSeguridad = cuentaEmbargo.getClaveSeguridad();
				}
			}
		}
		if (codSeguridad == null) {
			codSeguridad = EmbargosUtils.generateClaveSeguridad(cuentaTraba.getIban());
		}

		return codSeguridad;
	}
	
	@Mappings({
		@Mapping(source = "numeroRegistrosFichero", target = "numeroRegistrosFichero"),
		@Mapping(source = "importeTotalTrabado", target = "importeTotalRetenido")
	})
	public abstract FinFicheroFase4 generateFinFicheroFase4(FinFicheroFase3 finFicheroFase3, Integer numeroRegistrosFichero, BigDecimal importeTotalTrabado);
	
	@AfterMapping
	public void setFinFicheroFase4AfterMapping(@MappingTarget FinFicheroFase4 finFicheroFase4) {
	
		//TODO: eliminar este metodo si queda finalmente vacio.
	}

	
	@Mappings({ @Mapping(source = "codControlFichero", target = "controlFichero.codControlFichero"),
	})
	public abstract LevantamientoTraba generateLevantamiento(Long codControlFichero,
			OrdenLevantamientoRetencionFase5 ordenLevantamientoRetencionFase5, Traba traba, CustomerDTO DWHCustomer);

	@AfterMapping
	public void generateLevantamientoAfterMapping(@MappingTarget LevantamientoTraba levantamiento,
			OrdenLevantamientoRetencionFase5 ordenLevantamientoRetencionFase5, Traba traba, CustomerDTO DWHCustomer) {
		BigDecimal fechaUltmaModif = ICEDateUtils.actualDateToBigDecimal(ICEDateUtils.FORMAT_yyyyMMddHHmmss);
		String usuarioModif = EmbargosConstants.USER_AUTOMATICO;

		levantamiento.setUsuarioUltModificacion(usuarioModif);
		levantamiento.setFUltimaModificacion(fechaUltmaModif);
		levantamiento.setTraba(traba);

		levantamiento.setTipoLevantamiento(ordenLevantamientoRetencionFase5.getCodigoTipoLevantamientoARealizar());
		
		EstadoLevantamiento estadoLevantamiento = new EstadoLevantamiento();
		estadoLevantamiento.setCodEstado(EmbargosConstants.COD_ESTADO_LEVANTAMIENTO_PENDIENTE);
		levantamiento.setEstadoLevantamiento(estadoLevantamiento);
		// TODO TO BE DEFINED
		levantamiento.setEstadoEjecutado(BigDecimal.ZERO);
		levantamiento.setEstadoContable(BigDecimal.ZERO);
		levantamiento.setIndCasoRevisado(EmbargosConstants.IND_FLAG_NO);
		// sempre a null levantamiento.setCodDeudaDeudor();

		List<CuentaLevantamiento> cuentas = new ArrayList<>(6);
		levantamiento.setCuentaLevantamientos(cuentas);

		if (ordenLevantamientoRetencionFase5.getIbanCuenta1() != null
				&& !ordenLevantamientoRetencionFase5.getIbanCuenta1().isEmpty()) {
			CuentaLevantamiento cuentaLevantamiento1 = levantamientoHelperMapper.mapCuentaLevantamiento(levantamiento,
					ordenLevantamientoRetencionFase5.getIbanCuenta1(),
					ordenLevantamientoRetencionFase5.getImporteALevantarIban1(), DWHCustomer, traba, usuarioModif,
					fechaUltmaModif, ordenLevantamientoRetencionFase5.getCodigoTipoLevantamientoIban1());
			cuentaLevantamiento1.setNumeroOrdenCuenta(new BigDecimal(1));
			cuentas.add(cuentaLevantamiento1);
		}
		if (ordenLevantamientoRetencionFase5.getIbanCuenta2() != null
				&& !ordenLevantamientoRetencionFase5.getIbanCuenta2().isEmpty()) {
			CuentaLevantamiento cuentaLevantamiento2 = levantamientoHelperMapper.mapCuentaLevantamiento(levantamiento,
					ordenLevantamientoRetencionFase5.getIbanCuenta2(),
					ordenLevantamientoRetencionFase5.getImporteALevantarIban2(), DWHCustomer, traba, usuarioModif,
					fechaUltmaModif, ordenLevantamientoRetencionFase5.getCodigoTipoLevantamientoIban2());
			cuentaLevantamiento2.setNumeroOrdenCuenta(new BigDecimal(2));
			cuentas.add(cuentaLevantamiento2);
		}
		if (ordenLevantamientoRetencionFase5.getIbanCuenta3() != null
				&& !ordenLevantamientoRetencionFase5.getIbanCuenta3().isEmpty()) {
			CuentaLevantamiento cuentaLevantamiento3 = levantamientoHelperMapper.mapCuentaLevantamiento(levantamiento,
					ordenLevantamientoRetencionFase5.getIbanCuenta3(),
					ordenLevantamientoRetencionFase5.getImporteALevantarIban3(), DWHCustomer, traba, usuarioModif,
					fechaUltmaModif, ordenLevantamientoRetencionFase5.getCodigoTipoLevantamientoIban3());
			cuentaLevantamiento3.setNumeroOrdenCuenta(new BigDecimal(3));
			cuentas.add(cuentaLevantamiento3);
		}
		if (ordenLevantamientoRetencionFase5.getIbanCuenta4() != null
				&& !ordenLevantamientoRetencionFase5.getIbanCuenta4().isEmpty()) {
			CuentaLevantamiento cuentaLevantamiento4 = levantamientoHelperMapper.mapCuentaLevantamiento(levantamiento,
					ordenLevantamientoRetencionFase5.getIbanCuenta4(),
					ordenLevantamientoRetencionFase5.getImporteALevantarIban4(), DWHCustomer, traba, usuarioModif,
					fechaUltmaModif, ordenLevantamientoRetencionFase5.getCodigoTipoLevantamientoIban4());
			cuentaLevantamiento4.setNumeroOrdenCuenta(new BigDecimal(4));
			cuentas.add(cuentaLevantamiento4);
		}
		if (ordenLevantamientoRetencionFase5.getIbanCuenta5() != null
				&& !ordenLevantamientoRetencionFase5.getIbanCuenta5().isEmpty()) {
			CuentaLevantamiento cuentaLevantamiento5 = levantamientoHelperMapper.mapCuentaLevantamiento(levantamiento,
					ordenLevantamientoRetencionFase5.getIbanCuenta5(),
					ordenLevantamientoRetencionFase5.getImporteALevantarIban5(), DWHCustomer, traba, usuarioModif,
					fechaUltmaModif, ordenLevantamientoRetencionFase5.getCodigoTipoLevantamientoIban5());
			cuentaLevantamiento5.setNumeroOrdenCuenta(new BigDecimal(5));
			cuentas.add(cuentaLevantamiento5);
		}
		if (ordenLevantamientoRetencionFase5.getIbanCuenta6() != null
				&& !ordenLevantamientoRetencionFase5.getIbanCuenta6().isEmpty()) {
			CuentaLevantamiento cuentaLevantamiento6 = levantamientoHelperMapper.mapCuentaLevantamiento(levantamiento,
					ordenLevantamientoRetencionFase5.getIbanCuenta6(),
					ordenLevantamientoRetencionFase5.getImporteALevantarIban6(), DWHCustomer, traba, usuarioModif,
					fechaUltmaModif, ordenLevantamientoRetencionFase5.getCodigoTipoLevantamientoIban6());
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

	@Mappings({
			@Mapping(target = "fase", constant = ""+EmbargosConstants.COD_FASE_6),
			@Mapping(target = "fechaAbonoAlOrganismo", source = "fechaObtencionFicheroOrganismo"), // Nota: este mapeo se saca de 3 ejemplos remitidos por commerz
	})
	public abstract CabeceraEmisorFase6 generateCabeceraEmisorFase6(CabeceraEmisorFase3 cabeceraEmisorFase3);

	@Mappings({
			@Mapping(source = "numeroRegistrosFichero", target = "numeroRegistrosFichero"),
			@Mapping(source = "importeTotalALevantar", target = "importeTotalALevantar"),
			@Mapping(source = "importeNuevoTotalRetenido", target = "importeNuevoTotalRetenido")
	})
	public abstract FinFicheroFase6 generateFinFicheroFase6(FinFicheroFase3 finFicheroFase6, Integer numeroRegistrosFichero,
															BigDecimal importeTotalALevantar, BigDecimal importeNuevoTotalRetenido);
/*
	@AfterMapping
	public void setFinFicheroFase4AfterMapping(@MappingTarget FinFicheroFase4 finFicheroFase4) {

		//TODO: eliminar este metodo si queda finalmente vacio.
	}
	*/

	@Mappings({
			@Mapping(target = "codigoRegistro", constant = "6"),
			@Mapping(target = "nifDeudor", source="ordenEjecucionEmbargoFase3.nifDeudor"),
			@Mapping(target = "nombreDeudor", source="ordenEjecucionEmbargoFase3.nombreDeudor"),
			@Mapping(target = "domicilioDeudor", source="ordenEjecucionEmbargoFase3.domicilioDeudor"),
			@Mapping(target = "municipio", source="ordenEjecucionEmbargoFase3.municipio"),
			@Mapping(target = "codigoPostal", source="ordenEjecucionEmbargoFase3.codigoPostal"),
			@Mapping(target = "identificadorDeuda", source="ordenEjecucionEmbargoFase3.identificadorDeuda"),
			@Mapping(target = "importeTotalAEmbargar", source="ordenEjecucionEmbargoFase3.importeTotalAEmbargar"),
			@Mapping(target = "codigoDeuda", source="ordenEjecucionEmbargoFase3.codigoDeuda"),
			@Mapping(target = "importeTotalRetencionesEfectuadas", source = "traba.importeTrabado"),
			@Mapping(target = "totalImporteALevantar", source = "resultadoEmbargo.totalLevantado"),
			@Mapping(target = "importeTotalNetoEmbargadoAlDeudor", source = "resultadoEmbargo.totalNeto"),
			// tomar los siguientes como defaults, se sobreescriben en el after mapping cuando hay valor
			@Mapping(target = "ibanCuenta1", constant = "000000000000000000000000"),
			@Mapping(target = "codigoResultadoRetencionIban1", constant = "00"),
			@Mapping(target = "ibanCuenta2", constant = "000000000000000000000000"),
			@Mapping(target = "codigoResultadoRetencionIban2", constant = "00"),
			@Mapping(target = "ibanCuenta3", constant = "000000000000000000000000"),
			@Mapping(target = "codigoResultadoRetencionIban3", constant = "00"),
			@Mapping(target = "ibanCuenta4", constant = "000000000000000000000000"),
			@Mapping(target = "codigoResultadoRetencionIban4", constant = "00"),
			@Mapping(target = "ibanCuenta5", constant = "000000000000000000000000"),
			@Mapping(target = "codigoResultadoRetencionIban5", constant = "00"),
			@Mapping(target = "ibanCuenta6", constant = "000000000000000000000000"),
			@Mapping(target = "codigoResultadoRetencionIban6", constant = "00"),
			@Mapping(target = "codigoResultadoLevantamiento", constant = "0"),
			@Mapping(target = "resultadoLevantamientoIban1", constant = "0"),
			@Mapping(target = "resultadoLevantamientoIban2", constant = "0"),
			@Mapping(target = "resultadoLevantamientoIban3", constant = "0"),
			@Mapping(target = "resultadoLevantamientoIban4", constant = "0"),
			@Mapping(target = "resultadoLevantamientoIban5", constant = "0"),
			@Mapping(target = "resultadoLevantamientoIban6", constant = "0"),
	})
	public abstract ResultadoFinalEmbargoFase6 generateResultadoFinalEmbargoFase6(
			OrdenEjecucionEmbargoFase3 ordenEjecucionEmbargoFase3,
			OrdenEjecucionEmbargoComplementarioFase3 ordenEjecucionEmbargoComplementarioFase3,
			Embargo embargo, Traba traba, List<LevantamientoTraba> levantamientosList, ResultadoEmbargo resultadoEmbargo);

	@AfterMapping
	public void setResultadoFinalEmbargoFase6AfterMapping(
			@MappingTarget ResultadoFinalEmbargoFase6 resultadoFinalEmbargoFase6,
			OrdenEjecucionEmbargoFase3 ordenEjecucionEmbargoFase3,
			OrdenEjecucionEmbargoComplementarioFase3 ordenEjecucionEmbargoComplementarioFase3,
			Embargo embargo, Traba traba, List<LevantamientoTraba> levantamientosList, ResultadoEmbargo resultadoEmbargo)
	{
		// Copia de cuentas basado en setComunicacionResultadoRetencionFase4AfterMapping

		resultadoFinalEmbargoFase6.setFechaEjecucionRetenciones(ICEDateUtils.bigDecimalToDate(traba.getFechaTraba(), ICEDateUtils.FORMAT_yyyyMMdd));

		int cont = 1;

		List<CuentaTraba> cuentaTrabaOrderedList = new ArrayList(traba.getCuentaTrabas());
		Collections.sort(cuentaTrabaOrderedList, Comparator.comparing(CuentaTraba::getNumeroOrdenCuenta));

		for (CuentaTraba cuentaTraba : cuentaTrabaOrderedList) {
			if (EmbargosConstants.IND_FLAG_YES.equals(cuentaTraba.getAgregarATraba())) {

				String claveSeguridad = getCodigoSeguridad(traba, cuentaTraba);

				String resultadoRetencion = cuentaTraba.getCuentaTrabaActuacion() != null ?
						cuentaTraba.getCuentaTrabaActuacion().getCodExternoActuacion() : null;

				switch (cont) {
					case 1:
						resultadoFinalEmbargoFase6.setIbanCuenta1(cuentaTraba.getIban());
						resultadoFinalEmbargoFase6.setClaveSeguridadIban1(claveSeguridad);
						resultadoFinalEmbargoFase6.setImporteRetenidoIban1(cuentaTraba.getImporte());
						resultadoFinalEmbargoFase6.setCodigoResultadoRetencionIban1(resultadoRetencion);
						resultadoFinalEmbargoFase6.setResultadoLevantamientoIban1(getResultadoLevantamiento(levantamientosList, cuentaTraba.getCuenta()));
						resultadoFinalEmbargoFase6.setImporteNetoEmbargadoIban1(getResultadoLevantamiento(resultadoEmbargo, cuentaTraba));
						break;
					case 2:
						resultadoFinalEmbargoFase6.setIbanCuenta2(cuentaTraba.getIban());
						resultadoFinalEmbargoFase6.setClaveSeguridadIban2(claveSeguridad);
						resultadoFinalEmbargoFase6.setImporteRetenidoIban2(cuentaTraba.getImporte());
						resultadoFinalEmbargoFase6.setCodigoResultadoRetencionIban2(resultadoRetencion);
						resultadoFinalEmbargoFase6.setResultadoLevantamientoIban2(getResultadoLevantamiento(levantamientosList, cuentaTraba.getCuenta()));
						resultadoFinalEmbargoFase6.setImporteNetoEmbargadoIban2(getResultadoLevantamiento(resultadoEmbargo, cuentaTraba));
						break;
					case 3:
						resultadoFinalEmbargoFase6.setIbanCuenta3(cuentaTraba.getIban());
						resultadoFinalEmbargoFase6.setClaveSeguridadIban3(claveSeguridad);
						resultadoFinalEmbargoFase6.setImporteRetenidoIban3(cuentaTraba.getImporte());
						resultadoFinalEmbargoFase6.setCodigoResultadoRetencionIban3(resultadoRetencion);
						resultadoFinalEmbargoFase6.setResultadoLevantamientoIban3(getResultadoLevantamiento(levantamientosList, cuentaTraba.getCuenta()));
						resultadoFinalEmbargoFase6.setImporteNetoEmbargadoIban3(getResultadoLevantamiento(resultadoEmbargo, cuentaTraba));
						break;
					case 4:
						resultadoFinalEmbargoFase6.setIbanCuenta4(cuentaTraba.getIban());
						resultadoFinalEmbargoFase6.setClaveSeguridadIban4(claveSeguridad);
						resultadoFinalEmbargoFase6.setImporteRetenidoIban4(cuentaTraba.getImporte());
						resultadoFinalEmbargoFase6.setCodigoResultadoRetencionIban4(resultadoRetencion);
						resultadoFinalEmbargoFase6.setResultadoLevantamientoIban4(getResultadoLevantamiento(levantamientosList, cuentaTraba.getCuenta()));
						resultadoFinalEmbargoFase6.setImporteNetoEmbargadoIban4(getResultadoLevantamiento(resultadoEmbargo, cuentaTraba));
						break;
					case 5:
						resultadoFinalEmbargoFase6.setIbanCuenta5(cuentaTraba.getIban());
						resultadoFinalEmbargoFase6.setClaveSeguridadIban5(claveSeguridad);
						resultadoFinalEmbargoFase6.setImporteRetenidoIban5(cuentaTraba.getImporte());
						resultadoFinalEmbargoFase6.setCodigoResultadoRetencionIban5(resultadoRetencion);
						resultadoFinalEmbargoFase6.setResultadoLevantamientoIban5(getResultadoLevantamiento(levantamientosList, cuentaTraba.getCuenta()));
						resultadoFinalEmbargoFase6.setImporteNetoEmbargadoIban5(getResultadoLevantamiento(resultadoEmbargo, cuentaTraba));
						break;
					case 6:
						resultadoFinalEmbargoFase6.setIbanCuenta6(cuentaTraba.getIban());
						resultadoFinalEmbargoFase6.setClaveSeguridadIban6(claveSeguridad);
						resultadoFinalEmbargoFase6.setImporteRetenidoIban6(cuentaTraba.getImporte());
						resultadoFinalEmbargoFase6.setCodigoResultadoRetencionIban6(resultadoRetencion);
						resultadoFinalEmbargoFase6.setResultadoLevantamientoIban6(getResultadoLevantamiento(levantamientosList, cuentaTraba.getCuenta()));
						resultadoFinalEmbargoFase6.setImporteNetoEmbargadoIban6(getResultadoLevantamiento(resultadoEmbargo, cuentaTraba));
						break;
					default:
				}

				cont++;
			}
		}

		if (levantamientosList.size() > 0)
			resultadoFinalEmbargoFase6.setCodigoResultadoLevantamiento(levantamientosList.get(0).getTipoLevantamiento());
	}

	private String getResultadoLevantamiento(List<LevantamientoTraba> levantamientosList, String cuenta) {
		CuentaLevantamiento cuentaLevantamiento = null;

		for (LevantamientoTraba levantamientoTraba : levantamientosList) {
			for (CuentaLevantamiento cuentaLevantamientoActual : levantamientoTraba.getCuentaLevantamientos()) {
				if (cuenta.equals(cuentaLevantamientoActual.getCuenta())) {
					cuentaLevantamiento = cuentaLevantamientoActual;
					break;
				}
			}
		}

		if (cuentaLevantamiento == null) return "0";

		return cuentaLevantamiento.getActuacion();
	}

	private BigDecimal getResultadoLevantamiento(ResultadoEmbargo resultadoEmbargo, CuentaTraba cuentaTraba) {
		CuentaResultadoEmbargo cuentaResultadoEmbargo = null;

		for (CuentaResultadoEmbargo cuentaResultadoEmbargoActual : resultadoEmbargo.getCuentaResultadoEmbargos()) {
			if (cuentaResultadoEmbargoActual.getCuentaTraba() != null) {
				if (cuentaTraba.getCodCuentaTraba() == (cuentaResultadoEmbargoActual.getCuentaTraba().getCodCuentaTraba())) {
					cuentaResultadoEmbargo = cuentaResultadoEmbargoActual;
					break;
				}
			}
		}

		if (cuentaResultadoEmbargo == null) return null;

		return cuentaResultadoEmbargo.getImporteNeto();
	}
}
